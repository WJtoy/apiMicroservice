package com.wolfking.jeesite.modules.api.entity.sd.mapper;


import com.wolfking.jeesite.common.utils.Encodes;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderHistory;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * 订单与API历史订单详情模型转换
 */
@Component
public class RestOrderHistoryMapper extends CustomMapper<RestOrderHistory, Order>{

    @Override
    public void mapAtoB(RestOrderHistory a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestOrderHistory a, MappingContext context) {
        OrderCondition condition = b.getOrderCondition();
        a.setOrderId(condition.getOrderId());
        a.setQuarter(condition.getQuarter());
        a.setOrderNo(condition.getOrderNo());
        a.setUserName(condition.getUserName());
        a.setServicePhone(condition.getServicePhone());
        a.setServiceAddress(condition.getServiceAddress());
        //a.setServiceAddress(condition.getArea().getName().concat(" ").concat(condition.getServiceAddress()));
        a.setAcceptDate(b.getOrderStatus().getAcceptDate());
        a.setAppointDate(condition.getAppointmentDate());
        a.setStatus(condition.getStatus());
        a.setServicePoint(condition.getServicePoint());
        a.setEngineer(condition.getEngineer());
        a.setRemarks("");
        a.setAreaId(condition.getArea().getId().toString());
        a.setOrderServiceType(condition.getOrderServiceType());
        a.setCloseDate(condition.getCloseDate());
        a.setAppAbnormalyFlag(condition.getAppAbnormalyFlag());
        try {
            Dict orderServiceType = MSDictUtils.getDictByValue(String.valueOf(condition.getOrderServiceType()), "order_service_type");//切换为微服务
            if(orderServiceType != null){
                a.setOrderServiceTypeName(orderServiceType.getLabel());
            }
        }catch (Exception e){
            LogUtils.saveLog("读取订单服务类型错误","RestOrderDetailInfoMapper.mapBtoA",String.valueOf(condition.getOrderServiceType()),null,null,2);
        }
        a.setDescription(Encodes.unescapeHtml(b.getDescription()));//服务描述
        a.setRemarks("");
        a.setOrderServiceType(condition.getOrderServiceType());
        //a.setEngineerInvoiceDate(b.getOrderStatus().getEngineerInvoiceDate());//安维付款日期
        a.setEngineerInvoiceDate(b.getOrderStatus().getChargeDate());//安维对账日期
       // a.setIsComplained(condition.getIsComplained()>0?1:0);//18/01/24
        // 2019-08-29 投诉标识转移到orderStatus
        if(b.getOrderStatus() != null && b.getOrderStatus().getComplainFlag() != null){
            a.setIsComplained(b.getOrderStatus().getComplainFlag()>0?1:0);
        }
        //services
        if(b.getDetailList()!=null && b.getDetailList().size()>0){
            RestOrderDetail detail;
            for(OrderDetail m:b.getDetailList()){
                if(m.getDelFlag() != 0){
                    continue;
                }
                detail = new RestOrderDetail();
                detail.setId(String.valueOf(m.getId()));
                detail.setQuarter(b.getQuarter());
                detail.setServiceTimes(m.getServiceTimes());
                detail.setOrderId(String.valueOf(m.getOrderId()));
                detail.setProductId(String.valueOf(m.getProduct().getId()));
                detail.setProductName(m.getProduct().getName());
                detail.setQty(m.getQty());
                detail.setUnit(m.getProduct().getSetFlag()==1?"套":"台");
                detail.setServiceTypeName(m.getServiceType().getName());
                detail.setServicePointId(m.getServicePoint().getId());//网点
                detail.setEngineerId(m.getEngineer().getId());//安维师傅
                detail.setEngineer(m.getEngineer());//安维
                //fee
                detail.setEngineerExpressCharge(m.getEngineerExpressCharge());
                detail.setEngineerMaterialCharge(m.getEngineerMaterialCharge());
                detail.setEngineerTravelCharge(m.getEngineerTravelCharge());
                detail.setTravelNo(m.getTravelNo());
                detail.setEngineerServiceCharge(m.getEngineerServiceCharge());
                detail.setEngineerChage(m.getEngineerChage());
                detail.setRemarks(m.getRemarks());
                a.getServices().add(detail);
            }
            //涉及多个网点，费用统计放在外层处理
        }

    }
}
