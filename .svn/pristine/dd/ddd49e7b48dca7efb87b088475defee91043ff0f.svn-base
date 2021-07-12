/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.service;

import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.sd.dao.OrderAttachmentDao;
import com.wolfking.jeesite.modules.sd.dao.OrderDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * 订单附件Service
 * 
 * @author RyanLu
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderAttachmentService extends LongIDBaseService {

	@Autowired
	private OrderAttachmentDao attachmentDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private RedisUtilsLocal redisUtilsLocal;

	@Autowired
	private OrderService orderService;

	public OrderAttachment get(Long id) {
		if(id==null || id<=0){
			return new OrderAttachment();
		}
		return attachmentDao.get(id);
	}

	/**
	 * 新增订单附件
	 * 订单完成附件数量+1,redis保持同步
	 * @param attachment
	 */
	@Transactional(readOnly = false)
	public void save(OrderAttachment attachment) {
		if(attachment == null){
			throw new RuntimeException("参数值未空。");
		}
		if(attachment.getOrderId() == null || attachment.getOrderId()<=0){
			throw new RuntimeException("未关联订单:无法保存附件。");
		}
		Order order = attachment.getOrder();
		if(order == null) {
			order = orderService.getOrderById(attachment.getOrderId(), "", OrderUtils.OrderDataLevel.CONDITION, true);
		}
		if(order==null || order.getOrderCondition()==null){
			throw new RuntimeException("读取关联订单信息失败");
		}
		User user = attachment.getCreateBy();
		attachmentDao.insert(attachment);
		if(user.getUserType() != 5) {
			//log,app上传不记录日志
			OrderProcessLog log = new OrderProcessLog();
			log.setQuarter(order.getQuarter());
			log.setAction("添加附件");
			log.setOrderId(attachment.getOrderId());
			log.setActionComment(String.format("添加附件:%s,操作人:%s", attachment.getRemarks(), attachment.getCreateBy().getName()));
			log.setStatus(order.getOrderCondition().getStatus().getLabel());
			log.setStatusValue(order.getOrderCondition().getStatusValue());
			log.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
			log.setCloseFlag(0);
			log.setCreateBy(attachment.getCreateBy());
			log.setCreateDate(attachment.getCreateDate());
//			orderDao.insertProcessLog(log);
			log.setCustomerId(order.getOrderCondition() != null ? order.getOrderCondition().getCustomerId() : 0);
			log.setDataSourceId(order.getDataSourceId());
			orderService.saveOrderProcessLogNew(log);
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("quarter",order.getQuarter());
		map.put("orderId", attachment.getOrderId());
		map.put("finishPhotoQty", 1);//+1
		orderDao.updateCondition(map);

		OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
		builder.setOpType(OrderCacheOpType.UPDATE)
				.setOrderId(attachment.getOrderId())
				.incrFinishPhotoQty(1L)
				.setDeleteField(OrderCacheField.ATTACHMENTS);
		OrderCacheUtils.update(builder.build());

	}

	/**
	 * 删除订单附件
	 * @param attachment
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void delete(OrderAttachment attachment) {
		if (attachment == null) {
			throw new RuntimeException("参数值未空。");
		}
		if (attachment.getId() == null || attachment.getId() <= 0) {
			throw new RuntimeException("参数错误,无法删除附件。");
		}
		User user = new User(1l,"");
		attachmentDao.delete(attachment);//逻辑删除，文件定期备份和删除
		String key = String.format(RedisConstant.SD_ORDER, attachment.getOrderId());
		OrderAttachment[] attachments = redisUtilsLocal.hGet(RedisConstant.RedisDBType.REDIS_SD_DB, key, "attachments", OrderAttachment[].class);
		if (attachments != null && attachments.length > 0) {
			OrderAttachment orderAttachment;
			int idx = -1;
			for (int i = 0, size = attachments.length; i < size; i++) {
				orderAttachment = attachments[i];
				if (Objects.equals(orderAttachment.getId(), attachment.getId())) {
					idx = i;
					break;
				}
			}
			if (idx > -1) {
				attachments = ArrayUtils.remove(attachments, idx);
			} else {
				//已删除
				return;
			}
		} else {
			return;
		}

		//update order
		HashMap<String, Object> map = new HashMap<>();
		map.put("orderId", attachment.getOrderId());
		if(StringUtils.isNoneBlank(attachment.getQuarter())) {
			map.put("quarter", attachment.getQuarter());
		}
		map.put("finishPhotoQty", -1);//-1
		orderDao.updateCondition(map);

		Long num = 0L;


		//调用公共缓存
		OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
		builder.setOpType(OrderCacheOpType.UPDATE)
			   .setOrderId(attachment.getOrderId())
               .incrFinishPhotoQty(-1L);
		if(attachments.length == 0){
			builder.setDeleteField(OrderCacheField.ATTACHMENTS);
		}else{
			//update
			builder.setAttachments(Arrays.asList(attachments))
				   .setExpireSeconds(0L);
		}
		OrderCacheUtils.update(builder.build());

	}
}
