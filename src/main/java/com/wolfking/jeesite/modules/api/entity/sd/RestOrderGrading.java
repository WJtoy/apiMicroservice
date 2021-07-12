package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderGradingAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderHistoryAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 待客评工单
 */
@JsonAdapter(RestOrderGradingAdapter.class)
public class RestOrderGrading extends RestOrder {

    private String description = ""; //服务描述
    private Date engineerInvoiceDate;//安维付款日期

    //费用
    private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
    private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
    private Double engineerExpressCharge = 0.00;// 快递费（应付）
    private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
    private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
    private Double engineerCharge = 0.0;//网点合计费用
    private int isComplained = 0;//投诉标识 18/01/24

    /**
     * 好评单状态
     */
    @Getter
    @Setter
    private AppDict praiseStatus = new AppDict("0", "");

    private List<RestOrderDetail> services = Lists.newArrayList();

    public RestOrderGrading() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getEngineerInvoiceDate() {
        return engineerInvoiceDate;
    }

    public void setEngineerInvoiceDate(Date engineerInvoiceDate) {
        this.engineerInvoiceDate = engineerInvoiceDate;
    }

    public Double getEngineerOtherCharge() {
        return engineerOtherCharge;
    }

    public void setEngineerOtherCharge(Double engineerOtherCharge) {
        this.engineerOtherCharge = engineerOtherCharge;
    }

    @Override
    public int getIsComplained() {
        return isComplained;
    }

    @Override
    public void setIsComplained(int isComplained) {
        this.isComplained = isComplained;
    }
}
