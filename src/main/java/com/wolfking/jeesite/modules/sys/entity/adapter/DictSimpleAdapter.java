package com.wolfking.jeesite.modules.sys.entity.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.io.IOException;

/**
 * 数据字典（简单）自定义Gson序列化/序列化
 */
public class DictSimpleAdapter extends TypeAdapter<Dict> {

    @Override
    public Dict read(final JsonReader in) throws IOException {
        final Dict model = new Dict();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "label":
                    model.setLabel(in.nextString());
                    break;
                case "value":
                    model.setValue(in.nextString());
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final Dict model) throws IOException {
        out.beginObject();
        out.name("label").value(model.getLabel());
        out.name("value").value(model.getValue());
        out.endObject();
    }

    private static DictSimpleAdapter adapter;
    public DictSimpleAdapter() {}
    public static DictSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new DictSimpleAdapter();
        }
        return adapter;
    }

}
