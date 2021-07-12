package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetailInfoNew;



import java.io.IOException;

/**
 * @Auther wj
 * @Date 2020/12/1 17:09
 */


public class RestOrderDetailInfoNewAdapter extends TypeAdapter<RestOrderDetailInfoNew> {
    @Override
    public RestOrderDetailInfoNew read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderDetailInfoNew order) throws IOException {
        out.beginObject();

        //items
      /*  out.name("items").beginArray();
        for (final RestOrderItem item : order.getItems()) {
            RestOrderItemAdapter.getInstance().write(out, item);
        }
        out.endArray();*/

        //services
        out.name("services").beginArray();
        for (final RestOrderDetail item : order.getServices()) {
            RestOrderDetailAdapter.getInstance().write(out, item);
        }
        out.endArray();

        //fee
        out.name("engineerServiceCharge").value(order.getEngineerServiceCharge());
        out.name("engineerTravelCharge").value(order.getEngineerTravelCharge());
        out.name("engineerExpressCharge").value(order.getEngineerExpressCharge());
        out.name("engineerMaterialCharge").value(order.getEngineerMaterialCharge());
        out.name("engineerOtherCharge").value(order.getEngineerOtherCharge());
        out.name("engineerCharge").value(order.getEngineerCharge());
        //网点预估服务费 18/01/24
        out.name("estimatedServiceCost").value(order.getEstimatedServiceCost());
        out.name("isComplained").value(order.getIsComplained());//18/01/24
        out.name("hasAuxiliaryMaterials").value(order.getHasAuxiliaryMaterials());
        out.name("auxiliaryMaterialsTotalCharge").value(order.getAuxiliaryMaterialsTotalCharge());
        out.name("auxiliaryMaterialsActualTotalCharge").value(order.getAuxiliaryMaterialsActualTotalCharge());
        out.endObject();
    }


    private static RestOrderDetailInfoNewAdapter adapter;

    public RestOrderDetailInfoNewAdapter() {
    }



    public static RestOrderDetailInfoNewAdapter getInstance() {
        if (adapter == null) {
            adapter = new RestOrderDetailInfoNewAdapter();
        }
        return adapter;
    }

}
