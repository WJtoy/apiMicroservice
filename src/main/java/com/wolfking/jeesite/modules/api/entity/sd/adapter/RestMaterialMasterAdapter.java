package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.modules.api.entity.sd.RestMaterialMaster;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;
import com.wolfking.jeesite.modules.sd.entity.OrderAttachment;

import java.io.IOException;
import java.util.List;

/**
 * 配件序列化(for 列表)
 */
public class RestMaterialMasterAdapter extends TypeAdapter<RestMaterialMaster> {

    @Override
    public RestMaterialMaster read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestMaterialMaster entity) throws IOException {
        out.beginObject();
        out.name("id").value(entity.getId())
                .name("quarter").value(entity.getQuarter())
                .name("orderId").value(entity.getOrderId())
                .name("orderDetailId").value(entity.getOrderdetailId())
                .name("materialType").value(entity.getMaterialType())
                .name("materialTypeValue").value(entity.getMaterialTypeValue())
                .name("applyType").value(entity.getApplytype())
                .name("applyTypeValue").value(entity.getApplytypeValue())
                .name("status").value(entity.getStatus())
                .name("statusName").value(entity.getStatusName())
                .name("expressCompany").value(entity.getExpresscompany())
                .name("expressNo").value(entity.getExpressno())
                .name("remarks").value(entity.getRemarks())
                .name("details").value(entity.getDetails())
                .name("totalPrice").value(entity.getTotalprice())
                .name("createDate").value(entity.getCreateDate())
                .name("returnFlag").value(entity.getReturnFlag())
                .name("recycleFlag").value(entity.getRecycleFlag());
        //items
        out.name("items").beginArray();
        for(MaterialItem item:entity.getItems()){
            RestMatieralItemAdapter.getInstance().write(out,item);
        }
        out.endArray();
        //photos
        out.name("photos").beginArray();
//        String host = Global.getConfig("userfiles.host")+"/";
        for(MaterialAttachment photo:entity.getPhotos()){
            RestMaterialPhotoAdapter.getInstance().write(out,photo);
        }
        out.endArray();

        out.endObject();
    }

    private static RestMaterialMasterAdapter adapter;

    public RestMaterialMasterAdapter() {}

    public static RestMaterialMasterAdapter getInstance() {
        if (adapter == null){
            adapter = new RestMaterialMasterAdapter();
        }
        return adapter;
    }

}
