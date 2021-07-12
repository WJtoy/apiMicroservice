package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDMaterialCategory;
import com.wolfking.jeesite.modules.md.entity.MaterialCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDMaterialCategoryMapper extends CustomMapper<MDMaterialCategory, MaterialCategory> {
    @Override
    public void mapAtoB(MDMaterialCategory a, MaterialCategory b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(MaterialCategory b, MDMaterialCategory a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
