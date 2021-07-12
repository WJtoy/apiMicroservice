/**
 * Copyright &copy; 2014-2014 All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderReceivable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单应收表
 */
@Mapper
public interface OrderReceivableDao extends LongIDCrudDao<OrderReceivable> {

	/**
	 * 按订单id读取
	 * @param orderId
	 * @return
	 */
	 List<OrderReceivable> getByOrderId(@Param("orderId") long orderId, @Param("quarter") String quarter);


}
