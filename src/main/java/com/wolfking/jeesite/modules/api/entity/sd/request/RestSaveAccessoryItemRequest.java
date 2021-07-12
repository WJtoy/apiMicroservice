package com.wolfking.jeesite.modules.api.entity.sd.request;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;

import java.util.List;

/**
 * 保存配件申请单请求
 */
public class RestSaveAccessoryItemRequest  {

    private String materialId;//配件id
    private Integer qty = 1;
    private Double price = 0.0;//单价

    public RestSaveAccessoryItemRequest(){}

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
