package com.wolfking.jeesite.modules.api.entity.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestDict;

import java.io.IOException;

/**
 * 字典Gson序列化实现
 */
public class RestDictAdapter extends TypeAdapter<RestDict> {

    @Override
    public RestDict read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestDict dict) throws IOException {
        out.beginObject();

        out.name("label").value(dict.getLabel());
        out.name("value").value(dict.getValue());

        out.endObject();
    }

}
