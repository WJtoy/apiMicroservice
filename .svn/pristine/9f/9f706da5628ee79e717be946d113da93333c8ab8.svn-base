package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.sys.entity.User;

import java.io.IOException;

/**
 * 客户简单内容 Gson序列化/序列化
 */
public class CustomerSalesAdapter extends TypeAdapter<Customer> {

    @Override
    public Customer read(final JsonReader in) throws IOException {
        final Customer customer = new Customer();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    customer.setId(in.nextLong());
                    break;
                case "name":
                    customer.setName(in.nextString());
                    break;
                case "sales":
                    in.beginObject();
                    User sales = new User(0l,"");
                    if(in.peek() == JsonToken.NULL){
                        sales.setQq("");
                        sales.setMobile("");
                    }else {
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id":
                                    sales.setId(Long.valueOf(in.nextString()));
                                    break;
                                case "name":
                                    sales.setName(in.nextString());
                                    break;
                                case "qq":
                                    sales.setQq(in.nextString());
                                    break;
                                case "mobile":
                                    sales.setMobile(in.nextString());
                                    break;
                            }
                        }
                    }
                    customer.setSales(sales);
                    in.endObject();
                    break;
            }
        }

        in.endObject();
        return customer;
    }

    @Override
    public void write(final JsonWriter out, final Customer customer) throws IOException {
        out.beginObject();
        out.name("id").value(customer.getId());
        out.name("name").value(customer.getName());
        User sales = customer.getSales();
        if(sales !=null){
            out.name("sales")
                    .beginObject()
                    .name("id").value(sales.getId().toString())
                    .name("name").value(sales.getName())
                    .name("qq").value(sales.getQq())
                    .name("mobile").value(sales.getMobile())
                    .endObject();
        }
        out.endObject();
    }

    private static CustomerSalesAdapter adapter;
    public CustomerSalesAdapter() {}
    public static CustomerSalesAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerSalesAdapter();
        }
        return adapter;
    }
}
