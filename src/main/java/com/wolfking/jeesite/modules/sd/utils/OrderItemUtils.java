package com.wolfking.jeesite.modules.sd.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.InvalidProtocolBufferException;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.CacheDataTypeEnum;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.md.utils.ProductUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.entity.dto.OrderPbDto;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderItemUtils {

    private static ServiceTypeService serviceTypeService = SpringContextHolder.getBean(ServiceTypeService.class);

    /**
     * OrderItem列表转成json字符串
     *
     * @param orderItems
     * @return
     */
    public static String toOrderItemsJson(List<OrderItem> orderItems) {
        String json = null;
        if (orderItems != null && orderItems.size() > 0) {
            Gson gson = new GsonBuilder().registerTypeAdapter(OrderItem.class, OrderItemNewAdapter.getInstance()).create();
            json = gson.toJson(orderItems, new TypeToken<List<OrderItem>>() {
            }.getType());
            /**
             *  因为myCat1.6不支持在json或text类型的字段中存储英文括号，故将所有的英文括号替换成中文括号.
             */
            json = json.replace("(", "（");
            json = json.replace(")", "）");
        }
        return json;
    }

    /**
     * json字符串转成OrderItem列表
     *
     * @param json
     * @return

    public static List<OrderItem> fromOrderItemsJson(String json) {
        List<OrderItem> orderItems = null;
        if (StringUtils.isNotEmpty(json)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(OrderItem.class, OrderItemNewAdapter.getInstance()).create();
            orderItems = gson.fromJson(json, new TypeToken<List<OrderItem>>() {
            }.getType());
        }
        return orderItems != null ? orderItems : Collections.EMPTY_LIST;
    }*/

    /**
     * pb二进制转OrderItem列表
     *
     * @param bytes
     * @return
     */
    public static List<OrderItem> pbToItems(byte[] bytes) {
        if(bytes == null || bytes.length==0){
            return Lists.newArrayList();
        }
        OrderPbDto.OrderItemList itemList = null;
        try {
            itemList = OrderPbDto.OrderItemList.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            return Lists.newArrayList();
        }
        if (itemList == null) {
            return Lists.newArrayList();
        }
        List<OrderItem> items = Lists.newArrayListWithCapacity(itemList.getItemCount());
        List<OrderPbDto.OrderItem> list = itemList.getItemList();
        OrderItem model;
        for (OrderPbDto.OrderItem item : list) {
            model = new OrderItem();
            model.setItemNo(item.getItemNo());
            model.setProduct(new Product(item.getProductId()));
            model.setBrand(StrUtil.trimToEmpty(item.getBrand()));
            model.setProductSpec(StrUtil.trimToEmpty(item.getProductSpec()));
            model.setB2bProductCode(StrUtil.trimToEmpty(item.getB2BProductCode()));
            model.setQty(item.getQty());
            model.setServiceType(new ServiceType(item.getServiceTypeId()));
            model.setStandPrice(item.getStandPrice());
            model.setDiscountPrice(item.getDiscountPrice());
            model.setBlockedCharge(item.getBlockedCharge());
            model.setCharge(item.getCharge());
            model.setExpressCompany(new Dict(item.getExpressCompany()));
            model.setExpressNo(item.getExpressNo());
            model.setRemarks(item.getRemarks());
            model.setPics(item.getPicsList());
            Long lName;
            if(item.getProductType() != null && StrUtil.isNotBlank(item.getProductType().getName())){
                lName = StringUtils.toLong(item.getProductType().getName());
                NameValuePair<Long,String> productType = new NameValuePair<Long,String>(lName,item.getProductType().getValue());
                model.setProductType(productType);
            }
            if(item.getProductTypeItem() != null && StrUtil.isNotBlank(item.getProductTypeItem().getName())){
                lName = StringUtils.toLong(item.getProductTypeItem().getName());
                NameValuePair<Long,String> productTypeItem = new NameValuePair<Long,String>(lName,item.getProductTypeItem().getValue());
                model.setProductTypeItem(productTypeItem);
            }
            items.add(model);
        }
        return items;
    }

    /**
     * 设置列表中orderitem的某些属性，如服务类型、产品
     *
     * @param orderItems     OrderItem列表
     * @param cacheDataTypes CacheDataTypeEnum的集合
     * @return 设置好属性的OrderItem列表
     */
    public static List<OrderItem> setOrderItemProperties(List<OrderItem> orderItems, Set<CacheDataTypeEnum> cacheDataTypes) {
        if (orderItems != null && orderItems.size() > 0 && cacheDataTypes != null && cacheDataTypes.size() > 0) {
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            Map<Long, Product> productMap = ProductUtils.getAllProductMap();
            Map<String, Dict> expressCompanyMap = MSDictUtils.getDictMap("express_type");
            ServiceType serviceType = null;
            Product product = null;
            Dict expressCompany = null;
            for (OrderItem item : orderItems) {
                if (CacheDataTypeEnum.isExists(cacheDataTypes, CacheDataTypeEnum.SERVICETYPE)) {
                    serviceType = serviceTypeMap.get(item.getServiceType().getId());
                    item.setServiceType(serviceType);
                }
                if (CacheDataTypeEnum.isExists(cacheDataTypes, CacheDataTypeEnum.PRODUCT)) {
                    product = productMap.get(item.getProductId());
                    item.setProduct(product);
                }
                if (CacheDataTypeEnum.isExists(cacheDataTypes, CacheDataTypeEnum.EXPPRESSCOMPANY) &&
                        item.getExpressCompany() != null && StringUtils.isNotEmpty(item.getExpressCompany().getValue())) {
                    expressCompany = expressCompanyMap.get(item.getExpressCompany().getValue());
                    item.setExpressCompany(expressCompany);
                }
            }
        }
        return orderItems;
    }

    /**
     * 订单项目列表转Pb对象
     * @param items
     * @return
     */
    public static OrderPbDto.OrderItemList ItemsToPb(List<OrderItem> items){
        if(CollectionUtils.isEmpty(items)){
            return null;
        }
        OrderPbDto.OrderItemList.Builder builder = OrderPbDto.OrderItemList.newBuilder();
        OrderPbDto.OrderItem.Builder headBuilder;
        for(OrderItem item:items) {
            headBuilder = OrderPbDto.OrderItem.newBuilder()
                    .setItemNo(item.getItemNo())
                    .setProductId(item.getProductId())
                    .setB2BProductCode(item.getB2bProductCode())
                    .setBrand(item.getBrand())
                    .setProductSpec(item.getProductSpec())
                    .setQty(item.getQty())
                    .setServiceTypeId(item.getServiceType().getId())
                    .setStandPrice(item.getStandPrice())
                    .setDiscountPrice(item.getDiscountPrice())
                    .setCharge(item.getCharge())
                    .setBlockedCharge(item.getBlockedCharge())
                    .setExpressCompany(item.getExpressCompany().getValue())
                    .setExpressNo(item.getExpressNo())
                    .setRemarks(item.getRemarks());
            NameValuePair<Long,String> nameValuePair;
            if(item.getProductType() != null && item.getProductType().getName() != null){
                nameValuePair = item.getProductType();
                headBuilder.setProductType(OrderPbDto.NameValue.newBuilder()
                        .setName(String.valueOf(nameValuePair.getName()))
                        .setValue(nameValuePair.getValue())
                        .build());
            }
            if(item.getProductTypeItem() != null && item.getProductTypeItem().getName() != null){
                nameValuePair = item.getProductTypeItem();
                headBuilder.setProductTypeItem(OrderPbDto.NameValue.newBuilder()
                        .setName(String.valueOf(nameValuePair.getName()))
                        .setValue(nameValuePair.getValue())
                        .build());
            }
            if(!CollectionUtils.isEmpty(item.getPics())){
                headBuilder.addAllPics(item.getPics());
            }
            builder.addItem(headBuilder.build());
        }
        return builder.build();
    }

    /**
     * 订单项目列表转Pb对象
     * @param items
     * @return
     */
    public static byte[] ItemsToPbBytes(List<OrderItem> items){
        OrderPbDto.OrderItemList itemList = ItemsToPb(items);
        if(itemList == null){
            return null;
        }
        return itemList.toByteArray();
    }

}
