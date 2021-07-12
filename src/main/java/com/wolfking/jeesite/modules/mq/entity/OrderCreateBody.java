package com.wolfking.jeesite.modules.mq.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.util.Date;

/**
 * 下单
 * Created by Ryan on 2017/6/24.
 */
public class OrderCreateBody extends LongIDDataEntity<OrderCreateBody> {
    private Long orderId =0l;           //订单id
    private String quarter = "";         //季度,分片根据,如20171
    private Integer type = 1;      //订单类型 1:下单
    private Integer retryTimes = 0;     //重试次数，失败后再重试3次，超过3次视为失败
    private User triggerBy;     //触发者
    private Date triggerDate;       //触发时间

    private Integer status = 0;     // 狀態，10：待处理，20：失败重试中，30：处理成功，40：处理失败
    private String json = "";

    public OrderCreateBody(){}

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public User getTriggerBy() {
        return triggerBy;
    }

    public void setTriggerBy(User triggerBy) {
        this.triggerBy = triggerBy;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
