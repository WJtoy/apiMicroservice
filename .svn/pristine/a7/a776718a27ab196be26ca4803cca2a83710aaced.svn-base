package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.entity.OrderStatus;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 安维网点价格自定义Gson序列化/序列化
 */
public class ServicePriceAdapter extends TypeAdapter<ServicePrice> {

    @Override
    public ServicePrice read(final JsonReader in) throws IOException {
        final ServicePrice price = new ServicePrice();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    price.setId(in.nextLong());
                    break;
                case "servicePoint":
                    ServicePoint servicePoint = new ServicePoint();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                servicePoint.setId(in.nextLong());
                                break;
//                            case "servicePointNo":
//                                servicePoint.setServicePointNo(in.nextString());
//                                break;
//                            case "name":
//                                servicePoint.setName(in.nextString());
//                                break;
                        }
                    }
                    price.setServicePoint(servicePoint);
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
                        }
                    }
                    price.setProduct(product);
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
                        }
                    }
                    price.setServiceType(serviceType);
                    in.endObject();
                    break;
                //接单(客服)
                case "price":
                    price.setPrice(in.nextDouble());
                    break;
                case "discountPrice":
                    price.setDiscountPrice(in.nextDouble());
                    break;
            }
        }

        in.endObject();

        return price;
    }

    @Override
    public void write(final JsonWriter out, final ServicePrice price) throws IOException {
        out.beginObject();
        out.name("id").value(price.getId());

        if(price.getServicePoint() != null){
            out.name("servicePoint")
                    .beginObject()
                    .name("id").value(price.getServicePoint().getId())
                    .endObject();
        }
        if(price.getProduct() != null){
            out.name("product")
                    .beginObject()
                    .name("id").value(price.getProduct().getId())
                    .endObject();
        }
        if(price.getServiceType() != null){
            out.name("serviceType")
                    .beginObject()
                    .name("id").value(price.getServiceType().getId())
                    .endObject();
        }
        out.name("price").value(price.getPrice());
        out.name("discountPrice").value(price.getDiscountPrice());


        out.endObject();
    }

    private static ServicePriceAdapter adapter;
    public ServicePriceAdapter() {}
    public static ServicePriceAdapter getInstance() {
        if (adapter == null){
            adapter = new ServicePriceAdapter();
        }
        return adapter;
    }
}
