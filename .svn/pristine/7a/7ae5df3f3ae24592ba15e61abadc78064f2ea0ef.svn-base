package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import javax.validation.constraints.NotNull;

/**
 * Created by Jeff on 2017/4/28.
 */
public class ProductPrice extends LongIDDataEntity<ProductPrice> {

    private static final long serialVersionUID = 1L;

    private ServiceType serviceType;
    private Product product;
    private Double customerStandardPrice = 0.00;
    private Double customerDiscountPrice = 0.00;
    private Double engineerStandardPrice = 0.00;
    private Double engineerDiscountPrice = 0.00;
    private Dict priceType;

    public ProductPrice() {
        super();
    }

    public ProductPrice(Long id){
        super();
        this.id = id;
    }

    public ProductPrice(Long id, Product product, ServiceType serviceType)
    {
        this();
        this.id = id;
        this.product=product;
        this.serviceType=serviceType;
    }

    @NotNull(message = "产品不能为空")
    public Product getProduct()
    {
        return this.product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    @NotNull(message = "服务类型不能为空")
    public ServiceType getServiceType()
    {
        return this.serviceType;
    }

    public void setServiceType(ServiceType serviceType)
    {
        this.serviceType = serviceType;
    }

    public Double getCustomerStandardPrice()
    {
        return customerStandardPrice;
    }

    public void setCustomerStandardPrice(Double customerStandardPrice)
    {
        this.customerStandardPrice = customerStandardPrice;
    }

    public Double getCustomerDiscountPrice()
    {
        return customerDiscountPrice;
    }

    public void setCustomerDiscountPrice(Double customerDiscountPrice)
    {
        this.customerDiscountPrice = customerDiscountPrice;
    }

    public Double getEngineerStandardPrice()
    {
        return engineerStandardPrice;
    }

    public void setEngineerStandardPrice(Double engineerStandardPrice)
    {
        this.engineerStandardPrice = engineerStandardPrice;
    }

    public Double getEngineerDiscountPrice()
    {
        return engineerDiscountPrice;
    }

    public void setEngineerDiscountPrice(Double engineerDiscountPrice)
    {
        this.engineerDiscountPrice = engineerDiscountPrice;
    }

    public Dict getPriceType() {
        return priceType;
    }

    public void setPriceType(Dict priceType) {
        this.priceType = priceType;
    }
}
