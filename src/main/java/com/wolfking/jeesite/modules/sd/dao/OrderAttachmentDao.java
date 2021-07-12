/**
 * Copyright &copy; 2014-2014 All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件dao接口
 */
@Mapper
public interface OrderAttachmentDao extends LongIDCrudDao<OrderAttachment> {

    /**
     * 按订单id返回订单下所有附件
     * @param orderId
     * @return
     */
    List<OrderAttachment> getByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter);

}
