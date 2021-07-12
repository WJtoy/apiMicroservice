package com.wolfking.jeesite.modules.mq.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.mq.dao.OrderCreateMessageDao;
import com.wolfking.jeesite.modules.mq.entity.OrderCreateBody;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ryan on 2017/08/1.
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OrderCreateMessageService extends LongIDBaseService {

    @Resource
    private OrderCreateMessageDao dao;

    public OrderCreateBody get(long id) {
        return dao.get(id);
    }

    public OrderCreateBody getByOrderId(String quarter,long orderId) {
        return dao.getByOrderId(quarter,orderId);
    }

    @Transactional()
    public int insert(OrderCreateBody entity) {
        return dao.insert(entity);
    }

    @Transactional()
    public void update(OrderCreateBody entity) {
        dao.update(entity);
    }


}
