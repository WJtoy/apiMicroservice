package com.wolfking.jeesite.ms.logistics.dao;

import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 物流数据访问层
 *
 * @author Ryan Lu
 * @date 2019/5/27 10:07 AM
 * @since 1.0.0
 */
@Mapper
public interface LogisticsDao extends BaseDao {

    /**
     * 更新
     *
     * @return id、order_no、quarter
     */
    Order getOrderInfo(@Param("dataSource") int dataSource,
                       @Param("b2bOrderNo") String b2bOrderNo,
                       @Param("quarter") String quarter);

    /**
     * 根据工单ID获取工单的数据源ID
     */
    Integer getDataSourceIdByOrderId(@Param("orderId") Long orderId,
                                     @Param("quarter") String quarter);
}
