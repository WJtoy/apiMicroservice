package com.wolfking.jeesite.ms.tmall.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kkl.kklplus.entity.b2b.common.FourTuple;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderCompletedItem;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderAuxiliaryMaterialService;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.service.OrderItemService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class TmallOrderService extends B2BOrderManualBaseService {
    @Resource
    private OrderItemService orderItemService;
    @Autowired
    private OrderAuxiliaryMaterialService orderAuxiliaryMaterialService;

    @Autowired
    private OrderItemCompleteService orderItemCompleteService;

    @Autowired
    private ServicePointService servicePointService;

    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 创建天猫派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
        builder.setEngineerName(StringUtils.trimToEmpty(engineerName))
                .setEngineerMobile(StringUtils.trimToEmpty(engineerMobile));
        result.setAElement(true);
        result.setBElement(builder);
        return result;
    }

    /**
     * 创建天猫上门请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServiceRequestEntity() {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
        result.setAElement(true);
        result.setBElement(builder);
        return result;
    }

    /**
     * 创建天猫预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createTmallAppointRequestEntity(Date appointmentDate, User updater, Long servicePointId, Long engineerId) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate)
                    .setUpdaterName(updater.getName());
            Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
            if (engineer != null) {
                builder.setEngineerName(StringUtils.trimToEmpty(engineer.getName()));
                builder.setEngineerMobile(StringUtils.trimToEmpty(engineer.getContactInfo()));
            }
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createTmallCompleteRequestEntityNew(Long orderId, String quarter, List<OrderItem> orderItems, Date completeDate, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && completeDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemService.getOrderItems(quarter, orderId);//2020-12-20 sd_order -> sd_order_head
                if (order != null) {
                    orderItems = order.getItems();
                }
            }
            List<B2BOrderCompletedItem> completedItems = getB2BOrderCompletedItems(B2BDataSourceEnum.TMALL.id, orderId, quarter, orderItems);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(completeDate)
                    .setUpdaterName(updater.getName())
                    .setOrderCompletedItems(completedItems);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建天猫取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createTmallCancelRequestEntity(User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setUpdaterName(updater.getName());
            builder.setRemarks(StringUtils.trimToEmpty(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    /**
     * @return FourTuple:产品ID、B2B产品编码、产品数量、套组中包含的具体产品ID集合
     */
    private List<FourTuple<Long, String, Integer, Set<Long>>> getB2BProductCodeMappings(List<OrderItem> orderItems) {
        List<FourTuple<Long, String, Integer, Set<Long>>> result = Lists.newArrayList();
        if (orderItems != null && !orderItems.isEmpty()) {
            List<Long> tempIds = orderItems.stream().map(OrderItem::getProductId).distinct().collect(Collectors.toList());
            Map<Long, Product> productMap = productService.getProductMap(tempIds);
            Product product;
            Long productId;
            Set<Long> pIdSet;
            FourTuple<Long, String, Integer, Set<Long>> tuple;
            for (OrderItem item : orderItems) {
                product = productMap.get(item.getProductId());
                if (product != null && product.getId() != null) {
                    pIdSet = Sets.newHashSet();
                    tuple = new FourTuple<>();
                    tuple.setAElement(item.getProductId());
                    tuple.setBElement(StringUtils.toString(item.getB2bProductCode()));
                    tuple.setCElement(item.getQty());
                    tuple.setDElement(pIdSet);

                    if (product.getSetFlag() == 1) {
                        String[] setIds = product.getProductIds().split(",");
                        for (String id : setIds) {
                            productId = StringUtils.toLong(id);
                            if (productId > 0) {
                                pIdSet.add(productId);
                            }
                        }
                    }
                    result.add(tuple);
                }
            }
        }
        return result;
    }

    public List<B2BOrderCompletedItem> getB2BOrderCompletedItems(Integer dataSourceId, Long orderId, String quarter, List<OrderItem> orderItems) {
        List<B2BOrderCompletedItem> result = Lists.newArrayList();
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
            List<OrderItem> itemList = orderItems.stream().filter(i -> i.getProduct() != null && i.getProduct().getId() != null && i.getQty() != null).collect(Collectors.toList());
            if (!itemList.isEmpty()) {
                List<FourTuple<Long, String, Integer, Set<Long>>> b2BProductCodeMappings = getB2BProductCodeMappings(orderItems);
                List<OrderItemComplete> completedPicItems = orderItemCompleteService.getByOrderId(orderId, quarter);
                Map<Long, List<OrderItemComplete>> completedPicMap = Maps.newHashMap();
                Long productId;
                for (OrderItemComplete item : completedPicItems) {
                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                    productId = item.getProduct().getId();
                    if (completedPicMap.containsKey(productId)) {
                        completedPicMap.get(productId).add(item);
                    } else {
                        completedPicMap.put(productId, Lists.newArrayList(item));
                    }
                }

                List<AuxiliaryMaterial> auxiliaryMaterials = orderAuxiliaryMaterialService.getOrderAuxiliaryMaterialList(orderId, quarter);
                Map<Long, B2BSurchargeCategoryMapping> surchargeCategoryMappingMap = B2BMDUtils.getB2BSurchargeCategoryMap(dataSourceId);
                Map<Long, B2BSurchargeItemMapping> surchargeItemMappingMap = B2BMDUtils.getB2BSurchargeItemMap(dataSourceId);
                Map<Long, List<B2BOrderCompletedItem.B2BSurchargeItem>> auxiliaryMaterialMap = Maps.newHashMap();
                for (AuxiliaryMaterial item : auxiliaryMaterials) {
                    productId = item.getProduct().getId();
                    B2BOrderCompletedItem.B2BSurchargeItem surchargeItem = new B2BOrderCompletedItem.B2BSurchargeItem();
                    surchargeItem.setCategoryId(item.getCategory().getId());
                    B2BSurchargeCategoryMapping categoryMapping = surchargeCategoryMappingMap.get(item.getCategory().getId());
                    if (categoryMapping != null) {
                        surchargeItem.setCategoryName(categoryMapping.getB2bCategoryName());
                    }
                    surchargeItem.setItemId(item.getMaterial().getId());
                    surchargeItem.setItemQty(item.getQty());
                    surchargeItem.setUnitPrice(item.getMaterial().getPrice());
                    surchargeItem.setTotalPrice(item.getSubtotal());
                    B2BSurchargeItemMapping itemMapping = surchargeItemMappingMap.get(item.getMaterial().getId());
                    if (itemMapping != null) {
                        surchargeItem.setItemName(itemMapping.getB2bItemName());
                    }
                    if (auxiliaryMaterialMap.containsKey(productId)) {
                        auxiliaryMaterialMap.get(productId).add(surchargeItem);
                    } else {
                        auxiliaryMaterialMap.put(productId, Lists.newArrayList(surchargeItem));
                    }
                }

                OrderItemComplete itemComplete;
                ProductCompletePicItem picItem;
                B2BOrderCompletedItem completedItem;
                for (FourTuple<Long, String, Integer, Set<Long>> item : b2BProductCodeMappings) {
                    List<OrderItemComplete> picItems = null;
                    if (completedPicMap.containsKey(item.getAElement())) {
                        picItems = completedPicMap.get(item.getAElement());
                    } else {
                        if (!item.getDElement().isEmpty()) {
                            for (Long pId : item.getDElement()) {
                                if (completedPicMap.containsKey(pId)) {
                                    picItems = completedPicMap.get(pId);
                                    break;
                                }
                            }
                        }
                    }

                    List<B2BOrderCompletedItem.B2BSurchargeItem> surchargeItems = auxiliaryMaterialMap.get(item.getAElement());

                    int qty = item.getCElement();
                    if (picItems != null && picItems.size() > 0) {
                        qty = picItems.size();
                    }
                    qty = qty > 1 ? 1 : qty;

                    for (int i = 0; i < qty; i++) {
                        completedItem = new B2BOrderCompletedItem();
                        completedItem.setB2bProductCode(item.getBElement());

                        //设置完工图片、产品条码
                        if (picItems != null && picItems.size() > i) {
                            itemComplete = picItems.get(i);
                            Map<String, ProductCompletePicItem> picItemMap = itemComplete.getItemList().stream()
                                    .filter(p -> StringUtils.isNotBlank(p.getPictureCode()) && StringUtils.isNotBlank(p.getUrl()))
                                    .collect(Collectors.toMap(ProductCompletePicItem::getPictureCode, p -> p));

                            completedItem.setUnitBarcode(StringUtils.toString(itemComplete.getUnitBarcode()));
                            completedItem.setOutBarcode(StringUtils.toString(itemComplete.getOutBarcode()));
                            picItem = picItemMap.get("pic4");//条码图片
                            if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
                                completedItem.setPic4(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                            }
                            picItem = picItemMap.get("pic1");//现场图片
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
                        }

                        if (i == 0 && surchargeItems != null && !surchargeItems.isEmpty()) {
                            completedItem.setSurchargeItems(surchargeItems);
                        }

                        result.add(completedItem);
                    }
                }


            }
        }
        return result;
    }

}
