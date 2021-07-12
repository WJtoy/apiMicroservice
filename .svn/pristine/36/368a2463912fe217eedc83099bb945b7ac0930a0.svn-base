package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePicItem;

import java.io.IOException;

/**
 * 产品图片项的Gson序列化实现
 */
public class RestProductPicItemAdapter extends TypeAdapter<RestProductCompletePicItem> {

    @Override
    public RestProductCompletePicItem read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestProductCompletePicItem item) throws IOException {
        out.beginObject();
        out.name("pictureCode").value(item.getPictureCode());
        out.name("sort").value(item.getSort());
        out.name("title").value(item.getTitle());
        out.name("remarks").value(item.getRemarks());
        out.name("mustFlag").value(item.getMustFlag());
        out.name("url").value(item.getUrl());
        out.endObject();
    }

    private static RestProductPicItemAdapter adapter;

    private RestProductPicItemAdapter() {
    }

    public static RestProductPicItemAdapter getInstance() {
        if (adapter == null) {
            adapter = new RestProductPicItemAdapter();
        }
        return adapter;
    }

}
