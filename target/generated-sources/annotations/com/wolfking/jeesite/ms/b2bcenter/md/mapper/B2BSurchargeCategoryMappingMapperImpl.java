package com.wolfking.jeesite.ms.b2bcenter.md.mapper;

import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeCategoryMappingVModel;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:06+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class B2BSurchargeCategoryMappingMapperImpl extends B2BSurchargeCategoryMappingMapper {

    @Override
    public B2BSurchargeCategoryMappingVModel toB2BSurchargeCategoryMappingVModel(B2BSurchargeCategoryMapping surchargeCategoryMapping) {
        if ( surchargeCategoryMapping == null ) {
            return null;
        }

        B2BSurchargeCategoryMappingVModel b2BSurchargeCategoryMappingVModel = new B2BSurchargeCategoryMappingVModel();

        b2BSurchargeCategoryMappingVModel.setId( surchargeCategoryMapping.getId() );
        b2BSurchargeCategoryMappingVModel.setRemarks( surchargeCategoryMapping.getRemarks() );
        b2BSurchargeCategoryMappingVModel.setCreateBy( surchargeCategoryMapping.getCreateBy() );
        b2BSurchargeCategoryMappingVModel.setCreateById( surchargeCategoryMapping.getCreateById() );
        b2BSurchargeCategoryMappingVModel.setCreateDate( surchargeCategoryMapping.getCreateDate() );
        b2BSurchargeCategoryMappingVModel.setUpdateBy( surchargeCategoryMapping.getUpdateBy() );
        b2BSurchargeCategoryMappingVModel.setUpdateById( surchargeCategoryMapping.getUpdateById() );
        b2BSurchargeCategoryMappingVModel.setUpdateDate( surchargeCategoryMapping.getUpdateDate() );
        b2BSurchargeCategoryMappingVModel.setDelFlag( surchargeCategoryMapping.getDelFlag() );
        b2BSurchargeCategoryMappingVModel.setPage( surchargeCategoryMapping.getPage() );
        b2BSurchargeCategoryMappingVModel.setCreateDt( surchargeCategoryMapping.getCreateDt() );
        b2BSurchargeCategoryMappingVModel.setUpdateDt( surchargeCategoryMapping.getUpdateDt() );
        b2BSurchargeCategoryMappingVModel.setProcessFlag( surchargeCategoryMapping.getProcessFlag() );
        b2BSurchargeCategoryMappingVModel.setProcessTime( surchargeCategoryMapping.getProcessTime() );
        b2BSurchargeCategoryMappingVModel.setProcessComment( surchargeCategoryMapping.getProcessComment() );
        b2BSurchargeCategoryMappingVModel.setQuarter( surchargeCategoryMapping.getQuarter() );
        b2BSurchargeCategoryMappingVModel.setDataSource( surchargeCategoryMapping.getDataSource() );
        b2BSurchargeCategoryMappingVModel.setUniqueId( surchargeCategoryMapping.getUniqueId() );
        b2BSurchargeCategoryMappingVModel.setB2bOrderId( surchargeCategoryMapping.getB2bOrderId() );
        b2BSurchargeCategoryMappingVModel.setAuxiliaryMaterialCategoryId( surchargeCategoryMapping.getAuxiliaryMaterialCategoryId() );
        b2BSurchargeCategoryMappingVModel.setB2bCategoryName( surchargeCategoryMapping.getB2bCategoryName() );

        return b2BSurchargeCategoryMappingVModel;
    }
}
