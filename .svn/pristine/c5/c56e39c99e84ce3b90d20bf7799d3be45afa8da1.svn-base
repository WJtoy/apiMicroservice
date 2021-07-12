package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderAdditionalInfo;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import com.wolfking.jeesite.ms.tmall.md.adapter.B2bShopAdapter;

import java.io.IOException;
import java.util.List;

/**
 * 订单Redis自定义Gson序列化/序列化
 */
public class OrderRedisAdapter extends TypeAdapter<Order> {

    @Override
    public Order read(final JsonReader in) throws IOException {
        final Order order = new Order();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "dataSource":
                    if (in.peek() == JsonToken.NULL) {
                        order.setDataSource(new Dict("1", "快可立"));
                    } else {
                        order.setDataSource(DictSimpleAdapter.getInstance().read(in));
                    }
                    break;
                case "b2bOrderId":
                    if (in.peek() == JsonToken.NULL) {
                        order.setB2bOrderId(0L);
                    } else {
                        order.setB2bOrderId(in.nextLong());
                    }
                    break;
                case "workCardId":
                    if (in.peek() == JsonToken.NULL) {
                        order.setWorkCardId("");
                    } else {
                        order.setWorkCardId(in.nextString());
                    }
                    break;
                case "parentBizOrderId":
                    if (in.peek() == JsonToken.NULL) {
                        order.setParentBizOrderId("");
                    } else {
                        order.setParentBizOrderId(in.nextString());
                    }
                    break;
                case "b2bShop":
                    order.setB2bShop(B2bShopAdapter.getInstance().read(in));
                    break;
                case "orderChannel":
                    Dict channel = new Dict("1","线下单");
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                    }else {
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "label":
                                    channel.setLabel(in.nextString());
                                    break;
                                case "value":
                                    channel.setValue(String.valueOf(in.nextInt()));
                                    break;
                            }
                        }
                        in.endObject();
                    }
                    order.setOrderChannel(channel);
                    break;
                case "id":
                    order.setId(Long.valueOf(in.nextString()));
                    break;
                case "orderNo":
                    order.setOrderNo(in.nextString());
                    break;
                case "orderType":
                    in.beginObject();
                    Dict orderType = new Dict();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                orderType.setLabel(in.nextString());
                                break;
                            case "value":
                                orderType.setValue(String.valueOf(in.nextInt()));
                                break;
                        }
                    }
                    order.setOrderType(orderType);
                    in.endObject();
                    break;
                case "totalQty":
                    order.setTotalQty(in.nextInt());
                    break;
                case "verificationCode":
                    order.setVerificationCode(in.nextString());
                    break;
                case "quarter":
                    order.setQuarter(in.nextString());
                    break;
                case "confirmDoor":
                    order.setConfirmDoor(in.nextInt());
                    break;
                case "description":
                    order.setDescription(in.nextString());
                    break;
                case "repeateNo":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        order.setRepeateNo("");
                    } else {
                        order.setRepeateNo(in.nextString());
                    }
                    break;
                //Items
                case "items":
                    in.beginArray();
                    final List items = Lists.newArrayList();
                    while (in.hasNext()) {
                        items.add(OrderItemAdapter.getInstance().read(in));//调用OrderItem的序列化类
                    }
                    order.setItems(items);
                    in.endArray();
                    break;
                case "orderAdditionalInfo":
                    OrderAdditionalInfo orderAdditionalInfo = OrderAdditionalInfoAdapter.getInstance().read(in);
                    order.setOrderAdditionalInfo(orderAdditionalInfo);
                    break;
            }
        }

        in.endObject();
        return order;
    }

    @Override
    public void write(final JsonWriter out, final Order order) throws IOException {
        out.beginObject();
        out.name("dataSource");
        if (order.getDataSource() == null) {
            order.setDataSource(new Dict("1", "快可立"));
        }
        DictSimpleAdapter.getInstance().write(out, order.getDataSource());
        out.name("b2bOrderId").value(order.getB2bOrderId() == null ? 0 : order.getB2bOrderId());
        out.name("workCardId").value(order.getWorkCardId())
                .name("parentBizOrderId").value(order.getParentBizOrderId())
                .name("id").value(order.getId().toString())
                .name("orderNo").value(order.getOrderNo());
        if (order.getB2bShop() != null) {
            out.name("b2bShop");
            B2bShopAdapter.getInstance().write(out, order.getB2bShop());
        }
        if (order.getOrderType() != null) {
            out.name("orderType")
                    .beginObject()
                    .name("label").value(order.getOrderType().getLabel())
                    .name("value").value(Integer.parseInt(order.getOrderType().getValue()))
                    .endObject();
        }
        if (order.getOrderChannel() != null && order.getOrderChannel().getIntValue() > 0) {
            out.name("orderChannel")
                    .beginObject()
                    .name("label").value(StringUtils.trimToEmpty(order.getOrderChannel().getLabel()))
                    .name("value").value(Integer.parseInt(order.getOrderChannel().getValue()))
                    .endObject();
        }
        out.name("totalQty").value(order.getTotalQty())
                .name("verificationCode").value(order.getVerificationCode())
                .name("quarter").value(order.getQuarter())
                .name("confirmDoor").value(order.getConfirmDoor())
                .name("repeateNo").value(order.getRepeateNo())
                .name("description").value(order.getDescription().replace("\r\n", "<br>"));
        if (order.getItems() != null && order.getItems().size() > 0) {
            out.name("items").beginArray();
            for (final OrderItem item : order.getItems()) {
                OrderItemAdapter.getInstance().write(out, item);
            }
            out.endArray();
        }
        if (order.getOrderAdditionalInfo() != null) {
            out.name("orderAdditionalInfo");
            OrderAdditionalInfoAdapter.getInstance().write(out, order.getOrderAdditionalInfo());
        }
        out.endObject();
    }

    private static OrderRedisAdapter adapter;
    public OrderRedisAdapter() {}
    public static OrderRedisAdapter getInstance() {
        if (adapter == null) {
            adapter = new OrderRedisAdapter();
        }
        return adapter;
    }

}
