package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * 保险价格配置
 */
public class InsurancePrice extends LongIDDataEntity<InsurancePrice> {

    public InsurancePrice() {
        super();
    }
    public InsurancePrice(Long id) {
        super(id);
    }
    private ProductCategory category;
    private Double insurance=0.00;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Double getInsurance() {
        return insurance;
    }

    public void setInsurance(Double insurance) {
        this.insurance = insurance;
    }
}
