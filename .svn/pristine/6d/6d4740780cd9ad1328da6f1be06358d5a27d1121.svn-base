package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterialMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuxiliaryMaterialMasterDao extends LongIDCrudDao<AuxiliaryMaterialMaster> {

    AuxiliaryMaterialMaster getAuxiliaryMaterialMasterByOrderId(@Param("orderId") Long orderId,
                                                               @Param("quarter") String quarter);
}
