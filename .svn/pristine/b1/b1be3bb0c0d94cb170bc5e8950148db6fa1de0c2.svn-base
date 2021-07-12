package com.wolfking.jeesite.modules.api.entity.md.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.md.RestServicePointFinance;

import java.io.IOException;

public class RestServicePointFinanceAdapter extends TypeAdapter<RestServicePointFinance> {
    @Override
    public void write(JsonWriter jsonWriter, RestServicePointFinance restServicePointFinance) throws IOException {
        jsonWriter.beginObject();

        jsonWriter.name("paymentType")
                .beginObject()
                .name("label").value(restServicePointFinance.getPaymentType() == null ? "" : restServicePointFinance.getPaymentType().getLabel())
                .name("value").value(restServicePointFinance.getPaymentType() == null ? "" : restServicePointFinance.getPaymentType().getValue())
                .endObject();
        jsonWriter.name("bank")
                .beginObject()
                .name("label").value(restServicePointFinance.getBank() == null ? "" : restServicePointFinance.getBank().getLabel())
                .name("value").value(restServicePointFinance.getBank() == null ? "" : restServicePointFinance.getBank().getValue())
                .endObject();
        jsonWriter.name("branch").value(restServicePointFinance.getBranch());
        jsonWriter.name("bankNo").value(restServicePointFinance.getBankNo());
        jsonWriter.name("bankOwner").value(restServicePointFinance.getBankOwner());
        jsonWriter.name("invoiceFlag").value(restServicePointFinance.getInvoiceFlag());
        jsonWriter.name("balance").value(restServicePointFinance.getBalance());
        jsonWriter.name("totalAmount").value(restServicePointFinance.getTotalAmount());
        jsonWriter.name("lastPayDate").value(restServicePointFinance.getLastPayDate() == null ? null : restServicePointFinance.getLastPayDate().getTime());
        jsonWriter.name("lastPayAmount").value(restServicePointFinance.getLastPayAmount());
        jsonWriter.endObject();
    }

    @Override
    public RestServicePointFinance read(JsonReader jsonReader) throws IOException {
        return null;
    }
}