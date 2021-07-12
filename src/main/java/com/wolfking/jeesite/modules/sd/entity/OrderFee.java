package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderFeeAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单费用
 */
@JsonAdapter(OrderFeeAdapter.class)
public class OrderFee implements Serializable {

    public OrderFee(){}

    public OrderFee(Long orderId){
        this.orderId = orderId;
    }

    private Long orderId;
    private String quarter = "";//数据库分片，与订单相同
    private Double expectCharge = 0.00;// 订单预付金额(派单价)
    private Double blockedCharge = 0.00;// 冻结金额 合计每个产品冻结费用
    private Integer rebateFlag =0; //订单是否已经返点,0:未返点,1已返点

    // 客户
    private Dict orderPaymentType;// 客户结算方式(维护自客户)
    private Double serviceCharge = 0.00;// 服务费(应收)
    private Double materialCharge = 0.00;// 厂商配件費用(应收)
    //private Double dismantleCharge = 0.00;// 拆机费(应收) ?
    private Double expressCharge = 0.00;//快递费(应付)
    private Double travelCharge = 0.00;// 远程费(应收)
    private Double otherCharge = 0.00;// 其他費用(应收)
    private Double customerTimeLiness = 0.00; //客户时效(计时)(单位：小时) 18/06/04 因客户时效与安维时效统计方法不同
    private Double customerTimeLinessCharge = 0.00; //客户时效补贴(应收) 18/06/02
    private Double customerUrgentCharge= 0.0; //客户加急费(应收)
    private Double praiseFee = 0.0;//好评费(应收)
    // 合计订单金额(应收) = serviceCharge + materialCharge + expressCharge + travelCharge
    // + otherCharge + customerTimeLinessCharge + customerUrgentCharge
    // + praiseFee(2020-04-06)
    private Double orderCharge = 0.00;
    //派单时写入
    private Double customerPlanTravelCharge=0.00;// 客服派单预设的厂商远程费
    private Double customerPlanOtherCharge = 0.00;// 客服派单预设的厂商其他费用 2019/03/16

    // 安维
    private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
    private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
    private Double engineerExpressCharge = 0.00;//快递费(应付)
    //private Double engineerDismantleCharge = 0.00;// 拆机费(应付) ?
    private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
    private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
    private Double insuranceCharge = 0.00;//网点保险费(有保险为负数) 2018/05/14
    private Double timeLinessCharge = 0.00;//网点时效奖金(快可立补贴) 2018/05/17
    private Double subsidyTimeLinessCharge = 0.00; //时效费(客户补贴) 2018/06/08
    private Double engineerUrgentCharge = 0.00; //网点加急费(应付) 2018/06/07
    private Double engineerPraiseFee = 0.00; //好评费(应付)  2020-04-06
    private Double engineerTaxFee = 0.00;//扣点(应付 减项)  2020-04-06
    private Double engineerInfoFee = 0.00;//平台服务费(应付 减项)  2020-04-06

    private Double engineerTotalCharge = 0.00;// 安维总金额(应付)

    private Dict engineerPaymentType;// 安维结算方式
    //派单时写入
    private Double planTravelCharge=0.00;// 客服派单预设的安维远程费
    private String planTravelNo = "";//派单时预设的安维远程费的审批单号
    private Double planOtherCharge=0.00;// 客服派单预设的其它费用 18/01/25
    private Double planDistance = 0.00;//派单时预设的上门距离 18/01/25


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @DecimalMin(value = "0.0",message = "预付金额不能小于0")
    public Double getExpectCharge() {
        return expectCharge;
    }

    public void setExpectCharge(Double expectCharge) {
        this.expectCharge = expectCharge;
    }

    @DecimalMin(value = "0.0",message = "冻结金额不能小于0")
    public Double getBlockedCharge() {
        return blockedCharge;
    }

    public void setBlockedCharge(Double blockedCharge) {
        this.blockedCharge = blockedCharge;
    }

    @Range(min = 0,max = 1,message = "返点标识超出范围")
    public Integer getRebateFlag() {
        return rebateFlag;
    }

    public void setRebateFlag(Integer rebateFlag) {
        this.rebateFlag = rebateFlag;
    }

    @NotNull(message = "客户结算方式不能为空")
    public Dict getOrderPaymentType() {
        return orderPaymentType;
    }

