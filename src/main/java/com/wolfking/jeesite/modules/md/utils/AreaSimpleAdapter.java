package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * 区域（简单）自定义Gson序列化/序列化
 */
public class AreaSimpleAdapter extends TypeAdapter<Area> {

    @Override
    public Area read(final JsonReader in) throws IOException {
        final Area model = new Area();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        model.setId(null);
                        in.nextNull();
                    }else {
                        model.setId(in.nextLong());
                    }
                    break;
                case "name":
                    if(in.peek() == JsonToken.NULL){
                        model.setName("");
                        in.nextNull();
                    }else {
                        model.setName(in.nextString());
                    }
                    break;
                case "fullName":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setFullName("");
                    }else {
                        model.setFullName(in.nextString());
                    }
                    break;
                case "type":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setType(0);
                    }else {
                        model.setType(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Area model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("name").value(model.getName());
        out.name("fullName").value(model.getFullName()==null?"":model.getFullName());
        out.name("type").value(model.getType());
        out.endObject();
    }

    private static AreaSimpleAdapter adapter;

    public AreaSimpleAdapter() {}

    public static AreaSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new AreaSimpleAdapter();
        }
        return adapter;
    }
}
