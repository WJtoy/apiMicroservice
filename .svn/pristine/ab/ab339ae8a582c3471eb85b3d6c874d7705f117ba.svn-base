/**
 * Copyright &copy; 2014-2014 All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderStatusFlag;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单状态标记表
 */
@Mapper
public interface OrderStatusFlagDao extends LongIDCrudDao<OrderStatusFlag> {

    /**
     * 按订单id读取
     * @param orderId
     * @return
     */
    OrderStatusFlag getById(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 更改好评单状态
     */
    int UpdatePraiseStatus(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("praiseStatus") int praiseStatus);

    /**
     * 更改订单完工状态
     */
    int UpdateOrderCompleteStatus(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("completeStatus") int completeStatus);

    @MapKey("orderId")
    Map<Long, OrderStatusFlag> getStatusFlagMapByOrderIds(@Param("quarter") String quarter, @Param("orderIds") List<Long> orderIds);

}
