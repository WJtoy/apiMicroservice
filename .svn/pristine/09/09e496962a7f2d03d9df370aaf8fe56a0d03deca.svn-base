package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDProduct;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MDProductMapper extends CustomMapper<MDProduct, Product> {
    @Override
    public void mapAtoB(MDProduct a, Product b, MappingContext context) {
        b.setId(a.getId());
        b.setCategory(a.getProductCategoryId()==null?null:new ProductCategory(a.getProductCategoryId()));
        b.setName(a.getName());
        b.setPinYin(a.getPinYin());
        b.setBrand(a.getBrand());
        b.setModel(a.getModel());
        b.setSetFlag(a.getSetFlag() == null?0:a.getSetFlag());
        b.setProductIds(a.getProductIds());
        b.setSort(a.getSort()==null?0:a.getSort());
        b.setApproveFlag(a.getApproveFlag()==null?null:a.getApproveFlag());
        b.setRemarks(a.getRemarks());
        if(a.getProductTypeSpecList()!=null && a.getProductTypeSpecList().size()>0){
            b.setProductTypeSpecList(a.getProductTypeSpecList());
        }
    }

    @Override
    public void mapBtoA(Product b, MDProduct a, MappingContext context) {
        a.setId(b.getId());
        a.setProductCategoryId(b.getCategory()==null?null:b.getCategory().getId());
        a.setName(b.getName());
        a.setPinYin(b.getPinYin());
        a.setBrand(b.getBrand());
        a.setModel(b.getModel());
        a.setSetFlag(b.getSetFlag());
        a.setProductIds(b.getProductIds());
        a.setSort(b.getSort());
        a.setApproveFlag(b.getApproveFlag());
        a.setRemarks(b.getRemarks());
        if(b.getMaterialList()!=null && b.getMaterialList().size()>0){
            a.setMaterialIdList(b.getMaterialList().stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        if(b.getProductTypeSpecList() !=null && b.getProductTypeSpecList().size()>0){
            a.setProductTypeSpecList(b.getProductTypeSpecList());
        }
    }
}
