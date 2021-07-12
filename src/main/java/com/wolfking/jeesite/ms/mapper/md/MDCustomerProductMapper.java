package com.wolfking.jeesite.ms.mapper.md;
import com.kkl.kklplus.entity.md.MDCustomerProduct;
import com.wolfking.jeesite.modules.md.entity.*;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDCustomerProductMapper extends CustomMapper<MDCustomerProduct, CustomerProduct> {
    @Override
    public void mapAtoB(MDCustomerProduct a, CustomerProduct b, MappingContext context) {
        b.setId(a.getId());
        b.setCustomer(a.getCustomerId()==null?null:new Customer(a.getCustomerId()));
        b.setProduct(a.getProductId()==null?null:new Product(a.getProductId()));
        b.setFixSpec(a.getFixSpec());
        b.setFixSpecification(a.getFixSpecification());
        b.setVideoUrl(a.getVideoUrl());
        b.setRemoteFeeFlag(a.getRemoteFeeFlag());
    }

    @Override
    public void mapBtoA(CustomerProduct b, MDCustomerProduct a, MappingContext context) {
        a.setId(b.getId());
        a.setCustomerId(Optional.ofNullable(b.getCustomer()).map(Customer::getId).orElse(null));
        a.setProductId(Optional.ofNullable(b.getProduct()).map(Product::getId).orElse(null));
        a.setFixSpec(b.getFixSpec());
        a.setFixSpecification(b.getFixSpecification());
        a.setVideoUrl(b.getVideoUrl());
        a.setRemoteFeeFlag(b.getRemoteFeeFlag());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
