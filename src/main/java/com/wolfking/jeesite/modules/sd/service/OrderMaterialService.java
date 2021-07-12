/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.MaterialService;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.modules.sd.dao.OrderMaterialDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.material.service.B2BMaterialExecutor;
import com.wolfking.jeesite.ms.material.service.B2BMaterialExecutorFactory;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wolfking.jeesite.modules.sd.utils.OrderUtils.ORDER_LOCK_EXPIRED;

/**
 * 订单配件管理服务
 * 包含配件单及跟踪进度
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderMaterialService extends LongIDBaseService {


    @Autowired
    private OrderMaterialDao dao;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private OrderMaterialReturnService returnService;

    //b2b配件开关
    @Autowired
    private MicroServicesProperties msProperties;

    @Autowired
    private B2BMaterialExecutorFactory b2bMaterialExecutorFactory;


    //region 配件申请单

    /**
     * 按订单id返回配件申请记录（包含配件及图片）
     * 产品及配件需重缓存单独读取
     */
    public List<MaterialMaster> findMaterialMastersByOrderIdMS(Long orderId, String quarter) {
        List<MaterialMaster> list = dao.findMaterialMastersByOrderId(orderId, quarter);
//        List<MaterialMaster> list = dao.findMaterialMastersNoAttachByOrderId(orderId, quarter);
        if (list.size() > 0) {
            Map<String, Dict> expressTypeMap = MSDictUtils.getDictMap("express_type");
            //Map<String, Dict> materialTypeMap = MSDictUtils.getDictMap("MaterialType");
            Map<String, Dict> statusMap = MSDictUtils.getDictMap("material_apply_status");
            Map<String, Dict> applyTypeMap = MSDictUtils.getDictMap("material_apply_type");
            Map<Long, Product> productMap = Maps.newHashMap();
            Map<Long, Material> materialMap = Maps.newHashMap();
            Product product;
            //user微服务
            List<Long> userIds = list.stream().filter(i -> i.getCreateBy() != null && i.getCreateBy().getId() != null)
                    .map(i -> i.getCreateBy().getId()).distinct().collect(Collectors.toList());
            Map<Long, String> nameMap = MSUserUtils.getNamesByUserIds(userIds);
            for (MaterialMaster item : list) {
                if (item.getExpressCompany() != null && item.getExpressCompany().getValue() != null) {
                    Dict expressTypeDict = expressTypeMap.get(item.getExpressCompany().getValue());
                    item.getExpressCompany().setLabel(expressTypeDict != null ? expressTypeDict.getLabel() : "");
                }
                //if (item.getMaterialType() != null && StringUtils.toInteger(item.getMaterialType().getValue()) > 0) {
                //    Dict materialTypeDict = materialTypeMap.get(item.getMaterialType().getValue());
                //    item.getMaterialType().setLabel(materialTypeDict != null ? materialTypeDict.getLabel() : "");
                //}
                if (item.getStatus() != null && StringUtils.toInteger(item.getStatus().getValue()) > 0) {
                    Dict statusDict = statusMap.get(item.getStatus().getValue());
                    item.getStatus().setLabel(statusDict != null ? statusDict.getLabel() : "");
                }
                if (item.getApplyType() != null && StringUtils.toInteger(item.getApplyType().getValue()) > 0) {
                    Dict appTypeDict = applyTypeMap.get(item.getApplyType().getValue());
                    item.getApplyType().setLabel(appTypeDict != null ? appTypeDict.getLabel() : "");
                }
                if (item.getCreateBy() != null && item.getCreateBy().getId() != null) {
                    item.getCreateBy().setName(StringUtils.toString(nameMap.get(item.getCreateBy().getId())));
                }
                product = productMap.get(item.getProduct().getId());
                if (product == null) {
                    product = productService.getProductByIdFromCache(item.getProduct().getId());
                    if (product != null) {
                        item.setProduct(product);
                        productMap.put(product.getId(), product);
                    }
                } else {
                    item.setProduct(product);
                }
                //items
                loadItemMaterials(item.getItems(), materialMap, false);
            }
        }
        return list;
    }

    /**
     * 按订单返回配件申请单单头记录（不含配件和图片）
     */
    public List<MaterialMaster> findMaterialMasterHeadsByOrderId(Long orderId, String quarter) {
        return dao.findMaterialMasterHeadsByOrderId(orderId, quarter);
    }

    /**
     * 按id获得配件申请单单头信息
     *
     * @param id      申请单id
     * @param quarter 分片
     */
    public MaterialMaster getMaterialMasterHeadById(Long id, String quarter) {
        MaterialMaster master = dao.getMaterialMasterHeadById(id, quarter);
        if (master != null) {
            Dict status = MSDictUtils.getDictByValue(master.getStatus().getValue(), "material_apply_status");//切换为微服务
            if (status != null) {
                master.setStatus(status);
            }
            Product product = productService.getProductByIdFromCache(master.getProduct().getId());
            if (product != null) {
                master.setProduct(product);
            }
        }
        return master;
    }

    /**
     * 保存APP配件申请
     * 一次提交只产生一个配件单
     */
    @Transactional
    public void addAppMaterialApplies(Order order, List<MaterialMaster> materialMasters) {
        if (order == null || order.getOrderCondition() == null || materialMasters == null || materialMasters.size() == 0) {
            return;
        }
        Long orderId = order.getId();

        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, orderId);
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新页面。");
        }
        String quarter = order.getQuarter();
        User user = null;
        Date date = null;
        try {
            MaterialMaster materialMaster;
            MaterialItem item;
            List<MaterialItem> items;
            Set<String> materialNames = Sets.newHashSet();
            for (int i = 0, size = materialMasters.size(); i < size; i++) {
                materialMaster = materialMasters.get(i);
                if (user == null) {
                    user = materialMaster.getCreateBy();
                    date = materialMaster.getCreateDate();
                }
                dao.insertMaterialMaster(materialMaster);
                //product
                for (MaterialProduct mProduct : materialMaster.getProductInfos()) {
                    dao.insertMaterialProduct(mProduct);
                }
                //item
                items = materialMaster.getItems();
                Long itemId;
                for (int j = 0, jsize = items.size(); j < jsize; j++) {
                    item = items.get(j);
                    materialNames.add(item.getMaterial().getName());
                    dao.insertMaterialItem(item);
                }
                //attachment
                if (materialMaster.getAttachments() != null && materialMaster.getAttachments().size() > 0) {
                    List<MaterialAttachment> attachments = materialMaster.getAttachments();
                    MaterialAttachment attachment;
                    Long attcId;
                    for (int k = 0, ksize = attachments.size(); k < ksize; k++) {
                        attachment = attachments.get(k);
                        dao.insertMaterialAttach(attachment);
                        //关系表
                        dao.insertMaterialMasterAttachMap(materialMaster.getId(), attachment.getId(), materialMaster.getQuarter());
                    }
                }
            }
            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(quarter);//*
            processLog.setAction("申请配件");
            processLog.setOrderId(orderId);
            processLog.setActionComment(String.format("安维/客服人员申请配件：%s,请到配件单里进行查看", materialNames.toString()));
            processLog.setStatus(order.getOrderCondition().getStatus().getLabel());
            processLog.setStatusValue(Integer.parseInt(order.getOrderCondition().getStatus().getValue()));
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
//            dao.insertProcessLog(processLog);
            processLog.setCustomerId(order.getOrderCondition().getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            orderService.saveOrderProcessLogNew(processLog);

            HashMap<String, Object> params = Maps.newHashMap();
            //order condition
            params.clear();
            params.put("quarter", quarter);//*
            params.put("orderId", orderId);
            params.put("partsFlag", 1);
            params.put("partsApplyDate", System.currentTimeMillis());// 2019-03-17
            params.put("updateBy", user);
            params.put("updateDate", date);
            orderService.updateOrderCondition(params);
            // 向b2b申请配件单,厂家发货才同步到厂家系统
            materialMaster = materialMasters.get(0);
            /*if (materialMaster.getApplyType().getIntValue() == MaterialMaster.APPLY_TYPE_CHANGJIA) {
                newB2BMateiralForm(order.getDataSource().getIntValue(), materialMaster);
            }*/
            //向b2b申请配件单,厂家发货的配件才调用
            if ( materialMaster.getApplyType().getIntValue() == MaterialMaster.APPLY_TYPE_CHANGJIA &&
                    order.getDataSource().getIntValue() == B2BDataSourceEnum.JOYOUNG.id){ //九阳
                newB2BMateiralForm(order.getDataSource().getIntValue(),materialMaster);
            }else if(order.getDataSource().getIntValue() == B2BDataSourceEnum.XYINGYAN.id || order.getDataSource().getIntValue() == B2BDataSourceEnum.LB.id){ //新迎燕
                materialMaster.setB2bOrderId(order.getB2bOrderId());
                newB2BMateiralForm(order.getDataSource().getIntValue(),materialMaster);
            }
            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(orderId)
                    .setDeleteField(OrderCacheField.CONDITION);
            OrderCacheUtils.update(builder.build());
        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[orderMaterialService.addAppMaterialApplies] orderId:{}", orderId, e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

    /**
     * 向B2B微服务申请创建配件单
     *
     * @param dataSource     数据来源，未定义在application.yml的ms.b2bcenter.material下的不请求
     * @param mateiralMaster 工单系统配件单
     */
    @Transactional
    public void newB2BMateiralForm(Integer dataSource, MaterialMaster mateiralMaster) {
        // b2b配件
        if (msProperties.getB2bcenter().getMaterial().containsKey(dataSource)) {
            B2BDataSourceEnum dataSourceEnum = B2BDataSourceEnum.valueOf(dataSource);
            if (dataSourceEnum != null) {
                B2BMaterialExecutor b2BMaterialExecutor = b2bMaterialExecutorFactory.getExecutor(dataSourceEnum);
                if (b2BMaterialExecutor != null) {
                    StringBuilder address = new StringBuilder(250);
                    address.append(mateiralMaster.getArea().getName());
                    if (mateiralMaster.getSubArea() != null && mateiralMaster.getSubArea().getId() != null && mateiralMaster.getSubArea().getId() > 3) {
                        address.append(" ").append(mateiralMaster.getSubArea().getName());
                    }
                    address.append(" ").append(mateiralMaster.getUserAddress());
                    mateiralMaster.setUserAddress(StringUtils.left(address.toString().trim(), 250));
                    address = null;
                    MSResponse msResponse = b2BMaterialExecutor.newMaterialForm(mateiralMaster);
                    if (!MSResponse.isSuccessCode(msResponse)) {
                        throw new RuntimeException(msResponse.getMsg());
                    }
                }
            }
        }
    }

    /**
     * 删除配件申请
     *
     * @param orderId
     * @param quarter
     * @param formId
     * @param user
     */
    @Transactional(readOnly = false)
    public void deleteMaterialAppy(Long orderId, String quarter, Long formId,String formNo, User user,Dict orderStatus,Long customerId,Integer dataSource) {
        Date date = new Date();
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("id", formId);
        params.put("quarter", quarter);//*
        params.put("delFlag", 1);//*
        params.put("updateBy", user);
        params.put("updateDate", date);
        dao.updateMaterialMaster(params);
        int qty = dao.getMaterialMasterCountByOrderId(orderId, quarter, null);
        if (qty == 0) {
            params.clear();
            params.put("orderId", orderId);
            params.put("quarter", quarter);//*
            params.put("partsFlag", 0);
            params.put("partsApplyDate", 0); //2019-03-17
            orderService.updateOrderCondition(params);
        }
        //日志 2021-01-16
        //log
        OrderProcessLog processLog = new OrderProcessLog();
        processLog.setQuarter(quarter);//*
        processLog.setAction("APP删除配件");
        processLog.setOrderId(orderId);
        processLog.setActionComment(String.format("师傅APP操作，删除配件单：%s", formNo));
        processLog.setStatus(orderStatus.getLabel());
        processLog.setStatusValue(orderStatus.getIntValue());
        processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
        processLog.setCloseFlag(0);
        processLog.setCreateBy(user);
        processLog.setCreateDate(date);
        processLog.setCustomerId(customerId);
        processLog.setDataSourceId(dataSource);
        orderService.saveOrderProcessLogNew(processLog);
    }

    // 修改配件申请当头
    public void updateMaterialMaster(HashMap<String, Object> params) {
        dao.updateMaterialMaster(params);
    }

    //endregion

    //region 物流接口

    //endregion 物流接口
    @Transactional
    public void updateLogisticSignAt(Long orderId, String quarter, String expressNo, Date signAt) {
        dao.updateLogisticSignAt(orderId, quarter, expressNo, signAt, new Date());
    }

    //region 公共方法

    /**
     * 从缓存读取配件单明细中配件信息
     *
     * @param items
     * @param materialMap           配件本地缓存
     * @param loadProductOfMateiral 是否装载配件所属产品信息
     */
    private void loadItemMaterials(List<MaterialItem> items, Map<Long, Material> materialMap, boolean loadProductOfMateiral) {
        if (ObjectUtils.isEmpty(items)) {
            return;
        }
        Material material, tmpMaterial;
        Map<Long, Product> productMaps = Maps.newHashMapWithExpectedSize(10);
        for (MaterialItem itm : items) {
            if (itm == null) {
                continue;
            }
            tmpMaterial = itm.getMaterial();
            if (materialMap == null) {
                material = materialService.getFromCache(tmpMaterial.getId());
                if (material != null) {
                    itm.setMaterial(material);
                    if (loadProductOfMateiral) {
                        loadMatierlaProduct(itm, productMaps);
                    }
                }
            } else {
                if (materialMap.containsKey(tmpMaterial.getId())) {
                    itm.setMaterial(materialMap.get(tmpMaterial.getId()));
                } else {
                    material = materialService.getFromCache(tmpMaterial.getId());
                    if (material != null) {
                        materialMap.put(tmpMaterial.getId(), material);
                        itm.setMaterial(material);
                        if (loadProductOfMateiral) {
                            loadMatierlaProduct(itm, productMaps);
                        }
                    }
                }
            }
        }
    }

    /**
     * 装载配件所属产品信息
     *
     * @param item
     * @param productMaps
     */
    private void loadMatierlaProduct(MaterialItem item, Map<Long, Product> productMaps) {
        Product product;
        if (productMaps.containsKey(item.getProduct().getId())) {
            item.setProduct(productMaps.get(item.getProduct().getId()));
        } else {
            product = productService.getProductByIdFromCache(item.getProduct().getId());
            if (product != null) {
                productMaps.put(product.getId(), product);
                item.setProduct(product);
            }
        }
    }

    /**
     * 获得订单项产品
     * 如是套组，拆分为单品,同时一并返回产品的品牌，型号/规格及服务类型
     *
     * @param orderId 订单id
     * @param items   订单项目
     * @return Set<product> 可返回null
     */
    public Set<Product> getOrderProductSet(Long orderId, List<OrderItem> items, Map<Long, ServiceType> serviceTypeMap) {
        if (orderId == null || orderId <= 0 || ObjectUtils.isEmpty(items) || ObjectUtils.isEmpty(serviceTypeMap)) {
            return null;
        }
        List<Product> products = productService.findAllList();
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(LongIDBaseEntity::getId, p -> p));
        Set<Product> itemProductSet = Sets.newHashSetWithExpectedSize(items.size());
        OrderItem item;
        Product p;
        for (int i = 0, size = items.size(); i < size; i++) {
            item = items.get(i);
            p = item.getProduct();
            if (itemProductSet.contains(p)) {
                continue;
            }
            //使用订单项的品牌，型号/规格及服务类型
            p.setBrand(item.getBrand());
            p.setModel(item.getProductSpec());
            p.setServiceType(serviceTypeMap.get(item.getServiceType().getId()));
            itemProductSet.add(p);
        }
        Set<Product> productSet = Sets.newHashSetWithExpectedSize(itemProductSet.size() * 2);
        for (Product product : itemProductSet) {
            if (!productSet.contains(product)) {
                if (product.getSetFlag() == 1) {
                    //套组：拆分
                    parseProductSet(orderId, product, productMap, productSet, serviceTypeMap);
                } else {
                    productSet.add(product);
                }
            }
        }
        return productSet;
    }

    /**
     * 递归拆分套组
     *
     * @param orderId    订单id
     * @param product    产品,包含了品牌，型号/规格及服务类型(来自订单项目)
     * @param productMap 所有产品Map
     * @param productSet 拆分后单品
     */
    private void parseProductSet(Long orderId, Product product, Map<Long, Product> productMap, Set<Product> productSet, Map<Long, ServiceType> serviceTypeMap) {
        if (productSet.contains(product)) {
            return;
        }
        Product p = productMap.get(product.getId());
        if (p == null) {
            log.error("产品缓存中无产品:{} ,请检查缓存或订单项,订单id:{}", product.getId(), orderId);
        } else {
            if (p.getSetFlag() == 0) {
                return;
            }
            List<Long> subProductIdList = getSubProductIdList(p.getProductIds());
            if (!ObjectUtils.isEmpty(subProductIdList)) {
                Long id;
                for (int i = 0, size = subProductIdList.size(); i < size; i++) {
                    id = subProductIdList.get(i);
                    p = productMap.get(id);
                    if (productSet.contains(p)) {
                        continue;
                    }
                    //品牌，型号/规格及服务类型,来自订单项目
                    p.setBrand(product.getBrand());
                    p.setModel(product.getModel());
                    p.setServiceType(product.getServiceType());
                    if (p.getSetFlag() == 0) {
                        productSet.add(p);
                    } else {
                        parseProductSet(orderId, p, productMap, productSet, serviceTypeMap);
                    }
                }
            }
        }
    }

    /**
     * 拆分套组
     *
     * @param subProductIds 如:1,2,3,
     * @return
     */
    public List<Long> getSubProductIdList(String subProductIds) {
        if (org.apache.commons.lang3.StringUtils.isBlank(subProductIds)) {
            return null;
        }
        List<Long> subProductIdList = Lists.newArrayListWithCapacity(6);
        String[] ids = subProductIds.split(",");
        String sid = new String("");
        Long pid;
        for (int i = 0, size = ids.length; i < size; i++) {
            sid = ids[i];
            if (StringUtils.isBlank(sid)) {
                continue;
            }
            pid = Long.valueOf(sid);
            if (pid > 0) {
                subProductIdList.add(pid);
            }
        }
        return subProductIdList;
    }

    /**
     * 判断订单来源是否启用B2B配件微服务
     */
    public boolean isOpenB2BMaterialSource(Integer dataSourceId) {

        B2BDataSourceEnum dataSource = B2BDataSourceEnum.valueOf(dataSourceId);
        if (dataSource == null) {
            return false;
        }
        return msProperties.getB2bcenter().getMaterial().containsKey(dataSourceId);
    }

    //endregion 公共方法

    //region 列表

    //endregion

    //region 客评

    /**
     * 根据订单配件状态检查是否可以客评,退单,取消
     * old,作废
     * 1.配件单未审核,未发货，不能客评
     * 2.返件单未发货，不能客评
     * new 2019-07-10
     * 1.判断配件单，是否审核，没审核不能客评
     * 2.如驳回，可以客评
     * 3.审核通过，判断返件单是否需要返件，不需要返件，可以客评；需要返件，必须填了返件快递单号才能客评
     *
     * @param orderId
     * @param quarter
     * @return MSResponse MSResponse.isSuccesCode() == true ,可客评
     * code = 10000 ,不能客评
     * code = 1，需手动关闭再客评
     */
    public MSResponse<String> canGradeOfMaterialForm(Integer dataSource, long orderId, String quarter) {
        MSResponse<String> response = new MSResponse<String>();
        if (orderId <= 0) {
            return new MSResponse(MSErrorCode.newInstance(MSErrorCode.FAILURE, "订单参数错误"));
        }
        //1.配件单：未审核
        List<MaterialMaster> forms = dao.getMaterialFormsForGrade(orderId, quarter);
        //无配件单
        if (ObjectUtils.isEmpty(forms)) {
            return response;
        }
        //流的重用,使用Supplier
        Supplier<Stream<MaterialMaster>> streamSupplier = () -> forms.stream();
        long cnt = streamSupplier.get()
                .filter(t -> t.getStatus().getIntValue() == MaterialMaster.STATUS_NEW.intValue())
                .count();
        if (cnt > 0) {
            response.setCode(1);
            response.setMsg("订单有未审核的配件单");
            response.setData(forms.get(0).getOrderNo());
            return response;
        }
        //b2b配件单，检查：是否发货
        if (msProperties.getB2bcenter().getMaterial().containsKey(dataSource)) {
            cnt = streamSupplier.get()
                    .filter(t -> t.getStatus().getIntValue() == MaterialMaster.STATUS_APPROVED.intValue())
                    .count();
            if (cnt > 0) {
                response.setCode(1);
                response.setMsg("订单有未发货的配件单，待厂商系统处理");
                response.setData(forms.get(0).getOrderNo());
                return response;
            }
        }
        streamSupplier = null;
        // 2.返件单：未发货，配件单审核后才产生的返件单，因此返件单无需审核
        List<MaterialReturn> returnForms = returnService.getMaterialReturnListForGrade(orderId, quarter);
        //无返件单
        if (ObjectUtils.isEmpty(returnForms)) {
            return response;
        }
        cnt = returnForms.stream()
                .filter(t -> t.getStatus().getIntValue() <= MaterialMaster.STATUS_APPROVED.intValue())
                .count();
        if (cnt > 0) {
            //response.setCode(MSErrorCode.FAILURE.getCode());
            response.setCode(1);
            response.setMsg("订单有返件单未处理完成。");
            response.setData(returnForms.get(0).getOrderNo());
            return response;
        }
        return response;
    }

    /**
     * 读取下个申请次序
     */
    public int getNextApplyTime(Long orderId, String quarter) {
        return dao.getNextApplyTime(orderId, quarter);
    }

    //endregion

    //设置省和市
    public void setArea(OrderCondition orderCondition, MaterialMaster materialMaster) {
        //省市
        Area area = areaService.getFromCache(orderCondition.getArea().getId());
        if (area != null) {
            List<String> ids = Splitter.onPattern(",")
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(area.getParentIds());
            if (ids.size() >= 2) {
                materialMaster.setCityId(Long.valueOf(ids.get(ids.size() - 1)));
                materialMaster.setProvinceId(Long.valueOf(ids.get(ids.size() - 2)));
            }
        }
    }

    public List<NameValuePair<Long, String>> getOrderProductIdAndCustomerModels(List<OrderItem> items){
        if(ObjectUtils.isEmpty(items)){
            return Lists.newArrayList();
        }
        List<Long> productIds = items.stream().filter(i -> i.getProduct() != null && i.getProduct().getId() != null).map(OrderItem::getProductId).distinct().collect(Collectors.toList());
        Map<Long, Product> productMap = productService.getProductMap(productIds);
        List<NameValuePair<Long, String>> result = Lists.newArrayList();
        Product product;
        Set<Long> subProductIdSet;
        List<NameValuePair<Long, String>> subSet;
        for (OrderItem item : items) {
            product = productMap.get(item.getProductId());
            if (product != null) {
                if (product.getSetFlag() == 1) {
                    subProductIdSet = Sets.newHashSet();
                    final String[] setIds = product.getProductIds().split(",");
                    for (String id : setIds) {
                        subProductIdSet.add(StringUtils.toLong(id));
                    }
                    subSet = subProductIdSet.stream().filter(i->i > 0).map(i->new NameValuePair<>(i, item.getB2bProductCode())).collect(Collectors.toList());
                    if (!subSet.isEmpty()) {
                        result.addAll(subSet);
                    }
                } else {
                    result.add(new NameValuePair<>(item.getProductId(), item.getB2bProductCode()));
                }
            }
        }
        return result;
    }
}
