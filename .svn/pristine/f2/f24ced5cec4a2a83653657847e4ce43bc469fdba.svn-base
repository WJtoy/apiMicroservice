package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * 区域自定义Gson序列化/序列化
 */
public class AreaAdapter extends TypeAdapter<Area> {

    @Override
    public Area read(final JsonReader in) throws IOException {
        final Area model = new Area();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "type":
                    model.setType(in.nextInt());
                    break;
                case "code":
                    model.setCode(in.nextString());
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "sort":
                    model.setSort(in.nextInt());
                    break;
                case "fullName":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setFullName("");
                    }else {
                        model.setFullName(in.nextString());
                    }
                    break;
                case "parentIds":
                    model.setParentIds(in.nextString());
                    break;
                case "parent":
                    Area parent = new Area();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                parent.setId(in.nextLong());
                                break;
                            case "name":
                                parent.setName(in.nextString());
                                break;
                        }
                    }
                    model.setParent(parent);
                    in.endObject();
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
        out.name("type").value(model.getType());
        out.name("code").value(model.getCode());
        out.name("name").value(model.getName());
        out.name("sort").value(model.getSort());
        out.name("fullName").value(model.getFullName()==null?"":model.getFullName());
        out.name("parentIds").value(model.getParentIds());
        out.name("parent").beginObject()
                .name("id").value(model.getParent()==null?0l:model.getParent().getId())
                .name("name").value(model.getParent()==null?"":model.getParent().getName())
                .endObject();
        out.endObject();
    }

    private static AreaAdapter adapter;

    public AreaAdapter() {}

    public static AreaAdapter getInstance() {
        if (adapter == null){
            adapter = new AreaAdapter();
        }
        return adapter;
    }
}
