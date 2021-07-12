package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppPraisePicItem;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderDetailInfoAdapter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sd.entity.OrderAttachment;
import com.wolfking.jeesite.modules.sd.entity.OrderSuspendFlagEnum;
import com.wolfking.jeesite.modules.sd.entity.OrderSuspendTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单详情类
 */
@JsonAdapter(RestOrderDetailInfoAdapter.class)
public class RestOrderDetailInfo extends RestOrder {

    // Fields
    private Long customerId; //客户Id
    private String description = ""; //服务描述
    private Integer partsFlag = 0; //配件标识
    private Integer finishPhotoQty = 0;//完成照片数
    private Integer photoMinQty = 0;//要求最少上传照片数量
    private Integer photoMaxQty = 0;//要求最多上传照片数量
    private Integer appAbnormalyFlag = 0; //异常标志
    private Integer pendingFlag = 0;//停滞标志
    private Integer partsStatus = 0;//配件申请单状态 0:无配件申请 1:处理中 2:完成
    private int serviceFlag = 0;//上门服务标志，1:有上门服务 0:无上门服务
    private int reminderFlag = 0; //催单标识 0：无催单 19/07/09
    //以下两个属性在reminderFalg = 1 时使用
    private int reminderItemNo = 0;//第几次催单 19/11/25
    private long reminderTimeoutAt = 0;//催单超时时间(毫秒)

    private Integer serviceTimes = 0;//上门次数
    private List<OrderAttachment> photos = Lists.newArrayList();
    private List<RestOrderItem> items = Lists.newArrayList();
    private List<RestOrderDetail> services = Lists.newArrayList();
    private List<Product> products = Lists.newArrayList();//订单项产品列表，套组进行拆分
    //费用
    private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
    private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
    private Double engineerExpressCharge = 0.00;// 快递费（应付）
    private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
    private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
    //网点合计费用= engineerServiceCharge+engineerTravelCharge+engineerExpressCharge+engineerMaterialCharge+engineerOtherCharge
    private Double engineerCharge = 0.0;//网点合计费用
    private Double estimatedServiceCost = 0.0;//预估服务费 18/01/24
    private String kefuPhone = ""; //客服电话
    private int isComplained = 0;//投诉标识 18/01/24
    private int hasAuxiliaryMaterials = 0; //是否设置了辅材和服务项目
    private Double auxiliaryMaterialsTotalCharge = 0.0; //使用到的辅材和服务项目的总金额\
    private Double auxiliaryMaterialsActualTotalCharge = 0.0; //使用到的辅材和服务项目的总金额

    private String estimatedReceiveDate = "";//预计到货时间
    private Long arrivalDate = 0L; //实际到货时间 （2020-3-14新增）
    private String expectServiceTime = ""; //期望上门日期 (2020-3-14新增属)

    private Long buyDate = 0L;


    private List<RestProductCompletePic> picRules = Lists.newArrayList();
    private List<RestProductCompletePic> orderPics = Lists.newArrayList();
    private List<AppPraisePicItem> praisePics = Lists.newArrayList();

    /**
     * 用户地址中的区域部分
     */
    @Getter
    @Setter
    private String areaName = "";
    /**
     * 用户地址中的详细地址部分
     */
    @Getter
    @Setter
    private String subAddress = "";

    /**
     * 好评单状态
     */
    @Getter
    @Setter
    private Integer praiseStatus = 0;

    @Getter
    @Setter
    private Integer suspendFlag = OrderSuspendFlagEnum.NORMAL.getValue();
    @Getter
    @Setter
    private Integer suspendType = OrderSuspendTypeEnum.NONE.getValue();

    public RestOrderDetailInfo() {
    }



