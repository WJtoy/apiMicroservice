package com.wolfking.jeesite.modules.mq.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.mq.entity.OrderCreateBody;
import com.wolfking.jeesite.modules.mq.entity.OrderReport;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 下单消息队列
 * Created by Ryan on 2017/4/19.
 */
@Mapper
public interface OrderCreateMessageDao extends LongIDCrudDao<OrderCreateBody> {

    OrderCreateBody getByOrderId(@Param("quarter") String quarter,@Param("orderId") long orderId);
}
