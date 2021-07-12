package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDBrand;
import com.wolfking.jeesite.modules.md.entity.Brand;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDBrandMapper extends CustomMapper<MDBrand, Brand> {
    @Override
    public void mapAtoB(MDBrand a, Brand b, MappingContext context) {
        b.setId(a.getId());
        b.setCode(a.getCode());
        b.setName(a.getName());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(Brand b, MDBrand a, MappingContext context) {
        a.setId(b.getId());
        a.setCode(b.getCode());
        a.setName(b.getName());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
