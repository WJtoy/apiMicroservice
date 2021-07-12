package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;

import java.io.IOException;

/**
 * 配件申请单项目序列化
 */
public class RestMatieralItemAdapter extends TypeAdapter<MaterialItem> {

    @Override
    public MaterialItem read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final MaterialItem entity) throws IOException {
        out.beginObject();

        out.name("productName").value(entity.getMaterial().getName());
        out.name("qty").value(entity.getQty());
        out.name("price").value(entity.getPrice());
        out.name("totalPrice").value(entity.getTotalPrice());
        out.name("recycleFlag").value(entity.getRecycleFlag());
        out.name("recyclePrice").value(entity.getRecyclePrice());
        out.name("totalRecyclePrice").value(entity.getTotalRecyclePrice());
        out.name("unit").value("元"); //单位

        out.endObject();
    }

    private static RestMatieralItemAdapter adapter;

    public RestMatieralItemAdapter() {}

    public static RestMatieralItemAdapter getInstance() {
        if (adapter == null){
            adapter = new RestMatieralItemAdapter();
        }
        return adapter;
    }

}
