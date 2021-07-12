package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDServicePointPrice;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDServicePointPriceMapper extends CustomMapper<MDServicePointPrice, ServicePrice> {
    @Override
    public void mapAtoB(MDServicePointPrice a, ServicePrice b, MappingContext context) {
        b.setId(a.getId());
        b.setServicePoint(new ServicePoint(Optional.ofNullable(a.getServicePointId()).orElse(0L)));
        b.setProduct(new Product(Optional.ofNullable(a.getProductId()).orElse(0L)));
        b.setServiceType(new ServiceType(Optional.ofNullable(a.getServiceTypeId()).orElse(0L)));
        b.setPrice(a.getPrice());
        b.setDiscountPrice(a.getDiscountPrice());
        b.setUnit(new Dict(a.getUnit(), ""));
        b.setPriceType(new Dict(Optional.ofNullable(a.getPriceType()).orElse(0),""));
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
        b.setIsNewRecord(a.getIsNewRecord());
        b.setCreateBy(Optional.ofNullable(a.getCreateById()).map(r->new User(r)).orElse(null));
        b.setCreateDate(a.getCreateDate());
        b.setUpdateBy(Optional.ofNullable(a.getUpdateById()).map(r->new User(r)).orElse(null));
        b.setUpdateDate(a.getUpdateDate());
        b.setCustomizeFlag(a.getCustomizeFlag());
    }

    @Override
    public void mapBtoA(ServicePrice b, MDServicePointPrice a, MappingContext context) {
        a.setId(b.getId());
        a.setServicePointId(Optional.ofNullable(b.getServicePoint()).map(ServicePoint::getId).orElse(0L));
        a.setProductId(Optional.ofNullable(b.getProduct()).map(Product::getId).orElse(0L));
        a.setServiceTypeId(Optional.ofNullable(b.getServiceType()).map(ServiceType::getId).orElse(0L));
        a.setPrice(b.getPrice());
        a.setDiscountPrice(b.getDiscountPrice());
        a.setUnit(Optional.ofNullable(b.getUnit()).map(Dict::getValue).orElse(""));
        a.setPriceType(Optional.ofNullable(b.getPriceType()).map(r->Integer.valueOf(r.getValue())).orElse(0));
        a.setDelFlag(b.getDelFlag());
        a.setRemarks(b.getRemarks());
        a.setNewRecord(b.getIsNewRecord());
        a.setCreateById(Optional.ofNullable(b.getCreateBy()).map(User::getId).orElse(0L));
        a.setUpdateById(Optional.ofNullable(b.getUpdateBy()).map(User::getId).orElse(0L));
        a.setCreateDate(b.getCreateDate());
        a.setUpdateDate(b.getUpdateDate());
        a.setCustomizeFlag(b.getCustomizeFlag());
    }
}
