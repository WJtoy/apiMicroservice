/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.entity;


import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * 订单附件
 * 
 * @author ryan
 * @version 2015-03-10
 */
public class OrderAttachment extends LongIDDataEntity<OrderAttachment>
{

	private static final long serialVersionUID = 1L;

	private String filePath;

	private Long orderId;

	private String quarter = "";//数据库分片，与订单相同

	//辅助字段
	@GsonIgnore
	private Order order;

	public OrderAttachment()
	{
		super();
	}

	public OrderAttachment(String filePath)
	{

		super();
		this.filePath = filePath;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String file_path)
	{
		this.filePath = file_path;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
