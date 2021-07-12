package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;

import java.io.IOException;

public class UrgentSimpleAdapter extends TypeAdapter<UrgentLevel> {
    @Override
    public void write(JsonWriter out, UrgentLevel value) throws IOException {
        out.beginObject()
                .name("id").value(value.getId())
                .name("label").value(value.getLabel())
                .endObject();
    }

    @Override
    public UrgentLevel read(JsonReader in) throws IOException {
        final UrgentLevel model = new UrgentLevel();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    model.setId(in.nextLong());
                    break;
                case "label":
                    model.setLabel(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }
}
