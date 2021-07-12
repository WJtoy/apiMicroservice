package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Date;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
public class CustomerFinance extends LongIDDataEntity<CustomerFinance>
{
    private static final long serialVersionUID = 1L;
    public static final int YJ = 10;//月结
    public static final int JJ = 20;//即结
    public static final int YF = 30;//预付

    // Fields
    //private int paymentType=0;        //支付类型
    private Dict paymentType;        //支付类型
    private String rebateRate;      //返点率
    private String level;           //等级
    private int lockFlag;           //客户结账锁，当客户在结账的时候加锁，此时不能下单
    private String publicBank;      //对公开户行
    private String publicAccount;   //对公账户
    private String publicName;      //对公户名
    private String publicBranch;    //开户分行
    private String privateBank;     //对私开户行
    private String privateAccount;  //对私账户
    private String privateName;     //对私户名
    private String privateBranch;   //对私开户分行
    private Double credit=0.00;          //信用额度
    private int creditFlag = 1;          //是否使用信用额度
    private Double blockAmount=0.00;     //冻结金额（订单产生时从订单的下单金额+抵押金额汇总，结账的时候把冻结金额归零）
    private Double ordePaymentAmount=0.00;//待支付金额(生成对账单的时候从订单的实际订单金额汇总，结账的时候汇总待支付金额，待退金额，待补金额之后计算出账户当前余额的扣减金额）
    private Double refundAmount=0.00;     //退补单待退金额
    private Double replenishAmount=0.00;     //退补单待补金额
    private Double balance=0.00;             //账户当前余额(结账的时候，汇总待支付金额，待退金额，待补金额进行扣减)
    private Double prepaidAmount=0.00;       //充值金额（已充值总金额）
    private Double totalAmount=0.00;         //支付总金额(当前余额扣减的时候增加)
    private int unit=0;                   //单位
    private String taxpayerCode;        //纳税人编码
    private Double deposit=0.00;             //押金
    private Double transactionAmount = 0d; //异动金额，用于更新balance时的辅助属性
    //开票标记，0:不需要开票，1:需要开票
    private Integer invoiceFlag = 0;

    public String getRebateRate() {
        return rebateRate;
    }

    public void setRebateRate(String rebateRate) {
        this.rebateRate = rebateRate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getLockFlag() {

        return lockFlag;
    }

    public void setLockFlag(int lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getPublicBank() {

        return publicBank;
    }

    public void setPublicBank(String publicBank) {
        this.publicBank = publicBank;
    }

    public String getPublicAccount() {

        return publicAccount;
    }

    public void setPublicAccount(String publicAccount) {
        this.publicAccount = publicAccount;
    }

    public String getPublicName() {

        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getPublicBranch() {

        return publicBranch;
    }

    public void setPublicBranch(String publicBranch) {
        this.publicBranch = publicBranch;
    }

    public String getPrivateBank() {

        return privateBank;
    }

    public void setPrivateBank(String privateBank) {
        this.privateBank = privateBank;
    }

    public String getPrivateAccount() {

        return privateAccount;
    }

    public void setPrivateAccount(String privateAccount) {
        this.privateAccount = privateAccount;
    }

    public String getPrivateName() {

        return privateName;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }

    public String getPrivateBranch() {

        return privateBranch;
    }

    public void setPrivateBranch(String privateBranch) {
        this.privateBranch = privateBranch;
    }

    public Double getCredit() {

        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public int getCreditFlag() {

        return creditFlag;
    }

    public void setCreditFlag(int creditFlag) {
        this.creditFlag = creditFlag;
    }

    public Double getBlockAmount() {

        return blockAmount;
    }

    public void setBlockAmount(Double blockAmount) {
        this.blockAmount = blockAmount;
    }


    public Double getOrdePaymentAmount() {
        return ordePaymentAmount;
    }

    public void setOrdePaymentAmount(Double ordePaymentAmount) {
        this.ordePaymentAmount = ordePaymentAmount;
    }

    public Double getRefundAmount() {

        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Double getReplenishAmount() {

        return replenishAmount;
    }

    public void setReplenishAmount(Double replenishAmount) {
        this.replenishAmount = replenishAmount;
    }

    public Double getPrepaidAmount() {

        return prepaidAmount;
    }

    public void setPrepaidAmount(Double prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public Double getTotalAmount() {

        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getBalance() {

        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }



    public int getUnit() {

        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getTaxpayerCode() {

        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public Dict getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Dict paymentType) {
        this.paymentType = paymentType;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Integer invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }
}
