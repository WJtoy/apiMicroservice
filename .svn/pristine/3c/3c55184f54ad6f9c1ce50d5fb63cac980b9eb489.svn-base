package com.wolfking.jeesite.modules.api.entity.fi.mapper;

import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointBalance;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class RestServicePointBalanceMapper extends CustomMapper<RestServicePointBalance, ServicePointFinance> {
    @Override
    public void mapAtoB(RestServicePointBalance a, ServicePointFinance b, MappingContext context) {

    }

    @Override
    public void mapBtoA(ServicePointFinance b, RestServicePointBalance a, MappingContext context) {
        a.setBalance(b.getBalance());
        a.setPayable(b.getBalance());
        a.setTotalPayable(b.getBalance() + b.getTotalAmount());
        a.setTotalPaid(b.getTotalAmount());
        if (b.getLastPayAmount() > 0) {
            a.setLastPayAmount(b.getLastPayAmount());
            a.setLastPayDate(b.getLastPayDate() == null ? null : b.getLastPayDate().getTime());
        }
    }
}
