package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.OrderFee;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.io.IOException;

/**
 * 订单自定义Gson序列化/序列化
 */
public class OrderFeeAdapter extends TypeAdapter<OrderFee> {

    @Override
    public OrderFee read(final JsonReader in) throws IOException {
        final OrderFee fee = new OrderFee();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "expectCharge":
                    fee.setExpectCharge(in.nextDouble());
                    break;
                case "blockedCharge":
                    fee.setBlockedCharge(in.nextDouble());
                    break;
                case "rebateFlag":
                    fee.setRebateFlag(in.nextInt());
                    break;
                case "orderPaymentType":
                    in.beginObject();
                    Dict orderPaymentType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                orderPaymentType.setLabel(in.nextString());
                                break;
                            case "value":
                                orderPaymentType.setValue(in.nextString());
                                break;
                        }
                    }
                    fee.setOrderPaymentType(orderPaymentType);
                    in.endObject();
                    break;
                case "serviceCharge":
                    fee.setServiceCharge(in.nextDouble());
                    break;
                case "materialCharge":
                    fee.setMaterialCharge(in.nextDouble());
                    break;
                case "expressCharge":
                    fee.setExpressCharge(in.nextDouble());
                    break;
                case "travelCharge":
                    fee.setTravelCharge(in.nextDouble());
                    break;
                case "otherCharge":
                    fee.setOtherCharge(in.nextDouble());
                    break;
                case "orderCharge":
                    fee.setOrderCharge(in.nextDouble());
                    break;
                case "customerUrgentCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setCustomerUrgentCharge(0.00);
                    }else{
                        fee.setCustomerUrgentCharge(in.nextDouble());
                    }
                    break;
                case "customerTimeLiness":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setCustomerTimeLiness(0.00);
                    }else{
                        fee.setCustomerTimeLiness(in.nextDouble());
                    }
                    break;
                case "customerTimeLinessCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setCustomerTimeLinessCharge(0.00);
                    }else{
                        fee.setCustomerTimeLinessCharge(in.nextDouble());
                    }
                    break;
                case "customerPlanTravelCharge":
                    fee.setCustomerPlanTravelCharge(in.nextDouble());
                    break;
                case "customerPlanOtherCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setCustomerPlanOtherCharge(0.00);
                    }else{
                        fee.setCustomerPlanOtherCharge(in.nextDouble());
                    }
                    break;
                //安维
                case "engineerPaymentType":
                    in.beginObject();
                    Dict engineerPaymentType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                engineerPaymentType.setLabel(in.nextString());
                                break;
                            case "value":
                                engineerPaymentType.setValue(in.nextString());
                                break;
                        }
                    }
                    fee.setEngineerPaymentType(engineerPaymentType);
                    in.endObject();
                    break;
                case "engineerServiceCharge":
                    fee.setEngineerServiceCharge(in.nextDouble());
                    break;
                case "engineerTravelCharge":
                    fee.setEngineerTravelCharge(in.nextDouble());
                    break;
                case "engineerExpressCharge":
                    fee.setEngineerExpressCharge(in.nextDouble());
                    break;
                case "engineerMaterialCharge":
                    fee.setEngineerMaterialCharge(in.nextDouble());
                    break;
                case "engineerOtherCharge":
                    fee.setEngineerOtherCharge(in.nextDouble());
                    break;
                case "timeLinessCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setTimeLinessCharge(0.00);
                    }else{
                        fee.setTimeLinessCharge(in.nextDouble());
                    }
                    break;
                case "subsidyTimeLinessCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setSubsidyTimeLinessCharge(0.00);
                    }else{
                        fee.setSubsidyTimeLinessCharge(in.nextDouble());
                    }
                    break;
                case "insuranceCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setInsuranceCharge(0.00);
                    }else{
                        fee.setInsuranceCharge(in.nextDouble());
                    }
                    break;
                case "engineerUrgentCharge":
                    if(in.peek() == null) {
                        in.nextNull();
                        fee.setEngineerUrgentCharge(0.00);
                    }else{
                        fee.setEngineerUrgentCharge(in.nextDouble());
                    }
                    break;
                case "engineerTotalCharge":
                    fee.setEngineerTotalCharge(in.nextDouble());
                    break;
                case "planTravelCharge":
                    fee.setPlanTravelCharge(in.nextDouble());
                    break;
                case "planTravelNo":
                    fee.setPlanTravelNo(in.nextString());
                    break;
                case "planOtherCharge"://18/01/25
                    fee.setPlanOtherCharge(in.nextDouble());
                    break;
                case "planDistance"://18/01/25
                    fee.setPlanDistance(in.nextDouble());
                    break;
            }
        }

        in.endObject();

        return fee;
    }

    @Override
    public void write(final JsonWriter out, final OrderFee fee) throws IOException {
        out.beginObject();
        //customer
        out.name("expectCharge").value(fee.getExpectCharge())
            .name("blockedCharge").value(fee.getBlockedCharge())
            .name("rebateFlag").value(fee.getRebateFlag());
        if(fee.getOrderPaymentType() != null){
            out.name("orderPaymentType")
                    .beginObject()
                    .name("value").value(fee.getOrderPaymentType().getValue())
                    .name("label").value(fee.getOrderPaymentType().getLabel())
                    .endObject();
        }

        out.name("serviceCharge").value(fee.getServiceCharge())
            .name("materialCharge").value(fee.getMaterialCharge())
            .name("expressCharge").value(fee.getExpressCharge())
            .name("travelCharge").value(fee.getTravelCharge())
            .name("otherCharge").value(fee.getOtherCharge())
            .name("customerUrgentCharge").value(fee.getCustomerUrgentCharge())
            .name("customerTimeLiness").value(fee.getCustomerTimeLiness())
            .name("customerTimeLinessCharge").value(fee.getCustomerTimeLinessCharge())
            .name("orderCharge").value(fee.getOrderCharge())
            .name("customerPlanTravelCharge").value(fee.getCustomerPlanTravelCharge())
            .name("customerPlanOtherCharge").value(fee.getCustomerPlanOtherCharge());

        //enginerr
        if (fee.getEngineerPaymentType() != null){
            out.name("engineerPaymentType")
                    .beginObject()
                    .name("value").value(fee.getEngineerPaymentType().getValue())
                    .name("label").value(fee.getEngineerPaymentType().getLabel())
                    .endObject();
        }
        out.name("engineerServiceCharge").value(fee.getEngineerServiceCharge())
            .name("engineerTravelCharge").value(fee.getEngineerTravelCharge())
            .name("engineerExpressCharge").value(fee.getEngineerExpressCharge())
            .name("engineerMaterialCharge").value(fee.getEngineerMaterialCharge())
            .name("engineerOtherCharge").value(fee.getEngineerOtherCharge())
            .name("timeLinessCharge").value(fee.getTimeLinessCharge())//18/06/02
            .name("subsidyTimeLinessCharge").value(fee.getSubsidyTimeLinessCharge())//18/06/08
            .name("insuranceCharge").value(fee.getInsuranceCharge())//18/06/02
            .name("engineerUrgentCharge").value(fee.getEngineerUrgentCharge())//18/06/07
            .name("engineerTotalCharge").value(fee.getEngineerTotalCharge())
            .name("planTravelCharge").value(fee.getPlanTravelCharge())
            .name("planTravelNo").value(fee.getPlanTravelNo())
            .name("planOtherCharge").value(fee.getPlanOtherCharge())//18/01/25
            .name("planDistance").value(fee.getPlanDistance());//18/01/25

        out.endObject();
    }

    private static OrderFeeAdapter adapter;
    public OrderFeeAdapter() {}
    public static OrderFeeAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderFeeAdapter();
        }
        return adapter;
    }
}