    public void setOrderPaymentType(Dict orderPaymentType) {
        this.orderPaymentType = orderPaymentType;
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

    @DecimalMin(value = "0.0",message = "合计订单金额不能小于0")
    public Double getOrderCharge() {
        return orderCharge;
    }

    public void setOrderCharge(Double orderCharge) {
        this.orderCharge = orderCharge;
    }

    @DecimalMin(value = "0.0",message = "厂商远程费不能小于0")
    public Double getCustomerPlanTravelCharge() {
        return customerPlanTravelCharge;
    }

    public void setCustomerPlanTravelCharge(Double customerPlanTravelCharge) {
        this.customerPlanTravelCharge = customerPlanTravelCharge;
    }

    @DecimalMin(value = "0.0",message = "安维服务费不能小于0")
    public Double getEngineerServiceCharge() {
        return engineerServiceCharge;
    }

    public void setEngineerServiceCharge(Double engineerServiceCharge) {
        this.engineerServiceCharge = engineerServiceCharge;
    }

    @DecimalMin(value = "0.0",message = "安维远程费不能小于0")
    public Double getEngineerTravelCharge() {
        return engineerTravelCharge;
    }

    public void setEngineerTravelCharge(Double engineerTravelCharge) {
        this.engineerTravelCharge = engineerTravelCharge;
    }

    @DecimalMin(value = "0.0",message = "快递费不能小于0")
    public Double getEngineerExpressCharge() {
        return engineerExpressCharge;
    }

    public void setEngineerExpressCharge(Double engineerExpressCharge) {
        this.engineerExpressCharge = engineerExpressCharge;
    }

    @DecimalMin(value = "0.0",message = "安维配件费不能小于0")
    public Double getEngineerMaterialCharge() {
        return engineerMaterialCharge;
    }

    public void setEngineerMaterialCharge(Double engineerMaterialCharge) {
        this.engineerMaterialCharge = engineerMaterialCharge;
    }

    @DecimalMin(value = "0.0",message = "安维其它费用不能小于0")
    public Double getEngineerOtherCharge() {
        return engineerOtherCharge;
    }

    public void setEngineerOtherCharge(Double engineerOtherCharge) {
        this.engineerOtherCharge = engineerOtherCharge;
    }

    @DecimalMin(value = "0.0",message = "安维总金额不能小于0")
    public Double getEngineerTotalCharge() {
        return engineerTotalCharge;
    }

    public void setEngineerTotalCharge(Double engineerTotalCharge) {
        this.engineerTotalCharge = engineerTotalCharge;
    }

    public Dict getEngineerPaymentType() {
        return engineerPaymentType;
    }

    public void setEngineerPaymentType(Dict engineerPaymentType) {
        this.engineerPaymentType = engineerPaymentType;
    }

    @DecimalMin(value = "0.0",message = "远程费不能小于0")
    public Double getPlanTravelCharge() {
        return planTravelCharge;
    }

    public void setPlanTravelCharge(Double planTravelCharge) {
        this.planTravelCharge = planTravelCharge;
    }

    @Length(max = 20,message = "远程审批单号长度不能超过20位")
    public String getPlanTravelNo() {
        return planTravelNo;
    }

    public void setPlanTravelNo(String planTravelNo) {
        this.planTravelNo = planTravelNo;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Double getPlanOtherCharge() {
        return planOtherCharge;
    }

    public void setPlanOtherCharge(Double planOtherCharge) {
        this.planOtherCharge = planOtherCharge;
    }

    public Double getPlanDistance() {
        return planDistance;
    }

    public void setPlanDistance(Double planDistance) {
        this.planDistance = planDistance;
    }

    public Double getInsuranceCharge() {
        return insuranceCharge;
    }

    public void setInsuranceCharge(Double insuranceCharge) {
        this.insuranceCharge = insuranceCharge;
    }

    public Double getTimeLinessCharge() {
        return timeLinessCharge;
    }

    public void setTimeLinessCharge(Double timeLinessCharge) {
        this.timeLinessCharge = timeLinessCharge;
    }

    public Double getCustomerTimeLinessCharge() {
        return customerTimeLinessCharge;
    }

    public void setCustomerTimeLinessCharge(Double customerTimeLinessCharge) {
        this.customerTimeLinessCharge = customerTimeLinessCharge;
    }

    public Double getCustomerTimeLiness() {
        return customerTimeLiness;
    }

    public void setCustomerTimeLiness(Double customerTimeLiness) {
        this.customerTimeLiness = customerTimeLiness;
    }

    public Double getCustomerUrgentCharge() {
        return customerUrgentCharge;
    }

    public void setCustomerUrgentCharge(Double customerUrgentCharge) {
        this.customerUrgentCharge = customerUrgentCharge;
    }

    public Double getEngineerUrgentCharge() {
        return engineerUrgentCharge;
    }

    public void setEngineerUrgentCharge(Double engineerUrgentCharge) {
        this.engineerUrgentCharge = engineerUrgentCharge;
    }

    public Double getSubsidyTimeLinessCharge() {
        return subsidyTimeLinessCharge;
    }

    public void setSubsidyTimeLinessCharge(Double subsidyTimeLinessCharge) {
        this.subsidyTimeLinessCharge = subsidyTimeLinessCharge;
    }

    public Double getCustomerPlanOtherCharge() {
        return customerPlanOtherCharge;
    }

    public void setCustomerPlanOtherCharge(Double customerPlanOtherCharge) {
        this.customerPlanOtherCharge = customerPlanOtherCharge;
    }

    public Double getPraiseFee() {
        return praiseFee;
    }

    public void setPraiseFee(Double praiseFee) {
        this.praiseFee = praiseFee;
    }

    public Double getEngineerPraiseFee() {
        return engineerPraiseFee;
    }

    public void setEngineerPraiseFee(Double engineerPraiseFee) {
        this.engineerPraiseFee = engineerPraiseFee;
    }

    public Double getEngineerTaxFee() {
        return engineerTaxFee;
    }

    public void setEngineerTaxFee(Double engineerTaxFee) {
        this.engineerTaxFee = engineerTaxFee;
    }

    public Double getEngineerInfoFee() {
        return engineerInfoFee;
    }

    public void setEngineerInfoFee(Double engineerInfoFee) {
        this.engineerInfoFee = engineerInfoFee;
    }
}

