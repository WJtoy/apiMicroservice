package com.wolfking.jeesite.modules.md.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.md.entity.CustomerFinance;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.io.IOException;

/**
 * 客户服务价格自定义Gson序列化/序列化
 */
public class CustomerFinanceAdapter extends TypeAdapter<CustomerFinance> {

    @Override
    public CustomerFinance read(final JsonReader in) throws IOException {
        final CustomerFinance finance = new CustomerFinance();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "invoiceFlag":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        finance.setInvoiceFlag(0);
                    }else {
                        finance.setInvoiceFlag(in.nextInt());
                    }
                    break;
                case "paymentType":
                    in.beginObject();
                    Dict paymentType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                paymentType.setLabel(in.nextString());
                                break;
                            case "value":
                                paymentType.setValue(String.valueOf(in.nextInt()));
                                break;
                        }
                    }
                    in.endObject();
                    finance.setPaymentType(paymentType);
                    break;
            }
        }
        in.endObject();
        return finance;
    }

    @Override
    public void write(final JsonWriter out, final CustomerFinance finance) throws IOException {
        out.beginObject();
        out.name("invoiceFlag").value(finance.getInvoiceFlag());
        if(finance.getPaymentType() != null){
            out.name("paymentType")
                    .beginObject()
                    .name("label").value(finance.getPaymentType().getLabel())
                    .name("value").value(Integer.parseInt(finance.getPaymentType().getValue()))
                    .endObject();
        }
        out.endObject();
    }

    private static CustomerFinanceAdapter adapter;
    public CustomerFinanceAdapter() {}
    public static CustomerFinanceAdapter getInstance() {
        if (adapter == null){
            adapter = new CustomerFinanceAdapter();
        }
        return adapter;
    }
}
