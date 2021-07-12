package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.dto.MDCustomerDto;
import com.kkl.kklplus.entity.md.dto.MDCustomerProductDto;
import com.kkl.kklplus.entity.md.dto.MDProductDto;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerProduct;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;


@Component
public class MDCustomerProductDtoMapper extends CustomMapper<MDCustomerProductDto, CustomerProduct> {
    @Override
    public void mapAtoB(MDCustomerProductDto a, CustomerProduct b, MappingContext context) {
        Customer customer = new Customer();
        Product product = new Product();
        if(a.getCustomerDto()!=null){
            MDCustomerDto mdCustomerDto = a.getCustomerDto();
            if(mdCustomerDto.getId() !=null && mdCustomerDto.getId()>0){
                customer.setId(mdCustomerDto.getId());
            }
            if(StringUtils.isNotBlank(mdCustomerDto.getName())){
                customer.setName(mdCustomerDto.getName());
            }
            if(StringUtils.isNotBlank(mdCustomerDto.getCode())){
                customer.setCode(mdCustomerDto.getCode());
            }
        }
        if(a.getProductDto()!=null){
            MDProductDto mdProductDto = a.getProductDto();
            if(mdProductDto.getId() !=null && mdProductDto.getId()>0){
                product.setId(mdProductDto.getId());
            }
            if(StringUtils.isNotBlank(mdProductDto.getName())){
                product.setName(mdProductDto.getName());
            }
            if(mdProductDto.getSort() !=null && mdProductDto.getSort()>0){
                product.setSort(mdProductDto.getSort());
            }
            if(mdProductDto.getProductCategoryId() !=null && mdProductDto.getProductCategoryId()>0){
                ProductCategory productCategory = new ProductCategory(mdProductDto.getProductCategoryId());
                product.setCategory(productCategory);
            }
        }
        b.setCustomer(customer);
        b.setProduct(product);
    }

    @Override
    public void mapBtoA(CustomerProduct b, MDCustomerProductDto a, MappingContext context) {
    }
}
