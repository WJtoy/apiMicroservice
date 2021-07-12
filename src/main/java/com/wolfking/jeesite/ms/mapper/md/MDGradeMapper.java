package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDGrade;
import com.wolfking.jeesite.modules.md.entity.Grade;
import com.wolfking.jeesite.modules.md.entity.GradeItem;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MDGradeMapper extends CustomMapper<MDGrade, Grade> {

    @Autowired
    private MapperFacade mapper;

    @Override
    public void mapAtoB(MDGrade a, Grade b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setPoint(a.getPoint());
        b.setSort(a.getSort());
        b.setDictType(a.getDictType());
        if(a.getGradeItemList()!=null && a.getGradeItemList().size()>0){
            List<GradeItem> list = mapper.mapAsList(a.getGradeItemList(),GradeItem.class);
            if(list!=null && list.size()>0){
                b.setItemList(list);
            }
        }
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(Grade b, MDGrade a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setPoint(b.getPoint());
        a.setSort(b.getSort());
        a.setDictType(b.getDictType());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
