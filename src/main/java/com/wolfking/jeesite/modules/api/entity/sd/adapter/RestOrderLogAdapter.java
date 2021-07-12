package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;

import java.io.IOException;

/**
 * 订单日志Gson序列化实现 (For app)
 */
public class RestOrderLogAdapter extends TypeAdapter<OrderProcessLog> {

    @Override
    public OrderProcessLog read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final OrderProcessLog entity) throws IOException {
        out.beginObject();
        out.name("actionComment").value(entity.getActionComment());
        out.name("createDate").value(entity.getCreateDate().getTime());
        out.endObject();
    }

    private static RestOrderLogAdapter adapter;

    public RestOrderLogAdapter() {}

    public static RestOrderLogAdapter getInstance() {
        if (adapter == null){
            adapter = new RestOrderLogAdapter();
        }
        return adapter;
    }

}
