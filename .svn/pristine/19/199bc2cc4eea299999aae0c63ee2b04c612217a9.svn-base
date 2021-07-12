package com.wolfking.jeesite.modules.sd.entity;

import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialCategory;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Product;

public class AuxiliaryMaterial extends LongIDDataEntity<AuxiliaryMaterial> {

    /**
     * 工单ID
     */
    private Long orderId = 0L;

    /**
     * 辅材或收费子目所属的大类
     */
    private MDAuxiliaryMaterialCategory category;

    /**
     * 产品
     */
    private Product product;

    /**
     * 辅材或收费子目
     */
    private MDAuxiliaryMaterialItem material;

    /**
     * 辅材或收费子目数量
     */
    private Integer qty = 0;

    /**
     * 费用总金额
     */
    private Double subtotal = 0.0;

    /**
     * 费用单位
     */
    private String unit = "元";

    private String quarter = "";

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public MDAuxiliaryMaterialCategory getCategory() {
        return category;
    }

    public void setCategory(MDAuxiliaryMaterialCategory category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MDAuxiliaryMaterialItem getMaterial() {
        return material;
    }

    public void setMaterial(MDAuxiliaryMaterialItem material) {
        this.material = material;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
