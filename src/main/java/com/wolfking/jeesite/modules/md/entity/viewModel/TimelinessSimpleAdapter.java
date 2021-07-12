package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.io.IOException;

public class TimelinessSimpleAdapter extends TypeAdapter<TimelinessLevel> {
    @Override
    public void write(JsonWriter out, TimelinessLevel value) throws IOException {
        out.beginObject()
                .name("id").value(value.getId())
                .name("name").value(value.getName())
                .endObject();
    }

    @Override
    public TimelinessLevel read(JsonReader in) throws IOException {
        final TimelinessLevel model = new TimelinessLevel();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "name":
                    model.setName(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }
}
