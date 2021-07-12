package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerMaterial;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.Product;

import java.io.IOException;

/**
 * 客户配件（简单）自定义Gson序列化/序列化
 */
public class CustomerMaterialAdapter extends TypeAdapter<CustomerMaterial> {

    @Override
    public CustomerMaterial read(final JsonReader in) throws IOException {
        final CustomerMaterial model = new CustomerMaterial();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "isReturn":
                    model.setIsReturn(in.nextInt());
                    break;
                case "price":
                    model.setPrice(in.nextDouble());
                    break;
                case "customerPartCode":
                    model.setCustomerPartCode(in.nextString());
                    break;
                case "customerPartName":
                    model.setCustomerPartName(in.nextString());
                    break;
                case "warrantyDay":
                    model.setWarrantyDay(in.nextInt());
                    break;
                case "product":
                    Product product = new Product();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                product.setId(in.nextLong());
                                break;
                            case "name":
                                product.setName(in.nextString());
                                break;
                        }
                    }
                    model.setProduct(product);
                    in.endObject();
                    break;
                case "material":
                    Material material = new Material();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                material.setId(in.nextLong());
                                break;
                            case "name":
                                material.setName(in.nextString());
                                break;
                        }
                    }
                    model.setMaterial(material);
                    in.endObject();
                    break;
                case "remarks":
                    model.setRemarks(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final CustomerMaterial model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("isReturn").value(model.getIsReturn());
        out.name("price").value(model.getPrice());
        out.name("customerPartCode").value(model.getCustomerPartCode());
        out.name("customerPartName").value(model.getCustomerPartName());
        out.name("warrantyDay").value(model.getWarrantyDay());
        if(model.getProduct() != null){
            out.name("product")
                    .beginObject()
                    .name("id").value(model.getProduct().getId())
                    .endObject();
        }
        if(model.getMaterial() != null){
            out.name("material")
                    .beginObject()
                    .name("id").value(model.getMaterial().getId())
                    .endObject();
        }
        out.name("remarks").value(model.getRemarks());
        out.endObject();
    }

    private static CustomerMaterialAdapter adapter;
    public CustomerMaterialAdapter() {}
    public static CustomerMaterialAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerMaterialAdapter();
        }
        return adapter;
    }

}
