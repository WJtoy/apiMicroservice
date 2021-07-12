package com.wolfking.jeesite.modules.api.entity.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestMaterial;

import java.io.IOException;

/**
 * 配件Gson序列化实现
 */
public class RestMaterialAdapter extends TypeAdapter<RestMaterial> {

    @Override
    public RestMaterial read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestMaterial material) throws IOException {
        out.beginObject();

        out.name("id").value(material.getId().toString());
        out.name("name").value(material.getName());
        out.name("model").value(material.getModel());

        out.endObject();
    }

}
