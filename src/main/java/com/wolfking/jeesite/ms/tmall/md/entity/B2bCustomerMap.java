package com.wolfking.jeesite.ms.tmall.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.ms.tmall.md.adapter.B2bCustomerMapSimpleAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * B2B对接系统客户与工单系统客户关联定义表
 * 一对多
 * dataSoure + shopId (1) = customerId (n)
 */
@Data
@NoArgsConstructor
@JsonAdapter(B2bCustomerMapSimpleAdapter.class)
public class B2bCustomerMap extends LongIDDataEntity<B2bCustomerMap> {

    private int dataSource = 0;

    private String shopId = "";

    private String shopName;

    private Long customerId;

    private String customerName;//辅助字段，列表显示

    public B2bCustomerMap(String shopId){
        this.shopId = shopId;
    }

    public B2bCustomerMap(String shopId,String shopName){
        this.shopId = shopId;
        this.shopName = shopName;
    }
}
