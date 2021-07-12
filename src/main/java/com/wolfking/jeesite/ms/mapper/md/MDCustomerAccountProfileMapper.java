package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDCustomerAccountProfile;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDCustomerAccountProfileMapper  extends CustomMapper<MDCustomerAccountProfile,CustomerAccountProfile> {
    @Override
    public void mapAtoB(MDCustomerAccountProfile a, CustomerAccountProfile b, MappingContext context) {
        b.setId(a.getId());
        b.setCustomer(a.getCustomerId()==null?null:new Customer(a.getCustomerId()));
        b.setOrderApproveFlag(a.getOrderApproveFlag());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(CustomerAccountProfile b, MDCustomerAccountProfile a, MappingContext context) {
        a.setId(b.getId());
        a.setCustomerId(b.getCustomer() == null? null:b.getCustomer().getId());
        a.setOrderApproveFlag(b.getOrderApproveFlag());
        a.setRemarks(b.getRemarks());
    }
}
