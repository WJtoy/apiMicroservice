package com.wolfking.jeesite.ms.lb.sd.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.lb.sd.feign.LbOrderFeign;
import com.wolfking.jeesite.ms.material.mq.entity.mapper.B2BMaterialMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class LbOrderService extends B2BOrderManualBaseService {

//    @Resource
//    private OrderItemDao orderItemDao;
//
//    @Autowired
//    private OrderItemCompleteService orderItemCompleteService;

    @Autowired
    ServicePointService servicePointService;

    @Autowired
    private LbOrderFeign lbOrderFeign;

    private static B2BMaterialMapper mapper = Mappers.getMapper(B2BMaterialMapper.class);


//    private List<CanboOrderCompleted.CompletedItem> getCanboOrderCompletedItems(Long orderId, String quarter, List<OrderItem> orderItems) {
//        List<CanboOrderCompleted.CompletedItem> list = null;
//        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && orderItems != null && !orderItems.isEmpty()) {
//            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
//            CanboOrderCompleted.CompletedItem completedItem = null;
//            ProductCompletePicItem picItem = null;
//            if (completeList != null && !completeList.isEmpty()) {
//                list = com.google.common.collect.Lists.newArrayList();
//                Map<Long, List<String>> b2bProductCodeMap = getProductIdToB2BProductCodeMapping(orderItems);
//                List<String> b2bProductCodeList;
//                for (OrderItemComplete item : completeList) {
//                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
//                    Map<String, ProductCompletePicItem> picItemMap = Maps.newHashMap();
//                    for (ProductCompletePicItem innerItem : item.getItemList()) {
//                        picItemMap.put(innerItem.getPictureCode(), innerItem);
//                    }
//                    completedItem = new CanboOrderCompleted.CompletedItem();
//
//                    //获取B2B产品编码
//                    b2bProductCodeList = b2bProductCodeMap.get(item.getProduct().getId());
//                    if (b2bProductCodeList != null && !b2bProductCodeList.isEmpty()) {
//                        completedItem.setItemCode(b2bProductCodeList.get(0));
//                        if (b2bProductCodeList.size() > 1) {
//                            b2bProductCodeList.remove(0);
//                        }
//                    } else {
//                        completedItem.setItemCode("");
//                    }
//
//                    completedItem.setBarcode(StringUtils.toString(item.getUnitBarcode()));
//                    completedItem.setOutBarcode(StringUtils.toString(item.getOutBarcode()));
//                    //条码图片
//                    picItem = picItemMap.get("pic4");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic4(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    //现场图片
//                    picItem = picItemMap.get("pic1");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic1(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    picItem = picItemMap.get("pic2");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic2(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    picItem = picItemMap.get("pic3");
//                    if (picItem != null && StringUtils.isNotBlank(picItem.getUrl())) {
//                        completedItem.setPic3(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
//                    }
//                    list.add(completedItem);
//                }
//            }
//        }
//        return list;
//    }


    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 派单请求对象
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
     * 预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate, Long servicePointId, Long engineerId) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && servicePointId != null && servicePointId > 0 && engineerId != null && engineerId > 0) {
            Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
            if (engineer != null && StringUtils.isNotBlank(engineer.getName()) && StringUtils.isNotBlank(engineer.getContactInfo())) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setEffectiveDate(appointmentDate)
                        .setEngineerName(engineer.getName())
                        .setEngineerMobile(engineer.getContactInfo());
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

//    /**
//     * 创建退单申请请求对象
//     */
//    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createReturnOrderApplyRequestEntity(String remarks) {
//        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
//        B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
//        builder.setRemarks(StringUtils.toString(remarks));
//        result.setAElement(true);
//        result.setBElement(builder);
//        return result;
//    }


//    /**
//     * 创建同望完工请求对象
//     */
//    @Transactional()
//    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, List<OrderItem> orderItems, Double orderCharge) {
//        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
//        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
//            if (orderItems == null || orderItems.isEmpty()) {
//                Order order = orderItemDao.getOrderItems(quarter, orderId);
//                if (order != null) {
//                    orderItems = OrderItemUtils.fromOrderItemsJson(order.getOrderItemJson());
//                }
//            }
//            List<CanboOrderCompleted.CompletedItem> completedItems = getCanboOrderCompletedItems(orderId, quarter, orderItems);
//            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
//            builder.setCompletedItems(completedItems)
//                    .setOrderCharge(orderCharge);
//            result.setAElement(true);
//            result.setBElement(builder);
//        }
//        return result;
//    }

    //region 配件单

    /**
     * 申请配件单
     */
    public MSResponse newMaterial(MaterialMaster materialMaster) {
        try {
            B2BMaterial materialForm = mapper.toB2BMaterialForm(materialMaster);
            if (materialForm == null) {
                return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "配件单转新迎燕配件单错误"));
            }
            materialForm.setApplyType(materialMaster.getApplyType().getIntValue());
            materialForm.setB2bOrderId(materialMaster.getB2bOrderId());
            return lbOrderFeign.newMaterial(materialForm);
        } catch (Exception e) {
            log.error("orderId:{} ", materialMaster.getOrderId(), e);
            return new MSResponse<>(MSErrorCode.newInstance(MSErrorCode.FAILURE, "微服务接口执行失败"));
        }
    }

    /**
     * 处理完"审核"消息回调通知微服务
     */
    public MSResponse notifyApplyFlag(Long formId) {
        return lbOrderFeign.updateAuditFlag(formId);
    }

    /**
     * 处理完"已发货"消息回调通知微服务
     */
    public MSResponse notifyDeliverFlag(Long formId) {
        return lbOrderFeign.updateDeliverFlag(formId);
    }

    //endregion   配件单

}
