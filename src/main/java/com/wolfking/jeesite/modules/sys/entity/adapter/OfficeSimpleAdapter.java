package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Office;

import java.io.IOException;

/**
 * 数据字典（简单）自定义Gson序列化/序列化
 */
public class OfficeSimpleAdapter extends TypeAdapter<Office> {

    @Override
    public Office read(final JsonReader in) throws IOException {
        final Office model = new Office();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Office model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("name").value(model.getName());
        out.endObject();
    }


    private static OfficeSimpleAdapter adapter;
    public OfficeSimpleAdapter() {}
    public static OfficeSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new OfficeSimpleAdapter();
        }
        return adapter;
    }

}
