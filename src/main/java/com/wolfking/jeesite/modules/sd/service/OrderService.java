/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.AbnormalFormEnum;
import com.kkl.kklplus.entity.md.AppFeedbackEnum;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.kkl.kklplus.entity.push.AppMessageType;
import com.kkl.kklplus.entity.sys.SysSMSTypeEnum;
import com.kkl.kklplus.entity.voiceservice.OperateType;
import com.kkl.kklplus.entity.voiceservice.mq.MQVoiceSeviceMessage;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.AjaxJsonEntity;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.BitUtils;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetailInfoNew;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.md.service.*;
import com.wolfking.jeesite.modules.md.utils.ServicePointUtils;
import com.wolfking.jeesite.modules.mq.conf.NoticeMessageConfig;
import com.wolfking.jeesite.modules.mq.dao.OrderAutoCompleteDao;
import com.wolfking.jeesite.modules.mq.dto.MQNoticeMessage;
import com.wolfking.jeesite.modules.mq.dto.MQOrderAutoComplete;
import com.wolfking.jeesite.modules.mq.dto.MQOrderServicePointMessage;
import com.wolfking.jeesite.modules.mq.dto.MQWebSocketMessage;
import com.wolfking.jeesite.modules.mq.entity.OrderAutoComplete;
import com.wolfking.jeesite.modules.mq.sender.NoticeMessageSender;
import com.wolfking.jeesite.modules.mq.sender.OrderAutoCompleteDelaySender;
import com.wolfking.jeesite.modules.mq.sender.sms.SmsCallbackTaskMQSender;
import com.wolfking.jeesite.modules.mq.sender.sms.SmsMQSender;
import com.wolfking.jeesite.modules.mq.sender.voice.NewTaskMQSender;
import com.wolfking.jeesite.modules.mq.sender.voice.OperateTaskMQSender;
import com.wolfking.jeesite.modules.mq.service.ServicePointOrderBusinessService;
import com.wolfking.jeesite.modules.sd.dao.AuxiliaryMaterialMasterDao;
import com.wolfking.jeesite.modules.sd.dao.OrderDao;
import com.wolfking.jeesite.modules.sd.dao.OrderHeadDao;
import com.wolfking.jeesite.modules.sd.dao.OrderItemCompleteDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderAdditionalInfoUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.service.SystemService;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.modules.sys.utils.SeqUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderModifyService;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderProcessLogService;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderService;
import com.wolfking.jeesite.ms.cc.service.AbnormalFormService;
import com.wolfking.jeesite.ms.entity.AppPushMessage;
import com.wolfking.jeesite.ms.providermd.service.*;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.service.push.APPMessagePushService;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import com.wolfking.jeesite.ms.tmall.md.service.B2bCustomerMapService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wolfking.jeesite.modules.sd.utils.OrderUtils.ORDER_LOCK_EXPIRED;
import static java.util.Optional.ofNullable;

/**
 * 订单Service
 */
