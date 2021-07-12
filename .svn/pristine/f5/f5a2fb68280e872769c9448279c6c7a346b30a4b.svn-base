package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDCustomerUrgent;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.UrgentCustomer;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.modules.sys.entity.Area;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDCustomerUrgentMapper extends CustomMapper<MDCustomerUrgent, UrgentCustomer> {
    @Override
    public void mapAtoB(MDCustomerUrgent a, UrgentCustomer b, MappingContext context) {
        b.setId(a.getId());
        b.setCustomer(a.getCustomerId() == null? null:new Customer(a.getCustomerId()));
        b.setArea(a.getAreaId() == null? null:new Area(a.getAreaId()));
        b.setUrgentLevel(a.getUrgentLevelId()==null? null:new UrgentLevel(a.getUrgentLevelId()));
        b.setChargeIn(a.getChargeIn());
        b.setChargeOut(a.getChargeOut());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(UrgentCustomer b, MDCustomerUrgent a, MappingContext context) {
        a.setId(b.getId());
        a.setCustomerId(b.getCustomer()==null?null:b.getCustomer().getId());
        a.setAreaId(b.getArea()==null?null:b.getArea().getId());
        a.setUrgentLevelId(b.getUrgentLevel()==null?null:b.getUrgentLevel().getId());
        a.setChargeIn(b.getChargeIn());
        a.setChargeOut(b.getChargeOut());
        a.setRemarks(b.getRemarks());
    }
}
