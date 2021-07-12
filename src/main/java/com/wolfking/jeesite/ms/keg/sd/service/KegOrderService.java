package com.wolfking.jeesite.ms.keg.sd.service;

import com.google.common.collect.Lists;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BCenterPushOrderInfoToMsMessage;
import com.kkl.kklplus.entity.keg.sd.KegOrder;
import com.kkl.kklplus.entity.keg.sd.KegOrderCompleted;
import com.kkl.kklplus.entity.keg.sd.KegOrderItem;
import com.kkl.kklplus.entity.keg.sd.KegOrderServiceItem;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterPushOrderInfoToMsService;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.keg.sd.feign.KegOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class KegOrderService {

    @Autowired
    private AreaService areaService;
    @Autowired
    private ServiceTypeService serviceTypeService;
    @Autowired
    OrderItemCompleteService orderItemCompleteService;
    @Autowired
    private MicroServicesProperties microServicesProperties;
    @Autowired
    private KegOrderFeign kegOrderFeign;

    //region 工具方法

    /**
     * 是否是韩电客户
     */
    public boolean isKegCustomer(Long customerId) {
        List<Long> customerIds = microServicesProperties.getKeg().getCustomerIds();
        if (microServicesProperties.getKeg().getEnabled()
                && microServicesProperties.getKeg().getPushOrderInfoEnabled()
                && customerIds != null && !customerIds.isEmpty()
                && customerId != null && customerId > 0) {
            return customerIds.contains(customerId);
        }
        return false;
    }

    //endregion 工具方法

    //region 调用KEG微服务
    public MSResponse pushOrderInfoToMS(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        MSResponse response;
        if (message.getActionCode() == Order.ORDER_STATUS_NEW) {
            response = newOrder(message);
        } else if (message.getActionCode() == Order.ORDER_STATUS_COMPLETED) {
            response = completeOrder(message);
        } else {
            response = new MSResponse(MSErrorCode.SUCCESS);
            B2BCenterPushOrderInfoToMsService.saveFailureLog("KegOrderService.pushOrderInfoToMS", new JsonFormat().printToString(message), null);
        }
        return response;
    }


    private MSResponse newOrder(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        KegOrder order = new KegOrder();
        order.setCustomerId(message.getCustomerId());
        order.setOrderServiceType(message.getOrderServiceType());
        order.setMobile(message.getServicePhone());
        order.setAreaId(message.getAreaId());
        order.setAddress(message.getServiceAddress());
        order.setUserName(message.getUserName());
        order.setDescription(message.getDescription());
        order.setProvinceId(message.getProvinceId());
        order.setCityId(message.getCityId());
        order.setOrderNo(message.getOrderNo());
        order.setOrderId(message.getOrderId());
        order.setB2bDataSource(message.getDataSourceId());
        order.setCreateDate(message.getCreateDate());
        order.setCreateBy(message.getCreateBy());
        order.setQuarter(message.getQuarter());
        order.setB2bOrderNo(message.getWorkCardId());
        order.setOrderStatus(message.getStatus());
        order.setParentBizOrderId(message.getParentBizOrderId());
        order.setShopId(message.getShopId());
        order.setShopName(message.getShopName());
        order.setBuyDate(message.getBuyDate());
        List<KegOrderItem> itemList = Lists.newArrayList();
        KegOrderItem orderItem;
        for (MQB2BCenterPushOrderInfoToMsMessage.OrderItem item : message.getItemsList()) {
            orderItem = new KegOrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductSpec(item.getProductSpec());
            orderItem.setQty(item.getQty());
            orderItem.setServiceTypeId(item.getServiceTypeId());
            orderItem.setWarranty(item.getWarranty());
            itemList.add(orderItem);
        }
        order.setOrderItems(itemList);
        return kegOrderFeign.newOrder(order);
    }

    private MSResponse completeOrder(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        KegOrderCompleted order = new KegOrderCompleted();
        order.setOrderId(message.getOrderId());
        order.setCompletedDate(message.getCloseDate());
        order.setUpdateBy(message.getCreateBy());
        List<KegOrderServiceItem> itemList = Lists.newArrayList();
        KegOrderServiceItem serviceItem;
        for (MQB2BCenterPushOrderInfoToMsMessage.OrderDetail detail : message.getServicesList()) {
            serviceItem = new KegOrderServiceItem();
            serviceItem.setProductId(detail.getProductId());
            serviceItem.setPics(detail.getPicsList());
            serviceItem.setBarCode(detail.getBarCode());
            itemList.add(serviceItem);
        }
        order.setOrderServiceItems(itemList);
        return kegOrderFeign.completed(order);
    }

    //endregion 调用KEG微服务

    //region 创建消息体

    /**
     * 创建新单消息体
     */
    public MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage createNewOrderMessage(Order order) {
        MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage.Builder builder = MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage.newBuilder();
        builder.setErrorCode(0);
        builder.setMsCode(B2BDataSourceEnum.KEG.id);
        builder.setActionCode(Order.ORDER_STATUS_NEW);
        try {
            OrderCondition condition = order.getOrderCondition();
            List<OrderItem> orderItems = order.getItems();
            long cityId = 0;
            long provinceId = 0;
            Area area = areaService.getFromCache(condition.getArea().getId(), Area.TYPE_VALUE_COUNTY);
            if (area != null && StringUtils.isNotBlank(area.getParentIds())) {
                String[] parentIds = area.getParentIds().split(",");
                if (parentIds.length == 4) {
                    provinceId = StringUtils.toLong(parentIds[2]);
                    cityId = StringUtils.toLong(parentIds[3]);
                }
            }
            String shopId = order.getB2bShop() != null && StringUtils.isNotBlank(order.getB2bShop().getShopId()) ? order.getB2bShop().getShopId() : "";
            String shopName = "";
            if (StringUtils.isNotBlank(shopId)) {
                TwoTuple<Map<String, B2BCustomerMapping>, Map<String, B2BCustomerMapping>> allCustomerMappingMaps = B2BMDUtils.getAllCustomerMappingMaps();
                shopName = B2BMDUtils.getShopName(order.getDataSource().getIntValue(), shopId, allCustomerMappingMaps);
            }
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            builder.setCustomerId(condition.getCustomerId())
                    .setOrderServiceType(condition.getOrderServiceType())
                    .setServicePhone(condition.getServicePhone())
                    .setAreaId(condition.getArea().getId())
                    .setServiceAddress(condition.getAreaName() + condition.getServiceAddress())
                    .setUserName(condition.getUserName())
                    .setDescription(order.getDescription())
                    .setProvinceId(provinceId)
                    .setCityId(cityId)
                    .setOrderNo(order.getOrderNo())
                    .setOrderId(order.getId())
                    .setDataSourceId(order.getDataSource().getIntValue())
                    .setCreateDate(condition.getCreateDate().getTime())
                    .setCreateBy(condition.getCreateBy().getId())
                    .setQuarter(condition.getQuarter())
                    .setWorkCardId(StringUtils.toString(order.getWorkCardId()))
                    .setStatus(condition.getStatusValue())
                    .setParentBizOrderId(StringUtils.toString(order.getParentBizOrderId()))
                    .setShopId(shopId)
                    .setShopName(shopName)
                    .setBuyDate(condition.getCreateDate().getTime());
            MQB2BCenterPushOrderInfoToMsMessage.OrderItem orderItemMessage;
            ServiceType serviceType;
            for (OrderItem item : orderItems) {
                serviceType = serviceTypeMap.get(item.getServiceType().getId());
                orderItemMessage = MQB2BCenterPushOrderInfoToMsMessage.OrderItem.newBuilder()
                        .setProductId(item.getProductId())
                        .setProductSpec(item.getProductSpec())
                        .setQty(item.getQty())
                        .setServiceTypeId(item.getServiceType().getId())
                        .setWarranty(serviceType.getWarrantyStatus().getValue())
                        .build();
                builder.addItems(orderItemMessage);
            }

        } catch (Exception e) {
            builder.setErrorCode(10000);
            B2BCenterPushOrderInfoToMsService.saveFailureLog("KegOrderService.createNewOrderMessage", new JsonFormat().printToString(builder.build()), e);
        }
        return builder.build();
    }

    /**
     * 创建工单完成消息体
     */
    public MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage createCompleteOrderMessage(Long orderId, String quarter, Date closeDate, User updateBy) {
        MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage.Builder builder = MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage.newBuilder();
        builder.setErrorCode(0);
        builder.setMsCode(B2BDataSourceEnum.KEG.id);
        builder.setActionCode(Order.ORDER_STATUS_COMPLETED);
        builder.setCloseDate(closeDate.getTime());
        builder.setCreateBy(updateBy == null || updateBy.getId() == null ? B2BMDUtils.B2B_USER.getId() : updateBy.getId());
        builder.setOrderId(orderId);
        List<OrderItemComplete> list = orderItemCompleteService.getByOrderId(orderId, quarter);
        if (list != null && !list.isEmpty()) {
            MQB2BCenterPushOrderInfoToMsMessage.OrderDetail.Builder detailBuilder;
            for (OrderItemComplete item : list) {
                detailBuilder = MQB2BCenterPushOrderInfoToMsMessage.OrderDetail.newBuilder();
                item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                detailBuilder.setProductId(item.getProduct().getId())
                        .setBarCode(StringUtils.toString(item.getUnitBarcode()));
                for (ProductCompletePicItem picItem : item.getItemList()) {
                    if (StringUtils.isNotBlank(picItem.getUrl())) {
                        detailBuilder.addPics(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                    }
                }
                builder.addServices(detailBuilder.build());
            }
        }
        return builder.build();
    }

    //endregion 创建消息体

}
