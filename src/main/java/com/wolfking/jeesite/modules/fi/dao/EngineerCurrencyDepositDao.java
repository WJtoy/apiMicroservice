package com.wolfking.jeesite.modules.fi.dao;

import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrencyDeposit;
import com.wolfking.jeesite.modules.fi.entity.ServicePointDeposit;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Auther wj
 * @Date 2021/2/22 15:17
 */
@Mapper
public interface EngineerCurrencyDepositDao {

   List<EngineerCurrencyDeposit> getDepositList(@Param("quarter") String quarter,
                                                @Param("servicePointId") Long servicePointId,
                                                @Param("beginCreateDate") Date beginCreateDate,
                                                @Param("endCreateDate") Date endCreateDate,
                                                @Param("page") Page<EngineerCurrencyDeposit> page);

   /**
    * 那月份获取（充值）质保金
    * @param quarter
    * @param servicePointId
    * @param beginCreateDate
    * @param endCreateDate
    * @return
    */
   Double getServicePointDepositRechargeTotalMonth(@Param("quarter") String quarter,
                                            @Param("servicePointId") Long servicePointId,
                                            @Param("beginCreateDate") Date beginCreateDate,
                                            @Param("endCreateDate") Date endCreateDate);

   /**
    * 按月份获取（完工）质保金
    * @param quarter
    * @param servicePointId
    * @param beginCreateDate
    * @param endCreateDate
    * @return
    */
   Double getServicePointDepositCompleteTotalMonth(@Param("quarter") String quarter,
                                            @Param("servicePointId") Long servicePointId,
                                            @Param("beginCreateDate") Date beginCreateDate,
                                            @Param("endCreateDate") Date endCreateDate);





   ServicePointDeposit getServicePointDeposit(@Param("servicePointId") Long servicePointId);


}
