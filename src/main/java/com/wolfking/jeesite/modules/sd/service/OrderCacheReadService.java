package com.wolfking.jeesite.modules.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.md.service.CustomerService;
import com.wolfking.jeesite.modules.md.service.ProductCategoryService;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.dao.OrderAttachmentDao;
import com.wolfking.jeesite.modules.sd.dao.OrderDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderRedisAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import com.wolfking.jeesite.ms.tmall.md.service.B2bCustomerMapService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.StyledEditorKit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderCacheReadService extends LongIDBaseService {

    @Resource
    private OrderAttachmentDao attachmentDao;

    @Resource
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private B2bCustomerMapService b2bCustomerMapService;

    @Autowired
    private OrderCacheService orderCacheService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private static final OrderUtils.OrderDataLevel[] ORDER_DATA_LEVELS = OrderUtils.OrderDataLevel.values();

    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst) {
        return getOrderById(orderId, quarter, level, cacheFirst, false);
    }

    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst, boolean fromMasterDb) {
        Order order = null;
        try {
            order = getOrderFromCacheAndDB(orderId, quarter, level, cacheFirst, fromMasterDb,false);
        } catch (Exception e) {
            log.error("[OrderCacheReadService.getOrderById] read order:{}", orderId, e);
            LogUtils.saveLog("读取订单失败", "OrderCacheReadService.getOrderById", orderId.toString().concat(",quarter:").concat(quarter).concat(",level:").concat(level.toString()), e, null);
            orderCacheService.delete(orderId);
        }
        return order;
    }

    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst, boolean fromMasterDb,boolean loadCategory) {
        Order order = null;
        try {
            order = getOrderFromCacheAndDB(orderId, quarter, level, cacheFirst, fromMasterDb,loadCategory);
        } catch (Exception e) {
            log.error("[OrderCacheReadService.getOrderById] read order:{}", orderId, e);
            LogUtils.saveLog("读取订单失败", "OrderCacheReadService.getOrderById", orderId.toString().concat(",quarter:").concat(quarter).concat(",level:").concat(level.toString()), e, null);
            orderCacheService.delete(orderId);
        }
        return order;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 读取工单信息
     */
    private Order getOrderFromCacheAndDB(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst, boolean fromMasterDb,boolean loadCategory) {
        Order order = null;
        if (!cacheFirst) {
            order = getOrderFromDB(orderId, quarter, level, fromMasterDb);
        } else {
            OrderCacheResult cacheResult = orderCacheService.getOrderAllInfo(orderId);
            if (cacheResult == null) {
                order = getOrderFromDB(orderId, quarter, level, fromMasterDb);
                if (order != null && order.getOrderCondition() != null
                        && order.getOrderCondition().getChargeFlag() == 0
                        && order.getOrderCondition().getStatusValue() < Order.ORDER_STATUS_RETURNED) {
                    writeOrderAllInfoToCache(order);
                }
            } else {
                OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
                builder.setOrderId(orderId);
                /* Order */
                if (cacheResult.getInfo() != null) {
                    order = cacheResult.getInfo();
                    if (StringUtils.isBlank(order.getQuarter())) {
                        if (StringUtils.isNotBlank(cacheResult.getQuarter())) {
                            order.setQuarter(cacheResult.getQuarter());
                        } else {
                            order.setQuarter(quarter);
                        }
                    }
                    if (order.getDataSource() != null && StringUtils.isBlank(order.getDataSource().getLabel())) {
                        order.getDataSource().setLabel(getDataSourceNameFromMS(order.getDataSource()));
                    }
                } else {
                    order = orderService.getOrderByIdMS(orderId, quarter);
                    if (order == null) {
                        throw new OrderException("Order为null");
                    }
                    builder.setInfo(order);
                }
                /* OrderCondition */
                if (level.ordinal() >= OrderUtils.OrderDataLevel.CONDITION.ordinal()) {
                    OrderCondition condition;
                    if (cacheResult.getCondition() != null) {
                        condition = getOrderConditionFromCacheAndDB(cacheResult, orderId, quarter, fromMasterDb);
                    } else {
                        condition = getOrderConditionFromMasterDB(order.getId(), order.getQuarter());
                        if (condition != null) {
//                            order.getB2bShop().setShopName(getShopNameFromMS(order.getDataSource(), condition.getCustomer(), order.getB2bShop()));
                            order.getB2bShop().setShopName(getShopNameFromMSNew(order.getDataSource(), condition.getCustomer(), order.getB2bShop()));
                            setOrderCacheParamBuilder(builder, condition);
                        }
                    }
                    if (condition == null) {
                        throw new OrderException("OrderCondition为null");
                    }
                    order.setOrderCondition(condition);
                }
                /* OrderStatus */
                if (level.ordinal() >= OrderUtils.OrderDataLevel.STATUS.ordinal()) {
                    OrderStatus status;
                    if (cacheResult.getOrderStatus() != null) {
                        status = cacheResult.getOrderStatus();
                    } else {
                        status = orderService.getOrderStatusById(order.getId(), order.getQuarter(), fromMasterDb);
                        if (status == null) {
                            throw new OrderException("OrderStatus为null");
                        }
                        builder.setOrderStatus(status);
                    }
                    order.setOrderStatus(status);
                }
                /* OrderFee */
                if (level.ordinal() >= OrderUtils.OrderDataLevel.FEE.ordinal()) {
                    TwoTuple<OrderFee, List<OrderServicePointFee>> tuple = getOrderFeesFromDB(order.getId(), order.getQuarter(), fromMasterDb);
                    if (tuple.getAElement() == null) {
                        throw new OrderException("OrderFee为null");
                    }
                    order.setOrderFee(tuple.getAElement());
                    order.setServicePointFees(tuple.getBElement());
                }
                /* OrderDetail */
                if (level.ordinal() >= OrderUtils.OrderDataLevel.DETAIL.ordinal()) {
                    if (order.getOrderCondition().getStatusValue() >= Order.ORDER_STATUS_SERVICED && order.getOrderCondition().getServiceTimes() > 0) {
                        // 2020-11-12 getOrderDetailsFromDB中对下面处理做了判断，删除重复的处理
                        //读主库的请求 或 有上门服务 才访问数据库
                        List<OrderDetail> detailList = getOrderDetailsFromDB(order.getId(), order.getQuarter(), fromMasterDb);
                        order.setDetailList(detailList);
                        List<OrderAttachment> attachments;
                        if (cacheResult.getAttachments() != null && !cacheResult.getAttachments().isEmpty()) {
                            attachments = cacheResult.getAttachments();
                            order.setAttachments(attachments);
                        } else {
                            Integer finishPhotoQty = 0;
                            if (cacheResult.getFinishPhotoQty() != null) {
                                finishPhotoQty = StringUtils.toInteger(cacheResult.getFinishPhotoQty());
                            }
                            if (finishPhotoQty <= 0 && order.getOrderCondition() != null && order.getOrderCondition().getFinishPhotoQty() > 0) {
                                finishPhotoQty = order.getOrderCondition().getFinishPhotoQty();
                            }
                            if (finishPhotoQty > 0) {
                                attachments = getOrderAttachmentsFromDB(order.getId(), order.getQuarter());
                                order.setAttachments(attachments);
//                                builder.setAttachments(attachments); //图片附件信息不再缓存，该数据变更频繁，同步代价较高
                            }
                        }
                    }
                }
                /* 更新工单缓存/删除工单缓存 */
                if (order.getOrderCondition() != null) {
                    if (order.getOrderCondition().getChargeFlag() == 0 && order.getOrderCondition().getStatusValue() < Order.ORDER_STATUS_RETURNED) {
                        builder.setExpireSeconds(OrderUtils.ORDER_EXPIRED);
                        orderCacheService.update(builder.build());
                    } else {
                        orderCacheService.delete(orderId);
                    }
                }
            }
        }
        //产品品类
        long productCategoryId = Optional.ofNullable(order).map(o->o.getOrderCondition()).map(t->t.getProductCategoryId()).orElse(0L);
        if(loadCategory && productCategoryId >0){
            ProductCategory category = productCategoryService.getByIdForMD(productCategoryId);
            if(category != null){
                order.getOrderCondition().setProductCategory(category);
            }
        }
        return order;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 从数据库读取工单
     */
    private Order getOrderFromDB(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean fromMasterDb) {
        Order order = orderService.getOrderByIdMS(orderId, quarter);
        if (order != null) {
            if (StringUtils.isBlank(order.getOrderInfo())) {
                order.setOrderInfo(OrderRedisAdapter.getInstance().toJson(order));
            }
            if (level.ordinal() > OrderUtils.OrderDataLevel.HEAD.ordinal()) {
                Set<OrderUtils.OrderDataLevel> levels = Sets.newHashSet();
                for (OrderUtils.OrderDataLevel item : ORDER_DATA_LEVELS) {
                    if (level.ordinal() >= item.ordinal()) {
                        levels.add(item);
                    }
                }
                if (levels.contains(OrderUtils.OrderDataLevel.CONDITION)) {
                    OrderCondition condition = getOrderConditionFromMasterDB(order.getId(), order.getQuarter());
                    order.setOrderCondition(condition);
                    order.getB2bShop().setShopName(getShopNameFromMSNew(order.getDataSource(), condition.getCustomer(), order.getB2bShop()));
                }
                if (levels.contains(OrderUtils.OrderDataLevel.FEE)) {
                    TwoTuple<OrderFee, List<OrderServicePointFee>> tuple = getOrderFeesFromDB(order.getId(), order.getQuarter(), fromMasterDb);
                    order.setOrderFee(tuple.getAElement());
                    order.setServicePointFees(tuple.getBElement());
                }
                if (levels.contains(OrderUtils.OrderDataLevel.STATUS)) {
                    OrderStatus orderStatus = orderService.getOrderStatusById(order.getId(), order.getQuarter(), fromMasterDb);
                    order.setOrderStatus(orderStatus == null ? new OrderStatus() : orderStatus);
                }
                if (levels.contains(OrderUtils.OrderDataLevel.DETAIL)) {
                    order.setDetailList(getOrderDetailsFromDB(order.getId(), order.getQuarter(), fromMasterDb));
                    if (order.getOrderCondition().getFinishPhotoQty() > 0) {
                        order.setAttachments(getOrderAttachmentsFromDB(order.getId(), order.getQuarter()));
                    }
                }
            }
        }
        return order;
    }


    /**
     * 从数据库读取OrderFee、OrderServicePointFee
     */
    private TwoTuple<OrderFee, List<OrderServicePointFee>> getOrderFeesFromDB(Long orderId, String quarter, boolean fromMasterDb) {
        OrderFee orderFee = orderService.getOrderFeeById(orderId, quarter, fromMasterDb);
        if (orderFee == null) {
            throw new OrderException("OrderFee为null");
        }
        List<OrderServicePointFee> pointFees = orderService.getOrderServicePointFees(orderId, quarter, fromMasterDb);
        return new TwoTuple<>(orderFee, pointFees == null ? Lists.newArrayList() : pointFees);
    }

    /**
     * 从数据库读取实际上门服务项
     * 有上门服务的操作后（新增，删除），指定期限内(RedisConstant.SD_ORDER_DETAIL_FLAG_TIMEOUT)，读取主库，
     * 防止主从未同步完成，再有上门服务的操作，造成费用汇总错误
     */
    private List<OrderDetail> getOrderDetailsFromDB(Long orderId, String quarter, boolean fromMasterDb) {
        boolean detailFromMasterDb = fromMasterDb;
        if(!detailFromMasterDb){
            detailFromMasterDb = orderCacheService.hasSetDetailActionFlag(orderId);
        }
        List<OrderDetail> list = orderService.getOrderDetails(orderId, quarter, detailFromMasterDb);
        return list == null ? Lists.newArrayList() : list;
    }

    /**
     * 从数据库读取附件信息
     */
    private List<OrderAttachment> getOrderAttachmentsFromDB(Long orderId, String quarter) {
        List<OrderAttachment> list = attachmentDao.getByOrderId(orderId, quarter);
        List<Long> createByIds = list.stream().map(i -> i.getCreateBy().getId()).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = MSUserUtils.getMapByUserIds(createByIds);
        list.forEach(i -> {
            if (userMap.get(i.getCreateBy().getId()) != null) {
                i.setCreateBy(userMap.get(i.getCreateBy().getId()));
            }
        });
        return list;
    }

    /**
     * 从数据库读取OrderCondition
     */
    private OrderCondition getOrderConditionFromMasterDB(Long orderId, String quarter) {
        OrderCondition condition = orderService.getOrderConditionFromMasterById(orderId, quarter);
        if (condition != null) {
            condition.setVersion(condition.getUpdateDate().getTime());
            condition.setOrderId(orderId);
            TwoTuple<Customer, ServicePoint> tuple = getCustomerAndServicePointFromCache(condition.getCustomer(), condition.getServicePoint(),condition.getEngineer());
            condition.setCustomer(tuple.getAElement());
            condition.setServicePoint(tuple.getBElement());
        } else {
            throw new OrderException("OrderCondition为null");
        }
        return condition;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 从微服务读取店铺名称
     */
    private String getShopNameFromMSNew(Dict dataSourceDict, Customer customer, B2bCustomerMap b2bShop) {
        String shopName = "";
        if (dataSourceDict != null && B2BDataSourceEnum.isDataSource(dataSourceDict.getIntValue())
                && b2bShop != null && StringUtils.isNotBlank(b2bShop.getShopId())
                && customer != null && customer.getId() != null) {
            if (B2BDataSourceEnum.isB2BDataSource(dataSourceDict.getIntValue())) {
                shopName = b2bCustomerMapService.getShopName(dataSourceDict.getIntValue(), customer.getId(), b2bShop.getShopId());
            } else {
                shopName = B2BMDUtils.getShopName(customer.getId(), b2bShop.getShopId());
            }
        }
        return shopName;
    }

    /**
     * 获取数据源的名称
     */
    private String getDataSourceNameFromMS(Dict dataSourceDict) {
        Dict realDataSource;
        if (dataSourceDict == null || dataSourceDict.getIntValue() == 0) {
            realDataSource = MSDictUtils.getDictByValue("1", Order.ORDER_DATA_SOURCE_TYPE);
        } else {
            realDataSource = MSDictUtils.getDictByValue(dataSourceDict.getValue(), Order.ORDER_DATA_SOURCE_TYPE);
        }
        return realDataSource == null ? "" : StringUtils.toString(realDataSource.getLabel());
    }


    //------------------------------------------------------------------------------------------------------------------

    /**
     * 从缓存与数据库中读取OrderCondition
     */
    private OrderCondition getOrderConditionFromCacheAndDB(OrderCacheResult cacheResult, Long orderId, String quarter, boolean fromMasterDb) {
        OrderCondition result = new OrderCondition();
        try {
            BeanUtils.copyProperties(cacheResult.getCondition(), result);
        } catch (Exception e) {
            log.error("[OrderService.getOrderConditionFromCacheAndDB]", e);
        }
        //缓存中未缓存网点信息
        if(result.getStatusValue() >= Order.ORDER_STATUS_PLANNED && result.getStatusValue() <= Order.ORDER_STATUS_CHARGED && result.getServicePoint() == null){
            Long sid = orderService.getCurrentServicePointId(orderId,quarter);
            if(sid != null && sid >0){
                result.setServicePoint(new ServicePoint(sid));
            }
        }
        /* 使用缓存中刷新频率高的数据更新刷新频率低的数据 */
        result.setVersion(cacheResult.getVersion() != null ? cacheResult.getVersion() : (result.getUpdateDate() != null ? result.getUpdateDate().getTime() : System.currentTimeMillis()));
        result.setFeedbackFlag(cacheResult.getFeedbackFlag() != null ? cacheResult.getFeedbackFlag() : result.getFeedbackFlag());
        result.setFeedbackTitle(cacheResult.getFeedbackTitle() != null ? cacheResult.getFeedbackTitle() : result.getFeedbackTitle());
        result.setFeedbackDate(cacheResult.getFeedbackDate() != null ? cacheResult.getFeedbackDate() : result.getFeedbackDate());
        result.setQuarter(StringUtils.isBlank(result.getQuarter()) && StringUtils.isNotBlank(cacheResult.getQuarter()) ? cacheResult.getQuarter() : result.getQuarter());
        result.setPendingType(cacheResult.getPendingType() != null ? cacheResult.getPendingType() : result.getPendingType());
        result.setAppointmentDate(cacheResult.getAppointmentDate() != null ? cacheResult.getAppointmentDate() : result.getAppointmentDate());
        result.setFinishPhotoQty(cacheResult.getFinishPhotoQty() != null ? StringUtils.toInteger(cacheResult.getFinishPhotoQty()) : result.getFinishPhotoQty());
        result.setReservationTimes(cacheResult.getReservationTimes() != null ? cacheResult.getReservationTimes() : result.getReservationTimes());
        result.setReservationDate(cacheResult.getReservationDate() != null ? cacheResult.getReservationDate() : result.getReservationDate());

        /* 关键信息重新读取数据库 */
        OrderCondition conditionDb = getOrderConditionImportantPropertiesFromDbById(orderId, quarter, fromMasterDb);
        if (conditionDb != null) {
            result.setStatus(conditionDb.getStatus());
            result.setServiceTimes(conditionDb.getServiceTimes());
            result.setAppAbnormalyFlag(conditionDb.getAppAbnormalyFlag());
            result.setGradeFlag(conditionDb.getGradeFlag());
            result.setPendingFlag(conditionDb.getPendingFlag());
            result.setPartsFlag(conditionDb.getPartsFlag());
            result.setChargeFlag(conditionDb.getChargeFlag());
            result.setReplyFlag(conditionDb.getReplyFlag());
            result.setReplyFlagKefu(conditionDb.getReplyFlagKefu());
            result.setReplyFlagCustomer(conditionDb.getReplyFlagCustomer());
            //result.setIsComplained(conditionDb.getIsComplained()); //投诉转到orderStatus
            result.setRushOrderFlag(conditionDb.getRushOrderFlag());
            result.setAppCompleteType(conditionDb.getAppCompleteType());
            result.setAppCompleteDate(conditionDb.getAppCompleteDate());
            result.setSubStatus(conditionDb.getSubStatus());
            result.setSuspendFlag(conditionDb.getSuspendFlag());
            result.setSuspendType(conditionDb.getSuspendType());
            //result.setReminderFlag(conditionDb.getReminderFlag());//2019/07/09
            //result.setAutoGradeFlag(conditionDb.getAutoGradeFlag());
            //result.setAutoCompleteFlag(conditionDb.getAutoCompleteFlag());
        }

        TwoTuple<Customer, ServicePoint> tuple = getCustomerAndServicePointFromCache(result.getCustomer(), result.getServicePoint(),result.getEngineer());
        result.setCustomer(tuple.getAElement());
        result.setServicePoint(tuple.getBElement());

        return result;
    }

    /**
     * 从数据库读取OrderCondition中重要的属性值
     *
     * @return soc.status、soc.service_times、soc.app_abnormaly_flag、soc.grade_flag、soc.pending_flag、soc.charge_flag、
     * soc.parts_flag、soc.reply_flag、soc.reply_flag_kefu、soc.reply_flag_customer、soc.is_complained、soc.rush_order_flag、
     * soc.time_liness、soc.arrival_date、soc.app_complete_date、soc.app_complete_type、soc.sub_status,soc.reminder_flag
     */
    private OrderCondition getOrderConditionImportantPropertiesFromDbById(Long orderId, String quarter, boolean fromMasterDb) {
        OrderCondition condition;
        if (fromMasterDb) {
            condition = orderDao.getOrderConditionImportantPropertiesFromMasterById(orderId, quarter);
        } else {
            condition = orderDao.getOrderConditionImportantInfoById(orderId, quarter);
        }
        if (condition != null && condition.getStatus() != null && StringUtils.isNotBlank(condition.getStatus().getValue())) {
            String orderStatusLabel = MSDictUtils.getDictLabel(condition.getStatus().getValue(), "order_status", "状态错误");
            condition.getStatus().setLabel(orderStatusLabel);
        }
        return condition;
    }


    //------------------------------------------------------------------------------------------------------------------

    /**
     * 从缓存读取Customer、ServicePoint
     */
    private TwoTuple<Customer, ServicePoint> getCustomerAndServicePointFromCache(Customer c, ServicePoint ms, User engineer) {
        Customer customer = null;
        if (c != null && c.getId() != null && c.getId() > 0) {
            customer = customerService.getFromCache(c.getId());
        }
        ServicePoint servicePoint = null;
        if (ms != null && ms.getId() != null && ms.getId() > 0) {
            servicePoint = servicePointService.getFromCache(ms.getId());
            if(servicePoint == null){
                log.error("调用微服务读取网点:{} 无返回",ms.getId());
            }
        }else {
            //long eid = Optional.ofNullable(engineer.getId()).orElse(0l);
            long eid = Optional.ofNullable(engineer).map(User::getId).map(Long::longValue).orElse(0l);
            if(eid>0){
                //已派单
                log.error("传送参数有安维，无网点");
            }
        }
        return new TwoTuple<>(customer, servicePoint == null ? new ServicePoint(0L) : servicePoint);
    }

    /**
     * 将整个工单写入缓存
     */
    private void writeOrderAllInfoToCache(Order order) {
        OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
        builder.setOrderId(order.getId())
                .setWriteTime(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss:SSS"))
                .setInfo(order)
                .setQuarter(order.getQuarter())
                .setOrderStatus(order.getOrderStatus())
                .setExpireSeconds(OrderUtils.ORDER_EXPIRED);
//        if (order.getAttachments() != null && !order.getAttachments().isEmpty()) {
//            builder.setAttachments(order.getAttachments());
//        }
        setOrderCacheParamBuilder(builder, order.getOrderCondition());
        orderCacheService.update(builder.build());
    }

    /**
     * 配置工单缓存的更新参数
     */
    private void setOrderCacheParamBuilder(OrderCacheParam.Builder builder, OrderCondition condition) {
        builder.setCondition(condition)
                .setVersion(condition.getUpdateDate().getTime())
                .setFeedbackFlag(condition.getFeedbackFlag())
                .setFeedbackTitle(StringUtils.substring(condition.getFeedbackTitle(), 0, 20))
                .setFeedbackDate(condition.getFeedbackDate())
                .setReplyFlag(condition.getReplyFlag())
                .setReplyFlagKefu(condition.getReplyFlagKefu())
                .setReplyFlagCustomer(condition.getReplyFlagCustomer())
                .setPendingType(condition.getPendingFlag() > 0 ? condition.getPendingType() : null)
                .setAppointmentDate(condition.getAppointmentDate())
                .setFinishPhotoQty(StringUtils.toLong(condition.getFinishPhotoQty()))
                .setReservationDate(condition.getReservationDate())
                .setReservationTimes(condition.getReservationTimes());
    }

}
