package com.wolfking.jeesite.modules.mq.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.mq.dto.MQCustomer.Customer;
import com.wolfking.jeesite.modules.mq.dto.MQOrderReport.Kefu;

import java.util.Date;

/**
 * 报表统计数据消息
 * Created by Ryan on 2017/6/24.
 */
public class OrderReport extends LongIDDataEntity<OrderReport> {
    private Customer customer;      //客户
    private Long orderId =0l;           //订单id
    private Kefu kefu;              //客服
    private Integer orderType = 20;      //订单类型 20:下单 30：接单 40：派单 50：上门服务 80:完成 90：退单 100:取消
    private Integer retryTimes = 0;     //重试次数，失败后再重试3次，超过3次视为失败
    private Long provinceId = 0l;
    private String provinceName = "";
    private Long cityId=0l;
    private String cityName="";
    private Long areaId = 0l;
    private String areaName = "";
    private Long triggerBy = 0l;     //触发者
    private Date triggerDate;       //触发时间
    private String dataQuarter = "";         //季度,分片根据,如20171
    private Integer qty = 1;
    private Double amount = 0.00;
    private Integer status = 0;     // 狀態，10：待处理，20：失败重试中，30：处理成功，40：处理失败
    public OrderReport(){}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Kefu getKefu() {
        return kefu;
    }

    public void setKefu(Kefu kefu) {
        this.kefu = kefu;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDataQuarter() {
        return dataQuarter;
    }

    public void setDataQuarter(String dataQuarter) {
        this.dataQuarter = dataQuarter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
