package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderVoiceTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单智能回访记录
 *
 * @author Ryan
 * @date 2019-01-15
 */
@Mapper
public interface OrderVoiceTaskDao extends LongIDCrudDao<OrderVoiceTask> {

    OrderVoiceTask getByOrderId(@Param("quarter") String quarter, @Param("orderId") Long orderId);

    OrderVoiceTask getBaseInfoByOrderId(@Param("quarter") String quarter, @Param("orderId") Long orderId);
    
    Integer cancel(@Param("quarter") String quarter, @Param("orderId") Long orderId, @Param("updateDate") Long updateDate);

}
