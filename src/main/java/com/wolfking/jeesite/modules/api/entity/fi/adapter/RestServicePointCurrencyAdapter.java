package com.wolfking.jeesite.modules.api.entity.fi.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointCurrency;

import java.io.IOException;

public class RestServicePointCurrencyAdapter extends TypeAdapter<RestServicePointCurrency> {
    @Override
    public void write(JsonWriter jsonWriter, RestServicePointCurrency restServicePointCurrency) throws IOException {
        jsonWriter.beginObject();

        jsonWriter.name("actionType").value(restServicePointCurrency.getActionType());
        jsonWriter.name("actionTypeValue").value(restServicePointCurrency.getActionTypeValue());
        jsonWriter.name("beforeBalance").value(restServicePointCurrency.getBeforeBalance());
        jsonWriter.name("balance").value(restServicePointCurrency.getBalance());
        jsonWriter.name("amount").value(restServicePointCurrency.getAmount());
        jsonWriter.name("currencyNo").value(restServicePointCurrency.getCurrencyNo());
        jsonWriter.name("description").value(restServicePointCurrency.getDescription());
        jsonWriter.name("createDate").value(restServicePointCurrency.getCreateDate() == null ? null : restServicePointCurrency.getCreateDate().getTime());
        jsonWriter.name("month").value(restServicePointCurrency.getMonth())
                .name("amountIn").value(restServicePointCurrency.getAmountIn())
                .name("amountOut").value(restServicePointCurrency.getAmountOut());
        jsonWriter.endObject();
    }

    @Override
    public RestServicePointCurrency read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
