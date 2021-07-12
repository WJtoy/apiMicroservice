package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveAccessoryItemRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveAccessoryRequest;

import java.io.IOException;
import java.util.List;

/**
 * 配件申请提交Gson序列化实现 (For app)
 */
public class RestSaveAccessoryRequestAdapter extends TypeAdapter<RestSaveAccessoryRequest> {

    @Override
    public RestSaveAccessoryRequest read(final JsonReader in) throws IOException {

        final RestSaveAccessoryRequest model = new RestSaveAccessoryRequest();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "orderId":
                    model.setOrderId(in.nextString());
                    break;
                case "quarter":
                    model.setQuarter(in.nextString());
                    break;
                case "applyType":
                    model.setApplyType(in.nextInt());
                    break;
                case "productId":
                    model.setProductId(in.nextString());
                    break;
                case "orderDetailId":
                    model.setOrderDetailId(in.nextString());
                    break;
                case "remarks":
                    model.setRemarks(in.nextString());
                    break;
                //Items
                case "items":
                    in.beginArray();
                    final List items = Lists.newArrayList();
                    while (in.hasNext()) {
                        items.add(RestSaveAccessoryItemRequestAdapter.getInstance().read(in));//调用OrderItem的序列化类
                    }
                    model.setItems(items);
                    in.endArray();
                    break;

            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final RestSaveAccessoryRequest entity) throws IOException {
        out.beginObject()
                .name("id").value(entity.getId()==null?"":entity.getId())
                .name("orderId").value(entity.getOrderId())
                .name("quarter").value(entity.getQuarter())
                .name("applyType").value(entity.getApplyType())
                .name("productId").value(entity.getProductId())
                .name("orderDetailId").value(StringUtils.isBlank(entity.getOrderDetailId())?"":entity.getOrderDetailId())
                .name("remarks").value(entity.getRemarks());
        //items
        out.name("items").beginArray();
        for(final RestSaveAccessoryItemRequest item :entity.getItems()){
            RestSaveAccessoryItemRequestAdapter.getInstance().write(out,item);
        }
        out.endArray();

        out.endObject();
    }

    private static RestSaveAccessoryRequestAdapter adapter;

    public RestSaveAccessoryRequestAdapter() {}

    public static RestSaveAccessoryRequestAdapter getInstance() {
        if (adapter == null){
            adapter = new RestSaveAccessoryRequestAdapter();
        }
        return adapter;
    }

}
