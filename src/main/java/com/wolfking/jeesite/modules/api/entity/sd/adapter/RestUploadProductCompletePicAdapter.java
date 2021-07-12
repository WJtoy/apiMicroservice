package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderStatusLog;
import com.wolfking.jeesite.modules.api.entity.sd.RestUploadProductCompletePic;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;

import java.io.IOException;

/**
 * 上传完工图片的响应对象的Gson序列化实现
 */
public class RestUploadProductCompletePicAdapter extends TypeAdapter<RestUploadProductCompletePic> {

    @Override
    public RestUploadProductCompletePic read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestUploadProductCompletePic order) throws IOException {
        out.beginObject();
        out.name("uniqueId").value(order.getUniqueId());
        out.name("pictureCode").value(order.getPictureCode());
        out.name("url").value(order.getUrl());
        out.endObject();
    }

}
