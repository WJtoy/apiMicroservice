package com.wolfking.jeesite.modules.api.entity.sd.request;

/**
 * 催单明细请求参数
 */
public class RestGetReminderRequest {

    private Long orderId = 0L;
    private String quarter = "";

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
