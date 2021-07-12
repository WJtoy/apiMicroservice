package com.wolfking.jeesite.ms.b2bcenter.md.mapper;


import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeItemMappingVModel;
import org.mapstruct.Mapper;

@Mapper
public abstract class B2BSurchargeItemMappingMapper {

    public abstract B2BSurchargeItemMappingVModel toB2BSurchargeItemMappingVModel(B2BSurchargeItemMapping surchargeItemMapping);
}
