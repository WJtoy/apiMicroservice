package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderItemAdapter;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.Office;
import com.wolfking.jeesite.modules.sys.entity.adapter.OfficeSimpleAdapter;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单Gson序列化
 */
public class OrderItemAdapter extends TypeAdapter<OrderItem> {

    @Override
    public OrderItem read(final JsonReader in) throws IOException {
        final OrderItem item = new OrderItem();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    if(in.peek() == JsonToken.NULL){
                        in.nextNull();//must
                        item.setId(0l);
                    }else {
                        item.setId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "orderId":
                    if(in.peek() == JsonToken.NULL){
                          in.nextNull();//must
                        item.setOrderId(0l);
                    }else {
                        item.setOrderId(Long.valueOf(in.nextString()));
                    }
                    break;
                case "delFlag":
                    item.setDelFlag(in.nextInt());
                    break;
                case "itemNo":
                    item.setItemNo(in.nextInt());
                    break;
                case "product":
                    Product product = new Product();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                product.setId(in.nextLong());
                                break;
                            case "name":
                                product.setName(in.nextString());
                                break;
                            case "setFlag":
                                product.setSetFlag(in.nextInt());
                                break;
                            case "category":
                                ProductCategory category = new ProductCategory();
                                in.beginObject();
                                while (in.hasNext()) {
                                    switch (in.nextName()) {
                                        case "id":
                                            category.setId(in.nextLong());
                                            break;
                                        case "name":
                                            category.setName(in.nextString());
                                            break;
                                    }
                                }
                                in.endObject();
                                product.setCategory(category);
                                break;
                        }
                    }
                    item.setProduct(product);
                    in.endObject();
                    break;
                case "brand":
                    item.setBrand(in.nextString());
                    break;
                case "productSpec":
                    item.setProductSpec(in.nextString());
                    break;
                case "serviceType":
                    ServiceType serviceType = new ServiceType();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "id":
                                serviceType.setId(in.nextLong());
                                break;
                            case "name":
                                serviceType.setName(in.nextString());
                                break;
                        }
                    }
                    item.setServiceType(serviceType);
                    in.endObject();
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
                    Dict expCompany = new Dict();
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "label":
                                expCompany.setLabel(in.nextString());
                                break;
                            case "value":
                                expCompany.setValue(in.nextString());
                                break;
                        }
                    }
                    item.setExpressCompany(expCompany);
                    in.endObject();
                    break;
                case "expressNo":
                    item.setExpressNo(in.nextString());
                    break;
                case "remarks":
                    item.setRemarks(in.nextString());
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
                                    if(in.peek() == JsonToken.NULL){
                                        in.nextNull();
                                    }else {
                                        productType.setName(StringUtils.toLong(in.nextString()));
                                    }
                                    break;
                                case "value":
                                    if(in.peek() == JsonToken.NULL){
                                        in.nextNull();
                                    }else {
                                        productType.setValue(in.nextString());
                                    }
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
                                    if(in.peek() == JsonToken.NULL){
                                        in.nextNull();
                                    }else {
                                        productTypeItem.setName(StringUtils.toLong(in.nextString()));
                                    }
                                    break;
                                case "value":
                                    if(in.peek() == JsonToken.NULL){
                                        in.nextNull();
                                    }else {
                                        productTypeItem.setValue(in.nextString());
                                    }
                                    break;
                            }
                        }
                        item.setProductTypeItem(productTypeItem);
                        in.endObject();
                    }
                    break;
                case "pics":
                    /*
                    if(in.peek() == JsonToken.NULL){
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
        out.beginObject();
        out.name("id").value(item.getId()==null?null:item.getId().toString());
        out.name("orderId").value(item.getOrderId()==null?null:item.getOrderId().toString());
        out.name("itemNo").value(item.getItemNo());
        if(item.getProduct() != null) {
            out.name("product")
                    .beginObject()
                    .name("id").value(item.getProduct().getId())
                    .name("name").value(item.getProduct().getName())
                    .name("setFlag").value(item.getProduct().getSetFlag());
                    if(item.getProduct().getCategory()!=null){
                       out.name("category")
                               .beginObject()
                               .name("id").value(item.getProduct().getCategory().getId())
                               .name("name").value(item.getProduct().getCategory().getName())
                               .endObject();
                    }
                    out.endObject();
        }
        out.name("brand").value(item.getBrand());
        out.name("productSpec").value(item.getProductSpec());
        if(item.getServiceType() != null) {
            out.name("serviceType")
                    .beginObject()
                    .name("id").value(item.getServiceType().getId())
                    .name("name").value(item.getServiceType().getName())
                    .endObject();
        }
        out.name("standPrice").value(item.getStandPrice());
        out.name("discountPrice").value(item.getDiscountPrice());
        out.name("qty").value(item.getQty());
        out.name("charge").value(item.getCharge());
        out.name("blockedCharge").value(item.getBlockedCharge());
        if(item.getExpressCompany() != null) {
            out.name("expressCompany")
                    .beginObject()
                    .name("label").value(item.getExpressCompany().getLabel())
                    .name("value").value(item.getExpressCompany().getValue())
                    .endObject();
        }
        out.name("expressNo").value(item.getExpressNo());
        out.name("remarks").value(StringUtils.isBlank(item.getRemarks())?"":item.getRemarks());
        out.name("b2bProductCode").value(StringUtils.toString(item.getB2bProductCode())); /* orderItem增加B2B产品编码 */
        if(item.getProductType() != null && item.getProductType().getName() != null){
            out.name("productType")
                    .beginObject()
                    .name("name").value(item.getProductType().getName())
                    .name("value").value(item.getProductType().getValue())
                    .endObject();
        }
        if(item.getProductTypeItem() != null && item.getProductTypeItem().getName() != null){
            out.name("productTypeItem")
                    .beginObject()
                    .name("name").value(item.getProductTypeItem().getName())
                    .name("value").value(item.getProductTypeItem().getValue())
                    .endObject();
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
    }

    private static OrderItemAdapter adapter;
    public OrderItemAdapter() {}
    public static OrderItemAdapter getInstance() {
        if (adapter == null){
            adapter = new OrderItemAdapter();
        }
        return adapter;
    }
}