@Service
@Configurable
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class OrderService extends LongIDBaseService {

    @Resource
    protected OrderDao dao;
    @Autowired
    private OrderHeadDao orderHeadDao;
    @Resource
    private OrderAutoCompleteDao autoCompleteDao;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ServicePointService servicePointService;
    @Autowired
    private ServiceTypeService serviceTypeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private RedisUtilsLocal redisUtilsLocal;
    @Autowired
    private MSCustomerService msCustomerService;

    @Autowired
    private SmsMQSender smsMQSender;
    @Autowired
    private NoticeMessageSender noticeMessageSender;
    @Autowired
    private SmsCallbackTaskMQSender smsCallbackTaskMQSender;

    @Autowired
    private APPMessagePushService appMessagePushService;

    @Autowired
    private UrgentLevelService urgentLevelService;

    @Autowired
    private OrderVoiceTaskService orderVoiceTaskService;


    @Autowired
    private OrderAutoCompleteDelaySender orderAutoCompleteDelaySender;

    @Autowired
    B2BCenterOrderModifyService b2BCenterOrderModifyService;
    @Autowired
    private OrderMaterialService orderMaterialService;
    @SuppressWarnings("rawtypes")
    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private MapperFacade mapper;
    @Autowired
    private B2bCustomerMapService b2bCustomerMapService;

    @Autowired
    private B2BCenterOrderService b2BCenterOrderService;

    @Resource
    private OrderItemCompleteDao orderItemCompleteDao;
    @Autowired
    private OperateTaskMQSender operateTaskMQSender;

    @Autowired
    private NewTaskMQSender newTaskMQSender;

    @Autowired
    private OrderCacheReadService orderCacheReadService;

    @Autowired
    private ServicePointOrderBusinessService servicePointOrderBusinessService;

    @Autowired
    private B2BCenterOrderProcessLogService b2BCenterOrderProcessLogService;

    @Autowired
    private OrderCrushService crushService;

    @Autowired
    private MSProductService msProductService;

    @Autowired
    private MSServicePointService msServicePointService;

    @Autowired
    private MSEngineerService msEngineerService;

    @Autowired
    private MSErrorTypeService msErrorTypeService;
    @Autowired
    private MSErrorCodeService msErrorCodeService;

    @Autowired
    private MSActionCodeService msActionCodeService;

    @Autowired
    private OrderOpitionTraceService orderOpitionTraceService;

    @Autowired
    private AbnormalFormService abnormalFormService;

    @Autowired
    private InsurancePriceService insurancePriceService;
    @Autowired
    private OrderLocationService orderLocationService;
    @Autowired
    private OrderAdditionalInfoService orderAdditionalInfoService;
    @Autowired
    private AuxiliaryMaterialMasterDao auxiliaryMaterialMasterDao;

    @Autowired
    private MSRegionPermissionNewService msRegionPermissionNewService;

    @Autowired
    private MSServicePointPriceService msServicePointPriceService;

    //不发短信的数据源设定
    private String smIgnoreDataSources;
    private boolean voiceEnabled;
    private String siteCode;

    @Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.smIgnoreDataSources = webProperties.getShortMessage().getIgnoreDataSources();
        this.voiceEnabled = webProperties.getVoiceService().getEnabled();
        this.siteCode = webProperties.getSite().getCode();
    }


    //切换为微服务
    public OrderStatus getOrderStatusById(Long orderId, String quarter, Boolean fromMasterDb) {
        OrderStatus orderStatus = dao.getOrderStatusById(orderId, quarter, fromMasterDb);
        if (orderStatus != null) {
            if (orderStatus.getCancelResponsible() != null && StringUtils.toInteger(orderStatus.getCancelResponsible().getValue()) > 0) {
                String cancelResponsibleLabel = MSDictUtils.getDictLabel(orderStatus.getCancelResponsible().getValue(), "cancel_responsible", "");
                orderStatus.getCancelResponsible().setLabel(cancelResponsibleLabel);
            }
            //user微服务
            List<Long> userIds = Lists.newArrayList();
            if (orderStatus.getCustomerApproveBy() != null && orderStatus.getCustomerApproveBy().getId() != null) {
                userIds.add(orderStatus.getCustomerApproveBy().getId());
            }
            if (orderStatus.getPlanBy() != null && orderStatus.getPlanBy().getId() != null) {
                userIds.add(orderStatus.getPlanBy().getId());
            }
            if (orderStatus.getCloseBy() != null && orderStatus.getCloseBy().getId() != null) {
                userIds.add(orderStatus.getCloseBy().getId());
            }
            if (orderStatus.getChargeBy() != null && orderStatus.getChargeBy().getId() != null) {
                userIds.add(orderStatus.getChargeBy().getId());
            }
            if (orderStatus.getCancelApplyBy() != null && orderStatus.getCancelApplyBy().getId() != null) {
                userIds.add(orderStatus.getCancelApplyBy().getId());
            }
            if (orderStatus.getCancelApproveBy() != null && orderStatus.getCancelApproveBy().getId() != null) {
                userIds.add(orderStatus.getCancelApproveBy().getId());
            }
            Map<Long, String> nameMap = MSUserUtils.getNamesByUserIds(userIds);
            String userName = null;
            if (!nameMap.isEmpty()) {
                if (orderStatus.getCustomerApproveBy() != null && orderStatus.getCustomerApproveBy().getId() != null) {
                    orderStatus.getCustomerApproveBy().setName(StringUtils.toString(nameMap.get(orderStatus.getCustomerApproveBy().getId())));
                }
                if (orderStatus.getPlanBy() != null && orderStatus.getPlanBy().getId() != null) {
                    orderStatus.getPlanBy().setName(StringUtils.toString(nameMap.get(orderStatus.getPlanBy().getId())));
                }
                if (orderStatus.getCloseBy() != null && orderStatus.getCloseBy().getId() != null) {
                    orderStatus.getCloseBy().setName(StringUtils.toString(nameMap.get(orderStatus.getCloseBy().getId())));
                }
                if (orderStatus.getChargeBy() != null && orderStatus.getChargeBy().getId() != null) {
                    orderStatus.getChargeBy().setName(StringUtils.toString(nameMap.get(orderStatus.getChargeBy().getId())));
                }
                if (orderStatus.getCancelApplyBy() != null && orderStatus.getCancelApplyBy().getId() != null) {
                    orderStatus.getCancelApplyBy().setName(StringUtils.toString(nameMap.get(orderStatus.getCancelApplyBy().getId())));
                }
                if (orderStatus.getCancelApproveBy() != null && orderStatus.getCancelApproveBy().getId() != null) {
                    orderStatus.getCancelApproveBy().setName(StringUtils.toString(nameMap.get(orderStatus.getCancelApproveBy().getId())));
                }
            }
        }
        return orderStatus;
    }

    //切换为微服务
    public Order getOrderByIdMS(Long orderId, String quarter) {
        Order order = orderHeadDao.getOrderById(orderId, quarter);//2020-12-19 sd_order -> sd_order_head
        if (order == null) {
            return order;
        }
        //2020-12-19 sd_order -> sd_order_head end
        order.setItems(OrderItemUtils.pbToItems(order.getItemsPb()));
        order.setOrderAdditionalInfo(OrderAdditionalInfoUtils.pbBypesToAdditionalInfo(order.getAdditionalInfoPb()));
        //end
        OrderItemUtils.setOrderItemProperties(order.getItems(), Sets.newHashSet(CacheDataTypeEnum.SERVICETYPE, CacheDataTypeEnum.PRODUCT, CacheDataTypeEnum.EXPPRESSCOMPANY));
        order.setOrderInfo("");//必须设置为空，因为工单缓存读写用到该字段(该字段原来没有被从数据库读取)

        if (order.getOrderType() != null && Integer.parseInt(order.getOrderType().getValue()) > 0) {
            Dict orderTypeDict = MSDictUtils.getDictByValue(order.getOrderType().getValue(), "order_type");
            order.getOrderType().setLabel(orderTypeDict == null ? "" : orderTypeDict.getLabel());
        }
        Dict dataSource;
        if (order.getDataSource() == null || order.getDataSource().getIntValue() == 0) {
            dataSource = MSDictUtils.getDictByValue("1", Order.ORDER_DATA_SOURCE_TYPE);//快可立
        } else {
            dataSource = MSDictUtils.getDictByValue(order.getDataSource().getValue(), Order.ORDER_DATA_SOURCE_TYPE);
        }
        order.getDataSource().setLabel(dataSource == null ? "" : dataSource.getLabel());
        //shop/店铺
        if (order.getDataSource().getIntValue() >= B2BDataSourceEnum.KKL.id && order.getB2bShop() != null && StringUtils.isNotBlank(order.getB2bShop().getShopId())) {
            B2bCustomerMap b2bCustomerMap = b2bCustomerMapService.getShopInfo(order.getDataSource().getIntValue(), order.getB2bShop().getShopId());
            if (b2bCustomerMap != null) {
                order.setB2bShop(b2bCustomerMap);
            }
        }
        //销售渠道
        if (order.getOrderChannel() != null && order.getOrderChannel().getIntValue() > 0) {
            int channelValue = order.getOrderChannel().getIntValue();
            if (channelValue == 1) {
                order.getOrderChannel().setLabel("线下");
            } else {
                Dict channel = MSDictUtils.getDictByValue(order.getOrderChannel().getValue(), "sale_channel");
                if (channel != null) {
                    order.setOrderChannel(channel);
                }
            }
        }
        return order;
    }

    /**
     * 从主库读取派单时预设的费用
     * 因主从等原因，造成上门时从库数据不一定生效
     */
    public OrderFee getPresetFeeWhenPlanFromMasterDB(Long orderId,String quarter){
        return dao.getPresetFeeWhenPlanFromMasterDB(orderId,quarter);
    }

    /**
     * 订单费用
     */
    public OrderFee getOrderFeeById(Long orderId, String quarter, boolean fromMasterDb) {
        //切换为微服务
        OrderFee orderFee = dao.getOrderFeeById(orderId, quarter, fromMasterDb);
        if (orderFee != null) {
            Map<String, Dict> paymentTypeMap = MSDictUtils.getDictMap("PaymentType");
            if (orderFee.getOrderPaymentType() != null && Integer.parseInt(orderFee.getOrderPaymentType().getValue()) > 0) {
                orderFee.setOrderPaymentType(paymentTypeMap.get(orderFee.getOrderPaymentType().getValue()));
            }
            if (orderFee.getEngineerPaymentType() != null && Integer.parseInt(orderFee.getEngineerPaymentType().getValue()) > 0) {
                orderFee.setEngineerPaymentType(paymentTypeMap.get(orderFee.getEngineerPaymentType().getValue()));
            }
        }
        return orderFee;
    }

    /**
     * 网点费用汇总
     *
     * @param orderId
     * @param quarter
     * @param servicePointId
     * @return
     */
    public OrderServicePointFee getOrderServicePointFee(Long orderId, String quarter, Long servicePointId) {
        return dao.getOrderServicePointFee(quarter, orderId, servicePointId);
    }

    /**
     * 网点费用汇总
     *
     * @param orderId
     * @param quarter
     * @return
     */
    public List<OrderServicePointFee> getOrderServicePointFees(Long orderId, String quarter, boolean fromMasterDb) {
        List<OrderServicePointFee> fees = dao.getOrderServicePointFees(quarter, orderId, fromMasterDb);
        OrderServicePointFee fee;
        ServicePoint servicePoint;
        if (fees != null && fees.size() > 0) {
            for (int i = 0, size = fees.size(); i < size; i++) {
                fee = fees.get(i);
                servicePoint = servicePointService.getFromCache(fee.getServicePoint().getId());
                fee.setServicePoint(servicePoint);
            }
        }
        return fees;
    }

    //根据订单号获得订单id及分片
    public Order getOrderIdByNo(String orderNo, String quarter) {
        Map<String, Object> map = orderHeadDao.getOrderIdByNo(orderNo, quarter);
        if (map == null || map.size() == 0) {
            return null;
        }
        Order order = new Order();
        order.setId((Long) map.get("id"));
        order.setQuarter((String) map.get("quarter"));
        return order;
    }

    /**
     * 读取订单当前网点id
     *
     * @param orderId
     * @param quarter
     * @return
     */
    public Long getCurrentServicePointId(Long orderId, String quarter) {
        return dao.getCurrentServicePointId(orderId, quarter);
    }


    /*从主库读取 2017/11/19*/
    public OrderCondition getOrderConditionFromMasterById(Long orderId, String quarter) {
        //切换为微服务
        OrderCondition orderCondition = dao.getOrderConditionFromMasterById(orderId, quarter);
        // add on 2019-9-25 begin
        //ServicePoint微服务
        if (orderCondition != null && orderCondition.getServicePoint() != null && orderCondition.getServicePoint().getId() != null) {
            ServicePoint servicePoint = msServicePointService.getById(orderCondition.getServicePoint().getId());
            if (servicePoint != null) {
                orderCondition.getServicePoint().setServicePointNo(servicePoint.getServicePointNo());
                orderCondition.getServicePoint().setName(servicePoint.getName());
            }
        }
        // add on 2019-10-28 begin
        if (orderCondition != null && orderCondition.getEngineer() != null && orderCondition.getEngineer().getId() != null) {
            Engineer engineer = msEngineerService.getByIdFromCache(orderCondition.getEngineer().getId());
            if (engineer != null) {
                orderCondition.getEngineer().setName(engineer.getName());
                orderCondition.getEngineer().setMobile(engineer.getContactInfo());
            }
        }
        // add on 2019-10-28 end
        // add on 2019-9-25 end
        if (orderCondition.getPendingType() != null && Integer.parseInt(orderCondition.getPendingType().getValue()) > 0) {
            String pendingTypeLabel = MSDictUtils.getDictLabel(orderCondition.getPendingType().getValue(), "PendingType", "");
            orderCondition.getPendingType().setLabel(pendingTypeLabel);
        }
        if (orderCondition.getStatus() != null && Integer.parseInt(orderCondition.getStatus().getValue()) > 0) {
            String orderStatusLabel = MSDictUtils.getDictLabel(orderCondition.getStatus().getValue(), "order_status", "");
            orderCondition.getStatus().setLabel(orderStatusLabel);
        }
        if (orderCondition.getUrgentLevel() == null || orderCondition.getUrgentLevel().getId().longValue() == 0) {
            orderCondition.setUrgentLevel(new UrgentLevel(0l, "不加急"));
        } else {
            UrgentLevel urgentLevel = urgentLevelService.getFromCache(orderCondition.getUrgentLevel().getId());
            if (urgentLevel == null) {
                urgentLevel = orderCondition.getUrgentLevel();
                urgentLevel.setRemarks("读取错误");
            }
            orderCondition.setUrgentLevel(urgentLevel);
        }
        //客户微服务
        if (orderCondition.getCustomer() != null && orderCondition.getCustomer().getId() != null) {
            Customer customer = msCustomerService.getByIdToCustomer(orderCondition.getCustomer().getId());
            if (customer != null && customer.getId() != null && customer.getId() > 0) {
                orderCondition.setCustomer(customer);
            }
        }
        //user微服务
        List<Long> userIds = Lists.newArrayList();
        if (orderCondition.getCustomer() != null && orderCondition.getCustomer().getSales() != null && orderCondition.getCustomer().getSales().getId() != null) {
            userIds.add(orderCondition.getCustomer().getSales().getId());
        }
        if (orderCondition.getCreateBy() != null && orderCondition.getCreateBy().getId() != null) {
            userIds.add(orderCondition.getCreateBy().getId());
        }
        if (orderCondition.getUpdateBy() != null && orderCondition.getUpdateBy().getId() != null) {
            userIds.add(orderCondition.getUpdateBy().getId());
        }
        if (orderCondition.getKefu() != null && orderCondition.getKefu().getId() != null) {
            userIds.add(orderCondition.getKefu().getId());
        }
        Map<Long, User> userMap = MSUserUtils.getMapByUserIds(userIds);
        User user = null;
        if (!userMap.isEmpty()) {
            if (orderCondition.getCustomer() != null && orderCondition.getCustomer().getSales() != null && orderCondition.getCustomer().getSales().getId() != null) {
                user = userMap.get(orderCondition.getCustomer().getSales().getId());
                if (user != null) {
                    orderCondition.getCustomer().setSales(user);
                }
            }
            if (orderCondition.getCreateBy() != null && orderCondition.getCreateBy().getId() != null) {
                user = userMap.get(orderCondition.getCreateBy().getId());
                if (user != null) {
                    orderCondition.setCreateBy(user);
                }
            }
            if (orderCondition.getUpdateBy() != null && orderCondition.getUpdateBy().getId() != null) {
                user = userMap.get(orderCondition.getUpdateBy().getId());
                if (user != null) {
                    orderCondition.setUpdateBy(user);
                }
            }
            if (orderCondition.getKefu() != null && orderCondition.getKefu().getId() != null) {
                user = userMap.get(orderCondition.getKefu().getId());
                if (user != null) {
                    orderCondition.setKefu(user);
                }
            }
        }

        return orderCondition;
    }

    /**
     * 读取订单信息
     *
     * @param orderId    订单id
     * @param level      读取订单内容类别（见OrderUtils.OrderDataLevel）
     * @param cacheFirst 缓存优先
     * @return
     */
    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst) {
        return getOrderById(orderId, quarter, level, cacheFirst, false);
    }

    /**
     * 读取订单信息
     *
     * @param orderId      订单id
     * @param level        读取订单内容类别（见OrderUtils.OrderDataLevel）
     * @param cacheFirst   缓存优先
     * @param fromMasterDb 数据读取主库(包含condition部分，fee，details)
     * @return
     */
    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst, boolean fromMasterDb) {
        return orderCacheReadService.getOrderById(orderId, quarter, level, cacheFirst, fromMasterDb);
    }

    /**
     * 读取订单信息
     *
     * @param orderId      订单id
     * @param level        读取订单内容类别（见OrderUtils.OrderDataLevel）
     * @param cacheFirst   缓存优先
     * @param fromMasterDb 数据读取主库(包含condition部分，fee，details)
     * @param loadCategory 是否读取产品品类信息
     * @return
     */
    public Order getOrderById(Long orderId, String quarter, OrderUtils.OrderDataLevel level, boolean cacheFirst, boolean fromMasterDb,boolean loadCategory) {
        return orderCacheReadService.getOrderById(orderId, quarter, level, cacheFirst, fromMasterDb,loadCategory);
    }

    /**
     * 修改订单Condition
     * 使用hashmap传值，不存在的key在mybatis里为null
     */
    @Transactional(readOnly = false)
    public void updateOrderCondition(HashMap<String, Object> map) {
        dao.updateCondition(map);
    }

    /**
     * 更新催单信息
     */
    @Transactional
    public int updateReminderInfo(HashMap<String, Object> map) {
        dao.updateReminderInfo(map);
        int rtn = dao.updateConditionReminderFlag(map);
        //同步网点工单数据
        //有网点才同步
        Long spId = (Long) map.get("servicePointId");
        if (spId != null && spId > 0) {
            servicePointOrderBusinessService.relatedForm(
                    (long) map.get("orderId"),
                    (String) map.get("quarter"),
                    (int) map.get("reminderStatus"),
                    0,
                    0,
                    (long) map.get("reminderCreateBy"),
                    (long) map.get("reminderCreateAt")
            );
        }
        return rtn;
    }

    //切换为微服务
    public List<OrderDetail> getOrderDetails(Long orderId, String quarter, Boolean fromMasterDb) {
        List<OrderDetail> orderDetailList = dao.getOrderDetails(orderId, quarter, fromMasterDb);
        if (orderDetailList != null && orderDetailList.size() > 0) {
            // add on 2019-8-23 begin
            // product微服务
            Product product = new Product();
            List<Product> productList = msProductService.findListByConditions(product);
            Map<Long, Product> productMap = Maps.newHashMap();
            if (productList != null && productList.size() > 0) {
                productMap = productList.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
            }
            // add on 2019-8-23 end

            // add on 2019-9-25 begin
            // 获取网点的数据
            List<Long> servicePointIds = orderDetailList.stream().map(r -> r.getServicePoint().getId()).distinct().collect(Collectors.toList());
            // add on 2019-9-25 end

            // add on 2019-10-12 begin
            Map<Long, String> servicePointMap = MDUtils.getServicePointNamesByIds(servicePointIds);
            // add on 2019-10-12 end

            Map<String, Dict> paymentTypeMap = MSDictUtils.getDictMap("PaymentType");

            //获取服务类型调用微服务 add om 2019-10-15 key:服务类型id,value：服务名称
            Map<Long, String> serviceTypeMap = serviceTypeService.findAllIdsAndNames();
            // end


            // add on 2019-10-28 begin
            List<Long> engineerIds = orderDetailList.stream().map(r -> r.getEngineer().getId()).distinct().collect(Collectors.toList());
            Map<Long, String> engineerMap = MDUtils.getEngineerNamesByIds(engineerIds);
            // add on 2019-10-28 end
            for (OrderDetail orderDetail : orderDetailList) {
                if (orderDetail.getEngineerPaymentType() != null && Integer.parseInt(orderDetail.getEngineerPaymentType().getValue()) > 0) {
                    orderDetail.setEngineerPaymentType(paymentTypeMap.get(orderDetail.getEngineerPaymentType().getValue()));
                }
                // add on 2019-8-23 begin
                Product productEntity = productMap.get(orderDetail.getProduct().getId());
                if (productEntity != null) {
                    orderDetail.getProduct().setName(productEntity.getName());
                    orderDetail.getProduct().setCategory(productEntity.getCategory());
                    orderDetail.getProduct().setSetFlag(productEntity.getSetFlag());
                }
                // add on 2019-8-23 end
                orderDetail.getServicePoint().setName(servicePointMap.get(orderDetail.getServicePoint().getId()));  // add on 2019-9-25

                //服务类型调用服务 add on 2019-10-15
                String serviceName = serviceTypeMap.get(orderDetail.getServiceType().getId());
                if (StringUtils.isNotBlank(serviceName)) {
                    orderDetail.getServiceType().setName(serviceName);
                }
                // end
                orderDetail.getEngineer().setName(engineerMap.get(orderDetail.getEngineer().getId()));  // add on 2019-10-28 //Engineer微服务
            }
            //维修故障信息
            getErrorInfoFromMS(orderId, orderDetailList);
        }
        return orderDetailList;
    }

    /**
     * 从微服务读取故障相关信息
     */
    private void getErrorInfoFromMS(Long orderId, List<OrderDetail> list) {
        try {
            OrderDetail detail;
            Dict dict;
            // 故障分类
            MDErrorType errorType = null;
            // 故障现象
            MDErrorCode errorCode = null;
            // 故障分析&处理
            MDActionCodeDto actionCode = null;
            long pid;
            Map<String, Dict> orderServiceTypesMap = Maps.newHashMap();
            String strValue;
            Long errorId;
            for (int i = 0, size = list.size(); i < size; i++) {
                detail = list.get(i);
                if (detail.getServiceCategory().getIntValue() > 0) {
                    if (orderServiceTypesMap.isEmpty()) {
                        orderServiceTypesMap = MSDictUtils.getDictMap(Dict.DICT_TYPE_ORDER_SERVICE_TYPE);
                    }
                    dict = orderServiceTypesMap.get(detail.getServiceCategory().getValue());
                    if (dict != null) {
                        detail.setServiceCategory(dict);
                    } else {
                        detail.setServiceCategory(new Dict(detail.getServiceCategory().getIntValue(), ""));
                    }
                }
                errorId = ofNullable(detail.getErrorType()).map(t -> t.getId()).orElse(0L);
                if (errorId <= 0) {
                    continue;
                }
                pid = detail.getProduct().getId();
                errorType = msErrorTypeService.getByProductIdAndId(pid, errorId);
                if (errorType == null) {
                    continue;
                }
                detail.setErrorType(errorType);

                errorId = ofNullable(detail.getErrorCode()).map(t -> t.getId()).orElse(0L);
                if (errorId <= 0) {
                    continue;
                }
                errorCode = msErrorCodeService.getByProductIdAndId(pid, errorId);
                if (errorCode == null) {
                    continue;
                }
                detail.setErrorCode(errorCode);

                errorId = ofNullable(detail.getActionCode()).map(t -> t.getId()).orElse(0L);
                if (errorId > 0) {
                    actionCode = msActionCodeService.getByProductIdAndId(pid, errorId);
                    if (actionCode != null) {
                        detail.setActionCode(actionCode);
                    }
                }
            }
        } catch (Exception e) {
            log.error("从微服务转载读取订单上门服务项目错误,id:{}", orderId, e);
        }
    }


    /**
     * 读取网点具体安维师傅的派单记录
     *
     * @param orderId        订单id
     * @param quarter        分片
     * @param servicePointId 网点id
     * @param engineerId     安维id
     * @return
     */
    public OrderPlan getOrderPlan(Long orderId, String quarter, Long servicePointId, Long engineerId) {
        return dao.getOrderPlan(orderId, quarter, servicePointId, engineerId);
    }


    @Transactional
    public void updateDetail(HashMap<String, Object> params) {
        dao.updateDetail(params);
    }

    /**
     * 修改网点费用
     */
    @Transactional
    public void updateOrderServicePointFeeByMaps(HashMap<String, Object> params) {
        dao.updateOrderServicePointFeeByMaps(params);
    }

    @Transactional
    public void updateFee(HashMap<String, Object> params) {
        dao.updateFee(params);
    }

    /**
     * 停滞原因
     */
    @Transactional(readOnly = false)
    public void appPendingOrder(Order o, String remarks, boolean isNeedSendToB2b) {
        OrderCondition order = o.getOrderCondition();
        User user = order.getCreateBy();
        String time = "";
        // 时间取整点时间
        if (order.getAppointmentDate() != null) {
            if (DateUtils.getYear(order.getAppointmentDate()) > 9999) {
                throw new OrderException("日期超出范围");
            }
            time = DateUtils.formatDate(order.getAppointmentDate(), "yyyy-MM-dd HH:00:00");
            try {
                Date date = DateUtils.parse(time, "yyyy-MM-dd HH:00:00");
                order.setAppointmentDate(date);
            } catch (java.text.ParseException e) {
                throw new OrderException("日期格式错误");
            }
        }
        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getOrderId());
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新页面。");
        }
        try {
            Date date = new Date();

            //log
            OrderProcessLog log = new OrderProcessLog();
            log.setQuarter(order.getQuarter());
            log.setAction("预约上门");
            log.setOrderId(order.getOrderId());
            log.setActionComment(StringUtils.left(order.getRemarks(), 250));
            log.setStatus(order.getStatus().getLabel());
            log.setStatusValue(order.getStatusValue());
            log.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            log.setCloseFlag(0);
            log.setCreateBy(user);
            log.setCreateDate(date);
            log.setCustomerId(order.getCustomerId());
            log.setDataSourceId(o.getDataSourceId());
            saveOrderProcessLogNew(log);

            HashMap<String, Object> params = Maps.newHashMapWithExpectedSize(15);
            //condition
            params.put("quarter", order.getQuarter());
            params.put("orderId", order.getOrderId());
            //预约
            Boolean isReservation = order.getPendingType().getValue().equalsIgnoreCase("3");
            //设置订单的subStatus，Add by Zhoucy
            Integer iPendingType = StringUtils.toInteger(order.getPendingType().getValue());
            Date appointmentDate = order.getAppointmentDate();
            Date pendingTypeDate = order.getAppointmentDate();
            //如果预约时间在22点及以后，则将客服处理时间提前2小时（因为等工单预约到期时，客服已经下班了） Added by zhoucy
            if (DateUtils.getHourOfDay(pendingTypeDate) >= 22) {
                pendingTypeDate = DateUtils.addHour(pendingTypeDate, -2);
            }
            Date reservationDate = order.getAppointmentDate();
            Integer subStatus = Order.ORDER_SUBSTATUS_NEW;
            Dict pendingTypeDict = order.getPendingType();
            if (iPendingType == Order.PENDINGTYPE_FOLLOWING) {
                subStatus = null;
                pendingTypeDict = null;
                appointmentDate = null;
                reservationDate = null;
            } else if (iPendingType == Order.PENDINGTYPE_APPOINTED) {
                subStatus = Order.ORDER_SUBSTATUS_APPOINTED;
                params.put("reservationTimes", 1);
            } else if (iPendingType == Order.PENDINGTYPE_WAITINGPARTS) {
                subStatus = Order.ORDER_SUBSTATUS_WAITINGPARTS;
                params.put("reservationTimes", 1);
            } else {
                subStatus = Order.ORDER_SUBSTATUS_PENDING;
                params.put("reservationTimes", 1);
            }

            params.put("subStatus", subStatus);
            params.put("pendingType", pendingTypeDict);
            params.put("pendingTypeDate", pendingTypeDate);
            params.put("appointmentDate", appointmentDate);
            params.put("reservationDate", reservationDate);
            params.put("updateBy", user);
            params.put("updateDate", date);
            int appAbnormalyFlag = order.getAppAbnormalyFlag();
            if (1 == appAbnormalyFlag) {
                params.put("appAbnormalyFlag", appAbnormalyFlag);
            }
            dao.updateCondition(params);
            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getOrderId())
                    .setDeleteField(OrderCacheField.CONDITION)
                    .setDeleteField(OrderCacheField.PENDING_TYPE)
                    .setDeleteField(OrderCacheField.PENDING_TYPE_DATE);
            if (iPendingType != Order.PENDINGTYPE_FOLLOWING) {
                builder.setDeleteField(OrderCacheField.RESERVATION_TIMES)
                        .setDeleteField(OrderCacheField.APPOINTMENT_DATE);
                if (isReservation) {
                    builder.setDeleteField(OrderCacheField.RESERVATION_DATE);
                }
            }
            OrderCacheUtils.update(builder.build());

            Long servicePointId = order.getServicePoint() == null ? null : order.getServicePoint().getId();
            //region B2B消息队列
            //从派单处移到此处
            if (o.getDataSource() != null && iPendingType != Order.PENDINGTYPE_FOLLOWING && isNeedSendToB2b) {
                Date effectiveDate = order.getAppointmentDate();
                //APP的预约时间若在22点及以后，则发给B2B的预约时间增加18个小时
                //status -> 3
                Long engineerId = order.getEngineer() == null ? null : order.getEngineer().getId();
                b2BCenterOrderService.pendingOrder(o, servicePointId, engineerId, iPendingType, effectiveDate, user, date, remarks);
            }
            //endregion B2B消息队列

            //region 网点订单数据更新 2019-03-25
            servicePointOrderBusinessService.pending(order.getOrderId(), order.getQuarter(),
                    servicePointId, subStatus, pendingTypeDict.getIntValue(),
                    appointmentDate.getTime(), reservationDate.getTime(), appAbnormalyFlag,
                    user.getId(), date.getTime());
            //endregion

        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.appPendingOrder]orderId:{} ,pendintType:{} ,date:{} ,remarks:{}", order.getOrderId(), order.getPendingType().getLabel(), time, order.getRemarks(), e);
            throw new OrderException("保存错误", e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

    /**
     * 根据上门服务重新统计费用和上门次数
     *
     * @param details
     * @return
     */
    public HashMap<String, Object> recountFee(List<OrderDetail> details) {
        //应收
        Double serviceCharge = 0.0;
        Double materialCharge = 0.0;
        Double expressCharge = 0.0;
        Double travelCharge = 0.0;
        Double otherCharge = 0.0;
        Double orderCharge = 0.0;//合计
        //应付
        Double engineerServiceCharge = 0.0;
        Double engineerMaterialCharge = 0.0;
        Double engineerExpressCharge = 0.0;
        Double engineerTravelCharge = 0.0;
        Double engineerOtherCharge = 0.0;
        Double engineerTotalCharge = 0.0;//合计
        Integer serviceTimes = 0;
        OrderDetail d;
//        System.out.println(">>>detials count: " + details.size());
        for (int i = 0, size = details.size(); i < size; i++) {
            d = details.get(i);
            if (d.getDelFlag() == OrderDetail.DEL_FLAG_DELETE) {
                continue;
            }
//            System.out.println(
//                    String.format(">>>index:%s charge:%s materil charge:%s express:%s travel:%s other:%s "
//                            ,i
//                            ,d.getCharge()
//                            ,d.getMaterialCharge()
//                            ,d.getExpressCharge()
//                            ,d.getTravelCharge()
//                            ,d.getOtherCharge()
//                    ));
//            System.out.println(
//                    String.format(">>>Engineer charge:%s materil charge:%s express:%s travel:%s other:%s "
//                            ,d.getEngineerServiceCharge()
//                            ,d.getEngineerMaterialCharge()
//                            ,d.getEngineerExpressCharge()
//                            ,d.getEngineerTravelCharge()
//                            ,d.getEngineerOtherCharge()
//                    ));
            //取得有效记录中最大的上门服务次数
            if (serviceTimes < d.getServiceTimes()) {
                serviceTimes = d.getServiceTimes();
            }
            // 应收
            serviceCharge = serviceCharge + d.getCharge();//服务费
            materialCharge = materialCharge + d.getMaterialCharge();//配件费
            expressCharge = expressCharge + d.getExpressCharge();//快递费
            travelCharge = travelCharge + d.getTravelCharge();//远程费
            otherCharge = otherCharge + d.getOtherCharge();//其它

            // 应付
            engineerServiceCharge = engineerServiceCharge + d.getEngineerServiceCharge();//服务费
            engineerMaterialCharge = engineerMaterialCharge + d.getEngineerMaterialCharge();//配件费
            engineerExpressCharge = engineerExpressCharge + d.getEngineerExpressCharge();//快递费
            engineerTravelCharge = engineerTravelCharge + d.getEngineerTravelCharge();//远程费
            engineerOtherCharge = engineerOtherCharge + d.getEngineerOtherCharge();//其它

        }
        //应收合计
        orderCharge = serviceCharge
                + materialCharge
                + expressCharge
                + travelCharge
                + otherCharge;
        //应付合计
        engineerTotalCharge = engineerServiceCharge
                + engineerMaterialCharge
                + engineerExpressCharge
                + engineerTravelCharge
                + engineerOtherCharge;

        HashMap<String, Object> shResult = Maps.newHashMap();
        shResult.put("serviceTimes", serviceTimes);
        //应收
        shResult.put("serviceCharge", serviceCharge);
        shResult.put("materialCharge", materialCharge);
        shResult.put("expressCharge", expressCharge);
        shResult.put("travelCharge", travelCharge);
        shResult.put("otherCharge", otherCharge);
        shResult.put("orderCharge", orderCharge);
        //应付
        shResult.put("engineerServiceCharge", engineerServiceCharge);
        shResult.put("engineerMaterialCharge", engineerMaterialCharge);
        shResult.put("engineerExpressCharge", engineerExpressCharge);
        shResult.put("engineerTravelCharge", engineerTravelCharge);
        shResult.put("engineerOtherCharge", engineerOtherCharge);
        shResult.put("engineerTotalCharge", engineerTotalCharge);

//        System.out.println("serviceCharge:" + charge);
//        System.out.println("engineerTotalCharge:" + engineerTotalCharge);
        return shResult;
    }

    /**
     * 修改上门服务
     */
    @Transactional
    public void editDetail(HashMap<String, Object> params) {
        dao.editDetail(params);
    }


    /**
     * 重新计算费用，只计算本次上门服务项目
     * 循环计价，厂商取最高价，网点取最低价
     */
    public void rechargeOrder(List<OrderDetail> list, OrderDetail detail) {
        if (list == null || list.size() == 0) {
            return;
        }
        //本次上门，删除标记不等于1
        List<OrderDetail> items;
        OrderDetail m;
        int size;
        //1.厂商,先去标准价最高，标准价相同，取折扣价最低的
        Double cprice = 0.0;
        items = list.stream()
                .filter(t -> t.getServiceTimes() == detail.getServiceTimes() && t.getDelFlag().intValue() != 1)
                .sorted(Comparator.comparingDouble(OrderDetail::getStandPrice).reversed()
                        .thenComparingDouble(OrderDetail::getDiscountPrice))
                .collect(Collectors.toList());
        if (items.size() == 0) {
            return;
        }
        size = items.size();
        for (int i = 0; i < size; i++) {
            m = items.get(i);
            if (i == 0) {
                m.setCharge(m.getStandPrice() + m.getDiscountPrice() * (m.getQty() - 1));
            } else {
                m.setCharge(m.getDiscountPrice() * m.getQty());
            }
            cprice = cprice + m.getCharge();
        }

        //2.网点取价，循环累计，取最低价
        items = list.stream()
                .filter(t -> t.getServiceTimes() == detail.getServiceTimes() && t.getDelFlag().intValue() != 1)
                .collect(Collectors.toList());
        size = items.size();
        Double sprice = 0.0;
        Map<Integer, Double> servprices = Maps.newHashMap();//网点
        //1.循环累计，当前行取标准加，其余取折扣加
        for (int i = 0; i < size; i++) {
            sprice = 0.0;

            for (int j = 0; j < size; j++) {
                m = items.get(j);
                if (i == j) {
                    sprice = sprice + m.getEngineerStandPrice() + m.getEngineerDiscountPrice() * (m.getQty() - 1);
                } else {
                    sprice = sprice + m.getEngineerDiscountPrice() * m.getQty();
                }
            }
            servprices.put(i, sprice);
        }
        //2.取最低价
        int sidx = 0;
        sprice = servprices.values().stream().min(Comparator.comparingDouble(Double::doubleValue)).get();
        for (int j = 0; j < size; j++) {
            if (servprices.get(j).equals(sprice)) {
                sidx = j;
                break;
            }
        }

        //计价费用
        for (int i = 0; i < size; i++) {
            m = items.get(i);
            //网点
            if (i == sidx) {
                m.setEngineerServiceCharge(m.getEngineerStandPrice() + m.getEngineerDiscountPrice() * (m.getQty() - 1));
            } else {
                m.setEngineerServiceCharge(m.getEngineerDiscountPrice() * m.getQty());
            }
        }
        items = null;
    }

    /**
     * 派单及接单时，计算网点预估服务费用
     * 循环计价，取最低价
     */
    public Double calcServicePointCost(ServicePoint servicePoint, List<OrderItem> list) {
        if (list == null || list.size() == 0) {
            return 0.0d;
        }
        Long servicePointId = servicePoint.getId();
        List<OrderDetail> items = mapper.mapAsList(list, OrderDetail.class);
        //使用新的网点价格读取方法 2020-03-07
        List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> nameValuePairs = getOrderDetailProductAndServiceTypePairs(items);
        if (CollectionUtils.isEmpty(nameValuePairs)) {
            throw new OrderException("确认订单服务项目失败");
        }
        Map<String, ServicePrice> priceMap = servicePointService.getPriceMapByProductsFromCache(servicePointId, nameValuePairs);
        if (priceMap == null) {
            throw new OrderException("网点价格读取失败，请重试");
        }
        if (CollectionUtils.isEmpty(priceMap)) {
            OrderDetail orderDetail = items.get(0);
            if (orderDetail != null && orderDetail.getProduct() != null && orderDetail.getServiceType() != null) {
                throw new OrderException(String.format("网点[%s] 产品[%s] 未维护服务[%s]的价格", servicePoint.getName(), orderDetail.getProduct().getName(), orderDetail.getServiceType().getName()));
            } else {
                throw new OrderException("网点价格读取失败，没维护网点价格");
            }
        }

        OrderDetail m;
        int size;
        Double sprice = 0.0;
        size = items.size();
        //网点取价，循环累计，取最低价
        Map<Integer, Double> servprices = Maps.newHashMap();//网点
        ServicePrice price;
        //1.循环累计，当前行取标准加，其余取折扣加
        for (int i = 0; i < size; i++) {
            sprice = 0.0;
            for (int j = 0; j < size; j++) {
                m = items.get(j);
                final Long productId = m.getProduct().getId();
                final Long serviceTypeId = m.getServiceType().getId();
                price = priceMap.get(String.format("%d:%d", productId, serviceTypeId));
                /*
                price = streamSupplier.get().filter(t -> t.getProduct().getId().equals(productId)
                        && t.getServiceType().getId().equals(serviceTypeId) && t.getDelFlag() == 0)
                        .findFirst().orElse(null);
                */
                if (price == null) {
                    throw new OrderException(String.format("网点:%s 未定义产品:%s 未定义：%s 的价格", servicePoint.getName(), m.getProduct().getName(), m.getServiceType().getName()));
                }
                if (i == j) {
                    sprice = sprice + price.getPrice() + price.getDiscountPrice() * (m.getQty() - 1);
                } else {
                    sprice = sprice + price.getDiscountPrice() * m.getQty();
                }
            }
            servprices.put(i, sprice);
        }
        //2.取最低价
        int sidx = 0;
        sprice = servprices.values().stream().min(Comparator.comparingDouble(Double::doubleValue)).get();
        return sprice;
    }

    /**
     * 确认上门 （订单必须已经派单或接单）
     * 客服操作，直接自动添加所有的上门服务
     *
     * @param orderId     订单id
     * @param quarter     分片
     * @param user        操作人
     * @param confirmType 确认上门类型 0-客服 1-安维
     */
    @Transactional(readOnly = false)
    public void confirmDoorAuto(Long orderId, String quarter, User user, int confirmType) {
        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, orderId);
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("错误：此订单正在处理中，请稍候重试，或刷新订单。");
        }
        try {
            Order o = getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.DETAIL, true);
            if (o == null) {
                throw new OrderException("错误：读取订单信息失败");
            }
            if (!o.canService()) {
                throw new OrderException("错误：不能确认上门，请确认订单状态.");
            }
            if (o.getOrderCondition().getAppointmentDate() == null) {
                throw new OrderException("错误：没有设置预约时间，不允许直接确认上门.");
            }
            Date date = new Date();
            if (o.getOrderCondition().getAppointmentDate().getTime() > DateUtils.getEndOfDay(date).getTime()) {
                throw new OrderException("预约时间与当前不一致，请重新预约！");
            }
            if (o.getOrderCondition().getOrderServiceType() == OrderUtils.OrderTypeEnum.BACK.getId()
                    || o.getOrderCondition().getOrderServiceType() == OrderUtils.OrderTypeEnum.EXCHANGE.getId()) {
                throw new OrderException("错误：退货或换货工单请联系客服处理！");
            }
            //2020-09-24 接入云米，增加经纬度检查
            AjaxJsonEntity locationCheckResult = checkAddressLocation(o.getDataSource().getIntValue(),orderId,o.getQuarter());
            if(!locationCheckResult.getSuccess()){
                throw new OrderException("因"+locationCheckResult.getMessage() + "，不能确认上门。");
            }
            OrderCondition condition = o.getOrderCondition();
            //网点费用表
            Long servicePointId = condition.getServicePoint().getId();
            Long engineerId = condition.getEngineer() == null ? null : condition.getEngineer().getId();
            OrderServicePointFee orderServicePointFee = getOrderServicePointFee(orderId, o.getQuarter(), servicePointId);
            int dataSourceId = o.getDataSourceId();
            int prevStatus = condition.getStatusValue();
            List<OrderDetail> details = o.getDetailList();
            if (details == null) {
                details = Lists.newArrayList();
            }
            //上门服务
            details = details.stream()
                    .filter(t -> t.getDelFlag() == 0)
                    .collect(Collectors.toList());
            HashMap<String, Object> params = Maps.newHashMap();


            // 如果订单中已经有添加当前安维网点的上门服务就不再添加
            // 只记录log
            if (details.size() > 0) {
                OrderDetail detail = details.stream()
                        .filter(t -> t.getDelFlag() == 0
                                && Objects.equals(t.getServicePoint().getId(), condition.getServicePoint().getId())
                        )
                        .findFirst()
                        .orElse(null);
                if (detail != null) {
                    //log
                    OrderProcessLog processLog = new OrderProcessLog();
                    processLog.setQuarter(o.getQuarter());
                    processLog.setAction("确认上门");
                    processLog.setOrderId(orderId);
                    processLog.setActionComment(String.format("%s%s", confirmType == 0 ? "客服" : "安维", "确认上门"));
                    processLog.setStatus(condition.getStatus().getLabel());
                    processLog.setStatusValue(condition.getStatusValue());
                    processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
                    processLog.setCloseFlag(0);
                    processLog.setCreateBy(user);
                    processLog.setCreateDate(date);
                    processLog.setCustomerId(condition.getCustomerId());
                    processLog.setDataSourceId(dataSourceId);
                    saveOrderProcessLogNew(processLog);

                    //cache,淘汰
                    OrderCacheUtils.delete(orderId);

                    //add by Zhoucy 2018-9-3 19:50 重复确认上门也修改子状态及相关时间
                    params.clear();
                    params.put("quarter", o.getQuarter());
                    params.put("orderId", orderId);
                    /* 一键添加上门服务时：sub_status=50、pending_type_date = reservation_date = now、pending_type = 0， Add by Zhoucy*/
                    params.put("pendingType", new Dict("0", ""));
                    params.put("subStatus", Order.ORDER_SUBSTATUS_SERVICED);
                    params.put("pendingTypeDate", date);
                    params.put("reservationDate", date);
                    params.put("updateBy", user);
                    params.put("updateDate", date);
                    dao.updateCondition(params);

                    // 2019-03-25 网点订单数据更新
                    servicePointOrderBusinessService.confirmOnSiteService(orderId, o.getQuarter(), servicePointId, engineerId, prevStatus, Order.ORDER_SUBSTATUS_SERVICED, user.getId(), date.getTime());

                    //region B2B消息队列
                    if (o.getDataSourceId() == B2BDataSourceEnum.VIOMI.id
                            || o.getDataSourceId() == B2BDataSourceEnum.INSE.id) {
                        Long pointId = condition.getServicePoint() != null ? condition.getServicePoint().getId() : null;
                        b2BCenterOrderService.serviceOrder(o, pointId, engineerId, user, date);
                    }
                    return;
                }
            }

            OrderFee orderFee = o.getOrderFee();
            if(orderFee == null){
                throw new OrderException("错误：读取订单费用汇总数据失败！");
            }
            //2020-10-21 从主库读取派单时预设的费用和单号
            OrderFee feeMaster = getPresetFeeWhenPlanFromMasterDB(orderId,o.getQuarter());
            if(feeMaster == null){
                log.error("读取派单预设费用失败,orderId:{} , quarter:{}",orderId,o.getQuarter());
                throw new OrderException("错误：读取派单预设费用失败！");
            }
            orderFee.setPlanTravelCharge(feeMaster.getPlanTravelCharge());
            orderFee.setPlanTravelNo(feeMaster.getPlanTravelNo());
            orderFee.setPlanDistance(feeMaster.getPlanDistance());
            orderFee.setCustomerPlanTravelCharge(feeMaster.getCustomerPlanTravelCharge());
            orderFee.setPlanOtherCharge(feeMaster.getPlanOtherCharge());
            orderFee.setCustomerPlanOtherCharge(feeMaster.getCustomerPlanOtherCharge());

            // 确认上门改变订单的状态
            Dict status = new Dict();
            status.setValue(Order.ORDER_STATUS_SERVICED.toString());
            status.setLabel(MSDictUtils.getDictLabel(status.getValue(), "order_status", "已上门"));//切换为微服务

            Boolean firstService = true;//首次上门

            if (details.size() > 0) {
                firstService = false;
            }

            //以下代码，当前网点没有上门过，自动添加上门服务，有可能是二次上门
            //检查当前安维的付款方式
            Dict engineerPaymentType = orderFee.getEngineerPaymentType();
            if (engineerPaymentType == null || engineerPaymentType.getIntValue() <= 0) {
                //throw new OrderException(String.format("订单中安维网点：%s 的付款方式未设定", condition.getServicePoint().getName()));
                ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
                if (servicePoint != null && servicePoint.getFinance() != null
                        && servicePoint.getFinance().getPaymentType() != null
                        && servicePoint.getFinance().getPaymentType().getIntValue() > 0) {
                    engineerPaymentType = servicePoint.getFinance().getPaymentType();
                } else {
                    throw new OrderException(String.format("确认网点：%s 结算方式失败", condition.getServicePoint().getName()));
                }
            }
            Dict orderPaymentType = orderFee.getOrderPaymentType();
            if (orderPaymentType == null || StringUtils.isBlank(orderPaymentType.getValue())) {
                throw new OrderException(String.format("订单中客户：%s 的付款方式未设定", condition.getCustomer().getName()));
            }
            //Customer Price
            List<CustomerPrice> customerPrices = customerService.getPricesFromCache(condition.getCustomer().getId());
            if (customerPrices == null || customerPrices.size() == 0) {
                throw new OrderException(String.format("读取客户：%s价格失败", condition.getCustomer().getName()));
            }
            List<OrderItem> items = o.getItems();
            //ServicePoint Price
            //使用新的网点价格读取方法 2020-03-07
            List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> nameValuePairs = getOrderItemProductAndServiceTypePairs(items);
            if (CollectionUtils.isEmpty(nameValuePairs)) {
                throw new OrderException("确认订单服务项目失败");
            }
            Map<String, ServicePrice> priceMap = null;
            RestResult<Boolean> remoteCheckResult = checkServicePointRemoteArea(condition);
            if(remoteCheckResult.getCode() != ErrorCode.NO_ERROR.code){
                throw new OrderException(new StringJoiner("").add("判断区域是否为偏远区域错误:").add(remoteCheckResult.getMsg()).toString());
            }
            Boolean isRemoteArea = remoteCheckResult.getData();
            if(isRemoteArea) {
                priceMap = servicePointService.getRemotePriceMapByProductsFromCache(servicePointId, nameValuePairs);
            }else{
                priceMap = servicePointService.getPriceMapByProductsFromCache(servicePointId, nameValuePairs);
            }
            if (priceMap == null) {
                throw new OrderException(new StringJoiner("").add("网点").add(isRemoteArea?"偏远区域":"").add("价格读取失败，请重试").toString());
            }
            if (CollectionUtils.isEmpty(priceMap)) {
                throw new OrderException(new StringJoiner("").add("网点").add(isRemoteArea?"偏远区域":"").add("价格读取失败，未维护网点价格").toString());
            }

            //配件
            //只读取单头
            List<MaterialMaster> materials = orderMaterialService.findMaterialMasterHeadsByOrderId(orderId, o.getQuarter());
            if (materials == null) {
                materials = Lists.newArrayList();
            }

            CustomerPrice cprice;
            ServicePrice eprice;
            List<MaterialMaster> materialMasters = Lists.newArrayList();

            int serviceTimes = condition.getServiceTimes() + 1;//上门次数
            boolean isAddFlag = false;//是否远程费已计费过
            User u = condition.getEngineer();//类型是User,值是md_engineer.id
            Engineer engineer = servicePointService.getEngineerFromCache(condition.getServicePoint().getId(), u.getId());
            if (engineer == null) {
                throw new OrderException(String.format("读取安维师傅失败，id:%s", u.getId()));
            }
            OrderDetail firstDetail = null;//本次上门服务的第一笔记录
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            if (CollectionUtils.isEmpty(serviceTypeMap)) {
                throw new OrderException("读取服务项目失败。");
            }
            ServiceType st = null;
            int idx = 0;
            for (OrderItem item : o.getItems()) {
                final Product product = item.getProduct();
                final ServiceType serviceType = item.getServiceType();
                cprice = customerPrices.stream()
                        .filter(m -> Objects.equals(m.getProduct().getId(), product.getId()) && Objects.equals(m.getServiceType().getId(), serviceType.getId()))
                        .findFirst().orElse(null);
                if (cprice == null) {
                    throw new OrderException(String.format("未定义产品价格。客户：%s 产品:%s 服务：%s", condition.getCustomer().getName(), product.getName(), serviceType.getName()));
                }
                eprice = priceMap.get(String.format("%d:%d", product.getId(), serviceType.getId()));
                if (eprice == null) {
                    throw new OrderException(String.format("未定义产品价格。网点：%s 产品：%s 服务：%s", condition.getServicePoint().getName(), product.getName(), serviceType.getName()));
                }
                st = serviceTypeMap.get(serviceType.getId());
                if (st == null) {
                    throw new OrderException(String.format("服务项目【%s】读取失败，或不存在", serviceType.getId()));
                }
                OrderDetail detail = new OrderDetail();
                detail.setQuarter(o.getQuarter());
                detail.setEngineerStandPrice(eprice.getPrice());
                detail.setEngineerDiscountPrice(eprice.getDiscountPrice());
                detail.setStandPrice(cprice.getPrice());
                detail.setDiscountPrice(cprice.getDiscountPrice());
                detail.setOrderId(orderId);
                detail.setProduct(item.getProduct());
                detail.setProductSpec(item.getProductSpec());
                detail.setBrand(StringUtils.left(StringUtils.toString(item.getBrand()), 20));//实际上门服务项的品牌只保留前20个字符
                detail.setServiceTimes(serviceTimes);
                detail.setQty(item.getQty());
                detail.setServiceType(item.getServiceType());
                detail.setServiceCategory(new Dict(st.getOrderServiceType(), ""));
                detail.setRemarks("自动添加下单的服务项目");
                detail.setSyncChargeTags(0);

                //engineer
                detail.setServicePoint(condition.getServicePoint());
                detail.setEngineerPaymentType(engineerPaymentType);
                detail.setEngineer(engineer);

                detail.setCreateBy(user);
                detail.setCreateDate(date);
                detail.setTravelNo("");
                detail.setDelFlag(50 + idx);//new,important,配件使用该值与上门服务关联

                //配件（分两部分 1-已审核，2-未审核） 套组要分拆
                //1.已审核,未关联上门明细的,统计配件费
                //2.未审核的，先关联，再审核时重新计算配件费
                int[] materialStatus = new int[]{2, 3, 4};//2：待发货 3：已发货 4：已完成
                long[] subProducts = new long[]{};//产品
                subProducts = ArrayUtils.add(subProducts, detail.getProduct().getId().longValue());
                //套组，拆分产品
                Product p = productService.getProductByIdFromCache(product.getId());
                if (p.getSetFlag() == 1) {
                    List<Product> products = productService.getProductListOfSet(p.getId());
                    if (products != null && products.size() > 0) {
                        for (Product sp : products) {
                            subProducts = ArrayUtils.add(subProducts, sp.getId().longValue());
                        }
                    }
                }
                final long[] sids = ArrayUtils.clone(subProducts);
                List<MaterialMaster> relateMaterials = null;
                if (materials.size() > 0) {
                    relateMaterials = materials.stream()
                            .filter(
                                    t -> ArrayUtils.contains(materialStatus, Integer.parseInt(t.getStatus().getValue()))
                                            && Objects.equals(t.getOrderDetailId(), 0l)
                                            && ArrayUtils.contains(sids, t.getProductId().longValue())
                            )
                            .collect(Collectors.toList());
                    if (relateMaterials != null && relateMaterials.size() > 0) {
                        for (MaterialMaster m : relateMaterials) {
                            //id,这时候还未产生id,使用delFlag关联,值>=50
                            m.setOrderDetailId(Long.valueOf(detail.getDelFlag().toString()));
                            //应付，+
                            detail.setEngineerMaterialCharge(detail.getEngineerMaterialCharge() + m.getTotalPrice());
                            detail.setEngineerTotalCharge(detail.getEngineerChage());
                            //应收，+
                            detail.setMaterialCharge(detail.getMaterialCharge() + m.getTotalPrice());
                        }
                    }
                }
                //远程费
                if (!isAddFlag) {//预设的远程费用只记入一次
                    isAddFlag = true;
                    //网点
                    detail.setEngineerTravelCharge(orderFee.getPlanTravelCharge());//预设远程费
                    detail.setEngineerOtherCharge(orderFee.getPlanOtherCharge());//预设其他费用
                    detail.setTravelNo(StringUtils.isBlank(orderFee.getPlanTravelNo()) ? "" : orderFee.getPlanTravelNo());//审批单号
                    //厂商
                    detail.setTravelCharge(orderFee.getCustomerPlanTravelCharge());//厂商远程费
                    detail.setOtherCharge(orderFee.getCustomerPlanOtherCharge());//厂商其他费用 2019/03/17
                    if(detail.getTravelCharge() <= 0 || detail.getOtherCharge() <=0){
                        autoCountCustomerRemoteCharge(condition.getProductCategoryId(),detail);
                    }
                    //2020-11-22 远程费+其他费用的总费用受控品类处理
                    limitRemoteChargeCheck(condition.getProductCategoryId(),null,detail);
                }
                details.add(detail);

                //配件
                if (relateMaterials != null && relateMaterials.size() > 0) {
                    for (MaterialMaster m : relateMaterials) {
                        m.setOrderDetailId(Long.valueOf(detail.getDelFlag().toString()));//这时候还未产生id,使用delFlag关联,值>=50
                    }
                }

                if (idx == 0) {
                    firstDetail = detail;
                }
                idx++;
            }

            //保险费汇总(负数)
            Double insuranceCharge = getTotalOrderInsurance(o.getId(), o.getQuarter());
            if (insuranceCharge == null) {
                insuranceCharge = 0.00;
            }

            //保险单号生效
            OrderInsurance orderInsurance = null;
            boolean insuranceFormEnabled = false;
            orderInsurance = dao.getOrderInsuranceByServicePoint(o.getQuarter(), o.getId(), servicePointId);
            if (orderInsurance != null && orderInsurance.getDelFlag() == OrderInsurance.DEL_FLAG_DELETE) {
                insuranceFormEnabled = true;
                orderInsurance.setUpdateBy(user);
                orderInsurance.setUpdateDate(date);
                orderInsurance.setDelFlag(0);
                dao.updateOrderInsurance(orderInsurance);
                insuranceCharge = insuranceCharge - orderInsurance.getAmount();//保险启用
            }

            //OrderFee
            rechargeOrder(details, firstDetail);
            //重新汇总金额
            HashMap<String, Object> feeMap = recountFee(details);
            //应收
            orderFee.setServiceCharge((Double) feeMap.get("serviceCharge"));
            orderFee.setMaterialCharge((Double) feeMap.get("materialCharge"));
            orderFee.setExpressCharge((Double) feeMap.get("expressCharge"));
            orderFee.setTravelCharge((Double) feeMap.get("travelCharge"));
            orderFee.setOtherCharge((Double) feeMap.get("otherCharge"));
            orderFee.setOrderCharge((Double) feeMap.get("orderCharge"));//以上5项的合计
            //时效费
            //加急费，时效费(快可立补贴&客户补贴) 不需统计，因确认上门只能在客评前操作，因此在对账异常订单处理时不做此操作
            //orderFee.setOrderCharge(orderFee.getOrderCharge()+orderFee.getCustomerTimeLinessCharge());

            //应付
            orderFee.setEngineerServiceCharge((Double) feeMap.get("engineerServiceCharge"));
            orderFee.setEngineerMaterialCharge((Double) feeMap.get("engineerMaterialCharge"));
            orderFee.setEngineerExpressCharge((Double) feeMap.get("engineerExpressCharge"));
            orderFee.setEngineerTravelCharge((Double) feeMap.get("engineerTravelCharge"));
            orderFee.setEngineerOtherCharge((Double) feeMap.get("engineerOtherCharge"));
            orderFee.setEngineerTotalCharge((Double) feeMap.get("engineerTotalCharge"));//合计
            //保险费
            orderFee.setEngineerTotalCharge(orderFee.getEngineerTotalCharge() + insuranceCharge);
            //加急费，时效费(快可立补贴&客户补贴) 不需统计，因确认上门只能在客评前操作，因此在对账异常订单处理时不做此操作
            //orderFee.setEngineerTotalCharge(orderFee.getEngineerTotalCharge() + timeLinessCharge + subsidyTimeLinessCharge);//合计

            params.clear();
            //fee
            params.put("orderId", o.getId());
            params.put("quarter", o.getQuarter());
            //应收(客户)
            params.put("serviceCharge", orderFee.getServiceCharge()); //服务费
            params.put("materialCharge", orderFee.getMaterialCharge());// 配件费
            params.put("expressCharge", orderFee.getExpressCharge()); // 快递费
            params.put("travelCharge", orderFee.getTravelCharge()); //远程费
            params.put("otherCharge", orderFee.getOtherCharge());//其他費用
            params.put("orderCharge", orderFee.getOrderCharge());//合计

            //应付(安维)
            params.put("engineerServiceCharge", orderFee.getEngineerServiceCharge());//服务费
            params.put("engineerMaterialCharge", orderFee.getEngineerMaterialCharge());//配件费
            params.put("engineerExpressCharge", orderFee.getEngineerExpressCharge());//快递费
            params.put("engineerTravelCharge", orderFee.getEngineerTravelCharge());//远程费
            params.put("engineerOtherCharge", orderFee.getEngineerOtherCharge());//其它费用
            params.put("insuranceCharge", insuranceCharge);//保险费用(负数，扣减)
            //合计=其他费用合计-保险费
            params.put("engineerTotalCharge", orderFee.getEngineerTotalCharge());
            dao.updateFee(params);

            //condition
            condition.setServiceTimes(serviceTimes);
            //已派单 -> 已上门
            if (condition.getStatusValue() == Order.ORDER_STATUS_PLANNED) {
                condition.setStatus(status);
            } else {
                status = condition.getStatus();
            }

            params.clear();
            params.put("quarter", o.getQuarter());
            params.put("status", status);
            params.put("orderId", orderId);
            params.put("serviceTimes", serviceTimes);
            /* 一键添加上门服务时：sub_status=50、pending_type_date = reservation_date = now、pending_type = 0， Add by Zhoucy*/
            params.put("pendingType", new Dict("0", ""));
            params.put("subStatus", Order.ORDER_SUBSTATUS_SERVICED);
            params.put("pendingTypeDate", date);
            params.put("reservationDate", date);
            params.put("updateBy", user);
            params.put("updateDate", date);
            dao.updateCondition(params);

            //status
            if (firstService) {
                OrderStatus orderStatus = o.getOrderStatus();
                orderStatus.setServiceFlag(1);
                orderStatus.setServiceDate(date);
                orderStatus.setServiceTimes(serviceTimes);
                params.clear();
                params.put("quarter", o.getQuarter());
                params.put("orderId", o.getId());
                params.put("serviceFlag", 1);
                params.put("serviceDate", date);
                params.put("serviceTimes", serviceTimes);
                dao.updateStatus(params);
            }

            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(o.getQuarter());
            processLog.setAction("确认上门");
            processLog.setOrderId(orderId);
            processLog.setActionComment(String.format("%s%s", confirmType == 0 ? "客服" : "安维", "确认上门"));
            processLog.setStatus(condition.getStatus().getLabel());
            processLog.setStatusValue(condition.getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(dataSourceId);
            saveOrderProcessLogNew(processLog);
            //details
            OrderDetail model;
            MDErrorType errorType = null;
            MDErrorCode errorCode = null;
            MDActionCodeDto actionCode = null;
            boolean isnull;
            for (int i = 0, size = details.size(); i < size; i++) {
                model = details.get(i);
                if (model.getDelFlag() == OrderDetail.DEL_FLAG_DELETE) {
                    continue;
                }
                if (model.getId() == null || model.getId() <= 0) {
                    //log
                    processLog = new OrderProcessLog();
                    processLog.setQuarter(o.getQuarter());
                    processLog.setAction("上门服务:添加订单具体服务项目");
                    processLog.setOrderId(orderId);
                    // 2019-12-27 统一上门服务跟踪进度格式
                    //processLog.setActionComment(String.format("上门服务:添加订单具体服务项目:%s,产品:%s", model.getServiceType().getName(), model.getProduct().getName()));
                    if (StringUtils.isBlank(model.getOtherActionRemark())) {
                        processLog.setActionComment(String.format("%s【%s】", model.getServiceType().getName(), model.getProduct().getName()));
                    } else {
                        processLog.setActionComment(String.format("%s【%s】其他故障:【%s】", model.getServiceType().getName(), model.getProduct().getName(), model.getOtherActionRemark()));
                    }
                    processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
                    processLog.setStatus(condition.getStatus().getLabel());
                    processLog.setStatusValue(condition.getStatusValue());
                    if (firstService) {
                        processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
                    } else {
                        processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
                    }
                    processLog.setCloseFlag(0);
                    processLog.setCreateBy(user);
                    processLog.setCreateDate(date);
                    processLog.setCustomerId(condition.getCustomerId());
                    processLog.setDataSourceId(dataSourceId);
                    saveOrderProcessLogNew(processLog);
                    //insert
                    model.setQuarter(o.getQuarter());
                    Long delFalg = Long.valueOf(model.getDelFlag().toString());//*
                    model.setDelFlag(0);//* 还原
                    if (model.getServiceCategory() == null || model.getServiceCategory().getIntValue() == 0) {
                        //调用方未设定，以下单时的工单类型为准
                        model.setServiceCategory(new Dict(condition.getOrderServiceType(), ""));
                    }
                    if (model.getErrorType() == null || model.getErrorType().getId() == null) {
                        if (errorType == null) {
                            errorType = new MDErrorType();
                            errorType.setId(0L);
                        }
                        model.setErrorType(errorType);
                    }
                    if (model.getErrorCode() == null || model.getErrorCode().getId() == null) {
                        if (errorCode == null) {
                            errorCode = new MDErrorCode();
                            errorCode.setId(0L);
                        }
                        model.setErrorCode(errorCode);
                    }
                    isnull = false;
                    if (model.getActionCode() == null) {
                        isnull = true;
                    }
                    if (isnull || model.getActionCode().getId() == null) {
                        if (actionCode == null) {
                            actionCode = new MDActionCodeDto();
                            actionCode.setId(0L);
                            if (isnull) {
                                actionCode.setName(org.apache.commons.lang3.StringUtils.EMPTY);
                            } else {
                                if (StringUtils.isBlank(model.getActionCode().getName())) {
                                    actionCode.setName(org.apache.commons.lang3.StringUtils.EMPTY);
                                } else {
                                    actionCode.setName(model.getActionCode().getName());
                                }
                            }

                        }
                        model.setActionCode(actionCode);
                    }
                    model.setOtherActionRemark(StringUtils.trimToEmpty(model.getOtherActionRemark()));
                    dao.insertDetail(model);
                    //配件
                    materialMasters = materials.stream().filter(t -> Objects.equals(t.getOrderDetailId(), delFalg)).collect(Collectors.toList());
                    for (MaterialMaster m : materialMasters) {
                        m.setOrderDetailId(model.getId());
                        params.clear();
                        params.put("id", m.getId());
                        params.put("quarter", o.getQuarter());//*
                        params.put("orderDetailId", model.getId());//*
                        //以下两个字段只有状态变更才更新
                        //params.put("updateBy", user);
                        //params.put("updateDate", date);
                        orderMaterialService.updateMaterialMaster(params);
                    }
                }
            }

            /* 安维确认上门 */
            if (1 == confirmType) {
                params.clear();
                params.put("quarter", o.getQuarter());
                params.put("id", orderId);
                params.put("confirmDoor", 1);
                orderHeadDao.updateOrder(params);//2020-12-03 sd_order -> sd_order_head
            }

            //OrderServicePointFee 生效并汇总
            OrderDetail servicePointFeeSum = null;
            if (orderServicePointFee != null) {
                servicePointFeeSum = details.stream().filter(t -> t.getServicePoint().getId().longValue() == servicePointId.longValue() && t.getDelFlag() != OrderDetail.DEL_FLAG_DELETE)
                        .reduce(new OrderDetail(), (item1, item2) -> {
                            return new OrderDetail(
                                    item1.getEngineerServiceCharge() + item2.getEngineerServiceCharge(),
                                    item1.getEngineerTravelCharge() + item2.getEngineerTravelCharge(),
                                    item1.getEngineerExpressCharge() + item2.getEngineerExpressCharge(),
                                    item1.getEngineerMaterialCharge() + item2.getEngineerMaterialCharge(),
                                    item1.getEngineerOtherCharge() + item2.getEngineerOtherCharge()
                            );
                        });
            }
            params.clear();
            params.put("orderId", o.getId());
            params.put("quarter", o.getQuarter());
            params.put("servicePointId", servicePointId);
            params.put("delFlag", 0);
            //费用汇总
            if (orderServicePointFee != null && servicePointFeeSum != null) {
                params.put("serviceCharge", servicePointFeeSum.getEngineerServiceCharge());
                params.put("travelCharge", servicePointFeeSum.getEngineerTravelCharge());
                params.put("expressCharge", servicePointFeeSum.getEngineerExpressCharge());
                params.put("materialCharge", servicePointFeeSum.getEngineerMaterialCharge());
                params.put("otherCharge", servicePointFeeSum.getEngineerOtherCharge());
                //2021-03-04 首次派单，网点保险开关关闭，再次派单时，网点保险开关开启情况，上门服务时补偿处理
                if(insuranceFormEnabled && orderServicePointFee.getInsuranceCharge() == 0.00){
                    params.put("insuranceCharge",0-orderInsurance.getAmount());
                    orderServicePointFee.setInsuranceCharge(0-orderInsurance.getAmount());//保证后面计算没有问题
                    params.put("insuranceNo",orderInsurance.getInsuranceNo());
                }else {
                    params.put("insuranceCharge", orderServicePointFee.getInsuranceCharge());
                }
                params.put("timeLinessCharge", orderServicePointFee.getTimeLinessCharge());
                params.put("customerTimeLinessCharge", orderServicePointFee.getCustomerTimeLinessCharge());
                params.put("urgentCharge", orderServicePointFee.getUrgentCharge());
                //汇总
                Double engineerTotalCharge = servicePointFeeSum.getEngineerServiceCharge()
                        + servicePointFeeSum.getEngineerTravelCharge()
                        + servicePointFeeSum.getEngineerExpressCharge()
                        + servicePointFeeSum.getEngineerMaterialCharge()
                        + servicePointFeeSum.getEngineerOtherCharge()
                        + orderServicePointFee.getInsuranceCharge()
                        + orderServicePointFee.getTimeLinessCharge()
                        + orderServicePointFee.getCustomerTimeLinessCharge()
                        + orderServicePointFee.getUrgentCharge();
                params.put("orderCharge", engineerTotalCharge);
            }
            dao.updateOrderServicePointFeeByMaps(params);

            //cache
            OrderCacheUtils.setDetailActionFlag(orderId);
            OrderCacheUtils.delete(orderId);

            //region B2B消息队列
            if (prevStatus == Order.ORDER_STATUS_PLANNED || o.getDataSourceId() == B2BDataSourceEnum.VIOMI.id
                    || o.getDataSourceId() == B2BDataSourceEnum.INSE.id) {
                Long pointId = condition.getServicePoint() != null ? condition.getServicePoint().getId() : null;
                b2BCenterOrderService.serviceOrder(o, pointId, engineerId, user, date);
            }
            //endregion B2B消息队列

            //region 网点订单数据更新 2019-03-25
            servicePointOrderBusinessService.confirmOnSiteService(orderId, o.getQuarter(), servicePointId, engineerId, status.getIntValue(), Order.ORDER_SUBSTATUS_SERVICED, user.getId(), date.getTime());
            //endregion
            return  ;
        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.confirmDoorAuto] orderId:{}", orderId, e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

    /**
     *
     * @param orderId
     * @param quarter
     * @param user
     * @param confirmType
     */
    @Transactional(readOnly = false)
    public RestOrderDetailInfoNew confirmDoorAutoNew(Long orderId, String quarter, User user, int confirmType) {
        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, orderId);
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("错误：此订单正在处理中，请稍候重试，或刷新订单。");
        }
        try {
            Order o = getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.DETAIL, true);
            if (o == null) {
                throw new OrderException("错误：读取订单信息失败");
            }
            if (!o.canService()) {
                throw new OrderException("错误：不能确认上门，请确认订单状态.");
            }
            if (o.getOrderCondition().getAppointmentDate() == null) {
                throw new OrderException("错误：没有设置预约时间，不允许直接确认上门.");
            }
            Date date = new Date();
            if (o.getOrderCondition().getAppointmentDate().getTime() > DateUtils.getEndOfDay(date).getTime()) {
                throw new OrderException("预约时间与当前不一致，请重新预约！");
            }
            if (o.getOrderCondition().getOrderServiceType() == OrderUtils.OrderTypeEnum.BACK.getId()
                    || o.getOrderCondition().getOrderServiceType() == OrderUtils.OrderTypeEnum.EXCHANGE.getId()) {
                throw new OrderException("错误：退货或换货工单请联系客服处理！");
            }
            //2020-09-24 接入云米，增加经纬度检查
            AjaxJsonEntity locationCheckResult = checkAddressLocation(o.getDataSource().getIntValue(),orderId,o.getQuarter());
            if(!locationCheckResult.getSuccess()){
                throw new OrderException("因"+locationCheckResult.getMessage() + "，不能确认上门。");
            }
            RestOrderDetailInfoNew restOrderDetailInfoNew = null;
            OrderCondition condition = o.getOrderCondition();
            //网点费用表
            Long servicePointId = condition.getServicePoint().getId();
            Long engineerId = condition.getEngineer() == null ? null : condition.getEngineer().getId();
            OrderServicePointFee orderServicePointFee = getOrderServicePointFee(orderId, o.getQuarter(), servicePointId);
            int dataSourceId = o.getDataSourceId();
            int prevStatus = condition.getStatusValue();
            List<OrderDetail> details = o.getDetailList();
            if (details == null) {
                details = Lists.newArrayList();
            }
            //上门服务
            details = details.stream()
                    .filter(t -> t.getDelFlag() == 0)
                    .collect(Collectors.toList());
            HashMap<String, Object> params = Maps.newHashMap();

            // 如果订单中已经有添加当前安维网点的上门服务就不再添加
            // 只记录log
            if (details.size() > 0) {
                OrderDetail detail = details.stream()
                        .filter(t -> t.getDelFlag() == 0
                                && Objects.equals(t.getServicePoint().getId(), condition.getServicePoint().getId())
                        )
                        .findFirst()
                        .orElse(null);

                restOrderDetailInfoNew = mapper.map(o,RestOrderDetailInfoNew.class);

                if (detail != null) {
                    //log
                    OrderProcessLog processLog = new OrderProcessLog();
                    processLog.setQuarter(o.getQuarter());
                    processLog.setAction("确认上门");
                    processLog.setOrderId(orderId);
                    processLog.setActionComment(String.format("%s%s", confirmType == 0 ? "客服" : "安维", "确认上门"));
                    processLog.setStatus(condition.getStatus().getLabel());
                    processLog.setStatusValue(condition.getStatusValue());
                    processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
                    processLog.setCloseFlag(0);
                    processLog.setCreateBy(user);
                    processLog.setCreateDate(date);
                    processLog.setCustomerId(condition.getCustomerId());
                    processLog.setDataSourceId(dataSourceId);
                    saveOrderProcessLogNew(processLog);

                    //cache,淘汰
                    OrderCacheUtils.delete(orderId);

                    //add by Zhoucy 2018-9-3 19:50 重复确认上门也修改子状态及相关时间
                    params.clear();
                    params.put("quarter", o.getQuarter());
                    params.put("orderId", orderId);
                    /* 一键添加上门服务时：sub_status=50、pending_type_date = reservation_date = now、pending_type = 0， Add by Zhoucy*/
                    params.put("pendingType", new Dict("0", ""));
                    params.put("subStatus", Order.ORDER_SUBSTATUS_SERVICED);
                    params.put("pendingTypeDate", date);
                    params.put("reservationDate", date);
                    params.put("updateBy", user);
                    params.put("updateDate", date);
                    dao.updateCondition(params);

                    // 2019-03-25 网点订单数据更新
                    servicePointOrderBusinessService.confirmOnSiteService(orderId, o.getQuarter(), servicePointId, engineerId, prevStatus, Order.ORDER_SUBSTATUS_SERVICED, user.getId(), date.getTime());

                    //region B2B消息队列
                    if (o.getDataSourceId() == B2BDataSourceEnum.VIOMI.id
                            || o.getDataSourceId() == B2BDataSourceEnum.INSE.id) {
                        Long pointId = condition.getServicePoint() != null ? condition.getServicePoint().getId() : null;
                        b2BCenterOrderService.serviceOrder(o, pointId, engineerId, user, date);
                    }
                    return restOrderDetailInfoNew;
                }
            }

            OrderFee orderFee = o.getOrderFee();
            if(orderFee == null){
                throw new OrderException("错误：读取订单费用汇总数据失败！");
            }
            //2020-10-21 从主库读取派单时预设的费用和单号
            OrderFee feeMaster = getPresetFeeWhenPlanFromMasterDB(orderId,o.getQuarter());
            if(feeMaster == null){
                log.error("读取派单预设费用失败,orderId:{} , quarter:{}",orderId,o.getQuarter());
                throw new OrderException("错误：读取派单预设费用失败！");
            }
            orderFee.setPlanTravelCharge(feeMaster.getPlanTravelCharge());
            orderFee.setPlanTravelNo(feeMaster.getPlanTravelNo());
            orderFee.setPlanDistance(feeMaster.getPlanDistance());
            orderFee.setCustomerPlanTravelCharge(feeMaster.getCustomerPlanTravelCharge());
            orderFee.setPlanOtherCharge(feeMaster.getPlanOtherCharge());
            orderFee.setCustomerPlanOtherCharge(feeMaster.getCustomerPlanOtherCharge());

            // 确认上门改变订单的状态
            Dict status = new Dict();
            status.setValue(Order.ORDER_STATUS_SERVICED.toString());
            status.setLabel(MSDictUtils.getDictLabel(status.getValue(), "order_status", "已上门"));//切换为微服务

            Boolean firstService = true;//首次上门

            if (details.size() > 0) {
                firstService = false;
            }

            //以下代码，当前网点没有上门过，自动添加上门服务，有可能是二次上门
            //检查当前安维的付款方式
            Dict engineerPaymentType = orderFee.getEngineerPaymentType();
            if (engineerPaymentType == null || engineerPaymentType.getIntValue() <= 0) {
                //throw new OrderException(String.format("订单中安维网点：%s 的付款方式未设定", condition.getServicePoint().getName()));
                ServicePoint servicePoint = servicePointService.getFromCache(servicePointId);
                if (servicePoint != null && servicePoint.getFinance() != null
                        && servicePoint.getFinance().getPaymentType() != null
                        && servicePoint.getFinance().getPaymentType().getIntValue() > 0) {
                    engineerPaymentType = servicePoint.getFinance().getPaymentType();
                } else {
                    throw new OrderException(String.format("确认网点：%s 结算方式失败", condition.getServicePoint().getName()));
                }
            }
            Dict orderPaymentType = orderFee.getOrderPaymentType();
            if (orderPaymentType == null || StringUtils.isBlank(orderPaymentType.getValue())) {
                throw new OrderException(String.format("订单中客户：%s 的付款方式未设定", condition.getCustomer().getName()));
            }
            //Customer Price
            List<CustomerPrice> customerPrices = customerService.getPricesFromCache(condition.getCustomer().getId());
            if (customerPrices == null || customerPrices.size() == 0) {
                throw new OrderException(String.format("读取客户：%s价格失败", condition.getCustomer().getName()));
            }
            List<OrderItem> items = o.getItems();
            //ServicePoint Price
            //使用新的网点价格读取方法 2020-03-07
            List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> nameValuePairs = getOrderItemProductAndServiceTypePairs(items);
            if (CollectionUtils.isEmpty(nameValuePairs)) {
                throw new OrderException("确认订单服务项目失败");
            }
            Map<String, ServicePrice> priceMap = null;
            RestResult<Boolean> remoteCheckResult = checkServicePointRemoteArea(condition);
            if(remoteCheckResult.getCode() != ErrorCode.NO_ERROR.code){
                throw new OrderException(new StringJoiner("").add("判断区域是否为偏远区域错误:").add(remoteCheckResult.getMsg()).toString());
            }
            Boolean isRemoteArea = remoteCheckResult.getData();
            if(isRemoteArea) {
                priceMap = servicePointService.getRemotePriceMapByProductsFromCache(servicePointId, nameValuePairs);
            }else{
                priceMap = servicePointService.getPriceMapByProductsFromCache(servicePointId, nameValuePairs);
            }
            if (priceMap == null) {
                throw new OrderException(new StringJoiner("").add("网点").add(isRemoteArea?"偏远区域":"").add("价格读取失败，请重试").toString());
            }
            if (CollectionUtils.isEmpty(priceMap)) {
                throw new OrderException(new StringJoiner("").add("网点").add(isRemoteArea?"偏远区域":"").add("价格读取失败，未维护网点价格").toString());
            }

            //配件
            //只读取单头
            List<MaterialMaster> materials = orderMaterialService.findMaterialMasterHeadsByOrderId(orderId, o.getQuarter());
            if (materials == null) {
                materials = Lists.newArrayList();
            }

            CustomerPrice cprice;
            ServicePrice eprice;
            List<MaterialMaster> materialMasters = Lists.newArrayList();

            int serviceTimes = condition.getServiceTimes() + 1;//上门次数
            boolean isAddFlag = false;//是否远程费已计费过
            User u = condition.getEngineer();//类型是User,值是md_engineer.id
            Engineer engineer = servicePointService.getEngineerFromCache(condition.getServicePoint().getId(), u.getId());
            if (engineer == null) {
                throw new OrderException(String.format("读取安维师傅失败，id:%s", u.getId()));
            }
            OrderDetail firstDetail = null;//本次上门服务的第一笔记录
            Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
            if (CollectionUtils.isEmpty(serviceTypeMap)) {
                throw new OrderException("读取服务项目失败。");
            }
            ServiceType st = null;
            int idx = 0;
            for (OrderItem item : o.getItems()) {
                final Product product = item.getProduct();
                final ServiceType serviceType = item.getServiceType();
                cprice = customerPrices.stream()
                        .filter(m -> Objects.equals(m.getProduct().getId(), product.getId()) && Objects.equals(m.getServiceType().getId(), serviceType.getId()))
                        .findFirst().orElse(null);
                if (cprice == null) {
                    throw new OrderException(String.format("未定义产品价格。客户：%s 产品:%s 服务：%s", condition.getCustomer().getName(), product.getName(), serviceType.getName()));
                }
                eprice = priceMap.get(String.format("%d:%d", product.getId(), serviceType.getId()));
                if (eprice == null) {
                    throw new OrderException(String.format("未定义产品价格。网点：%s 产品：%s 服务：%s", condition.getServicePoint().getName(), product.getName(), serviceType.getName()));
                }
                st = serviceTypeMap.get(serviceType.getId());
                if (st == null) {
                    throw new OrderException(String.format("服务项目【%s】读取失败，或不存在", serviceType.getId()));
                }
                OrderDetail detail = new OrderDetail();
                detail.setQuarter(o.getQuarter());
                detail.setEngineerStandPrice(eprice.getPrice());
                detail.setEngineerDiscountPrice(eprice.getDiscountPrice());
                detail.setStandPrice(cprice.getPrice());
                detail.setDiscountPrice(cprice.getDiscountPrice());
                detail.setOrderId(orderId);
                detail.setProduct(item.getProduct());
                detail.setProductSpec(item.getProductSpec());
                detail.setBrand(StringUtils.left(StringUtils.toString(item.getBrand()), 20));//实际上门服务项的品牌只保留前20个字符
                detail.setServiceTimes(serviceTimes);
                detail.setQty(item.getQty());
                detail.setServiceType(item.getServiceType());
                detail.setServiceCategory(new Dict(st.getOrderServiceType(), ""));
                detail.setRemarks("自动添加下单的服务项目");
                detail.setSyncChargeTags(0);

                //engineer
                detail.setServicePoint(condition.getServicePoint());
                detail.setEngineerPaymentType(engineerPaymentType);
                detail.setEngineer(engineer);

                detail.setCreateBy(user);
                detail.setCreateDate(date);
                detail.setTravelNo("");
                detail.setDelFlag(50 + idx);//new,important,配件使用该值与上门服务关联

                //配件（分两部分 1-已审核，2-未审核） 套组要分拆
                //1.已审核,未关联上门明细的,统计配件费
                //2.未审核的，先关联，再审核时重新计算配件费
                int[] materialStatus = new int[]{2, 3, 4};//2：待发货 3：已发货 4：已完成
                long[] subProducts = new long[]{};//产品
                subProducts = ArrayUtils.add(subProducts, detail.getProduct().getId().longValue());
                //套组，拆分产品
                Product p = productService.getProductByIdFromCache(product.getId());
                if (p.getSetFlag() == 1) {
                    List<Product> products = productService.getProductListOfSet(p.getId());
                    if (products != null && products.size() > 0) {
                        for (Product sp : products) {
                            subProducts = ArrayUtils.add(subProducts, sp.getId().longValue());
                        }
                    }
                }
                final long[] sids = ArrayUtils.clone(subProducts);
                List<MaterialMaster> relateMaterials = null;
                if (materials.size() > 0) {
                    relateMaterials = materials.stream()
                            .filter(
                                    t -> ArrayUtils.contains(materialStatus, Integer.parseInt(t.getStatus().getValue()))
                                            && Objects.equals(t.getOrderDetailId(), 0l)
                                            && ArrayUtils.contains(sids, t.getProductId().longValue())
                            )
                            .collect(Collectors.toList());
                    if (relateMaterials != null && relateMaterials.size() > 0) {
                        for (MaterialMaster m : relateMaterials) {
                            //id,这时候还未产生id,使用delFlag关联,值>=50
                            m.setOrderDetailId(Long.valueOf(detail.getDelFlag().toString()));
                            //应付，+
                            detail.setEngineerMaterialCharge(detail.getEngineerMaterialCharge() + m.getTotalPrice());
                            detail.setEngineerTotalCharge(detail.getEngineerChage());
                            //应收，+
                            detail.setMaterialCharge(detail.getMaterialCharge() + m.getTotalPrice());
                        }
                    }
                }
                //远程费
                if (!isAddFlag) {//预设的远程费用只记入一次
                    isAddFlag = true;
                    //网点
                    detail.setEngineerTravelCharge(orderFee.getPlanTravelCharge());//预设远程费
                    detail.setEngineerOtherCharge(orderFee.getPlanOtherCharge());//预设其他费用
                    detail.setTravelNo(StringUtils.isBlank(orderFee.getPlanTravelNo()) ? "" : orderFee.getPlanTravelNo());//审批单号
                    //厂商
                    detail.setTravelCharge(orderFee.getCustomerPlanTravelCharge());//厂商远程费
                    detail.setOtherCharge(orderFee.getCustomerPlanOtherCharge());//厂商其他费用 2019/03/17
                    if(detail.getTravelCharge() <= 0 || detail.getOtherCharge() <=0){
                        autoCountCustomerRemoteCharge(condition.getProductCategoryId(),detail);
                    }
                    //2020-11-22 远程费+其他费用的总费用受控品类处理
                    limitRemoteChargeCheck(condition.getProductCategoryId(),null,detail);
                }
                details.add(detail);

                //配件
                if (relateMaterials != null && relateMaterials.size() > 0) {
                    for (MaterialMaster m : relateMaterials) {
                        m.setOrderDetailId(Long.valueOf(detail.getDelFlag().toString()));//这时候还未产生id,使用delFlag关联,值>=50
                    }
                }

                if (idx == 0) {
                    firstDetail = detail;
                }
                idx++;
            }

            //保险费汇总(负数)
            Double insuranceCharge = getTotalOrderInsurance(o.getId(), o.getQuarter());
            if (insuranceCharge == null) {
                insuranceCharge = 0.00;
            }

            //保险单号生效
            OrderInsurance orderInsurance = null;
            boolean insuranceFormEnabled = false;
            orderInsurance = dao.getOrderInsuranceByServicePoint(o.getQuarter(), o.getId(), servicePointId);
            if (orderInsurance != null && orderInsurance.getDelFlag() == OrderInsurance.DEL_FLAG_DELETE) {
                insuranceFormEnabled = true;
                orderInsurance.setUpdateBy(user);
                orderInsurance.setUpdateDate(date);
                orderInsurance.setDelFlag(0);
                dao.updateOrderInsurance(orderInsurance);
                insuranceCharge = insuranceCharge - orderInsurance.getAmount();//保险启用
            }

            //OrderFee
            rechargeOrder(details, firstDetail);
            //重新汇总金额
            HashMap<String, Object> feeMap = recountFee(details);
            //应收
            orderFee.setServiceCharge((Double) feeMap.get("serviceCharge"));
            orderFee.setMaterialCharge((Double) feeMap.get("materialCharge"));
            orderFee.setExpressCharge((Double) feeMap.get("expressCharge"));
            orderFee.setTravelCharge((Double) feeMap.get("travelCharge"));
            orderFee.setOtherCharge((Double) feeMap.get("otherCharge"));
            orderFee.setOrderCharge((Double) feeMap.get("orderCharge"));//以上5项的合计
            //时效费
            //加急费，时效费(快可立补贴&客户补贴) 不需统计，因确认上门只能在客评前操作，因此在对账异常订单处理时不做此操作
            //orderFee.setOrderCharge(orderFee.getOrderCharge()+orderFee.getCustomerTimeLinessCharge());

            //应付
            orderFee.setEngineerServiceCharge((Double) feeMap.get("engineerServiceCharge"));
            orderFee.setEngineerMaterialCharge((Double) feeMap.get("engineerMaterialCharge"));
            orderFee.setEngineerExpressCharge((Double) feeMap.get("engineerExpressCharge"));
            orderFee.setEngineerTravelCharge((Double) feeMap.get("engineerTravelCharge"));
            orderFee.setEngineerOtherCharge((Double) feeMap.get("engineerOtherCharge"));
            orderFee.setEngineerTotalCharge((Double) feeMap.get("engineerTotalCharge"));//合计
            //保险费
            orderFee.setEngineerTotalCharge(orderFee.getEngineerTotalCharge() + insuranceCharge);
            //加急费，时效费(快可立补贴&客户补贴) 不需统计，因确认上门只能在客评前操作，因此在对账异常订单处理时不做此操作
            //orderFee.setEngineerTotalCharge(orderFee.getEngineerTotalCharge() + timeLinessCharge + subsidyTimeLinessCharge);//合计

            params.clear();
            //fee
            params.put("orderId", o.getId());
            params.put("quarter", o.getQuarter());
            //应收(客户)
            params.put("serviceCharge", orderFee.getServiceCharge()); //服务费
            params.put("materialCharge", orderFee.getMaterialCharge());// 配件费
            params.put("expressCharge", orderFee.getExpressCharge()); // 快递费
            params.put("travelCharge", orderFee.getTravelCharge()); //远程费
            params.put("otherCharge", orderFee.getOtherCharge());//其他費用
            params.put("orderCharge", orderFee.getOrderCharge());//合计

            //应付(安维)
            params.put("engineerServiceCharge", orderFee.getEngineerServiceCharge());//服务费
            params.put("engineerMaterialCharge", orderFee.getEngineerMaterialCharge());//配件费
            params.put("engineerExpressCharge", orderFee.getEngineerExpressCharge());//快递费
            params.put("engineerTravelCharge", orderFee.getEngineerTravelCharge());//远程费
            params.put("engineerOtherCharge", orderFee.getEngineerOtherCharge());//其它费用
            params.put("insuranceCharge", insuranceCharge);//保险费用(负数，扣减)
            //合计=其他费用合计-保险费
            params.put("engineerTotalCharge", orderFee.getEngineerTotalCharge());
            dao.updateFee(params);

            //condition
            condition.setServiceTimes(serviceTimes);
            //已派单 -> 已上门
            if (condition.getStatusValue() == Order.ORDER_STATUS_PLANNED) {
                condition.setStatus(status);
            } else {
                status = condition.getStatus();
            }

            params.clear();
            params.put("quarter", o.getQuarter());
            params.put("status", status);
            params.put("orderId", orderId);
            params.put("serviceTimes", serviceTimes);
            /* 一键添加上门服务时：sub_status=50、pending_type_date = reservation_date = now、pending_type = 0， Add by Zhoucy*/
            params.put("pendingType", new Dict("0", ""));
            params.put("subStatus", Order.ORDER_SUBSTATUS_SERVICED);
            params.put("pendingTypeDate", date);
            params.put("reservationDate", date);
            params.put("updateBy", user);
            params.put("updateDate", date);
            dao.updateCondition(params);

            //status
            if (firstService) {
                OrderStatus orderStatus = o.getOrderStatus();
                orderStatus.setServiceFlag(1);
                orderStatus.setServiceDate(date);
                orderStatus.setServiceTimes(serviceTimes);
                params.clear();
                params.put("quarter", o.getQuarter());
                params.put("orderId", o.getId());
                params.put("serviceFlag", 1);
                params.put("serviceDate", date);
                params.put("serviceTimes", serviceTimes);
                dao.updateStatus(params);
            }

            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(o.getQuarter());
            processLog.setAction("确认上门");
            processLog.setOrderId(orderId);
            processLog.setActionComment(String.format("%s%s", confirmType == 0 ? "客服" : "安维", "确认上门"));
            processLog.setStatus(condition.getStatus().getLabel());
            processLog.setStatusValue(condition.getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(dataSourceId);
            saveOrderProcessLogNew(processLog);
            //details
            OrderDetail model;
            MDErrorType errorType = null;
            MDErrorCode errorCode = null;
            MDActionCodeDto actionCode = null;
            boolean isnull;
            for (int i = 0, size = details.size(); i < size; i++) {
                model = details.get(i);
                if (model.getDelFlag() == OrderDetail.DEL_FLAG_DELETE) {
                    continue;
                }
                if (model.getId() == null || model.getId() <= 0) {
                    //log
                    processLog = new OrderProcessLog();
                    processLog.setQuarter(o.getQuarter());
                    processLog.setAction("上门服务:添加订单具体服务项目");
                    processLog.setOrderId(orderId);
                    // 2019-12-27 统一上门服务跟踪进度格式
                    //processLog.setActionComment(String.format("上门服务:添加订单具体服务项目:%s,产品:%s", model.getServiceType().getName(), model.getProduct().getName()));
                    if (StringUtils.isBlank(model.getOtherActionRemark())) {
                        processLog.setActionComment(String.format("%s【%s】", model.getServiceType().getName(), model.getProduct().getName()));
                    } else {
                        processLog.setActionComment(String.format("%s【%s】其他故障:【%s】", model.getServiceType().getName(), model.getProduct().getName(), model.getOtherActionRemark()));
                    }
                    processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
                    processLog.setStatus(condition.getStatus().getLabel());
                    processLog.setStatusValue(condition.getStatusValue());
                    if (firstService) {
                        processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
                    } else {
                        processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
                    }
                    processLog.setCloseFlag(0);
                    processLog.setCreateBy(user);
                    processLog.setCreateDate(date);
                    processLog.setCustomerId(condition.getCustomerId());
                    processLog.setDataSourceId(dataSourceId);
                    saveOrderProcessLogNew(processLog);
                    //insert
                    model.setQuarter(o.getQuarter());
                    Long delFalg = Long.valueOf(model.getDelFlag().toString());//*
                    model.setDelFlag(0);//* 还原
                    if (model.getServiceCategory() == null || model.getServiceCategory().getIntValue() == 0) {
                        //调用方未设定，以下单时的工单类型为准
                        model.setServiceCategory(new Dict(condition.getOrderServiceType(), ""));
                    }
                    if (model.getErrorType() == null || model.getErrorType().getId() == null) {
                        if (errorType == null) {
                            errorType = new MDErrorType();
                            errorType.setId(0L);
                        }
                        model.setErrorType(errorType);
                    }
                    if (model.getErrorCode() == null || model.getErrorCode().getId() == null) {
                        if (errorCode == null) {
                            errorCode = new MDErrorCode();
                            errorCode.setId(0L);
                        }
                        model.setErrorCode(errorCode);
                    }
                    isnull = false;
                    if (model.getActionCode() == null) {
                        isnull = true;
                    }
                    if (isnull || model.getActionCode().getId() == null) {
                        if (actionCode == null) {
                            actionCode = new MDActionCodeDto();
                            actionCode.setId(0L);
                            if (isnull) {
                                actionCode.setName(org.apache.commons.lang3.StringUtils.EMPTY);
                            } else {
                                if (StringUtils.isBlank(model.getActionCode().getName())) {
                                    actionCode.setName(org.apache.commons.lang3.StringUtils.EMPTY);
                                } else {
                                    actionCode.setName(model.getActionCode().getName());
                                }
                            }

                        }
                        model.setActionCode(actionCode);
                    }
                    model.setOtherActionRemark(StringUtils.trimToEmpty(model.getOtherActionRemark()));
                    dao.insertDetail(model);
                    //配件
                    materialMasters = materials.stream().filter(t -> Objects.equals(t.getOrderDetailId(), delFalg)).collect(Collectors.toList());
                    for (MaterialMaster m : materialMasters) {
                        m.setOrderDetailId(model.getId());
                        params.clear();
                        params.put("id", m.getId());
                        params.put("quarter", o.getQuarter());//*
                        params.put("orderDetailId", model.getId());//*
                        //以下两个字段只有状态变更才更新
                        //params.put("updateBy", user);
                        //params.put("updateDate", date);
                        orderMaterialService.updateMaterialMaster(params);
                    }
                }
            }

            /* 安维确认上门 */
            if (1 == confirmType) {
                params.clear();
                params.put("quarter", o.getQuarter());
                params.put("id", orderId);
                params.put("confirmDoor", 1);
                orderHeadDao.updateOrder(params);//2020-12-03 sd_order -> sd_order_head
            }

            //OrderServicePointFee 生效并汇总
            OrderDetail servicePointFeeSum = null;
            if (orderServicePointFee != null) {
                servicePointFeeSum = details.stream().filter(t -> t.getServicePoint().getId().longValue() == servicePointId.longValue() && t.getDelFlag() != OrderDetail.DEL_FLAG_DELETE)
                        .reduce(new OrderDetail(), (item1, item2) -> {
                            return new OrderDetail(
                                    item1.getEngineerServiceCharge() + item2.getEngineerServiceCharge(),
                                    item1.getEngineerTravelCharge() + item2.getEngineerTravelCharge(),
                                    item1.getEngineerExpressCharge() + item2.getEngineerExpressCharge(),
                                    item1.getEngineerMaterialCharge() + item2.getEngineerMaterialCharge(),
                                    item1.getEngineerOtherCharge() + item2.getEngineerOtherCharge()
                            );
                        });
            }
            params.clear();
            params.put("orderId", o.getId());
            params.put("quarter", o.getQuarter());
            params.put("servicePointId", servicePointId);
            params.put("delFlag", 0);
            //费用汇总
            if (orderServicePointFee != null && servicePointFeeSum != null) {
                params.put("serviceCharge", servicePointFeeSum.getEngineerServiceCharge());
                params.put("travelCharge", servicePointFeeSum.getEngineerTravelCharge());
                params.put("expressCharge", servicePointFeeSum.getEngineerExpressCharge());
                params.put("materialCharge", servicePointFeeSum.getEngineerMaterialCharge());
                params.put("otherCharge", servicePointFeeSum.getEngineerOtherCharge());
                //2021-03-04 首次派单，网点保险开关关闭，再次派单时，网点保险开关开启情况，上门服务时补偿处理
                if(insuranceFormEnabled && orderServicePointFee.getInsuranceCharge() == 0.00){
                    params.put("insuranceCharge",0-orderInsurance.getAmount());
                    orderServicePointFee.setInsuranceCharge(0-orderInsurance.getAmount());//保证后面计算没有问题
                    params.put("insuranceNo",orderInsurance.getInsuranceNo());
                }else {
                    params.put("insuranceCharge", orderServicePointFee.getInsuranceCharge());
                }
                params.put("timeLinessCharge", orderServicePointFee.getTimeLinessCharge());
                params.put("customerTimeLinessCharge", orderServicePointFee.getCustomerTimeLinessCharge());
                params.put("urgentCharge", orderServicePointFee.getUrgentCharge());
                //汇总
                Double engineerTotalCharge = servicePointFeeSum.getEngineerServiceCharge()
                        + servicePointFeeSum.getEngineerTravelCharge()
                        + servicePointFeeSum.getEngineerExpressCharge()
                        + servicePointFeeSum.getEngineerMaterialCharge()
                        + servicePointFeeSum.getEngineerOtherCharge()
                        + orderServicePointFee.getInsuranceCharge()
                        + orderServicePointFee.getTimeLinessCharge()
                        + orderServicePointFee.getCustomerTimeLinessCharge()
                        + orderServicePointFee.getUrgentCharge();
                params.put("orderCharge", engineerTotalCharge);
            }
            dao.updateOrderServicePointFeeByMaps(params);

            //cache
            OrderCacheUtils.setDetailActionFlag(orderId);
            OrderCacheUtils.delete(orderId);

            //region B2B消息队列
            if (prevStatus == Order.ORDER_STATUS_PLANNED || o.getDataSourceId() == B2BDataSourceEnum.VIOMI.id
                    || o.getDataSourceId() == B2BDataSourceEnum.INSE.id) {
                Long pointId = condition.getServicePoint() != null ? condition.getServicePoint().getId() : null;
                b2BCenterOrderService.serviceOrder(o, pointId, engineerId, user, date);
            }
            //endregion B2B消息队列

            //region 网点订单数据更新 2019-03-25
            servicePointOrderBusinessService.confirmOnSiteService(orderId, o.getQuarter(), servicePointId, engineerId, status.getIntValue(), Order.ORDER_SUBSTATUS_SERVICED, user.getId(), date.getTime());
            //endregion

            o.setDetailList(details);
            restOrderDetailInfoNew =  mapper.map(o,RestOrderDetailInfoNew.class);

            return restOrderDetailInfoNew;
        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.confirmDoorAutoNew] orderId:{}", orderId, e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

    /**
     * 远程费+其他费用总费用受控品类
     * (同次)上门服务中远程费与其他费用的合计金额超过设定金额，不允许保存
     * 费用不超过设定金额，应收为0
     * @param productCategoryId 品类id
     * @param list 之前的上门服务,不包含本次上门服务
     * @param detail 本次上门服务
     */
    @Transactional
    public void limitRemoteChargeCheck(Long productCategoryId,List<OrderDetail> list, OrderDetail detail) {
        long id = Optional.ofNullable(productCategoryId).orElse(0L);
        if(id <= 0){
            log.error("传入品类ID小于等于0,{}",id);
            return;
        }
        Dict limitRemoteDict = MSDictUtils.getDictByValue(productCategoryId.toString(), OrderUtils.LIMIT_REMOTECHARGE_CATEGORY_DICT);
        if (limitRemoteDict == null) {
            return;
        }
        double limitCharge = Double.valueOf(limitRemoteDict.getSort());
        if(limitCharge < 0){
            limitCharge = 0;
        }
        int serviceTimes = detail.getServiceTimes();
        if(!CollectionUtils.isEmpty(list)) {
            TwoTuple<Double, Double> total = list.stream()
                    .filter(t -> t.getServiceTimes() == serviceTimes && t.getDelFlag() == 0)
                    .map(t -> new TwoTuple<Double, Double>(t.getEngineerTravelCharge(), t.getEngineerOtherCharge()))
                    .reduce(new TwoTuple<Double, Double>(0.00, 0.00), (d1, d2) -> {
                        return new TwoTuple<Double, Double>(d1.getAElement() + d2.getAElement(), d1.getBElement() + d2.getBElement());
                    });
            total.setAElement(total.getAElement() + detail.getEngineerTravelCharge());
            total.setBElement(total.getBElement() + detail.getEngineerOtherCharge());
            double charge = total.getAElement() + total.getBElement();
            if (charge > limitCharge) {
                throw new RuntimeException(MessageFormat.format("远程费用和其他费用合计{0,number,#.##}元，已超过上限{1,number,#.##}元，不能保存！请确认是否操作退单!", charge, limitCharge));
            }
        }else{
            double charge = detail.getEngineerTravelCharge() + detail.getEngineerOtherCharge();
            if (charge > limitCharge) {
                throw new RuntimeException(MessageFormat.format("远程费用和其他费用合计{0,number,#.##}元，已超过上限{1,number,#.##}元，不能保存！请确认是否操作退单!", charge, limitCharge));
            }
        }
        //应收清零
        detail.setTravelCharge(0.00);
        detail.setOtherCharge(0.00);
    }

    /**
     * 自动计入应收（客户应付）远程费及其他费用
     * 数据字典:customer:auto:count:remotecharge:category 中配置的品类，
     * 自动将网点远程费及其他费用复制到应收属性
     * @param detail
     */
    private void autoCountCustomerRemoteCharge(Long productCategoryId,OrderDetail detail){
        if(detail== null){
            return;
        }
        //网点无远程费及其他费用(都是0），忽略
        if(detail.getEngineerTravelCharge() <= 0 && detail.getEngineerOtherCharge() <= 0){
            return;
        }
        // 应收远程费及其他费用，已有费用，忽略
        if(detail.getTravelCharge() > 0 && detail.getOtherCharge() > 0){
            return;
        }
        // 检查品类是否是特殊处理的品类
        //Long categoryId = Optional.ofNullable(detail.getProduct()).map(t->t.getCategory()).map(c->c.getId()).orElse(0L);
        if(productCategoryId== null || productCategoryId <= 0){
            return;
        }
        try {
            //get config from dict
            Dict dict = MSDictUtils.getDictByValue(productCategoryId.toString(), OrderUtils.SYNC_CUSTOMER_CHARGE_DICT);
            if (dict == null || !dict.getValue().equals(productCategoryId.toString())) {
                return;
            }
            int tags = detail.getSyncChargeTags();
            //应收已有费用的忽略
            if(detail.getTravelCharge() == 0 && detail.getEngineerTravelCharge() > 0){
                detail.setTravelCharge(detail.getEngineerTravelCharge());//远程费
                tags = BitUtils.addTags(tags,BitUtils.positionToTag(OrderUtils.SyncCustomerCharge.TRAVEL.ordinal()));
            }
            if(detail.getOtherCharge() == 0 && detail.getEngineerOtherCharge() > 0) {
                detail.setOtherCharge(detail.getEngineerOtherCharge());//其他费用
                tags = BitUtils.addTags(tags,BitUtils.positionToTag(OrderUtils.SyncCustomerCharge.OTHER.ordinal()));
            }
            detail.setSyncChargeTags(tags);
        }catch (Exception e){
            log.error("读取客户自动计算远程费品类配置错误,category:{}",productCategoryId,e);
        }
    }

    /**
     * 标记app异常
     *
     * @param orderId
     * @param quarter
     * @param servicePointId 网点id
     * @param pendingType    停滞类型
     * @param user           反馈用户
     * @param remarks        备注
     */
    @Transactional(readOnly = false)
    public void saveAppAbnormaly(Long orderId, String quarter, Long servicePointId, Dict pendingType, User user, String remarks) {
        String lockkey = null;
        Boolean locked = false;
        try {
            lockkey = String.format(RedisConstant.SD_ORDER_LOCK, orderId);
            //获得锁
            locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
            if (!locked) {
                throw new OrderException("此订单正在处理中，请稍候重试");
            }

            Date date = new Date();
            Order order = getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null || order.getItems() == null) {
                throw new OrderException("读取订单信息失败");
            }
            Integer statusValue = order.getOrderCondition().getStatusValue();
            if (statusValue != Order.ORDER_STATUS_SERVICED && statusValue != Order.ORDER_STATUS_PLANNED) {
                throw new OrderException("保存标记异常失败，订单状态错误");
            }

            OrderCondition condition = order.getOrderCondition();
            Integer orgAppAbnormalyFlag = condition.getAppAbnormalyFlag();
            if (0 == orgAppAbnormalyFlag) {
                HashMap<String, Object> params = Maps.newHashMap();
                params.put("orderId", orderId);
                params.put("quarter", order.getQuarter());
                params.put("appAbnormalyFlag", 1);
                dao.updateCondition(params);
                //同步网点工单数据
                Long spId = ofNullable(condition.getServicePoint()).map(t -> t.getId()).orElse(0L);
                servicePointOrderBusinessService.abnormalyFlag(
                        order.getId(),
                        order.getQuarter(),
                        spId,
                        1,
                        user.getId(),
                        date.getTime()
                );
            }

            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(order.getQuarter());
            processLog.setAction("进度跟踪");
            processLog.setOrderId(orderId);
            processLog.setActionComment("异常原因："
                    .concat(pendingType.getLabel().replace(",", "").replace("，", ""))
                    .concat("，备注：")
                    .concat(remarks));
            processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
            processLog.setStatus(condition.getStatus().getLabel());
            processLog.setStatusValue(condition.getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_TRACKING);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);

            //异常单
            AbnormalForm abnormalForm = null;
            try {
                AbnormalFormEnum.SubType subType = AbnormalFormEnum.SubType.fromCode(Integer.valueOf(pendingType.getValue()));
                if (subType == null) {
                    subType = AbnormalFormEnum.SubType.OLD_ABNORMAL_OPERATION;
                }
                abnormalForm = abnormalFormService.handleAbnormalForm(order, pendingType.getLabel(), user, AppFeedbackEnum.Channel.APP.getValue(),
                        AbnormalFormEnum.FormType.OLD_APP_ABNORMALY.getCode(), subType.getCode(), processLog.getActionComment());
                if (abnormalForm != null) {
                    abnormalForm.setOpinionLogId(0L);
                    abnormalFormService.save(abnormalForm);
                }
            } catch (Exception e) {
                log.error("[orderService.saveAppAbnormaly]保存异常单失败 form:{}", GsonUtils.getInstance().toGson(abnormalForm), e);
            }

            //region Notice Message
            if (orgAppAbnormalyFlag == 0 && condition.getKefu() != null) {
                try {

                    MQNoticeMessage.NoticeMessage message = MQNoticeMessage.NoticeMessage.newBuilder()
                            .setOrderId(condition.getOrderId())
                            .setQuarter(condition.getQuarter())
                            .setNoticeType(NoticeMessageConfig.NOTICE_TYPE_APPABNORMALY)
                            .setCustomerId(condition.getCustomer().getId())
                            .setKefuId(condition.getKefu().getId())
                            .setAreaId(condition.getArea().getId())
                            .setTriggerBy(MQWebSocketMessage.User.newBuilder()
                                    .setId(user.getId())
                                    .setName(user.getName())
                                    .build()
                            )
                            .setTriggerDate(date.getTime())
                            .setDelta(1)
                            .build();

                    try {
                        noticeMessageSender.send(message);
                    } catch (Exception e) {
                        //消息队列发送错误
                        log.error("[OrderService.saveAppabnormaly] send MQNoticeMessage,orderId:{} ,user:{} ,pendingType:{}", orderId, user.getId(), pendingType.getValue(), e);
                    }

                } catch (Exception e) {
                    log.error("[OrderService.saveAppabnormaly] send MQNoticeMessage,orderId:{} ,user:{} ,pendingType:{}", orderId, user.getId(), pendingType.getValue(), e);
                }
            }
            //endregion Notice Message

            //cache,淘汰,调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(orderId)
                    .setDeleteField(OrderCacheField.CONDITION);
            OrderCacheUtils.update(builder.build());

        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);//释放锁
            }
        }
    }

    /**
     * 工单完成(app)
     */
    @Transactional(readOnly = false)
    public RestResult saveOrderComplete(Order order, User user, Dict completeType, Long buyDate, String remarks) {
        long index = 0;
        if (order == null || order.getOrderCondition() == null || completeType == null || user == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "参数错误：为空");
        }
        //检查sub_status 2019/01/14
        OrderCondition condition = order.getOrderCondition();
        if (condition.getSubStatus() == Order.ORDER_SUBSTATUS_APPCOMPLETED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.message);
        }
        int orderStatusValue = condition.getStatusValue();
        if ( orderStatusValue == Order.ORDER_STATUS_APP_COMPLETED.intValue() || orderStatusValue == Order.ORDER_STATUS_COMPLETED.intValue() || orderStatusValue == Order.ORDER_STATUS_CHARGED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_FINISH_SERVICE.code, ErrorCode.ORDER_FINISH_SERVICE.message);
        } else if (orderStatusValue > Order.ORDER_STATUS_CHARGED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "订单已取消或已退单");
        }
        String lockkey = null;//锁
        Boolean locked = false;
        Boolean autoCompleteFlag = true;//自动完工标记
        try {
            Date date = new Date();
            lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getId());
            //获得锁
            locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
            if (!locked) {
                return RestResultGenerator.custom(ErrorCode.ORDER_REDIS_LOCKED.code, ErrorCode.ORDER_REDIS_LOCKED.message);
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("orderNo:").append(order.getOrderNo());
            buffer.append(" completeType:").append(completeType.getValue());

            //region check

            //检查状态
            int statusValue = condition.getStatusValue();
            if (statusValue != Order.ORDER_STATUS_SERVICED) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.message);
            } else {
                //TODO: APP完工[55]
                Dict appCompletedStatus =  MSDictUtils.getDictByValue(Order.ORDER_STATUS_APP_COMPLETED.toString(), Dict.DICT_TYPE_ORDER_STATUS);
                condition.setStatus(appCompletedStatus);
            }
            //检查未审核或未发货配件申请单
            // 根据订单配件状态检查是否可以客评 2019/06/13 22:56 at home
            MSResponse msResponse = orderMaterialService.canGradeOfMaterialForm(order.getDataSourceId(), order.getId(), order.getQuarter());
            if (!MSResponse.isSuccessCode(msResponse)) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "订单完成失败，" + msResponse.getMsg());
            }
            //检查客户要求完成照片数量
            Customer customer = customerService.getFromCache(order.getOrderCondition().getCustomer().getId());
            if (customer == null) {
                return RestResultGenerator.custom(ErrorCode.RECORD_NOT_EXIST.code, "客户不存在，或读取客户信息失败");
            }
            if (customer.getMinUploadNumber() > 0 && order.getOrderCondition().getFinishPhotoQty() < customer.getMinUploadNumber()) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "请先上传客户要求的最少服务效果图");
            }
            if (!checkOrderProductBarCode(order.getId(), order.getQuarter(), order.getOrderCondition().getCustomer().getId(), order.getDetailList())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "请先上传产品条码");
            }
            if (checkOrderProductBarCodeIsRepeat(order.getId(), order.getQuarter(), order.getOrderCondition().getCustomer().getId(), order.getDetailList())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "产品条码不能重复");
            }

            Integer orgAppAbnormaly = condition.getAppAbnormalyFlag();//如果原来异常，本次也异常，不需要累加异常数量
            buffer.append(" orgAppAbnormaly:").append(orgAppAbnormaly.toString());
            //品类
            ProductCategory category = condition.getProductCategory();
            int autoGradeFlag = Optional.ofNullable(condition.getProductCategory()).map(p->p.getAutoGradeFlag()).orElse(0);
            //完成类型
            Integer appAbnormalyFlag = 0;
            //2021/05/25 增加客户自动完工开关判断
            int customerAutoCompleteOrderFlag = Optional.ofNullable(customer).map(t->t.getAutoCompleteOrder()).orElse(0);
            if(customerAutoCompleteOrderFlag == 0){
                //客户自动完工开关未开启
                autoCompleteFlag = false;
                buffer.append(" 客户自动完工开关:关闭");
            } else if (1 == orgAppAbnormaly) {//订单已经app异常，改成短信回复客评
                autoCompleteFlag = false;
            } else if (!"compeled_all".equalsIgnoreCase(completeType.getValue()) && !"compeled_all_notest".equalsIgnoreCase(completeType.getValue())) {
                //已完成工单全部内容但未试机 不标记异常  2020-01-07
                autoCompleteFlag = false;
                appAbnormalyFlag = 1;
            } else if(autoGradeFlag == 0){
                //判断品类：autoGradeFlag
                buffer.append(" categoryAutoGradeFlag:").append(autoGradeFlag);
                autoCompleteFlag = false;
            }
            buffer.append(" appAbnormalyFlag:").append(appAbnormalyFlag.toString());
            if (true == autoCompleteFlag) {
                String checkResult = checkAutoComplete(order);
                buffer.append(" checkResult:").append(checkResult);
                if (StringUtils.isBlank(checkResult)) {
                    //网点自动完工检查
                    List<Long> points = order.getDetailList().stream()
                            .map(t -> t.getServicePoint().getId())
                            .distinct()
                            .collect(Collectors.toList());
                    ServicePoint servicePoint;
                    buffer.append(" servicepoint ids:");
                    if (null == points || 0 == points.size()) {
                        autoCompleteFlag = false;
                        buffer.append("[]{}");
                    } else {
                        buffer.append("[").append(StringUtils.join(points, ",")).append("]{");
                        for (int i = 0, size = points.size(); i < size; i++) {
                            servicePoint = servicePointService.getFromCache(points.get(i));
                            if (servicePoint == null) {
                                autoCompleteFlag = false;
                                buffer.append(" servicepointId:").append(points.get(i).toString()).append(" null get from redis");
                                buffer.append(" ,autoCompleteFlag:").append(autoCompleteFlag.toString());
                                break;
                            } else if (servicePoint.getAutoCompleteOrder() == 0) {
                                autoCompleteFlag = false;
                                buffer.append(" servicepointId:").append(points.get(i).toString()).append(" ,AutoCompleteOrder:").append(new Integer(servicePoint.getAutoCompleteOrder()).toString());
                                buffer.append(" ,autoCompleteFlag:").append(autoCompleteFlag.toString());
                                break;
                            }
                        }
                        buffer.append("}");
                    }
                } else {
                    autoCompleteFlag = false;
                }
            }

            //endregion check

            //region save to db
            HashMap<String, Object> params = Maps.newHashMap();
            condition.setPendingFlag(2);//正常
            condition.setPendingType(new Dict(0, ""));
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("appCompleteType", completeType.getValue().trim());//完工类型
            params.put("appCompleteDate", date);//完工日期

            params.put("pendingFlag", condition.getPendingFlag());
            params.put("pendingType", condition.getPendingType());
            if (1 == appAbnormalyFlag) {
                condition.setAppAbnormalyFlag(appAbnormalyFlag);//app异常
                params.put("appAbnormalyFlag", appAbnormalyFlag);
            }
            params.put("subStatus", Order.ORDER_SUBSTATUS_APPCOMPLETED);//Add by Zhoucy
            params.put("status", condition.getStatus());
            dao.updateCondition(params);
            if (buyDate != null && buyDate > 0) {
                orderAdditionalInfoService.updateBuyDate(order.getId(), order.getQuarter(), buyDate);
            }

            //更新未完工单数
            ServicePoint servicepoint = condition.getServicePoint();
            if(servicepoint!=null && servicepoint.getId()!=null && servicepoint.getId()>0){
                Map<String,Object> unfinishedOrderCountParamsMap = Maps.newHashMap();
                unfinishedOrderCountParamsMap.put("id",servicepoint.getId());
                unfinishedOrderCountParamsMap.put("unfinishedOrderCount",-1);
                try {
                    msServicePointService.updateUnfinishedOrderCountByMapForSD(unfinishedOrderCountParamsMap);
                }catch (Exception e){
                    String param = "servicePointId:"+servicepoint.getId()+",orderId:"+order.getId();
                    LogUtils.saveLog("更新网点未完工单数量失败","APP完工",param,e,user);
                }
            }

            if (1 == appAbnormalyFlag) {
                //意见跟踪日志
                OrderOpitionTrace opitionTrace = OrderOpitionTrace.builder()
                        .channel(AppFeedbackEnum.Channel.APP.getValue())
                        .quarter(order.getQuarter())
                        .orderId(order.getId())
                        .servicePointId(condition.getServicePoint().getId())
                        .appointmentAt(0)
                        .opinionId(0)
                        .parentId(0)
                        .opinionType(AppFeedbackEnum.FeedbackType.APP_COMPLETE.getValue())
                        .opinionValue(0)
                        .opinionLabel("App完工不符合自动完工条件，标记为异常")
                        .isAbnormaly(1)
                        .remark(StringUtils.left(completeType.getValue() + ": " + remarks, 250))
                        .createAt(System.currentTimeMillis())
                        .createBy(user)
                        .times(1)
                        .totalTimes(1)
                        .build();
                orderOpitionTraceService.insert(opitionTrace);

                //异常单
                Integer subType = 0;
                if (condition.getOrderServiceType() == 1) {
                    subType = AbnormalFormEnum.SubType.INSTALL_ERROR.code;
                } else {
                    subType = AbnormalFormEnum.SubType.REPAIR_ERROR.code;
                }
                String reason = completeType.getLabel() + ": " + remarks;
                AbnormalForm abnormalForm = abnormalFormService.handleAbnormalForm(order, reason, user, AppFeedbackEnum.Channel.APP.getValue(),
                        AbnormalFormEnum.FormType.APP_COMPLETE.code, subType, "App完工不符合自动完工条件");
                try {
                    if (abnormalForm != null) {
                        abnormalForm.setOpinionLogId(opitionTrace.getId());
                        abnormalFormService.save(abnormalForm);
                    }
                } catch (Exception e) {
                    log.error("[OrderService.SaveOrderComplete]app完工保存异常单失败 form:{}", GsonUtils.getInstance().toGson(abnormalForm), e);
                }
            }
            AuxiliaryMaterialMaster auxiliaryMaterialMaster= auxiliaryMaterialMasterDao.getAuxiliaryMaterialMasterPrice(order.getId(),order.getQuarter());
            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(order.getQuarter());
            processLog.setAction("安维完成");
            processLog.setOrderId(order.getId());
            double actualTotalCharge = auxiliaryMaterialMaster!=null && auxiliaryMaterialMaster.getActualTotalCharge()!=null ? auxiliaryMaterialMaster.getActualTotalCharge() : 0.0;
            processLog.setActionComment(String.format("%s,备注:%s。辅材收费: %.2f元", completeType.getLabel(), (StringUtils.isBlank(remarks) ? "无" : remarks), actualTotalCharge));
            processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
            processLog.setStatus(condition.getStatus().getLabel());
            processLog.setStatusValue(condition.getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setRemarks(remarks);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);

            //endregion save to db

            //region 消息队列

            //region 短信回访
            //增加检查客户短信发送开关，1:才发送 2018/04/12
            //未在配置中：shortmessage.ignore-data-sources  //2018-12-05
            List<String> ignoreDataSources = StringUtils.isBlank(smIgnoreDataSources) ? Lists.newArrayList() : Splitter.on(",").trimResults().splitToList(smIgnoreDataSources);
            if (1 == customer.getShortMessageFlag()
                    && 0 == appAbnormalyFlag
                    && false == autoCompleteFlag
                    && completeType.getValue().equalsIgnoreCase("compeled_all")
                    && !ignoreDataSources.contains(order.getDataSource().getValue())
            ) {
                buffer.append(" 发送客户客评短信");
                // 尊敬的用户，您的售后维修工单已经由张三师傅完成，请您直接回复数字对师傅的服务进行评价：
                // 1 非常满意 2 一般 3 不满意，谢谢您的支持！祝您生活愉快！
                StringBuffer strContent = new StringBuffer();
                //strContent.append("您的售后工单已完成，请回复数字对师傅的服务进行评价：1非常满意 ,2一般, 3不满意,4还有产品未完成，谢谢您的支持！");//old
                strContent.append("您的服务已完成，请回复数字对师傅评价：1满意 2一般 3不满意。您的差评，我们将考核师傅500元并停单培训一周，感谢您对服务的监督");//2019/06/03
                smsCallbackTaskMQSender.send(condition.getOrderId(), order.getQuarter(), condition.getServicePhone(), strContent.toString(), "", null, "", user.getId(), date.getTime());
            }
            //endregion 短信

            //region 智能回访
            //2019/01/18 更改：只要app完成就发语音回访
            //2019/01/21 更改：不自动完成的才发语音回访
            //2019/04/13 更改：有重单的不发语音回访
            if (voiceEnabled && !autoCompleteFlag && StringUtils.isNoneBlank(siteCode)
                    && StringUtils.isBlank(order.getRepeateNo())) {
                sendNewVoiceTaskMessage(siteCode, order, user.getName(), date);
            }
            //endregion 智能回访

            //region 异常统计
            if (1 == appAbnormalyFlag && 0 == orgAppAbnormaly) {
                buffer.append(" 发送异常统计消息");
                sendNoticeMessage(
                        NoticeMessageConfig.NOTICE_TYPE_APPABNORMALY,
                        order.getId(),
                        order.getQuarter(),
                        customer,
                        condition.getKefu(),
                        condition.getArea().getId(),
                        user,
                        date
                );
            }
            //endregion 异常统计

            b2BCenterOrderService.appCompleteOrder(order, user, date);

            //region 调用公共缓存

            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getId())
                    .setDeleteField(OrderCacheField.INFO)
                    .setDeleteField(OrderCacheField.CONDITION)
                    .setDeleteField(OrderCacheField.PENDING_TYPE)
                    .setDeleteField(OrderCacheField.PENDING_TYPE_DATE);
            OrderCacheUtils.update(builder.build());

            //endregion

            //region 自动完工
            if (true == autoCompleteFlag) {
                buffer.append(" 发送自动完工消息");
                //自动完工调用saveGrade，因此此处不需要发送B2B订单状态变更消息
                sendAutoCompleteMessage(order.getId(), order.getQuarter(), user, date);
            }
            //endregion 自动完工

            //region 网点订单数据更新 2019-03-25
            servicePointOrderBusinessService.appComplete(
                    order.getId(),
                    order.getQuarter(),
                    Order.ORDER_SUBSTATUS_APPCOMPLETED,
                    completeType.getValue(),
                    appAbnormalyFlag,
                    user.getId(),
                    date.getTime()
            );
            //endregion

            //region 消息队列

            log.info("app自动完工:{}", buffer.toString());
            if (false == autoCompleteFlag) {
                LogUtils.saveLog("app自动完工", "OrderService.SaveOrderComplete", buffer.toString(), null, null);
            }

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            log.error("[OrderService.SaveOrderComplete]=={}== orderId:{}", index, order.getId(), oe);
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.SaveOrderComplete]=={}== orderId:{}", index, order.getId(), e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);//释放锁
            }
        }
    }

    /**
     * 工单完成(app)
     */
    @Transactional(readOnly = false)
    public RestResult saveOrderCompleteV2(Order order, User user, Dict completeType, Long buyDate, String remarks) {
        long index = 0;
        if (order == null || order.getOrderCondition() == null || completeType == null || user == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "参数错误：为空");
        }
        //检查sub_status 2019/01/14
        OrderCondition condition = order.getOrderCondition();
        if (condition.getSubStatus() == Order.ORDER_SUBSTATUS_APPCOMPLETED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.message);
        }
        int orderStatusValue = condition.getStatusValue();
        if ( orderStatusValue == Order.ORDER_STATUS_APP_COMPLETED.intValue() || orderStatusValue == Order.ORDER_STATUS_COMPLETED.intValue() || orderStatusValue == Order.ORDER_STATUS_CHARGED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_FINISH_SERVICE.code, ErrorCode.ORDER_FINISH_SERVICE.message);
        } else if (orderStatusValue > Order.ORDER_STATUS_CHARGED.intValue()) {
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "订单已取消或已退单");
        }
        String lockkey = null;//锁
        Boolean locked = false;
        Boolean autoCompleteFlag = true;//自动完工标记
        try {
            Date date = new Date();
            lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getId());
            //获得锁
            locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
            if (!locked) {
                return RestResultGenerator.custom(ErrorCode.ORDER_REDIS_LOCKED.code, ErrorCode.ORDER_REDIS_LOCKED.message);
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("orderNo:").append(order.getOrderNo());
            buffer.append(" completeType:").append(completeType.getValue());

            //region check

            //检查状态
            int statusValue = condition.getStatusValue();
            if (statusValue != Order.ORDER_STATUS_SERVICED) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.message);
            } else {
                //TODO: APP完工[55]
                Dict appCompletedStatus =  MSDictUtils.getDictByValue(Order.ORDER_STATUS_APP_COMPLETED.toString(), Dict.DICT_TYPE_ORDER_STATUS);
                condition.setStatus(appCompletedStatus);
            }
            //检查未审核或未发货配件申请单
            // 根据订单配件状态检查是否可以客评 2019/06/13 22:56 at home
            MSResponse msResponse = orderMaterialService.canGradeOfMaterialForm(order.getDataSourceId(), order.getId(), order.getQuarter());
            if (!MSResponse.isSuccessCode(msResponse)) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "订单完成失败，" + msResponse.getMsg());
            }
            //检查客户要求完成照片数量
            Customer customer = customerService.getFromCache(order.getOrderCondition().getCustomer().getId());
            if (customer == null) {
                return RestResultGenerator.custom(ErrorCode.RECORD_NOT_EXIST.code, "客户不存在，或读取客户信息失败");
            }
            if (customer.getMinUploadNumber() > 0 && order.getOrderCondition().getFinishPhotoQty() < customer.getMinUploadNumber()) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "请先上传客户要求的最少服务效果图");
            }
            if (!checkOrderProductBarCode(order.getId(), order.getQuarter(), order.getOrderCondition().getCustomer().getId(), order.getDetailList())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "请先上传产品条码");
            }
            if (checkOrderProductBarCodeIsRepeat(order.getId(), order.getQuarter(), order.getOrderCondition().getCustomer().getId(), order.getDetailList())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEORDERCOMPLETE.code, "产品条码不能重复");
            }

            Integer orgAppAbnormaly = condition.getAppAbnormalyFlag();//如果原来异常，本次也异常，不需要累加异常数量


            //完成类型
            Integer appAbnormalyFlag = 0;
            //2021/05/25 增加客户自动完工开关判断
            int customerAutoCompleteOrderFlag = Optional.ofNullable(customer).map(t->t.getAutoCompleteOrder()).orElse(0);
            if(customerAutoCompleteOrderFlag == 0){
                //客户自动完工开关未开启

                buffer.append(" 客户自动完工开关:关闭");
            }  else if (!"compeled_all".equalsIgnoreCase(completeType.getValue()) && !"compeled_all_notest".equalsIgnoreCase(completeType.getValue())) {
                //已完成工单全部内容但未试机 不标记异常  2020-01-07
                appAbnormalyFlag = 1;
            }
            buffer.append(" appAbnormalyFlag:").append(appAbnormalyFlag.toString());


            //endregion check

            //region save to db
            HashMap<String, Object> params = Maps.newHashMap();
            condition.setPendingFlag(2);//正常
            condition.setPendingType(new Dict(0, ""));
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("appCompleteType", completeType.getValue().trim());//完工类型
            params.put("appCompleteDate", date);//完工日期

            params.put("pendingFlag", condition.getPendingFlag());
            params.put("pendingType", condition.getPendingType());
            if (1 == appAbnormalyFlag) {
                condition.setAppAbnormalyFlag(appAbnormalyFlag);//app异常
                params.put("appAbnormalyFlag", appAbnormalyFlag);
            }
            params.put("subStatus", Order.ORDER_SUBSTATUS_APPCOMPLETED);//Add by Zhoucy
            params.put("status", condition.getStatus());
            dao.updateCondition(params);
            if (buyDate != null && buyDate > 0) {
                orderAdditionalInfoService.updateBuyDate(order.getId(), order.getQuarter(), buyDate);
            }

            //更新未完工单数
            ServicePoint servicepoint = condition.getServicePoint();
            if(servicepoint!=null && servicepoint.getId()!=null && servicepoint.getId()>0){
                Map<String,Object> unfinishedOrderCountParamsMap = Maps.newHashMap();
                unfinishedOrderCountParamsMap.put("id",servicepoint.getId());
                unfinishedOrderCountParamsMap.put("unfinishedOrderCount",-1);
                try {
                    msServicePointService.updateUnfinishedOrderCountByMapForSD(unfinishedOrderCountParamsMap);
                }catch (Exception e){
                    String param = "servicePointId:"+servicepoint.getId()+",orderId:"+order.getId();
                    LogUtils.saveLog("更新网点未完工单数量失败","APP完工",param,e,user);
                }
            }

            if (1 == appAbnormalyFlag) {
                //意见跟踪日志
                OrderOpitionTrace opitionTrace = OrderOpitionTrace.builder()
                        .channel(AppFeedbackEnum.Channel.APP.getValue())
                        .quarter(order.getQuarter())
                        .orderId(order.getId())
                        .servicePointId(condition.getServicePoint().getId())
                        .appointmentAt(0)
                        .opinionId(0)
                        .parentId(0)
                        .opinionType(AppFeedbackEnum.FeedbackType.APP_COMPLETE.getValue())
                        .opinionValue(0)
                        .opinionLabel("App完工不符合自动完工条件，标记为异常")
                        .isAbnormaly(1)
                        .remark(StringUtils.left(completeType.getValue() + ": " + remarks, 250))
                        .createAt(System.currentTimeMillis())
                        .createBy(user)
                        .times(1)
                        .totalTimes(1)
                        .build();
                orderOpitionTraceService.insert(opitionTrace);

                //异常单
                Integer subType = 0;
                if (condition.getOrderServiceType() == 1) {
                    subType = AbnormalFormEnum.SubType.INSTALL_ERROR.code;
                } else {
                    subType = AbnormalFormEnum.SubType.REPAIR_ERROR.code;
                }
                String reason = completeType.getLabel() + ": " + remarks;
                AbnormalForm abnormalForm = abnormalFormService.handleAbnormalForm(order, reason, user, AppFeedbackEnum.Channel.APP.getValue(),
                        AbnormalFormEnum.FormType.APP_COMPLETE.code, subType, "App完工不符合自动完工条件");
                try {
                    if (abnormalForm != null) {
                        abnormalForm.setOpinionLogId(opitionTrace.getId());
                        abnormalFormService.save(abnormalForm);
                    }
                } catch (Exception e) {
                    log.error("[OrderService.SaveOrderComplete]app完工保存异常单失败 form:{}", GsonUtils.getInstance().toGson(abnormalForm), e);
                }
            }
            AuxiliaryMaterialMaster auxiliaryMaterialMaster= auxiliaryMaterialMasterDao.getAuxiliaryMaterialMasterPrice(order.getId(),order.getQuarter());
            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(order.getQuarter());
            processLog.setAction("安维完成");
            processLog.setOrderId(order.getId());
            double actualTotalCharge = auxiliaryMaterialMaster!=null && auxiliaryMaterialMaster.getActualTotalCharge()!=null ? auxiliaryMaterialMaster.getActualTotalCharge() : 0.0;
            processLog.setActionComment(String.format("%s,备注:%s。辅材收费: %.2f元", completeType.getLabel(), (StringUtils.isBlank(remarks) ? "无" : remarks), actualTotalCharge));
            processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
            processLog.setStatus(condition.getStatus().getLabel());
            processLog.setStatusValue(condition.getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setRemarks(remarks);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);

            //endregion save to db

            //region 消息队列

            //region 异常统计
            if (1 == appAbnormalyFlag && 0 == orgAppAbnormaly) {
                buffer.append(" 发送异常统计消息");
                sendNoticeMessage(
                        NoticeMessageConfig.NOTICE_TYPE_APPABNORMALY,
                        order.getId(),
                        order.getQuarter(),
                        customer,
                        condition.getKefu(),
                        condition.getArea().getId(),
                        user,
                        date
                );
            }
            //endregion 异常统计

            b2BCenterOrderService.appCompleteOrder(order, user, date);

            //region 调用公共缓存

            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getId())
                    .setDeleteField(OrderCacheField.INFO)
                    .setDeleteField(OrderCacheField.CONDITION)
                    .setDeleteField(OrderCacheField.PENDING_TYPE)
                    .setDeleteField(OrderCacheField.PENDING_TYPE_DATE);
            OrderCacheUtils.update(builder.build());

            //endregion


            //region 网点订单数据更新 2019-03-25
            servicePointOrderBusinessService.appComplete(
                    order.getId(),
                    order.getQuarter(),
                    Order.ORDER_SUBSTATUS_APPCOMPLETED,
                    completeType.getValue(),
                    appAbnormalyFlag,
                    user.getId(),
                    date.getTime()
            );
            //endregion
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            log.error("[OrderService.SaveOrderComplete]=={}== orderId:{}", index, order.getId(), oe);
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.SaveOrderComplete]=={}== orderId:{}", index, order.getId(), e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);//释放锁
            }
        }
    }

    /**
     * 电话联系用户
     */
    @Transactional(readOnly = false)
    public RestResult saveCallUser(Order order, User user) {
        if (order == null || order.getOrderCondition() == null || order.getOrderStatus() == null || user == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "参数错误：为空");
        }
        String lockkey = null;//锁
        Boolean locked = false;
        try {
            Date date = new Date();
            lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getId());
            //获得锁
            locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
            if (!locked) {
                return RestResultGenerator.custom(ErrorCode.ORDER_REDIS_LOCKED.code, ErrorCode.ORDER_REDIS_LOCKED.message);
            }
            if (order.getOrderStatus().getFirstContactDate() == null) {
                HashMap<String, Object> params = Maps.newHashMap();
                params.put("orderId", order.getId());
                params.put("quarter", order.getQuarter());
                params.put("firstContactDate", new Date());
                dao.updateStatus(params);
            }
            //log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(order.getQuarter());
            processLog.setOrderId(order.getId());
            processLog.setAction("安维联系用户");
            processLog.setActionComment("安维人员APP联系用户");
            processLog.setStatus(order.getOrderCondition().getStatus().getLabel());
            processLog.setStatusValue(order.getOrderCondition().getStatusValue());
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setRemarks("安维人员APP联系用户");
//            dao.insertProcessLog(processLog);
            processLog.setCustomerId(order.getOrderCondition() != null ? order.getOrderCondition().getCustomerId() : 0);
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);
            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getId())
                    .setDeleteField(OrderCacheField.ORDER_STATUS);
            OrderCacheUtils.update(builder.build());

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);//释放锁
            }
        }
    }

    /**
     * 检查是否可以自动完工
     *
     * @param order
     * @return String, 检查结果，空字符串代表：可以
     * <p>
     * (x)2018/06/04 Ryan (2018/06/06 cancel)
     * 有保险费，时效奖金，不能自动完工
     */
    public String checkAutoComplete(Order order) {
        //1.单服务项目
        if (order.getDetailList().stream().filter(t -> t.getDelFlag() == 0).count() > 1) {
            return "此订单不符合自动完工要求:多个上门服务项";
        }

        OrderDetail detail = order.getDetailList().stream().filter(t -> t.getDelFlag() == 0).findFirst().orElse(null);
        if (detail == null) {
            return "此订单不符合自动完工要求:无上门服务项";
        }
        //2.数量1
        if (detail.getQty() > 1) {
            return "此订单不符合自动完工要求:上门服务项数量大于1";
        }
        //3.下单和上门是否一致
        StringBuilder itemstring = new StringBuilder(100);
        order.getItems().stream()
                .sorted(Comparator.comparing(OrderItem::getProductId))
                .forEach(t -> {
                    itemstring.append("S#").append(t.getServiceType().getId()).append("#S")
                            .append("P#").append(t.getProduct().getId()).append("#P")
                            .append("Q#").append(t.getQty()).append("#Q");
                });
        //实际上门明细,只读取安装项目
        StringBuilder detailstring = new StringBuilder(200);
        detailstring.append("S#").append(detail.getServiceType().getId()).append("#S")
                .append("P#").append(detail.getProduct().getId()).append("#P")
                .append("Q#").append(detail.getQty()).append("#Q");
        if (!itemstring.toString().equalsIgnoreCase(detailstring.toString())) {
            return "下单的项目与实际上门不一致";
        }

        //4.安装单
        String azCode = new String("II");
        //mark on 2019-10-11
       /* List<ServiceType> serviceTypes = serviceTypeService.findAllList();
        ServiceType azServiceType = serviceTypes.stream().filter(t -> t.getCode().equalsIgnoreCase(azCode)).findFirst().orElse(null);
        if (azServiceType == null) {
            return "此订单不符合自动完工要求:非安装单";
        }*/
        // 调用微服务获取服务类型,只返回id和code start on 2019-10-11
        Map<Long, String> map = serviceTypeService.findIdsAndCodes();
        Long serviceTypeId = map.entrySet().stream().filter(t -> t.getValue().equalsIgnoreCase(azCode)).map(t -> t.getKey()).findFirst().orElse(null);
        if (serviceTypeId == null || serviceTypeId <= 0) {
            return "此订单不符合自动完工要求:非安装单";
        }
        //end
        //final Long azServiceTypeId = azServiceType.getId();
        final Long azServiceTypeId = serviceTypeId;
        if (!detail.getServiceType().getId().equals(azServiceTypeId)) {
            return "此订单不符合自动完工要求:非安装单";
        }
        //5.单品
        Product product = productService.getProductByIdFromCache(detail.getProductId());
        if (product == null || product.getSetFlag() == 1) {
            return "此订单不符合自动完工要求:非单品";
        }
        //6.应收金额一致,项次数量一致 才可自动生成对帐单（也有可能出现安装单->维修单）
        OrderFee orderFee = order.getOrderFee();
        if (!Objects.equals(orderFee.getExpectCharge() - orderFee.getCustomerUrgentCharge(), orderFee.getOrderCharge())
                || order.getDetailList().size() != order.getItems().size()) {
            return "此订单不符合自动完工要求:下单项目与实际上门服务项目记录数不一致";
        }
        //7.实际上门明细费用判断，有配件费、其它、远程费、快递费的不能自动对账
        //应收
        if (detail.getMaterialCharge() > 0 || detail.getOtherCharge() > 0
                || detail.getTravelCharge() > 0 || detail.getExpressCharge() > 0) {
            return "此订单不符合自动完工要求:应收有服务费外的其他费用";
        }
        //应付
        if (detail.getEngineerMaterialCharge() > 0 || detail.getEngineerOtherCharge() > 0 ||
                detail.getEngineerTravelCharge() > 0 || detail.getEngineerExpressCharge() > 0) {
            return "此订单不符合自动完工要求:应付有服务费外的其他费用";
        }
        /*8.有保险费，时效奖金，加急费，不能自动完工
        OrderFee fee = order.getOrderFee();
        if(fee != null){
            //保险
            if(fee.getInsuranceCharge() != 0){
                return "此订单不符合自动完工要求:网点有购买保险";
            }
            //网点时效费
            if(fee.getTimeLinessCharge() != 0){
                return "此订单不符合自动完工要求:网点有时效费用";
            }
            //客户应收时效费
            if(fee.getCustomerTimeLinessCharge() != 0){
                return "此订单不符合自动完工要求:客户有应收时效费用";
            }
            //加急费
        }*/
        return "";
    }


    /**
     * 获得订单产品列表
     *
     * @param items      订单项
     * @param spliteSet  套组是否拆分
     * @param includeSet 是否返回套组id
     * @return
     */
    public Set<Product> getOrderProducts(List<OrderItem> items, Boolean spliteSet, Boolean includeSet) {
        Set<Product> ids = Sets.newHashSet();
        if (items == null || items.size() == 0) {
            return ids;
        }
        StringBuffer sb = new StringBuffer();
        items.stream().map(t -> t.getProduct().getId()).distinct().forEach(
                t -> {
                    Product p = productService.getProductByIdFromCache(t);
                    if (p != null) {
                        if (p.getSetFlag() == 1) {//set
                            if (includeSet) {
                                ids.add(p);
                            }
                            if (spliteSet) {
                                final String[] sids = p.getProductIds().split(",");
                                for (String id : sids) {
                                    if (StringUtils.isNoneBlank(id)) {
                                        Product p1 = productService.getProductByIdFromCache(Long.valueOf(id));
                                        if (p1 != null) {
                                            ids.add(p1);
                                        }
                                    }
                                }
                            }
                        } else {
                            ids.add(p);
                        }
                    }
                }
        );
        return ids;
    }

    /**
     * 获得下单时订单项产品是套组，且包含指定产品
     *
     * @param productId 组成产品
     * @param items     订单项
     * @return
     */
    public long[] getSetProductIdIncludeMe(Long productId, List<OrderItem> items) {
        List<Long> ids = Lists.newArrayList();
        if (productId == null || productId <= 0 || items == null || items.size() == 0) {
            return new long[]{};
        }
        StringBuffer sb = new StringBuffer();
        items.stream().map(t -> t.getProduct().getId()).distinct().forEach(
                t -> {
                    Product p = productService.getProductByIdFromCache(t);
                    if (p.getSetFlag() == 1) {
                        sb.setLength(0);
                        sb.append(",".concat(p.getProductIds()).concat(","));
                        if (sb.toString().contains(String.format(",%s,", productId.toString()))) {
                            ids.add(t);
                        }
                    }
                }
        );
        return ids.stream().mapToLong(i -> i).toArray();
    }

    /**
     * 保存工单日志（计算日志可见性标记值）
     */
    @Transactional(readOnly = false)
    public void saveOrderProcessLogNew(OrderProcessLog processLog) {
        if (processLog.getVisibilityFlag() == null || processLog.getVisibilityFlag() == VisibilityFlagEnum.NONE.getValue()) {
            int visiblityValue = OrderUtils.calcProcessLogVisibilityFlag(processLog);
            processLog.setVisibilityFlag(visiblityValue);
        }
        saveOrderProcessLogWithNoCalcVisibility(processLog);
    }

    /**
     * 保存工单日志（不计算日志可见性标记值）
     */
    @Transactional(readOnly = false)
    public void saveOrderProcessLogWithNoCalcVisibility(OrderProcessLog processLog) {
        dao.insertProcessLog(processLog);
        b2BCenterOrderProcessLogService.pushOrderProcessLogToMS(processLog);
    }

    /**
     * 返回产品及其服务项目组成的键值对列表
     *
     * @param items 服务项目列表
     * @return
     */
    public List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> getOrderDetailProductAndServiceTypePairs(List<OrderDetail> items) {
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        Set<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> valuePairs = items.stream().map(t -> {
            return new com.kkl.kklplus.entity.common.NameValuePair<Long, Long>(t.getProductId(), t.getServiceType().getId());
        }).collect(Collectors.toSet());
        return new ArrayList<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>>(valuePairs);
    }

    /**
     * 返回产品及其服务项目组成的键值对列表
     *
     * @param items 服务项目列表
     * @return
     */
    public List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> getOrderItemProductAndServiceTypePairs(List<OrderItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        Set<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> valuePairs = items.stream().map(t -> {
            return new com.kkl.kklplus.entity.common.NameValuePair<Long, Long>(t.getProductId(), t.getServiceType().getId());
        }).collect(Collectors.toSet());
        return new ArrayList<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>>(valuePairs);
    }


    /**
     * 网点派单给安维人员
     *
     * @param order
     */
    @Transactional(readOnly = false)
    public void servicePointPlanOrder(Order order) {
        if (order == null || order.getId() == null) {
            throw new OrderException("派单失败：参数无值。");
        }

        Long servicePointId = Optional.ofNullable(order.getOrderCondition()).map(s->s.getServicePoint()).map(s->s.getId()).orElse(0L);
        if(servicePointId == 0){
            throw new OrderException("读取网点编号失败，请联系客服!");
        }
        //新派师傅
        Long engineerId = Optional.ofNullable(order.getOrderCondition()).map(o->o.getEngineer()).map(e->e.getId()).orElse(0L);
        if(engineerId ==0){
            throw new OrderException("读取安维信息失败，请联系客服");
        }
        Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
        if (engineer == null) {
            throw new OrderException(String.format("未找到安维:%s的信息", engineerId.toString()));
        }

        User user = order.getCreateBy();

        Order o = null;
        try {
            o = getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
        } catch (Exception e) {
            //LogUtils.saveLog("网点派单错误", "OrderService.servicePointPlanOrder.getOrderById", "order id:" + order.getId().toString(), e, user);
        }

        if (o == null || o.getOrderCondition() == null) {
            throw new OrderException("确认订单信息失败。");
        }

        if (!o.canPlanOrder()) {
            throw new OrderException("该订单不能派单，请刷新页面查看订单是否已取消。");
        }

        //已派师傅
        Long oldEngineerId = Optional.ofNullable(o.getOrderCondition()).map(t->t.getEngineer()).map(e->e.getId()).orElse(0L);

        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getId());
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新页面。");
        }

        try {
            Date date = new Date();
            HashMap<String, Object> params = Maps.newHashMap();
            String label = MSDictUtils.getDictLabel(String.valueOf(Order.ORDER_STATUS_PLANNED), "order_status", "已派单");//切换为微服务
            Dict status = new Dict(Order.ORDER_STATUS_PLANNED, label);
            params.put("quarter", o.getQuarter());
            params.put("orderId", order.getId());
            //params.put("operationAppFlag",0);
            params.put("engineer", engineer);
            params.put("updateBy", user);
            params.put("updateDate", date);
            dao.updateCondition(params);

            //log,派单
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setQuarter(o.getQuarter());
            processLog.setAction("派单");
            processLog.setOrderId(order.getId());
            processLog.setActionComment(String.format("安维网点派单给安维人员:%s,操作人:%s,备注:%s", engineer.getName(), user.getName(), order.getRemarks()));
            processLog.setActionComment(StringUtils.left(processLog.getActionComment(), 255));
            processLog.setStatus(status.getLabel());
            processLog.setStatusValue(Integer.parseInt(status.getValue()));
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setCustomerId(o.getOrderCondition().getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);
            //更新接单数安维
            //新师傅
            params.put("id", engineer.getId());//新安维帐号
            params.put("planCount", 1);//派单数+1
            params.put("updateBy", user);
            params.put("updateDate", date);
            msEngineerService.updateEngineerByMap(params);  // add on 2019-10-18 //Engineer微服务

            //原师傅
            if (oldEngineerId != null) {
                params.remove("id");
                params.put("id", oldEngineerId);//原安维帐号
                params.remove("planCount");
                params.put("planCount", -1);//派单数-1
            }
            msEngineerService.updateEngineerByMap(params);  // add on 2019-10-18 //Engineer微服务

            //update order condition
            OrderCondition rediscondition = o.getOrderCondition();
            User engineerUser = new User();
            engineerUser.setId(engineer.getId());
            engineerUser.setName(engineer.getName());
            engineerUser.setMobile(engineer.getContactInfo());//2017/09/21

            rediscondition.setEngineer(engineerUser);
            rediscondition.setUpdateBy(user);
            rediscondition.setUpdateDate(date);

            // 原来的安维人员派单量-1，新的+1
            engineer.setPlanCount(engineer.getPlanCount() + 1);

            //派单记录表 2018/01/24
            Integer nextPlanTimes = dao.getOrderPlanMaxTimes(o.getId(), o.getQuarter());
            if (nextPlanTimes == null) {
                nextPlanTimes = 1;
            } else {
                nextPlanTimes++;//+1
            }
            //prev
            OrderPlan preOrderPlan = dao.getOrderPlan(o.getId(), o.getQuarter(), servicePointId, oldEngineerId);
            Double serviceCost = 0.0;
            if (preOrderPlan == null) {
                serviceCost = calcServicePointCost(order.getOrderCondition().getServicePoint(), o.getItems());
            } else {
                serviceCost = preOrderPlan.getEstimatedServiceCost();
            }
            OrderPlan orderPlan = dao.getOrderPlan(o.getId(), o.getQuarter(), servicePointId, engineer.getId());
            if (orderPlan == null || orderPlan.getId() == null) {
                orderPlan = new OrderPlan();
                orderPlan.setQuarter(o.getQuarter());
                orderPlan.setOrderId(o.getId());
                orderPlan.setServicePoint(order.getOrderCondition().getServicePoint());
                orderPlan.setEngineer(engineer);
                orderPlan.setIsMaster(0);//*
                orderPlan.setPlanTimes(nextPlanTimes);//*
                orderPlan.setCreateBy(user);
                orderPlan.setCreateDate(date);
                orderPlan.setUpdateBy(new User(0l));
                //同网点,与前次相同
                if (preOrderPlan != null) {
                    orderPlan.setEstimatedServiceCost(preOrderPlan.getEstimatedServiceCost());
                    orderPlan.setEstimatedDistance(preOrderPlan.getEstimatedDistance());
                    orderPlan.setEstimatedOtherCost(preOrderPlan.getEstimatedOtherCost());
                } else {
                    orderPlan.setEstimatedServiceCost(serviceCost);
                    orderPlan.setEstimatedDistance(0.0);
                    orderPlan.setEstimatedOtherCost(0.0);
                }

                dao.insertOrderPlan(orderPlan);
            } else {
                HashMap<String, Object> planMaps = Maps.newHashMap();
                planMaps.put("id", orderPlan.getId());
                planMaps.put("planTimes", nextPlanTimes);
                if (preOrderPlan != null) {
                    planMaps.put("estimatedServiceCost", preOrderPlan.getEstimatedServiceCost());//服务费
                    planMaps.put("estimatedDistance", preOrderPlan.getEstimatedDistance());//距离
                    planMaps.put("estimatedOtherCost", preOrderPlan.getEstimatedOtherCost());//其它费用
                } else {
                    planMaps.put("estimatedServiceCost", serviceCost);//服务费
                    planMaps.put("estimatedDistance", 0.0);//距离
                    planMaps.put("estimatedOtherCost", 0.0);//其它费用
                }
                planMaps.put("updateBy", user);
                planMaps.put("updateDate", date);
                dao.UpdateOrderPlan(planMaps);
            }

            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getId())
                    .incrVersion(1L)
                    .setCondition(rediscondition)
                    .setExpireSeconds(0L);
            OrderCacheUtils.update(builder.build());

            //派单时通知B2B
            b2BCenterOrderService.planOrder(o, engineer, user, date);
            b2BCenterOrderService.servicePointPlanOrder(o, engineer, user, date);

            //region 消息队列
            // 短信通知
            // 发送用户短信
            //未在配置中：shortmessage.ignore-data-sources  //2018-12-05
            List<String> ignoreDataSources = StringUtils.isBlank(smIgnoreDataSources) ? Lists.newArrayList() : Splitter.on(",").trimResults().splitToList(smIgnoreDataSources);
            if (!ignoreDataSources.contains(o.getDataSource().getValue()) && order.getSendUserMessageFlag() != null && order.getSendUserMessageFlag() == 1) {
                StringBuffer userContent = new StringBuffer();
                // 派单后给用户发送短信
                try {
                    if (engineer.getAppFlag() == 0)// 无APP的师傅人工派单给用户短信
                    {
                        // 您好！您有华帝吸油烟机1台需要維修，已为您安排何师傅13396963302。如48小时内未接到电话或在服务过程中有疑问，
                        // 请致电客服黄小姐0757-26169178/400-666-3653（9:00～18:00）。
                        // 祝您生活愉快！
                        // 2019-07-18
                        //您的优盟燃气热水器1台安装，罗师傅18962284455已接单,客服李小姐0757-29235638/4006663653
                        userContent.append("您的");
                        OrderItem item;
                        for (int i = 0, size = o.getItems().size(); i < size; i++) {
                            item = o.getItems().get(i);
                            userContent
                                    .append(item.getBrand())
                                    .append(com.wolfking.jeesite.common.utils.StringUtils2.getStandardProductName(item.getProduct().getName()))
                                    .append(item.getQty())
                                    .append(item.getProduct().getSetFlag() == 0 ? "台" : "套")
                                    .append(item.getServiceType().getName())
                                    .append((i == (size - 1)) ? "" : " ");
                        }
                        userContent.append("，");
                        userContent.append(engineer.getName().substring(0, 1));
                        userContent.append("师傅").append(engineer.getContactInfo())
                                .append("已接单，");
                        if (rediscondition.getKefu() != null) {
                            userContent
                                    .append("客服")
                                    .append(rediscondition.getKefu().getName().substring(0, 1)).append("小姐")
                                    .append(rediscondition.getKefu().getPhone())
                                    .append("/");
                        }
                        userContent.append(MSDictUtils.getDictSingleValue("400ServicePhone", "4006663653"));
                        // 使用新的短信发送方法 2019/02/28
                        smsMQSender.sendNew(rediscondition.getServicePhone(),
                                userContent.toString(),
                                "",
                                user.getId(),
                                date.getTime(),
                                SysSMSTypeEnum.ORDER_PLANNED_SERVICE_POINT
                        );
                    } else {
                        //使用过APP的师傅短信  09-27 by kody
                        // 2019-07-18
                        //您的优盟燃气热水器1台安装，罗师傅18962284455已接单,客服李小姐0757-29235638/4006663653
                        userContent.append("您的");
                        OrderItem item;
                        for (int i = 0, size = o.getItems().size(); i < size; i++) {
                            item = o.getItems().get(i);
                            userContent
                                    .append(item.getBrand())
                                    .append(com.wolfking.jeesite.common.utils.StringUtils2.getStandardProductName(item.getProduct().getName()))
                                    .append(item.getQty())
                                    .append(item.getProduct().getSetFlag() == 0 ? "台" : "套")
                                    .append(item.getServiceType().getName())
                                    .append((i == (size - 1)) ? "" : " ");
                        }
                        userContent.append("，");
                        userContent.append(engineer.getName().substring(0, 1));
                        userContent.append("师傅").append(engineer.getContactInfo()).append("已接单,");
                        if (rediscondition.getKefu() != null) {
                            userContent
                                    .append("客服")
                                    .append(rediscondition.getKefu().getName().substring(0, 1)).append("小姐")
                                    .append(rediscondition.getKefu().getPhone())
                                    .append("/");
                        }
                        userContent.append(MSDictUtils.getDictSingleValue("400ServicePhone", "4006663653"));
                        // 使用新的短信发送方法 2019/02/28
                        smsMQSender.sendNew(rediscondition.getServicePhone(),
                                userContent.toString(),
                                "",
                                user.getId(),
                                date.getTime(),
                                SysSMSTypeEnum.ORDER_PLANNED_SERVICE_POINT
                        );
                    }
                } catch (Exception e) {
                    LogUtils.saveLog(
                            "网点派单-发送短信失败",
                            "OrderService.servicePointPlanOrder",
                            MessageFormat.format("mobile:{0},content:{1},triggerBy:{2},triggerDate:{3}", rediscondition.getServicePhone(), userContent.toString(), user.getId(), date.getTime()),
                            e,
                            user
                    );
                }
            }

            //APP通知 2018/01/10
            try {
                User engieerAccount = systemService.getUserByEngineerId(engineer.getId());// 变更从cashe中取
                if (engieerAccount != null && engieerAccount.getAppLoged() == 1) {
                    // 张三师傅，在您附近有一张上门安装百得油烟机的工单，请尽快登陆APP接单~
                    try {
                        //将推送切换为微服务
                        AppPushMessage pushMessage = new AppPushMessage();
                        pushMessage.setPassThroughType(AppPushMessage.PassThroughType.NOTIFICATION);
                        pushMessage.setMessageType(AppMessageType.PLANORDER);
                        pushMessage.setSubject("");
                        pushMessage.setContent("");
                        pushMessage.setTimestamp(System.currentTimeMillis());
                        pushMessage.setUserId(engieerAccount.getId());
                        pushMessage.setDescription(engieerAccount.getName().substring(0, 1).concat("师傅,有新单派给您，请及时打开APP进行查看处理"));
                        appMessagePushService.sendMessage(pushMessage);

                    } catch (Exception e) {
                        log.error("[OrderService.servicePointPlanOrder]app notice - uid:{}", engineer.getId(), e);
                    }
                }
            } catch (Exception e) {
                log.error("[OrderService.servicePointPlanOrder]app notice - uid:{} ,msg:{}{}", engineer.getId(), engineer.getName().substring(0, 1), "师傅,有新单派给您，请及时打开APP进行查看处理", e);
            }

            //region 网点订单更新  2019-03-25
            servicePointOrderBusinessService.changeEngineer(o.getId(), o.getQuarter(), servicePointId, engineerId, engineer.getMasterFlag(), user.getId(), date.getTime());
            //endregion

            //endregion 消息队列


        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.servicePointPlanOrder] orderId:{} ,servicePointId:{} ,engineerId:{}", order.getId(), servicePointId, engineerId, e);
            throw new RuntimeException("网点派单错误:" + e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

    /**
     * app网点接单
     * order.orderCondition.engineer,servicePoint,customer 在传入前，需从缓存中获得完整内容
     * <p>
     * 主帐号才能接单，且appFlag=1
     *
     * @param order
     */
    @Transactional(readOnly = false)
    public void grabOrder(Order order, User user, Engineer engineer) {
        if (order == null || order.getId() == null) {
            throw new OrderException("接单失败：参数无值。");
        }
        if (user == null || engineer == null) {
            throw new OrderException("接单失败：无帐号信息。");
        }
        if (engineer.getServicePoint() == null) {
            throw new OrderException("接单失败：无网点信息。");
        }
        //锁
        long releaseLockTime = 5;//5秒后锁过期
        String lockkey = String.format(RedisConstant.SD_ORDER_LOCK, order.getId());
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, ORDER_LOCK_EXPIRED);//60秒
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新订单。");
        }

        try {
            //再取订单状态
            Map<String, Object> conMap = dao.getOrderConditionSpecialFromMasterById(order.getId(), order.getQuarter());
            Integer statusValue = (Integer) conMap.get("status_value");
            if (statusValue >= Order.ORDER_STATUS_PLANNED) {
                throw new OrderException("接单失败,订单已被其他网点接单");
            }
            Order o = getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.STATUS, true);
            if (o == null || o.getOrderCondition() == null) {
                throw new OrderException("接单失败,读取订单错误，请重试");
            }
            OrderCondition condition = order.getOrderCondition();
            String key = String.format(RedisConstant.SD_ORDER, order.getId());
            Integer prevStatus = order.getOrderCondition().getStatusValue();
            Date date = new Date();
            HashMap<String, Object> params = Maps.newHashMap();

            Dict status = MSDictUtils.getDictByValue(Order.ORDER_STATUS_PLANNED.toString(), "order_status");//切换为微服务
            if (status == null) {
                status = new Dict(Order.ORDER_STATUS_PLANNED.toString(), "已派单");
            }
            ServicePoint servicePoint = engineer.getServicePoint();
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("operationAppFlag", 1);//app接单
            params.put("servicePoint", servicePoint);
            params.put("engineer", engineer);
            params.put("status", status);
            params.put("updateBy", user);
            params.put("updateDate", date);
            //网点接单时，appointment_date=null、pending_type = 0、reservation_date = pending_type_date = now、sub_status = 10
            params.put("resetAppointmentDate", true);
            params.put("pendingType", new Dict(0, ""));
            params.put("reservationDate", date);
            params.put("pendingTypeDate", date);
            params.put("subStatus", Order.ORDER_SUBSTATUS_PLANNED);//Add by Zhoucy
            // 突击单关闭 Add by Ryan
            if (condition.getRushOrderFlag() == 1 || condition.getRushOrderFlag() == 3) {
                params.put("rushOrderFlag", 2);
            }
            dao.updateCondition(params);
            //fee
            params.clear();
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("engineerPaymentType", servicePoint.getFinance().getPaymentType());//安维付款方式
            dao.updateFee(params);

            //Status
            params.clear();
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("acceptDate", date);
            params.put("planBy", user);
            params.put("planDate", date);
            params.put("planComment", "安维接单派单");
            dao.updateStatus(params);

            // log
            OrderProcessLog processLog = new OrderProcessLog();
            processLog.setOrderId(order.getId());
            processLog.setQuarter(order.getQuarter());
            processLog.setAction("安维接单派单");
            processLog.setActionComment(String.format("安维接单派单:%s,操作人:%s", order.getOrderNo(), user.getName()));
            processLog.setStatus(status.getLabel());
            processLog.setStatusValue(Order.ORDER_STATUS_PLANNED);
            processLog.setStatusFlag(OrderProcessLog.OPL_SF_CHANGED_STATUS);
            processLog.setCloseFlag(0);
            processLog.setCreateBy(user);
            processLog.setCreateDate(date);
            processLog.setCustomerId(condition.getCustomerId());
            processLog.setDataSourceId(order.getDataSourceId());
            saveOrderProcessLogNew(processLog);

            //更新接单数(网点,安维)
            params.clear();
            params.put("id", servicePoint.getId());
            params.put("planCount", 1);//派单数+1
            params.put("updateBy", user);
            params.put("updateDate", date);
            //servicePointDao.updateServicePointByMap(params);//mark on 2020-1-17   web端去md_servicepoint
            //servicePointService.updateServicePointByMap(params); // add on 2019-10-4  //mark on 2020-1-16 集中调用MD微服务
            HashMap<String, Object> paramsForServicePoint = Maps.newHashMap();  // add on 2020-1-16
            paramsForServicePoint.put("id", servicePoint.getId()); // add on 2020-1-16
            paramsForServicePoint.put("planCount", 1);//派单数+1    // add on 2020-1-16
            paramsForServicePoint.put("unfinishedOrderCount",1);//未完工单数+1

            //安维
            params.remove("id");
            params.put("id", engineer.getId());
            //servicePointDao.updateEngineerByMap(params);  //mark on 2020-1-13 web端去除md_engineer
            //msEngineerService.updateEngineerByMap(params);  // add on 2019-10-18 //Engineer微服务
            HashMap<String, Object> paramsForEngineer = Maps.newHashMap();  // add on 2020-1-16
            paramsForEngineer.put("planCount", 1);//派单数+1    // add on 2020-1-16
            paramsForEngineer.put("id", engineer.getId());     // add on 2020-1-16

            //cache
            servicePoint.setPlanCount(servicePoint.getPlanCount() + 1);
            //servicePointService.updateServicePointCache(servicePoint);//mark on 2020-1-17   web端去md_servicepoint

            //engineer planCount+1
            engineer.setPlanCount(engineer.getPlanCount() + 1);
            //servicePointService.updateEngineerCache(engineer);//mark on 2020-1-17   web端去md_servicepoint

            //派单记录表 2018/01/24
            Integer nextPlanTimes = dao.getOrderPlanMaxTimes(order.getId(), order.getQuarter());
            if (nextPlanTimes == null) {
                nextPlanTimes = 1;
            } else {
                nextPlanTimes++;//+1
            }
            OrderPlan orderPlan = dao.getOrderPlan(order.getId(), order.getQuarter(), servicePoint.getId(), engineer.getId());
            if (orderPlan == null || orderPlan.getId() == null) {
                String insuranceNo = new String("");
                Double insuranceAmount = 0.0;
//                if (servicePoint.getInsuranceFlag() == 1) {
                if (ServicePointUtils.servicePointInsuranceEnabled(servicePoint)) {
                    //保险费
                    List<Long> categorids = order.getItems().stream().filter(t -> t.getDelFlag() == 0).map(t -> t.getProduct().getCategory().getId()).distinct().collect(Collectors.toList());
                    insuranceAmount = getOrderInsuranceAmount(categorids);
                    if (insuranceAmount == null) {
                        //throw new RuntimeException("请确认产品类别是否设置了保险费！");
                        insuranceAmount = 0.0;
                    }
                    //保险费大于0，才生成保单
                    if (insuranceAmount > 0) {
                        //保险单号
                        insuranceNo = SeqUtils.NextSequenceNo("orderInsuranceNo");
                        if (StringUtils.isBlank(insuranceNo)) {
                            insuranceNo = SeqUtils.NextSequenceNo("orderInsuranceNo");
                            if (StringUtils.isBlank(insuranceNo)) {
                                throw new RuntimeException("生成工单保险单号错误");
                            }
                        }
                    }
                }
                orderPlan = new OrderPlan();
                //orderPlan.setId(SeqUtils.NextID());
                orderPlan.setQuarter(order.getQuarter());
                orderPlan.setOrderId(order.getId());
                orderPlan.setServicePoint(servicePoint);
                orderPlan.setEngineer(engineer);
                orderPlan.setIsMaster(engineer.getMasterFlag());//*
                orderPlan.setPlanTimes(nextPlanTimes);//*
                orderPlan.setCreateBy(user);
                orderPlan.setCreateDate(date);
                orderPlan.setUpdateBy(new User(0l));
                //距离
                orderPlan.setEstimatedDistance(0.00d);
                //服务费
                Double amount = calcServicePointCost(servicePoint, order.getItems());
                orderPlan.setEstimatedServiceCost(amount);
                dao.insertOrderPlan(orderPlan);
//                if (servicePoint.getInsuranceFlag() == 1 && insuranceAmount > 0) {
                if (ServicePointUtils.servicePointInsuranceEnabled(servicePoint) && insuranceAmount > 0) {
                    //保险单
                    OrderInsurance orderInsurance = new OrderInsurance();
                    orderInsurance.setAmount(insuranceAmount);
                    orderInsurance.setInsuranceNo(insuranceNo);
                    orderInsurance.setOrderId(order.getId());
                    orderInsurance.setOrderNo(order.getOrderNo());
                    orderInsurance.setQuarter(order.getQuarter());
                    orderInsurance.setServicePointId(servicePoint.getId());
                    Engineer primary = servicePoint.getPrimary();
                    orderInsurance.setAssured(primary.getName());
                    orderInsurance.setPhone(primary.getContactInfo());
                    orderInsurance.setAddress(primary.getAddress());
                    orderInsurance.setInsureDate(date);
                    orderInsurance.setInsuranceDuration(12);//投保期限12个月
                    orderInsurance.setCreateBy(user);
                    orderInsurance.setCreateDate(date);
                    dao.insertOrderInsurance(orderInsurance);
                    //网点费用,默认无效
                    OrderServicePointFee servicePointFee = mapper.map(orderInsurance, OrderServicePointFee.class);
                    servicePointFee.setServicePoint(servicePoint);
                    dao.insertOrderServicePointFee(servicePointFee);
                    //不更新OrderFee.insuranceCharge
                } else {
                    //Service Point Fee
                    OrderServicePointFee servicePointFee = new OrderServicePointFee();
                    servicePointFee.setServicePoint(servicePoint);
                    servicePointFee.setOrderId(order.getId());
                    servicePointFee.setQuarter(order.getQuarter());
                    dao.insertOrderServicePointFee(servicePointFee);
                }
            } else {
                HashMap<String, Object> planMaps = Maps.newHashMap();
                planMaps.put("id", orderPlan.getId());
                planMaps.put("planTimes", nextPlanTimes);
                planMaps.put("updateBy", user);
                planMaps.put("updateDate", date);
                dao.UpdateOrderPlan(planMaps);
            }

            //关闭突击单
            if (condition.getRushOrderFlag() == 1 || condition.getRushOrderFlag() == 3) {
                crushService.closeOrderCurshByOrderId(order.getId(), order.getQuarter(), 1, null, user, date);
            }
            condition.setRushOrderFlag(2);

            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(order.getId())
                    .setDeleteField(OrderCacheField.CONDITION)
                    .setDeleteField(OrderCacheField.ORDER_STATUS);
            OrderCacheUtils.update(builder.build());

            //add on 2020-1-16 begin  集中处理MD微服务调用
            servicePointService.updateServicePointByMap(paramsForServicePoint);
            msEngineerService.updateEngineerByMap(paramsForEngineer);
            //add on 2020-1-16 end

            b2BCenterOrderService.planOrder(order, engineer, user, date);

            //region 短信通知
            //检查客户短信发送开关，1:才发送
            Customer customer = null;
            try {
                customer = customerService.getFromCache(condition.getCustomer().getId());
            } catch (Exception e) {
                LogUtils.saveLog("网点接单:检查客户短信开关异常", "OrderService.grabOrder", order.getId().toString(), e, user);
            }
            //发送短信 1.未取到客户信息 2.取到，且短信发送标记为：1
            //未在配置中：shortmessage.ignore-data-sources  //2018-12-05
            List<String> ignoreDataSources = StringUtils.isBlank(smIgnoreDataSources) ? Lists.newArrayList() : Splitter.on(",").trimResults().splitToList(smIgnoreDataSources);
            if (!ignoreDataSources.contains(order.getDataSource().getValue())
                    && (customer == null || (customer != null && customer.getShortMessageFlag() == 1))
                    && servicePoint.getPlanContactFlag() == 0
            ) {
                // 网点联系人 为网点负责人(0)时此处发送短信;师傅(1)在网点派单或App派单时再发短信 2020-11-19
                StringBuffer userContent = new StringBuffer(250);
                try {
                    userContent.append("您的");
                    OrderItem item;
                    for (int i = 0, size = o.getItems().size(); i < size; i++) {
                        item = o.getItems().get(i);
                        userContent
                                .append(item.getBrand())
                                .append(com.wolfking.jeesite.common.utils.StringUtils2.getStandardProductName(item.getProduct().getName()))
                                .append(item.getQty())
                                .append(item.getProduct().getSetFlag() == 0 ? "台" : "套")
                                .append(item.getServiceType().getName())
                                .append((i == (size - 1)) ? "" : " ");
                    }
                    userContent.append("，");
                    userContent.append(engineer.getName().substring(0, 1));
                    userContent.append("师傅").append(engineer.getContactInfo()).append("已接单,");
                    if (condition.getKefu() != null) {
                        userContent
                                .append("客服")
                                .append(condition.getKefu().getName().substring(0, 1)).append("小姐")
                                .append(condition.getKefu().getPhone())
                                .append("/");
                    }
                    userContent.append(MSDictUtils.getDictSingleValue("400ServicePhone", "4006663653"));
                    // 使用新的短信发送方法 2019/02/28
                    smsMQSender.sendNew(condition.getServicePhone(),
                            userContent.toString(),
                            "",
                            user.getId(),
                            date.getTime(),
                            SysSMSTypeEnum.ORDER_ACCEPTED_APP
                    );
                } catch (Exception e) {
                    LogUtils.saveLog(
                            "网点接单-发送短信失败",
                            "OrderService.grabOrder",
                            MessageFormat.format("mobile:{0},content:{1},triggerBy:{2},triggerDate:{3}", condition.getServicePhone(), userContent.toString(), user.getId(), date.getTime()),
                            e,
                            user
                    );
                }
            }

            //region 网点订单数据更新 2019-03-25
            OrderCondition oc = o.getOrderCondition();
            OrderStatus orderStatus = o.getOrderStatus();
            int orderChannel = ofNullable(o.getOrderChannel()).map(Dict::getIntValue).orElse(0);
            MQOrderServicePointMessage.ServicePointMessage.Builder spMsgBuilder = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                    .setOperationType(MQOrderServicePointMessage.OperationType.Create)
                    .setOrderChannel(orderChannel)
                    .setDataSource(o.getDataSourceId())
                    .setOrderId(order.getId())
                    .setQuarter(order.getQuarter())
                    .setSubStatus(Order.ORDER_SUBSTATUS_PLANNED)
                    .setOperationAt(date.getTime())
                    .setOperationBy(user.getId())
                    .setResetAppointmentDate(1)//重置null
                    .setOrderInfo(MQOrderServicePointMessage.OrderInfo.newBuilder()
                            .setOrderNo(o.getOrderNo())
                            .setOrderServiceType(oc.getOrderServiceType())
                            .setAreaId(oc.getArea().getId())
                            .setAreaName(oc.getArea().getName())
                            .setStatus(status.getIntValue())
                            .build())
                    .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                            .setServicePointId(servicePoint.getId())
                            .setEngineerId(servicePoint.getPrimary().getId())
                            .setPrevServicePointId(0)
                            .setPlanOrder(1)
                            .setPlanType(OrderServicePoint.PlanType.APP.ordinal())
                            .build())
                    .setUserInfo(MQOrderServicePointMessage.UserInfo.newBuilder()
                            .setUserName(oc.getUserName())
                            .setPhone(oc.getServicePhone())
                            .setAddress(oc.getServiceAddress())
                            .build())
                    .setPlanDate(date.getTime())
                    .setReservationDate(date.getTime())
                    .setPendingType(0)
                    .setMasterFlag(1) // 主账号
                    .setAbnormalyFlag(oc.getAppAbnormalyFlag())
                    .setUrgentLevelId(oc.getUrgentLevel().getId().intValue())
                    .setReminderFlag(orderStatus.getReminderStatus())
                    .setComplainFlag(orderStatus.getComplainFlag());
            servicePointOrderBusinessService.planOrder(spMsgBuilder);
            //endregion

            //endregion 消息队列


        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            log.error("[OrderService.grabOrder] orderId:{}", order.getId(), e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (locked && lockkey != null) {
                if (releaseLockTime > 0) {
                    redisUtilsLocal.expire(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, releaseLockTime);
                } else {
                    redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
                }
            }
        }
    }

    /**
     * 发送统计消息
     * 包含：问题反馈，反馈处理,app异常
     */
    public void sendNoticeMessage(Integer noticeType, Long orderId, String quarter, Customer customer, User
            kefu, Long areaId, User user, Date date) {
        MQNoticeMessage.NoticeMessage message = null;
        try {
            message = MQNoticeMessage.NoticeMessage.newBuilder()
                    .setOrderId(orderId)
                    .setQuarter(quarter)
                    .setNoticeType(noticeType)
                    .setCustomerId(customer.getId())
                    .setKefuId(kefu == null ? 0 : kefu.getId())
                    .setAreaId(areaId)
                    .setTriggerBy(MQWebSocketMessage.User.newBuilder()
                            .setId(user.getId())
                            .setName(user.getName())
                            .build()
                    )
                    .setTriggerDate(date.getTime())
                    .setDelta(1)
                    .build();

            try {
                noticeMessageSender.send(message);
            } catch (Exception e) {
                //消息队列发送错误
                log.error("[OrderService.sendNoticeMessage] content:{} ", GsonUtils.getInstance().toGson(message), e);
                //LogUtils.saveLog("发送统计消息错误", "sendNoticeMessage", GsonUtils.getInstance().toGson(message), e, user);
            }

        } catch (Exception e) {
            log.error("[OrderService.sendNoticeMessage] noticeType:{} ,orderId:{} ,customer:{} ,kefu:{} ,area:{}"
                    , noticeType
                    , orderId
                    , customer.getId()
                    , kefu == null ? "0" : kefu.getId()
                    , areaId
                    , e
            );
        }

    }

    /**
     * 发送自动完工消息(app触发)
     */
    public void sendAutoCompleteMessage(Long orderId, String quarter, User user, Date date) {
        Long index = 0l;
        MQOrderAutoComplete.OrderAutoComplete message = null;
        try {
            message = MQOrderAutoComplete.OrderAutoComplete.newBuilder()
                    .setOrderId(orderId)
                    .setQuarter(quarter)
                    .setTriggerBy(user.getId())
                    .setTriggerDate(date.getTime())
                    .build();
            orderAutoCompleteDelaySender.send(message);
        } catch (Exception e) {
            OrderAutoComplete entry = new OrderAutoComplete();
            entry.setOrderId(orderId);
            entry.setQuarter(quarter);
            entry.setTriggerBy(user.getId());
            entry.setTriggerDate(date);
            entry.setCreateBy(user);
            entry.setCreateDate(date);
            log.error("[OrderService.sendAutoCompleteMessage]=={}== orderId:{} ,entry:{}", index, orderId, GsonUtils.getInstance().toGson(entry), e);
            try {
                entry.setId(SeqUtils.NextID());
                autoCompleteDao.insert(entry);
            } catch (Exception e1) {
                log.error("[OrderService.sendAutoCompleteMessage]=={}== orderId:{}", index, orderId, e1);
            }
        }
    }

    /**
     * 根据订单项计算保险费
     *
     * @param categorids 产品类别
     * @return 保险费(null代表未设定保险费)
     */
    public Double getOrderInsuranceAmount(List<Long> categorids) {
        if (categorids == null || categorids.size() == 0) {
            return 0D;
        }
        List<InsurancePrice> prices = insurancePriceService.findAllList();
        Optional<Double> insurance = prices.stream().filter(t -> categorids.contains(t.getCategory().getId())).map(t -> t.getInsurance()).max(Double::compareTo);
        if (insurance.isPresent()) {
            return insurance.get();
        } else {
            return null;
        }
    }

    /**
     * 订单保险费合计(返回Null或负数)
     */
    public Double getTotalOrderInsurance(long orderId, String quarter) {
        if (orderId <= 0 || StringUtils.isBlank(quarter)) {
            return null;
        }
        return dao.getTotalOrderInsurance(orderId, quarter);
    }

    public boolean checkOrderProductBarCode(Long orderId, String quarter, Long customerId, List<OrderDetail> orderDetails) {
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && customerId != null && customerId > 0) {
            if (orderDetails != null && !orderDetails.isEmpty()) {
                List<OrderItemComplete> uploadedCompletePics = orderItemCompleteDao.getByOrderId(orderId, quarter);

                //拆分套组，获取最终的产品id
                List<Long> tempProductIds = orderDetails.stream()
                        .filter(i -> i.getProduct() != null && i.getProduct().getId() != null)
                        .map(OrderDetail::getProductId).collect(Collectors.toList());
                Map<Long, Product> productMap = productService.getProductMap(tempProductIds);
                Set<Long> prodcutIdSet = Sets.newHashSet();
                Product product = null;
                Long productId = null;
                for (Long idLong : tempProductIds) {
                    product = productMap.get(idLong);
                    if (product != null) {
                        if (product.getSetFlag() == 1) {
                            final String[] setIds = product.getProductIds().split(",");
                            for (String idString : setIds) {
                                productId = StringUtils.toLong(idString);
                                if (productId > 0) {
                                    prodcutIdSet.add(productId);
                                }
                            }
                        } else {
                            prodcutIdSet.add(idLong);
                        }
                    }
                }

                uploadedCompletePics = uploadedCompletePics.stream().filter(i -> prodcutIdSet.contains(i.getProduct().getId())).collect(Collectors.toList());//只检查OrderDetail中的产品
                if (!uploadedCompletePics.isEmpty()) {
                    List<Long> productIds = uploadedCompletePics.stream().map(i -> i.getProduct().getId()).distinct().collect(Collectors.toList());
                    Map<Long, ProductCompletePic> completePicRuleMap = OrderUtils.getCustomerProductCompletePicMap(productIds, customerId);
                    ProductCompletePic picRule = null;
                    for (OrderItemComplete item : uploadedCompletePics) {
                        picRule = completePicRuleMap.get(item.getProduct().getId());
                        if (picRule != null && picRule.getBarcodeMustFlag() != null && picRule.getBarcodeMustFlag() == 1
                                && StringUtils.isBlank(item.getUnitBarcode())) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkOrderProductBarCodeIsRepeat(Long orderId, String quarter, Long customerId, List<OrderDetail> orderDetails) {
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && customerId != null && customerId > 0) {
            if (orderDetails != null && !orderDetails.isEmpty()) {
                List<OrderItemComplete> uploadedCompletePics = orderItemCompleteDao.getByOrderId(orderId, quarter);
                List<OrderItemComplete> noEmptyList = uploadedCompletePics.stream().filter(a -> a.getUnitBarcode()!=null).collect(Collectors.toList());
                List<String> distinctUploadedCompletePics = noEmptyList.stream().map(OrderItemComplete::getUnitBarcode).distinct().collect(Collectors.toList());
                if (noEmptyList.size()!=distinctUploadedCompletePics.size()){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 继续任务
     *
     * @param orderId
     * @param user
     * @param date
     */
    public void keepOnVoiceOperateMessage(String site, Long orderId, String quarter, String user, Date date) {
        if (orderId == null || orderId <= 0 || StringUtils.isBlank(site)) {
            return;
        }
        if (StringUtils.isBlank(user)) {
            return;
        }
        try {
            MQVoiceSeviceMessage.OperateCommand operateCommand = MQVoiceSeviceMessage.OperateCommand.newBuilder()
                    .setSite(site)
                    .setOrderId(orderId)
                    .setCommand(OperateType.KEEP_ON.code)
                    .setCreateBy(user)
                    .setCreateDate(date == null ? System.currentTimeMillis() : date.getTime())
                    .build();
            operateTaskMQSender.send(operateCommand);
            orderVoiceTaskService.cancel(quarter, orderId, date.getTime());
        } catch (Exception e) {
            log.error("[停止智能回访错误]orderId:{},user:{},date:{}", orderId, user, date, e);
        }
    }

    /**
     * 新建智能回访任务
     *
     * @param order 订单
     * @param kefu  客服
     * @param date
     */
    public void sendNewVoiceTaskMessage(String site, Order order, String kefu, Date date) {
        if (order == null || StringUtils.isBlank(site)) {
            return;
        }
        OrderCondition orderCondition = order.getOrderCondition();
        if (orderCondition == null) {
            return;
        }
        String quarter = order.getQuarter();
        if (StringUtils.isBlank(quarter)) {
            quarter = orderCondition.getQuarter();
        }
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            return;
        }

        if (StringUtils.isBlank(orderCondition.getUserName()) || StringUtils.isBlank(kefu)
                || StringUtils.isBlank(orderCondition.getServicePhone())) {
            return;
        }
        //voice task
        OrderVoiceTask task = orderVoiceTaskService.getBaseInfoByOrderId(quarter, order.getId());
        //已存在的任务，继续任务
        if (task != null) {
            keepOnVoiceOperateMessage(site, order.getId(), quarter, kefu, date);
            return;
        }
        OrderItem firstItem = null;
        if (items.size() == 1) {
            firstItem = items.get(0);
        } else {
            //items.stream().sorted(Comparator.comparing(OrderItem::getServiceType))
            firstItem = items.stream().sorted(
                    Comparator.comparing(OrderItem::getProduct, (x, y) -> {
                        //再按套组邮箱
                        if (x == null && y != null) {
                            return 1;
                        } else if (x != null && y == null) {
                            return -1;
                        } else if (x == null && y == null) {
                            return -1;
                        } else {
                            if (x.getId().equals(y.getId())) {
                                return 0;
                            } else if (x.getSetFlag() == 1) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }).thenComparing(OrderItem::getServiceType, (x, y) -> {
                        //先按照服务排序,安装优先
                        if (x == null && y != null) {
                            return 1;
                        } else if (x != null && y == null) {
                            return -1;
                        } else if (x == null && y == null) {
                            return -1;
                        } else {
                            if (x.getId().equals(y.getId())) {
                                return 0;
                            } else if (x.getName().contains("安装")) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }))
                    .findFirst().orElse(null);
        }
        Product product = productService.getProductByIdFromCache(firstItem.getProduct().getId());
        if (product == null) {
            product = firstItem.getProduct();
        }
        //无开场白的产品不自动回访
        if (StringUtils.isBlank(product.getPinYin())) {
            return;
        }
        String productName = product.getName();
        ServiceType serviceType = firstItem.getServiceType();
        String openingSpeech = product.getPinYin() + (serviceType.getName().contains("安装") ? "1" : "2");
        try {
            //用户名长度超长要截取，保留20个字符
            MQVoiceSeviceMessage.Task message = MQVoiceSeviceMessage.Task.newBuilder()
                    .setVoiceType(1)
                    .setSite(site)
                    .setOrderId(orderCondition.getOrderId())
                    .setQuarter(quarter)
                    .setUserName(StringUtils.left(orderCondition.getUserName(), 20))
                    .setPhone(orderCondition.getServicePhone())
                    .setProducts(productName)
                    .setOpeningSpeech(openingSpeech) //开场白（产品+服务） 1：安装 2：其他
                    .setCaption(orderCondition.getOrderNo() + "自动回访")
                    .setCreateBy(kefu)
                    .setCreateDate(date == null ? System.currentTimeMillis() : date.getTime())
                    .build();

            task = new OrderVoiceTask();
            task.setOrderId(message.getOrderId());
            task.setQuarter(message.getQuarter());
            task.setCreateBy(message.getCreateBy());
            task.setCreateDate(message.getCreateDate());
            task.setProjectCaption(message.getCaption());
            task.setRemark(message.getProducts());
            task.setUserName(message.getUserName());
            task.setPhone(message.getPhone());
            task.setVoiceType(message.getVoiceType());
            orderVoiceTaskService.insert(task);

            newTaskMQSender.send(message);
        } catch (Exception e) {
            log.error("[发送智能回访消息错误]orderId:{},userName:{},phone:{},product:{},openingSpeech:{},kefu:{}",
                    orderCondition.getOrderId(), orderCondition.getUserName(), orderCondition.getServicePhone(), product, openingSpeech, kefu,
                    e);
        }
    }


    /**
     * 检查订单用户地址经纬度坐标
     * @param dataSource    数据源
     * @param orderId       订单ID
     * @param quarter       分片
     * @return
     */
    public AjaxJsonEntity checkAddressLocation(int dataSource, Long orderId, String quarter){
        if(B2BDataSourceEnum.VIOMI.id != dataSource){
            return AjaxJsonEntity.success("",null);
        }
        OrderLocation location = orderLocationService.getByOrderId(orderId,quarter);
        if(location == null || location.getLatitude()<=0 || location.getLongitude()<=0){
            return AjaxJsonEntity.fail("用户地址定位经纬度坐标缺失", null);
        }
        return AjaxJsonEntity.success("",null);
    }

    //region 挂起

    @Transactional(readOnly = false)
    public void suspendOrder(Long orderId, String quarter, OrderSuspendTypeEnum type, OrderSuspendFlagEnum flag) {
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("orderId", orderId);
        params.put("quarter", quarter);
        params.put("suspendType", type.getValue());
        params.put("suspendFlag", flag.getValue());
        updateOrderCondition(params);
    }

    //endregion 挂起

    //region 偏远区域

    /**
     * 检查街道是否是偏远区域
     * @param orderCondition    订单信息表
     * @return  data: true 是；false 不是
     */
    public RestResult<Boolean> checkServicePointRemoteArea(OrderCondition orderCondition){
        if(orderCondition == null){
            return RestResultGenerator.success(false);
        }
        long categroyId= Optional.ofNullable(orderCondition.getProductCategoryId()).orElse(0L);
        long areaId = Optional.ofNullable(orderCondition.getArea()).map(t->t.getId()).orElse(0L);
        long subAreaId = Optional.ofNullable(orderCondition.getSubArea()).map(t->t.getId()).orElse(0L);
        if(categroyId <=0){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code,"品类参数无值");
        }
        if(areaId <= 0){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code,"区域参数无值");
        }
        //判断街道是否属于：不稳定区域/远程区域
        MSResponse<Integer> msResponse = msRegionPermissionNewService.getRemoteAreaStatusFromCacheForSD(categroyId, areaId, subAreaId);
        if(MSResponse.isSuccessCode(msResponse)){
            if(msResponse.getData()==1){
                return RestResultGenerator.success(true);
            }else{
                return RestResultGenerator.success(false);
            }
        }
        return RestResultGenerator.custom(msResponse.getCode(),msResponse.getMsg());
    }


    /**
     * 检查街道是否是偏远区域，并检查网点是否设定了价格
     * @param servicePointId    网点
     * @param orderCondition    订单信息表
     * @param items             订单项目
     * @return  code=0: 通过验证：1.不是偏远区域；2.是偏远区域，且已设定服务价格；
     *          code>0: 验证不通过：1.参数错误；2.运行时异常；3.价格未设定
     */
    public RestResult<Object> checkServicePointRemoteAreaAndPrice(long servicePointId,OrderCondition orderCondition,List<OrderItem> items){
        if(servicePointId <= 0){
            return RestResultGenerator.custom(ErrorCode.READ_ORDER_FAIL.code,"参数：网点编码无内容。");
        }
        RestResult<Boolean> remoteCheckResult = checkServicePointRemoteArea(orderCondition);
        if(remoteCheckResult.getCode() != ErrorCode.NO_ERROR.code){
            return RestResultGenerator.custom(ErrorCode.READ_ORDER_FAIL.code,new StringJoiner("").add("判断区域是否为偏远区域错误:").add(remoteCheckResult.getMsg()).toString());
        }
        Boolean isRemoteArea = (Boolean)remoteCheckResult.getData();
        if(isRemoteArea){
            //检查是否有设定价格
            if (CollectionUtils.isEmpty(items)) {
                return RestResultGenerator.custom(ErrorCode.READ_ORDER_FAIL.code,"读取订单详细服务项目失败。");
            }
            Set<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> valuePairs = items.stream().map(t -> {
                return new com.kkl.kklplus.entity.common.NameValuePair<Long, Long>(t.getProductId(), t.getServiceType().getId());
            }).collect(Collectors.toSet());
            List<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>> nameValuePairs = new ArrayList<com.kkl.kklplus.entity.common.NameValuePair<Long, Long>>(valuePairs);
            List<ServicePrice> prices = msServicePointPriceService.findPricesListByRemotePriceFlagFromCacheForSD(servicePointId, nameValuePairs);
            if(CollectionUtils.isEmpty(prices)){
                return RestResultGenerator.custom(ErrorCode.READ_ORDER_FAIL.code,"网点未定义偏远区域价格。");
            }else if(prices.size() < valuePairs.size()){
                return RestResultGenerator.custom(ErrorCode.READ_ORDER_FAIL.code,"网点未定义此单中部分产品的偏远区域价格。");
            }
            return RestResultGenerator.success();
        }
        return RestResultGenerator.success();
    }
    //endregion
}
