package com.wolfking.jeesite.modules.api.entity.md.mapper;

import com.wolfking.jeesite.modules.api.entity.md.RestEngineer;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class RestEngineerMapper extends CustomMapper<RestEngineer, Engineer> {

    @Override
    public void mapAtoB(RestEngineer a, Engineer b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Engineer b, RestEngineer a, MappingContext context) {
        a.setId(b.getId().toString());
        a.setName(b.getName());
        a.setOrderCount(b.getOrderCount());
        a.setPlanCount(b.getPlanCount());
        a.setBreakCount(b.getBreakCount());
        a.setGrade(b.getGrade());
        a.setPhone(b.getContactInfo());
        a.setAddress(b.getAddress());

    }
}
