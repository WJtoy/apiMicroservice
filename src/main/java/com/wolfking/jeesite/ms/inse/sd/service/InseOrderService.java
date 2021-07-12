package com.wolfking.jeesite.ms.inse.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderProcessLogMessage;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.inse.sd.InseOrderRemark;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.inse.sd.feign.InseOrderFeign;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class InseOrderService extends B2BOrderManualBaseService {

    @Resource
    private OrderItemDao orderItemDao;

    @Autowired
    private InseOrderFeign inseOrderFeign;

    @Autowired
    OrderItemCompleteService orderItemCompleteService;


    //region 创建工单日志消息实体

    /**
     * 创建优盟工单日志消息实体
     */
    public TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> createOrderProcessLogReqEntity(OrderProcessLog log) {
        TwoTuple<Boolean, B2BOrderProcessLogReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (log.getCreateBy() != null && StringUtils.isNotBlank(log.getCreateBy().getName())
                && log.getCreateDate() != null && StringUtils.isNotBlank(log.getActionComment())) {
            B2BOrderProcessLogReqEntity.Builder builder = new B2BOrderProcessLogReqEntity.Builder();
            builder.setOperatorName(log.getCreateBy().getName())
                    .setLogDt(log.getCreateDate().getTime())
                    .setLogContext(log.getActionComment())
                    .setDataSourceId(B2BDataSourceEnum.INSE.id);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    /**
     * 往优盟微服务推送工单日志
     */
    public MSResponse pushOrderProcessLogToMS(MQB2BOrderProcessLogMessage.B2BOrderProcessLogMessage message) {
        InseOrderRemark orderProcessLog = new InseOrderRemark();
        orderProcessLog.setOrderId(message.getOrderId());
        orderProcessLog.setRemark(message.getLogContext());
        orderProcessLog.setCreateById(message.getCreateById());
        orderProcessLog.setCreateDt(message.getCreateDt());
        MSResponse response = inseOrderFeign.saveLog(orderProcessLog);
        return response;
    }

    //endregion 创建工单日志消息实体


    //region 其他

    private List<CanboOrderCompleted.CompletedItem> getInseOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
        List<CanboOrderCompleted.CompletedItem> list = Lists.newArrayList();
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
            Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);
//            for (OrderItem item : orderItems) {
//                if (StringUtils.isNotBlank(item.getB2bProductCode())) {
//                    if (b2bProductCodeMap.containsKey(item.getProductId())) {
//                        b2bProductCodeMap.get(item.getProductId()).add(item.getB2bProductCode());
//                    } else {
//                        b2bProductCodeMap.put(item.getProductId(), Lists.newArrayList(item.getB2bProductCode()));
//                    }
//                }
//            }
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            Map<Long, List<OrderItemComplete>> itemCompleteMap = Maps.newHashMap();
            for (OrderItemComplete item : completeList) {
                if (itemCompleteMap.containsKey(item.getProduct().getId())) {
                    itemCompleteMap.get(item.getProduct().getId()).add(item);
                } else {
                    itemCompleteMap.put(item.getProduct().getId(), Lists.newArrayList(item));
                }
            }

            CanboOrderCompleted.CompletedItem completedItem;
            for (Map.Entry<Long, List<String>> item : b2bProductCodeMap.entrySet()) {
                List<OrderItemComplete> itemCompletes = itemCompleteMap.get(item.getKey());
                for (int i = 0; i < item.getValue().size(); i++) {
                    completedItem = new CanboOrderCompleted.CompletedItem();
                    completedItem.setItemCode(item.getValue().get(i));

                    if (itemCompletes != null && itemCompletes.size() > i) {
                        OrderItemComplete itemComplete = itemCompletes.get(i);
                        List<ProductCompletePicItem> picItems = OrderUtils.fromProductCompletePicItemsJson(itemComplete.getPicJson());
                        if (picItems != null && !picItems.isEmpty()) {
                            completedItem.setPic1(OrderPicUtils.getOrderPicHostDir() + picItems.get(0).getUrl());
                        }
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
     * 创建樱雪派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(Long engineerId, String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (engineerId != null && StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerId(engineerId.toString())
                    .setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建樱雪预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建樱雪上门请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServiceRequestEntity() {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
        result.setAElement(true);
        result.setBElement(builder);
        return result;
    }

    /**
     * 创建樱雪完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createInseCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemDao.getOrderItems(quarter, orderId);
                if (order != null) {
                    //orderItems = OrderItemUtils.fromOrderItemsJson(order.getOrderItemJson());
                    orderItems = OrderItemUtils.pbToItems(order.getItemsPb());//2020-12-17 sd_order -> sd_order_head
                }
            }
            List<CanboOrderCompleted.CompletedItem> completedItems = getInseOrderCompletedItems(orderId, quarter, orderItems);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setCompletedItems(completedItems);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

}
