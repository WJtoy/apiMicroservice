/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.dao.OrderOpitionTraceDao;
import com.wolfking.jeesite.modules.sd.entity.OrderOpitionTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * App反馈服务层
 *
 * @author RyanLu
 * @version 2020-01-08
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderOpitionTraceService extends LongIDBaseService {

    @Autowired
    private OrderOpitionTraceDao opitionTraceDao;


    /**
     * 按网点Id读取同反馈类型反馈次数
     * @param orderId
     * @param quarter
     * @param opitionId
     * @param servicePointId
     * @return
     */
    public Integer getTimesByServicepoint(Long orderId, String quarter,Integer opitionId,Long servicePointId) {
        return opitionTraceDao.getTimesByServicepoint(orderId,quarter,opitionId,servicePointId);
    }

    /**
     * 按反馈类型读取反馈总次数
     * @param orderId
     * @param quarter
     * @param opitionId
     */
    public Integer getTotalTimesByOpinionType(Long orderId, String quarter, Integer opitionId) {
        return opitionTraceDao.getTotalTimesByOpinionType(orderId,quarter,opitionId);
    }

    /**
     * 新增App反馈
     */
    @Transactional(readOnly = false)
    public void insert(OrderOpitionTrace feedback) throws Exception {
        if (feedback.getOrderId() == null || feedback.getOrderId() <= 0L) {
            throw new RuntimeException("未关联订单:无法保存反馈");
        }
        //数据库分片
        if (StringUtils.isBlank(feedback.getQuarter())) {
            throw new RuntimeException("参数错误：无分片");
        }
        if(feedback.getParentId() ==0){
            feedback.setParentId(feedback.getOpinionId());
        }
        opitionTraceDao.insert(feedback);
    }



}
