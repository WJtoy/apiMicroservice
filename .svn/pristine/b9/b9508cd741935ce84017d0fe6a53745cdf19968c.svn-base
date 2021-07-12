package com.wolfking.jeesite.ms.mapper.sys;

import com.kkl.kklplus.entity.sys.SysOffice;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Office;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SysOfficeMapper extends CustomMapper<Office, SysOffice> {
    @Override
    public void mapAtoB(Office a, SysOffice b, MappingContext context) {
        b.setId(a.getId());
        if (a.getParent() != null) {
            Office parentOffice = a.getParent();
            SysOffice parentSysOffice = new SysOffice();
            parentSysOffice.setId(parentOffice.getId());
            b.setParent(parentSysOffice);
        }
        b.setParentIds(a.getParentIds());
        b.setAreaId(Optional.ofNullable(a.getArea()).map(Area::getId).orElse(0L));
        b.setCode(a.getCode());
        b.setName(a.getName());
        b.setType(a.getType());
        b.setGrade(a.getGrade());
        b.setAddress(a.getAddress());
        b.setZipCode(a.getZipCode());
        b.setMaster(a.getMaster());
        b.setPhone(a.getPhone());
        b.setPhone(a.getPhone());
        b.setFax(a.getFax());
        b.setEmail(a.getEmail());
        b.setCreateById(Optional.ofNullable(a.getCreateBy()).map(User::getId).orElse(0L));
        b.setCreateDate(a.getCreateDate());
        b.setUpdateById(Optional.ofNullable(a.getUpdateBy()).map(User::getId).orElse(0L));
        b.setUpdateDate(a.getUpdateDate());
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
        try {
            b.setUseable(Integer.valueOf(a.getUseable()));
        } catch (Exception ex) {
            b.setUseable(0);
        }
        b.setSort(a.getSort());
    }

    @Override
    public void mapBtoA(SysOffice b, Office a, MappingContext context) {
        a.setId(b.getId());
        if (b.getParent() != null) {
            Office parentOffice = new Office();
            SysOffice parentSysOffice = b.getParent();
            parentOffice.setId(parentSysOffice.getId());
            parentOffice.setName(parentSysOffice.getName());
            a.setParent(parentOffice);
        }
        a.setParentIds(b.getParentIds());
        a.setArea(new Area(Optional.ofNullable(b.getAreaId()).orElse(0L)));
        a.setCode(b.getCode());
        a.setName(b.getName());
        a.setType(b.getType());
        a.setGrade(b.getGrade());
        a.setAddress(b.getAddress());
        a.setZipCode(b.getZipCode());
        a.setMaster(b.getMaster());
        a.setPhone(b.getPhone());
        a.setPhone(b.getPhone());
        a.setFax(b.getFax());
        a.setEmail(b.getEmail());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setUseable(Optional.ofNullable(b.getUseable()).orElse(0).toString());
        a.setSort(b.getSort());
    }
}
