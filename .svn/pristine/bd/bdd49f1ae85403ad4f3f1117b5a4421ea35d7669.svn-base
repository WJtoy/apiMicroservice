package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.common.Pair;
import com.wolfking.jeesite.modules.api.entity.sd.RestRepairInfo;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 订单上门服务 <-> 维修信息 互转
 */
@Component
public class RestRepairInfoMapper extends CustomMapper<RestRepairInfo, OrderDetail>{

    @Override
    public void mapAtoB(RestRepairInfo a, OrderDetail b, MappingContext context) {
        if(a == null){
            b = null;
            return;
        }
        if(b == null){
            b = new OrderDetail();
        }
        b.setId(Long.valueOf(a.getId()));
        b.setOrderId(Long.valueOf(a.getOrderId()));
        b.setQuarter(a.getQuarter());
        //Product product = new Product(StringUtils.toLong(a.getProductId()));
        //b.setProduct(product);
        b.setRemarks(StringUtils.toString(a.getRemarks()));
        MDErrorType errorType = new MDErrorType();
        errorType.setId(StringUtils.toLong(a.getErrorType().getKey()));
        errorType.setName(StringUtils.toString(a.getErrorType().getValue()));
        b.setErrorType(errorType);
        MDErrorCode errorCode = new MDErrorCode();
        errorCode.setId(StringUtils.toLong(a.getErrorCode().getKey()));
        errorCode.setName(StringUtils.toString(a.getErrorCode().getValue()));
        b.setErrorCode(errorCode);
        MDActionCode actionCode = new MDActionCode();
        actionCode.setId(StringUtils.toLong(a.getActionCode().getKey()));
        actionCode.setName(StringUtils.toString(a.getActionCode().getValue()));
        b.setActionCode(actionCode);
        b.setOtherActionRemark(StringUtils.toString(a.getOtherActionRemark()));
        long sid = StringUtils.toLong(a.getServiceTypeId());
        ServiceType serviceType = new ServiceType(sid,"",a.getServiceTypeName());
        b.setServiceType(serviceType);
    }

    @Override
    public void mapBtoA(OrderDetail b, RestRepairInfo a, MappingContext context) {
        if(b == null){
             a = null;
             return;
        }
        if(a == null){
            a = new RestRepairInfo();
        }

        a.setId(Optional.ofNullable(b.getId()).map(t->t.toString()).orElse("0"));
        a.setOrderId(Optional.ofNullable(b.getOrderId()).map(t->t.toString()).orElse("0"));
        a.setQuarter(b.getQuarter());
        a.setProductId(b.getProduct().getId().toString());
        a.setProductName(String.format("%s %s %s ",b.getProduct().getName(),b.getBrand(),b.getProductSpec()).trim());
        a.setServiceTypeId(b.getServiceType().getId().toString());
        a.setServiceTypeName(b.getServiceType().getName());
        a.setRemarks(StringUtils.toString(b.getRemarks()));
        a.setServiceCategory(new Pair<Integer, String>(b.getServiceCategory().getIntValue(),b.getServiceCategory().getLabel()));
        a.setErrorType(new Pair<String,String>(b.getErrorType().getId().toString(),StringUtils.toString(b.getErrorType().getName())));
        a.setErrorCode(new Pair<String,String>(b.getErrorCode().getId().toString(),StringUtils.toString(b.getErrorCode().getName())));
        a.setActionCode(new Pair<String,String>(b.getActionCode().getId().toString(),StringUtils.toString(b.getActionCode().getName())));
        a.setOtherActionRemark(StringUtils.toString(b.getOtherActionRemark()));
        a.setRemarks(StringUtils.toString(b.getRemarks()));
    }

}
