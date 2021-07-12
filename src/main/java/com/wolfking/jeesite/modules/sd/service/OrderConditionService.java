package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.modules.sd.dao.OrderConditionDao;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderConditionStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther wj
 * @Date 2021/3/15 11:23
 */

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OrderConditionService {

    @Autowired
    private OrderConditionDao orderConditionDao;

    public OrderConditionStatus getOrderInfo(Long orderId, String quarter){
       return orderConditionDao.getOrderInfo( orderId, quarter);
    }


}
