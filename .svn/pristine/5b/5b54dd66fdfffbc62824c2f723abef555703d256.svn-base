package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDPlanRadius;
import com.wolfking.jeesite.modules.md.entity.PlanRadius;
import com.wolfking.jeesite.modules.sys.entity.Area;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDPlanRadiusMapper extends CustomMapper<MDPlanRadius, PlanRadius> {
    @Override
    public void mapAtoB(MDPlanRadius a, PlanRadius b, MappingContext context) {
        b.setId(a.getId());
        b.setAutoPlanFlag(a.getAutoPlanFlag());
        b.setRadius1(a.getRadius1());
        b.setRadius2(a.getRadius2());
        b.setRadius3(a.getRadius3());
        b.setArea(a.getAreaId()==null?null:new Area(a.getAreaId()));
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
    }

    @Override
    public void mapBtoA(PlanRadius b, MDPlanRadius a, MappingContext context) {
        a.setId(b.getId());
        a.setAutoPlanFlag(b.getAutoPlanFlag());
        a.setRadius1(b.getRadius1());
        a.setRadius2(b.getRadius2());
        a.setRadius3(b.getRadius3());
        a.setAreaId(b.getArea()==null?null:b.getArea().getId());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
        if(b.getAreaIdList()!=null && b.getAreaIdList().size()>0){
            a.setAreaIdList(b.getAreaIdList());
        }
    }
}
