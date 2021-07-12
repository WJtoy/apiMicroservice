package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.Data;

@Data
public class BrandCategory extends LongIDDataEntity<BrandCategory> {
    private Brand brand;
    private ProductCategory category;

    public BrandCategory() {
    }

    public BrandCategory(Long id) {
        super(id);
    }
}
