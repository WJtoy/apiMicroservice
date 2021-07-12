package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 订单实际服务明细Gson序列化
 * todo: 完工维修属性未处理 2019-12-28
 */
public class OrderDetailAdapter extends TypeAdapter<OrderDetail> {

    private final String  dateFormat = new String("yyyy-MM-dd HH:mm:ss");

    @Override
    public OrderDetail read(final JsonReader in) throws IOException {
        final OrderDetail item = new OrderDetail();
        StringBuilder strd = new StringBuilder();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    item.setId(Long.valueOf(in.nextString()));
                    break;
                case "orderId":
                    item.setOrderId(Long.valueOf(in.nextString()));
                    break;
                case "itemNo":
                    item.setItemNo(in.nextInt());
                    break;
                case "serviceTimes":
                    item.setServiceTimes(in.nextInt());
                    break;
                case "brand":
                    item.setBrand(in.nextString());
                    break;
                case "productSpec":
                    item.setProductSpec(in.nextString());
                    break;
                case "qty":
                    item.setQty(in.nextInt());
                    break;
                //customer
                case "standPrice":
                    item.setStandPrice(in.nextDouble());
                    break;
                case "discountPrice":
                    item.setDiscountPrice(in.nextDouble());
                    break;
                case "charge":
                    item.setCharge(in.nextDouble());
                    break;
                case "materialCharge":
                    item.setMaterialCharge(in.nextDouble());
                    break;
                case "expressCharge":
                    item.setExpressCharge(in.nextDouble());
                    break;
                case "travelCharge":
                    item.setTravelCharge(in.nextDouble());
                    break;
                case "travelNo":
                    item.setTravelNo(in.nextString());
                    break;
                case "otherCharge":
                    item.setOtherCharge(in.nextDouble());
                    break;
                //engineer
                case "engineerStandPrice":
                    item.setEngineerStandPrice(in.nextDouble());
                    break;
                case "engineerDiscountPrice":
                    item.setEngineerDiscountPrice(in.nextDouble());
                    break;
                case "engineerServiceCharge":
                    item.setEngineerServiceCharge(in.nextDouble());
                    break;
                case "engineerTravelCharge":
                    item.setEngineerTravelCharge(in.nextDouble());
                    break;
                case "engineerExpressCharge":
                    item.setEngineerExpressCharge(in.nextDouble());
                    break;
                case "engineerMaterialCharge":
                    item.setEngineerMaterialCharge(in.nextDouble());
                    break;
                case "engineerOtherCharge":
                    item.setEngineerOtherCharge(in.nextDouble());
                    break;
                case "engineerInvoiceDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        item.setEngineerInvoiceDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            item.setEngineerInvoiceDate(date);
                        } catch (ParseException e) {
                            item.setEngineerInvoiceDate(null);
                            try {
                                LogUtils.saveLog("日期格式错误:", "OrderDetailAdapter.read", String.format("id:%s,engineerInvoiceDate:%s", item.getId(), strd), e, null);
                            }catch (Exception e1){}
                        }
                    }
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
                    break;
                case "delFlag":
                    item.setDelFlag(in.nextInt());
                    break;
                case "servicePoint":
                    ServicePoint servicePoint = new ServicePoint();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                servicePoint.setId(Long.valueOf(in.nextString()));
                                break;
                            case "name":
                                servicePoint.setName(in.nextString());
                                break;
                        }
                    }
                    item.setServicePoint(servicePoint);
                    in.endObject();
                    break;
                case "engineer":
                    Engineer engineer = new Engineer();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                engineer.setId(Long.valueOf(in.nextString()));
                                break;
                            case "name":
                                engineer.setName(in.nextString());
                                break;
                        }
                    }
                    item.setEngineer(engineer);
                    in.endObject();
                    break;
                case "engineerPaymentType":
                    Dict pt = new Dict();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "value":
                                pt.setValue(in.nextString());
                                break;
                            case "label":
                                pt.setLabel(in.nextString());
                                break;
                        }
                    }
                    item.setEngineerPaymentType(pt);
                    in.endObject();
                    break;
                case "serviceType":
                    ServiceType serviceType = new ServiceType();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                serviceType.setId(in.nextLong());
                                break;
                            case "name":
                                serviceType.setName(in.nextString());
                                break;
                        }
                    }
                    item.setServiceType(serviceType);
                    in.endObject();
                    break;
                case "product":
                    Product product = new Product();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                product.setId(in.nextLong());
                                break;
                            case "name":
                                product.setName(in.nextString());
                                break;
                            case "setFlag":
                                if(in.peek() == JsonToken.NULL){
                                    in.nextNull();
                                    product.setSetFlag(0);
                                }else{
                                    product.setSetFlag(in.nextInt());
                                }
                                break;
                            case "category":
                                ProductCategory category = new ProductCategory();
                                in.beginObject();
                                while (in.hasNext()){
                                    switch (in.nextName()) {
                                        case "id":
                                            category.setId(in.nextLong());
                                            break;
                                    }
                                }
                                product.setCategory(category);
                                in.endObject();
                                break;
                        }
                    }
                    item.setProduct(product);
                    in.endObject();
                    break;
                case "createDate":
                    strd.setLength(0);
                    strd.append(in.nextString());
                    if(StringUtils.isBlank(strd)){
                        item.setCreateDate(null);
                    }else{
                        try{
                            Date date = DateUtils.parse(strd.toString(),dateFormat);
                            item.setCreateDate(date);
                        } catch (ParseException e) {
                            item.setCreateDate(null);
                            try {
                                LogUtils.saveLog("日期格式错误:", "OrderDetailAdapter.read", String.format("id:%s,engineerInvoiceDate:%s", item.getId(), strd), e, null);
                            }catch (Exception e1){}
                        }
                    }
                    break;
            }
        }
        in.endObject();
        strd.setLength(0);
        return item;
    }
    
    @Override
    public void write(final JsonWriter out, final OrderDetail item) throws IOException {
        out.beginObject();
        out.name("id").value(item.getId().toString())
                .name("orderId").value(item.getOrderId().toString())
                .name("itemNo").value(item.getItemNo())
                .name("serviceTimes").value(item.getServiceTimes())
                .name("brand").value(item.getBrand())
                .name("productSpec").value(item.getProductSpec())
                .name("qty").value(item.getQty())
                .name("remarks").value(item.getRemarks())
                .name("delFlag").value(item.getDelFlag());
        //engineer
        out.name("engineerStandPrice").value(item.getEngineerStandPrice())
                .name("engineerDiscountPrice").value(item.getEngineerDiscountPrice())
                .name("engineerServiceCharge").value(item.getEngineerServiceCharge())
                .name("engineerTravelCharge").value(item.getEngineerTravelCharge())
                .name("engineerExpressCharge").value(item.getEngineerExpressCharge())
                .name("engineerMaterialCharge").value(item.getEngineerMaterialCharge())
                .name("engineerOtherCharge").value(item.getEngineerOtherCharge());
        if(item.getEngineerInvoiceDate() !=null){
            out.name("engineerInvoiceDate").value(DateUtils.formatDate(item.getEngineerInvoiceDate(),dateFormat));
        }
        //cucsomter
        out.name("standPrice").value(item.getStandPrice())
                .name("discountPrice").value(item.getDiscountPrice())
                .name("charge").value(item.getCharge())
                .name("materialCharge").value(item.getMaterialCharge())
                .name("expressCharge").value(item.getExpressCharge())
                .name("travelCharge").value(item.getTravelCharge())
                .name("travelNo").value(item.getTravelNo())
                .name("otherCharge").value(item.getOtherCharge());
        if(item.getServicePoint() !=null && item.getServicePoint().getId() != null){
            out.name("servicePoint")
                    .beginObject()
                    .name("id").value(item.getServicePoint().getId().toString())
                    .name("name").value(item.getServicePoint().getName())
                    .endObject();
        }
        if(item.getEngineer() != null && item.getEngineer().getId() != null){
            out.name("engineer")
                    .beginObject()
                    .name("id").value(item.getEngineer().getId().toString())
                    .name("name").value(item.getEngineer().getName())
                    .endObject();
        }
        if(item.getEngineerPaymentType() != null && StringUtils.isNoneBlank(item.getEngineerPaymentType().getValue()) ){
            out.name("engineerPaymentType")
                    .beginObject()
                    .name("value").value(item.getEngineerPaymentType().getValue())
                    .name("label").value(item.getEngineerPaymentType().getLabel())
                    .endObject();
        }
        if(item.getServiceType() != null) {
            out.name("serviceType")
                    .beginObject()
                    .name("id").value(item.getServiceType().getId())
                    .name("name").value(item.getServiceType().getName())
                    .endObject();
        }
        if(item.getProduct() != null) {
            if (item.getProduct().getCategory() != null) {
                out.name("product")
                        .beginObject()
                        .name("id").value(item.getProduct().getId())
                        .name("name").value(item.getProduct().getName())
                        .name("setFlag").value(item.getProduct().getSetFlag())
                        .name("category").beginObject().name("id").value(item.getProduct().getCategory().getId()).endObject()
                        .endObject();
            }else{
                out.name("product")
                        .beginObject()
                        .name("id").value(item.getProduct().getId())
                        .name("name").value(item.getProduct().getName())
                        .endObject();
            }
        }
        if(item.getCreateDate() != null){
            out.name("createDate").value(DateUtils.formatDate(item.getCreateDate(),dateFormat));
        }
        out.endObject();
    }

    private static OrderDetailAdapter adapter;
    public OrderDetailAdapter() {}
    public static OrderDetailAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderDetailAdapter();
        }
        return adapter;
    }
}
