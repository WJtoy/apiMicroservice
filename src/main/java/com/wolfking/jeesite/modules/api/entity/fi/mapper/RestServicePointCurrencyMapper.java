package com.wolfking.jeesite.modules.api.entity.fi.mapper;

import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointCurrency;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class RestServicePointCurrencyMapper extends CustomMapper<RestServicePointCurrency, EngineerCurrency> {
    @Override
    public void mapAtoB(RestServicePointCurrency a, EngineerCurrency b, MappingContext context) {

    }

    @Override
    public void mapBtoA(EngineerCurrency b, RestServicePointCurrency a, MappingContext context) {
        a.setActionTypeValue(b.getActionType());
        a.setBeforeBalance(b.getBeforeBalance());
        a.setBalance(b.getBalance());
        String remarks = b.getRemarks();
        if (b.getActionType().equals(50)) {
            a.setAmount(-b.getAmount());
            remarks = StringUtils.substringBeforeLast(remarks, "结师傅付款") + "结师傅付款";
        } else {
            a.setAmount(b.getAmount());
        }
        a.setCurrencyNo(b.getCurrencyNo());
        a.setDescription(remarks);
        a.setCreateDate(b.getCreateDate());
    }
}
