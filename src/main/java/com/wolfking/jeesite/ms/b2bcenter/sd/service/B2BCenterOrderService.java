package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import cn.hutool.core.util.ObjectUtil;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderStatusUpdateMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.*;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.validate.OrderValidate;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.mq.sender.B2BOrderStatusUpdateMQSender;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateFailureLog;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import com.wolfking.jeesite.ms.b2bcenter.sd.utils.B2BOrderUtils;
import com.wolfking.jeesite.ms.canbo.sd.service.CanboOrderService;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.inse.sd.service.InseOrderService;
import com.wolfking.jeesite.ms.jd.sd.service.JdOrderService;
import com.wolfking.jeesite.ms.jdue.sd.service.JDUEOrderService;
import com.wolfking.jeesite.ms.jdueplus.sd.service.JDUEPlusOrderService;
import com.wolfking.jeesite.ms.jinjing.service.JinJingOrderService;
import com.wolfking.jeesite.ms.joyoung.sd.service.JoyoungOrderService;
import com.wolfking.jeesite.ms.konka.sd.service.KonkaOrderService;
import com.wolfking.jeesite.ms.lb.sd.service.LbOrderService;
import com.wolfking.jeesite.ms.mbo.service.MBOOrderService;
import com.wolfking.jeesite.ms.mqi.sd.service.MqiOrderService;
import com.wolfking.jeesite.ms.pdd.sd.service.PddOrderService;
import com.wolfking.jeesite.ms.philips.sd.service.PhilipsOrderService;
import com.wolfking.jeesite.ms.sf.sd.service.SFOrderService;
import com.wolfking.jeesite.ms.suning.sd.service.SuningOrderService;
import com.wolfking.jeesite.ms.supor.sd.service.SuporOrderService;
import com.wolfking.jeesite.ms.tmall.sd.service.TmallOrderService;
import com.wolfking.jeesite.ms.um.sd.service.UMOrderService;
import com.wolfking.jeesite.ms.usatonga.service.UsatonGaOrderService;
import com.wolfking.jeesite.ms.viomi.sd.service.VioMiOrderService;
import com.wolfking.jeesite.ms.weber.service.WeberOrderService;
import com.wolfking.jeesite.ms.xyyplus.sd.service.XYYPlusOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCenterOrderService {

    @Autowired
    private B2BOrderStatusUpdateMQSender b2BOrderStatusUpdateMQSender;
    @Autowired
    private OrderCacheReadService orderCacheReadService;

    @Autowired
    private CanboOrderService canboOrderService;
    @Autowired
    private WeberOrderService weberOrderService;
    @Autowired
    private MBOOrderService mboOrderService;
    @Autowired
    private SuporOrderService suporOrderService;
    @Autowired
    private JinJingOrderService jinJingOrderService;
    @Autowired
    private UsatonGaOrderService usatonGaOrderService;
    @Autowired
    private MqiOrderService mqiOrderService;

    @Autowired
    private TmallOrderService tmallOrderService;

    @Autowired
    private JdOrderService jdOrderService;

    @Autowired
    private InseOrderService inseOrderService;

    @Autowired
    private KonkaOrderService konkaOrderService;

    @Autowired
    private JoyoungOrderService joyoungOrderService;

    @Autowired
    private SuningOrderService suningOrderService;

    @Autowired
    private JDUEOrderService jdueOrderService;

    @Autowired
    private JDUEPlusOrderService jduePlusOrderService;

    @Autowired
    private XYYPlusOrderService xyyPlusOrderService;

    @Autowired
    private LbOrderService lbOrderService;

    @Autowired
    private UMOrderService umOrderService;

    @Autowired
    private PddOrderService pddOrderService;

    @Autowired
    private VioMiOrderService vioMiOrderService;
    @Autowired
    private SFOrderService sfOrderService;
    @Autowired
    private PhilipsOrderService philipsOrderService;

    @Autowired
    private MicroServicesProperties microServicesProperties;

    public static final User KKL_SYSTEM_USER = new User(0L, "快可立全国联保", "4006663653");
    public static final long USER_ID_KKL_AUTO_GRADE = 2L; //用户回复短信

    //private static final SequenceIdUtils sequenceIdUtils = new SequenceIdUtils(ThreadLocalRandom.current().nextInt(32), ThreadLocalRandom.current().nextInt(32));
    @Autowired
    private SequenceIdService sequenceIdService;

    //-----------------------------------------------------------------------------------------------------------公用方法

    //region 公用方法

    /**
     * 是否需要发送工单状态消息给B2B微服务
     */
    private boolean isNeedSendOrderStatusMsgToB2B(Integer dataSourceId) {
        return B2BOrderUtils.canInvokeB2BMicroService(dataSourceId);
    }

    /**
     * 记录发送B2B工单状态变更消息过程中失败的情况
     */
    private void saveFailureLog(B2BOrderStatusUpdateFailureLog failureLog, String methodName) {
        try {
            String logJson = GsonUtils.toGsonString(failureLog);
            LogUtils.saveLog("B2BCenterOrderService.saveFailureLog", methodName, logJson, null, null);
        } catch (Exception e) {
            log.error("B2BCenterOrderService.saveFailureLog", e);
        }
    }

    //endregion 公用方法

    //----------------------------------------------------------------------------------------------创建工单状态变更消息实体

    //region 创建工单状态变更消息实体

    private void setB2BOrderStatusUpdateReqEntityProperties(B2BOrderStatusUpdateReqEntity.Builder entityBuilder, B2BOrderStatusEnum status, B2BOrderActionEnum action,
                                                            Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long kklOrderId, String kklOrderNo,
                                                            User updater, Date updateDate) {
        if (entityBuilder != null) {
            entityBuilder
                    .setStatus(status)
                    .setActionType(action == null ? B2BOrderActionEnum.NONE : action)
                    .setDataSourceId(dataSourceId)
                    .setB2bOrderId(b2bOrderId == null ? 0L : b2bOrderId)
                    .setB2bOrderNo(b2bOrderNo)
                    .setOrderId(kklOrderId == null ? 0L : kklOrderId)
                    .setKklOrderNo(StringUtils.toString(kklOrderNo))
                    .setUpdaterId(updater == null || updater.getId() == null ? 0L : updater.getId())
                    .setUpdateDate(updateDate == null ? new Date() : updateDate);
        }

    }

    /**
     * B2B派单（APP接单、客户派单、网点派单）
     */
    public void planOrder(Order order, Engineer engineer, User updater, Date updateDate) {
        if (order != null && order.getId() != null) {
            if (order.getOrderCondition() != null && order.getOrderCondition().getCustomerId() > 0) {
                updateOrderStatus(B2BDataSourceEnum.UM, order.getOrderCondition().getCustomerId(), order.getId(), B2BOrderStatusEnum.PLANNED);
            } else {
                Order cachedOrder = orderCacheReadService.getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true, false);
                if (cachedOrder != null) {
                    updateOrderStatus(B2BDataSourceEnum.UM, cachedOrder.getOrderCondition().getCustomerId(), cachedOrder.getId(), B2BOrderStatusEnum.PLANNED);
                }
            }
        }
        if (order != null && order.getDataSource() != null && engineer != null) {
            Long servicePointId = engineer.getServicePoint() != null && engineer.getServicePoint().getId() != null ? engineer.getServicePoint().getId() : 0;
            planB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(), order.getId(), order.getOrderNo(),
                    servicePointId, engineer.getId(), engineer.getName(), engineer.getContactInfo(), updater, updateDate);
        }
    }

    private void planB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo,
                              Long servicePointId, Long engineerId, String engineerName, String engineerMobile, User updater, Date updateDate) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            String defaultEngineerName = microServicesProperties.getB2bcenter().getDefaultEngineerName();
            String defaultEngineerMobile = microServicesProperties.getB2bcenter().getDefaultEngineerPhone();
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (B2BDataSourceEnum.isTooneDataSourceId(dataSourceId)) {
                result = canboOrderService.createPlanRequestEntity(dataSourceId, servicePointId, engineerName, engineerMobile);
//                if (dataSourceId != B2BDataSourceEnum.USATON.id && dataSourceId != B2BDataSourceEnum.CANBO.id) {//阿斯丹顿和康宝传真实的电话
//                    if (StringUtils.isNotBlank(microServicesProperties.getCanbo().getDefaultPhoneNumber())) {
//                        result.getBElement().setEngineerMobile(microServicesProperties.getCanbo().getDefaultPhoneNumber());
//                    }
//                }
            } else if (dataSourceId == B2BDataSourceEnum.WEBER.id) {
                result = weberOrderService.createPlanRequestEntity(dataSourceId, servicePointId, engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getWeber().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getWeber().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.MBO.id) {
                result = mboOrderService.createPlanRequestEntity(dataSourceId, servicePointId, engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getMbo().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getMbo().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.SUPOR.id) {
                result = suporOrderService.createPlanRequestEntity(defaultEngineerName, defaultEngineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getSupor().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getSupor().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.JINJING.id) {
                result = jinJingOrderService.createPlanRequestEntity(dataSourceId, servicePointId, engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getJinjing().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getJinjing().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.USATON_GA.id) {
                result = usatonGaOrderService.createPlanRequestEntity(dataSourceId, servicePointId, engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getUsatonGa().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getUsatonGa().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.MQI.id) {
                result = mqiOrderService.createPlanRequestEntity(engineerName, engineerMobile);
            } else if (dataSourceId == B2BDataSourceEnum.JD.id) {
                result = jdOrderService.createJdPlanRequestEntity(servicePointId, engineerName, engineerMobile);
            } else if (dataSourceId == B2BDataSourceEnum.INSE.id) {
                result = inseOrderService.createPlanRequestEntity(engineerId, engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getInse().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getInse().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.KONKA.id) {
                result = konkaOrderService.createPlanRequestEntity(engineerName, engineerMobile, "");
                if (StringUtils.isNotBlank(microServicesProperties.getKonka().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getKonka().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.JOYOUNG.id) {
                result = joyoungOrderService.createPlanRequestEntity(engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getJoyoung().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getJoyoung().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.SUNING.id) {
                result = suningOrderService.createSuningPlanRequestEntity(engineerId, engineerName, engineerMobile);
            } else if (dataSourceId == B2BDataSourceEnum.JDUE.id) {
                result = jdueOrderService.createPlanRequestEntity(engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getJdue().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getJdue().getDefaultPhoneNumber());
                }
            }
//            else if (dataSourceId == B2BDataSourceEnum.JDUEPLUS.id) {
//                result = jduePlusOrderService.createPlanRequestEntity(engineerName, engineerMobile);
//                if (StringUtils.isNotBlank(microServicesProperties.getJduePlus().getDefaultPhoneNumber())) {
//                    result.getBElement().setEngineerMobile(microServicesProperties.getJduePlus().getDefaultPhoneNumber());
//                }
//            }
            else if (dataSourceId == B2BDataSourceEnum.XYINGYAN.id) {
                result = xyyPlusOrderService.createPlanRequestEntity(engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getXyyPlus().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getXyyPlus().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.LB.id) {
                result = lbOrderService.createPlanRequestEntity(engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getLb().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getLb().getDefaultPhoneNumber());
                }
            } else if (dataSourceId == B2BDataSourceEnum.TMALL.id) {
                result = tmallOrderService.createPlanRequestEntity(engineerName, engineerMobile);
            } else if (dataSourceId == B2BDataSourceEnum.PDD.id) {
                result = pddOrderService.createPlanRequestEntity(engineerId, engineerName, engineerMobile);
            } else if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                result = vioMiOrderService.createPlanRequestEntity(engineerName, engineerMobile, updater);
            } else if (dataSourceId == B2BDataSourceEnum.SF.id) {
                result = sfOrderService.createPlanRequestEntity(engineerName, engineerMobile);
            }  else if (dataSourceId == B2BDataSourceEnum.PHILIPS.id) {
                result = philipsOrderService.createPlanRequestEntity();
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.PLANNED, B2BOrderActionEnum.PLAN,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());
            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, b2bOrderNo, engineerId, engineerName, engineerMobile, updater, updateDate, B2BOrderStatusEnum.PLANNED);
                    saveFailureLog(log, "planB2BOrder");
                }
            }
        }
    }

    /**
     * 网点派单（APP派单、网点Web派单）
     */
    public void servicePointPlanOrder(Order order, Engineer engineer, User updater, Date updateDate) {
        if (order != null && order.getDataSource() != null && engineer != null) {
            Long servicePointId = engineer.getServicePoint() != null && engineer.getServicePoint().getId() != null ? engineer.getServicePoint().getId() : 0;
            servicePointPlanB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(), order.getId(), order.getOrderNo(),
                    servicePointId, engineer.getId(), engineer.getName(), engineer.getContactInfo(), updater, updateDate);
        }
    }

    private void servicePointPlanB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo,
                                          Long servicePointId, Long engineerId, String engineerName, String engineerMobile, User updater, Date updateDate) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (dataSourceId == B2BDataSourceEnum.JDUEPLUS.id) {
                result = jduePlusOrderService.createServicePointPlanRequestEntity(engineerName, engineerMobile);
                if (StringUtils.isNotBlank(microServicesProperties.getJduePlus().getDefaultPhoneNumber())) {
                    result.getBElement().setEngineerMobile(microServicesProperties.getJduePlus().getDefaultPhoneNumber());
                }
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.SERVICE_POINT_PLANNED, B2BOrderActionEnum.SERVICE_POINT_PLAN,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());
            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, b2bOrderNo, engineerId, engineerName, engineerMobile, updater, updateDate, B2BOrderStatusEnum.SERVICE_POINT_PLANNED);
                    saveFailureLog(log, "servicePointPlanB2BOrder");
                }
            }
        }
    }


    /**
     * B2B预约
     */
    public void pendingOrder(Order order, Long servicePointId, Long engineerId, Integer pendingType, Date appointmentDate, User updater, Date updateDate, String remarks) {
        if (order != null && order.getId() != null && order.getOrderCondition() != null && order.getOrderCondition().getCustomerId() > 0) {
            updateOrderStatus(B2BDataSourceEnum.UM, order.getOrderCondition().getCustomerId(), order.getId(), B2BOrderStatusEnum.APPOINTED);
        }
        if (order != null && order.getDataSource() != null) {
            appointB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(), order.getId(), order.getOrderNo(),
                    servicePointId, engineerId, pendingType, appointmentDate, remarks, updater, updateDate);
        }
    }

    private void appointB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo,
                                 Long servicePointId, Long engineerId, Integer pendingType, Date effectiveDate, String remarks, User updater, Date updateDate) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (B2BDataSourceEnum.isTooneDataSourceId(dataSourceId)) {
                result = canboOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.WEBER.id) {
                result = weberOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.MBO.id) {
                result = mboOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.SUPOR.id) {
                result = suporOrderService.createAppointRequestEntity(effectiveDate, pendingType);
            } else if (dataSourceId == B2BDataSourceEnum.JINJING.id) {
                result = jinJingOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.USATON_GA.id) {
                result = usatonGaOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            }  else if (dataSourceId == B2BDataSourceEnum.MQI.id) {
                result = mqiOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.TMALL.id) {
                result = tmallOrderService.createTmallAppointRequestEntity(effectiveDate, updater, servicePointId, engineerId);
            } else if (dataSourceId == B2BDataSourceEnum.JD.id) {
                result = jdOrderService.createJdPlanAndAppointRequestEntity(pendingType, effectiveDate, servicePointId, engineerId);
            } else if (dataSourceId == B2BDataSourceEnum.INSE.id) {
                result = inseOrderService.createAppointRequestEntity(effectiveDate);
            } else if (dataSourceId == B2BDataSourceEnum.KONKA.id) {
                result = konkaOrderService.createAppointRequestEntity(effectiveDate, updater, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.JOYOUNG.id) {
                result = joyoungOrderService.createAppointRequestEntity(pendingType, effectiveDate, updater, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.SUNING.id) {
                result = suningOrderService.createSuningAppointRequestEntity(effectiveDate, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.JDUE.id) {
                result = jdueOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.JDUEPLUS.id) {
                result = jduePlusOrderService.createAppointRequestEntity(effectiveDate, remarks, updater);
            } else if (dataSourceId == B2BDataSourceEnum.XYINGYAN.id) {
                result = xyyPlusOrderService.createAppointRequestEntity(effectiveDate, servicePointId, engineerId);
            } else if (dataSourceId == B2BDataSourceEnum.LB.id) {
                result = lbOrderService.createAppointRequestEntity(effectiveDate, servicePointId, engineerId);
            } else if (dataSourceId == B2BDataSourceEnum.PDD.id) {
                result = pddOrderService.createAppointRequestEntity(effectiveDate, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                result = vioMiOrderService.createAppointRequestEntity(effectiveDate, updater, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.SF.id) {
                result = sfOrderService.createAppointRequestEntity(effectiveDate, servicePointId, engineerId, updater, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.PHILIPS.id) {
                result = philipsOrderService.createAppointRequestEntity(effectiveDate);
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.APPOINTED, B2BOrderActionEnum.APPOINT,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());
            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, b2bOrderNo, updater, servicePointId, engineerId, updateDate, effectiveDate, remarks, B2BOrderStatusEnum.APPOINTED);
                    saveFailureLog(log, "appointB2BOrder");
                }
            }
        }
    }

    /**
     * B2B上门服务
     */
    public void serviceOrder(Order order, Long servicePointId, Long engineerId, User updater, Date updateDate) {
        if (order != null && order.getId() != null && order.getOrderCondition() != null && order.getOrderCondition().getCustomerId() > 0) {
            updateOrderStatus(B2BDataSourceEnum.UM, order.getOrderCondition().getCustomerId(), order.getId(), B2BOrderStatusEnum.SERVICED);
        }
        if (order != null && order.getDataSource() != null) {
            serviceB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(), order.getId(), order.getOrderNo(), order.getQuarter(),
                    servicePointId, engineerId, updater, updateDate, "");
        }
    }

    private void serviceB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo, String quarter,
                                 Long servicePointId, Long engineerId, User updater, Date updateDate, String remarks) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (dataSourceId == B2BDataSourceEnum.KONKA.id) {
                result = konkaOrderService.createServiceRequestEntity(updateDate, servicePointId, engineerId, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.JOYOUNG.id) {
                result = joyoungOrderService.createServiceRequestEntity(updateDate, servicePointId, engineerId, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.MQI.id) {
                result = mqiOrderService.createServiceRequestEntity(updateDate, servicePointId, engineerId, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.TMALL.id) {
                result = tmallOrderService.createServiceRequestEntity();
            } else if (dataSourceId == B2BDataSourceEnum.PDD.id) {
                result = pddOrderService.createServiceRequestEntity(updateDate, servicePointId, engineerId, remarks);
            } else if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                result = vioMiOrderService.createServiceRequestEntity(orderId, quarter, updater);
            }
//            //TODO: 确认上门时，不调用樱雪的上门接口
//            else if (dataSourceId == B2BDataSourceEnum.INSE.id) {
//                result = inseOrderService.createServiceRequestEntity();
//            }
            else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.SERVICED, B2BOrderActionEnum.SERVICE,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());
            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, b2bOrderNo, servicePointId, engineerId, updater, updateDate, B2BOrderStatusEnum.SERVICED);
                    saveFailureLog(log, "serviceB2BOrder");
                }
            }
        }
    }

    @Transactional()
    public void appCompleteOrder(Order order, User updater, Date updateDate) {
//        if (order != null && order.getDataSource() != null) {
//            OrderCondition condition = order.getOrderCondition();
//            if (condition == null || condition.getCreateDate() == null || condition.getCustomer() == null || condition.getCustomer().getId() == null) {
//                order = orderCacheReadService.getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true, false);
//            }
            if (order != null && order.getDataSource() != null) {
                long customerId = order.getOrderCondition() != null && order.getOrderCondition().getCustomer() != null ? order.getOrderCondition().getCustomerId() : 0;
                long servicePointId = order.getOrderCondition() != null && order.getOrderCondition().getServicePoint() != null && order.getOrderCondition().getServicePoint().getId() != null ? order.getOrderCondition().getServicePoint().getId() : 0;
                Date orderCreateDate = order.getOrderCondition() != null && order.getOrderCondition().getCreateDate() != null ? order.getOrderCondition().getCreateDate() : null;
                appCompleteB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(),
                        order.getId(), order.getOrderNo(), order.getQuarter(), customerId, servicePointId, order.getItems(), orderCreateDate, updater, updateDate);
            }
