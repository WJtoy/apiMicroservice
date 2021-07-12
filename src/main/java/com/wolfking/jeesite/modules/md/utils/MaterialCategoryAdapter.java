package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.MaterialCategory;

import java.io.IOException;

/**
 * 配件类别（简单）自定义Gson序列化/序列化
 */
public class MaterialCategoryAdapter extends TypeAdapter<MaterialCategory> {
    @Override
    public MaterialCategory read(final JsonReader in) throws IOException {
        final MaterialCategory model = new MaterialCategory();
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
    public void write(final JsonWriter out, final MaterialCategory model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("name").value(model.getName());
        out.endObject();
    }

    private static MaterialCategoryAdapter adapter;

    public MaterialCategoryAdapter() {}

    public static MaterialCategoryAdapter getInstance() {
        if (adapter == null){
            adapter = new MaterialCategoryAdapter();
        }
        return adapter;
    }
}
