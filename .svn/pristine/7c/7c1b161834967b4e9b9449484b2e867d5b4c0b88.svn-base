package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestOrderBaseRequest;

import java.io.IOException;

/**
 * 订单请求Gson序列化实现 (For app)
 */
public class RestOrderBaseRequestAdapter extends TypeAdapter<RestOrderBaseRequest> {

    @Override
    public RestOrderBaseRequest read(final JsonReader in) throws IOException {

        final RestOrderBaseRequest model = new RestOrderBaseRequest();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextString());
                    break;
                case "orderId":
                    model.setOrderId(in.nextString());
                    break;
                case "quarter":
                    model.setQuarter(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderBaseRequest entity) throws IOException {
        out.beginObject()
                .name("id").value(entity.getId()==null?"":entity.getId())
                .name("orderId").value(entity.getOrderId())
                .name("quarter").value(entity.getQuarter())
                .endObject();
    }

    private static RestOrderBaseRequestAdapter adapter;

    public RestOrderBaseRequestAdapter() {}

    public static RestOrderBaseRequestAdapter getInstance() {
        if (adapter == null){
            adapter = new RestOrderBaseRequestAdapter();
        }
        return adapter;
    }

}
