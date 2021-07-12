/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.sd.dao.OrderStatusFlagDao;
import com.wolfking.jeesite.modules.sd.entity.OrderStatusFlag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 订单状态标记表服务层
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderStatusFlagService extends LongIDBaseService {

    /**
     * 持久层对象
     */
    @Resource
    protected OrderStatusFlagDao dao;

    /**
     * 新增
     */
    @Transactional(readOnly = false)
    public void insert(OrderStatusFlag entity) {
        dao.insert(entity);
    }

    public OrderStatusFlag getByOrderId(long orderId,String quarter){
        if(orderId <= 0){
            return null;
        }
        return dao.getById(orderId,quarter);
    }

    /**
     * 更改好评单状态
     * @param orderId
     * @param quarter
     * @param praiseStatus
     * @return
     */
    @Transactional
    public int updatePraiseStatus(long orderId,String quarter, int praiseStatus){
        if(praiseStatus <= 0){
            return 0;
        }
        return dao.UpdatePraiseStatus(orderId,quarter,praiseStatus);
    }

    /**
     * 更改订单完工状态
     * @param orderId
     * @param quarter
     * @param completeStatus
     * @return
     */
    @Transactional
    public int UpdateOrderCompleteStatus(long orderId,String quarter, int completeStatus){
        if(completeStatus <= 0){
            return 0;
        }
        return dao.UpdateOrderCompleteStatus(orderId,quarter,completeStatus);
    }

    /**
     * 根据工单id获取好评状态
     * @param orderIds
     * @return
     */
    public Map<Long,OrderStatusFlag> getStatusFlagMapByOrderIds(String quarter,List<Long> orderIds){
        if(orderIds==null){
            return Maps.newHashMap();
        }
        return dao.getStatusFlagMapByOrderIds(quarter,orderIds);
    }
}
