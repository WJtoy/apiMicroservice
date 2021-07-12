package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.dto.MDCustomerDto;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.kkl.kklplus.entity.md.dto.MDProductDto;
import com.kkl.kklplus.entity.md.dto.MDServiceTypeDto;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerPrice;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;


@Component
public class MDCustomerPriceDtoMapper extends CustomMapper<MDCustomerPriceDto, CustomerPrice> {
    @Override
    public void mapAtoB(MDCustomerPriceDto a, CustomerPrice b, MappingContext context) {
        b.setId(a.getId());
        Customer customer = new Customer();
        if(a.getCustomerDto()!=null && a.getCustomerDto().getId() !=null && a.getCustomerDto().getId()>0){
            MDCustomerDto mdCustomerDto = a.getCustomerDto();
            customer.setId(mdCustomerDto.getId());
            if(StringUtils.isNotBlank(mdCustomerDto.getName())){
                customer.setName(mdCustomerDto.getName());
            }
            if(StringUtils.isNotBlank(mdCustomerDto.getCode())){
                customer.setCode(mdCustomerDto.getCode());
            }
        }
        b.setCustomer(customer);
        Product product = new Product();
        if(a.getProductDto() !=null && a.getProductDto().getId() !=null && a.getProductDto().getId()>0){
            MDProductDto productDto = a.getProductDto();
            product.setId(productDto.getId());
            if(productDto.getSort()!=null){
                product.setSort(productDto.getSort());
            }
            if(StringUtils.isNotBlank(productDto.getName())){
                product.setName(productDto.getName());
            }
        }
        b.setProduct(product);
        ServiceType serviceType = new ServiceType();
        if(a.getServiceTypeDto() !=null && a.getServiceTypeDto().getId()!=null && a.getServiceTypeDto().getId()>0){
            MDServiceTypeDto serviceTypeDto = a.getServiceTypeDto();
            serviceType.setId(serviceTypeDto.getId());
            if(serviceTypeDto.getSort()!=null){
                serviceType.setSort(serviceTypeDto.getSort());
            }
            if(StringUtils.isNotBlank(serviceTypeDto.getName())){
                serviceType.setName(serviceTypeDto.getName());
            }
            if(StringUtils.isNotBlank(serviceTypeDto.getCode())){
                serviceType.setCode(serviceTypeDto.getCode());
            }
            if(StringUtils.isNotBlank(serviceTypeDto.getWarrantyStatus())){
                Dict dict = new Dict(serviceTypeDto.getWarrantyStatus());
                serviceType.setWarrantyStatus(dict);
            }
        }
        b.setServiceType(serviceType);
        b.setBlockedPrice(a.getBlockedPrice());
        b.setDiscountPrice(a.getDiscountPrice());
        b.setPrice(a.getPrice());
        b.setDelFlag(a.getDelFlag());
        b.setRemarks(a.getRemarks());
    }

    @Override
    public void mapBtoA(CustomerPrice b, MDCustomerPriceDto a, MappingContext context) {
         a.setId(b.getId());
         if(b.getCustomer() !=null && b.getCustomer().getId()!=null && b.getCustomer().getId()>0){
             MDCustomerDto mdCustomerDto = new MDCustomerDto();
             mdCustomerDto.setId(b.getCustomer().getId());
             a.setCustomerDto(mdCustomerDto);
         }
         if(b.getProduct() !=null && b.getProduct().getId() !=null && b.getProduct().getId()>0){
             MDProductDto mdProductDto = new MDProductDto();
             mdProductDto.setId(b.getProduct().getId());
             a.setProductDto(mdProductDto);
         }
    }
}
