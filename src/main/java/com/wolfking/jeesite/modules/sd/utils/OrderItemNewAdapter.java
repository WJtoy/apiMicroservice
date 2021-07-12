package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单子项Gson序列化
 * 专用于将数据库json字段与OrderItem实体之间的类型转换
 */
public class OrderItemNewAdapter extends TypeAdapter<OrderItem> {

    @Override
    public OrderItem read(final JsonReader in) throws IOException {
        final OrderItem item = new OrderItem();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    item.setId(in.nextLong());
                    break;
                case "orderId":
                    item.setOrderId(in.nextLong());
                    break;
                case "itemNo":
                    item.setItemNo(in.nextInt());
                    break;
                case "productId":
                    item.setProduct(new Product(in.nextLong()));
                    break;
                case "brand":
                    item.setBrand(in.nextString());
                    break;
                case "productSpec":
                    item.setProductSpec(in.nextString());
                    break;
                case "serviceTypeId":
                    item.setServiceType(new ServiceType(in.nextLong()));
                    break;
                case "standPrice":
                    item.setStandPrice(in.nextDouble());
                    break;
                case "discountPrice":
                    item.setDiscountPrice(in.nextDouble());
                    break;
                case "qty":
                    item.setQty(in.nextInt());
                    break;
                case "charge":
                    item.setCharge(in.nextDouble());
                    break;
                case "blockedCharge":
                    item.setBlockedCharge(in.nextDouble());
                    break;
                case "expressCompany":
                    Dict expCompany = item.getExpressCompany();
                    if(expCompany == null){
                        expCompany = new Dict();
                    }
                    expCompany.setValue(in.nextString());
                    item.setExpressCompany(expCompany);
                    break;
                case "expressCompanyName":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                    }else {
                        Dict expCompany1 = item.getExpressCompany();
                        if (expCompany1 == null) {
                            expCompany1 = new Dict();
                        }
                        expCompany1.setLabel(in.nextString());
                        item.setExpressCompany(expCompany1);
                    }
                    break;
                case "expressNo":
                    item.setExpressNo(in.nextString());
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
                    break;
                case "quarter":
                    item.setQuarter(in.nextString());
                    break;
                case "b2bProductCode": /* orderItem增加B2B产品编码 */
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setB2bProductCode("");
                    }else {
                        item.setB2bProductCode(in.nextString());
                    }
                    break;
                case "productType":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setProductType(new NameValuePair<>(0L, StringUtils.EMPTY));
                    }else{
                        NameValuePair<Long,String> productType = new NameValuePair<Long,String>(0L,StringUtils.EMPTY);
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "name":
                                    productType.setName(StringUtils.toLong(in.nextString()));
                                    break;
                                case "value":
                                    productType.setValue(in.nextString());
                                    break;
                            }
                        }
                        item.setProductType(productType);
                        in.endObject();
                    }
                    break;
                case "productTypeItem":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setProductTypeItem(new NameValuePair<>(0L, StringUtils.EMPTY));
                    }else{
                        NameValuePair<Long,String> productTypeItem = new NameValuePair<Long,String>(0L,StringUtils.EMPTY);
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "name":
                                    productTypeItem.setName(StringUtils.toLong(in.nextString()));
                                    break;
                                case "value":
                                    productTypeItem.setValue(in.nextString());
                                    break;
                            }
                        }
                        item.setProductTypeItem(productTypeItem);
                        in.endObject();
                    }
                    break;
                case "pics":
                    /*if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setPics(Lists.newArrayListWithCapacity(0));
                    }else{
                        StringBuilder sbPic = new StringBuilder();
                        sbPic.append(in.nextString());
                        if(org.apache.commons.lang3.StringUtils.isBlank(sbPic.toString())){
                            item.setPics(Lists.newArrayListWithCapacity(0));
                            sbPic.setLength(0);
                        }else{
                            List<String> pics = Splitter.onPattern(",") //[~|-]
                                    .omitEmptyStrings()
                                    .trimResults()
                                    .splitToList(sbPic.toString());
                            item.setPics(pics);
                        }
                    }*/
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();
                        item.setPics(Lists.newArrayListWithCapacity(0));
                    }else{
                        in.beginArray();
                        List<String> pics = Lists.newArrayList();
                        while (in.hasNext()) {
                            pics.add(in.nextString());
                        }
                        item.setPics(pics);
                        in.endArray();
                    }
                    break;
            }
        }
        in.endObject();
        return item;
    }

    @Override
    public void write(final JsonWriter out, final OrderItem item) throws IOException {
        StringBuilder str = new StringBuilder();
        out.beginObject();
        Long id = item.getId() != null ? item.getId() : 0;
        out.name("id").value(id);
        Long orderId = item.getOrderId() != null ? item.getOrderId() : 0;
        out.name("orderId").value(orderId);
        out.name("itemNo").value(item.getItemNo());
        Long productId = item.getProduct() == null ? 0 : item.getProduct().getId() == null ? 0 : item.getProduct().getId();
        out.name("productId").value(productId);
        out.name("brand").value(item.getBrand());
        out.name("productSpec").value(item.getProductSpec());
        Long serviceTypeId = item.getServiceType() == null ? 0 : item.getServiceType().getId() == null ? 0 : item.getServiceType().getId();
        out.name("serviceTypeId").value(serviceTypeId);
        out.name("standPrice").value(item.getStandPrice());
        out.name("discountPrice").value(item.getDiscountPrice());
        out.name("qty").value(item.getQty());
        out.name("charge").value(item.getCharge());
        out.name("blockedCharge").value(item.getBlockedCharge());
        String expressCompanyStr = item.getExpressCompany() == null ? "" : item.getExpressCompany().getValue() == null ? "" : item.getExpressCompany().getValue();
        out.name("expressCompany").value(expressCompanyStr);
        String companyName = Optional.ofNullable(item.getExpressCompany()).map(t->t.getLabel()).orElse(StringUtils.EMPTY);
        out.name("expressCompanyName").value(companyName);
        out.name("expressNo").value(item.getExpressNo());
        out.name("remarks").value(StringUtils.isBlank(item.getRemarks()) ? "" : item.getRemarks());
        out.name("quarter").value(item.getQuarter());
        out.name("b2bProductCode").value(StringUtils.toString(item.getB2bProductCode())); /* orderItem增加B2B产品编码 */
        if(item.getProductType() != null){
            str.setLength(0);
            str.append(String.valueOf(item.getProductType().getName()));
            if(!str.toString().equals("null")){
                out.name("productType")
                        .beginObject()
                        .name("name").value(str.toString())
                        .name("value").value(item.getProductType().getValue())
                        .endObject();
            }
        }
        if(item.getProductTypeItem() != null){
            str.setLength(0);
            str.append(String.valueOf(item.getProductTypeItem().getName()));
            if(!str.toString().equals("null")) {
                out.name("productTypeItem")
                        .beginObject()
                        .name("name").value(str.toString())
                        .name("value").value(item.getProductTypeItem().getValue())
                        .endObject();
            }
        }
        if(!CollectionUtils.isEmpty(item.getPics())){
            //out.name("pics").value(item.getPics().stream().collect(Collectors.joining(",")));
            out.name("pics")
                    .beginArray();
            for (final String pic : item.getPics()) {
                out.value(pic);
            }
            out.endArray();
        }
        out.endObject();
        str.setLength(0);
    }

    private static OrderItemNewAdapter adapter;
    public OrderItemNewAdapter() {}
    public static OrderItemNewAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderItemNewAdapter();
        }
        return adapter;
    }
}
