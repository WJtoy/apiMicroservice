/**
 * Copyright &copy; 2014-2014 All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderCrush;
import com.wolfking.jeesite.modules.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 突击单
 */
@Mapper
public interface OrderCrushDao extends LongIDCrudDao<OrderCrush> {

    /**
     * 按订单关闭突击单突击单
     */
    int closeOrderCurshByOrderId(
            @Param("orderId") long orderId,
            @Param("quarter") String quarter,
            @Param("status") int status,
            @Param("closeRemark") String closeRemark,
            @Param("closeBy") User closeBy,
            @Param("closeDate") Date closeDate
    );

}
