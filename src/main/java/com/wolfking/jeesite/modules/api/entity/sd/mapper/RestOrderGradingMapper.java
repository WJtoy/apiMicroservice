package com.wolfking.jeesite.modules.api.entity.sd.mapper;


import com.kkl.kklplus.entity.praise.PraiseStatusEnum;
import com.wolfking.jeesite.common.utils.Encodes;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderGrading;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * 订单与API历史订单详情模型转换
 */
@Component
public class RestOrderGradingMapper extends CustomMapper<RestOrderGrading, Order> {

    @Override
    public void mapAtoB(RestOrderGrading a, Order b, MappingContext context) {

    }

    @Override
    public void mapBtoA(Order b, RestOrderGrading a, MappingContext context) {
        a.setDataSource(b.getDataSourceId());
        OrderCondition condition = b.getOrderCondition();
        a.setOrderId(condition.getOrderId());
        a.setQuarter(condition.getQuarter());
        a.setOrderNo(condition.getOrderNo());
        a.setUserName(condition.getUserName());
        a.setServicePhone(condition.getServicePhone());
        a.setServiceAddress(condition.getServiceAddress());
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
        a.setDescription(Encodes.unescapeHtml(b.getDescription()));//服务描述
        a.setRemarks("");
        a.setOrderServiceType(condition.getOrderServiceType());
        a.setEngineerInvoiceDate(b.getOrderStatus().getChargeDate());//安维对账日期
        //a.setIsComplained(condition.getIsComplained() > 0 ? 1 : 0);//18/01/24
        // 2019-08-29 投诉标识转移到orderStatus
        if(b.getOrderStatus() != null && b.getOrderStatus().getComplainFlag() != null){
            a.setIsComplained(b.getOrderStatus().getComplainFlag()>0?1:0);
        }
        if (b.getOrderStatusFlag() != null) {
            PraiseStatusEnum statusEnum = PraiseStatusEnum.fromCode(b.getOrderStatusFlag().getPraiseStatus());
            if (statusEnum != null) {
                a.setPraiseStatus(new AppDict(String.valueOf(statusEnum.code), statusEnum.msg));
            }
        }
        //a.setReminderFlag(condition.getReminderFlag());//催单标志 19/07/09
        if(b.getOrderStatus() != null && b.getOrderStatus().getReminderStatus() != null) {
            a.setReminderFlag(b.getOrderStatus().getReminderStatus());//催单标识 2019/08/15
        }
        if (b.getDetailList() != null && b.getDetailList().size() > 0) {
            RestOrderDetail detail;
            double totalServiceCharge = 0;
            double totalMaterialCharge = 0;
            double totalTravelCharge = 0;
            double totalExpressCharge = 0;
            double totalOtherCharge = 0;
            double totalEngineerCharge = 0;
            for (OrderDetail m : b.getDetailList()) {
                detail = new RestOrderDetail();
                detail.setId(String.valueOf(m.getId()));
                detail.setQuarter(b.getQuarter());
                detail.setServiceTimes(m.getServiceTimes());
                detail.setOrderId(String.valueOf(m.getOrderId()));
                if (m.getProduct() != null) {
                    detail.setProductId(String.valueOf(m.getProduct().getId()));
                    detail.setProductName(m.getProduct().getName());
                    detail.setUnit(m.getProduct().getSetFlag() == 1 ? "套" : "台");
                }
                detail.setQty(m.getQty());
                detail.setServiceTypeName(m.getServiceType().getName());
                detail.setServicePointId(m.getServicePoint().getId());//网点
                detail.setEngineerId(m.getEngineer().getId());//安维师傅
                detail.setEngineer(m.getEngineer());//安维
                //fee
                detail.setEngineerServiceCharge(m.getEngineerServiceCharge());
                detail.setEngineerMaterialCharge(m.getEngineerMaterialCharge());
                detail.setEngineerTravelCharge(m.getEngineerTravelCharge());
                detail.setEngineerExpressCharge(m.getEngineerExpressCharge());
                detail.setEngineerOtherCharge(m.getEngineerOtherCharge());
                detail.setEngineerChage(m.getEngineerChage());
                detail.setTravelNo(m.getTravelNo());
                detail.setRemarks(m.getRemarks());
                a.getServices().add(detail);

                totalServiceCharge = totalServiceCharge + m.getEngineerServiceCharge();
                totalMaterialCharge = totalMaterialCharge + m.getEngineerMaterialCharge();
                totalTravelCharge = totalTravelCharge + m.getEngineerTravelCharge();
                totalExpressCharge = totalExpressCharge + m.getEngineerExpressCharge();
                totalOtherCharge = totalOtherCharge + m.getEngineerOtherCharge();
                totalEngineerCharge = totalEngineerCharge + m.getEngineerChage();
            }

            a.setEngineerServiceCharge(totalServiceCharge);
            a.setEngineerMaterialCharge(totalMaterialCharge);
            a.setEngineerTravelCharge(totalTravelCharge);
            a.setEngineerExpressCharge(totalExpressCharge);
            a.setEngineerOtherCharge(totalOtherCharge);
            a.setEngineerCharge(totalEngineerCharge);
        }

    }
}
