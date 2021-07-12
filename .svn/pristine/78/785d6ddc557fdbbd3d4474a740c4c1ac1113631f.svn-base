package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDGradeItem;
import com.wolfking.jeesite.modules.md.entity.Grade;
import com.wolfking.jeesite.modules.md.entity.GradeItem;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDGradeItemMapper extends CustomMapper<MDGradeItem, GradeItem> {
    @Override
    public void mapAtoB(MDGradeItem a, GradeItem b, MappingContext context) {
        b.setId(a.getId());
        b.setGrade((a.getGradeId()==null || a.getGradeId()<=0)?new Grade():new Grade(a.getGradeId()));
        b.setPoint(a.getPoint());
        b.setDictValue(a.getDictValue());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(GradeItem b, MDGradeItem a, MappingContext context) {
        a.setId(b.getId());
        a.setGradeId(b.getGrade() == null?null:b.getGrade().getId());
        a.setPoint(b.getPoint());
        a.setDictValue(b.getDictValue());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
