package com.wolfking.jeesite.modules.fi.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeff on 2017/4/19.
 */
@Mapper
public interface EngineerCurrencyDao extends LongIDCrudDao<EngineerCurrency> {

    List<EngineerCurrency> getServicePointCurrencyListForApi(@Param("servicePointId") Long servicePointId,
                                                             @Param("actionType") Integer actionType,
                                                             @Param("beginDate") Date beginDate,
                                                             @Param("endDate") Date endDate,
                                                             @Param("currencyNo") String currencyNo,
                                                             @Param("quarters") List<String> quarters,
                                                             @Param("page") Page<EngineerCurrency> page);

    /**
     * 网点帐户明细按月汇总
     */
    List<Map<String, Object>> getServicePointCurrencySummryByMonthApi(@Param("servicePointId") Long servicePointId,
                                                                      @Param("actionType") Integer actionType,
                                                                      @Param("beginDate") Date beginDate,
                                                                      @Param("endDate") Date endDate);

    EngineerCurrency getFirstCurrency(@Param("servicePointId") long servicePointId);

}
