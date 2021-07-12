package com.wolfking.jeesite.modules.sd.utils;

import cn.hutool.core.util.StrUtil;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderAdditionalInfo;

import java.io.IOException;

/**
 * 专用于将数据库json字段与OrderAdditionalInfo实体之间的类型转换
 */
public class OrderAdditionalInfoAdapter extends TypeAdapter<OrderAdditionalInfo> {

    @Override
    public OrderAdditionalInfo read(final JsonReader in) throws IOException {
        final OrderAdditionalInfo info = new OrderAdditionalInfo();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "estimatedReceiveDt":
                    in.nextLong();// 有部分工单有该属性，必须跳过否则报错
                    break;
                case "estimatedReceiveDate":
                    info.setEstimatedReceiveDate(in.nextString());
                    break;
                case "buyDate":
                    info.setBuyDate(in.nextLong());
                    break;
                case "expectServiceTime":
                    info.setExpectServiceTime(in.nextString());
                    break;
                case "siteCode":
                    info.setSiteCode(in.nextString());
                    break;
                case "siteName":
                    info.setSiteName(in.nextString());
                    break;
                case "engineerName":
                    info.setEngineerName(in.nextString());
                    break;
                case "engineerMobile":
                    info.setEngineerMobile(in.nextString());
                    break;
                case "orderDataSource":
                    info.setOrderDataSource(in.nextString());
                    break;
            }
        }
        in.endObject();
        return info;
    }


    @Override
    public void write(final JsonWriter out, final OrderAdditionalInfo item) throws IOException {
        out.beginObject();
        String estimatedArriveDate = StringUtils.toString(item.getEstimatedReceiveDate());
        out.name("estimatedReceiveDate").value(estimatedArriveDate);
        Long buyDate = item.getBuyDate() == null ? 0 : item.getBuyDate();
        out.name("buyDate").value(buyDate);
        String expectServiceTime = StringUtils.toString(item.getExpectServiceTime());
        out.name("expectServiceTime").value(expectServiceTime);
        out.name("siteCode").value(StrUtil.trimToEmpty(item.getSiteCode()))
                .name("siteName").value(StrUtil.trimToEmpty(item.getSiteName()))
                .name("engineerName").value(StrUtil.trimToEmpty(item.getEngineerName()))
                .name("engineerMobile").value(StrUtil.trimToEmpty(item.getEngineerMobile()));
        out.name("orderDataSource").value(StrUtil.trimToEmpty(item.getOrderDataSource()));
        out.endObject();
    }

    private static OrderAdditionalInfoAdapter adapter = new OrderAdditionalInfoAdapter();

    private OrderAdditionalInfoAdapter() {
    }

    public static OrderAdditionalInfoAdapter getInstance() {
        return adapter;
    }
}
