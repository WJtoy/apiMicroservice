package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.entity.ServiceType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 服务类型（简单）自定义Gson序列化/序列化
 */
public class ServiceTypeSimpleAdapter extends TypeAdapter<ServiceType> {

    @Override
    public ServiceType read(final JsonReader in) throws IOException {
        final ServiceType st = new ServiceType();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    st.setId(in.nextLong());
                    break;
                case "code":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        st.setCode("");
                    }else {
                        st.setCode(in.nextString());
                    }
                    break;
                case "name":
                    st.setName(in.nextString());
                    break;
                case "orderServiceType":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        st.setOrderServiceType(0);
                    }else {
                        st.setOrderServiceType(in.nextInt());
                    }
                    break;
                case "autoGradeFlag":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        st.setAutoGradeFlag(0);
                    }else {
                        st.setAutoGradeFlag(in.nextInt());
                    }
                    break;
                case "autoChargeFlag":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        st.setAutoChargeFlag(0);
                    }else {
                        st.setAutoChargeFlag(in.nextInt());
                    }
                    break;
                case "relateErrorTypeFlag":
                    if(in.peek()== JsonToken.NULL){
                        in.nextNull();
                        st.setRelateErrorTypeFlag(0);
                    }else {
                        st.setRelateErrorTypeFlag(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return st;
    }

    @Override
    public void write(final JsonWriter out, final ServiceType serviceType) throws IOException {
        out.beginObject()
                .name("id").value(serviceType.getId())
                .name("code").value(serviceType.getCode())
                .name("name").value(serviceType.getName())
                .name("orderServiceType").value(serviceType.getOrderServiceType())
                .name("autoGradeFlag").value(serviceType.getAutoGradeFlag())
                .name("autoChargeFlag").value(serviceType.getAutoChargeFlag())
                .name("relateErrorTypeFlag").value(serviceType.getRelateErrorTypeFlag())
        .endObject();
    }

    private static ServiceTypeSimpleAdapter adapter;
    public ServiceTypeSimpleAdapter() {}
    public static ServiceTypeSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new ServiceTypeSimpleAdapter();
        }
        return adapter;
    }
}
