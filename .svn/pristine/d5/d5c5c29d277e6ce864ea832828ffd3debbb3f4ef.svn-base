package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;

import java.io.IOException;

/**
 * 客户简单内容 Gson序列化/序列化
 * 只包含：id,name
 */
public class CustomerAccountProfileAdapter extends TypeAdapter<CustomerAccountProfile> {

    @Override
    public CustomerAccountProfile read(final JsonReader in) throws IOException {
        final CustomerAccountProfile profile = new CustomerAccountProfile();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    profile.setId(in.nextLong());
                    break;
                case "customer":
                    profile.setCustomer(CustomerSalesAdapter.getInstance().read(in));
                    break;
                case "orderApproveFlag":
                    profile.setOrderApproveFlag(in.nextInt());
                    break;

            }
        }

        in.endObject();

        return profile;
    }

    @Override
    public void write(final JsonWriter out, final CustomerAccountProfile profile) throws IOException {
        out.beginObject();
        out.name("id").value(profile.getId());
        if(profile.getCustomer() != null) {
            out.name("customer");
            CustomerSalesAdapter.getInstance().write(out,profile.getCustomer());
        }
        out.name("orderApproveFlag").value(profile.getOrderApproveFlag());
        out.endObject();
    }

    private static CustomerAccountProfileAdapter adapter;
    public CustomerAccountProfileAdapter() {}
    public static CustomerAccountProfileAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerAccountProfileAdapter();
        }
        return adapter;
    }
}
