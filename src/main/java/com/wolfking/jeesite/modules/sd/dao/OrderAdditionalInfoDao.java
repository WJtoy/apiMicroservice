package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OrderAdditionalInfoDao extends BaseDao {

    Order getOrderAdditionalInfo(@Param("orderId") Long orderId,
                                 @Param("quarter") String quarter);

}
