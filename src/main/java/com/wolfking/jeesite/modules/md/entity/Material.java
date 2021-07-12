package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 配件
 * Created on 2017-04-12 by Ryan Lu
 */
public class Material extends LongIDDataEntity<Material>
{
    private static final long serialVersionUID = 1L;
    private String name = "";
    private String model = "";
    private Double price;
    private Integer isReturn = 0;
    private List<Long> productList = Lists.newArrayList();
    private MaterialCategory materialCategory;
    @GsonIgnore
    private Product product;

    public Material() {
        super();
    }

    public Material(int isReturn,double price) {
        super();
        this.isReturn = isReturn;
        this.price = price;
    }

    public Material(Long id){
        this.id = id;
    }

    @NotNull(message="名称不能为空")
    @Length(min = 2,max = 30,message = "名称长度应为(2~30)位")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Length(min = 2,max = 30,message = "型号长度应为(2~30)位")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Long> getProductList() {
        return productList;
    }

    public void setProductList(List<Long> productList) {
        this.productList = productList;
    }

    public MaterialCategory getMaterialCategory() {
        return materialCategory;
    }

    public void setMaterialCategory(MaterialCategory materialCategory) {
        this.materialCategory = materialCategory;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
