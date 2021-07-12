/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.controller.sd;

import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.AbnormalFormEnum;
import com.kkl.kklplus.entity.md.AppFeedbackEnum;
import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.kkl.kklplus.utils.ExceptionUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.sd.RestAppFeedback;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSetAppointmentDateRequest;
import com.wolfking.jeesite.modules.api.service.sd.RestOrderService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderOpitionTrace;
import com.wolfking.jeesite.modules.sd.service.OrderOpitionTraceService;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.cc.service.AbnormalFormService;
import com.wolfking.jeesite.ms.providermd.service.MSAppFeedbackTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * App反馈
 *
 * @author Ryan
 * @version 2020-01-08
 */
@Slf4j
@RestController
@RequestMapping("/api/feedback/")
public class RestAppFeedbackController extends RestBaseController {

    @Autowired
    private RestOrderService restOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private MSAppFeedbackTypeService msAppFeedbackTypeService;

    @Autowired
    private OrderOpitionTraceService orderOpitionTraceService;

    @Autowired
    private AbnormalFormService abnormalFormService;

    /**
     * app反馈 - 停滞
     * {
     * "orderId": "订单id",
     * "quarter": "数据分片",
     * "pendingType": 停滞原因id,
     * "appointmentDate": 预约日期,
     * "remarks": "备注"
     * }
     */
    @RequestMapping(value = "/setPengding", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> appPengding(HttpServletRequest request, HttpServletResponse response,
                                          @RequestBody RestSetAppointmentDateRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (orderRequest.getPendingType() == null || orderRequest.getPendingType() <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "请选择停滞原因");
        }
        long feedbackId = orderRequest.getPendingType().longValue();
        if (orderRequest.getAppointmentDate() == null || orderRequest.getAppointmentDate() <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "日期未设定");
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        if (orderRequest.getAppointmentDate() == null || orderRequest.getAppointmentDate() <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "预约日期未设定");
        }
        Date date = new Date();
        Date appointmentDate = new Date(orderRequest.getAppointmentDate());
        if (DateUtils.pastMinutes(DateUtils.addHour(date, -1), appointmentDate) <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "预约日期应在现在之后");
        }
        //账号
        User user = getLoginUser(request);
        if (user == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        long userId = user.getId();
        Dict pendingType = null;
        try {
            //get 反馈内容 from md
            MDAppFeedbackType mdAppFeedbackType = msAppFeedbackTypeService.getByIdFromCache(feedbackId);
            if (mdAppFeedbackType == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取反馈类型:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取工单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            OrderCondition condition = order.getOrderCondition();
            if (condition.getServicePoint() == null || condition.getEngineer() == null) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取师傅信息失败");
            }
            Engineer engineer = servicePointService.getEngineerFromCache(condition.getServicePoint().getId(), condition.getEngineer().getId());
            if (null == engineer) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取师傅信息失败");
            }
            //检查订单状态：1.停滞 2.APP异常 4.取消工单(APP异常) 5.申请改派(APP异常)
            int feedbackType = mdAppFeedbackType.getFeedbackType();
            if (feedbackType == 1 || feedbackType == 2 || feedbackType == 4 || feedbackType == 5) {

                RestResult restResult = restOrderService.checkOrderStatus(order);
                if (restResult.getCode() != ErrorCode.NO_ERROR.code) {
                    return restResult;
                }
            }
            pendingType = new Dict(mdAppFeedbackType.getValue(), mdAppFeedbackType.getName());
            RestAppFeedback restAppFeedback = new RestAppFeedback()
                    .setOrder(order)
                    .setOrderId(orderId)
                    .setQuarter(order.getQuarter())
                    .setPendingType(pendingType)
                    .setAppointmentAt(orderRequest.getAppointmentDate());
            //times 同网点反馈
            Integer times = orderOpitionTraceService.getTimesByServicepoint(orderId, order.getQuarter(), mdAppFeedbackType.getParentId().intValue(), condition.getServicePoint().getId());
            if (times == null) {
                times = 1;
            } else {
                times = times + 1;
            }
            //total times 同订单反馈
            Integer totalTimes = orderOpitionTraceService.getTotalTimesByOpinionType(orderId, order.getQuarter(), mdAppFeedbackType.getParentId().intValue());
            if (totalTimes == null) {
                totalTimes = 1;
            } else {
                totalTimes = totalTimes + 1;
            }
            OrderOpitionTrace opitionTrace = OrderOpitionTrace.builder()
                    .channel(AppFeedbackEnum.Channel.APP.getValue())
                    .quarter(condition.getQuarter())
                    .orderId(orderId)
                    .servicePointId(condition.getServicePoint().getId())
                    .appointmentAt(appointmentDate.getTime())
                    .opinionId(mdAppFeedbackType.getId().intValue())
                    .parentId(mdAppFeedbackType.getParentId().intValue())
                    .opinionType(mdAppFeedbackType.getFeedbackType())
                    .opinionValue(mdAppFeedbackType.getValue())
                    .opinionLabel(StringUtils.left(mdAppFeedbackType.getLabel(), 250))
                    .isAbnormaly(mdAppFeedbackType.getIsAbnormaly())
                    .remark(StringUtils.left(orderRequest.getRemarks(), 250))
                    .createAt(date.getTime())
                    .createBy(user)
                    .times(times)
                    .totalTimes(totalTimes)
                    .build();
            // 累计超过指定次数 isAbnormaly= 0,abnormalyOverTimes >0

            if (mdAppFeedbackType.getIsAbnormaly() == 0 && mdAppFeedbackType.getAbnormalyOverTimes() > 0) {
                if (mdAppFeedbackType.getSumType() == AppFeedbackEnum.SummaryType.ORDER.getValue() && totalTimes >= mdAppFeedbackType.getAbnormalyOverTimes()) {
                    //按订单控制
                    opitionTrace.setIsAbnormaly(1);
                } else if (mdAppFeedbackType.getSumType() == AppFeedbackEnum.SummaryType.SERVICEPOINT.getValue() && times >= mdAppFeedbackType.getAbnormalyOverTimes()) {
                    //按网点控制
                    opitionTrace.setIsAbnormaly(1);
                }
            }
            AbnormalForm abnormalForm = null;
            if (opitionTrace.getIsAbnormaly() == 1) {
                abnormalForm = abnormalFormService.handleAbnormalForm(order, mdAppFeedbackType.getLabel(), user, AppFeedbackEnum.Channel.APP.getValue(),
                        AbnormalFormEnum.FormType.APP_ABNORMALY.code, AbnormalFormEnum.SubType.PENGING.getCode(), orderRequest.getRemarks());
            }
            restAppFeedback.setAbnormalForm(abnormalForm);
            restAppFeedback.setOrderOpitionTrace(opitionTrace);
            return restOrderService.saveAppPengdingV2(request, restAppFeedback, user);
        } catch (Exception e) {
            log.error("[新App停滞", e);
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[新App停滞] user:{} ,json:{}", userId, gson, e);
            } catch (Exception e1) {
                log.error("[新App停滞] user:{}", userId, e);
            }
            return RestResultGenerator.exception("保存停滞原因错误:" + StringUtils.left(ExceptionUtils.getRootCause(e).getMessage(), 100));
        }
    }

    /**
     * app反馈 - 标记异常
     * {
     * "orderId": "订单id",
     * "quarter": "数据分片",
     * "pendingType": 反馈id,
     * "remarks": "备注"
     * }
     */
    @RequestMapping(value = "setAbnormal", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> saveWOAbnormal(HttpServletRequest request, HttpServletResponse response,
                                             @RequestBody RestSetAppointmentDateRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "订单ID或分片为空");
        }
        if (orderRequest.getPendingType() == null || orderRequest.getPendingType() <= 0) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "请选择反馈原因");
        }
        long feedbackId = orderRequest.getPendingType().longValue();
        if (StringUtils.isNoneBlank(orderRequest.getRemarks()) && orderRequest.getRemarks().trim().length() > 250) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "备注说明长度过长，请勿超过250字");
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        //账号
        User user = getLoginUser(request);
        if (user == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        long userId = user.getId();
        try {
            //get 反馈内容 from md
            MDAppFeedbackType mdAppFeedbackType = msAppFeedbackTypeService.getByIdFromCache(feedbackId);
            if (mdAppFeedbackType == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取反馈类型:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            MDAppFeedbackType parent = msAppFeedbackTypeService.getByIdFromCache(mdAppFeedbackType.getParentId());
            if (parent == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取反馈类型:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取工单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            OrderCondition condition = order.getOrderCondition();
            if (condition.getServicePoint() == null || condition.getEngineer() == null) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取师傅信息失败");
            }
            //times：同网点反馈
            Integer times = orderOpitionTraceService.getTimesByServicepoint(orderId, order.getQuarter(), mdAppFeedbackType.getParentId().intValue(), condition.getServicePoint().getId());
            if (times == null) {
                times = 1;
            } else {
                times = times + 1;
            }
            //total times：同订单反馈
            Integer totalTimes = orderOpitionTraceService.getTotalTimesByOpinionType(orderId, order.getQuarter(), mdAppFeedbackType.getParentId().intValue());
            if (totalTimes == null) {
                totalTimes = 1;
            } else {
                totalTimes = totalTimes + 1;
            }
            RestAppFeedback restAppFeedback = new RestAppFeedback()
                    .setOrder(order)
                    .setOrderId(orderId)
                    .setQuarter(order.getQuarter())
                    .setPendingType(null)
                    .setAppointmentAt(0);

            OrderOpitionTrace opitionTrace = OrderOpitionTrace.builder()
                    .channel(AppFeedbackEnum.Channel.APP.getValue())
                    .quarter(condition.getQuarter())
                    .orderId(orderId)
                    .servicePointId(condition.getServicePoint().getId())
                    .appointmentAt(0)
                    .opinionId(mdAppFeedbackType.getId().intValue())
                    .parentId(mdAppFeedbackType.getParentId().intValue())
                    .parent(parent)
                    .opinionType(mdAppFeedbackType.getFeedbackType())
                    .opinionValue(mdAppFeedbackType.getValue())
                    .opinionLabel(StringUtils.left(mdAppFeedbackType.getLabel(), 250))
                    .isAbnormaly(0)
                    .remark(StringUtils.left(orderRequest.getRemarks(), 250))
                    .createAt(System.currentTimeMillis())
                    .createBy(user)
                    .times(times)
                    .totalTimes(totalTimes)
                    .build();
            if (mdAppFeedbackType.getIsAbnormaly() == 1) {
                if (mdAppFeedbackType.getSumType() == AppFeedbackEnum.SummaryType.ORDER.getValue() && totalTimes >= mdAppFeedbackType.getAbnormalyOverTimes()) {
                    //按订单控制
                    opitionTrace.setIsAbnormaly(1);
                } else if (mdAppFeedbackType.getSumType() == AppFeedbackEnum.SummaryType.SERVICEPOINT.getValue() && times >= mdAppFeedbackType.getAbnormalyOverTimes()) {
                    //按网点控制
                    opitionTrace.setIsAbnormaly(1);
                }
            }

            //异常单
            AbnormalForm abnormalForm = abnormalFormService.handleAbnormalForm(order, mdAppFeedbackType.getLabel(), user, AppFeedbackEnum.Channel.APP.getValue(),
                    AbnormalFormEnum.FormType.APP_ABNORMALY.code, parent.getId().intValue(), orderRequest.getRemarks());
            restAppFeedback.setOrderOpitionTrace(opitionTrace);
            restAppFeedback.setAbnormalForm(abnormalForm);
            restOrderService.saveAppAbnormalyV2(restAppFeedback, user);
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[新App异常] user:{} ,json:{}", userId, gson, e);
            } catch (Exception e1) {
                log.error("[新App异常] user:{}", userId, e);
            }
            return RestResultGenerator.exception("订单标记异常失败");
        }
    }

}
