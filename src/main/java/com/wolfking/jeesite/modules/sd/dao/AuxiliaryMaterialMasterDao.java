package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterial;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterialMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuxiliaryMaterialMasterDao extends BaseDao {

    AuxiliaryMaterialMaster getAuxiliaryMaterialMasterByOrderId(@Param("orderId") Long orderId,
                                                               @Param("quarter") String quarter);

    AuxiliaryMaterialMaster getAuxiliaryMaterialMasterPrice(@Param("orderId") Long orderId, @Param("quarter") String quarter);

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(AuxiliaryMaterialMaster entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(AuxiliaryMaterialMaster entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param entity
     * @return
     */
    int delete(AuxiliaryMaterialMaster entity);
}
