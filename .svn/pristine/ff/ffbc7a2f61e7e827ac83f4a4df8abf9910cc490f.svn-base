package com.wolfking.jeesite.modules.sd.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wolfking.jeesite.modules.sd.entity.OrderGrade;

import java.io.IOException;

/**
 * 订单客评具体客评项 Gson序列化
 */
public class OrderGradeItemAdapter extends TypeAdapter<OrderGrade> {

    @Override
    public OrderGrade read(final JsonReader in) throws IOException {
        final OrderGrade item = new OrderGrade();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "gradeId":
                    item.setGradeId(in.nextLong());
                    break;
                case "gradeName":
                    item.setGradeName(in.nextString());
                    break;
                case "gradeItemId":
                    item.setGradeItemId(in.nextLong());
                    break;
                case "gradeItemName":
                    item.setGradeItemName(in.nextString());
                    break;
                case "sort":
                    item.setSort(in.nextInt());
                    break;
                case "point":
                    item.setPoint(in.nextInt());
                    break;
            }
        }
        in.endObject();
        return item;
    }

    @Override
    public void write(final JsonWriter out, final OrderGrade item) throws IOException {
        out.beginObject()
            .name("gradeId").value(item.getGradeId())
            .name("gradeName").value(item.getGradeName())
            .name("gradeItemId").value(item.getGradeItemId())
            .name("gradeItemName").value(item.getGradeItemName())
            .name("sort").value(item.getSort())
            .name("point").value(item.getPoint())
        .endObject();
    }

    private static OrderGradeItemAdapter adapter;
    public OrderGradeItemAdapter() {}
    public static OrderGradeItemAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderGradeItemAdapter();
        }
        return adapter;
    }
}
