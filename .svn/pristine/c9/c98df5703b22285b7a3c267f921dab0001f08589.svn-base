package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Brand;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * 品牌（简单）自定义Gson序列化/序列化
 */
public class BrandSimpleAdapter extends TypeAdapter<Brand> {

    @Override
    public Brand read(final JsonReader in) throws IOException {
        final Brand model = new Brand();
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
                case "sort":
                    if(in.peek() == JsonToken.NULL){
                        model.setSort(10);
                        in.nextNull();
                    }else {
                        model.setSort(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Brand model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("name").value(model.getName());
        out.name("sort").value(model.getSort());
        out.endObject();
    }

    private static BrandSimpleAdapter adapter;

    public BrandSimpleAdapter() {}

    public static BrandSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new BrandSimpleAdapter();
        }
        return adapter;
    }
}
