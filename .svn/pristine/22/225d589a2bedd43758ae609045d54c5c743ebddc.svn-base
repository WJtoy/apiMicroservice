package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.AreaTimeLiness;
import com.wolfking.jeesite.modules.md.entity.Brand;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;

/**
 * 自定义Gson序列化/序列化
 *
 * @author Ryan Lu
 * @date 2019/5/18 3:59 PM
 * @since 1.0.0
 */
public class AreaTimeLinessAdapter extends TypeAdapter<AreaTimeLiness>  {
    @Override
    public AreaTimeLiness read(final JsonReader in) throws IOException {
        final AreaTimeLiness model = new AreaTimeLiness();
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
                case "isOpen":
                    model.setIsOpen(in.nextInt());
                    break;
                case "productCategoryId":
                    model.setProductCategoryId(in.nextLong());
                    break;
                case "area":
                    in.beginObject();
                    Area area = new Area();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                area.setId(in.nextLong());
                                break;
                            case "name":
                                area.setName(in.nextString());
                                break;
                            case "type":
                                area.setType(in.nextInt());
                                break;
                        }
                    }
                    model.setArea(area);
                    in.endObject();
                    break;
            }
        }
        in.endObject();
        return model;
    }

    @Override
    public void write(final JsonWriter out, final AreaTimeLiness model) throws IOException {
        out.beginObject();
        out.name("id").value(model.getId())
            .name("isOpen").value(model.getIsOpen());
        out.name("productCategoryId").value(model.getProductCategoryId());
        if(model.getArea() != null) {
            Area area = model.getArea();
            out.name("area").beginObject()
                    .name("id").value(area.getId())
                    .name("name").value(area.getName())
                    .name("type").value(area.getType())
                    .endObject();
        }
        out.endObject();
    }

    private static AreaTimeLinessAdapter adapter;

    public AreaTimeLinessAdapter() {}

    public static AreaTimeLinessAdapter getInstance() {
        if (adapter == null){
            adapter = new AreaTimeLinessAdapter();
        }
        return adapter;
    }
}
