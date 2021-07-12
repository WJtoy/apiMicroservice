package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDCustomerTimeliness;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerTimeliness;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.modules.sys.entity.Area;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDCustomerTimelinessMapper extends CustomMapper<MDCustomerTimeliness, CustomerTimeliness> {
    @Override
    public void mapAtoB(MDCustomerTimeliness a, CustomerTimeliness b, MappingContext context) {
        b.setId(a.getId());
        b.setCustomer(a.getCustomerId() == null? null:new Customer(a.getCustomerId()));
        b.setArea(a.getAreaId() == null? null:new Area(a.getAreaId()));
        b.setTimelinessLevel(a.getTimelinessLevelId()==null? null:new TimelinessLevel(a.getTimelinessLevelId()));
        b.setChargeIn(a.getChargeIn());
        b.setChargeOut(a.getChargeOut());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(CustomerTimeliness b, MDCustomerTimeliness a, MappingContext context) {
        a.setId(b.getId());
        a.setCustomerId(b.getCustomer()==null?null:b.getCustomer().getId());
        a.setAreaId(b.getArea()==null?null:b.getArea().getId());
        a.setTimelinessLevelId(b.getTimelinessLevel()==null?null:b.getTimelinessLevel().getId());
        a.setChargeIn(b.getChargeIn());
        a.setChargeOut(b.getChargeOut());
        a.setRemarks(b.getRemarks());
    }
}
