package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderStatusEnum;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class B2BOrderStatusUpdateFailureLog implements Serializable {

    private Integer dataSourceId;
    private Long orderId;
    private String b2bOrderNo;
    private B2BOrderStatusEnum status;
    private String remarks;
    private String engineerName;
    private String engineerMobile;
    private Long updaterId;
    private String updaterName;
    private String updaterMobile;
    private Date updateDate;
    private Long servicePointId;
    private Long engineerId;
    private Date effectiveDate;
    private String quarter;
    private Integer orderItemsLength;
    private String orderItemsProductIds;
    private Integer kklCancelType;

    public B2BOrderStatusUpdateFailureLog(Integer dataSourceId, Long orderId, User updater, Date updateDate, B2BOrderStatusEnum status) {
        this.dataSourceId = dataSourceId;
        this.orderId = orderId;
        setUpdater(updater);
        this.updateDate = updateDate;
        this.status = status;
    }

    public B2BOrderStatusUpdateFailureLog(Integer dataSourceId, String b2bOrderNo, Long engineerId, String engineerName, String engineerMobile, User updater, Date updateDate, B2BOrderStatusEnum status) {
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
        this.engineerId = engineerId;
        this.engineerName = engineerName;
        this.engineerMobile = engineerMobile;
        setUpdater(updater);
        this.updateDate = updateDate;
        this.status = status;
    }

    public B2BOrderStatusUpdateFailureLog(Integer dataSourceId, String b2bOrderNo, User updater,
                                          Long servicePointId, Long engineerId, Date updateDate, Date effectiveDate, String remarks, B2BOrderStatusEnum status) {
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
        setUpdater(updater);
        this.servicePointId = servicePointId;
        this.engineerId = engineerId;
        this.updateDate = updateDate;
        this.effectiveDate = effectiveDate;
        this.remarks = remarks;
        this.status = status;
    }

    public B2BOrderStatusUpdateFailureLog(Integer dataSourceId, String b2bOrderNo, Long servicePointId, Long engineerId, User updater, Date updateDate, B2BOrderStatusEnum status) {
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
        this.servicePointId = servicePointId;
        this.engineerId = engineerId;
        setUpdater(updater);
        this.updateDate = updateDate;
        this.status = status;
    }

    public B2BOrderStatusUpdateFailureLog(Long orderId, String quarter, List<OrderItem> orderItems, Integer dataSourceId, String b2bOrderNo,
                                          User updater, Date updateDate, Date effectiveDate, String remarks, B2BOrderStatusEnum status) {
        this.orderId = orderId;
        this.quarter = quarter;
        setOrderItems(orderItems);
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
        setUpdater(updater);
        this.updateDate = updateDate;
        this.effectiveDate = effectiveDate;
        this.remarks = remarks;
        this.status = status;
    }

    public B2BOrderStatusUpdateFailureLog(Integer dataSourceId, String b2bOrderNo, Integer kklCancelType, User updater,
                                          Date updateDate, Date effectiveDate, String remarks, B2BOrderStatusEnum status) {
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
        this.kklCancelType = kklCancelType;
        setUpdater(updater);
        this.updateDate = updateDate;
        this.effectiveDate = effectiveDate;
        this.remarks = remarks;
        this.status = status;
    }

    public void setUpdater(User updater) {
        if (updater != null) {
            this.updaterId = updater.getId();
            this.updaterName = updater.getName();
            this.updaterMobile = updater.getMobile();
        }
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        if (orderItems != null && !orderItems.isEmpty()) {
            this.orderItemsLength = orderItems.size();
            List<String> productIds = orderItems.stream().filter(i -> i.getProductId() != null).map(i -> i.getProductId().toString()).collect(Collectors.toList());
            if (!productIds.isEmpty()) {
                this.orderItemsProductIds = String.join(",", productIds);
            }
        }
    }
}
