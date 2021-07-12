package com.wolfking.jeesite.modules.sd.entity.mapper;

import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * OrderDetail <-> OrderItem
 */
@Component
public class OrderItemToDetailMapper extends CustomMapper<OrderDetail, OrderItem>{

    @Override
    public void mapAtoB(OrderDetail a, OrderItem b, MappingContext context) {

    }

    @Override
    public void mapBtoA(OrderItem b, OrderDetail a, MappingContext context) {
        a.setProduct(b.getProduct());
        a.setServiceType(b.getServiceType());
        a.setQty(b.getQty());
        a.setServiceTimes(1);
        a.setDelFlag(0);
    }
}
