package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
public class CustomerProduct extends LongIDDataEntity<CustomerProduct>
{
    private Customer customer;          //客户
    private Product product;            //产品
    @GsonIgnore
    private int count;                  //为saveorupdate数据操作

    /**
     * 安装规范
     * */
    private String fixSpec = "";

    /**
     * 安装规范的html
     */
    private String fixSpecification="";
    /**
     * 视频链接
     * */
    private String videoUrl = "";

    /**
     * 远程费用标识 1-有远程费,2-无远程费,缺省为有远程费
     * */
    private Integer remoteFeeFlag = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {

        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFixSpec() {
        return fixSpec;
    }

    public void setFixSpec(String fixSpec) {
        this.fixSpec = fixSpec;
    }

    public String getFixSpecification() {
        return fixSpecification;
    }

    public void setFixSpecification(String fixSpecification) {
        this.fixSpecification = fixSpecification;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getRemoteFeeFlag() {
        return remoteFeeFlag;
    }

    public void setRemoteFeeFlag(Integer remoteFeeFlag) {
        this.remoteFeeFlag = remoteFeeFlag;
    }
}
