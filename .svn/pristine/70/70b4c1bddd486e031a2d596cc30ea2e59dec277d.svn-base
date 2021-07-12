package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderGrading;

import java.io.IOException;

/**
 * 历史订单Gson序列化实现
 */
public class RestOrderGradingAdapter extends TypeAdapter<RestOrderGrading> {

    @Override
    public RestOrderGrading read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderGrading order) throws IOException {
        out.beginObject();

        out.name("dataSource").value(order.getDataSource())
                .name("orderId").value(order.getOrderId().toString())
                .name("quarter").value(order.getQuarter())
                .name("orderNo").value(order.getOrderNo())
                .name("userName").value(order.getUserName())
                .name("servicePhone").value(order.getServicePhone())
                .name("serviceAddress").value(order.getServiceAddress());
        out.name("status")
                .beginObject()
                .name("label").value(order.getStatus() == null ? "" : order.getStatus().getLabel())
                .name("value").value(order.getStatus() == null ? "" : order.getStatus().getValue())
                .endObject();
        out.name("engineer").beginObject()
                .name("id").value(order.getEngineer() == null ? "0" : order.getEngineer().getId().toString())
                .name("name").value(order.getEngineer() == null ? "" : order.getEngineer().getName())
                .endObject();
        out.name("appointDate").value(order.getAppointDate() == null ? 0 : order.getAppointDate().getTime())
                .name("acceptDate").value(order.getAcceptDate() == null ? 0 : order.getAcceptDate().getTime())
                .name("remarks").value(order.getRemarks()) //厂商说明
                .name("description").value(order.getDescription())//服务描述
                .name("orderServiceType").value(order.getOrderServiceType())
                .name("orderServiceTypeName").value(order.getOrderServiceTypeName())
                .name("engineerInvoiceDate").value(order.getEngineerInvoiceDate() == null ? 0 : order.getEngineerInvoiceDate().getTime())
                .name("areaId").value(order.getAreaId())
                .name("closeDate").value(order.getCloseDate() == null ? 0 : order.getCloseDate().getTime())
                .name("appAbnormalyFlag").value(order.getAppAbnormalyFlag())
                .name("reminderFlag").value(order.getReminderFlag());//催单标志 19/07/09

        //services
        out.name("services").beginArray();
        for (final RestOrderDetail item : order.getServices()) {
            RestOrderDetailAdapter.getInstance().write(out, item);
        }
        out.endArray();
        //fee
        out.name("engineerServiceCharge").value(order.getEngineerServiceCharge())
                .name("engineerTravelCharge").value(order.getEngineerTravelCharge())
                .name("engineerExpressCharge").value(order.getEngineerExpressCharge())
                .name("engineerMaterialCharge").value(order.getEngineerMaterialCharge())
                .name("engineerOtherCharge").value(order.getEngineerOtherCharge())
                .name("engineerCharge").value(order.getEngineerCharge())
                .name("isComplained").value(order.getIsComplained());//18/01/24

        out.name("praiseStatus").beginObject();
        out.name("value").value(order.getPraiseStatus().getValue());
        out.name("label").value(order.getPraiseStatus().getLabel());
        out.endObject();

        out.endObject();
    }


    private static RestOrderGradingAdapter adapter;

    public RestOrderGradingAdapter() {
    }

    public static RestOrderGradingAdapter getInstance() {
        if (adapter == null) {
            adapter = new RestOrderGradingAdapter();
        }
        return adapter;
    }

}
