package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 订单配件申请Gson序列化
 */
public class RestMaterialPhotoAdapter extends TypeAdapter<MaterialAttachment> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public MaterialAttachment read(final JsonReader in) throws IOException {
        return null;
    }


    @Override
    public void write(final JsonWriter out, final MaterialAttachment item) throws IOException {
        out.beginObject();
        if(item.getId() != null) {
            out.name("id").value(item.getId().toString());
        }
        //out.name("quarter").value(item.getQuarter());
        out.name("filePath").value(item.getFilePath());
        out.name("remarks").value(item.getRemarks());
        //if(item.getCreateDate() != null){
        //    out.name("createDate").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
        //}else{
        //    out.name("createDate").value("");
        //}

        out.endObject();
    }

    private static RestMaterialPhotoAdapter adapter;

    public RestMaterialPhotoAdapter() {}

    public static RestMaterialPhotoAdapter getInstance() {
        if (adapter == null){
            adapter = new RestMaterialPhotoAdapter();
        }
        return adapter;
    }
}
