package com.wolfking.jeesite.modules.api.entity.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestRepairAction;

import java.io.IOException;

/**
 * 维修故障处理Gson序列化实现
 */
public class RestRepairActionAdapter extends TypeAdapter<RestRepairAction> {

    @Override
    public RestRepairAction read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestRepairAction action) throws IOException {
        out.beginObject()
                .name("key").value(action.getKey())
                .name("value").value(action.getValue())
                .name("serviceTypeId").value(action.getServiceTypeId())
                .name("serviceTypeName").value(action.getServiceTypeName())
                .endObject();
    }

}
