package com.wolfking.jeesite.modules.fi.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServiceType;

import java.util.Date;

/**
 * Created by Jeff on 2017/4/14.
 */
public class EngineerChargeMaster extends LongIDDataEntity<EngineerChargeMaster> {
    private Long orderId;
    private String orderNo;
    private Long customerId;
    private ServicePoint servicePoint;
    private Double serviceCharge;
    private Double expressCharge;
    private Double travelCharge;
    private Double materialCharge;
    private Double insuranceCharge = 0.0;
    private Double customerTimeLinessCharge = 0.0;
    private Double timeLinessCharge = 0.0;
    private Double urgentCharge = 0.0;
    private Double praiseFee = 0.0;
    private Double taxFee = 0.0;
    private Double infoFee = 0.0;
    private Double otherCharge;
    private String insuranceNo = "";
    private Integer paymentType;
    private Integer status;
    private Integer chargeOrderType;
    private Double taxFeeRate = 0.0;
    private Double infoFeeRate = 0.0;
    private Date invoiceDate;
    private String quarter;

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
    
    public Long getCustomerId() { return customerId; }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
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

    public Double getInsuranceCharge() {
        return insuranceCharge;
    }

    public void setInsuranceCharge(Double insuranceCharge) {
        this.insuranceCharge = insuranceCharge;
    }

    public Double getCustomerTimeLinessCharge() {
        return customerTimeLinessCharge;
    }

    public void setCustomerTimeLinessCharge(Double customerTimeLinessCharge) {
        this.customerTimeLinessCharge = customerTimeLinessCharge;
    }

    public Double getTimeLinessCharge() {
        return timeLinessCharge;
    }

    public void setTimeLinessCharge(Double timeLinessCharge) {
        this.timeLinessCharge = timeLinessCharge;
    }

    public Double getUrgentCharge() {
        return urgentCharge;
    }

    public void setUrgentCharge(Double urgentCharge) {
        this.urgentCharge = urgentCharge;
    }

    public Double getPraiseFee() { return praiseFee; }

    public void setPraiseFee(Double praiseFee) { this.praiseFee = praiseFee; }

    public Double getTaxFee() { return taxFee; }

    public void setTaxFee(Double taxFee) { this.taxFee = taxFee; }

    public Double getInfoFee() { return infoFee; }

    public void setInfoFee(Double infoFee) { this.infoFee = infoFee; }

    public Double getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(Double otherCharge) {
        this.otherCharge = otherCharge;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
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

    public Integer getChargeOrderType() { return chargeOrderType; }

    public void setChargeOrderType(Integer chargeOrderType) { this.chargeOrderType = chargeOrderType; }

    public Double getTaxFeeRate() { return taxFeeRate; }

    public void setTaxFeeRate(Double taxFeeRate) { this.taxFeeRate = taxFeeRate; }

    public Double getInfoFeeRate() { return infoFeeRate; }

    public void setInfoFeeRate(Double infoFeeRate) { this.infoFeeRate = infoFeeRate; }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
