package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.io.IOException;

/**
 * 用户（简单）自定义Gson序列化/序列化
 */
public class UserSimpleAdapter extends TypeAdapter<User> {

    @Override
    public User read(final JsonReader in) throws IOException {
        final User model = new User();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                    }else {
                        model.setId(in.nextLong());
                    }
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
                case "userType":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        model.setUserType(0);
                    }else {
                        model.setUserType(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final User model) throws IOException {
        out.beginObject()
                .name("id").value(model.getId())
                .name("name").value(model.getName())
                .name("userType").value(model.getUserType())
            .endObject();
    }

    private static UserSimpleAdapter adapter;
    public UserSimpleAdapter() {}
    public static UserSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new UserSimpleAdapter();
        }
        return adapter;
    }
}
