package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;

import java.io.IOException;

/**
 * 客户简单内容 Gson序列化/序列化
 * 只包含：id,name
 */
public class CustomerSimpleAdapter extends TypeAdapter<Customer> {

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
        out.endObject();
    }

    private static CustomerSimpleAdapter adapter;
    public CustomerSimpleAdapter() {}
    public static CustomerSimpleAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerSimpleAdapter();
        }
        return adapter;
    }
}
