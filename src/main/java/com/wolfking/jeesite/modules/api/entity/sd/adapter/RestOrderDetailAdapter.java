package com.wolfking.jeesite.modules.api.entity.sd.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;

import java.io.IOException;

/**
 * 订单上门服务项目Gson序列化实现
 * @author Ryan
 * @date 2019-12-28
 * 新增完工维修内容
 */
public class RestOrderDetailAdapter extends TypeAdapter<RestOrderDetail> {

    @Override
    public RestOrderDetail read(final JsonReader in) throws IOException {
        return null;
    }

    @Override
    public void write(final JsonWriter out, final RestOrderDetail entity) throws IOException {
        out.beginObject()
                .name("id").value(entity.getId())
                .name("serviceTimes").value(entity.getServiceTimes())
                .name("productId").value(entity.getProductId())
                .name("productName").value(entity.getProductName())
                .name("qty").value(entity.getQty())
                .name("unit").value(entity.getUnit())
                .name("serviceTypeName").value(entity.getServiceTypeName())
                .name("servicePointId").value(entity.getServicePointId() == null?"0":entity.getServicePointId().toString())
                .name("engineerId").value(entity.getEngineerId() == null?"0":entity.getEngineerId().toString())
                .name("engineerServiceCharge").value(entity.getEngineerServiceCharge())
                .name("engineerTravelCharge").value(entity.getEngineerTravelCharge())
                .name("travelNo").value(StringUtils.isBlank(entity.getTravelNo())?"":entity.getTravelNo())
                .name("engineerExpressCharge").value(entity.getEngineerExpressCharge())
                .name("engineerMaterialCharge").value(entity.getEngineerMaterialCharge())
                .name("engineerOtherCharge").value(entity.getEngineerOtherCharge())
                .name("engineerCharge").value(entity.getEngineerChage())
                .name("remarks").value(entity.getRemarks());
        //完工维修
        //服务类型(安装，维修，...)
        out.name("serviceCategoryId").value(entity.getServiceCategoryId())
                .name("serviceCategoryName").value(entity.getServiceCategoryName())
                .name("errorTypeName").value(entity.getErrorTypeName())
                .name("errorCodeName").value(entity.getErrorCodeName())
                .name("actionCodeName").value(entity.getActionCodeName())
                .name("otherActionRemark").value(entity.getOtherActionRemark())
                .name("hasRepaired").value(entity.getHasRepaired());

        out.endObject();
    }

    private static RestOrderDetailAdapter adapter;

    public RestOrderDetailAdapter() {}

    public static RestOrderDetailAdapter getInstance() {
        if (adapter == null){
            adapter = new RestOrderDetailAdapter();
        }
        return adapter;
    }

}
