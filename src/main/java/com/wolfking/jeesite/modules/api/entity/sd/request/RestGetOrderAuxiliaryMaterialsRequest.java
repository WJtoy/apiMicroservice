package com.wolfking.jeesite.modules.api.entity.sd.request;

public class RestGetOrderAuxiliaryMaterialsRequest {

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
