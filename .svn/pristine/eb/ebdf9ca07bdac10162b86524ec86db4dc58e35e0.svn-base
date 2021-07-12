package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDCustomerPrice;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDCustomerPriceMapper extends CustomMapper<MDCustomerPrice, CustomerPrice> {
    @Override
    public void mapAtoB(MDCustomerPrice a, CustomerPrice b, MappingContext context) {
        b.setId(a.getId());
        if(a.getCustomerId() !=null && a.getCustomerId()>0){
            b.setCustomer(new Customer(a.getCustomerId()));
        }
        if(a.getProductId() !=null && a.getProductId()>0){
            b.setProduct(new Product(a.getProductId()));
        }
        if(a.getServiceTypeId() !=null && a.getServiceTypeId()>0){
            b.setServiceType(new ServiceType(a.getServiceTypeId()));
        }
        b.setBlockedPrice(a.getBlockedPrice());
        b.setDiscountPrice(a.getDiscountPrice());
        b.setPrice(a.getPrice());
        b.setPriceType(new Dict(a.getPriceType().toString()));
        if(!a.getUnit().equals(b.getUnit().getValue())){
            b.setUnit(new Dict(a.getUnit()));
        }
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(CustomerPrice b, MDCustomerPrice a, MappingContext context) {
        a.setId(b.getId());
        a.setCustomerId(Optional.ofNullable(b.getCustomer()).map(Customer::getId).orElse(null));
        a.setProductId(Optional.ofNullable(b.getProduct()).map(Product::getId).orElse(null));
        a.setServiceTypeId(Optional.ofNullable(b.getServiceType()).map(ServiceType::getId).orElse(null));
        a.setBlockedPrice(b.getBlockedPrice());
        a.setDiscountPrice(b.getDiscountPrice());
        a.setPrice(b.getPrice());
        if(b.getPriceType()!=null){
            a.setPriceType(b.getPriceType().getIntValue());
        }
        a.setUnit(b.getUnit().getValue());
        a.setDelFlag(b.getDelFlag());
        a.setRemarks(b.getRemarks());
        a.setNewRecord(b.getIsNewRecord());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
