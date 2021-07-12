package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 完成工单上传图片
 */
@Mapper
public interface OrderItemCompleteDao extends LongIDCrudDao<OrderItemComplete> {

    List<OrderItemComplete> getByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    Integer getProductQty(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("productId") Long productId);

    OrderItemComplete getById(@Param("id") Long id, @Param("quarter") String quarter);

    void updateBarCode(OrderItemComplete entity);


}
