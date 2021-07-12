package com.wolfking.jeesite.ms.globalmapping.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCategoryServicePointMapper {

    //List<Long> findListByProductCategoryId(@Param("productCategoryId") Long productCategoryId);  mark on 2020-6-12

    /**
     * 批量按网点id检查网点是否有指定品类权限
     * @param sids  网点id列表  ，不超过100个
     * @param productCategoryId 品类id
     * @return
     */
    /*
    // mark on 2020-6-12 begin
    List<Long> findListByProductCategoryIdAndServicePointIds(@Param("servicePointIds") List<Long> sids,@Param("productCategoryId") Long productCategoryId);

    List<Long> getByServicePointId(@Param("servicePointId") Long servicePointId);

    int insertProductCategoryIds(@Param("servicePointId") Long servicePointId,
                                 @Param("productCategoryIds") List<Long> productCategoryIds);

    int deleteByServicePointId(@Param("servicePointId") Long servicePointId);
    // mark on 2020-6-12 end
    */
}
