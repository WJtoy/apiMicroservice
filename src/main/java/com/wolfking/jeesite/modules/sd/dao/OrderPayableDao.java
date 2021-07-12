/**
 * Copyright &copy; 2014-2014 All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderPayable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单应付表
 */
@Mapper
public interface OrderPayableDao extends LongIDCrudDao<OrderPayable> {

	/**
	 * 按订单id读取
	 * @param orderId
	 * @return
	 */
	 List<OrderPayable> getByOrderId(@Param("orderId") long orderId, @Param("quarter") String quarter);

}
