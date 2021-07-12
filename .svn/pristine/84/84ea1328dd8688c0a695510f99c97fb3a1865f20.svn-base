package com.wolfking.jeesite.modules.mq.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

import java.util.Date;

/**
 * APP确认完成，自动客评及对账队列
 * Created by Ryan on 2017/12/05.
 */
public class OrderAutoComplete extends LongIDDataEntity<OrderAutoComplete> {
    private Long orderId;           //订单id
    private String quarter;         //季度,分片根据,如20171
    private Integer retryTimes=0;     //重试次数，失败后再重试3次，超过3次视为失败
    private Integer status = 10;         //狀態，10：待处理，20：失败重试中，30：处理成功，40：处理失败
    private String description = "";     //备注
    private Long triggerBy;         //触发者
    private Date triggerDate;       //触发时间

    public OrderAutoComplete(){}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTriggerBy() {
        return triggerBy;
    }

    public void setTriggerBy(Long triggerBy) {
        this.triggerBy = triggerBy;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
