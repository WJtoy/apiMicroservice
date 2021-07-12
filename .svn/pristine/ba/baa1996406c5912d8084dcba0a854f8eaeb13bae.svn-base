package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;

import java.io.IOException;

/**
 * 下单时客户内容 Gson序列化/序列化
 * 只包含：id,name,urgentFlag
 */
public class CreatOrderCustomerAdapter extends TypeAdapter<Customer> {

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
                case "urgentFlag":
                    customer.setUrgentFlag(in.nextInt());
                    break;
            }
        }

        in.endObject();

        return customer;
    }

    @Override
    public void write(final JsonWriter out, final Customer customer) throws IOException {
        out.beginObject()
            .name("id").value(customer.getId())
            .name("name").value(customer.getName())
            .name("urgentFlag").value(customer.getUrgentFlag())
            .endObject();
    }

    private static CreatOrderCustomerAdapter adapter;

    public CreatOrderCustomerAdapter() {}

    public static CreatOrderCustomerAdapter getInstance() {
        if (adapter == null){
            adapter = new CreatOrderCustomerAdapter();
        }
        return adapter;
    }
}
