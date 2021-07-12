package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MDServicePointPriceVMAdapter extends TypeAdapter<MDServicePointPriceVM> {
    @Override
    public void write(JsonWriter out, MDServicePointPriceVM value) throws IOException {

    }

    @Override
    public MDServicePointPriceVM read(JsonReader in) throws IOException {
        MDServicePointPriceVM.MDServicePointPriceVMBuilder builder = new MDServicePointPriceVM.MDServicePointPriceVMBuilder();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    builder.id(in.nextLong());
                    break;
                case "servicePointId":
                    in.nextLong();
                    break;
                case "productId":
                    builder.productId(in.nextLong());
                    break;
                case "serviceTypeId":
                    builder.serviceTypeId(in.nextLong());
                    break;
                case "price":
                    builder.price(in.nextDouble());
                    break;
                case "discountPrice":
                    builder.discountPrice(in.nextDouble());
                    break;
                case "unit":
                    in.nextString();
                    break;
                case "priceType":
                    in.nextInt();
                    break;
                case "remarks":
                    in.nextString();
                    break;
                case "delFlag":
                    builder.delFlag(in.nextInt());
                    break;
            }
        }
        in.endObject();
        return builder.build();
    }
}
