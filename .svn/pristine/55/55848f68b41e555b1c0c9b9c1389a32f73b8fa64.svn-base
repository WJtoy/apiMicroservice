package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDTimelinessLevel;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDTimelinessLevelMapper extends CustomMapper<MDTimelinessLevel, TimelinessLevel> {
    @Override
    public void mapAtoB(MDTimelinessLevel a, TimelinessLevel b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setChargeIn(a.getChargeIn());
        b.setChargeOut(a.getChargeOut());
        b.setSort(a.getSort());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(TimelinessLevel b, MDTimelinessLevel a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setChargeIn(b.getChargeIn());
        a.setChargeOut(b.getChargeOut());
        a.setSort(b.getSort());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
