package com.wolfking.jeesite.ms.mapper.sys;

import com.kkl.kklplus.entity.sys.SysDict;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MSDictMapper extends CustomMapper<SysDict, Dict> {

    @Override
    public void mapAtoB(SysDict a, Dict b, MappingContext context) {
        b.setId(a.getId());
        b.setRemarks(a.getRemarks());
        b.setCreateBy(a.getCreateById()==null?null:new User(a.getCreateById()));
        b.setCreateDate(a.getCreateDate());
        b.setUpdateBy(a.getUpdateById()==null?null:new User(a.getUpdateById()));
        b.setUpdateDate(a.getUpdateDate());
        b.setDelFlag(a.getDelFlag());
        b.setIsNewRecord(a.getIsNewRecord());

        b.setParentId(a.getParentId());
        b.setLabel(a.getLabel());
        b.setValue(a.getValue());
        b.setType(a.getType());
        b.setDescription(a.getDescription());
        b.setSort(a.getSort());
        b.setAloneManagement(a.getAloneManagement());
    }

    @Override
    public void mapBtoA(Dict b, SysDict a, MappingContext context) {
        a.setId(b.getId());
        a.setRemarks(b.getRemarks());
        a.setCreateBy(b.getCreateBy());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateBy(b.getUpdateBy());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
        a.setDelFlag(b.getDelFlag());
        a.setNewRecord(b.getIsNewRecord());

        a.setParentId(b.getParentId());
        a.setLabel(b.getLabel());
        a.setValue(b.getValue());
        a.setType(b.getType());
        a.setDescription(b.getDescription());
        a.setSort(b.getSort());
        a.setAloneManagement(b.getAloneManagement());
    }
}
