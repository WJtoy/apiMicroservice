package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderGrab;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;

import java.io.IOException;

/**
 * 抢单Gson序列化实现
 */
public class RestOrderGrabAdapter extends TypeAdapter<RestOrderGrab> {

    @Override
    public RestOrderGrab read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderGrab order) throws IOException {
        out.beginObject()
                .name("dataSource").value(order.getDataSource())
                .name("orderId").value(order.getOrderId().toString())
                .name("quarter").value(order.getQuarter())
                .name("orderNo").value(order.getOrderNo())
                .name("userName").value(order.getUserName())
                .name("servicePhone").value(order.getServicePhone())
                .name("serviceAddress").value(order.getServiceAddress())
                .name("approveDate").value(order.getApproveDate()==null?0:order.getApproveDate().getTime())
                .name("description").value(order.getDescription().replace("<br>",System.getProperty("line.separator")))
                .name("remarks").value(order.getRemarks())
                .name("orderServiceType").value(order.getOrderServiceType())
                .name("orderServiceTypeName").value(order.getOrderServiceTypeName())
                .name("areaId").value(order.getAreaId())
                .name("isComplained").value(order.getIsComplained())//18/01/24
                .name("reminderFlag").value(order.getReminderFlag());//催单标志 19/07/09

        //items
        out.name("items").beginArray();
        for(final RestOrderItem item :order.getItems()){
            RestOrderItemAdapter.getInstance().write(out,item);
        }
        out.endArray();

        out.endObject();
    }

}
