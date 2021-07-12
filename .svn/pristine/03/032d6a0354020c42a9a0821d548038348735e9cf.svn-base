package com.wolfking.jeesite.ms.tmall.md.entity;

import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.ms.tmall.md.adapter.B2BProductModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * B2B对接系统客户与工单系统产品关联定义表
 * data_source + shop_id + customer_category_id = product_id
 */
@Data
@NoArgsConstructor
public class B2bProductMap extends LongIDDataEntity<B2bProductMap> {

    private int dataSource = 0;

    private String shopId = "";

    private String shopName;//

    private Long customerId;

    private String customerName = "";//

    @GsonIgnore
    private String customerCategoryId;

    @GsonIgnore
    private Product product;

    private List<B2BProductModel> list;//辅助字段 为了批量添加数据


}
