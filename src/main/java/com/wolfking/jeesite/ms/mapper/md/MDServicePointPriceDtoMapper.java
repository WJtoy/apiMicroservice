package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.dto.MDProductDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointPriceDto;
import com.kkl.kklplus.entity.md.dto.MDServiceTypeDto;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDServicePointPriceDtoMapper extends CustomMapper<MDServicePointPriceDto, ServicePrice> {
    @Override
    public void mapAtoB(MDServicePointPriceDto a, ServicePrice b, MappingContext context) {
        b.setId(a.getId());
        if (a.getServicePoint() != null) {
            MDServicePointDto servicePointDto = a.getServicePoint();
            ServicePoint servicePoint = new ServicePoint();
            servicePoint.setId(servicePointDto.getId());
            servicePoint.setServicePointNo(servicePointDto.getServicePointNo());
            servicePoint.setName(servicePointDto.getName());
            b.setServicePoint(servicePoint);
        }
        if (a.getProduct() != null) {
            MDProductDto productDto = a.getProduct();
            Product product = new Product();
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            b.setProduct(product);
        }
        if (a.getServiceType() != null) {
            MDServiceTypeDto serviceTypeDto = a.getServiceType();
            ServiceType serviceType = new ServiceType();
            serviceType.setId(serviceTypeDto.getId());
            serviceType.setName(serviceTypeDto.getName());
            serviceType.setCode(serviceTypeDto.getCode());
            b.setServiceType(serviceType);
        }
        b.setPrice(a.getPrice());
        b.setDiscountPrice(a.getDiscountPrice());
        Dict unitDict = MSDictUtils.getDictByValue(a.getUnit(), "unit");
        b.setUnit(unitDict);
        Dict priceType = MSDictUtils.getDictByValue(Optional.ofNullable(a.getPriceType()).orElse(0).toString(), "PriceType");
        b.setPriceType(priceType);
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(ServicePrice b, MDServicePointPriceDto a, MappingContext context) {
        if (b.getServicePoint() != null) {
            ServicePoint servicePoint = b.getServicePoint();
            MDServicePointDto servicePointDto = new MDServicePointDto();
            servicePointDto.setId(servicePoint.getId());
            servicePointDto.setServicePointNo(servicePoint.getServicePointNo());
            servicePointDto.setName(servicePoint.getName());
            a.setServicePoint(servicePointDto);
        }
        if (b.getProduct() != null) {
            Product product = b.getProduct();
            MDProductDto productDto = new MDProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            a.setProduct(productDto);
        }
        if (b.getServiceType() != null) {
            ServiceType serviceType = b.getServiceType();
            MDServiceTypeDto serviceTypeDto = new MDServiceTypeDto();
            serviceTypeDto.setId(serviceType.getId());
            serviceTypeDto.setName(serviceType.getName());
            serviceTypeDto.setCode(serviceType.getCode());
            a.setServiceType(serviceTypeDto);
        }

        a.setPrice(b.getPrice());
        a.setDiscountPrice(b.getDiscountPrice());
        a.setUnit(Optional.ofNullable(b.getUnit()).map(Dict::getValue).orElse(""));
        a.setPriceType(Optional.ofNullable(b.getPriceType()).map(r->Integer.valueOf(r.getValue())).orElse(0));
        a.setDelFlag(b.getDelFlag());
        a.setRemarks(b.getRemarks());
    }
}
