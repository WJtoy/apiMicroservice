package com.wolfking.jeesite.ms.tmall.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;

import java.io.IOException;

/**
 * Gson序列化
 */
public class B2bCustomerMapSimpleAdapter extends TypeAdapter<B2bCustomerMap> {

    @Override
    public B2bCustomerMap read(final JsonReader in) throws IOException {
        final B2bCustomerMap item = new B2bCustomerMap();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();//must
                        item.setId(0l);
                    }else {
                        item.setId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "dataSource":
                    if(in.peek() == JsonToken.NULL){
                          in.nextNull();//must
                        item.setDataSource(0);
                    }else {
                        item.setDataSource(in.nextInt());
                    }
                    break;
                case "customerId":
                    if(in.peek() == JsonToken.NULL){
                          in.nextNull();//must
                        item.setCustomerId(0l);
                    }else {
                        item.setCustomerId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "customerName":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setCustomerName("");
                    }else {
                        item.setCustomerName(in.nextString());
                    }
                    break;
                case "shopId":
                    item.setShopId(in.nextString());
                    break;
                case "shopName":
                    item.setShopName(in.nextString());
                    break;
            }
        }
        in.endObject();
        return item;
    }


    @Override
    public void write(final JsonWriter out, final B2bCustomerMap item) throws IOException {
        out.beginObject();
        out.name("id").value(item.getId()==null?null:item.getId().toString())
                .name("dataSource").value(item.getDataSource())
                .name("customerId").value(item.getCustomerId()==null?"0":item.getCustomerId().toString())
                .name("customerName").value(item.getCustomerName()==null?"":item.getCustomerName())
                .name("shopId").value(item.getShopId()==null?"":item.getShopId())
                .name("shopName").value(item.getShopName()==null?"":item.getShopName());
        out.endObject();
    }

    private static B2bCustomerMapSimpleAdapter adapter;
    public B2bCustomerMapSimpleAdapter() {}
    public static B2bCustomerMapSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new B2bCustomerMapSimpleAdapter();
        }
        return adapter;
    }
}
