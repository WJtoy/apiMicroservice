package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;

import java.io.IOException;

/**
 * 产品（精简）自定义Gson序列化/序列化
 */
public class ProductSimplexAdapter extends TypeAdapter<Product> {

    @Override
    public Product read(final JsonReader in) throws IOException {
        final Product model = new Product();
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
    public void write(final JsonWriter out, final Product model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId())
            .name("name").value(model.getName());
        out.endObject();
    }

    private static ProductSimplexAdapter adapter;
    public ProductSimplexAdapter() {}
    public static ProductSimplexAdapter getInstance() {
        if (adapter == null){
            adapter = new ProductSimplexAdapter();
        }
        return adapter;
    }
}
