package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.viewModel.ServicePointCrush;

import java.io.IOException;

/**
 * 安维网点的主帐号自定义Gson序列化/序列化
 * 主要是在突击的时候保存在突击单里面的安维网店信息
 */
public class ServicePointCrushAdapter extends TypeAdapter<ServicePointCrush> {

    @Override
    public ServicePointCrush read(final JsonReader in) throws IOException {
        final ServicePointCrush servicePoint = new ServicePointCrush();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    servicePoint.setId(in.nextLong());
                    break;
                case "name":
                    servicePoint.setName(in.nextString());
                    break;
                case "contactInfo1":
                    servicePoint.setContactInfo1(in.nextString());
                    break;
                case "servicePointNo":
                    servicePoint.setServicePointNo(in.nextString());
                    break;
                case "address":
                    if(in.peek() == JsonToken.NULL){
                        servicePoint.setAddress("");
                    }else{
                        servicePoint.setAddress(in.nextString());
                    }
                    break;
                case "remarks":
                    servicePoint.setRemarks(in.nextString());
                    break;
                case "planRemark":
                    servicePoint.setPlanRemarks(in.nextString());
                    break;
                case "crushRemark":
                    servicePoint.setCrushRemarks(in.nextString());
                    break;
                case "seqNo":
                    servicePoint.setSeqNo(in.nextInt());
                    break;
                case "appFlag":
                    servicePoint.setAppFlag(in.nextString());
                    break;
                case "paymentType":
                    if(in.peek() == JsonToken.NULL){
                        servicePoint.setPaymentType("");
                    }else {
                        servicePoint.setPaymentType(in.nextString());
                    }
                    break;
                case "master":
                    servicePoint.setMaster(in.nextString());
                    break;
                case "orderCount":
                    if(in.peek() != JsonToken.NULL){
                        servicePoint.setOrderCount(in.nextInt());
                    }
                    break;
            }
        }
        in.endObject();
        return servicePoint;
    }

    @Override
    public void write(final JsonWriter out, final ServicePointCrush servicePoint) throws IOException {
        out.beginObject()
                .name("id").value(servicePoint.getId())
                .name("name").value(servicePoint.getName())
                .name("contactInfo1").value(servicePoint.getContactInfo1())
                .name("servicePointNo").value(servicePoint.getServicePointNo())
                .name("address").value(servicePoint.getAddress())
                .name("remarks").value(servicePoint.getRemarks())
                .name("planRemark").value(servicePoint.getPlanRemarks())
                .name("crushRemark").value(servicePoint.getCrushRemarks())
                .name("appFlag").value(servicePoint.getAppFlag())
                .name("paymentType").value(servicePoint.getPaymentType())
                .name("seqNo").value(servicePoint.getSeqNo())
                .name("master").value(servicePoint.getMaster())
                .name("orderCount").value(servicePoint.getOrderCount())
            .endObject();
    }

    private static ServicePointCrushAdapter adapter;
    public ServicePointCrushAdapter() {}
    public static ServicePointCrushAdapter getInstance() {
        if (adapter == null){
            adapter = new ServicePointCrushAdapter();
        }
        return adapter;
    }
}
