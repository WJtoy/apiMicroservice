package com.wolfking.jeesite.common.config;

import com.wolfking.jeesite.modules.md.utils.*;
import com.wolfking.jeesite.modules.sd.utils.*;
import com.wolfking.jeesite.modules.sys.entity.adapter.MenuAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 订单Gson的控制类
 * Ryan Lu
 */
@Component
public class GsonConfig {

    //Sys

    @Bean(name = "menuAdapter")
    public MenuAdapter menuAdapter() {
        return MenuAdapter.getInstance();
    }

    @Bean(name = "areaAdapter")
    public AreaAdapter areaAdapter() {
        return AreaAdapter.getInstance();
    }

    /// MD
    @Bean(name = "customerAdapter")
    public CustomerAdapter customerAdapter() {
        return CustomerAdapter.getInstance();
    }


    //region order
    /**
     * 完整订单
     * @return
     */
    @Bean(name = "orderAdapter")
    public OrderAdapter orderAdapter() {
        return OrderAdapter.getInstance();
    }

    @Bean(name = "orderRedisAdapter")
    public OrderRedisAdapter orderRedisAdapter(){
        return OrderRedisAdapter.getInstance();
    }

    /**
     * 订单查询条件
     * @return
     */
    @Bean(name = "orderConditionAdapter")
    public OrderConditionAdapter orderConditionAdapter() {
        return OrderConditionAdapter.getInstance();
    }

    /**
     * 订单查询条件redis 适配器
     * @return
     */
    @Bean(name = "orderConditionRedisAdapter")
    public OrderConditionRedisAdapter orderConditionRedisAdapter() {
        return OrderConditionRedisAdapter.getInstance();
    }

    /**
     * 订单实际服务明细
     * @return
     */
    @Bean(name = "orderDetailAdapter")
    public OrderDetailAdapter orderDetailAdapter() {
        return OrderDetailAdapter.getInstance();
    }

    /**
     * 订单费用
     * @return
     */
    @Bean(name = "orderFeeAdapter")
    public OrderFeeAdapter orderFeeAdapter() {
        return OrderFeeAdapter.getInstance();
    }

    /**
     * 订单项目
     * @return
     */
    @Bean(name = "orderItemAdapter")
    public OrderItemAdapter orderItemAdapter() {
        return OrderItemAdapter.getInstance();
    }

    /**
     * 订单状态
     * @return
     */
    @Bean(name = "orderStatusAdapter")
    public OrderStatusAdapter orderStatusAdapter() {
        return OrderStatusAdapter.getInstance();
    }

    //endregion

    //region 网点

    @Bean(name = "servicePointAdapter")
    public ServicePointAdapter servicePointAdapter() {
        return ServicePointAdapter.getInstance();
    }

    @Bean(name = "servicePointFinanceAdapter")
    public ServicePointFinanceAdapter servicePointFinanceAdapter(){
        return ServicePointFinanceAdapter.getInstance();
    }

    @Bean(name = "servicePointPrimaryAdapter")
    public ServicePointPrimaryAdapter servicePointPrimaryAdapter(){
        return ServicePointPrimaryAdapter.getInstance();
    }

    //endregion

}

