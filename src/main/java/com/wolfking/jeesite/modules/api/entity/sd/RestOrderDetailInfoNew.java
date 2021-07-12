package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;

import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderDetailInfoNewAdapter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Auther wj
 * @Date 2020/12/1 17:06
 */
@Getter
@Setter
@JsonAdapter(RestOrderDetailInfoNewAdapter.class)
public class RestOrderDetailInfoNew  {


    //费用
    private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
    private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
    private Double engineerExpressCharge = 0.00;// 快递费（应付）
    private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
    private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
    //网点合计费用= engineerServiceCharge+engineerTravelCharge+engineerExpressCharge+engineerMaterialCharge+engineerOtherCharge
    private Double engineerCharge = 0.0;//网点合计费用
    private Double estimatedServiceCost = 0.0;//预估服务费 18/01/24
    private int isComplained = 0;//投诉标识 18/01/24
    private int hasAuxiliaryMaterials = 0; //是否设置了辅材和服务项目
    private Double auxiliaryMaterialsTotalCharge = 0.0; //使用到的辅材和服务项目的总金额\
    private Double auxiliaryMaterialsActualTotalCharge = 0.0; //使用到的辅材和服务项目的总金额
    private int serviceFlag = 0;//上门服务标志，1:有上门服务 0:无上门服务
    private Integer serviceTimes = 1;//上门次数

    public void setServices(List<RestOrderDetail> services) {
        this.services = services;
    }

    public List<RestOrderDetail> getServices() {
        return services;
    }

    private List<RestOrderDetail> services = Lists.newArrayList();


}
