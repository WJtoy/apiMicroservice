package com.wolfking.jeesite.modules.md.utils;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;

import java.io.IOException;
import java.util.List;

/**
 * 客户列表（简单）自定义Gson序列化/序列化
 */
public class CustomerSimpleListAdapter extends TypeAdapter<List<Customer>> {

    @Override
    public List<Customer> read(final JsonReader in) throws IOException {
        final List<Customer> list = Lists.newArrayList();
        in.beginArray();
        while (in.hasNext()) {
            list.add(CustomerSimpleAdapter.getInstance().read(in));//调用Customer的序列化类
        }
        in.endArray();
        return list;
    }

    @Override
    public void write(final JsonWriter out, final List<Customer> customers) throws IOException {
        out.beginArray();
        for (final Customer item : customers) {
            CustomerSimpleAdapter.getInstance().write(out, item);
        }
        out.endArray();
    }

    private static CustomerSimpleListAdapter adapter;
    public CustomerSimpleListAdapter() {}
    public static CustomerSimpleListAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerSimpleListAdapter();
        }
        return adapter;
    }
}
