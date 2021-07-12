/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.service.MaterialService;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.modules.sd.dao.OrderMaterialReturnDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单返件件管理服务
 * 包含返件单及跟踪进度
 *
 * @author Ryan
 * @date 2019-06-28
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderMaterialReturnService extends LongIDBaseService {



    @Autowired
    private OrderMaterialReturnDao dao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MaterialService materialService;

    //region 返件单


    /**
     * 按id获得返件单信息
     * 包含单身及附件
     *
     * @param id           返件单id
     * @param masterId     配件单id
     * @param quarter      分片
     * @param withOrder    返回订单基本信息
     * @param withPending  返回跟踪信息
     * @param withReceivor 返回收件信息
     * @param withClose    返回关闭信息
     * @return
     */
    public MaterialReturn getMaterialReturnById(Long id, Long masterId, String quarter, Integer withOrder, Integer withPending, Integer withReceivor, Integer withClose) {
        MaterialReturn materialReturn = dao.getReturnFormById(id, masterId, quarter, withOrder, withPending, withReceivor, withClose);
        loadMaterialReturnInfo(materialReturn, true);
        if (materialReturn != null) {
            //图片
            List<MaterialAttachment> attachements = dao.findAttachementsByReturnId(materialReturn.getId(), materialReturn.getQuarter());
            if (attachements == null) {
                attachements = Lists.newArrayList();
            }
            materialReturn.setAttachments(attachements);
        }
        return materialReturn;
    }

    /**
     * 按id获得返件单信息，包含单身
     * 不包含图片
     *
     * @param id           返件单id
     * @param masterId     配件单id
     * @param quarter      分片
     * @param withOrder    返回订单基本信息
     * @param withPending  返回跟踪信息
     * @param withReceivor 返回收件信息
     * @param withClose    返回关闭信息
     * @return
     */
    public MaterialReturn getMaterialReturnNoAttachById(Long id, Long masterId, String quarter, Integer withOrder, Integer withPending, Integer withReceivor, Integer withClose) {
        MaterialReturn materialReturn = dao.getReturnFormById(id, masterId, quarter, withOrder, withPending, withReceivor, withClose);
        //切换为微服务
        loadMaterialReturnInfo(materialReturn, true);
        return materialReturn;
    }

    /**
     * 读取订单返件状态信息，判断是否可客评
     * 包含id,status,masterId
     */
    public List<MaterialReturn> getMaterialReturnListForGrade(@Param("orderId") Long orderId, @Param("quarter") String quarter) {
        return dao.getMaterialReturnListForGrade(orderId, quarter);
    }

    /**
     * 更新返件申请单-物流信息 For app
     * 必须要有快递单号
     * 2018/05/15,状态为：已发货
     * 2019/06/28 返件不订阅物流
     */
    @Transactional(readOnly = false)
    public void updateMaterialReturnApplyExpressForApp(Order order, MaterialReturn materialReturn) {
        if (order == null || order.getOrderCondition() == null) {
            throw new OrderException("读取订单单据失败");
        }
        //检查，返件申请快递必须填写
        if (StringUtils.isBlank(materialReturn.getExpressCompany().getValue()) || StringUtils.isBlank(materialReturn.getExpressNo())) {
            throw new OrderException("快递信息不完整，请输入完整后再保存");
        }
        //update 返件单
        HashMap<String, Object> params = Maps.newHashMapWithExpectedSize(10);
        params.put("quarter", order.getQuarter());
        params.put("id", materialReturn.getId());
        params.put("expressCompany", materialReturn.getExpressCompany());
        params.put("expressNo", materialReturn.getExpressNo());
        params.put("updateBy", materialReturn.getUpdateBy());
        params.put("updateDate", materialReturn.getUpdateDate());
        params.put("sendDate", materialReturn.getUpdateDate());
        params.put("status", new Dict(MaterialMaster.STATUS_SENDED, "已发货"));//2018/05/15
        dao.updateMaterialReturn(params);

        //attachment
        if (materialReturn.getAttachments() != null && materialReturn.getAttachments().size() > 0) {
            List<MaterialAttachment> attachments = materialReturn.getAttachments();
            MaterialAttachment attachment;
            Long attcId;
            for (int k = 0, ksize = attachments.size(); k < ksize; k++) {
                attachment = attachments.get(k);
                dao.insertMaterialAttach(attachment);
                //关系表
                dao.insertMaterialMasterAttachMap(materialReturn.getId(), attachment.getId(), materialReturn.getQuarter());
            }
        }

        //订单log
        OrderProcessLog processLog = new OrderProcessLog();
        processLog.setQuarter(order.getQuarter());
        processLog.setAction("返件发货");
        processLog.setOrderId(order.getId());
        processLog.setActionComment(String.format("APP返件发货,操作人:%s", materialReturn.getUpdateBy().getName()));
        processLog.setStatus(order.getOrderCondition().getStatus().getLabel());
        processLog.setStatusValue(order.getOrderCondition().getStatusValue());
        processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
        processLog.setCloseFlag(0);
        processLog.setCreateBy(materialReturn.getUpdateBy());
        processLog.setCreateDate(materialReturn.getUpdateDate());
        processLog.setCustomerId(order.getOrderCondition().getCustomerId());
        processLog.setDataSourceId(order.getDataSourceId());
        orderService.saveOrderProcessLogNew(processLog);
    }

    //endregion

    //region 公共方法

    private void loadMaterialReturnInfo(MaterialReturn form, boolean loadItems) {
        if (form != null) {
            if (form.getStatus() != null && StringUtils.toInteger(form.getStatus().getValue()) > 0) {
                String statusLabel = MSDictUtils.getDictLabel(form.getStatus().getValue(), "material_apply_status", "");
                form.getStatus().setLabel(statusLabel);
            }
            if (form.getApplyType() != null && StringUtils.toInteger(form.getApplyType().getValue()) > 0) {
                String applyTypeLabel = MSDictUtils.getDictLabel(form.getApplyType().getValue(), "material_apply_type", "");
                form.getApplyType().setLabel(applyTypeLabel);
            }
            Product product = productService.getProductByIdFromCache(form.getProduct().getId());
            if (product != null) {
                form.setProduct(product);
            }
            //express company
            if (StringUtils.isNotBlank(form.getExpressCompany().getValue())) {
                Dict company = MSDictUtils.getDictByValue(form.getExpressCompany().getValue(), "express_type");
                if (company != null) {
                    form.setExpressCompany(company);
                }
            }
            //items
            if (loadItems) {
                loadItemMaterials(form.getItems(), null);
            }
        }
    }

    /**
     * 从缓存读取配件单明细中配件信息
     *
     * @param items
     * @param materialMap 配件本地缓存
     */
    private void loadItemMaterials(List<MaterialReturnItem> items, Map<Long, Material> materialMap) {
        if (ObjectUtils.isEmpty(items)) {
            return;
        }
        Material material, tmpMaterial;
        for (MaterialReturnItem itm : items) {
            if (itm == null) {
                continue;
            }
            tmpMaterial = itm.getMaterial();
            if (materialMap == null) {
                material = materialService.getFromCache(tmpMaterial.getId());
                if (material != null) {
                    itm.setMaterial(material);
                }
            } else {
                if (materialMap.containsKey(tmpMaterial.getId())) {
                    itm.setMaterial(materialMap.get(tmpMaterial.getId()));
                } else {
                    material = materialService.getFromCache(tmpMaterial.getId());
                    if (material != null) {
                        materialMap.put(tmpMaterial.getId(), material);
                        itm.setMaterial(material);
                    }
                }
            }
        }
    }

    //endregion 公共方法

}
