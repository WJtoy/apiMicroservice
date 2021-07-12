package com.wolfking.jeesite.ms.b2bcenter.md.mapper;


import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeCategoryMappingVModel;
import org.mapstruct.Mapper;

@Mapper
public abstract class B2BSurchargeCategoryMappingMapper {

    public abstract B2BSurchargeCategoryMappingVModel toB2BSurchargeCategoryMappingVModel(B2BSurchargeCategoryMapping surchargeCategoryMapping);

}
