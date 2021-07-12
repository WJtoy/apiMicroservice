package com.wolfking.jeesite.modules.md.entity;

/**
 * Created by Jeff on 2017/7/9.
 */
public class ServicePointProduct {
    private ServicePoint servicePoint;
    private Product product;

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