    public Long getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Long buyDate) {
        this.buyDate = buyDate;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPartsFlag() {
        return partsFlag;
    }

    public void setPartsFlag(Integer partsFlag) {
        this.partsFlag = partsFlag;
    }

    public Integer getFinishPhotoQty() {
        return finishPhotoQty;
    }

    public void setFinishPhotoQty(Integer finishPhotoQty) {
        this.finishPhotoQty = finishPhotoQty;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public List<OrderAttachment> getPhotos() {
        return photos;
    }

    public void setPhotos(List<OrderAttachment> photos) {
        this.photos = photos;
    }

    public List<RestOrderItem> getItems() {
        return items;
    }

    public void setItems(List<RestOrderItem> items) {
        this.items = items;
    }

    public List<RestOrderDetail> getServices() {
        return services;
    }

    public void setServices(List<RestOrderDetail> services) {
        this.services = services;
    }

    public Double getEngineerCharge() {
        return engineerCharge;
    }

    public void setEngineerCharge(Double engineerCharge) {
        this.engineerCharge = engineerCharge;
    }

    public Double getEngineerServiceCharge() {
        return engineerServiceCharge;
    }

    public void setEngineerServiceCharge(Double engineerServiceCharge) {
        this.engineerServiceCharge = engineerServiceCharge;
    }

    public Double getEngineerTravelCharge() {
        return engineerTravelCharge;
    }

    public void setEngineerTravelCharge(Double engineerTravelCharge) {
        this.engineerTravelCharge = engineerTravelCharge;
    }

    public Double getEngineerExpressCharge() {
        return engineerExpressCharge;
    }

    public void setEngineerExpressCharge(Double engineerExpressCharge) {
        this.engineerExpressCharge = engineerExpressCharge;
    }

    public Double getEngineerMaterialCharge() {
        return engineerMaterialCharge;
    }

    public void setEngineerMaterialCharge(Double engineerMaterialCharge) {
        this.engineerMaterialCharge = engineerMaterialCharge;
    }

    public Double getEngineerOtherCharge() {
        return engineerOtherCharge;
    }

    public void setEngineerOtherCharge(Double engineerOtherCharge) {
        this.engineerOtherCharge = engineerOtherCharge;
    }

    public Integer getPhotoMaxQty() {
        return photoMaxQty;
    }

    public void setPhotoMaxQty(Integer photoMaxQty) {
        this.photoMaxQty = photoMaxQty;
    }

    public Integer getPhotoMinQty() {
        return photoMinQty;
    }

    public void setPhotoMinQty(Integer photoMinQty) {
        this.photoMinQty = photoMinQty;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getPendingFlag() {
        return pendingFlag;
    }

    public void setPendingFlag(Integer pendingFlag) {
        this.pendingFlag = pendingFlag;
    }

    public Integer getAppAbnormalyFlag() {
        return appAbnormalyFlag;
    }

    public void setAppAbnormalyFlag(Integer appAbnormalyFlag) {
        this.appAbnormalyFlag = appAbnormalyFlag;
    }

    public Integer getPartsStatus() {
        return partsStatus;
    }

    public void setPartsStatus(Integer partsStatus) {
        this.partsStatus = partsStatus;
    }

    public int getServiceFlag() {
        return serviceFlag;
    }

    public void setServiceFlag(int serviceFlag) {
        this.serviceFlag = serviceFlag;
    }

    public String getKefuPhone() {
        return kefuPhone;
    }

    public void setKefuPhone(String kefuPhone) {
        this.kefuPhone = kefuPhone;
    }

    public Double getEstimatedServiceCost() {
        return estimatedServiceCost;
    }

    public void setEstimatedServiceCost(Double estimatedServiceCost) {
        this.estimatedServiceCost = estimatedServiceCost;
    }

    @Override
    public int getIsComplained() {
        return isComplained;
    }

    @Override
    public void setIsComplained(int isComplained) {
        this.isComplained = isComplained;
    }

    public List<RestProductCompletePic> getPicRules() {
        return picRules;
    }

    public void setPicRules(List<RestProductCompletePic> picRules) {
        this.picRules = picRules;
    }

    public List<RestProductCompletePic> getOrderPics() {
        return orderPics;
    }

    public void setOrderPics(List<RestProductCompletePic> orderPics) {
        this.orderPics = orderPics;
    }

    public int getHasAuxiliaryMaterials() {
        return hasAuxiliaryMaterials;
    }

    public void setHasAuxiliaryMaterials(int hasAuxiliaryMaterials) {
        this.hasAuxiliaryMaterials = hasAuxiliaryMaterials;
    }

    public Double getAuxiliaryMaterialsTotalCharge() {
        return auxiliaryMaterialsTotalCharge;
    }

    public void setAuxiliaryMaterialsTotalCharge(Double auxiliaryMaterialsTotalCharge) {
        this.auxiliaryMaterialsTotalCharge = auxiliaryMaterialsTotalCharge;
    }

    public Double getAuxiliaryMaterialsActualTotalCharge() {
        return auxiliaryMaterialsActualTotalCharge;
    }

    public void setAuxiliaryMaterialsActualTotalCharge(Double auxiliaryMaterialsActualTotalCharge) {
        this.auxiliaryMaterialsActualTotalCharge = auxiliaryMaterialsActualTotalCharge;
    }

    @Override
    public int getReminderFlag() {
        return reminderFlag;
    }

    @Override
    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public String getEstimatedReceiveDate() {
        return estimatedReceiveDate;
    }

    public void setEstimatedReceiveDate(String estimatedReceiveDate) {
        this.estimatedReceiveDate = estimatedReceiveDate;
    }

    public Long getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Long arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getExpectServiceTime() {
        return expectServiceTime;
    }

    public void setExpectServiceTime(String expectServiceTime) {
        this.expectServiceTime = expectServiceTime;
    }

    @Override
    public int getReminderItemNo() {
        return reminderItemNo;
    }

    @Override
    public void setReminderItemNo(int reminderItemNo) {
        this.reminderItemNo = reminderItemNo;
    }

    @Override
    public long getReminderTimeoutAt() {
        return reminderTimeoutAt;
    }

    @Override
    public void setReminderTimeoutAt(long reminderTimeoutAt) {
        this.reminderTimeoutAt = reminderTimeoutAt;
    }

    public List<AppPraisePicItem> getPraisePics() {
        return praisePics;
    }

    public void setPraisePics(List<AppPraisePicItem> praisePics) {
        this.praisePics = praisePics;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
