package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProductTimeLiness;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.TimeLinessPrice;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDProductTimeLinessMapper extends CustomMapper<MDProductTimeLiness, TimeLinessPrice> {
    @Override
    public void mapAtoB(MDProductTimeLiness a, TimeLinessPrice b, MappingContext context) {
        b.setId(a.getId());
        b.setCategory(a.getProductCategoryId()==null?null:new ProductCategory(a.getProductCategoryId()));
        b.setTimeLinessLevel(a.getTimeLinessLevel()==null?null:new Dict(a.getTimeLinessLevel(),""));
        b.setAmount(a.getAmount());
        b.setRemarks(a.getRemarks());
        b.setDelFlag(a.getDelFlag());
    }

    @Override
    public void mapBtoA(TimeLinessPrice b, MDProductTimeLiness a, MappingContext context) {
        a.setId(b.getId());
        a.setProductCategoryId(b.getCategory()==null?null:b.getCategory().getId());
        a.setTimeLinessLevel(b.getTimeLinessLevel()==null?null:b.getTimeLinessLevel().getIntValue());
        a.setAmount(b.getAmount());
        a.setRemarks(b.getRemarks());
        a.setDelFlag(b.getDelFlag());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
    }
}
