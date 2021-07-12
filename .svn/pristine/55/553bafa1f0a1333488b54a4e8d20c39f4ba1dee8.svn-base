package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDAreaTimeLiness;
import com.wolfking.jeesite.modules.md.entity.AreaTimeLiness;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDAreaTimeLinessMapper extends CustomMapper<MDAreaTimeLiness, AreaTimeLiness> {
    @Override
    public void mapAtoB(MDAreaTimeLiness a, AreaTimeLiness b, MappingContext context) {
        b.setId(a.getId());
        b.setArea(Optional.ofNullable(a.getAreaId()).map(r->new Area(r)).orElse(null));
        b.setIsOpen(a.getIsOpen());
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
        b.setProductCategoryId(a.getProductCategoryId());
    }

    @Override
    public void mapBtoA(AreaTimeLiness b, MDAreaTimeLiness a, MappingContext context) {
        a.setId(b.getId());
        a.setAreaId(Optional.ofNullable(b.getArea()).map(Area::getId).orElse(null));
        a.setIsOpen(b.getIsOpen());
        a.setProductCategoryId(b.getProductCategoryId());
        a.setNewRecord(b.getIsNewRecord());
        a.setCreateById(Optional.ofNullable(b.getCreateBy()).map(User::getId).orElse(null));
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(Optional.ofNullable(b.getUpdateBy()).map(User::getId).orElse(null));
        a.setUpdateDate(b.getUpdateDate());
    }
}
