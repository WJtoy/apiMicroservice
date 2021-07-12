package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestServicePointOrderInfo;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * 订单与API订单详情模型转换
 */
@Component
public class RestServicePointOrderInfoMapper extends CustomMapper<RestServicePointOrderInfo, Order>{

    @Override
    public void mapAtoB(RestServicePointOrderInfo a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestServicePointOrderInfo a, MappingContext context) {
        OrderCondition condition = b.getOrderCondition();
        a.setUserName(condition.getUserName());
        a.setServicePhone(condition.getServicePhone());
        a.setServiceAddress(condition.getArea().getName().concat(" ").concat(condition.getServiceAddress()));
        //a.setReminderFlag(condition.getReminderFlag());//催单标志 19/07/09
        if(b.getOrderStatus().getReminderStatus() != null) {
            a.setReminderFlag(b.getOrderStatus().getReminderStatus());//催单标志 2019/08/15
        }
        if(b.getOrderStatus().getChargeDate() !=null){
            a.setInvoiceDate(b.getOrderStatus().getChargeDate().getTime());
        }

        //services
        if(b.getDetailList()!=null && b.getDetailList().size()>0){
            RestOrderDetail detail;
            for(OrderDetail m:b.getDetailList()){
                if(m.getDelFlag() != 0){
                    continue;
                }
                detail = new RestOrderDetail();
                detail.setServiceTimes(m.getServiceTimes());
                detail.setProductName(m.getProduct().getName());
                detail.setQty(m.getQty());
                detail.setUnit(m.getProduct().getSetFlag()==1?"套":"台");
                detail.setServiceTypeName(m.getServiceType().getName());
                a.getServices().add(detail);
            }
            //涉及多个网点，费用统计放在外层处理
        }

    }
}
