package com.wolfking.jeesite.modules.sd.utils;

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
public class MaterialAttachmentAdapter extends TypeAdapter<MaterialAttachment> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public MaterialAttachment read(final JsonReader in) throws IOException {
        final MaterialAttachment item = new MaterialAttachment();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek()==JsonToken.NULL){
                        in.nextNull();
                        item.setId(null);
                    }else {
                        item.setId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "orderId":
                    item.setOrderId(Long.valueOf(in.nextString()));
                    break;
                case "quarter":
                    item.setQuarter(in.nextString());
                    break;
                case "filePath":
                    item.setFilePath(in.nextString());
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
                    break;
                case "createDate":
                    StringBuilder strd = new StringBuilder();
                    strd.append(in.nextString());
                    if (StringUtils.isBlank(strd)) {
                        item.setCreateDate(null);
                    } else {
                        try {
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            item.setCreateDate(date);
                        } catch (ParseException e) {
                            item.setCreateDate(null);
                            try {
                                LogUtils.saveLog("日期格式错误:", "MaterialAttachmentAdapter.read", String.format("id:%s,createDate:%s", item.getId(), strd.toString()), e, null);
                            }catch (Exception e1){}
                        }
                    }
                    strd.setLength(0);
                    break;
            }
        }
        in.endObject();
        return item;
    }


    @Override
    public void write(final JsonWriter out, final MaterialAttachment item) throws IOException {
        out.beginObject();
        if(item.getId() != null) {
            out.name("id").value(item.getId().toString());
        }
        out.name("orderId").value(item.getOrderId()==null?"0":item.getOrderId().toString());
        out.name("quarter").value(item.getQuarter());
        out.name("filePath").value(item.getFilePath());
        out.name("remarks").value(item.getRemarks());
        if(item.getCreateDate() != null){
            out.name("createDate").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
        }else{
            out.name("createDate").value("");
        }

        out.endObject();
    }

    private static MaterialAttachmentAdapter adapter;
    public MaterialAttachmentAdapter() {}
    public static MaterialAttachmentAdapter getInstance() {
        if (adapter == null){
            adapter = new MaterialAttachmentAdapter();
        }
        return adapter;
    }
}
