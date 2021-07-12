package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCenterOrderModifyService {

//    @Autowired
//    private B2BCenterModifyB2BOrderMQSender b2BCenterModifyB2BOrderMQSender;
//    @Autowired
//    private SuningOrderService suningOrderService;
//    @Autowired
//    private XYYPlusOrderService xyyPlusOrderService;
//    @Autowired
//    private AreaService areaService;
//    @Autowired
//    private OrderService orderService;


    /**
     * 修改B2B工单
     */
//    @Transactional()
//    public void modifyB2BOrder(Order order, boolean isRereadOrder) {
//        if (order != null && order.getId() != null && order.getId() > 0
//                && B2BOrderUtils.canModifyB2BOrder(order.getDataSourceId())) {
//            if (isRereadOrder) {
//                order = orderService.getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
//            }
//            if (order != null && order.getId() != null && order.getId() > 0
//                    && StringUtils.isNotBlank(order.getWorkCardId())
//                    && B2BOrderUtils.canModifyB2BOrder(order.getDataSourceId())) {
//                B2BOrderModifyEntity.Builder builder = new B2BOrderModifyEntity.Builder();
//                builder.setDataSourceId(order.getDataSource().getIntValue())
//                        .setKklOrderId(order.getId())
//                        .setB2bOrderNo(order.getWorkCardId())
//                        .setOperateTime((new Date()).getTime());
//
//                OrderCondition condition = order.getOrderCondition();
//                if (condition != null) {
//                    if (StringUtils.isNotBlank(condition.getUserName()) && StringUtils.isNotBlank(condition.getServicePhone())) {
//                        builder.setUserName(condition.getUserName())
//                                .setUserMobile(condition.getServicePhone())
//                                .setUserPhone(StringUtils.toString(condition.getPhone2()));
//                    }
//                    if (condition.getArea() != null && condition.getArea().getId() != null && condition.getArea().getId() > 0
//                            && StringUtils.isNotBlank(condition.getServiceAddress())) {
//                        Area countyArea = areaService.getFromCache(condition.getArea().getId());
//                        Area provinceArea = null;
//                        Area cityArea = null;
//                        if (countyArea != null && StringUtils.isNotBlank(countyArea.getParentIds())) {
//                            String[] parentIds = countyArea.getParentIds().split(",");
//                            if (parentIds.length == 4) {
//                                provinceArea = areaService.getFromCache(StringUtils.toLong(parentIds[2]));
//                                cityArea = areaService.getFromCache(StringUtils.toLong(parentIds[3]));
//                            }
//                        }
//                        if (provinceArea != null && StringUtils.isNotBlank(provinceArea.getName())
//                                && cityArea != null && StringUtils.isNotBlank(cityArea.getName())
//                                && StringUtils.isNotBlank(countyArea.getName())) {
//                            builder.setUserProvince(provinceArea.getName())
//                                    .setUserCity(cityArea.getName())
//                                    .setUserCounty(countyArea.getName())
//                                    .setUserStreet(condition.getServiceAddress());
//                        }
//                    }
//                }
//                B2BOrderModifyEntity modifyEntity = builder.build();
//                if (!(StringUtils.isNotBlank(modifyEntity.getUserName()) && StringUtils.isNotBlank(modifyEntity.getUserMobile())
//                        && StringUtils.isNotBlank(modifyEntity.getUserProvince()) && StringUtils.isNotBlank(modifyEntity.getUserCity())
//                        && StringUtils.isNotBlank(modifyEntity.getUserCounty()) && StringUtils.isNotBlank(modifyEntity.getUserStreet()))) {
//                    B2BOrderModifyEntity.saveFailureLog(modifyEntity, "工单的用户信息与地址信息不完整", "B2BCenterOrderModifyService.modifyB2BOrder", null);
//                }
//                sendModifyB2BOrderMessage(modifyEntity);
//            } else {
//                Map<String, Object> params = Maps.newHashMap();
//                if (order != null) {
//                    params.put("orderId", order.getId() != null ? order.getId() : 0);
//                    params.put("workCardId", StringUtils.toString(order.getWorkCardId()));
//                    params.put("dataSourceId", order.getDataSourceId());
//                } else {
//                    params.put("order", "null");
//                }
//                String logJson = GsonUtils.toGsonString(params);
//                LogUtils.saveLog("读取工单失败", "B2BCenterOrderModifyService.modifyB2BOrder", logJson, null, null);
//            }
//        }
//    }


//    /**
//     * 往修改B2B工单的消息队列发消息
//     */
//    private void sendModifyB2BOrderMessage(B2BOrderModifyEntity entity) {
//        MQB2BOrderModifyMessage.B2BOrderModifyMessage.Builder builder = MQB2BOrderModifyMessage.B2BOrderModifyMessage.newBuilder();
//        builder.setDataSource(entity.getDataSourceId())
//                .setKklOrderId(entity.getKklOrderId())
//                .setB2BOrderNo(entity.getB2bOrderNo())
//                .setOperateTime(entity.getOperateTime())
//                .setUserName(entity.getUserName())
//                .setUserMobile(entity.getUserMobile())
//                .setUserPhone(entity.getUserPhone())
//                .setRemarks(entity.getRemarks())
//                .setUserProvince(entity.getUserProvince())
//                .setUserCity(entity.getUserCity())
//                .setUserCounty(entity.getUserCounty())
//                .setUserStreet(entity.getUserStreet());
//        b2BCenterModifyB2BOrderMQSender.send(builder.build());
//    }

}
