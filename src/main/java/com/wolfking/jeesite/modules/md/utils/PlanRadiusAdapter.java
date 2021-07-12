package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.PlanRadius;
import com.wolfking.jeesite.modules.md.entity.ServicePointStation;
import com.wolfking.jeesite.modules.sys.entity.Area;

import java.io.IOException;
import java.util.Optional;

public class PlanRadiusAdapter extends TypeAdapter<PlanRadius> {

    @Override
    public void write(JsonWriter out, PlanRadius planRadius) throws IOException {
        out.beginObject();
        out.name("id").value(planRadius.getId())
                .name("radius1").value(planRadius.getRadius1())
                .name("radius2").value(planRadius.getRadius1())
                .name("radius3").value(planRadius.getRadius1())
                .name("autoPlanFlag").value(planRadius.getAutoPlanFlag());

        Optional.ofNullable(planRadius).map(PlanRadius::getArea).ifPresent(r->{
            try {
                out.name("area").beginObject()
                    .name("id").value(r.getId())
                    .name("name").value(r.getName())
                    //.name("fullName").value(r.getFullName())
                    .name("type").value(r.getType())
                    .endObject();
            } catch(Exception ex) {}
        });
        out.endObject();
    }

    @Override
    public PlanRadius read(JsonReader in) throws IOException {
        PlanRadius planRadius = PlanRadius.builder().build();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    planRadius.setId(in.nextLong());
                    break;
                case "radius1":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        planRadius.setRadius1(0);
                    } else {
                        planRadius.setRadius1(in.nextInt());
                    }
                    break;
                case "radius2":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        planRadius.setRadius2(0);
                    } else {
                        planRadius.setRadius2(in.nextInt());
                    }
                    break;
                case "radius3":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        planRadius.setRadius3(0);
                    } else {
                        planRadius.setRadius3(in.nextInt());
                    }
                    break;
                case "autoPlanFlag":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        planRadius.setAutoPlanFlag(0);
                    } else {
                        planRadius.setAutoPlanFlag(in.nextInt());
                    }
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
//                            case "fullName":
//                                area.setFullName(in.nextString());
//                                break;
                            case "type":
                                area.setType(in.nextInt());
                        }
                    }
                    planRadius.setArea(area);
                    in.endObject();
                    break;
            }
        }
        in.endObject();
        return planRadius;
    }

    private static PlanRadiusAdapter adapter;
    public PlanRadiusAdapter() {}
    public static PlanRadiusAdapter getInstance() {
        if (adapter == null){
            adapter = new PlanRadiusAdapter();
        }
        return adapter;
    }
}
