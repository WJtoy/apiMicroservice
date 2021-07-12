package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderGrabAdapter;

import java.util.Date;
import java.util.List;

/**
 * 抢单显示内容
 */
@JsonAdapter(RestOrderGrabAdapter.class)
public class RestOrderGrab {

    // Fields
    private int dataSource; // 数据源
    private Long orderId;
    private String quarter;//数据库分片
    private String orderNo = "";// 订单号
    private String userName= "";//用户姓名
    private String servicePhone = "";//电话
    private String serviceAddress = "";//地址
    private Date approveDate; //订单审核日期,发布日期
    private String description = ""; //服务描述
    private String remarks = "";//备注
    private Integer orderServiceType = 1;//工单类型 1：安装单 2:维修单
    private String orderServiceTypeName = "";//服务类型
    private String areaId;//区域id
    private int isComplained = 0;//投诉标识 18/01/24
    private int reminderFlag = 0; //催单标识 0：无催单 19/07/09

    private List<RestOrderItem> items = Lists.newArrayList();

    public RestOrderGrab(){	}

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public RestOrderGrab(Long orderId)
    {
        this.orderId = orderId;
    }

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


    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Integer getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(Integer orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public String getOrderServiceTypeName() {
        return orderServiceTypeName;
    }

    public void setOrderServiceTypeName(String orderServiceTypeName) {
        this.orderServiceTypeName = orderServiceTypeName;
    }



    public List<RestOrderItem> getItems() {
        return items;
    }

    public void setItems(List<RestOrderItem> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getIsComplained() {
        return isComplained;
    }

    public void setIsComplained(int isComplained) {
        this.isComplained = isComplained;
    }

    public int getReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
    }
}
