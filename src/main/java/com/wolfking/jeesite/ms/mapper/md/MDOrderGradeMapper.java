package com.wolfking.jeesite.ms.mapper.md;

import com.wolfking.jeesite.modules.md.entity.Grade;
import com.wolfking.jeesite.modules.sd.entity.OrderGrade;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;


@Component
public class MDOrderGradeMapper extends CustomMapper<Grade, OrderGrade> {

    @Override
    public void mapAtoB(Grade a, OrderGrade b, MappingContext context) {
        b.setId(a.getId());
        b.setGradeName(a.getName());
        b.setSort(a.getSort());
        b.setDictType(a.getDictType());
        b.setPoint(a.getPoint());
        b.setRemarks(a.getRemarks());
        if(a.getItemList()!=null && a.getItemList().size()>0){
            b.setItems(a.getItemList());
        }
    }
}
