package com.wolfking.jeesite.ms.jdueplus.sd.service;

import com.google.common.collect.Maps;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderProcessLogMessage;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderStatusUpdateMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderStatusEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.jdueplus.*;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.jdueplus.sd.feign.JDUEPlusOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 京东优易+
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class JDUEPlusOrderService extends B2BOrderManualBaseService {

    @Autowired
    private JDUEPlusOrderFeign jduePlusOrderFeign;

    @Resource
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderItemCompleteService orderItemCompleteService;


    /**
     * 取消工单转换
     */
    public MSResponse cancelOrderTransition(B2BOrderTransferResult b2BOrderTransferResult) {
        return jduePlusOrderFeign.cancelOrderTransition(b2BOrderTransferResult);
    }


    //region 工单状态检查


    /**
     * 批量检查工单是否可转换
     */
    public MSResponse checkB2BOrderProcessFlag(List<B2BOrderTransferResult> b2bOrderNos) {
        return jduePlusOrderFeign.checkWorkcardProcessFlag(b2bOrderNos);
    }

    //endregion 工单状态检查

    /**
     * 批量更新工单的路由标记
     */
    public MSResponse updateOrderRoutingFlagAll() {
        return jduePlusOrderFeign.updateSystemIdAll();
    }

    //region 更新工单转换状态

    /**
     * 调用同望微服务的B2B工单转换进度更新接口
     */
    public MSResponse sendB2BOrderConversionProgressUpdateCommandToB2B(List<B2BOrderTransferResult> progressList) {
        return jduePlusOrderFeign.updateTransferResult(Lists.newArrayList(progressList));
    }

    //endregion 更新工单转换状态


    //region 康宝工单状态变更

    /**
     * 往康宝微服务发送工单状态更新命令
     */
    public MSResponse sendOrderStatusUpdateCommandToB2B(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        MSResponse response = null;
        if (message.getStatus() == B2BOrderStatusEnum.PLANNED.value) {
            response = jdueOrderPlanned(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(), message.getEngineerName(), message.getEngineerMobile(), message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.APPOINTED.value) {
            response = jdueOrderAppointed(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(), message.getUpdaterName(), message.getEffectiveDt(), message.getRemarks(), message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.COMPLETED.value) {
            List<MQB2BOrderStatusUpdateMessage.CompletedItem> messageCompletedItemList = message.getCompletedItemList();
            List<JDUEPlusOrderCompleted.CompletedItem> completedItemList = Lists.newArrayList();
            if (messageCompletedItemList != null && messageCompletedItemList.size() > 0) {
                JDUEPlusOrderCompleted.CompletedItem completedItem = null;
                for (MQB2BOrderStatusUpdateMessage.CompletedItem item : messageCompletedItemList) {
                    completedItem = new JDUEPlusOrderCompleted.CompletedItem();
                    completedItem.setItemCode(item.getItemCode());
                    completedItem.setPic1(item.getPic1());
                    completedItem.setPic2(item.getPic2());
                    completedItem.setPic3(item.getPic3());
                    completedItem.setPic4(item.getPic4());
                    completedItem.setBarcode(item.getBarcode());
                    completedItem.setOutBarcode(item.getOutBarcode());
                    completedItemList.add(completedItem);
                }
            }
            response = jdueOrderCompleted(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(), completedItemList, message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.CANCELED.value) {
            response = jdueOrderCancelled(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(), message.getUpdaterName(), message.getEffectiveDt(), message.getRemarks(), message.getUpdaterId(), message.getUpdateDt());
        } else {
            response = new MSResponse(MSErrorCode.SUCCESS);
            String msgJson = new JsonFormat().printToString(message);
            LogUtils.saveLog("B2B工单状态变更消息的状态错误", "JDUEOrderService.sendOrderStatusUpdateCommandToB2B", msgJson, null, null);
        }
        return response;
    }

    /**
     * 同望派单
     */
    private MSResponse jdueOrderPlanned(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String engineerName, String engineerMobile, long createById, long createDt) {
        JDUEPlusOrderPlanned planned = new JDUEPlusOrderPlanned();
        planned.setUniqueId(messageId);
        planned.setDataSource(dataSource);
        planned.setB2bOrderId(b2bOrderId);
        planned.setOrderNo(b2bOrderNo);
        planned.setEngineerName(engineerName);
        planned.setEngineerMobile(engineerMobile);
        planned.setCreateById(createById);
        planned.setUpdateById(createById);
        Date date = new Date(createDt);
        planned.setCreateDate(date);
        planned.setUpdateDate(date);
        return jduePlusOrderFeign.orderPlanned(planned);
    }

    /**
     * 同望预约
     */
    private MSResponse jdueOrderAppointed(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String operator, Long appointmentDt, String remark, long createById, long createDt) {
        JDUEPlusOrderAppointed appointed = new JDUEPlusOrderAppointed();
        appointed.setUniqueId(messageId);
        appointed.setDataSource(dataSource);
        appointed.setB2bOrderId(b2bOrderId);
        appointed.setOrderNo(b2bOrderNo);
        appointed.setBookMan(operator);
        appointed.setBookDt(appointmentDt);
        appointed.setBookRemark(remark);
        appointed.setCreateById(createById);
        appointed.setUpdateById(createById);
        Date date = new Date(createDt);
        appointed.setCreateDate(date);
        appointed.setUpdateDate(date);
        return jduePlusOrderFeign.orderAppointed(appointed);
    }

    /**
     * 同望完成
     */
    private MSResponse jdueOrderCompleted(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, List<JDUEPlusOrderCompleted.CompletedItem> completedItems, long createById, long createDt) {
        JDUEPlusOrderCompleted completed = new JDUEPlusOrderCompleted();
        completed.setUniqueId(messageId);
        completed.setDataSource(dataSource);
        completed.setB2bOrderId(b2bOrderId);
        completed.setOrderNo(b2bOrderNo);
        completed.setItems(completedItems);
        completed.setCreateById(createById);
        completed.setUpdateById(createById);
        Date date = new Date(createDt);
        completed.setCreateDate(date);
        completed.setUpdateDate(date);
        return jduePlusOrderFeign.orderCompleted(completed);
    }

    /**
     * 同望取消
     */
    private MSResponse jdueOrderCancelled(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String operator, Long cancelDt, String remark, long createById, long createDt) {
        JDUEPlusOrderCancelled cancelled = new JDUEPlusOrderCancelled();
        cancelled.setUniqueId(messageId);
        cancelled.setDataSource(dataSource);
        cancelled.setB2bOrderId(b2bOrderId);
        cancelled.setOrderNo(b2bOrderNo);
        cancelled.setCancelMan(operator);
        cancelled.setCancelDt(cancelDt);
        cancelled.setCancelRemark(remark);
        cancelled.setCreateById(createById);
        cancelled.setUpdateById(createById);
        Date date = new Date(createDt);
        cancelled.setCreateDate(date);
        cancelled.setUpdateDate(date);
        return jduePlusOrderFeign.orderCancelled(cancelled);
    }

    //endregion region 工单状态变更


    //region 其他


    private List<CanboOrderCompleted.CompletedItem> getCanboOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
        List<CanboOrderCompleted.CompletedItem> list = null;
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            CanboOrderCompleted.CompletedItem completedItem = null;
            ProductCompletePicItem picItem = null;
            if (completeList != null && !completeList.isEmpty()) {
                list = com.google.common.collect.Lists.newArrayList();
                Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);
//                for (OrderItem orderItem : orderItems) {
//                    if (StringUtils.isNotBlank(orderItem.getB2bProductCode())) {
//                        if (b2bProductCodeMap.containsKey(orderItem.getProductId())) {
//                            b2bProductCodeMap.get(orderItem.getProductId()).add(orderItem.getB2bProductCode());
//                        } else {
//                            b2bProductCodeMap.put(orderItem.getProductId(), com.google.common.collect.Lists.newArrayList(orderItem.getB2bProductCode()));
//                        }
//                    }
//                }
                List<String> b2bProductCodeList;
                for (OrderItemComplete item : completeList) {
                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                    Map<String, ProductCompletePicItem> picItemMap = Maps.newHashMap();
                    for (ProductCompletePicItem innerItem : item.getItemList()) {
                        picItemMap.put(innerItem.getPictureCode(), innerItem);
                    }
                    completedItem = new CanboOrderCompleted.CompletedItem();

                    //获取B2B产品编码
                    b2bProductCodeList = b2bProductCodeMap.get(item.getProduct().getId());
                    if (b2bProductCodeList != null && !b2bProductCodeList.isEmpty()) {
                        completedItem.setItemCode(b2bProductCodeList.get(0));
                        if (b2bProductCodeList.size() > 1) {
                            b2bProductCodeList.remove(0);
                        }
                    } else {
                        completedItem.setItemCode("");
                    }

                    completedItem.setBarcode(StringUtils.toString(item.getUnitBarcode()));
                    completedItem.setOutBarcode(StringUtils.toString(item.getOutBarcode()));
                    //条码图片
                    picItem = picItemMap.get("pic4");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic4(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    //现场图片
                    picItem = picItemMap.get("pic1");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic1(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    picItem = picItemMap.get("pic2");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic2(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    picItem = picItemMap.get("pic3");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic3(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    list.add(completedItem);
                }
            }
        }
        return list;
    }

    //endregion 其他


    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 创建同望派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServicePointPlanRequestEntity(String engineerName, String engineerMobile) {
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
     * 创建同望预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate, String remarks, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate)
                    .setUpdaterName(updater.getName())
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建同望完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemDao.getOrderItems(quarter, orderId);
                if (order != null) {
                    //orderItems = OrderItemUtils.fromOrderItemsJson(order.getOrderItemJson());
                    orderItems = OrderItemUtils.pbToItems(order.getItemsPb());//2020-12-17 sd_order -> sd_order_head
                }
            }
            List<CanboOrderCompleted.CompletedItem> completedItems = getCanboOrderCompletedItems(orderId, quarter, orderItems);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setCompletedItems(completedItems);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建同望取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCancelRequestEntity(Date cancelDate, User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (cancelDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(cancelDate)
                    .setUpdaterName(updater.getName())
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    //region  日志

    /**
     * 往微服务推送工单日志
     */
    public MSResponse pushOrderProcessLogToMS(MQB2BOrderProcessLogMessage.B2BOrderProcessLogMessage message) {
        JDUEPlusOrderLog log = new JDUEPlusOrderLog();
        log.setUniqueId(message.getId());
        log.setOrderId(message.getOrderId());
        log.setLogAction(message.getLogTitle());
        log.setLogContent(message.getLogContext());
        log.setCreateById(message.getCreateById());
        log.setDataSource(message.getDataSourceId());
        return jduePlusOrderFeign.saveLog(log);
    }

    /**
     * 创建日志消息实体
     */
    public TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> createOrderProcessLogReqEntity(OrderProcessLog log, Integer dataSourceId) {
        TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (log.getCreateBy() != null && log.getCreateBy().getId() != null
                && StringUtils.isNotBlank(log.getAction()) && StringUtils.isNotBlank(log.getActionComment())) {
            B2BOrderProcessLogReqEntity.Builder builder = new B2BOrderProcessLogReqEntity.Builder();
            builder.setOperatorName(log.getCreateBy().getName())
                    .setCreateById(log.getCreateBy().getId())
                    .setLogTitle(log.getAction())
                    .setLogContext(log.getActionComment())
                    .setDataSourceId(dataSourceId);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }
    //endregion  日志

}
