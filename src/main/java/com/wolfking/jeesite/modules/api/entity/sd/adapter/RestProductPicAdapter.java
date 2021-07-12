package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePicItem;

import java.io.IOException;

/**
 * 产品图片的Gson序列化实现
 */
public class RestProductPicAdapter extends TypeAdapter<RestProductCompletePic> {

    @Override
    public RestProductCompletePic read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestProductCompletePic productPic) throws IOException {
        out.beginObject();

        out.name("uniqueId").value(productPic.getUniqueId());
        out.name("productId").value(productPic.getProductId());
        out.name("productName").value(productPic.getProductName());
//        out.name("itemNo").value(productPic.getItemNo());
        out.name("productQty").value(productPic.getProductQty());
        out.name("productSN").value(productPic.getProductSN());
        out.name("items").beginArray();
        for (final RestProductCompletePicItem item : productPic.getItems()) {
            RestProductPicItemAdapter.getInstance().write(out, item);
        }
        out.endArray();

        out.endObject();
    }


    private static RestProductPicAdapter adapter;

    public RestProductPicAdapter() {
    }

    public static RestProductPicAdapter getInstance() {
        if (adapter == null) {
            adapter = new RestProductPicAdapter();
        }
        return adapter;
    }

}
