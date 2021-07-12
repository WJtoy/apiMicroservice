package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.UserRegion;

import java.io.IOException;

/**
 * 用户区域自定义Gson序列化/序列化
 */
public class UserRegionAdapter extends TypeAdapter<UserRegion> {

    @Override
    public UserRegion read(final JsonReader in) throws IOException {
        final UserRegion model = new UserRegion();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setId(0L);
                    }else {
                        model.setId(in.nextLong());
                    }
                    break;
                case "userId":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setUserId(0L);
                    }else {
                        model.setUserId(in.nextLong());
                    }
                    break;
                case "areaType":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setAreaType(0);
                    }else {
                        model.setAreaType(in.nextInt());
                    }
                    break;
                case "provinceId":
                    model.setProvinceId(in.nextLong());
                    break;
                case "cityId":
                    model.setCityId(in.nextLong());
                    break;
                case "areaId":
                    model.setAreaId(in.nextLong());
                    break;
                default:
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final UserRegion model) throws IOException {
        out.beginObject()
                .name("id").value(model.getId()==null?0L:model.getId())
                .name("userId").value(model.getUserId())
                .name("areaType").value(model.getAreaType())
                .name("provinceId").value(model.getProvinceId())
                .name("cityId").value(model.getCityId())
                .name("areaId").value(model.getAreaId())
            .endObject();
    }

    private static UserRegionAdapter adapter;
    public UserRegionAdapter() {}
    public static UserRegionAdapter getInstance() {
        if (adapter == null){
            adapter = new UserRegionAdapter();
        }
        return adapter;
    }
}
