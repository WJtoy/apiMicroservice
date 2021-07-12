package com.wolfking.jeesite.ms.joyoung.sd.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.kkl.kklplus.entity.common.material.B2BMaterialClose;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.joyoung.sd.feign.JoyoungOrderFeign;
import com.wolfking.jeesite.ms.material.mq.entity.mapper.B2BMaterialMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JoyoungOrderService extends B2BOrderManualBaseService {

//    @Resource
//    private OrderItemDao orderItemDao;

    @Autowired
    private JoyoungOrderFeign joyoungOrderFeign;

    private static B2BMaterialMapper mapper = Mappers.getMapper(B2BMaterialMapper.class);

    @Autowired
    OrderItemCompleteService orderItemCompleteService;

    @Autowired
    private ServicePointService servicePointService;

    //region 其他

//    private List<CanboOrderCompleted.CompletedItem> getJoyoungOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
//        List<CanboOrderCompleted.CompletedItem> list = null;
//        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
//            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
//            CanboOrderCompleted.CompletedItem completedItem = null;
//            ProductCompletePicItem picItem = null;
//            if (completeList != null && !completeList.isEmpty()) {
//                list = com.google.common.collect.Lists.newArrayList();
//                Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);
////                for (OrderItem orderItem : orderItems) {
////                    if (StringUtils.isNotBlank(orderItem.getB2bProductCode())) {
////                        if (b2bProductCodeMap.containsKey(orderItem.getProductId())) {
////                            b2bProductCodeMap.get(orderItem.getProductId()).add(orderItem.getB2bProductCode());
////                        } else {
////                            b2bProductCodeMap.put(orderItem.getProductId(), com.google.common.collect.Lists.newArrayList(orderItem.getB2bProductCode()));
////                        }
////                    }
////                }
//                List<String> b2bProductCodeList;
//                for (OrderItemComplete item : completeList) {
//                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
//                    Map<String, ProductCompletePicItem> picItemMap = Maps.newHashMap();
//                    for (ProductCompletePicItem innerItem : item.getItemList()) {
//                        picItemMap.put(innerItem.getPictureCode(), innerItem);
//                    }
//                    completedItem = new CanboOrderCompleted.CompletedItem();
//
//                    //获取B2B产品编码
//                    b2bProductCodeList = b2bProductCodeMap.get(item.getProduct().getId());
//                    if (b2bProductCodeList != null && !b2bProductCodeList.isEmpty()) {
//                        completedItem.setItemCode(b2bProductCodeList.get(0));
//                        if (b2bProductCodeList.size() > 1) {
//                            b2bProductCodeList.remove(0);
//                        }
//                    } else {
//                        completedItem.setItemCode("");
//                    }
//
//                    completedItem.setBarcode(StringUtils.toString(item.getUnitBarcode()));
//                    completedItem.setOutBarcode(StringUtils.toString(item.getOutBarcode()));
//                    //条码图片
//                    picItem = picItemMap.get("pic4");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic4(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    //现场图片
//                    picItem = picItemMap.get("pic1");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic1(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    picItem = picItemMap.get("pic2");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic2(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    picItem = picItemMap.get("pic3");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic3(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    list.add(completedItem);
//                }
//            }
//        }
//        return list;
//    }

    //endregion 其他

    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    //region 创建工单状态变更情况实体

    /**
     * 创建九阳派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建九阳预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Integer pendingType, Date appointmentDate, User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (updater != null && StringUtils.isNotBlank(updater.getName()) && appointmentDate != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setUpdaterName(updater.getName())
                    .setEffectiveDate(appointmentDate)
                    .setRemarks(StringUtils.toString(remarks))
                    .setPendingType(pendingType == null ? "" : pendingType.toString());
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建九阳上门请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServiceRequestEntity(Date visitedDate, Long servicePointId, Long engineerId, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (visitedDate != null && servicePointId != null && servicePointId > 0 && engineerId != null && engineerId > 0) {
            Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
            if (engineer != null && StringUtils.isNotBlank(engineer.getName()) && StringUtils.isNotBlank(engineer.getContactInfo())) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setEngineerName(engineer.getName())
                        .setEffectiveDate(visitedDate)
                        .setRemarks(StringUtils.toString(remarks));
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

//    /**
//     * 创建九阳完工请求对象
//     */
//    @Transactional()
//    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems, String remarks) {
//        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
//        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
//            if (orderItems == null || orderItems.isEmpty()) {
//                Order order = orderItemDao.getOrderItems(quarter, orderId);
//                if (order != null) {
//                    orderItems = OrderItemUtils.fromOrderItemsJson(order.getOrderItemJson());
//                }
//            }
//            List<CanboOrderCompleted.CompletedItem> completedItems = getJoyoungOrderCompletedItems(orderId, quarter, orderItems);
//            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
//            builder.setCompletedItems(completedItems)
//                    .setRemarks(StringUtils.toString(remarks));
//            result.setAElement(true);
//            result.setBElement(builder);
//        }
//        return result;
//    }

//    /**
//     * 创建九阳取消请求对象
//     */
//    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCancelRequestEntity(Integer kklCancelType, String remarks, Date cancelDate, User updater) {
//        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
//        StringBuilder cancelResponsible = new StringBuilder();
//        if (kklCancelType != null) {
//            cancelResponsible.append(MSDictUtils.getDictLabel(kklCancelType.toString(), Dict.DICT_TYPE_CANCEL_RESPONSIBLE, ""));
//        }
//        if (StringUtils.isNotBlank(remarks)) {
//            if (StringUtils.isNotBlank(cancelResponsible.toString())) {
//                cancelResponsible.append(":");
//            }
//            cancelResponsible.append(remarks);
//        }
//        if (StringUtils.isNotBlank(cancelResponsible.toString()) && cancelDate != null) {
//            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
//            builder.setEffectiveDate(cancelDate)
//                    .setUpdaterName(updater != null && StringUtils.isNotBlank(updater.getName()) ? updater.getName() : "")
//                    .setRemarks(cancelResponsible.toString());
//            result.setAElement(true);
//            result.setBElement(builder);
//        }
//        return result;
//    }

    //endregion 创建工单状态变更情况实体

    //region 创建工单日志消息实体

    /**
     * 创建九阳工单日志消息实体
     */
    public TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> createOrderProcessLogReqEntity(OrderProcessLog log) {
        TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (log.getCreateBy() != null && StringUtils.isNotBlank(log.getCreateBy().getName())
                && log.getCreateDate() != null && StringUtils.isNotBlank(log.getActionComment())) {
            B2BOrderProcessLogReqEntity.Builder builder = new B2BOrderProcessLogReqEntity.Builder();
            builder.setOperatorName(log.getCreateBy().getName())
                    .setLogDt(log.getCreateDate().getTime())
                    .setLogContext(log.getActionComment())
                    .setDataSourceId(B2BDataSourceEnum.JOYOUNG.id);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


//    /**
//     * 往九阳微服务推送工单日志
//     */
//    public MSResponse pushOrderProcessLogToMS(MQB2BOrderProcessLogMessage.B2BOrderProcessLogMessage message) {
//        JoyoungOrderProcessLog orderProcessLog = new JoyoungOrderProcessLog();
//        orderProcessLog.setOrderId(message.getOrderId());
//        orderProcessLog.setOperatorName(message.getOperatorName());
//        orderProcessLog.setLogDate(message.getLogDt());
//        orderProcessLog.setLogType(message.getLogType());
//        orderProcessLog.setLogContent(message.getLogContext());
//        orderProcessLog.setCreateById(message.getCreateById());
//        orderProcessLog.setCreateDt(message.getCreateDt());
//        MSResponse response = joyoungOrderFeign.saveOrderProcesslog(orderProcessLog);
//        return response;
//    }

    //endregion 创建工单日志消息实体

    //region 配件

    /**
     * 申请配件单
     */
    public MSResponse newMaterialForm(MaterialMaster materialMaster) {
        try {
            B2BMaterial materialForm = mapper.toB2BMaterialForm(materialMaster);
            if (materialForm == null) {
                return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "配件单转九阳配件单错误"));
            }
            return joyoungOrderFeign.newMaterialForm(materialForm);
        } catch (Exception e) {
            log.error("orderId:{} ", materialMaster.getOrderId(), e);
            return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "微服务接口执行失败"));
        }
    }

    /**
     * 关闭配件单
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     */
    public MSResponse materialClose(Long formId, String formNo, B2BMaterialClose.CloseType closeType, String remark, Long user) {
        if (formId == null || formId <= 0 || StringUtils.isBlank(formNo)) {
            return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "参数不合法"));
        }
        B2BMaterialClose joyoung = B2BMaterialClose.builder()
                .kklMasterId(formId)
                .kklMasterNo(formNo)
                .closeType(closeType.getCode())
                .remark(remark)
                .build();
        joyoung.setCreateById(user);
        joyoung.setCreateDt(System.currentTimeMillis());
        joyoung.setUpdateById(user);
        joyoung.setUpdateDt(System.currentTimeMillis());
        return joyoungOrderFeign.materialClose(joyoung);
    }

    /**
     * by订单关闭
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     */
    public MSResponse materialCloseByOrder(Long orderId, B2BMaterialClose.CloseType closeType, String remark, Long user) {
        if (orderId == null || orderId <= 0) {
            return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "参数不合法"));
        }
        B2BMaterialClose joyoung = B2BMaterialClose.builder()
                .kklOrderId(orderId)
                .closeType(closeType.getCode())
                .remark(remark)
                .build();
        joyoung.setCreateById(user);
        joyoung.setCreateDt(System.currentTimeMillis());
        joyoung.setUpdateById(user);
        joyoung.setUpdateDt(System.currentTimeMillis());
        return joyoungOrderFeign.materialCloseByOrder(joyoung);
    }

    /**
     * 处理完"审核"消息回调通知微服务
     */
    public MSResponse notifyApplyFlag(Long formId) {
        return joyoungOrderFeign.updateApplyFlag(formId);
    }

    /**
     * 处理完"已发货"消息回调通知微服务
     */
    public MSResponse notifyDeliverFlag(Long formId) {
        return joyoungOrderFeign.updateDeliverFlag(formId);
    }

    //endregion 配件

    //region 验证产品SN

    public MSResponse checkProductSN(String productSn) {
        return joyoungOrderFeign.getProductData(productSn);
    }

    //endregion 验证产品SN
}
