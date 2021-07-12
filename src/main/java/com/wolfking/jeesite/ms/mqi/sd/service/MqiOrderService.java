package com.wolfking.jeesite.ms.mqi.sd.service;

import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.service.OrderItemService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.mqi.sd.feign.MqiOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class MqiOrderService extends B2BOrderManualBaseService {

    @Resource
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemCompleteService orderItemCompleteService;
    @Autowired
    private ServicePointService servicePointService;
    @Autowired
    private MqiOrderFeign mqiOrderFeign;

    //region 其他


    private List<CanboOrderCompleted.CompletedItem> getCanboOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
        List<CanboOrderCompleted.CompletedItem> list = null;
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            CanboOrderCompleted.CompletedItem completedItem = null;
            ProductCompletePicItem picItem = null;
            if (completeList != null && !completeList.isEmpty()) {
                list = com.google.common.collect.Lists.newArrayList();
                Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);
                ;
//                for (OrderItem orderItem : orderItems) {
//                    if (StringUtils.isNotBlank(orderItem.getB2bProductCode())) {
//                        if (b2bProductCodeMap.containsKey(orderItem.getProductId())) {
//                            b2bProductCodeMap.get(orderItem.getProductId()).add(orderItem.getB2bProductCode());
//                        } else {
//                            b2bProductCodeMap.put(orderItem.getProductId(), com.google.common.collect.Lists.newArrayList(orderItem.getB2bProductCode()));
//                        }
//                    }
//                }
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
                    list.add(completedItem);
                }
            }
        }
        return list;
    }

    //endregion 其他


    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 创建派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate, String remarks, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate)
                    .setUpdaterName(updater.getName())
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建上门请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createServiceRequestEntity(Date visitedDate, Long servicePointId, Long engineerId, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (visitedDate != null && servicePointId != null && servicePointId > 0 && engineerId != null && engineerId > 0) {
            Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
            if (engineer != null && StringUtils.isNotBlank(engineer.getName()) && StringUtils.isNotBlank(engineer.getContactInfo())) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setEngineerName(engineer.getName())
                        .setEffectiveDate(visitedDate)
                        .setRemarks(StringUtils.toString(remarks));
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

    /**
     * 创建完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemService.getOrderItems(quarter, orderId);//2020-12-20 sd_order -> sd_order_head
                if (order != null) {
                    orderItems = order.getItems();
                }
            }
            List<CanboOrderCompleted.CompletedItem> completedItems = getCanboOrderCompletedItems(orderId, quarter, orderItems);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setCompletedItems(completedItems);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCancelRequestEntity(Date cancelDate, User updater, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (cancelDate != null && updater != null && StringUtils.isNotBlank(updater.getName())) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(cancelDate)
                    .setUpdaterName(updater.getName())
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    //region 验证产品SN

    public MSResponse checkProductSN(String b2bOrderNo, String productSn) {
        return mqiOrderFeign.getProductData(b2bOrderNo, productSn);
    }

    //endregion 验证产品SN
}
