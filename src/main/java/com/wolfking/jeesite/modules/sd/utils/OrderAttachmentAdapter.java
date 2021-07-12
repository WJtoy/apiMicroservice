package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrder;
import com.wolfking.jeesite.modules.sd.entity.OrderAttachment;

import java.io.IOException;
import java.util.Date;

/**
 * 订单附件Gson序列化实现
 */
public class OrderAttachmentAdapter extends TypeAdapter<OrderAttachment> {

    @Override
    public OrderAttachment read(final JsonReader in) throws IOException {
        final OrderAttachment entity = new OrderAttachment();
        String strd = new String("");
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    entity.setId(Long.valueOf(in.nextString()));
                    break;
                case "quarter":
                    entity.setQuarter(in.nextString());
                    break;
                case "filePath":
                    entity.setFilePath(in.nextString());
                    break;
                case "remarks":
                    entity.setRemarks(in.nextString());
                    break;
                case "createDate":
                    entity.setCreateDate(new Date(in.nextLong()));
                    break;
            }
        }

        in.endObject();

        return entity;
    }

    @Override
    public void write(final JsonWriter out, final OrderAttachment entity) throws IOException {
        out.beginObject();

        out.name("id").value(entity.getId().toString());
        out.name("quarter").value(entity.getQuarter());
        out.name("filePath").value(entity.getFilePath());
        out.name("remarks").value(entity.getRemarks());
        out.name("createDate").value(entity.getCreateDate().getTime());

        out.endObject();
    }

    private static OrderAttachmentAdapter adapter;

    public OrderAttachmentAdapter() {}

    public static OrderAttachmentAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderAttachmentAdapter();
        }
        return adapter;
    }

}
