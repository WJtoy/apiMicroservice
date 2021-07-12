package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDServiceType;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MDServiceTypeMapper extends CustomMapper<MDServiceType, ServiceType> {

    @Override
    public void mapAtoB(MDServiceType a, ServiceType b, MappingContext context) {
        b.setId(a.getId());
        b.setName(a.getName());
        b.setCode(a.getCode());
        b.setOrderServiceType(a.getOrderServiceType());
        if(a.getUnit()!=null && a.getUnit()>=0){
            b.setUnit(a.getUnit());
        }else{
            b.setUnit(0);
        }
        b.setAutoChargeFlag(a.getAutoChargeFlag());
        b.setAutoGradeFlag(a.getAutoGradeFlag());
        b.setBlockedPrice(a.getBlockedPrice());
        b.setDiscountPrice(a.getDiscountPrice());
        b.setEngineerDiscountPrice(a.getEngineerDiscountPrice());
        b.setEngineerPrice(a.getEngineerPrice());
        if(a.getOpenForCustomer()!=null && a.getOpenForCustomer()>0){
            b.setOpenForCustomer(a.getOpenForCustomer());
        }else{
            b.setOpenForCustomer(0);
        }
        b.setPrice(a.getPrice());
        if(a.getSort()!=null &&  a.getSort()>0){
            b.setSort(a.getSort());
        }else{
            b.setSort(0);
        }
        if(StringUtils.isNotBlank(a.getWarrantyStatus())){
            b.setWarrantyStatus(new Dict(a.getWarrantyStatus()));
        }
        b.setRemarks(a.getRemarks());
        b.setRelateErrorTypeFlag(a.getRelateErrorTypeFlag());
        b.setTaxFeeFlag(a.getTaxFeeFlag());
        b.setInfoFeeFlag(a.getInfoFeeFlag());
    }

    @Override
    public void mapBtoA(ServiceType b, MDServiceType a, MappingContext context) {
        a.setId(b.getId());
        a.setName(b.getName());
        a.setCode(b.getCode());
        a.setOrderServiceType(b.getOrderServiceType());
        a.setUnit(b.getUnit());
        a.setAutoChargeFlag(b.getAutoChargeFlag());
        a.setAutoGradeFlag(b.getAutoGradeFlag());
        a.setBlockedPrice(b.getBlockedPrice());
        a.setDiscountPrice(b.getDiscountPrice());
        a.setEngineerDiscountPrice(b.getEngineerDiscountPrice());
        a.setEngineerPrice(b.getEngineerPrice());
        a.setOpenForCustomer(b.getOpenForCustomer());
        a.setPrice(b.getPrice());
        a.setSort(b.getSort());
        a.setWarrantyStatus(b.getWarrantyStatus()==null?"":b.getWarrantyStatus().getValue());
        a.setRemarks(b.getRemarks());
        a.setCreateById(b.getCreateBy()==null?null:b.getCreateBy().getId());
        a.setCreateDate(b.getCreateDate());
        a.setUpdateById(b.getUpdateBy()==null?null:b.getUpdateBy().getId());
        a.setUpdateDate(b.getUpdateDate());
        a.setRelateErrorTypeFlag(b.getRelateErrorTypeFlag());
        a.setTaxFeeFlag(b.getTaxFeeFlag());
        a.setInfoFeeFlag(b.getInfoFeeFlag());
    }
}
