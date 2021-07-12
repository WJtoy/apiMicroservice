package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.CustomerProductModel;

import java.io.IOException;

/**
 * 产品（简单）自定义Gson序列化/序列化
 */
public class CustomerProductModelSimpleAdapter extends TypeAdapter<CustomerProductModel> {

    @Override
    public CustomerProductModel read(final JsonReader in) throws IOException {
        final CustomerProductModel model = new CustomerProductModel();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "customerModel":
                    model.setCustomerModel(in.nextString());
                    break;
                case "customerProductName":
                    model.setCustomerProductName(in.nextString());
                    break;
                case "productId":
                    model.setProductId(in.nextLong());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final CustomerProductModel model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("customerModel").value(model.getCustomerModel());
        out.name("customerProductName").value(model.getCustomerProductName());
        out.name("productId").value(model.getProductId());
        out.endObject();
    }

    private static CustomerProductModelSimpleAdapter adapter;
    public CustomerProductModelSimpleAdapter() {}
    public static CustomerProductModelSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerProductModelSimpleAdapter();
        }
        return adapter;
    }
}
