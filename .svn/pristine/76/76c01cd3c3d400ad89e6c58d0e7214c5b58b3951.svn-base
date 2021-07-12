package com.wolfking.jeesite.modules.fi.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;

/**
 * Created by Jeff on 2017/4/14.
 */
public class EngineerCurrency extends LongIDDataEntity<EngineerCurrency> {

    public static final int PAYMENT_TYPE_CASH = 10;  //现金
    public static final int PAYMENT_TYPE_TRANSFER_ACCOUNT =20; //转账
    public static final int PAYMENT_TYPE_ORDER_INVOICE =30; //结帐转入
    public static final int PAYMENT_TYPE_WRITE_OFF =40; //退补

    public static final int CURRENCY_TYPE_IN    = 10;           //收入
    public static final int CURRENCY_TYPE_OUT   = 20;           //支出
    public static final int CURRENCY_TYPE_NONE  = 0;            //无

    public static final int ACTION_TYPE_NONE   = 0;//无
    public static final int ACTION_TYPE_PREPAY      = 10;//预付
    public static final int ACTION_TYPE_CHARGE      = 20;//安维结账
    public static final int ACTION_TYPE_REFUND      = 30;//退款
    public static final int ACTION_TYPE_REPLENISH   = 40;//补款
    public static final int ACTION_TYPE_PAY         = 50;//付款，提现后打款
    public static final int ACTION_TYPE_APPLY       = 60;//申请提现
    public static final int ACTION_TYPE_PAY_APPLY   = 70;//财务打款申请
    public static final int ACTION_TYPE_REFUND_REPLENISH = 3040; //退补，该值在查询条件中使用

    private ServicePoint servicePoint;
    private Integer currencyType;
    private String currencyNo;
    private Double beforeBalance;
    private Double balance;
    private Double amount;
    private Integer paymentType;
    private Integer actionType;
    private String actionTypeName;
    private String quarter;

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencyNo() {
        return currencyNo;
    }

    public void setCurrencyNo(String currencyNo) {
        this.currencyNo = currencyNo;
    }

    public Double getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(Double beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getActionTypeName() {
        return actionTypeName;
    }

    public void setActionTypeName(String actionTypeName) {
        this.actionTypeName = actionTypeName;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
