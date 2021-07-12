package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * 加急等级自定义Gson序列化/序列化
 */
public class UrgentLevelSimpleAdapter extends TypeAdapter<UrgentLevel> {

    @Override
    public UrgentLevel read(final JsonReader in) throws IOException {
        final UrgentLevel model = new UrgentLevel();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        model.setId(null);
                        in.nextNull();
                    }else {
                        model.setId(in.nextLong());
                    }
                    break;
                case "chargeIn":
                    if(in.peek() == JsonToken.NULL){
                        model.setChargeIn(0.0d);
                        in.nextNull();
                    }else {
                        model.setChargeIn(in.nextDouble());
                    }
                    break;
                case "chargeOut":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setChargeOut(0.00d);
                    }else {
                        model.setChargeOut(in.nextDouble());
                    }
                    break;
                case "remarks":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                    }else {
                        model.setRemarks(in.nextString());
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final UrgentLevel model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId());
        out.name("chargeIn").value(model.getChargeIn());
        out.name("chargeOut").value(model.getChargeOut());
        out.name("remarks").value(model.getRemarks());
        out.endObject();
    }

    private static UrgentLevelSimpleAdapter adapter;
    public UrgentLevelSimpleAdapter() {}
    public static UrgentLevelSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new UrgentLevelSimpleAdapter();
        }
        return adapter;
    }
}
