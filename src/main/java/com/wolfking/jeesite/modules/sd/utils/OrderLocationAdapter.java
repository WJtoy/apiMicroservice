package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.OrderLocation;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * @autor Ryan Lu
 * @date 2019/4/24 5:59 PM
 */
public class OrderLocationAdapter extends TypeAdapter<OrderLocation> {

    @Override
    public OrderLocation read(final JsonReader in) throws IOException {
        final OrderLocation order = new OrderLocation();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "orderId":
                    order.setOrderId(Long.valueOf(in.nextString()));
                    break;
                case "quarter":
                    order.setQuarter(in.nextString());
                    break;
                case "area":
                    in.beginObject();
                    Area area = new Area();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                area.setId(in.nextLong());
                                break;
                            case "name":
                                area.setName(in.nextString());
                                break;
                            case "fullName":
                                area.setFullName(in.nextString());
                                break;
                        }
                    }
                    order.setArea(area);
                    in.endObject();
                    break;
                case "longitude":
                    order.setLongitude(in.nextDouble());
                    break;
                case "latitude":
                    order.setLatitude(in.nextDouble());
                    break;
                case "distance":
                    order.setDistance(in.nextDouble());
                    break;
            }
        }

        in.endObject();

        return order;
    }

    @Override
    public void write(final JsonWriter out, final OrderLocation order) throws IOException {
        out.beginObject();
        out.name("orderId").value(order.getOrderId())
            .name("quarter").value(order.getQuarter());

        if(order.getArea() != null && order.getArea().getId() != null){
            out.name("area")
                    .beginObject()
                    .name("id").value(order.getArea().getId())
                    .name("name").value(order.getArea().getName())
                    .name("fullName").value(order.getArea().getFullName())
                    .endObject();
        }
        out.name("longitude").value(order.getLongitude())
            .name("latitude").value(order.getLatitude())
            .name("distance").value(order.getDistance());
        out.endObject();
    }

    private static OrderLocationAdapter adapter;
    public OrderLocationAdapter() {}
    public static OrderLocationAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderLocationAdapter();
        }
        return adapter;
    }
}
