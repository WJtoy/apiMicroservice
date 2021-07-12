package com.wolfking.jeesite.ms.mapper.sys;

import com.kkl.kklplus.entity.sys.SysArea;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SysAreaMapper  extends CustomMapper<Area, SysArea> {
    @Override
    public void mapAtoB(Area a, SysArea b, MappingContext context) {
        b.setId(a.getId());
        b.setParentIds(a.getParentIds());
        b.setCode(a.getCode());
        b.setName(a.getName());
        b.setFullName(a.getFullName());
        b.setType(a.getType());
        b.setSort(a.getSort());
        b.setCreateById(Optional.ofNullable(a.getCreateBy()).map(User::getId).orElse(0L));
        b.setCreateDate(Optional.ofNullable(a.getCreateDate()).orElse(null));
        b.setUpdateById(Optional.ofNullable(a.getUpdateBy()).map(User::getId).orElse(0L));
        b.setUpdateDate(Optional.ofNullable(a.getUpdateDate()).orElse(null));
        if (a.getParent() != null) {
            Area area = a.getParent();
            SysArea sysArea = new SysArea();
            sysArea.setId(area.getId());
            sysArea.setParentIds(area.getParentIds());
            sysArea.setCode(area.getCode());
            sysArea.setName(area.getName());
            sysArea.setFullName(area.getFullName());
            sysArea.setType(area.getType());
            sysArea.setSort(area.getSort());
            sysArea.setCreateById(Optional.ofNullable(area.getCreateBy()).map(User::getId).orElse(0L));
            sysArea.setCreateDate(Optional.ofNullable(area.getCreateDate()).orElse(null));
            sysArea.setUpdateById(Optional.ofNullable(area.getUpdateBy()).map(User::getId).orElse(0L));
            sysArea.setUpdateDate(Optional.ofNullable(area.getUpdateDate()).orElse(null));

            if (area.getParent() != null) {
                Area parentArea = area.getParent();

                SysArea parentSysArea = new SysArea();
                parentSysArea.setId(parentArea.getId());
                parentSysArea.setParentIds(parentArea.getParentIds());
                parentSysArea.setCode(parentArea.getCode());
                parentSysArea.setName(parentArea.getName());
                parentSysArea.setFullName(parentArea.getFullName());
                parentSysArea.setType(parentArea.getType());
                parentSysArea.setSort(parentArea.getSort());
                parentSysArea.setCreateById(Optional.ofNullable(parentArea.getCreateBy()).map(User::getId).orElse(0L));
                parentSysArea.setCreateDate(Optional.ofNullable(parentArea.getCreateDate()).orElse(null));
                parentSysArea.setUpdateById(Optional.ofNullable(parentArea.getUpdateBy()).map(User::getId).orElse(0L));
                parentSysArea.setUpdateDate(Optional.ofNullable(parentArea.getUpdateDate()).orElse(null));
                sysArea.setParent(parentSysArea);
            }

            b.setParent(sysArea);
        }
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(SysArea b, Area a, MappingContext context) {
        a.setId(b.getId());
        a.setParentIds(b.getParentIds());
        a.setCode(b.getCode());
        a.setName(b.getName());
        a.setFullName(b.getFullName());
        a.setType(b.getType());
        a.setCreateBy(new User(Optional.ofNullable(b.getCreateById()).orElse(0L)));
        a.setCreateDate(Optional.ofNullable(b.getCreateDate()).orElse(null));
        a.setUpdateBy(new User(Optional.ofNullable(b.getUpdateById()).orElse(0L)));
        a.setUpdateDate(Optional.ofNullable(b.getUpdateDate()).orElse(null));
        a.setDelFlag(b.getDelFlag());
        a.setSort(b.getSort());
        a.setRemarks(b.getRemarks());
        if (b.getParent() != null) {
            SysArea sysArea = b.getParent();
            Area area = new Area();
            area.setId(sysArea.getId());
            area.setParentIds(sysArea.getParentIds());
            area.setCode(sysArea.getCode());
            area.setName(sysArea.getName());
            area.setFullName(sysArea.getFullName());
            area.setType(sysArea.getType());
            area.setCreateBy(new User(Optional.ofNullable(sysArea.getCreateById()).orElse(0L)));
            area.setCreateDate(Optional.ofNullable(sysArea.getCreateDate()).orElse(null));
            area.setUpdateBy(new User(Optional.ofNullable(sysArea.getUpdateById()).orElse(0L)));
            area.setUpdateDate(Optional.ofNullable(sysArea.getUpdateDate()).orElse(null));
            area.setDelFlag(sysArea.getDelFlag());
            area.setSort(sysArea.getSort());

            if (sysArea.getParent() != null) {
                SysArea parentSysArea = sysArea.getParent();

                Area parentArea = new Area();
                parentArea.setId(parentSysArea.getId());
                parentArea.setParentIds(parentSysArea.getParentIds());
                parentArea.setCode(parentSysArea.getCode());
                parentArea.setName(parentSysArea.getName());
                parentArea.setFullName(parentSysArea.getFullName());
                parentArea.setType(parentSysArea.getType());
                parentArea.setCreateBy(new User(Optional.ofNullable(parentSysArea.getCreateById()).orElse(0L)));
                parentArea.setCreateDate(Optional.ofNullable(parentSysArea.getCreateDate()).orElse(null));
                parentArea.setUpdateBy(new User(Optional.ofNullable(parentSysArea.getUpdateById()).orElse(0L)));
                parentArea.setUpdateDate(Optional.ofNullable(parentSysArea.getUpdateDate()).orElse(null));
                parentArea.setDelFlag(parentSysArea.getDelFlag());
                parentArea.setSort(parentSysArea.getSort());

                area.setParent(parentArea);
            }

            a.setParent(area);
        }
    }
}
