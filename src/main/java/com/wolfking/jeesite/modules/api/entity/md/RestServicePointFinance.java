package com.wolfking.jeesite.modules.api.entity.md;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.md.adapter.RestServicePointFinanceAdapter;

import java.util.Date;

@JsonAdapter(RestServicePointFinanceAdapter.class)
public class RestServicePointFinance {
    private RestDict paymentType;
    private RestDict bank;
    private String branch;
    private String bankNo;
    private String bankOwner;
    private Integer invoiceFlag;
    private Double balance;
    private Double totalAmount;
    private Date lastPayDate;
    private Double lastPayAmount;

    public RestDict getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(RestDict paymentType) {
        this.paymentType = paymentType;
    }

    public RestDict getBank() {
        return bank;
    }

    public void setBank(RestDict bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankOwner() {
        return bankOwner;
    }

    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Integer invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public Double getLastPayAmount() {
        return lastPayAmount;
    }

    public void setLastPayAmount(Double lastPayAmount) {
        this.lastPayAmount = lastPayAmount;
    }

}
