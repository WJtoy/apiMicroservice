package com.wolfking.jeesite.ms.viomi.sd.service;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderCompletedItem;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderPraiseItem;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderValidateItem;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.praise.Praise;
import com.kkl.kklplus.entity.praise.PraisePicItem;
import com.kkl.kklplus.entity.validate.OrderValidate;
import com.kkl.kklplus.entity.validate.ValidatePicItem;
import com.kkl.kklplus.entity.viomi.sd.VioMiOrderSnCode;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.BitUtils;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.service.OrderLocationService;
import com.wolfking.jeesite.modules.sd.service.OrderMaterialService;
import com.wolfking.jeesite.modules.sd.utils.OrderAdditionalInfoUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.dao.B2BOrderDao;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.material.mq.entity.mapper.B2BMaterialMapper;
import com.wolfking.jeesite.ms.praise.service.OrderPraiseService;
import com.wolfking.jeesite.ms.providermd.service.*;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import com.wolfking.jeesite.ms.validate.service.MSOrderValidateService;
import com.wolfking.jeesite.ms.viomi.sd.feign.VioMiOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class VioMiOrderService extends B2BOrderManualBaseService {

    private static final User KKL_VIOMI_B2B_USER = new User(0L, "快可立全国联保", "075729235666");
    private static final TwoTuple<Double, Double> DEFAULT_LOCATION = new TwoTuple<>(113.27661753, 22.75775595);
    private static final String VIOMI_VERIFY_CODE = "200618";
    private static B2BMaterialMapper mapper = Mappers.getMapper(B2BMaterialMapper.class);

    @Resource
    private B2BOrderDao b2BOrderDao;

    @Autowired
    private VioMiOrderFeign vioMiOrderFeign;
    @Autowired
    private OrderCacheReadService orderCacheReadService;
    @Autowired
    private OrderLocationService orderLocationService;
    @Autowired
    private OrderItemCompleteService orderItemCompleteService;
    @Autowired
    private MSCustomerMaterialService msCustomerMaterialService;
    @Autowired
    private MSCustomerErrorTypeService msCustomerErrorTypeService;
    @Autowired
    private MSCustomerErrorCodeService msCustomerErrorCodeService;
    @Autowired
    private MSCustomerErrorActionService msCustomerErrorActionService;
    @Autowired
    private OrderPraiseService orderPraiseService;
    @Autowired
    private MSOrderValidateService msOrderValidateService;
    @Autowired
    private MSServicePointService msServicePointService;
    @Autowired
    private OrderMaterialService orderMaterialService;

    //region 工单处理

    /**
     * 创建派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(String engineerName, String engineerMobile, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile)
                    .setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "");
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate, User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (updater != null && StringUtils.isNotBlank(updater.getName()) && appointmentDate != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setUpdaterName(updater.getName())
                    .setEffectiveDate(appointmentDate)
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建上门请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServiceRequestEntity(Long kklOrderId, String kklQuarter, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (kklOrderId != null) {
            OrderLocation location = orderLocationService.getByOrderId(kklOrderId, kklQuarter);
            if (location == null) {
                location = new OrderLocation();
                location.setLongitude(DEFAULT_LOCATION.getAElement());
                location.setLatitude(DEFAULT_LOCATION.getBElement());
            }
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setLongitude(location.getLongitude())
                    .setLatitude(location.getLatitude())
                    .setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "");
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建App完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppCompleteRequestEntity(Long orderId, String quarter, Long customerId, Long servicePointId, List<OrderItem> orderItems, Date orderCreateDate, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0) {
            long buyDt = 0;
            //2020-12-19 sd_order -> sd_order_head
            OrderAdditionalInfo orderAdditionalInfo = null;
            Order order = b2BOrderDao.getOrderAdditionalInfo(orderId, quarter);
            if(order != null && order.getAdditionalInfoPb() != null && order.getAdditionalInfoPb().length > 0){
                orderAdditionalInfo = OrderAdditionalInfoUtils.pbBypesToAdditionalInfo(order.getAdditionalInfoPb());
            }
            //end
            if (orderAdditionalInfo != null && orderAdditionalInfo.getBuyDate() != null && orderAdditionalInfo.getBuyDate() > 0) {
                buyDt = orderAdditionalInfo.getBuyDate();
            } else {
                buyDt = orderCreateDate != null ? orderCreateDate.getTime() : 0;
            }
            List<B2BOrderCompletedItem> completedItems = getOrderCompletedItems(customerId, orderId, quarter, orderItems, buyDt);
            B2BOrderPraiseItem praiseItem = getOrderPraiseItem(orderId, quarter, servicePointId);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setOrderCompletedItems(completedItems)
                    .setOrderPraiseItem(praiseItem)
                    .setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "");
            ;
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    private List<B2BOrderCompletedItem> getOrderCompletedItems(Long customerId, Long orderId, String quarter, List<OrderItem> orderItems, long buyDt) {
        List<B2BOrderCompletedItem> result = Lists.newArrayList();
        if (orderId != null && orderId > 0) {
            Set<Long> productIdSet = Sets.newHashSet();

            Map<Long, B2BOrderCompletedItem> completedItemMap = Maps.newConcurrentMap();
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            B2BOrderCompletedItem completedItem;
            if (completeList != null && !completeList.isEmpty()) {
                for (OrderItemComplete item : completeList) {
                    productIdSet.add(item.getProduct().getId());
                    completedItem = new B2BOrderCompletedItem();
                    completedItem.setProductId(item.getProduct().getId());
                    completedItem.setUnitBarcode(StringUtils.toString(item.getUnitBarcode()));

                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                    B2BOrderCompletedItem.PicItem newPicItem;
                    for (ProductCompletePicItem innerItem : item.getItemList()) {
                        if (StringUtils.isNotEmpty(innerItem.getPictureCode()) && StringUtils.isNotEmpty(innerItem.getUrl())) {
                            newPicItem = new B2BOrderCompletedItem.PicItem();
                            newPicItem.setCode(innerItem.getPictureCode());
                            newPicItem.setUrl(OrderPicUtils.getOrderPicHostDir() + innerItem.getUrl());
                            completedItem.getPicItems().add(newPicItem);
                        }
                    }
                    //多个相同的产品仅保存第一个
                    if (!completedItemMap.containsKey(item.getProduct().getId())) {
                        completedItemMap.put(item.getProduct().getId(), completedItem);
                    }
                    result.add(completedItem);
                }
            }

            //读取维修故障信息
            Map<Long, List<B2BOrderCompletedItem.ErrorItem>> errorMap = Maps.newConcurrentMap();
            List<OrderDetail> details = b2BOrderDao.getOrderErrors(orderId, quarter);
            if (details != null && !details.isEmpty()) {
                List<NameValuePair<Long, Long>> errorTypeIds = Lists.newArrayList();
                List<NameValuePair<Long, Long>> errorCodeIds = Lists.newArrayList();
                List<NameValuePair<Long, Long>> errorActionIds = Lists.newArrayList();
                for (OrderDetail detail : details) {
                    productIdSet.add(detail.getProductId());
                    errorTypeIds.add(new NameValuePair<>(detail.getProduct().getId(), detail.getErrorType().getId()));
                    errorCodeIds.add(new NameValuePair<>(detail.getProduct().getId(), detail.getErrorCode().getId()));
                    errorActionIds.add(new NameValuePair<>(detail.getProduct().getId(), detail.getActionCode().getId()));
                }
                List<MDErrorType> errorTypes = msCustomerErrorTypeService.findListByCustomerIdAndProductIdsAndIds(customerId, errorTypeIds);
                List<MDErrorCode> errorCodes = msCustomerErrorCodeService.findListByCustomerIdAndProductIdsAndIds(customerId, errorCodeIds);
                List<MDActionCode> actionCodes = msCustomerErrorActionService.findListByCustomerIdAndProductIdsAndIds(customerId, errorActionIds);
                Map<String, MDErrorType> errorTypeMap = Maps.newConcurrentMap();
                for (MDErrorType type : errorTypes) {
                    errorTypeMap.put(String.format("%d:%d", type.getProductId(), type.getId()), type);
                }
                Map<String, MDErrorCode> errorCodeMap = Maps.newConcurrentMap();
                for (MDErrorCode code : errorCodes) {
                    errorCodeMap.put(String.format("%d:%d", code.getProductId(), code.getId()), code);
                }
                Map<String, MDActionCode> errorActionMap = Maps.newConcurrentMap();
                for (MDActionCode action : actionCodes) {
                    errorActionMap.put(String.format("%d:%d", action.getProductId(), action.getId()), action);
                }
                Long productId;
                MDErrorType errorType;
                MDErrorCode errorCode;
                MDActionCode actionCode;
                B2BOrderCompletedItem.ErrorItem errorItem;
                for (OrderDetail detail : details) {
                    errorItem = new B2BOrderCompletedItem.ErrorItem();
                    productId = detail.getProductId();
                    errorType = errorTypeMap.get(String.format("%d:%d", productId, detail.getErrorType().getId()));
                    if (errorType != null) {
                        errorItem.setErrorTypeId(errorType.getId());
                        errorItem.setErrorType(errorType.getName());
                    }
                    errorCode = errorCodeMap.get(String.format("%d:%d", productId, detail.getErrorCode().getId()));
                    if (errorCode != null) {
                        errorItem.setErrorCodeId(errorCode.getId());
                        errorItem.setErrorCode(errorCode.getName());
                    }
                    actionCode = errorActionMap.get(String.format("%d:%d", productId, detail.getActionCode().getId()));
                    if (actionCode != null) {
                        errorItem.setErrorAnalysisId(actionCode.getId());
                        errorItem.setErrorAnalysis(actionCode.getAnalysis());
                        errorItem.setErrorActionId(actionCode.getId());
                        errorItem.setErrorAction(actionCode.getName());
                    }
                    if (errorMap.containsKey(productId)) {
                        errorMap.get(productId).add(errorItem);
                    } else {
                        errorMap.put(productId, Lists.newArrayList(errorItem));
                    }
                }
            }

            //读取配件信息
            Map<Long, List<B2BOrderCompletedItem.Material>> materialMap = Maps.newConcurrentMap();
            List<MaterialItem> materials = b2BOrderDao.getOrderMaterials(orderId, quarter);
            if (materials != null && !materials.isEmpty()) {
                List<NameValuePair<Long, String>> productIdAndCustomerModels = orderMaterialService.getOrderProductIdAndCustomerModels(orderItems);
                List<CustomerMaterial> params = Lists.newArrayList();
                CustomerMaterial param;
                String customerModel;
                CustomerProductModel productModel;
                for (MaterialItem item : materials) {
                    productIdSet.add(item.getProduct().getId());
                    param = new CustomerMaterial();
                    param.setCustomer(new Customer(customerId));
                    param.setProduct(item.getProduct());
                    param.setMaterial(new Material(item.getMaterial().getId()));
                    customerModel = productIdAndCustomerModels.stream().filter(i -> i.getName().equals(item.getProduct().getId())).findFirst().map(NameValuePair::getValue).orElse("");
                    productModel = new CustomerProductModel();
                    productModel.setCustomerModelId(customerModel);
                    param.setCustomerProductModel(productModel);
                    params.add(param);
                }
                List<CustomerMaterial> customerMaterials = msCustomerMaterialService.findListByCustomerMaterial(params);
                Map<String, String> customerPartCodeMap = Maps.newConcurrentMap();
                if (customerMaterials != null && !customerMaterials.isEmpty()) {
                    for (CustomerMaterial customerMaterial : customerMaterials) {
                        customerPartCodeMap.put(String.format("%d:%d", customerMaterial.getProduct().getId(), customerMaterial.getMaterial().getId()), customerMaterial.getCustomerPartCode());
                    }
                }
                String key;
                Long productId;
                Long materialId;
                String materialCode;
                B2BOrderCompletedItem.Material material;
                for (MaterialItem item : materials) {
                    productId = item.getProduct().getId();
                    materialId = item.getMaterial().getId();
                    key = String.format("%d:%d", productId, materialId);
                    materialCode = customerPartCodeMap.get(key);
                    material = new B2BOrderCompletedItem.Material();
                    material.setMaterialId(materialId);
                    material.setMaterialCode(StringUtils.toString(materialCode));
                    material.setQty(item.getQty());
                    if (materialMap.containsKey(productId)) {
                        materialMap.get(productId).add(material);
                    } else {
                        materialMap.put(productId, Lists.newArrayList(material));
                    }
                }
            }
            //设置维修故障与配件
            for (Long pId : productIdSet) {
                B2BOrderCompletedItem newCompletedItem = completedItemMap.get(pId);
                if (newCompletedItem == null) {
                    newCompletedItem = new B2BOrderCompletedItem();
                    newCompletedItem.setProductId(pId);
                    result.add(newCompletedItem);
                }
                List<B2BOrderCompletedItem.ErrorItem> b2bErrorItems = errorMap.get(pId);
                if (b2bErrorItems != null) {
                    newCompletedItem.setErrorItems(b2bErrorItems);
                }
                List<B2BOrderCompletedItem.Material> b2bMaterials = materialMap.get(pId);
                if (b2bMaterials != null) {
                    newCompletedItem.setMaterials(b2bMaterials);
                }
                newCompletedItem.setBuyDt(buyDt);
            }
        }
        return result;
    }

    private B2BOrderPraiseItem getOrderPraiseItem(Long orderId, String quarter, Long servicePointId) {
        B2BOrderPraiseItem praiseItem = null;
        Praise praise = orderPraiseService.getByOrderId(quarter, orderId, servicePointId);
        if (praise != null && praise.getPicItems() != null && !praise.getPicItems().isEmpty()) {
            praiseItem = new B2BOrderPraiseItem();
            for (PraisePicItem item : praise.getPicItems()) {
                if (StringUtils.isNotBlank(item.getUrl())) {
                    praiseItem.getPicUrls().add(OrderPicUtils.getPraisePicUrl(item.getUrl()));
                }
            }
        }
        return praiseItem;
    }

    /**
     * 创建工单鉴定请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createValidateRequestEntity(Long orderId, String quarter, Long servicePointId, Date orderCreateDate, OrderValidate orderValidate, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0) {
            long buyDt = 0;
            //2020-12-19 sd_order -> sd_order_head
            OrderAdditionalInfo orderAdditionalInfo = null;
            Order order = b2BOrderDao.getOrderAdditionalInfo(orderId, quarter);
            if(order != null && order.getAdditionalInfoPb() != null && order.getAdditionalInfoPb().length > 0){
                orderAdditionalInfo = OrderAdditionalInfoUtils.pbBypesToAdditionalInfo(order.getAdditionalInfoPb());
            }
            //end
            if (orderAdditionalInfo != null && orderAdditionalInfo.getBuyDate() != null && orderAdditionalInfo.getBuyDate() > 0) {
                buyDt = orderAdditionalInfo.getBuyDate();
            } else {
                buyDt = orderCreateDate != null ? orderCreateDate.getTime() : 0;
            }
            B2BOrderValidateItem validateItem = getOrderValidateItem(orderValidate);
            if (validateItem != null) {
                validateItem.setBuyDt(buyDt);
                ServicePoint servicePoint = msServicePointService.getSimpleCacheById(servicePointId);
                if (servicePoint != null && StringUtils.isNotBlank(servicePoint.getName())) {
                    validateItem.setReceiver(servicePoint.getName());
                }
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setOrderValidateItem(validateItem)
                        .setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "");
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

    private B2BOrderValidateItem getOrderValidateItem(OrderValidate validate) {
        B2BOrderValidateItem result = null;
        if (validate != null) {
//            OrderValidate validate = msOrderValidateService.getUncompletedOrderValidate(orderId, quarter);
            result = new B2BOrderValidateItem();
            result.setProductId(validate.getProductId());
            result.setProductSn(validate.getProductSn());
            result.setIsFault(validate.getIsFault());
            result.setErrorDescription(validate.getErrorDescription());
            result.setCheckValidateDetail(validate.getCheckValidateDetail());
            result.setPackValidateDetail(validate.getPackValidateDetail());
            result.setReceiver(validate.getReceiver());
            result.setReceivePhone(validate.getReceivePhone());
            result.setReceiveAddress(validate.getReceiveAddress());

            B2BOrderValidateItem.ErrorItem errorItem = new B2BOrderValidateItem.ErrorItem();
            if (validate.getErrorTypeId() != null && validate.getErrorTypeId() > 0) {
                MDErrorType errorType = msCustomerErrorTypeService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getErrorTypeId());
                if (errorType != null) {
                    errorItem.setErrorTypeId(errorType.getId());
                    errorItem.setErrorType(errorType.getName());
                }
            }
            if (validate.getErrorCodeId() != null && validate.getErrorCodeId() > 0) {
                MDErrorCode errorCode = msCustomerErrorCodeService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getErrorCodeId());
                if (errorCode != null) {
                    errorItem.setErrorCodeId(errorCode.getId());
                    errorItem.setErrorCode(errorCode.getName());
                }
            }
            if (validate.getActionCodeId() != null && validate.getActionCodeId() > 0) {
                MDActionCode actionCode = msCustomerErrorActionService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getActionCodeId());
                if (actionCode != null) {
                    errorItem.setErrorAnalysisId(actionCode.getId());
                    errorItem.setErrorAnalysis(actionCode.getAnalysis());
                    errorItem.setErrorActionId(actionCode.getId());
                    errorItem.setErrorAction(actionCode.getName());
                }
            }
            result.setErrorItem(errorItem);

            Dict dict;
            List<String> checkValidateResultValues = BitUtils.getPositions(validate.getCheckValidateResult(), String.class);
            if (ObjectUtil.isNotEmpty(checkValidateResultValues)) {
                Map<String, Dict> checkValidateResultMap = msOrderValidateService.getCheckValidateResultMap();
                for (String value : checkValidateResultValues) {
                    dict = checkValidateResultMap.get(value);
                    if (dict != null) {
                        result.getCheckValidateResultValues().add(dict.getLabel());
                    }
                }
            }
            List<String> packValidateResultValues = BitUtils.getPositions(validate.getPackValidateResult(), String.class);
            if (ObjectUtil.isNotEmpty(packValidateResultValues)) {
                Map<String, Dict> packValidateResultMap = msOrderValidateService.getPackValidateResultMap();
                for (String value : packValidateResultValues) {
                    dict = packValidateResultMap.get(value);
                    if (dict != null) {
                        result.getPackValidateResultValues().add(dict.getLabel());
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(validate.getPicItems())) {
                for (ValidatePicItem item : validate.getPicItems()) {
                    result.getPicUrls().add(OrderPicUtils.getPraisePicUrl(item.getUrl()));
                }
            }
        }
        return result;
    }


    /**
     * 创建完成请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
        builder.setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "");
        result.setAElement(true);
        result.setBElement(builder);
        return result;
    }


    /**
     * 创建取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCancelRequestEntity(Integer kklCancelType, User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (kklCancelType != null && updater != null && updater.getId() != null && updater.getId() > 0) {
            updater = MSUserUtils.get(updater.getId());
            if (updater == null) {
                updater = KKL_VIOMI_B2B_USER;
            }
            String cancelReason = B2BMDUtils.getVioMiCancelReason(kklCancelType);
            if (StringUtils.isNotBlank(cancelReason)) {
                String updaterName = StringUtils.isNotBlank(updater.getName()) ? updater.getName() : KKL_VIOMI_B2B_USER.getName();
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setUpdaterName(updaterName)
                        .setB2bReason(cancelReason)
                        .setRemarks(StringUtils.toString(remarks));
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

    //endregion 工单处理

    //region  日志

    /**
     * 创建云米日志消息实体
     */
    public TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> createOrderProcessLogReqEntity(OrderProcessLog log) {
        TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (log.getCreateBy() != null && StringUtils.isNotBlank(log.getCreateBy().getName())
                && StringUtils.isNotBlank(log.getActionComment())) {
            B2BOrderProcessLogReqEntity.Builder builder = new B2BOrderProcessLogReqEntity.Builder();
            builder.setOperatorName(log.getCreateBy().getName())
                    .setLogContext(log.getActionComment())
                    .setDataSourceId(B2BDataSourceEnum.VIOMI.id);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }
    //endregion  日志

    //region 验证产品SN

    public MSResponse checkProductSN(String b2bOrderNo, String productSn, User operator) {
        VioMiOrderSnCode params = new VioMiOrderSnCode();
        params.setOrderNumber(b2bOrderNo);
        params.setSnCode(productSn);
        params.setCreateById(operator.getId());
        return vioMiOrderFeign.getGradeSn(params);
    }

    //endregion 验证产品SN
}
