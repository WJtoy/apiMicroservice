package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveAccessoryItemRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveAccessoryRequest;

import java.io.IOException;

/**
 * 配件申请提交Gson序列化实现 (For app)
 */
public class RestSaveAccessoryItemRequestAdapter extends TypeAdapter<RestSaveAccessoryItemRequest> {

    @Override
    public RestSaveAccessoryItemRequest read(final JsonReader in) throws IOException {

        final RestSaveAccessoryItemRequest model = new RestSaveAccessoryItemRequest();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                //case "productId":
                //    model.setProductId(in.nextString());
                //    break;
                case "materialId":
                    model.setMaterialId(in.nextString());
                    break;
                case "qty":
                    model.setQty(in.nextInt());
                    break;
                case "price":
                    model.setPrice(in.nextDouble());
                    break;

            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final RestSaveAccessoryItemRequest entity) throws IOException {
        out.beginObject()
                //.name("productId").value(entity.getProductId())
                .name("materialId").value(entity.getMaterialId())
                .name("qty").value(entity.getQty())
                .name("price").value(entity.getPrice())
                .endObject();
    }

    private static RestSaveAccessoryItemRequestAdapter adapter;

    public RestSaveAccessoryItemRequestAdapter() {}

    public static RestSaveAccessoryItemRequestAdapter getInstance() {
        if (adapter == null){
            adapter = new RestSaveAccessoryItemRequestAdapter();
        }
        return adapter;
    }

}
