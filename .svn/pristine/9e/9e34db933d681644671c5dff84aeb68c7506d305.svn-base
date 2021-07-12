/**
 * Copyright &copy; 2014-2014 All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.OrderOpitionTrace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 工单意见记录接口
 * @author Ryan
 * @version 2020-01-08
 */
@Mapper
public interface OrderOpitionTraceDao extends LongIDCrudDao<OrderOpitionTrace> {


    /**
     * 按网点Id读取同反馈类型反馈次数
     * @param orderId
     * @param quarter
     * @param opinionId
     * @param servicePointId
     * @return
     */
    Integer getTimesByServicepoint(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("opinionId") Integer opinionId, @Param("servicePointId") Long servicePointId);

    /**
     * 按反馈类型读取反馈总次数
     * @param orderId
     * @param quarter
     * @param opinionId
     * @return
     */
    Integer getTotalTimesByOpinionType(@Param("orderId") Long orderId, @Param("quarter") String quarter, @Param("opinionId") Integer opinionId);


}
