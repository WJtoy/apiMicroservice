package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sd.entity.AuxiliaryMaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import com.wolfking.jeesite.modules.sd.entity.ThreeTuple;
import com.wolfking.jeesite.modules.sd.service.OrderAuxiliaryMaterialService;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class B2BOrderManualBaseService extends B2BOrderAutoBaseService {

    @Autowired
    OrderCacheReadService orderCacheReadService;
    @Autowired
    OrderItemCompleteService orderItemCompleteService;

    //region 获取完工图片与条码

    public Map<Long, List<String>> getProductIdToB2BProductCodeMapping(List<OrderItem> orderItems) {
        Map<Long, List<String>> result = Maps.newHashMap();
        if (orderItems != null && !orderItems.isEmpty()) {
            List<Long> pIds = orderItems.stream().filter(i -> i.getProduct() != null && i.getProduct().getId() != null).map(OrderItem::getProductId).distinct().collect(Collectors.toList());
            Map<Long, Product> productMap = productService.getProductMap(pIds);
            for (OrderItem item : orderItems) {
                Product product = productMap.get(item.getProductId());
                if (product != null && product.getId() != null) {
                    ThreeTuple<Set<Long>, String, Integer> tuple = new ThreeTuple<>();
                    tuple.setBElement(StringUtils.toString(item.getB2bProductCode()));
                    tuple.setCElement(item.getQty());
                    if (product.getSetFlag() == 1) {
                        String[] setIds = product.getProductIds().split(",");
                        for (String id : setIds) {
                            Long productId = StringUtils.toLong(id);
                            if (productId > 0) {
                                if (result.containsKey(productId)) {
                                    if (StringUtils.isNotBlank(item.getB2bProductCode()) && !result.get(productId).contains(item.getB2bProductCode())) {
                                        result.get(productId).add(item.getB2bProductCode());
                                    }
                                } else {
                                    result.put(productId, Lists.newArrayList(item.getB2bProductCode()));
                                }
                            }
                        }

                    } else {
                        if (result.containsKey(item.getProductId())) {
                            if (StringUtils.isNotBlank(item.getB2bProductCode()) && !result.get(item.getProductId()).contains(item.getB2bProductCode())) {
                                result.get(item.getProductId()).add(item.getB2bProductCode());
                            }
                        } else {
                            result.put(item.getProductId(), Lists.newArrayList(item.getB2bProductCode()));
                        }
                    }
                }
            }
        }
        return result;
    }

    @Autowired
    private OrderAuxiliaryMaterialService orderAuxiliaryMaterialService;

    protected double getActualTotalSurcharge(Long orderId, String quarter) {
        double actualTotalSurcharge = 0;
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            AuxiliaryMaterialMaster master = orderAuxiliaryMaterialService.getAuxiliaryMaterialMaster(orderId, quarter);
            if (master != null && master.getActualTotalCharge() != null) {
                actualTotalSurcharge = master.getActualTotalCharge();
            }
        }
        return actualTotalSurcharge;
    }


    protected List<CanboOrderCompleted.CompletedItem> getOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
        List<CanboOrderCompleted.CompletedItem> result = Lists.newArrayList();
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            CanboOrderCompleted.CompletedItem completedItem;
            ProductCompletePicItem picItem;
            if (completeList != null && !completeList.isEmpty()) {
                Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);

                List<String> b2bProductCodeList;
                for (OrderItemComplete item : completeList) {
                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                    Map<String, ProductCompletePicItem> picItemMap = Maps.newHashMap();
                    for (ProductCompletePicItem innerItem : item.getItemList()) {
                        picItemMap.put(innerItem.getPictureCode(), innerItem);
                    }
                    completedItem = new CanboOrderCompleted.CompletedItem();

                    //获取B2B产品编码
                    b2bProductCodeList = b2bProductCodeMap.get(item.getProduct().getId());
                    if (b2bProductCodeList != null && !b2bProductCodeList.isEmpty()) {
                        completedItem.setItemCode(b2bProductCodeList.get(0));
                        if (b2bProductCodeList.size() > 1) {
                            b2bProductCodeList.remove(0);
                        }
                    } else {
                        completedItem.setItemCode("");
                    }

                    completedItem.setBarcode(StringUtils.toString(item.getUnitBarcode()));
                    completedItem.setOutBarcode(StringUtils.toString(item.getOutBarcode()));
                    //条码图片
                    picItem = picItemMap.get("pic4");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic4(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    //现场图片
                    picItem = picItemMap.get("pic1");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic1(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    picItem = picItemMap.get("pic2");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic2(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    picItem = picItemMap.get("pic3");
                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                        completedItem.setPic3(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                    result.add(completedItem);
                }
            }
        }
        return result;
    }

    //endregion
}
