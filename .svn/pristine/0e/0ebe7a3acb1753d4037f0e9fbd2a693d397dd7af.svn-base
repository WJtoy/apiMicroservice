package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;

import java.io.IOException;

/**
 * 专用于将数据库json字段与ProductCompletePicItem实体之间的类型转换
 *
 * @date 2019-06-25
 * @author ryan
 * 增加属性：上传日期/uploadDate 的序列化/反序列化
 */
public class ProductCompletedPicItemAdapter extends TypeAdapter<ProductCompletePicItem> {

    @Override
    public ProductCompletePicItem read(final JsonReader in) throws IOException {
        final ProductCompletePicItem model = new ProductCompletePicItem();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "checked":
                    model.setChecked(in.nextInt());
                    break;
                case "mustFlag":
                    model.setMustFlag(in.nextInt());
                    break;
                case "pictureCode":
                    model.setPictureCode(in.nextString());
                    break;
                case "remarks":
                    model.setRemarks(in.nextString());
                    break;
                case "sort":
                    model.setSort(in.nextInt());
                    break;
                case "title":
                    model.setTitle(in.nextString());
                    break;
                case "url":
                    model.setUrl(in.nextString());
                    break;
                case "uploadDate":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setUploadDate(null);
                    }else {
                        model.setUploadDate(DateUtils.longToDate(in.nextLong()));
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final ProductCompletePicItem model) throws IOException {
        out.beginObject()
                .name("checked").value(model.getChecked())
                .name("mustFlag").value(model.getMustFlag())
                .name("pictureCode").value(model.getPictureCode())
                .name("remarks").value(model.getRemarks())
                .name("sort").value(model.getSort())
                .name("title").value(model.getTitle())
                .name("url").value(model.getUrl());
        if(model.getUploadDate() != null){
            out.name("uploadDate").value(model.getUploadDate().getTime());
        }
        out.endObject();
    }

    private static ProductCompletedPicItemAdapter adapter;
    public ProductCompletedPicItemAdapter() {}
    public static ProductCompletedPicItemAdapter getInstance() {
        if (adapter == null){
            adapter = new ProductCompletedPicItemAdapter();
        }
        return adapter;
    }
}
