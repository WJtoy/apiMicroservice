package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestServicePointOrderInfo;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;

import java.io.IOException;

/**
 * 订单列表中 订单基本信息(For网点)Gson序列化实现
 */
public class RestServicePointOrderInfoAdapter extends TypeAdapter<RestServicePointOrderInfo> {

    @Override
    public RestServicePointOrderInfo read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestServicePointOrderInfo order) throws IOException {
        out.beginObject();

        out.name("userName").value(order.getUserName());
        out.name("servicePhone").value(order.getServicePhone());
        out.name("serviceAddress").value(order.getServiceAddress());
        out.name("invoiceDate").value(order.getInvoiceDate());
        out.name("estimatedServiceCost").value(order.getEstimatedServiceCost());//18/01/24
        out.name("reminderFlag").value(order.getReminderFlag());//19/07/09
        //services
        out.name("services").beginArray();
        for(final RestOrderDetail item :order.getServices()){
            RestOrderDetailAdapter.getInstance().write(out,item);
        }
        out.endArray();

        out.endObject();
    }


    private static RestServicePointOrderInfoAdapter adapter;

    public RestServicePointOrderInfoAdapter() {}

    public static RestServicePointOrderInfoAdapter getInstance() {
        if (adapter == null){
            adapter = new RestServicePointOrderInfoAdapter();
        }
        return adapter;
    }

}
