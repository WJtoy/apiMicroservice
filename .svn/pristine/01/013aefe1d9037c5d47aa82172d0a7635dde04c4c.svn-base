package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductPicMapping;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductPicMappingMapper extends CustomMapper<MDProductPicMapping, ProductCompletePic> {
    @Override
    public void mapAtoB(MDProductPicMapping a, ProductCompletePic b, MappingContext context) {
        b.setId(a.getId());
        //b.setProduct(a.getProductId()==null?null:new Product(a.getProductId()));
        Product product = new Product();
        product.setId(a.getProductId());
        product.setName(a.getProductName());
        b.setProduct(product);
        b.setJsonInfo(a.getJsonInfo());
        if(StringUtils.isNotBlank(b.getJsonInfo())){
           b.parseItemsFromJson();
        }
        b.setCustomer(a.getCustomerId()==null?null:new Customer(a.getCustomerId()));
        b.setBarcodeMustFlag(a.getBarcodeMustFlag());
    }

    @Override
    public void mapBtoA(ProductCompletePic b, MDProductPicMapping a, MappingContext context) {
        a.setId(b.getId());
        a.setProductId(b.getProduct()==null?null:b.getProduct().getId());
        a.setProductName(b.getProduct()==null?null:b.getProduct().getName());
        a.setJsonInfo(b.getJsonInfo());
        a.setCustomerId(b.getCustomer()==null?null:b.getCustomer().getId());
        a.setBarcodeMustFlag(b.getBarcodeMustFlag());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
