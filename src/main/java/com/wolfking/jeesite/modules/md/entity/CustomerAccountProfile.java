package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.CustomerSalesAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServicePointPrimaryAdapter;

/**
 * 客户实体类
 * Created on 2017-04-12.
 */
public class CustomerAccountProfile extends LongIDDataEntity<CustomerAccountProfile>
{
    @JsonAdapter(CustomerSalesAdapter.class)
    private Customer customer;            //客户
    private int orderApproveFlag = -1;         //下单是否需要审核，1：要审核

    public CustomerAccountProfile(){
        super();
    }

    public CustomerAccountProfile(int orderApproveFlag){
        this.orderApproveFlag = orderApproveFlag;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getOrderApproveFlag() {
        return orderApproveFlag;
    }

    public void setOrderApproveFlag(int orderApproveFlag) {
        this.orderApproveFlag = orderApproveFlag;
    }
}
