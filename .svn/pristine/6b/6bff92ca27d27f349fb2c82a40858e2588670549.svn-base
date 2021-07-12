package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.common.utils.Encodes;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderGrab;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * 订单与抢单模型转换
 */
@Component
public class RestOrderGrabMapper extends CustomMapper<RestOrderGrab, Order>{

    @Override
    public void mapAtoB(RestOrderGrab a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestOrderGrab a, MappingContext context) {
        a.setDataSource(b.getDataSourceId());
        OrderCondition condition = b.getOrderCondition();
        a.setOrderId(condition.getOrderId());
        a.setQuarter(condition.getQuarter());
        a.setOrderNo(condition.getOrderNo());
        a.setUserName(condition.getUserName());
        a.setServicePhone(condition.getServicePhone());
        //a.setServiceAddress(condition.getArea().getName().concat(" ").concat(condition.getServiceAddress()));
        a.setServiceAddress(condition.getServiceAddress());
        a.setApproveDate(b.getOrderStatus().getCustomerApproveDate());
        a.setDescription(Encodes.unescapeHtml(b.getDescription()));
        a.setRemarks("");
        a.setOrderServiceType(condition.getOrderServiceType());
        a.setOrderServiceType(condition.getOrderServiceType());
        a.setOrderServiceTypeName(condition.getOrderServiceTypeName());
        a.setAreaId(condition.getArea().getId().toString());
        //a.setIsComplained(condition.getIsComplained());//18/01/24
        // 2019-08-29 投诉标识转移到orderStatus
        if(b.getOrderStatus() != null && b.getOrderStatus().getComplainFlag() != null){
            a.setIsComplained(b.getOrderStatus().getComplainFlag()>0?1:0);
        }
        //a.setReminderFlag(condition.getReminderFlag());//催单标识 19/07/09
        if(b.getOrderStatus() != null && b.getOrderStatus().getReminderStatus() != null) {
            a.setReminderFlag(b.getOrderStatus().getReminderStatus());//催单标识 2019/08/15
        }
        //items
        RestOrderItem ritem;
        for(OrderItem item:b.getItems()){
            ritem = new RestOrderItem();
            ritem.setItemNo(item.getItemNo().toString());
            ritem.setProductId(item.getProduct().getId());
            ritem.setProductName(item.getProduct().getName());
            ritem.setProductSpec(item.getProductSpec());
            ritem.setBrand(item.getBrand());
            ritem.setQty(item.getQty());
            ritem.setServiceTypeId(item.getServiceType().getId());
            ritem.setServiceTypeName(item.getServiceType().getName());
            ritem.setUnit(item.getProduct().getSetFlag()==1?"套":"台");
            ritem.setRemarks(item.getRemarks());
            a.getItems().add(ritem);
        }
    }
}
