package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductInsurance;
import com.wolfking.jeesite.modules.md.entity.InsurancePrice;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductInsuranceMapper extends CustomMapper<MDProductInsurance, InsurancePrice> {

    @Override
    public void mapAtoB(MDProductInsurance a, InsurancePrice b, MappingContext context) {
        b.setId(a.getId());
        b.setCategory(a.getProductCategoryId()==null?null:new ProductCategory(a.getProductCategoryId()));
        b.setInsurance(a.getInsurance());
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
        b.setCreateDate(a.getCreateDate());
    }

    @Override
    public void mapBtoA(InsurancePrice b, MDProductInsurance a, MappingContext context) {
        a.setId(b.getId());
        a.setProductCategoryId(b.getCategory()==null?null:b.getCategory().getId());
        a.setInsurance(b.getInsurance());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
    }
}
