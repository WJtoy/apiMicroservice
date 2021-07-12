package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单网点费用汇总
 */
@Data
@NoArgsConstructor
public class OrderServicePointFee implements Serializable {

    public OrderServicePointFee(Long orderId,String quarter){
        this.orderId = orderId;
        this.quarter = quarter;
    }

    private String quarter = "";//数据库分片，与订单相同
    private Long orderId;
    @JsonAdapter(ServicePointSimpleAdapter.class)
    private ServicePoint servicePoint;//网点
    
    private Integer rebateFlag =0; //订单是否已经返点,0:未返点,1已返点
    //费用
    private Double serviceCharge = 0.00;// 服务费
    private Double materialCharge = 0.00;// 厂商配件費用
    private Double expressCharge = 0.00;//快递费(应付)
    private Double travelCharge = 0.00;// 远程费
    private Double otherCharge = 0.00;// 其他費用
    private Double insuranceCharge = 0.00;//保险费
    private String insuranceNo = "";//保险单号
    private Double timeLiness = 0.00;//时效(派单~客评的用时) 单位小时 //2018/05/17
    private Double timeLinessCharge = 0.00;//时效费
    private Double customerTimeLiness = 0.00;//客户时效(下单/到货日期~客评的用时) 单位小时 2018/06/06
    private Double customerTimeLinessCharge = 0.00;//时效费 2018/06/06
    private Double urgentCharge = 0.00;//加急费
    private Double praiseFee = 0.00; //好评费(应付)  2020-04-06
    private Double taxFee = 0.00;//扣点(应付 减项)  2020-04-06
    private Double iInfoFee = 0.00;//平台服务费(应付 减项)  2020-04-06

    private Double orderCharge = 0.00;// 合计,以上费用汇总
    private int delFlag = 1;//逻辑删除标记

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @DecimalMin(value = "0.0",message = "保险费不能小于0")
    public Double getInsuranceCharge() {
        return insuranceCharge;
    }

    public void setInsuranceCharge(Double insuranceCharge) {
        this.insuranceCharge = insuranceCharge;
    }

    @DecimalMin(value = "0.0",message = "时效费不能小于0")
    public Double getTimeLinessCharge() {
        return timeLinessCharge;
    }

    public void setTimeLinessCharge(Double timeLinessCharge) {
        this.timeLinessCharge = timeLinessCharge;
    }

    @Range(min = 0,max = 1,message = "返点标识超出范围")
    public Integer getRebateFlag() {
        return rebateFlag;
    }

    public void setRebateFlag(Integer rebateFlag) {
        this.rebateFlag = rebateFlag;
    }

    @DecimalMin(value = "0.0",message = "服务费不能小于0")
    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @DecimalMin(value = "0.0",message = "厂商配件费用不能小于0")
    public Double getMaterialCharge() {
        return materialCharge;
    }

    public void setMaterialCharge(Double materialCharge) {
        this.materialCharge = materialCharge;
    }

    @DecimalMin(value = "0.0",message = "快递费不能小于0")
    public Double getExpressCharge() {
        return expressCharge;
    }

    public void setExpressCharge(Double expressCharge) {
        this.expressCharge = expressCharge;
    }

    @DecimalMin(value = "0.0",message = "远程费不能小于0")
    public Double getTravelCharge() {
        return travelCharge;
    }

    public void setTravelCharge(Double travelCharge) {
        this.travelCharge = travelCharge;
    }

    @DecimalMin(value = "0.0",message = "其他费用不能小于0")
    public Double getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(Double otherCharge) {
        this.otherCharge = otherCharge;
    }

    @DecimalMin(value = "0.0",message = "合计费用不能小于0")
    public Double getOrderCharge() {
        return orderCharge;
    }

    public void setOrderCharge(Double orderCharge) {
        this.orderCharge = orderCharge;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    @NotNull(message = "请选择网点")
    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    @DecimalMin(value = "0.0",message = "加急费不能小于0")
    public Double getUrgentCharge() {
        return urgentCharge;
    }

    public void setUrgentCharge(Double urgentCharge) {
        this.urgentCharge = urgentCharge;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public Double getTimeLiness() {
        return timeLiness;
    }

    public void setTimeLiness(Double timeLiness) {
        this.timeLiness = timeLiness;
    }

    public Double getCustomerTimeLiness() {
        return customerTimeLiness;
    }

    public void setCustomerTimeLiness(Double customerTimeLiness) {
        this.customerTimeLiness = customerTimeLiness;
    }

    public Double getCustomerTimeLinessCharge() {
        return customerTimeLinessCharge;
    }

    public void setCustomerTimeLinessCharge(Double customerTimeLinessCharge) {
        this.customerTimeLinessCharge = customerTimeLinessCharge;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public Double getPraiseFee() {
        return praiseFee;
    }

    public void setPraiseFee(Double praiseFee) {
        this.praiseFee = praiseFee;
    }

    public Double getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(Double taxFee) {
        this.taxFee = taxFee;
    }

    public Double getiInfoFee() {
        return iInfoFee;
    }

    public void setiInfoFee(Double iInfoFee) {
        this.iInfoFee = iInfoFee;
    }
}

