package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuxiliaryMaterialDao extends LongIDCrudDao<AuxiliaryMaterial> {

    List<AuxiliaryMaterial> getAuxiliaryMaterialsByOrderId(@Param("orderId") Long orderId,
                                                           @Param("quarter") String quarter,
                                                           @Param("delFlag") Integer delFlag);

    Integer hasAuxiliaryMaterials(@Param("orderId") Long orderId,
                                  @Param("quarter") String quarter);

}
