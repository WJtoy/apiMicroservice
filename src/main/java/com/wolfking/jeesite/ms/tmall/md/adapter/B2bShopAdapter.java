package com.wolfking.jeesite.ms.tmall.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;

import java.io.IOException;

/**
 * 店铺Gson序列化
 */
public class B2bShopAdapter extends TypeAdapter<B2bCustomerMap> {

    @Override
    public B2bCustomerMap read(final JsonReader in) throws IOException {
        final B2bCustomerMap item = new B2bCustomerMap();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
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
        out.name("shopId").value(item.getShopId()==null?"":item.getShopId())
           .name("shopName").value(item.getShopName()==null?"":item.getShopName());
        out.endObject();
    }

    private static B2bShopAdapter adapter;
    public B2bShopAdapter() {}
    public static B2bShopAdapter getInstance() {
        if (adapter == null){
            adapter = new B2bShopAdapter();
        }
        return adapter;
    }
}
