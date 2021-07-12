package com.wolfking.jeesite.modules.api.entity.sd.request;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;

import java.util.List;

/**
 * 保存配件申请单请求
 */
public class RestSaveAccessoryRequest extends RestOrderBaseRequest {
    private String productId;//产品id
    private Integer applyType = 2;//1:向师傅购买(自购) 2:厂家寄发
    private String orderDetailId;


    private List<RestSaveAccessoryItemRequest> items = Lists.newArrayList();
    //private List<String> attachments = Lists.newArrayList();

    public RestSaveAccessoryRequest(){}

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public List<RestSaveAccessoryItemRequest> getItems() {
        return items;
    }

    public void setItems(List<RestSaveAccessoryItemRequest> items) {
        this.items = items;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    //public List<String> getAttachments() {
    //    return attachments;
    //}

    //public void setAttachments(List<String> attachments) {
    //    this.attachments = attachments;
    //}
}
