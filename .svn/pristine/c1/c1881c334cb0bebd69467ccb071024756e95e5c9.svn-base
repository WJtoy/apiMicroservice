package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderStatusLog;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;

import java.io.IOException;

/**
 * 订单列表中订单Gson序列化实现
 */
public class RestOrderStatusLogAdapter extends TypeAdapter<RestOrderStatusLog> {

    @Override
    public RestOrderStatusLog read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderStatusLog order) throws IOException {
        out.beginObject();

        out.name("orderNo").value(order.getOrderNo());
        out.name("logs").beginArray();
        for(final OrderProcessLog log :order.getLogs()){
            RestOrderLogAdapter.getInstance().write(out,log);
        }
        out.endArray();

        out.endObject();
    }

}
