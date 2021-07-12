package com.wolfking.jeesite.modules.sd.entity.mapper;

import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sd.entity.OrderInsurance;
import com.wolfking.jeesite.modules.sd.entity.OrderServicePointFee;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * OrderInsurance <-> OrderServicePointFee
 */
@Component
public class OrderInsuranceToSevicePointFeeMapper extends CustomMapper<OrderInsurance, OrderServicePointFee>{

    @Override
    public void mapAtoB(OrderInsurance a, OrderServicePointFee b, MappingContext context) {
        b.setOrderId(a.getOrderId());
        b.setQuarter(a.getQuarter());
        b.setServicePoint(new ServicePoint(a.getServicePointId()));
        b.setInsuranceCharge(0-a.getAmount());
        b.setOrderCharge(0-a.getAmount());
        b.setInsuranceNo(a.getInsuranceNo());
    }

    @Override
    public void mapBtoA(OrderServicePointFee b, OrderInsurance a, MappingContext context) {
        //a.setProduct(b.getProduct());
        //a.setServiceType(b.getServiceType());
        //a.setQty(b.getQty());
        //a.setServiceTimes(1);
        //a.setDelFlag(0);
    }
}
