package com.wolfking.jeesite.modules.api.entity.md.mapper;

import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.wolfking.jeesite.modules.api.entity.md.RestRepairAction;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class RestRepairActionMapper extends CustomMapper<RestRepairAction, MDActionCodeDto> {

    @Override
    public void mapAtoB(RestRepairAction a, MDActionCodeDto b, MappingContext context) {

    }

    @Override
    public void mapBtoA(MDActionCodeDto b, RestRepairAction a, MappingContext context) {
        if (b == null) {
            a = null;
            return;
        }

        a.setKey(b.getId() == null ? "0" : b.getId().toString());
        a.setValue(b.getName());
        a.setServiceTypeId(b.getServiceTypeId());
        a.setServiceTypeName(b.getServiceTypeName());
    }
}
