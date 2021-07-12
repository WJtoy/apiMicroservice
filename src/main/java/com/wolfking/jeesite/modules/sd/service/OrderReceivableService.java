/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.dao.OrderReceivableDao;
import com.wolfking.jeesite.modules.sd.entity.OrderReceivable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单应收表服务层
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderReceivableService extends LongIDBaseService {

    /**
     * 持久层对象
     */
    @Resource
    protected OrderReceivableDao dao;

    /**
     * 新增
     */
    @Transactional(readOnly = false)
    public void insert(OrderReceivable entity) {
        dao.insert(entity);
    }

    /**
     * 按订单id读取
     * @param orderId
     * @param quarter
     * @return
     */
    public List<OrderReceivable> getByOrderId(long orderId, String quarter){
        if(orderId <= 0 || StringUtils.isBlank(quarter) ){
            return null;
        }
        return dao.getByOrderId(orderId,quarter);
    }

}
