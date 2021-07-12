package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDUrgentLevel;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;


@Component
public class MDUrgentLevelMapper extends CustomMapper<MDUrgentLevel, UrgentLevel> {
    @Override
    public void mapAtoB(MDUrgentLevel a, UrgentLevel b, MappingContext context) {
        b.setId(a.getId());
        b.setLabel(a.getLabel());
        b.setChargeIn(a.getChargeIn());
        b.setChargeOut(a.getChargeOut());
        b.setSort(a.getSort());
        b.setMarkBgcolor(a.getMarkBgcolor());
        b.setCreateDate(a.getCreateDate());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(UrgentLevel b, MDUrgentLevel a, MappingContext context) {
        a.setId(b.getId());
        a.setLabel(b.getLabel());
        a.setChargeIn(b.getChargeIn());
        a.setChargeOut(b.getChargeOut());
        a.setSort(b.getSort());
        a.setMarkBgcolor(b.getMarkBgcolor());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
