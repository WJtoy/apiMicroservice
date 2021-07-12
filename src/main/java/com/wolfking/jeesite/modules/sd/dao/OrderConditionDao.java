package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.modules.sd.entity.OrderConditionStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther wj
 * @Date 2021/3/15 10:32
 */
@Mapper
public interface OrderConditionDao {

    OrderConditionStatus getOrderInfo(@Param("orderId")Long orderId, @Param("quarter")String quarter);


}
