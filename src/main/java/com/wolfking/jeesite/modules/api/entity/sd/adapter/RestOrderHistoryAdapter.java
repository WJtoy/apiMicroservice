package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderHistory;

import java.io.IOException;

/**
 * 历史订单Gson序列化实现
 */
public class RestOrderHistoryAdapter extends TypeAdapter<RestOrderHistory> {

    @Override
    public RestOrderHistory read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderHistory order) throws IOException {
        out.beginObject();

        out.name("orderId").value(order.getOrderId().toString());
        out.name("quarter").value(order.getQuarter());
        out.name("orderNo").value(order.getOrderNo());
        out.name("userName").value(order.getUserName());
        out.name("servicePhone").value(order.getServicePhone());
        out.name("serviceAddress").value(order.getServiceAddress());
        out.name("status")
                .beginObject()
                .name("label").value(order.getStatus()==null?"":order.getStatus().getLabel())
                .name("value").value(order.getStatus()==null?"":order.getStatus().getValue())
                .endObject();
        out.name("engineer").beginObject()
                .name("id").value(order.getEngineer()==null?"0":order.getEngineer().getId().toString())
                .name("name").value(order.getEngineer()==null?"":order.getEngineer().getName())
                .endObject();
        out.name("appointDate").value(order.getAppointDate()==null?0:order.getAppointDate().getTime());
        out.name("acceptDate").value(order.getAcceptDate()==null?0:order.getAcceptDate().getTime());
        out.name("remarks").value(order.getRemarks());//厂商说明
        out.name("description").value(order.getDescription());//服务描述
        out.name("orderServiceType").value(order.getOrderServiceType());
        out.name("orderServiceTypeName").value(order.getOrderServiceTypeName());
        out.name("engineerInvoiceDate").value(order.getEngineerInvoiceDate()==null?0:order.getEngineerInvoiceDate().getTime());
        out.name("areaId").value(order.getAreaId());
        out.name("closeDate").value(order.getCloseDate()==null?0:order.getCloseDate().getTime());
        out.name("appAbnormalyFlag").value(order.getAppAbnormalyFlag());

        //services
        out.name("services").beginArray();
        for(final RestOrderDetail item :order.getServices()){
            RestOrderDetailAdapter.getInstance().write(out,item);
        }
        out.endArray();
        //fee
        out.name("engineerServiceCharge").value(order.getEngineerServiceCharge());
        out.name("engineerTravelCharge").value(order.getEngineerTravelCharge());
        out.name("engineerExpressCharge").value(order.getEngineerExpressCharge());
        out.name("engineerMaterialCharge").value(order.getEngineerMaterialCharge());
        out.name("engineerOtherCharge").value(order.getEngineerOtherCharge());
        out.name("engineerCharge").value(order.getEngineerCharge());
        out.name("isComplained").value(order.getIsComplained());//18/01/24
        out.endObject();
    }


    private static RestOrderHistoryAdapter adapter;

    public RestOrderHistoryAdapter() {}

    public static RestOrderHistoryAdapter getInstance() {
        if (adapter == null){
            adapter = new RestOrderHistoryAdapter();
        }
        return adapter;
    }

}
