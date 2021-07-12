package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.dto.MDProductDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointDto;
import com.kkl.kklplus.entity.md.dto.MDServicePointProductDto;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointProduct;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDServicePointProductDtoMapper extends CustomMapper<MDServicePointProductDto, ServicePointProduct> {
    @Override
    public void mapAtoB(MDServicePointProductDto a, ServicePointProduct b, MappingContext context) {
        if (a.getProduct() != null) {
            MDProductDto productDto = a.getProduct();
            Product product = new Product();
            product.setId(productDto.getId());
            product.setName(productDto.getName());
            if (productDto.getProductCategoryId() != null) {
                product.setCategory(new ProductCategory(productDto.getProductCategoryId()));
            }
            product.setSort(productDto.getSort());

            b.setProduct(product);
        }
        if (a.getServicePoint() != null) {
            ServicePoint servicePoint = new ServicePoint();
            MDServicePointDto servicePointDto = a.getServicePoint();
            servicePoint.setId(servicePointDto.getId());
            servicePoint.setServicePointNo(servicePointDto.getServicePointNo());
            servicePoint.setName(servicePointDto.getName());

            b.setServicePoint(servicePoint);
        }
    }

    @Override
    public void mapBtoA(ServicePointProduct b, MDServicePointProductDto a, MappingContext context) {
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
            productDto.setProductCategoryId(Optional.ofNullable(product.getCategory()).map(ProductCategory::getId).orElse(null));
            productDto.setSort(product.getSort());
            a.setProduct(productDto);
        }
    }
}
