package com.wolfking.jeesite.ms.b2bcenter.md.mapper;

import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeItemMappingVModel;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-16T18:08:05+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class B2BSurchargeItemMappingMapperImpl extends B2BSurchargeItemMappingMapper {

    @Override
    public B2BSurchargeItemMappingVModel toB2BSurchargeItemMappingVModel(B2BSurchargeItemMapping surchargeItemMapping) {
        if ( surchargeItemMapping == null ) {
            return null;
        }

        B2BSurchargeItemMappingVModel b2BSurchargeItemMappingVModel = new B2BSurchargeItemMappingVModel();

        b2BSurchargeItemMappingVModel.setId( surchargeItemMapping.getId() );
        b2BSurchargeItemMappingVModel.setRemarks( surchargeItemMapping.getRemarks() );
        b2BSurchargeItemMappingVModel.setCreateBy( surchargeItemMapping.getCreateBy() );
        b2BSurchargeItemMappingVModel.setCreateById( surchargeItemMapping.getCreateById() );
        b2BSurchargeItemMappingVModel.setCreateDate( surchargeItemMapping.getCreateDate() );
        b2BSurchargeItemMappingVModel.setUpdateBy( surchargeItemMapping.getUpdateBy() );
        b2BSurchargeItemMappingVModel.setUpdateById( surchargeItemMapping.getUpdateById() );
        b2BSurchargeItemMappingVModel.setUpdateDate( surchargeItemMapping.getUpdateDate() );
        b2BSurchargeItemMappingVModel.setDelFlag( surchargeItemMapping.getDelFlag() );
        b2BSurchargeItemMappingVModel.setPage( surchargeItemMapping.getPage() );
        b2BSurchargeItemMappingVModel.setCreateDt( surchargeItemMapping.getCreateDt() );
        b2BSurchargeItemMappingVModel.setUpdateDt( surchargeItemMapping.getUpdateDt() );
        b2BSurchargeItemMappingVModel.setProcessFlag( surchargeItemMapping.getProcessFlag() );
        b2BSurchargeItemMappingVModel.setProcessTime( surchargeItemMapping.getProcessTime() );
        b2BSurchargeItemMappingVModel.setProcessComment( surchargeItemMapping.getProcessComment() );
        b2BSurchargeItemMappingVModel.setQuarter( surchargeItemMapping.getQuarter() );
        b2BSurchargeItemMappingVModel.setDataSource( surchargeItemMapping.getDataSource() );
        b2BSurchargeItemMappingVModel.setUniqueId( surchargeItemMapping.getUniqueId() );
        b2BSurchargeItemMappingVModel.setB2bOrderId( surchargeItemMapping.getB2bOrderId() );
        b2BSurchargeItemMappingVModel.setAuxiliaryMaterialItemId( surchargeItemMapping.getAuxiliaryMaterialItemId() );
        b2BSurchargeItemMappingVModel.setB2bItemName( surchargeItemMapping.getB2bItemName() );

        return b2BSurchargeItemMappingVModel;
    }
}
