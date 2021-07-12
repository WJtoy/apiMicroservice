package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDEngineer;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDEngineerMapper extends CustomMapper<MDEngineer, Engineer> {
    @Override
    public void mapAtoB(MDEngineer a, Engineer b, MappingContext context) {
        b.setId(a.getId());
        b.setServicePoint(Optional.ofNullable(a.getServicePointId()).map(r->new ServicePoint(r)).orElse(null));
        b.setName(a.getName());
        b.setLevel(new Dict(Optional.ofNullable(a.getLevel()).orElse(0),""));
        b.setContactInfo(a.getContactInfo());
        b.setArea(Optional.ofNullable(a.getAreaId()).map(r->new Area(r)).orElse(null));
        b.setAddress(a.getAddress());
        b.setGrade(a.getGrade());
        b.setQq(a.getQq());
        b.setMasterFlag(a.getMasterFlag());
        b.setAppFlag(a.getAppFlag());
        b.setOrderCount(Optional.ofNullable(a.getOrderCount()).orElse(0));
        b.setPlanCount(Optional.ofNullable(a.getPlanCount()).orElse(0));
        b.setBreakCount(Optional.ofNullable(a.getBreakCount()).orElse(0));
        b.setForTmall(a.getForTmall());
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
        b.setReminderCount(a.getReminderCount());
        b.setComplainCount(a.getComplainCount());
        b.setEngineerAddress(a.getEngineerAddress());
        b.setTemperature(a.getTemperature());
    }

    @Override
    public void mapBtoA(Engineer b, MDEngineer a, MappingContext context) {
        a.setId(b.getId());
        a.setServicePointId(Optional.ofNullable(b.getServicePoint()).map(ServicePoint::getId).orElse(null));
        a.setName(b.getName());
        a.setLevel(Optional.ofNullable(b.getLevel()).map(Dict::getValue).map(Integer::valueOf).orElse(null));
        a.setContactInfo(b.getContactInfo());
        a.setAreaId(Optional.ofNullable(b.getArea()).map(Area::getId).orElse(null));
        a.setAddress(b.getAddress());
        a.setGrade(b.getGrade());
        a.setQq(b.getQq());
        a.setMasterFlag(b.getMasterFlag());
        a.setAppFlag(b.getAppFlag());
        a.setOrderCount(b.getOrderCount());
        a.setPlanCount(b.getPlanCount());
        a.setBreakCount(b.getBreakCount());
        a.setForTmall(b.getForTmall());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setReminderCount(b.getReminderCount());
        a.setComplainCount(b.getComplainCount());
        a.setEngineerAddress(b.getEngineerAddress());
        a.setTemperature(b.getTemperature());
        a.setUpdateById(Optional.ofNullable(b.getUpdateBy()).map(User::getId).orElse(0L));
        a.setUpdateDate(Optional.ofNullable(b.getUpdateDate()).orElse(new java.util.Date()));
    }
}
