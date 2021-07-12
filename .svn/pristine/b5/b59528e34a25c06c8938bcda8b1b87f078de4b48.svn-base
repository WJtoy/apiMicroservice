package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductPrice;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductPrice;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductPriceMapper extends CustomMapper<MDProductPrice, ProductPrice> {
    @Override
    public void mapAtoB(MDProductPrice a, ProductPrice b, MappingContext context) {
        b.setId(a.getId());
        b.setProduct(a.getProductId()==null?null:new Product(a.getProductId()));
        b.setServiceType(a.getServiceTypeId()==null?null:new ServiceType(a.getServiceTypeId()));
        b.setCustomerStandardPrice(a.getCustomerStandardPrice());
        b.setCustomerDiscountPrice(a.getCustomerDiscountPrice());
        b.setEngineerStandardPrice(a.getEngineerStandardPrice());
        b.setEngineerDiscountPrice(a.getEngineerDiscountPrice());
        Dict priceType = a.getPriceType()==null?null:MSDictUtils.getDictByValue(a.getPriceType().toString(), "PriceType");
        b.setPriceType(priceType==null?null:priceType);
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
    }

    @Override
    public void mapBtoA(ProductPrice b, MDProductPrice a, MappingContext context) {
        a.setId(b.getId());
        a.setProductId(b.getProduct()==null?null:b.getProduct().getId());
        a.setServiceTypeId(b.getServiceType()==null?null:b.getServiceType().getId());
        a.setCustomerStandardPrice(b.getCustomerStandardPrice());
        a.setCustomerDiscountPrice(b.getCustomerDiscountPrice());
        a.setEngineerStandardPrice(b.getEngineerStandardPrice());
        a.setEngineerDiscountPrice(b.getEngineerDiscountPrice());
        a.setPriceType(b.getPriceType()==null?null:b.getPriceType().getIntValue());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setNewRecord(b.getIsNewRecord());
    }
}
