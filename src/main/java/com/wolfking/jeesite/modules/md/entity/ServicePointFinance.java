package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointFinanceAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 安维商财务信息表
 * Created on 2017-04-16.
 */
@JsonAdapter(ServicePointFinanceAdapter.class)
public class ServicePointFinance implements Serializable {

    public ServicePointFinance(){}

    private Long id;
    private Dict paymentType;    //结算方式 PaymentType
    private Dict bank;            //开户行
    private String branch = "";          //分行
    private String bankNo = "";          //账号
    private String bankOwner = "";       //开户人
    private Dict bankIssue;       //付款失败描述
    private Dict status;         //网点状态，当存在付款失败描述时，网点的状态必须变更成异常(2)

    //现金余额 - 结账的时候增加，提现的时候减少
    private double balance=0;
    //即结现金余额(结账的时候增加，提现的时候减少)
    private double dailyBalance=0.00;
    //提现总金额 - 师傅从该平台一共收入多少总金额
    private double totalAmount = 0;
    //平台服务费总金额
    private double platformFee = 0;
    //待支付金额 - 在订单完成的时候加总,结账的时候汇总三个特殊金额后转入到现金余额
    private double orderPaymentAmount = 0;
    //欠款金额 -预付或预支金额,结账的时候,待支付金额扣减该金额之后计算出现金余额
    private double debtsAmount = 0;
    private String debtsDescrption = ""; //欠款描述
    //待退金额 - 退补单待退的金额,结账的时候,待支付金额扣减该金额之后计算出现金余额
    private double refundAmount = 0;
    //待补金额 - 退补单待补的金额,结账的时候,待支付金额加总该金额之后计算出现金余额
    private double replenishAmount = 0;

    //开票标记，0:不需要开票，1:需要开票
    private Integer invoiceFlag = 0;

    //是否扣点
    private Integer discountFlag = 0;
    //扣点(小数，如0.01代表百分之一)
    private double discount = 0.00;

    private Dict unit = new Dict("RMB","人民币");//单位

    //最后付款日期
    private Date lastPayDate;
    //最后付款金额
    private double lastPayAmount = 0;

    //辅助用
    private double serviceCharge;//服务费
    private double expressCharge;//快递费
    private double travelCharge;//远程费
    private double materialCharge;//配件费
    private double taxFee = 0;//扣点
    private double infoFee = 0;//信息费-平台费改为入帐时启用
    private double otherCharge;//其他费用
    private double deposit= 0;//总质保金(完工+充值)
    private double depositRecharge = 0; //充值质保金
    //待付款金额
    private double payableAmount=0.00;
    private Integer payableYear;
    private Integer payableMonth;
    private String  payableMonthField;             //月份栏位名称
    private double insuranceAmount; //购买保险总金额
    //抵扣逻辑
    private double minusAmount=0.00; //应付值为负值
    private double deductedAmount=0.00; //已经抵扣款


    public double getDepositRecharge() {
        return depositRecharge;
    }

    public void setDepositRecharge(double depositRecharge) {
        this.depositRecharge = depositRecharge;
    }
    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(message = "结算方式不能为空")
    public Dict getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Dict paymentType) {
        this.paymentType = paymentType;
    }

    public Dict getBank() {
        return bank;
    }

    public void setBank(Dict bank) {
        this.bank = bank;
    }

    @Length(min = 1,max = 50,message = "开户行不能为空，且长度不能超过50")
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Length(min = 1,max = 50,message = "银行帐号不能为空，且长度不能超过50")
    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Length(min = 1,max = 50,message = "开户人不能为空，且长度不能超过50")
    public String getBankOwner() {
        return bankOwner;
    }

    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner;
    }

    public Dict getBankIssue() {
        return bankIssue;
    }

    public void setBankIssue(Dict bankIssue) {
        this.bankIssue = bankIssue;
    }

    public Dict getStatus() {
        return status;
    }

    public void setStatus(Dict status) {
        this.status = status;
    }

    @DecimalMin(value = "0.0",message = "现金余额不能小于0")
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDailyBalance() {
        return dailyBalance;
    }

    public void setDailyBalance(double dailyBalance) {
        this.dailyBalance = dailyBalance;
    }

    @DecimalMin(value = "0.0",message = "提现总金额不能小于0")
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(double platformFee) {
        this.platformFee = platformFee;
    }

    public double getOrderPaymentAmount() {
        return orderPaymentAmount;
    }

    public void setOrderPaymentAmount(double orderPaymentAmount) {
        this.orderPaymentAmount = orderPaymentAmount;
    }

    @DecimalMin(value = "0.0",message = "欠款金额不能小于0")
    public double getDebtsAmount() {
        return debtsAmount;
    }

    public void setDebtsAmount(double debtsAmount) {
        this.debtsAmount = debtsAmount;
    }

    @DecimalMin(value = "0.0",message = "待退金额不能小于0")
    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    @DecimalMin(value = "0.0",message = "待补金额不能小于0")
    public double getReplenishAmount() {
        return replenishAmount;
    }

    public void setReplenishAmount(double replenishAmount) {
        this.replenishAmount = replenishAmount;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Integer invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    @NotNull(message = "币别不能为空")
    public Dict getUnit() {
        return unit;
    }

    public void setUnit(Dict unit) {
        this.unit = unit;
    }
    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getExpressCharge() {
        return expressCharge;
    }

    public void setExpressCharge(double expressCharge) {
        this.expressCharge = expressCharge;
    }

    public double getTravelCharge() {
        return travelCharge;
    }

    public void setTravelCharge(double travelCharge) {
        this.travelCharge = travelCharge;
    }

    public double getMaterialCharge() {
        return materialCharge;
    }

    public void setMaterialCharge(double materialCharge) {
        this.materialCharge = materialCharge;
    }

    public double getTaxFee() { return taxFee; }

    public void setTaxFee(double taxFee) { this.taxFee = taxFee; }

    public double getInfoFee() { return infoFee; }

    public void setInfoFee(double infoFee) { this.infoFee = infoFee; }

    public double getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(double otherCharge) {
        this.otherCharge = otherCharge;
    }

    public String getDebtsDescrption() {
        return debtsDescrption;
    }

    public void setDebtsDescrption(String debtsDescrption) {
        this.debtsDescrption = debtsDescrption;
    }

    public Date getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public double getLastPayAmount() {
        return lastPayAmount;
    }

    public void setLastPayAmount(double lastPayAmount) {
        this.lastPayAmount = lastPayAmount;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Integer getPayableYear() {
        return payableYear;
    }

    public void setPayableYear(Integer payableYear) {
        this.payableYear = payableYear;
    }

    public Integer getPayableMonth() {
        return payableMonth;
    }

    public void setPayableMonth(Integer payableMonth) {
        this.payableMonth = payableMonth;
        this.payableMonthField = "m".concat(payableMonth.toString());
    }

    public String getPayableMonthField() {
        return payableMonthField;
    }

    public double getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    @DecimalMin(value = "0.0",message = "扣点不能小于0")
    @DecimalMax(value = "100",message = "扣点不能超过100")
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getMinusAmount() { return this.minusAmount; }

    public void setMinusAmount(double minusAmount){
        this.minusAmount = minusAmount;
    }

    public double getDeductedAmount() { return this.deductedAmount; }

    public void setDeductedAmount(double deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Integer getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(Integer discountFlag) {
        this.discountFlag = discountFlag;
    }
}
