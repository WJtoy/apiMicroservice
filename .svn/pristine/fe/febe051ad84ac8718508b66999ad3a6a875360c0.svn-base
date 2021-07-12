package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDMaterial;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.MaterialCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDMaterialMapper extends CustomMapper<MDMaterial, Material> {
    @Override
    public void mapAtoB(MDMaterial a, Material b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setModel(a.getModel());
        b.setPrice(a.getPrice());
        b.setIsReturn(a.getIsReturn());
        b.setMaterialCategory(a.getCategoryId()==null?new MaterialCategory():new MaterialCategory(a.getCategoryId()));
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(Material b, MDMaterial a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setModel(b.getModel());
        a.setPrice(b.getPrice());
        a.setIsReturn(b.getIsReturn());
        a.setCategoryId(b.getMaterialCategory()==null?null:b.getMaterialCategory().getId());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
