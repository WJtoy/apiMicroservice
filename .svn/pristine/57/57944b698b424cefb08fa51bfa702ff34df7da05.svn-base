package com.wolfking.jeesite.ms.mbo.service;

import com.google.common.collect.Maps;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderProcessLogMessage;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderStatusUpdateMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderActionEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderStatusEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.mbo.sd.*;
import com.kkl.kklplus.entity.weber.sd.WeberOrderPlanned;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.mbo.feign.MBOOrderFeign;
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

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class MBOOrderService extends B2BOrderManualBaseService {

    @Autowired
    private MBOOrderFeign mboOrderFeign;
    @Resource
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderItemCompleteService orderItemCompleteService;
    @Autowired
    private OrderCacheReadService orderCacheReadService;


    /**
     * 取消工单转换
     */
    public MSResponse cancelOrderTransition(B2BOrderTransferResult b2BOrderTransferResult) {
        return mboOrderFeign.cancelOrderTransition(b2BOrderTransferResult);
    }


    //region 工单状态检查


    /**
     * 批量检查工单是否可转换
     */
    public MSResponse checkB2BOrderProcessFlag(List<B2BOrderTransferResult> b2bOrderNos) {
        return mboOrderFeign.checkWorkcardProcessFlag(b2bOrderNos);
    }

    //endregion 工单状态检查


    //region 更新工单转换状态

    /**
     * 调用同望微服务的B2B工单转换进度更新接口
     */
    public MSResponse sendB2BOrderConversionProgressUpdateCommandToB2B(List<B2BOrderTransferResult> progressList) {
        return mboOrderFeign.updateTransferResult(Lists.newArrayList(progressList));
    }

    //endregion 更新工单转换状态


    //region 康宝工单状态变更

    /**
     * 往微服务发送工单状态更新命令
     */
    public MSResponse sendOrderStatusUpdateCommandToB2B(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        MSResponse response = null;
        if (message.getStatus() == B2BOrderStatusEnum.PLANNED.value) {
            response = orderPlanned(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(),
                    message.getEngineerName(), message.getEngineerMobile(), message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.APPOINTED.value) {
            response = orderAppointed(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(),
                    message.getUpdaterName(), message.getEffectiveDt(), message.getRemarks(), message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.COMPLETED.value) {
            List<MQB2BOrderStatusUpdateMessage.CompletedItem> messageCompletedItemList = message.getCompletedItemList();
            List<MBOOrderCompleted.CompletedItem> completedItemList = Lists.newArrayList();
            if (messageCompletedItemList != null && messageCompletedItemList.size() > 0) {
                MBOOrderCompleted.CompletedItem completedItem = null;
                for (MQB2BOrderStatusUpdateMessage.CompletedItem item : messageCompletedItemList) {
                    completedItem = new MBOOrderCompleted.CompletedItem();
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
            response = orderCompleted(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(),
                    message.getB2BOrderNo(), completedItemList, message.getUpdaterId(), message.getUpdateDt());
        } else if (message.getStatus() == B2BOrderStatusEnum.CANCELED.value) {
            response = orderCancelled(message.getMessageId(), message.getDataSource(), message.getB2BOrderId(), message.getB2BOrderNo(),
                    message.getUpdaterName(), message.getEffectiveDt(), message.getRemarks(), message.getUpdaterId(), message.getUpdateDt());
        } else {
            response = new MSResponse(MSErrorCode.SUCCESS);
            String msgJson = new JsonFormat().printToString(message);
            LogUtils.saveLog("B2B工单状态变更消息的状态错误", "MBOOrderService.sendOrderStatusUpdateCommandToB2B", msgJson, null, null);
        }
        return response;
    }

    /**
     * 派单
     */
    private MSResponse orderPlanned(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String engineerName, String engineerMobile, long createById, long createDt) {
        MBOOrderPlanned planned = new MBOOrderPlanned();
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
        return mboOrderFeign.orderPlanned(planned);
    }

    /**
     * 预约
     */
    private MSResponse orderAppointed(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String operator, Long appointmentDt, String remark, long createById, long createDt) {
        MBOOrderAppointed appointed = new MBOOrderAppointed();
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
        return mboOrderFeign.orderAppointed(appointed);
    }

    /**
     * 完成
     */
    private MSResponse orderCompleted(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, List<MBOOrderCompleted.CompletedItem> completedItems, long createById, long createDt) {
        MBOOrderCompleted completed = new MBOOrderCompleted();
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
        return mboOrderFeign.orderCompleted(completed);
    }

    /**
     * 取消
     */
    private MSResponse orderCancelled(Long messageId, Integer dataSource, Long b2bOrderId, String b2bOrderNo, String operator, Long cancelDt, String remark, long createById, long createDt) {
        MBOOrderCancelled cancelled = new MBOOrderCancelled();
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
        return mboOrderFeign.orderCancelled(cancelled);
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
     * 创建派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(Integer dataSourceId, Long servicePointId, String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder;
        if (StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建预约请求对象
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
     * 创建完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemDao.getOrderItems(quarter, orderId);
                if (order != null) {
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
     * 创建取消请求对象
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
        MBOOrderLog log = new MBOOrderLog();
        log.setUniqueId(message.getId());
        log.setOrderId(message.getOrderId());
        log.setLogAction(message.getLogTitle());
        log.setLogContent(message.getLogContext());
        log.setCreateById(message.getCreateById());
        log.setDataSource(message.getDataSourceId());
        return mboOrderFeign.saveLog(log);
    }

    /**
     * 创建云米日志消息实体
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
