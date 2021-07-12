/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.sd.dao.OrderVoiceTaskDao;
import com.wolfking.jeesite.modules.sd.entity.OrderVoiceTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 订单Job Service
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderVoiceTaskService extends LongIDBaseService {

    /**
     * 持久层对象
     */
    @Resource
    protected OrderVoiceTaskDao dao;

    /**
     * 按订单读取完整智能回访记录
     * @param quarter
     * @param orderId
     * @return
     */
    public OrderVoiceTask getByOrderId(String quarter,Long orderId){
        return dao.getByOrderId(quarter,orderId);
    }

    /**
     * 按订单读取智能回访记录的基本信息
     * @param quarter
     * @param orderId
     * @return
     */
    public OrderVoiceTask getBaseInfoByOrderId(String quarter,Long orderId){
        return dao.getBaseInfoByOrderId(quarter,orderId);
    }



    @Transactional(readOnly = false)
    public void insert(OrderVoiceTask entity){
        dao.insert(entity);
    }



    @Transactional(readOnly = false)
    public void cancel(String quarter,Long orderId,Long updateDate){
        dao.cancel(quarter,orderId,updateDate);
    }

}
