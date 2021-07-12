package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.google.common.collect.Sets;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrder;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RestOrderMapper extends CustomMapper<RestOrder, Order>{

    @Override
    public void mapAtoB(RestOrder a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestOrder a, MappingContext context) {
        OrderCondition condition = b.getOrderCondition();
        a.setOrderId(condition.getOrderId());
        a.setQuarter(condition.getQuarter());
        a.setOrderNo(condition.getOrderNo());
        a.setUserName(condition.getUserName());
        a.setServicePhone(condition.getServicePhone());
        a.setServiceAddress(condition.getServiceAddress());
        //a.setServiceAddress(condition.getArea().getName().concat(" ").concat(condition.getServiceAddress()));
        a.setAcceptDate(b.getOrderStatus().getPlanDate());
        a.setAppointDate(condition.getAppointmentDate());
        a.setStatus(condition.getStatus());
        a.setEngineer(condition.getEngineer());
        a.setOrderServiceType(condition.getOrderServiceType());
        a.setAreaId(condition.getArea().getId().toString());
        a.setAppCompleteType(condition.getAppCompleteType());
        a.setAppAbnormalyFlag(condition.getAppAbnormalyFlag());
        //a.setReminderFlag(condition.getReminderFlag());//催单标识 0：没有催单 19/07/09
        if(b.getOrderStatus() != null && b.getOrderStatus().getReminderStatus() != null) {
            a.setReminderFlag(b.getOrderStatus().getReminderStatus());//催单标识 2019/08/15
        }
        Set<String> sets = Sets.newHashSet("0","2","3");
        if(condition.getPendingType() !=null
                && StringUtils.isNotBlank(condition.getPendingType().getValue())
                && !sets.contains(condition.getPendingType().getValue())
                && condition.getAppointmentDate() != null
                && DateUtils.pastMinutes(condition.getAppointmentDate())<0 ){
            a.setPendingFlag(1);
        }else{
            a.setPendingFlag(0);
        }
        a.setRemarks("");
        //a.setIsComplained(condition.getIsComplained()>0?1:0);//18/01/24
        // 2019-08-29 投诉标识转移到orderStatus
        if(b.getOrderStatus() != null && b.getOrderStatus().getComplainFlag() != null){
            a.setIsComplained(b.getOrderStatus().getComplainFlag()>0?1:0);
        }
    }
}
