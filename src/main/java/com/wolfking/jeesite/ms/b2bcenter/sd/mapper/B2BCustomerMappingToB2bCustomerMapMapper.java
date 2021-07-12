package com.wolfking.jeesite.ms.b2bcenter.sd.mapper;

import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * B2BCustomerMapping <-> B2bCustomerMap
 */
@Component
public class B2BCustomerMappingToB2bCustomerMapMapper extends CustomMapper<B2BCustomerMapping, B2bCustomerMap> {

    @Override
    public void mapAtoB(B2BCustomerMapping a, B2bCustomerMap b, MappingContext context) {
        b.setId(a.getId());
        b.setDataSource(a.getDataSource());
        b.setCustomerId(a.getCustomerId());
        b.setCustomerName(a.getCustomerName());
        b.setShopId(a.getShopId());
        b.setShopName(a.getShopName());
    }

    @Override
    public void mapBtoA(B2bCustomerMap b, B2BCustomerMapping a, MappingContext context) {
        b.setId(a.getId());
        b.setDataSource(a.getDataSource());
        b.setCustomerId(a.getCustomerId());
        b.setCustomerName(a.getCustomerName());
        b.setShopId(a.getShopId());
        b.setShopName(a.getShopName());
    }
}
