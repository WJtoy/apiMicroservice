package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestOrderSearchRequest;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Rest网点查询条件类于后台Web查询条件类相互转换
 */
@Component
public class RestServicePointSearcheModelToWebMapper extends CustomMapper<RestOrderSearchRequest,OrderServicePointSearchModel>{

    @Override
    public void mapAtoB(RestOrderSearchRequest a, OrderServicePointSearchModel b, MappingContext context) {
        b.setOrderListType(a.getListType());
        b.setUserName(a.getUserName());
        b.setUserPhone(a.getPhone());
        b.setAddress(a.getAddress());
        b.setOrderNo(a.getOrderNo());
        b.setEngineerName(a.getEngineerName());
        b.setAreaId(a.getAreaId());
        b.setOrderServiceType(a.getOrderServiceType());
        b.setComplaint(a.getComplaint());
        b.setUrgent(a.getUrgent());
        b.setReminder(a.getReminder());
        if(a.getBeginAcceptDate()!=null && a.getBeginAcceptDate()>0){
            b.setBeginAcceptDate(DateUtils.getDateStart(new Date(a.getBeginAcceptDate())));
        }
        if(a.getEndAcceptDate()!=null && a.getEndAcceptDate()>0){
            b.setEndAcceptDate(DateUtils.getDateEnd(new Date(a.getEndAcceptDate())));
        }
//        if(a.getBeginAppointDate()!=null && a.getBeginAppointDate()>0){
//            b.setAppointmentDate(DateUtils.getDateStart(new Date(a.getBeginAppointDate())));
//        }
        if (a.getBeginAppointDate() != null && a.getBeginAppointDate() > 0) {
            b.setBeginAppointDate(DateUtils.getDateStart(new Date(a.getBeginAppointDate())));
        }
        if (a.getEndAppointDate() != null && a.getEndAppointDate() > 0) {
            b.setEndAppointDate(DateUtils.getDateEnd(new Date(a.getEndAppointDate())));
        }

        //private Date appointmentDate;
        //private Long servicePointId;
        //private Long engineerId;

    }

    @Override
    public void mapBtoA(OrderServicePointSearchModel b, RestOrderSearchRequest a, MappingContext context) {

    }
}
