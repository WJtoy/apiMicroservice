package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.BaseDao;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AuxiliaryMaterialDao extends BaseDao {

    List<AuxiliaryMaterial> getAuxiliaryMaterialsByOrderId(@Param("orderId") Long orderId,
                                                           @Param("quarter") String quarter,
                                                           @Param("delFlag") Integer delFlag);

    Integer hasAuxiliaryMaterials(@Param("orderId") Long orderId,
                                  @Param("quarter") String quarter);


    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    AuxiliaryMaterial get(long id);


    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(AuxiliaryMaterial entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(AuxiliaryMaterial entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     *
     * @param entity
     * @return
     */
    int delete(AuxiliaryMaterial entity);

    int deleteByOrderId(@Param("orderId") Long orderId, @Param("quarter") String quarter,
                        @Param("updateById") Long updateById, @Param("updateDate")Date updateDate);

}
