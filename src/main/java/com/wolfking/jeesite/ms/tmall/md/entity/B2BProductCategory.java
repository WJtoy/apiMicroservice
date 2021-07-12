package com.wolfking.jeesite.ms.tmall.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class B2BProductCategory extends LongIDDataEntity<B2BProductCategory> {

    /**
     * 数据源：淘宝
     */
    public static final Integer DATA_SOURCE_TMALL   = 2;

    /**
     * 工单系统中的网点id
     */
    @Getter
    @Setter
    private Long servicePointId;

    /**
     * 工单系统中的商品id
     */
    @Getter
    @Setter
    private Long productId;

    /**
     * 工单系统中的商品分类id
     */
    @Getter
    @Setter
    private Long productCategoryId;

    /**
     * 天猫中叶子类目id
     */
    @Getter
    @Setter
    private Long customerCategoryId;
}
