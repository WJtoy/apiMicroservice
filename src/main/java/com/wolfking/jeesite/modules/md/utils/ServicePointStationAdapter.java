package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointStation;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class ServicePointStationAdapter extends TypeAdapter<ServicePointStation> {
    @Override
    public void write(JsonWriter out, ServicePointStation servicePointStation) throws IOException {
        out.beginObject();
        out.name("id").value(servicePointStation.getId())
                .name("name").value(servicePointStation.getName())
                .name("address").value(servicePointStation.getAddress())
                .name("longtitude").value(servicePointStation.getLongtitude())
                .name("latitude").value(servicePointStation.getLatitude())
                .name("radius").value(servicePointStation.getRadius())
                .name("autoPlanFlag").value(servicePointStation.getAutoPlanFlag());
        Optional.ofNullable(servicePointStation).map(ServicePointStation::getArea).ifPresent(r->{
            try {
                out.name("area").beginObject()
                        .name("id").value(r.getId())
                        .name("name").value(r.getName())
                        .name("type").value(r.getType())
                        .name("parent").beginObject()
                            .name("id").value(r.getParent()==null?-1L:(r.getParent().getId()==null?-1L:r.getParent().getId()))
                            .endObject()
                        .endObject();
            } catch(Exception ex) {}
        });
        out.endObject();
    }

    @Override
    public ServicePointStation read(JsonReader in) throws IOException {
        final ServicePointStation servicePointStation = new ServicePointStation();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()){
                case "id":
                    servicePointStation.setId(in.nextLong());
                    break;
                case "name":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        servicePointStation.setName("");
                    } else {
                        servicePointStation.setName(in.nextString());
                    }
                    break;
                case "address":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        servicePointStation.setAddress("");
                    } else {
                        servicePointStation.setAddress(in.nextString());
                    }
                    break;
                case "longtitude":
                    servicePointStation.setLatitude(in.nextDouble());
                    break;
                case "latitude":
                    servicePointStation.setLatitude(in.nextDouble());
                    break;
                case "radius":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        servicePointStation.setRadius(0);
                    } else {
                        servicePointStation.setRadius(in.nextInt());
                    }
                    break;
                case "autoPlanFlag":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        servicePointStation.setAutoPlanFlag(0);
                    } else {
                        servicePointStation.setAutoPlanFlag(in.nextInt());
                    }
                    break;
                /*case "servicePoint":
                    //servicePointStation.setServicePoint(servicePointAdapter.read(in));
                    in.beginObject();
                    ServicePoint servicePoint = new ServicePoint();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                servicePoint.setId(in.nextLong());
                                break;
                            case "name":
                                if (in.peek() == JsonToken.NULL) {
                                    in.nextNull();
                                    servicePoint.setName("");
                                } else {
                                    servicePoint.setName(in.nextString());
                                }
                                break;
                            case "autoPlanFlag":
                                if (in.peek() == JsonToken.NULL) {
                                    in.nextNull();
                                    servicePoint.setAutoPlanFlag(0);
                                } else {
                                    servicePoint.setAutoPlanFlag(in.nextInt());
                                }
                                break;
                        }
                    }
                    servicePointStation.setServicePoint(servicePoint);
                    in.endObject();
                    break;*/
                case "area":
                    //servicePointStation.setArea(areaAdapter.read(in));
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
                            case "parent":
                                in.beginObject();
                                Area parentArea = new Area();
                                while (in.hasNext()){
                                    switch (in.nextName()){
                                        case "id":
                                            if (in.peek() == JsonToken.NULL) {
                                                in.nextNull();
                                                parentArea.setId(0L);
                                            } else {
                                                parentArea.setId(in.nextLong());
                                            }
                                            break;
                                    }
                                }
                                area.setParent(parentArea);
                                in.endObject();
                                break;
                        }
                    }
                    servicePointStation.setArea(area);
                    in.endObject();
                    break;
            }
        }
        in.endObject();

        return servicePointStation;
    }

    private static ServicePointStationAdapter adapter;
    public ServicePointStationAdapter() {}
    public static ServicePointStationAdapter getInstance() {
        if (adapter == null){
            adapter = new ServicePointStationAdapter();
        }
        return adapter;
    }
}
