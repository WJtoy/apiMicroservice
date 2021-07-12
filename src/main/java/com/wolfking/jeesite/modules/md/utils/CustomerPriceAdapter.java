package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 客户服务价格自定义Gson序列化/序列化
 */
public class CustomerPriceAdapter extends TypeAdapter<CustomerPrice> {

    @Override
    public CustomerPrice read(final JsonReader in) throws IOException {
        final CustomerPrice price = new CustomerPrice();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    price.setId(in.nextLong());
                    break;
                case "customer":
                    Customer customer = new Customer();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                customer.setId(in.nextLong());
                                break;
                        }
                    }
                    price.setCustomer(customer);
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
                            case "model":
                                product.setModel(in.nextString());
                                break;
                            case "brand":
                                product.setBrand(in.nextString());
                                break;
                            case "sort":
                                if(in.peek() == null){
                                    in.nextNull();
                                }else{
                                    product.setSort(in.nextInt());
                                }
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
                            case "code":
                                serviceType.setCode(in.nextString());
                                break;
                            case "name":
                                serviceType.setName(in.nextString());
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
                case "blockedPrice":
                    price.setBlockedPrice(in.nextDouble());
                    break;
                case "remarks":
                    price.setRemarks(in.nextString());
                    break;
            }
        }

        in.endObject();

        return price;
    }

    @Override
    public void write(final JsonWriter out, final CustomerPrice price) throws IOException {
        out.beginObject();
        out.name("id").value(price.getId());

        if(price.getCustomer() != null){
            out.name("customer")
                    .beginObject()
                    .name("id").value(price.getCustomer().getId())
                    .endObject();
        }
        if(price.getProduct() != null){
            out.name("product")
                    .beginObject()
                    .name("id").value(price.getProduct().getId())
                    .name("name").value(price.getProduct().getName())
                    .name("brand").value(price.getProduct().getBrand())
                    .name("model").value(price.getProduct().getModel())
                    .name("sort").value(price.getProduct().getSort())
                    .endObject();
        }
        if(price.getServiceType() != null){
            out.name("serviceType")
                    .beginObject()
                    .name("id").value(price.getServiceType().getId())
                    .name("name").value(price.getServiceType().getName())
                    .name("code").value(price.getServiceType().getCode())
                    .endObject();
        }
        out.name("price").value(price.getPrice());
        out.name("discountPrice").value(price.getDiscountPrice());
        out.name("blockedPrice").value(price.getBlockedPrice());
        out.name("remarks").value(price.getRemarks());


        out.endObject();
    }

    private static CustomerPriceAdapter adapter;
    public CustomerPriceAdapter() {}
    public static CustomerPriceAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerPriceAdapter();
        }
        return adapter;
    }
}