//        }
    }

    private void appCompleteB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo, String quarter,
                                     Long customerId, Long servicePointId, List<OrderItem> orderItems,
                                     Date orderCreateDate, User updater, Date updateDate) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (B2BDataSourceEnum.isTooneDataSourceId(dataSourceId) && dataSourceId == B2BDataSourceEnum.USATON.id) {
                result = canboOrderService.createCompleteRequestEntity(orderId, quarter, orderItems);
            } else if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                result = vioMiOrderService.createAppCompleteRequestEntity(orderId, quarter, customerId, servicePointId, orderItems, orderCreateDate, updater);
            } else if (dataSourceId == B2BDataSourceEnum.INSE.id) {
                result = inseOrderService.createInseCompleteRequestEntity(orderId, quarter, orderItems);
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.APP_COMPLETED, B2BOrderActionEnum.APP_COMPLETE,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());

            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, orderId, updater, updateDate, B2BOrderStatusEnum.APP_COMPLETED);
                    saveFailureLog(log, "appCompleteB2BOrder");
                }
            }
        }
    }

    @Transactional()
    public void validateOrder(Order order, OrderValidate orderValidate, User updater, Date updateDate) {
        if (order != null && order.getDataSource() != null) {
            long servicePointId = order.getOrderCondition() != null && order.getOrderCondition().getServicePoint() != null && order.getOrderCondition().getServicePoint().getId() != null ? order.getOrderCondition().getServicePoint().getId() : 0;
            Date orderCreateDate = order.getOrderCondition() != null && order.getOrderCondition().getCreateDate() != null ? order.getOrderCondition().getCreateDate() : null;
            validateB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(),
                    order.getId(), order.getOrderNo(), order.getQuarter(), servicePointId, orderValidate, orderCreateDate, updater, updateDate);
        }
    }

    private void validateB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo, String quarter,
                                  Long servicePointId, OrderValidate orderValidate,
                                  Date orderCreateDate, User updater, Date updateDate) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                result = vioMiOrderService.createValidateRequestEntity(orderId, quarter, servicePointId, orderCreateDate, orderValidate, updater);
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.VALIDATE, B2BOrderActionEnum.VALIDATE,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());
            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(dataSourceId, orderId, updater, updateDate, B2BOrderStatusEnum.VALIDATE);
                    saveFailureLog(log, "validateB2BOrder");
                }
            }
        }
    }

    /**
     * B2B完成工单
     * 阿斯丹顿上传好评照片前就调完工接口
     */
    @Transactional()
    public void completeOrder(Order order, Date completedDate, User updater, Date updateDate) {
        if (order != null && order.getId() != null) {
            OrderCondition condition = order.getOrderCondition();
            if (condition != null) {
                updateOrderStatus(B2BDataSourceEnum.UM, condition.getCustomerId(), order.getId(), B2BOrderStatusEnum.COMPLETED, completedDate);
            } else {
                Order cachedOrder = orderCacheReadService.getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true, false);
                if (cachedOrder != null) {
                    condition = cachedOrder.getOrderCondition();
                    updateOrderStatus(B2BDataSourceEnum.UM, condition.getCustomerId(), cachedOrder.getId(), B2BOrderStatusEnum.COMPLETED, completedDate);
                }
            }
            if (order.getDataSource() != null) {
                Date appCompleteDate = condition != null ? condition.getAppCompleteDate() : null;
                double orderCharge = order.getOrderFee() != null && order.getOrderFee().getOrderCharge() != null ? order.getOrderFee().getOrderCharge() : 0;
                completeB2BOrder(order.getDataSourceId(), order.getB2bOrderId(), order.getWorkCardId(), order.getId(), order.getOrderNo(), order.getQuarter(), order.getItems(), orderCharge,
                        completedDate, appCompleteDate, updater, updateDate, "");
            }
        }
    }

    private void completeB2BOrder(Integer dataSourceId, Long b2bOrderId, String b2bOrderNo, Long orderId, String orderNo, String quarter, List<OrderItem> orderItems, double orderCharge,
                                  Date effectiveDate, Date appCompleteDate, User updater, Date updateDate, String remarks) {
        if (isNeedSendOrderStatusMsgToB2B(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo)) {
            TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result;
            if (B2BDataSourceEnum.isTooneDataSourceId(dataSourceId) && dataSourceId == B2BDataSourceEnum.USATON.id) {
                result = canboOrderService.createCompleteRequestEntity(orderId, quarter, orderItems);
            } else {
                result = new TwoTuple<>(true, null);
            }
            if (result.getAElement() && result.getBElement() != null) {
                setB2BOrderStatusUpdateReqEntityProperties(result.getBElement(), B2BOrderStatusEnum.COMPLETED, B2BOrderActionEnum.COMPLETE,
                        dataSourceId, b2bOrderId, b2bOrderNo, orderId, orderNo, updater, updateDate);
                result.getBElement().setAppCompleteDt(appCompleteDate == null ? 0L : appCompleteDate.getTime());
                sendB2BOrderStatusUpdateMessage(result.getBElement().build());

            } else {
                if (!result.getAElement()) {
                    B2BOrderStatusUpdateFailureLog log = new B2BOrderStatusUpdateFailureLog(orderId, quarter, orderItems, dataSourceId, b2bOrderNo, updater, updateDate, effectiveDate, remarks, B2BOrderStatusEnum.COMPLETED);
                    saveFailureLog(log, "completeB2BOrder");
                }
            }
        }
    }

    //endregion 创建工单状态变更消息实体

    //------------------------------------------------------------------------------------------往队列发送消息、处理队列消息

    //region 向队列发送消息、处理消息

    /**
     * 发送B2B工单的状态变更消息
     */
    private void sendB2BOrderStatusUpdateMessage(B2BOrderStatusUpdateReqEntity reqEntity) {
        MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage.Builder builder = MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage.newBuilder();
        builder.setDataSource(reqEntity.getDataSourceId())
                .setOrderId(reqEntity.getOrderId())
                .setKklOrderNO(reqEntity.getKklOrderNo())
                .setB2BOrderId(reqEntity.getB2bOrderId())
                .setB2BOrderNo(reqEntity.getB2bOrderNo())
                .setStatus(reqEntity.getStatus().value)
                .setUpdaterId(reqEntity.getUpdaterId())
                .setUpdaterMobile(reqEntity.getUpdaterMobile())
                .setUpdaterName(reqEntity.getUpdaterName())
                .setEngineerId(StringUtils.toString(reqEntity.getEngineerId()))
                .setEngineerName(StringUtils.toString(reqEntity.getEngineerName()))
                .setEngineerMobile(StringUtils.toString(reqEntity.getEngineerMobile()))
                .setUpdateDt(reqEntity.getUpdateDate().getTime())
                .setEffectiveDt(reqEntity.getEffectiveDate() != null ? reqEntity.getEffectiveDate().getTime() : 0)
                .setRemarks(reqEntity.getRemarks())
                .setAppointmentStatus(reqEntity.getAppointmentStatus())
                .setInstallStatus(reqEntity.getInstallStaus())
                .setServicePointId(reqEntity.getServicePointId() == null ? 0 : reqEntity.getServicePointId())
                .setMessageId(sequenceIdService.nextId())
                .setActionType(reqEntity.getActionType().value)
                .setOrderCharge(reqEntity.getOrderCharge() == null ? 0.0 : reqEntity.getOrderCharge())
                .setKklPendingType(StringUtils.toString(reqEntity.getPendingType()))
                .setActualTotalSurcharge(reqEntity.getActualTotalSurcharge() == null ? 0.0 : reqEntity.getActualTotalSurcharge())
                .setCustomerTotalCharge(reqEntity.getCustomerTotalCharge() == null ? 0.0 : reqEntity.getCustomerTotalCharge())
                .setChargeAt(reqEntity.getChargeAt() == null ? 0 : reqEntity.getChargeAt())
                .setAppCompleteDt(reqEntity.getAppCompleteDt() == null ? 0 : reqEntity.getAppCompleteDt())
                .setB2BReason(StringUtils.toString(reqEntity.getB2bReason()))
                .setLongitude(reqEntity.getLongitude() == null ? 0.0 : reqEntity.getLongitude())
                .setLatitude(reqEntity.getLatitude() == null ? 0.0 : reqEntity.getLatitude())
                .setVerifyCode(StringUtils.toString(reqEntity.getVerifyCode()))
                .setExtraField1(StringUtils.toString(reqEntity.getExtraField1()));
        if (reqEntity.getOrderCompletedItems() != null && !reqEntity.getOrderCompletedItems().isEmpty()) {
            MQB2BOrderStatusUpdateMessage.CompletedItem.Builder completedItemBuilder;
            MQB2BOrderStatusUpdateMessage.B2BSurchargeItem surchargeItem;
            MQB2BOrderStatusUpdateMessage.ErrorItem errorItem;
            MQB2BOrderStatusUpdateMessage.Material material;
            MQB2BOrderStatusUpdateMessage.PicItem picItem;
            for (B2BOrderCompletedItem item : reqEntity.getOrderCompletedItems()) {
                completedItemBuilder = MQB2BOrderStatusUpdateMessage.CompletedItem.newBuilder()
                        .setItemCode(StringUtils.toString(item.getB2bProductCode()))
                        .setPic1(StringUtils.toString(item.getPic1()))
                        .setPic2(StringUtils.toString(item.getPic2()))
                        .setPic3(StringUtils.toString(item.getPic3()))
                        .setPic4(StringUtils.toString(item.getPic4()))
                        .setBarcode(StringUtils.toString(item.getUnitBarcode()))
                        .setOutBarcode(StringUtils.toString(item.getOutBarcode()))
                        .setBuyDt(item.getBuyDt());
                for (B2BOrderCompletedItem.B2BSurchargeItem innerItem : item.getSurchargeItems()) {
                    surchargeItem = MQB2BOrderStatusUpdateMessage.B2BSurchargeItem.newBuilder()
                            .setCategoryId(innerItem.getCategoryId())
                            .setCategoryName(innerItem.getCategoryName())
                            .setItemId(innerItem.getItemId())
                            .setItemName(innerItem.getItemName())
                            .setItemQty(innerItem.getItemQty())
                            .setUnitPrice(innerItem.getUnitPrice())
                            .setTotalPrice(innerItem.getTotalPrice())
                            .build();
                    completedItemBuilder.addSurchargeItems(surchargeItem);
                }
                for (B2BOrderCompletedItem.ErrorItem innerItem : item.getErrorItems()) {
                    errorItem = MQB2BOrderStatusUpdateMessage.ErrorItem.newBuilder()
                            .setErrorTypeId(innerItem.getErrorTypeId())
                            .setErrorType(innerItem.getErrorType())
                            .setErrorCodeId(innerItem.getErrorCodeId())
                            .setErrorCode(innerItem.getErrorCode())
                            .setErrorAnalysisId(innerItem.getErrorAnalysisId())
                            .setErrorAnalysis(innerItem.getErrorAnalysis())
                            .setErrorActionId(innerItem.getErrorActionId())
                            .setErrorAction(innerItem.getErrorAction())
                            .build();
                    completedItemBuilder.addErrorItem(errorItem);
                }
                for (B2BOrderCompletedItem.Material innerItem : item.getMaterials()) {
                    material = MQB2BOrderStatusUpdateMessage.Material.newBuilder()
                            .setMaterialId(innerItem.getMaterialId())
                            .setMaterialCode(innerItem.getMaterialCode())
                            .setQty(innerItem.getQty())
                            .build();
                    completedItemBuilder.addMaterial(material);
                }
                for (B2BOrderCompletedItem.PicItem innerItem : item.getPicItems()) {
                    picItem = MQB2BOrderStatusUpdateMessage.PicItem.newBuilder()
                            .setCode(innerItem.getCode())
                            .setUrl(innerItem.getUrl())
                            .build();
                    completedItemBuilder.addPicItem(picItem);
                }
                builder.addCompletedItem(completedItemBuilder.build());
            }
        } else {
            if (reqEntity.getCompletedItems() != null && !reqEntity.getCompletedItems().isEmpty()) {
                MQB2BOrderStatusUpdateMessage.CompletedItem completedItem;
                for (CanboOrderCompleted.CompletedItem item : reqEntity.getCompletedItems()) {
                    completedItem = MQB2BOrderStatusUpdateMessage.CompletedItem.newBuilder()
                            .setItemCode(StringUtils.toString(item.getItemCode()))
                            .setPic1(StringUtils.toString(item.getPic1()))
                            .setPic2(StringUtils.toString(item.getPic2()))
                            .setPic3(StringUtils.toString(item.getPic3()))
                            .setPic4(StringUtils.toString(item.getPic4()))
                            .setBarcode(StringUtils.toString(item.getBarcode()))
                            .setOutBarcode(StringUtils.toString(item.getOutBarcode()))
                            .build();
                    builder.addCompletedItem(completedItem);
                }
            }
        }
        if (reqEntity.getServiceItems() != null && !reqEntity.getServiceItems().isEmpty()) {
            MQB2BOrderStatusUpdateMessage.ServiceItem serviceItem;
            for (B2BOrderServiceItem item : reqEntity.getServiceItems()) {
                serviceItem = MQB2BOrderStatusUpdateMessage.ServiceItem.newBuilder()
                        .setServiceItemId(item.getServiceItemId())
                        .setServiceAt(item.getServiceAt())
                        .setProductId(item.getProductId())
                        .setServiceTypeId(item.getServiceTypeId())
                        .setQty(item.getQty())
                        .setCharge(item.getCharge())
                        .build();
                builder.addServiceItem(serviceItem);
            }
        }
        if (reqEntity.getOrderPraiseItem() != null && !reqEntity.getOrderPraiseItem().getPicUrls().isEmpty()) {
            MQB2BOrderStatusUpdateMessage.PraiseItem praiseItem = MQB2BOrderStatusUpdateMessage.PraiseItem.newBuilder()
                    .addAllPicUrl(reqEntity.getOrderPraiseItem().getPicUrls())
                    .build();
            builder.setPraiseItem(praiseItem);
        }
        if (reqEntity.getOrderValidateItem() != null) {
            B2BOrderValidateItem params = reqEntity.getOrderValidateItem();
            B2BOrderValidateItem.ErrorItem innerItem = params.getErrorItem();
            MQB2BOrderStatusUpdateMessage.ErrorItem errorItem = MQB2BOrderStatusUpdateMessage.ErrorItem.newBuilder()
                    .setErrorTypeId(innerItem.getErrorTypeId())
                    .setErrorType(innerItem.getErrorType())
                    .setErrorCodeId(innerItem.getErrorCodeId())
                    .setErrorCode(innerItem.getErrorCode())
                    .setErrorAnalysisId(innerItem.getErrorAnalysisId())
                    .setErrorAnalysis(innerItem.getErrorAnalysis())
                    .setErrorActionId(innerItem.getErrorActionId())
                    .setErrorAction(innerItem.getErrorAction())
                    .build();
            MQB2BOrderStatusUpdateMessage.ValidateItem validateItem = MQB2BOrderStatusUpdateMessage.ValidateItem.newBuilder()
                    .setProductId(params.getProductId())
                    .setProductSn(params.getProductSn())
                    .setBuyDt(params.getBuyDt())
                    .setIsFault(params.getIsFault())
                    .setErrorDescription(params.getErrorDescription())
                    .setCheckValidateDetail(params.getCheckValidateDetail())
                    .setPackValidateDetail(params.getPackValidateDetail())
                    .setReceiver(params.getReceiver())
                    .setReceivePhone(params.getReceivePhone())
                    .setReceiveAddress(params.getReceiveAddress())
                    .setErrorItem(errorItem)
                    .addAllCheckValidateResultValues(params.getCheckValidateResultValues())
                    .addAllPackValidateResultValues(params.getPackValidateResultValues())
                    .addAllPicUrl(params.getPicUrls())
                    .build();
            builder.setValidateItem(validateItem);
        }
        b2BOrderStatusUpdateMQSender.send(builder.build());
    }

    //endregion 向队列发送消息、处理消息

    //-----------------------------------------------------------------------------------------------------------更新B2B微服务DB的工单状态
    //region 更新B2B微服务DB的工单状态

    private MSResponse updateOrderStatus(B2BDataSourceEnum dataSource, Long customerId, Long kklOrderId, B2BOrderStatusEnum status) {
        return updateOrderStatus(dataSource, customerId, kklOrderId, status, null);
    }

    private MSResponse updateOrderStatus(B2BDataSourceEnum dataSource, Long customerId, Long kklOrderId, B2BOrderStatusEnum status, Date closeDate) {
        MSResponse response = new MSResponse(MSErrorCode.SUCCESS);
        if (B2BMDUtils.isOrderStatusUpdateEnabled(dataSource, customerId) && kklOrderId != null && kklOrderId > 0 && status != null) {
            response = umOrderService.updateOrderStatus(kklOrderId, status, closeDate == null ? 0 : closeDate.getTime());
        }
        return response;
    }

    //endregion 更新B2B微服务DB的工单状态

    //region 检查产品条码

    public MSResponse checkProductSN(Integer dataSourceId, String b2bOrderNo, String productSn, User operator) {
        MSResponse response = new MSResponse(MSErrorCode.SUCCESS);
        if (B2BDataSourceEnum.isB2BDataSource(dataSourceId) && StringUtils.isNotBlank(b2bOrderNo) && StringUtils.isNotBlank(productSn)) {
            operator = (operator == null || operator.getId() == null ? B2BOrderVModel.b2bUser : operator);
            if (dataSourceId == B2BDataSourceEnum.VIOMI.id) {
                response = vioMiOrderService.checkProductSN(b2bOrderNo, productSn, operator);
            } else if (dataSourceId == B2BDataSourceEnum.JOYOUNG.id) {
                response = joyoungOrderService.checkProductSN(productSn);
            }  else if (dataSourceId == B2BDataSourceEnum.MQI.id) {
                response = mqiOrderService.checkProductSN(b2bOrderNo, productSn);
            }
        }
        return response;
    }

    //endregion 检查产品条码

    //region 检查维修故障

    /**
     * 检查维修单的上门服务是否包含维修故障
     */
    public boolean checkRepairError(Integer dataSourceId, Integer orderServiceType, List<OrderDetail> orderDetails) {
        if (dataSourceId != B2BDataSourceEnum.VIOMI.id) {
            return true;
        }
        boolean result = false;
        if (ObjectUtil.isNotEmpty(orderDetails)) {
            result = true;
            for (OrderDetail detail : orderDetails) {
                if (orderServiceType == OrderUtils.OrderTypeEnum.REPAIRE.getId()
                        && (detail.getErrorType() == null || detail.getErrorType().getId() == null || detail.getActionCode() == null || detail.getActionCode().getId() == null)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    //endregion 检查维修故障

}
