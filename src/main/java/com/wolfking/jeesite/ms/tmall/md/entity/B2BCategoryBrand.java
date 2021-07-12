package com.wolfking.jeesite.ms.tmall.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class B2BCategoryBrand extends LongIDDataEntity<B2BCategoryBrand> {

    /**
     * 工单系统中的网点id
     */
    @Getter
    @Setter
    private Long servicePointId;

    /**
     * 工单系统中的商品分类id
     */
    @Getter
    @Setter
    private Long productCategoryId;

    /**
     * 工单系统中的品牌id
     */
    @Getter
    @Setter
    private Long brandId;

    /**
     * 编号，第三方如天猫的品牌id
     */
    @Getter
    @Setter
    private String brandCode;
}
