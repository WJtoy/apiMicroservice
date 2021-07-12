package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.Order;

import java.io.IOException;

/**
 * 订单自定义Gson序列化/序列化
 */
public class OrderNoAdapter extends TypeAdapter<Order> {

    @Override
    public Order read(final JsonReader in) throws IOException {
        final Order model = new Order();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(Long.valueOf(in.nextString()));
                    break;
                case "orderNo":
                    model.setOrderNo(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Order model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId().toString())
            .name("orderNo").value(model.getOrderNo());
        out.endObject();
    }
    
    private static OrderNoAdapter adapter;
    public OrderNoAdapter() {}
    public static OrderNoAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderNoAdapter();
        }
        return adapter;
    }
}
