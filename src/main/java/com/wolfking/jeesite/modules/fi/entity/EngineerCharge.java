package com.wolfking.jeesite.modules.fi.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.*;

import java.util.Date;

/**
 * Created by Jeff on 2017/4/14.
 */
public class EngineerCharge extends LongIDDataEntity<EngineerCharge> {
    //对帐单状态
    public static final Integer EC_STATUS_NEW = 10;//未转存
    public static final Integer EC_STATUS_CLOSED = 20;//已转存
    //对帐单类型,0:原对帐单,1:退补单
    public static final Integer EC_TYPE_ORIGINAL = 0;
    public static final Integer EC_TYPE_WRITE_OFF = 1;

    private Long orderId;
    private String orderNo;
    private Long orderDetailId;
    private Long customerId;
    private ServicePoint servicePoint;
    private Engineer engineer;
    private Product product;
    private ServiceType serviceType;
    private Integer qty;
    private Double serviceCharge;
    private Double expressCharge;
    private Double travelCharge;
    private Double materialCharge;
    private Double otherCharge;
    private Integer serviceTimes;
    private Integer paymentType;
    private Integer status;
    private String statusName = ""; //切换为微服务
    private Integer chargeOrderType;
    private Double taxFeeRate = 0.0;
    private Double infoFeeRate = 0.0;
    private String quarter;
    private Date orderCloseDate;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public Engineer getEngineer() {
        return engineer;
    }

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getExpressCharge() {
        return expressCharge;
    }

    public void setExpressCharge(Double expressCharge) {
        this.expressCharge = expressCharge;
    }

    public Double getTravelCharge() {
        return travelCharge;
    }

    public void setTravelCharge(Double travelCharge) {
        this.travelCharge = travelCharge;
    }

    public Double getMaterialCharge() {
        return materialCharge;
    }

    public void setMaterialCharge(Double materialCharge) {
        this.materialCharge = materialCharge;
    }

    public Double getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(Double otherCharge) {
        this.otherCharge = otherCharge;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getChargeOrderType() {
        return chargeOrderType;
    }

    public void setChargeOrderType(Integer chargeOrderType) {
        this.chargeOrderType = chargeOrderType;
    }

    public Double getTaxFeeRate() { return taxFeeRate; }

    public void setTaxFeeRate(Double taxFeeRate) { this.taxFeeRate = taxFeeRate; }

    public Double getInfoFeeRate() { return infoFeeRate; }

    public void setInfoFeeRate(Double infoFeeRate) { this.infoFeeRate = infoFeeRate; }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Date getOrderCloseDate() {
        return orderCloseDate;
    }

    public void setOrderCloseDate(Date orderCloseDate) {
        this.orderCloseDate = orderCloseDate;
    }
}
