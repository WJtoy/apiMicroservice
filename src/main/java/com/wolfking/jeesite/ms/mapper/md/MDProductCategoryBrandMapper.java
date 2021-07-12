package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductCategoryBrand;
import com.wolfking.jeesite.modules.md.entity.Brand;
import com.wolfking.jeesite.modules.md.entity.BrandCategory;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductCategoryBrandMapper extends CustomMapper<MDProductCategoryBrand, BrandCategory> {
    @Override
    public void mapAtoB(MDProductCategoryBrand a, BrandCategory b, MappingContext context) {
        b.setId(a.getId());
        b.setCategory(a.getProductCategoryId()==null?null:new ProductCategory(a.getProductCategoryId()));
        b.setBrand(a.getBrandId()==null?null:new Brand(a.getBrandId()));
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
    }

    @Override
    public void mapBtoA(BrandCategory b, MDProductCategoryBrand a, MappingContext context) {
        a.setId(b.getId());
        a.setProductCategoryId(b.getCategory()==null?null:b.getCategory().getId());
        a.setBrandId(b.getBrand()==null?null:b.getBrand().getId());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
    }
}
