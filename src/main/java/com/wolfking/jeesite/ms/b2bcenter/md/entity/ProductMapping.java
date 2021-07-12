package com.wolfking.jeesite.ms.b2bcenter.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductMapping extends LongIDDataEntity {

    private Integer dataSource = 0;

    private String customerCategoryId;

    private Long productId;

    private String productName;

    //private List<B2BProductModel> list;//辅助字段 为了批量添加数据

    private List<String> categoryIdList;

    //private List<CategoryIdMode> categoryIdList;
}
