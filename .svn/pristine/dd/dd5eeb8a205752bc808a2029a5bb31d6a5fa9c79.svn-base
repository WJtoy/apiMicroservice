package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;

import java.io.IOException;
import java.util.Map.Entry;

/**
 * 安维 Gson序列化/序列化
 */
public class EngineerAdapter extends TypeAdapter<Engineer> {

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
                case "servicePoint":
                    in.beginObject();
                    ServicePoint servicePoint = new ServicePoint();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                servicePoint.setId(in.nextLong());
                                break;
                        }
                    }
                    engineer.setServicePoint(servicePoint);
                    in.endObject();
                    break;
                case "contactInfo":
                    engineer.setContactInfo(in.nextString());
                    break;
                case "address":
                    engineer.setAddress(in.nextString());
                    break;
                case "masterFlag":
                    engineer.setMasterFlag(in.nextInt());
                    break;
                case "appFlag":
                    engineer.setAppFlag(in.nextInt());
                    break;
                case "appLoged":
                    if(in.peek()== JsonToken.NULL){
                        engineer.setAppLoged(0);
                        in.nextNull();
                    }else {
                        engineer.setAppLoged(in.nextInt());
                    }
                    break;
                case "grade":
                    engineer.setGrade(in.nextDouble());
                    break;
                case "level":
                    engineer.setLevel(DictSimpleAdapter.getInstance().read(in));
                    break;
                case "orderCount":
                    engineer.setOrderCount(in.nextInt());
                    break;
                case "planCount":
                    engineer.setPlanCount(in.nextInt());
                    break;
                case "breakCount":
                    engineer.setBreakCount(in.nextInt());
                    break;
                case "accountId":
                    if(in.peek() == JsonToken.NULL){
                        engineer.setAccountId(0l);
                        in.nextNull();
                    }else{
                        engineer.setAccountId(in.nextLong());
                    }
                    break;
                case "temperature":
                    engineer.setTemperature(in.nextDouble());
                    break;
                case "forTmall": //B2B
                    engineer.setForTmall(in.nextInt());
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
        if(engineer.getServicePoint()!=null && engineer.getServicePoint().getId() != null){
            out.name("servicePoint")
                    .beginObject()
                    .name("id").value(engineer.getServicePoint().getId())
                    .endObject();
        }
        out.name("contactInfo").value(engineer.getContactInfo())
                .name("address").value(engineer.getAddress())
                .name("masterFlag").value(engineer.getMasterFlag())
                .name("appFlag").value(engineer.getAppFlag())
                .name("appLoged").value(engineer.getAppLoged())
                .name("grade").value(engineer.getGrade());
        if(engineer.getLevel() != null){
            out.name("level");
            DictSimpleAdapter.getInstance().write(out,engineer.getLevel());
        }
        out.name("orderCount").value(engineer.getOrderCount())
                .name("planCount").value(engineer.getPlanCount())
                .name("breakCount").value(engineer.getBreakCount());
        if(engineer.getAccountId()==null){
            out.name("accountId").value(0l);
        }else{
            out.name("accountId").value(engineer.getAccountId());
        }
        out.name("temperature").value(engineer.getTemperature());
        out.name("forTmall").value(engineer.getForTmall()); //B2B
        out.endObject();
    }

    private static EngineerAdapter adapter;
    public EngineerAdapter() {}
    public static EngineerAdapter getInstance() {
        if (adapter == null){
            adapter = new EngineerAdapter();
        }
        return adapter;
    }

}
