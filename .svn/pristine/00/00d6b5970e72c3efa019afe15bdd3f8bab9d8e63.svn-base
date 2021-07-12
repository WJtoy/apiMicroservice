package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.kkl.kklplus.utils.StringUtils;


import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;

import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetailInfoNew;

import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.sd.entity.*;

import com.wolfking.jeesite.modules.sys.entity.Dict;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;


/**
 * @Auther wj
 * @Date 2020/12/1 18:15
 */

@Component
public class RestOrderDetailInfoNewMapper extends CustomMapper<RestOrderDetailInfoNew, Order> {


    @Autowired
    private ServiceTypeService serviceTypeService;

    @Override
    public void mapAtoB(RestOrderDetailInfoNew a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestOrderDetailInfoNew a, MappingContext context) {


        //a.setIsComplained(condition.getIsComplained()>0?1:0);//2018/04/13
        // 2019-08-29 投诉标识转移到orderStatus
        if(b.getOrderStatus() != null && b.getOrderStatus().getComplainFlag() != null){
            a.setIsComplained(b.getOrderStatus().getComplainFlag()>0?1:0);
        }


        //services
        if(b.getDetailList()!=null && b.getDetailList().size()>0){
            a.setServiceFlag(1);//有上门服务
            RestOrderDetail detail;
            for(OrderDetail m:b.getDetailList()){
                if(m.getDelFlag() != 0){
                    continue;
                }
                detail = new RestOrderDetail()
                        .setId(String.valueOf(m.getId()))
                        .setQuarter(b.getQuarter())
                        .setServiceTimes(m.getServiceTimes())
                        .setOrderId(String.valueOf(m.getOrderId()))
                        .setProductId(String.valueOf(m.getProduct().getId()))
                        .setProductName(m.getProduct().getName())
                        .setQty(m.getQty())
                        .setUnit(m.getProduct().getSetFlag()==1?"套":"台")
                        .setServiceTypeName(m.getServiceType().getName())
                        .setServicePointId(m.getServicePoint().getId())
                        .setEngineerId(m.getEngineer().getId())
                        .setEngineerExpressCharge(m.getEngineerExpressCharge())
                        .setEngineerMaterialCharge(m.getEngineerMaterialCharge())
                        .setEngineerTravelCharge(m.getEngineerTravelCharge())
                        .setTravelNo(m.getTravelNo())
                        .setEngineerServiceCharge(m.getEngineerServiceCharge())
                        .setEngineerChage(m.getEngineerChage())
                        .setEngineerOtherCharge(m.getEngineerOtherCharge())
                        .setRemarks(m.getRemarks());
                //维修
                //服务类型
                Dict serviceCategory = Optional.ofNullable(m.getServiceCategory())
                        .filter(t->StringUtils.isNotBlank(t.getValue()))
                        .orElseGet(() -> {
                            return new Dict("0","");
                        });
                detail.setServiceCategoryId(StringUtils.toLong(serviceCategory.getValue()));
                detail.setServiceCategoryName(StringUtils.toString(serviceCategory.getLabel()));
                //故障类型，故障现象，故障处理
                detail.setErrorTypeName(Optional.ofNullable(m.getErrorType()).map(t->t.getName()).orElse(StringUtils.EMPTY));
                detail.setErrorCodeName(Optional.ofNullable(m.getErrorCode()).map(t->t.getName()).orElse(StringUtils.EMPTY));
                detail.setActionCodeName(Optional.ofNullable(m.getActionCode()).map(t->t.getName()).orElse(StringUtils.EMPTY));
                //}
                detail.setOtherActionRemark(StringUtils.toString(m.getOtherActionRemark()));
                //判断是否已添加维修故障信息
                detail.setHasRepaired(0);
                //维修
                if(serviceCategory.getIntValue() == 2){
                    if(m.getErrorType().getId() > 0) {
                        detail.setHasRepaired(1);
                    }else{
                        if(StringUtils.isNotBlank(detail.getOtherActionRemark())){
                            detail.setHasRepaired(1);
                        }
                    }
                }
                a.getServices().add(detail);
            }
            //涉及多个网点，费用统计放在外层处理
        }

       // a.setSuspendFlag(condition.getSuspendFlag() == null ? 0 : condition.getSuspendFlag());
        //a.setSuspendType(condition.getSuspendType() == null ? 0 : condition.getSuspendType());
    }
}
