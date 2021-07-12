package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestServicePointOrderInfoAdapter;

import java.util.List;

/**
 * 网点订单信息类(For网点)
 */
@JsonAdapter(RestServicePointOrderInfoAdapter.class)
public class RestServicePointOrderInfo {

    // Fields
    private String userName= "";//用户姓名
    private String servicePhone = "";//电话
    private String serviceAddress = "";//地址
    private Long invoiceDate = 0l;//结算日期
    private Double estimatedServiceCost = 0.0;//预估服务费 18/01/24
    private int reminderFlag = 0; //催单标识 0：无催单 19/07/09
    private List<RestOrderDetail> services = Lists.newArrayList();

    public RestServicePointOrderInfo(){	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<RestOrderDetail> getServices() {
        return services;
    }

    public void setServices(List<RestOrderDetail> services) {
        this.services = services;
    }

    public Double getEstimatedServiceCost() {
        return estimatedServiceCost;
    }

    public void setEstimatedServiceCost(Double estimatedServiceCost) {
        this.estimatedServiceCost = estimatedServiceCost;
    }

    public int getReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
    }
}
