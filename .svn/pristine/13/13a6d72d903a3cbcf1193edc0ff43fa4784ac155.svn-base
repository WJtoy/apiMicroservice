package com.wolfking.jeesite.modules.api.entity.sd;


import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestMaterialMasterAdapter;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;

import java.util.List;

/**
 * 配件申请单(for list)
 */
@JsonAdapter(RestMaterialMasterAdapter.class)
public class RestMaterialMaster {

    private String id;
    private String quarter = "";//数据库分片，与订单相同
    private String orderId;
    private String orderdetailId;
    private String materialTypeValue;//配件类型
    private String materialType;//配件类型：1:配件 2:返件
    private String applytype;//类型：1:向师傅购买(自购) 2:厂家寄发
    private String applytypeValue;//类型：1:向师傅购买(自购) 2:厂家寄发
    private String status;
    private String statusName;
    private String expresscompany;
    private String expressno;
    private String remarks;
    private double totalprice;
    private String details;
    private Long createDate;
    private Integer returnFlag; //有无返件标识 1:有返件
    private Integer recycleFlag;//有无回收标识 1：有回收
    private List<MaterialItem> items = Lists.newArrayList();//项目
    private List<MaterialAttachment> photos = Lists.newArrayList();//图片

    public RestMaterialMaster(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderdetailId() {
        return orderdetailId;
    }

    public void setOrderdetailId(String orderdetailId) {
        this.orderdetailId = orderdetailId;
    }

    public String getApplytype() {
        return applytype;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getExpresscompany() {
        return expresscompany;
    }

    public void setExpresscompany(String expresscompany) {
        this.expresscompany = expresscompany;
    }

    public String getExpressno() {
        return expressno;
    }

    public void setExpressno(String expressno) {
        this.expressno = expressno;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Integer getRecycleFlag() {
        return recycleFlag;
    }

    public void setRecycleFlag(Integer recycleFlag) {
        this.recycleFlag = recycleFlag;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public List<MaterialItem> getItems() {
        return items;
    }

    public void setItems(List<MaterialItem> items) {
        this.items = items;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Integer getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(Integer returnFlag) {
        this.returnFlag = returnFlag;
    }

    public List<MaterialAttachment> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MaterialAttachment> photos) {
        this.photos = photos;
    }


    public String getMaterialTypeValue() {
        return materialTypeValue;
    }

    public void setMaterialTypeValue(String materialTypeValue) {
        this.materialTypeValue = materialTypeValue;
    }

    public String getApplytypeValue() {
        return applytypeValue;
    }

    public void setApplytypeValue(String applytypeValue) {
        this.applytypeValue = applytypeValue;
    }
}
