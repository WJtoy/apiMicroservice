package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 安维网点的主帐号自定义Gson序列化/序列化
 */
public class ServicePointPrimaryAdapter extends TypeAdapter<Engineer> {

    @Override
    public Engineer read(final JsonReader in) throws IOException {
        final Engineer engineer = new Engineer();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    engineer.setId(in.nextLong());
                    break;
                case "name":
                    engineer.setName(in.nextString());
                    break;
                case "appFlag":
                    engineer.setAppFlag(in.nextInt());
                    break;
                case "contactInfo":
                    engineer.setContactInfo(in.nextString());
                    break;
                case "appLoged":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        engineer.setAppLoged(0);
                    }else {
                        engineer.setAppLoged(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return engineer;
    }

    @Override
    public void write(final JsonWriter out, final Engineer engineer) throws IOException {
        out.beginObject();
        out.name("id").value(engineer.getId());
        out.name("name").value(engineer.getName());
        out.name("appFlag").value(engineer.getAppFlag());
        out.name("contactInfo").value(engineer.getContactInfo());
        out.name("appLoged").value(engineer.getAppLoged());
        out.endObject();
    }

    private static ServicePointPrimaryAdapter adapter;
    public ServicePointPrimaryAdapter() {}
    public static ServicePointPrimaryAdapter getInstance() {
        if (adapter == null){
            adapter = new ServicePointPrimaryAdapter();
        }
        return adapter;
    }
}
