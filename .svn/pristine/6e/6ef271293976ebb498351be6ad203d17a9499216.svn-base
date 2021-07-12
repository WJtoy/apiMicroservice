package com.wolfking.jeesite.ms.um.sd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderServiceItem;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderStatusEnum;
import com.kkl.kklplus.entity.um.sd.UmOrderStatusUpdate;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.um.sd.feign.UmOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UMOrderService {

    @Autowired
    private UmOrderFeign umOrderFeign;

    @Autowired
    private OrderCacheReadService orderCacheReadService;


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
                    .setDataSourceId(B2BDataSourceEnum.UM.id);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    //endregion 创建工单日志消息实体


    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createChargeRequestEntity(Long kklOrderId, String kklQuarter, Long chargeAt) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        try {
            Order order = orderCacheReadService.getOrderById(kklOrderId, kklQuarter, OrderUtils.OrderDataLevel.DETAIL, true, true);
            if (order != null) {
                double customerTotalCharge = 0.0;
                List<B2BOrderServiceItem> serviceItems = Lists.newArrayList();
                if (order.getDetailList() != null && order.getDetailList().size() > 0) {

                    B2BOrderServiceItem serviceItem;
                    for (OrderDetail detail : order.getDetailList()) {
                        serviceItem = new B2BOrderServiceItem();
                        serviceItem.setServiceItemId(detail.getId() != null ? detail.getId() : 0);
                        serviceItem.setServiceAt(detail.getCreateDate() != null ? detail.getCreateDate().getTime() : 0);
                        serviceItem.setProductId(detail.getProductId() != null ? detail.getProductId() : 0);
                        serviceItem.setServiceTypeId(detail.getServiceType() != null && detail.getServiceType().getId() != null ? detail.getServiceType().getId() : 0);
                        serviceItem.setQty(detail.getQty());
                        serviceItem.setCharge(detail.getCustomerCharge() != null ? detail.getCustomerCharge() : 0.0);
                        customerTotalCharge = customerTotalCharge + serviceItem.getCharge();
                        serviceItems.add(serviceItem);
                    }
                }
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setServiceItems(serviceItems)
                        .setCustomerTotalCharge(customerTotalCharge)
                        .setChargeAt(chargeAt);
                result.setAElement(true);
                result.setBElement(builder);
            }
        } catch (Exception e) {
            LogUtils.saveLog("创建B2B操作请求实体", "UMOrderService.createChargeRequestEntity", "kklOrderId: " + kklOrderId + ";" + "创建优盟对账请求实体失败.", e, null);
        }
        return result;
    }

    /**
     * 更新优盟微服务数据库中的工单状态
     */
    public MSResponse updateOrderStatus(Long kklOrderId, B2BOrderStatusEnum statusEnum, Long closeDt) {
        int status = UmOrderStatusUpdate.STATUS_NEW;
        switch (statusEnum) {
            case NEW:
                status = UmOrderStatusUpdate.STATUS_NEW;
                break;
            case PLANNED:
                status = UmOrderStatusUpdate.STATUS_PLANNED;
                break;
            case APPOINTED:
                status = UmOrderStatusUpdate.STATUS_APPOINTED;
                break;
            case SERVICED:
                status = UmOrderStatusUpdate.STATUS_SERVICED;
                break;
            case COMPLETED:
                status = UmOrderStatusUpdate.STATUS_COMPLETED;
                break;
            case CANCELED:
                status = UmOrderStatusUpdate.STATUS_CANCELLED;
                break;
        }
        UmOrderStatusUpdate params = new UmOrderStatusUpdate();
        params.setKklOrderId(kklOrderId);
        params.setOrderStatus(status);
        params.setCloseDate(closeDt);
        return umOrderFeign.statusUpdate(params);
    }


}
