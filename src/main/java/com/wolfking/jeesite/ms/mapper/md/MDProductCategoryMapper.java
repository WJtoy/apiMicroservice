package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductCategory;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductCategoryMapper  extends CustomMapper<MDProductCategory, ProductCategory> {
    @Override
    public void mapAtoB(MDProductCategory a, ProductCategory b, MappingContext context) {
        b.setId(a.getId());
        b.setCode(a.getCode());
        b.setName(a.getName());
        b.setSort(a.getSort());
        b.setVipFlag(a.getVipFlag());
        b.setRemarks(a.getRemarks());
        b.setGroupCategory(a.getGroupCategory());
        b.setAutoGradeFlag(a.getAutoGradeFlag());
        b.setAppCompleteFlag(a.getAppCompleteFlag());
    }

    @Override
    public void mapBtoA(ProductCategory b, MDProductCategory a, MappingContext context) {
        a.setId(b.getId());
        a.setCode(b.getCode());
        a.setName(b.getName());
        a.setSort(b.getSort());
        a.setVipFlag(b.getVipFlag());
        a.setRemarks(b.getRemarks());
        a.setGroupCategory(b.getGroupCategory());
        a.setAutoGradeFlag(b.getAutoGradeFlag());
        a.setAppCompleteFlag(b.getAppCompleteFlag());
    }
}
