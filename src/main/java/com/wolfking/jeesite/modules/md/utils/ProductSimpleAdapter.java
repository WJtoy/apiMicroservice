package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServiceType;

import java.io.IOException;

/**
 * 产品（简单）自定义Gson序列化/序列化
 */
public class ProductSimpleAdapter extends TypeAdapter<Product> {

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
                case "pinYin":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setPinYin("");
                    }else {
                        model.setPinYin(in.nextString());
                    }
                    break;
                case "setFlag":
                    model.setSetFlag(in.nextInt());
                    break;
                case "category":
                    ProductCategory category = new ProductCategory();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                category.setId(in.nextLong());
                                break;
                            case "name":
                                category.setName(in.nextString());
                                break;
                        }
                    }
                    in.endObject();
                    model.setCategory(category);
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Product model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("setFlag").value(model.getSetFlag());
        out.name("name").value(model.getName());
        out.name("pinYin").value(model.getPinYin());
        if (model.getCategory() != null){
            out.name("category")
                    .beginObject()
                    .name("id").value(model.getCategory().getId())
                    .name("name").value(model.getCategory().getName())
                    .endObject();
        }
        out.endObject();
    }

    private static ProductSimpleAdapter adapter;
    public ProductSimpleAdapter() {}
    public static ProductSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new ProductSimpleAdapter();
        }
        return adapter;
    }
}
