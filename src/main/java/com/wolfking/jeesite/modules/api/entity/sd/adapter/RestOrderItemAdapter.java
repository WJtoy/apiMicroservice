package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;

import java.io.IOException;

/**
 * 订单项目Gson序列化实现
 */
public class RestOrderItemAdapter extends TypeAdapter<RestOrderItem> {

    @Override
    public RestOrderItem read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderItem entity) throws IOException {
        out.beginObject();

        out.name("itemNo").value(entity.getItemNo().toString());
        out.name("productId").value(entity.getProductId().toString());
        out.name("productName").value(entity.getProductName());
        out.name("brand").value(entity.getBrand());
        out.name("productSpec").value(entity.getProductSpec());
        out.name("serviceTypeId").value(entity.getServiceTypeId().toString());
        out.name("serviceTypeName").value(entity.getServiceTypeName());
        out.name("warrantyStatus").value(entity.getWarrantyStatus());
        out.name("qty").value(entity.getQty());
        out.name("unit").value(entity.getUnit());
        out.name("remarks").value(entity.getRemarks());
        out.name("hasFixSpec").value(entity.getHasFixSpec());

        out.name("pics").beginArray();
        for (final RestOrderItem.PicItem item : entity.getPics()) {
            out.beginObject();
            out.name("url").value(item.getUrl());
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    private static RestOrderItemAdapter adapter;

    public RestOrderItemAdapter() {}

    public static RestOrderItemAdapter getInstance() {
        if (adapter == null){
            adapter = new RestOrderItemAdapter();
        }
        return adapter;
    }

}
