package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestOrderHistoryRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestOrderSearchRequest;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Rest网点历史帝国的查询条件转换
 */
@Component
public class RestServicePointHistorySearcheModelMapper extends CustomMapper<RestOrderHistoryRequest,OrderServicePointSearchModel>{

    @Override
    public void mapAtoB(RestOrderHistoryRequest a, OrderServicePointSearchModel b, MappingContext context) {
        b.setIsEngineerInvoiced(a.getIsEngineerInvoiced());
        b.setUserName(a.getUserName());
        b.setUserPhone(a.getPhone());
        b.setAddress(a.getAddress());
        b.setOrderNo(a.getOrderNo());
        b.setEngineerName(a.getEngineerName());
        b.setOrderServiceType(a.getOrderServiceType());
        if(a.getBeginAcceptDate()!=null && a.getBeginAcceptDate()>0){
            b.setBeginAcceptDate(DateUtils.getDateStart(new Date(a.getBeginAcceptDate())));
        }
        if(a.getEndAcceptDate()!=null && a.getEndAcceptDate()>0){
            b.setEndAcceptDate(DateUtils.getDateEnd(new Date(a.getEndAcceptDate())));
        }
    }

    @Override
    public void mapBtoA(OrderServicePointSearchModel b, RestOrderHistoryRequest a, MappingContext context) {

    }
}
