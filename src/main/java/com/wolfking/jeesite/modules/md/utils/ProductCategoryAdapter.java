package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;

import java.io.IOException;

/**
 * 产品（简单）自定义Gson序列化/序列化
 */
public class ProductCategoryAdapter extends TypeAdapter<ProductCategory> {

    @Override
    public ProductCategory read(final JsonReader in) throws IOException {
        final ProductCategory model = new ProductCategory();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "code":
                    model.setCode(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final ProductCategory model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("code").value(model.getCode());
        out.name("name").value(model.getName());
        out.endObject();
    }

    private static ProductCategoryAdapter adapter;
    public ProductCategoryAdapter() {}
    public static ProductCategoryAdapter getInstance() {
        if (adapter == null){
            adapter = new ProductCategoryAdapter();
        }
        return adapter;
    }
}
