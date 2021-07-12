package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单地理信息表数据访问接口
 * Created on 2019-04-25.
 */
@Mapper
public interface OrderLocationDao extends LongIDCrudDao<OrderLocation> {
    OrderLocation getByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);
}
