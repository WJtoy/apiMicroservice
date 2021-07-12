/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.controller.sd;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.praise.PraiseStatusEnum;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.exception.AttachmentSaveFailureException;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppPraisePicItem;
import com.wolfking.jeesite.modules.api.entity.sd.*;
import com.wolfking.jeesite.modules.api.entity.sd.request.*;
import com.wolfking.jeesite.modules.api.service.sd.AppOrderPraiseNewService;
import com.wolfking.jeesite.modules.api.service.sd.AppSubAccountOrderListService;
import com.wolfking.jeesite.modules.api.service.sd.RestOrderService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.md.service.CustomerMaterialService;
import com.wolfking.jeesite.modules.md.service.MaterialService;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import com.wolfking.jeesite.modules.sd.service.*;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.viewModel.FileUpload;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.modules.sys.utils.SeqUtils;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import com.wolfking.jeesite.modules.utils.OperationCommand;
import com.wolfking.jeesite.modules.utils.VerificationUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderService;
import com.wolfking.jeesite.ms.providermd.service.MSAppFeedbackTypeService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerProductService;
import com.wolfking.jeesite.ms.providermd.service.MSRegionPermissionNewService;
import com.wolfking.jeesite.ms.providermd.service.MSServicePointPriceService;
import com.wolfking.jeesite.ms.service.sys.MSDictService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 订单处理
 *
 * @author Ryan
 * @version 2017-11-27
 */
@Slf4j
@RestController
@RequestMapping("/api/order/")
public class RestOrderController extends RestBaseController {

    @Autowired
    private SequenceIdService sequenceIdService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MSDictService msDictService;

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private AppSubAccountOrderListService appSubAccountOrderListService;

    @Autowired
    private RestOrderService restOrderService;

    @Autowired
    private OrderMaterialService orderMaterialService;

    @Autowired
    private OrderMaterialReturnService orderMaterialReturnService;

    @Autowired
    private CustomerMaterialService customerMaterialService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MSRegionPermissionNewService msRegionPermissionNewService;

    @Autowired
    private OrderStatusFlagService orderStatusFlagService;

    @Autowired
    private AppOrderPraiseNewService orderPraiseNewService;

    @Autowired
    private B2BCenterOrderService b2BCenterOrderService;

    @Autowired
    private MSCustomerProductService msCustomerProductService;

    @Bean
    public MapperFactory getFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    @Autowired
    private MSServicePointPriceService msServicePointPriceService;

    //region 列表

    /**
     * CS2001 & CS2002
     *
     * 查询条件：工单号码,用户姓名,用户电话,用户地址,工单类型查询订单,负责区域,接单日期,工单状态,预约日期
     * ,投诉,加急,催单 等
     */
    @RequestMapping(value = "list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> list(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody RestOrderSearchRequest orderRequest) {
        if (orderRequest == null || orderRequest.getListType() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (orderRequest.getPageNo() == null || orderRequest.getPageNo() == 0) {
            orderRequest.setPageNo(1);
        }
        if (orderRequest.getPageSize() == null || orderRequest.getPageSize() == 0) {
            orderRequest.setPageSize(10);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            OrderServicePointSearchModel searchModel = mapper.map(orderRequest, OrderServicePointSearchModel.class);
            searchModel.setServicePointId(userInfo.getServicePointId());
            searchModel.setIsAppRequest(1);
            if (!userInfo.getPrimary()) {
                searchModel.setEngineerId(userInfo.getEngineerId());
            } else {
                searchModel.setEngineerId(null);
                searchModel.setMasterId(userInfo.getEngineerId());
            }
            Page<OrderServicePointSearchModel> searchPage = new Page<>(orderRequest.getPageNo(), orderRequest.getPageSize());
            Page<RestOrder> page;
            if (userInfo.getPrimary()) {
                boolean hasNoSubAccount = orderRequest.getHasNoSubAccount() != null && orderRequest.getHasNoSubAccount() == 1;
                page = appOrderService.getNotCompletedOrderList(searchPage, searchModel, hasNoSubAccount);
            } else {
                page = appSubAccountOrderListService.getNotCompletedOrderList(searchPage, searchModel);
            }
            return RestResultGenerator.success(page);
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.list] user:{} ,json:{}", userInfo.getUserId(), GsonUtils.toGsonString(orderRequest), e);
            } catch (Exception e1) {
                log.error("[RestOrderController.list] user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }

    }

    /**
     * CS2020
     * 可抢工单列表
     */
    @RequestMapping(value = "grabList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> grabList(HttpServletRequest request, HttpServletResponse response) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (!userInfo.getPrimary()) {
                return RestResultGenerator.custom(ErrorCode.USRE_NO_ACCESS_APP.code, "您的帐号不是网点主帐号，无接单权限");
            }
            Engineer engineer = servicePointService.getEngineerFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            if (null == engineer) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取帐号信息失败");
            }
            if (0 == engineer.getAppFlag()) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, "您的帐号未开通手机接单权限");
            }
            //md_servicepoint-status -> 网点状态为10才允许使用APP接单
            ServicePoint servicePoint = servicePointService.getFromCache(userInfo.getServicePointId());
            if (!servicePoint.canGrabOrPlanOrder()) {
                return RestResultGenerator.custom(ErrorCode.USRE_NO_ACCESS_APP.code, "您的网点当前不允许手机接单");
            }
            List<RestOrderGrab> orders = appOrderService.getGrabOrderList(userInfo.getServicePointId(), userInfo.getEngineerId());
            return RestResultGenerator.success(orders);
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.grabList] user:{}", userInfo.getUserId(), e);
            } catch (Exception e1) {
                log.error("[RestOrderController.grabList] user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }

    }


    /**
     * CS2019
     * 历史订单
     * 查询条件：工单号码,用户姓名,用户电话,用户地址,工单类型,负责区域,接单日期,是否已结算 等
     */
    @RequestMapping(value = "history", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> historyNew(HttpServletRequest request, HttpServletResponse response,
                                         @RequestBody RestOrderHistoryRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (orderRequest.getPageNo() == null || orderRequest.getPageNo() == 0) {
            orderRequest.setPageNo(1);
        }
        if (orderRequest.getPageSize() == null || orderRequest.getPageSize() == 0) {
            orderRequest.setPageSize(10);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            OrderServicePointSearchModel searchModel = mapper.map(orderRequest, OrderServicePointSearchModel.class);
            searchModel.setServicePointId(userInfo.getServicePointId());
            if (userInfo.getPrimary()) {
                searchModel.setEngineerId(null);
            } else {
                searchModel.setEngineerId(userInfo.getEngineerId());
            }
            Page<OrderServicePointSearchModel> searchPage = new Page<>(orderRequest.getPageNo(), orderRequest.getPageSize());
            Page<RestOrderGrading> resPage = appOrderService.getCompletedOrderList(searchPage, searchModel, userInfo.getPrimary());
            return RestResultGenerator.success(resPage);
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.historyNew] user:{} ,json:{}", userId, GsonUtils.toGsonString(orderRequest), e);
            } catch (Exception e1) {
                log.error("[RestOrderController.historyNew] user:{}", userId, e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * CS2002
     * 网点待客评工单列表
     * 查询条件：工单号码,用户姓名,用户电话,用户地址,工单类型,接单日期,安维姓名 等
     */
    @RequestMapping(value = "gradingList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> gradingListNew(HttpServletRequest request, HttpServletResponse response,
                                             @RequestBody RestOrderHistoryRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (orderRequest.getPageNo() == null || orderRequest.getPageNo() == 0) {
            orderRequest.setPageNo(1);
        }
        if (orderRequest.getPageSize() == null || orderRequest.getPageSize() == 0) {
            orderRequest.setPageSize(10);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            OrderServicePointSearchModel searchModel = mapper.map(orderRequest, OrderServicePointSearchModel.class);
            searchModel.setServicePointId(userInfo.getServicePointId());
            if (userInfo.getPrimary()) {
                searchModel.setServicePointId(userInfo.getServicePointId());
                searchModel.setEngineerId(null);
            } else {
                searchModel.setServicePointId(userInfo.getServicePointId());
                searchModel.setEngineerId(userInfo.getEngineerId());
            }
            Page<OrderServicePointSearchModel> searchPage = new Page<>(orderRequest.getPageNo(), orderRequest.getPageSize());
            Page<RestOrderGrading> resPage = appOrderService.getGradingOrderList(searchPage, searchModel, userInfo.getPrimary());
            return RestResultGenerator.success(resPage);
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.gradingListNew] user:{} ,json:{}", userInfo.getUserId(), GsonUtils.toGsonString(orderRequest), e);
            } catch (Exception e1) {
                log.error("[RestOrderController.gradingListNew] user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    //endregion 列表

    //region 业务

    /**
     * CS2021
     * 网点接单
     */
    @RequestMapping(value = "grab", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> grabWO(HttpServletRequest request, HttpServletResponse response,
                                     @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "订单参数类型错误");
        }
        long userId = 0;
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (false == userInfo.getPrimary()) {
                return RestResultGenerator.custom(ErrorCode.USRE_NO_ACCESS_APP.code, "您的帐号不是网点主帐号，无抢单权限");
            }
            User user = UserUtils.getAcount(userInfo.getUserId());
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Engineer engineer = servicePointService.getEngineerFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            if (null == engineer) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取帐号信息失败");
            }
            if (0 == engineer.getAppFlag()) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, "您的帐号未开通手机抢单权限");
            }
            if (userInfo.getServicePointId() != engineer.getServicePoint().getId().longValue()) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, "您的帐号现在归属的网点与登录时网点不同");
            }
            ServicePoint servicePoint = servicePointService.getFromCache(engineer.getServicePoint().getId());
            if (servicePoint == null) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取帐号的网点信息失败");
            }
            engineer.setServicePoint(servicePoint);
            //订单读取时，已从缓存中读取了customer完整信息
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.DETAIL, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            if (order.getOrderCondition().getCustomer() == null || order.getOrderCondition().getCustomer().getFinance() == null
                    || order.getOrderCondition().getCustomer().getFinance().getPaymentType() == null
                    || StringUtils.isBlank(order.getOrderCondition().getCustomer().getFinance().getPaymentType().getValue())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_GRAB.code, "厂商信息不完整，请重试");
            }
            if (!order.canPlanOrder()) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_GRAB.code, "订单已派单");
            }
<<<<<<< .mine

            boolean bool =VerificationUtils.compareOrderStatus(orderId,orderRequest.getQuarter(), OperationCommand.OperationCode.GRAB.code);
            if (!bool){
                RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_GRAB.code, ErrorCode.ORDER_CAN_NOT_GRAB.message);
            }
||||||| .r12970
=======
            //街道偏远区域及网点服务价格检查 2021/05/17
            RestResult<Object> remoteCheckResult = orderService.checkServicePointRemoteAreaAndPrice(servicePoint.getId(), order.getOrderCondition(), order.getItems());
            if(remoteCheckResult.getCode() != ErrorCode.NO_ERROR.code){
                return remoteCheckResult;
            }
            //判断网点容量与未完工数量 2021/06/02
            if(servicePoint.getUnfinishedOrderCount()>=servicePoint.getCapacity()){
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_GRAB.code, "您未完工单数量已经超过工单容量,接单失败");
            }
>>>>>>> .r13215
            orderService.grabOrder(order, user, engineer);
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.grabWO] user:{} ,json:{}", userInfo.getUserId(), gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "REST抢单失败",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.grabWO] user:{}", userInfo.getUserId(), e);
            }
            if (e instanceof RuntimeException) {
                return RestResultGenerator.exception(e.getMessage().replace("java.lang.RuntimeException:", ""));
            } else {
                return RestResultGenerator.exception("抢单失败");
            }
        }
    }

    /**
     * CS2026
     * 订单基本信息 For 网点
     */
    @RequestMapping(value = "servicePointOrderInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> servicePointOrderInfo(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || (StringUtils.isBlank(orderRequest.getOrderId()) && StringUtils.isBlank(orderRequest.getOrderNo()))) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        if (StringUtils.isNotBlank(orderRequest.getOrderId())) {
            try {
                orderId = Long.valueOf(orderRequest.getOrderId());
            } catch (Exception e) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
            }
        }
        if (orderId == null && StringUtils.isBlank(orderRequest.getOrderNo())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = null;
            if (orderId == null) {
                String quarter = orderRequest.getQuarter();
                if (StringUtils.isBlank(quarter)) {
                    quarter = QuarterUtils.getOrderQuarterFromNo(orderRequest.getOrderNo().trim());
                }
                order = orderService.getOrderIdByNo(orderRequest.getOrderNo().trim(), quarter);
                if (order == null) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
                }
                order = orderService.getOrderById(order.getId(), order.getQuarter(), OrderUtils.OrderDataLevel.DETAIL, true);
            } else {
                order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.DETAIL, true);
            }
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }

            List<OrderDetail> details = order.getDetailList();
            if (details != null && details.size() > 0) {
                if (userInfo.getPrimary()) {//主帐号
                    details = details.stream()
                            .filter(t -> t.getServicePoint().getId().equals(Long.valueOf(userInfo.getServicePointId())))
                            .collect(Collectors.toList());
                } else {
                    details = details.stream()
                            .filter(t -> t.getServicePoint().getId().equals(Long.valueOf(userInfo.getServicePointId()))
                                    && t.getEngineer().getId().equals(Long.valueOf(userInfo.getEngineerId()))
                            )
                            .collect(Collectors.toList());
                }
                if (details.size() > 0) {
                    order.setDetailList(details);
                } else {
                    order.setDetailList(Lists.newArrayList());
                }
            }
            RestServicePointOrderInfo entity = mapper.map(order, RestServicePointOrderInfo.class);
            OrderCondition orderCondition = order.getOrderCondition();
            //预估服务费用，从派单记录表中取
            OrderPlan orderPlan = orderService.getOrderPlan(order.getId(), order.getQuarter(), orderCondition.getServicePoint().getId(), orderCondition.getEngineer().getId());
            if (orderPlan == null) {
                entity.setEstimatedServiceCost(0.0d);
            } else {
                entity.setEstimatedServiceCost(orderPlan.getEstimatedServiceCost());
            }
            return RestResultGenerator.success(entity);
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.servicePointOrderInfo] user:{} ,json:{}", userId, GsonUtils.toGsonString(orderRequest), e);
            } catch (Exception e1) {
                log.error("[RestOrderController.servicePointOrderInfo] user:{}", userId, e);
            }
            return RestResultGenerator.exception("读取网点订单信息");
        }
    }

    /**
     * CS2004
     * 订单详情
     */
    @RequestMapping(value = "detail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> orderDetailInfo(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本工单详情接口已停用，请及时更新APP");
    }

    /**
     * CS2004
     * 订单详情
     */
    @RequestMapping(value = "detailNew", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getOrderDetailNew(HttpServletRequest request, HttpServletResponse response,
                                                @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.DETAIL, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            MappingContext orikaContext = new MappingContext.Factory().getContext();
            //配件
            if (1 == order.getOrderCondition().getPartsFlag()) {
                List<MaterialMaster> materials = orderMaterialService.findMaterialMasterHeadsByOrderId(orderId, order.getQuarter());
                order.setMaterials(materials);
                List<MaterialReturn> returnMaterials = orderMaterialReturnService.getMaterialReturnListForGrade(orderId,order.getQuarter());
                orikaContext.setProperty("returnMaterials",returnMaterials);
            }
            //products
            Set<Product> products = orderService.getOrderProducts(order.getItems(), true, false);
            if (products.size() > 0) {
                order.setProducts(Lists.newArrayList(products));
            }
            RestOrderDetailInfo entity = mapper.map(order, RestOrderDetailInfo.class,orikaContext);

            //判断产品是否有安装规范
            List<Long> productIds = entity.getItems().stream().map(RestOrderItem::getProductId).distinct().collect(Collectors.toList());
            Map<Long, Integer> hasFixSpecMap = msCustomerProductService.findFixSpecByCustomerIdAndProductIdsFromCache(entity.getCustomerId(), productIds);
            if (hasFixSpecMap != null) {
                for (RestOrderItem restOrderItem : entity.getItems()) {
                    restOrderItem.setHasFixSpec(hasFixSpecMap.get(restOrderItem.getProductId())==null?0:hasFixSpecMap.get(restOrderItem.getProductId()));
                }
            }
            if (order.getOrderStatus().getReminderStatus() == 1) {
                //待回复催单
                RestOrder restOrder = new RestOrder();
                restOrder.setOrderId(order.getId());
                restOrder.setQuarter(order.getQuarter());
                restOrder.setReminderFlag(order.getOrderStatus().getReminderStatus());
                List<RestOrder> restOrders = Lists.newArrayList(restOrder);
                appOrderService.loadWaitReplyReminderInfo(restOrders);
                entity.setReminderItemNo(restOrder.getReminderItemNo());
                entity.setReminderTimeoutAt(restOrder.getReminderTimeoutAt());
            }
            //好评标记
            OrderStatusFlag orderStatusFlag = orderStatusFlagService.getByOrderId(order.getId(), order.getQuarter());
            if (orderStatusFlag != null) {
                entity.setPraiseStatus(orderStatusFlag.getPraiseStatus());
                if (orderStatusFlag.getPraiseStatus() > PraiseStatusEnum.NONE.code) {
                    List<AppPraisePicItem> praisePicItems = orderPraiseNewService.getUploadedPraisePics(order.getId(), order.getQuarter(), userInfo.getServicePointId());
                    entity.setPraisePics(praisePicItems);
                }
            }
            //产品图片规则
            Long customerId = order.getOrderCondition().getCustomer() == null ? 0 : order.getOrderCondition().getCustomer().getId();
            List<RestProductCompletePic> picRules = appOrderService.getProductPicRules(order.getItems(), customerId);
            entity.setPicRules(picRules);
            //已经上传的产品图片
            List<RestProductCompletePic> orderPics = appOrderService.getUploadedProductPics(orderId, order.getQuarter(), customerId);
            entity.setOrderPics(orderPics);

            //是否设置了辅材或服务项目
            ThreeTuple<Boolean, Double, Double> auxiliaryMaterialQtyAndTotalCharge = appOrderService.getQtyAndTotalChargeOfOrderAuxiliaryMaterial(orderId, order.getQuarter());
            entity.setHasAuxiliaryMaterials(auxiliaryMaterialQtyAndTotalCharge.getAElement() ? 1 : 0);
            entity.setAuxiliaryMaterialsTotalCharge(auxiliaryMaterialQtyAndTotalCharge.getBElement());
            entity.setAuxiliaryMaterialsActualTotalCharge(auxiliaryMaterialQtyAndTotalCharge.getCElement());

            //进一步处理
            entity.setIsAppCompleted(0);
            Long buyDate = order.getOrderAdditionalInfo() != null && order.getOrderAdditionalInfo().getBuyDate() != null ? order.getOrderAdditionalInfo().getBuyDate() : 0L;
            entity.setBuyDate(buyDate);
            if (!order.getOrderCondition().getServicePoint().getId().equals(userInfo.getServicePointId())) {
                entity.setIsAppCompleted(1);
            } else if (StringUtils.isNoneBlank(order.getOrderCondition().getAppCompleteType())) {
                entity.setIsAppCompleted(1);
            }
            //预估服务费用，从派单记录表中取
            OrderCondition orderCondition = order.getOrderCondition();
            OrderPlan orderPlan = orderService.getOrderPlan(order.getId(), order.getQuarter(), orderCondition.getServicePoint().getId(), orderCondition.getEngineer().getId());
            if (orderPlan == null || !userInfo.getPrimary()) {
                entity.setEstimatedServiceCost(0.0d);
            } else {
                entity.setEstimatedServiceCost(orderPlan.getEstimatedServiceCost() + orderPlan.getEstimatedTravelCost() + orderPlan.getEstimatedOtherCost());//=服务费+远程费+其它费用
            }
            if (entity.getServices().size() > 0) {
                Map<String, Double> fees = Maps.newHashMapWithExpectedSize(6);
                fees.put("engineerServiceCharge", 0.0);
                fees.put("engineerTravelCharge", 0.0);
                fees.put("engineerExpressCharge", 0.0);
                fees.put("engineerMaterialCharge", 0.0);
                fees.put("engineerOtherCharge", 0.0);
                fees.put("engineerChage", 0.0);
                //安维只能看到自己做的上门服务
                List<RestOrderDetail>  services = entity.getServices().stream()
                        .filter(t -> t.getServicePointId().equals(Long.valueOf(userInfo.getServicePointId()))
                                && t.getEngineerId().equals(Long.valueOf(userInfo.getEngineerId()))
                        )
                        .collect(Collectors.toList());
                if (services.size() > 0) {
                    services.stream().forEach(t -> {
                        fees.put("engineerServiceCharge", fees.get("engineerServiceCharge") + t.getEngineerServiceCharge());
                        fees.put("engineerTravelCharge", fees.get("engineerTravelCharge") + t.getEngineerTravelCharge());
                        fees.put("engineerExpressCharge", fees.get("engineerExpressCharge") + t.getEngineerExpressCharge());
                        fees.put("engineerMaterialCharge", fees.get("engineerMaterialCharge") + t.getEngineerMaterialCharge());
                        fees.put("engineerOtherCharge", fees.get("engineerOtherCharge") + t.getEngineerOtherCharge());
                        fees.put("engineerChage", fees.get("engineerChage") + t.getEngineerChage());
                    });
                    entity.setServices(services);
                    entity.setEngineerServiceCharge(fees.get("engineerServiceCharge"));
                    entity.setEngineerTravelCharge(fees.get("engineerTravelCharge"));
                    entity.setEngineerExpressCharge(fees.get("engineerExpressCharge"));
                    entity.setEngineerMaterialCharge(fees.get("engineerMaterialCharge"));
                    entity.setEngineerOtherCharge(fees.get("engineerOtherCharge"));
                    entity.setEngineerCharge(fees.get("engineerChage"));

                } else {
                    entity.setServices(Lists.newArrayList());
                }
            }
            return RestResultGenerator.success(entity);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.orderDetailInfo] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "REST读取订单详情",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.orderDetailInfo] user:{}", userId, e);
            }
            return RestResultGenerator.exception("读取订单详情错误");
        }
    }

    /**
     * CS2024
     * 上传订单完成图片
     */
    @RequestMapping(value = "uploadFinishPic", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> uploadFinishPic(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam("file") MultipartFile[] files,
                                              @RequestParam("json") String json) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本图片上传接口已停用，请及时更新APP");
    }

    /**
     * CS2024
     * 上传订单完成图片
     */
    @RequestMapping(value = "uploadFinishPicNew", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> uploadFinishPicNew(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam("file") MultipartFile[] files,
                                                 @RequestParam("json") String json) {
        if (files == null || files.length == 0) {
            return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "请选择图片文件");
        }
        RestUploadProductCompletePicRequest param = GsonUtils.getInstance().fromJson(json, RestUploadProductCompletePicRequest.class);
        if (param == null || param.getOrderId() == null || param.getOrderId() <= 0 || StringUtils.isBlank(param.getQuarter())
                || param.getProductId() == null || param.getProductId() <= 0 || StringUtils.isBlank(param.getPictureCode())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(param.getOrderId(), param.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            TwoTuple<Boolean, String> saveFileResult = OrderPicUtils.saveFile(request, files[0]);
            if (saveFileResult.getAElement() && StringUtils.isNotBlank(saveFileResult.getBElement())) {
                Long customerId = order.getOrderCondition().getCustomer() == null ? 0 : order.getOrderCondition().getCustomer().getId();
                RestUploadProductCompletePic restPicItem = appOrderService.uploadOrderPic(saveFileResult.getBElement(), param.getOrderId(), param.getQuarter(),
                        param.getProductId(), param.getUniqueId(), param.getPictureCode(), user, customerId);
                if (restPicItem != null) {
                    return RestResultGenerator.success(restPicItem);
                } else {
                    return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "上传失败", files);
                }
            } else {
                return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "上传失败", files);
            }
        } catch (OrderException orderException) {
            return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, orderException.getLocalizedMessage());
        } catch (Exception e) {
            log.error("[RestOrderController.uploadFinishPic] user:{} ,json:{}", userId, json, e);
            return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "上传失败");
        }
    }

    /**
     * CS2013
     * 删除订单完成图片
     */
    @RequestMapping(value = "deleteFinishPic", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> deleteFinishPic(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本图片删除接口已停用，请及时更新APP");
    }

    /**
     * CS2013
     * 删除订单完成图片
     */
    @RequestMapping(value = "deleteFinishPicNew", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> deleteFinishPicNew(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestBody RestUploadProductCompletePicRequest param) {
        if (param == null || param.getOrderId() == null || param.getOrderId() <= 0 || StringUtils.isBlank(param.getQuarter())
                || param.getUniqueId() == null || param.getUniqueId() <= 0 || StringUtils.isBlank(param.getPictureCode())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            appOrderService.deletePic(request, param.getOrderId(), param.getQuarter(), param.getUniqueId(), param.getPictureCode(), user);
            return RestResultGenerator.success();
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(param);
                log.error("[RestOrderController.deleteFinishPic] user:{} ,json:{}", userId, gson, e);
            } catch (Exception e1) {
                log.error("[RestOrderController.orderDetailInfo] user:{}", userId, e);
            }
            return RestResultGenerator.exception("删除完成照片失败");
        }
    }

    @RequestMapping(value = "saveProductSN", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> saveProductSN(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody RestSaveProductSNRequest params) {
        if (params == null || params.getUniqueId() == null || params.getUniqueId() <= 0
                || StringUtils.isBlank(params.getProductSN())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (params.getProductSN().length() > 50) {
            return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "产品条码太长，请检查后重试");
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (params.getDataSource()!=null
                    && (params.getDataSource() == B2BDataSourceEnum.VIOMI.id
                    || params.getDataSource() == B2BDataSourceEnum.JOYOUNG.id
                    || params.getDataSource() == B2BDataSourceEnum.MQI.id)
                    && StringUtils.isNotBlank(params.getWorkCardId())){
                MSResponse msResponse = b2BCenterOrderService.checkProductSN(params.getDataSource(), params.getWorkCardId(),
                        params.getProductSN(), user);
                if (msResponse.getCode()!= MSErrorCode.CODE_VALUE_SUCCESS){
                    return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "产品条码检查错误，请检查后重试");
                }
            }
            appOrderService.updateProductSN(params.getUniqueId(), params.getProductSN(), user);

            return RestResultGenerator.success();
        } catch (Exception e) {
            String json = GsonUtils.toGsonString(params);
            log.error("[RestOrderController.saveProductSN] user:{} ,json:{}", userId, json, e);
            return RestResultGenerator.custom(ErrorCode.MEMBER_PICTURE_UPLOAD_FAILED.code, "上传失败");
        }
    }

    /**
     * CS2014
     * 按订单id清除订单所有完成图片
     */
    @RequestMapping(value = "clearFinishPics", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> deletePicByWO(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody RestOrderBaseRequest orderRequest) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本图片清空接口已停用，请及时更新APP");
    }

    /**
     * CS2016
     * 订单日志
     */
    @RequestMapping(value = "statusLogs", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getAppOrderLogs(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.HEAD, true);
            if (order == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            RestOrderStatusLog log = new RestOrderStatusLog();
            log.setOrderNo(order.getOrderNo());

            List<OrderProcessLog> logs = appOrderService.getOrderProcessLogs(orderId, orderRequest.getQuarter().trim(), userInfo.getServicePointId());
            log.setLogs(logs);
            return RestResultGenerator.success(log);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.getAppOrderLogs] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(request, null, e, "Rest读取订单日志", "POST", gson, new User(userId));
            } catch (Exception e1) {
                log.error("[RestOrderController.getAppOrderLogs] user:{}", userId, e);
            }
            return RestResultGenerator.exception("读取订单日志错误");
        }
    }

    /**
     * CS2017
     * 预约日期
     */
    @RequestMapping(value = "setAppointmentDate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> setAppointmentDate(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestBody RestSetAppointmentDateRequest orderRequest) {
        return restOrderService.saveAppAppointmentDate(request, null, orderRequest);
    }

    /**
     * CS2008
     * 停滞原因
     */
    @RequestMapping(value = "setPengding", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> appPengding(HttpServletRequest request, HttpServletResponse response,
                                          @RequestBody RestSetAppointmentDateRequest orderRequest) {
        return restOrderService.saveAppPengding(request, null, orderRequest);
    }

    /**
     * CS2009
     * App标记异常
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
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "请选择停滞原因");
        }
        if (StringUtils.isNoneBlank(orderRequest.getRemarks()) && orderRequest.getRemarks().trim().length() > 200) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "备注说明长度过长，请勿超过200字");
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Dict pendingType = msDictService.getDictByValue(orderRequest.getPendingType().toString(), "order_abnormal_reason");
            if (pendingType == null) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVEABNORMAL.code, "订单标记异常失败：请选择异常原因");
            }
            orderService.saveAppAbnormaly(orderId, orderRequest.getQuarter(), userInfo.getServicePointId(), pendingType, user, orderRequest.getRemarks().trim());

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.saveWOAbnormal] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest订单标记异常",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.saveWOAbnormal] user:{}", userId, e);
            }
            return RestResultGenerator.exception("订单标记异常失败");
        }
    }

    /**
     * CS2005
     * 确认上门：一键添加上门服务项目
     */
    @RequestMapping(value = "confirmDoor", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> quickCompleteWO(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "参数输入不完整：订单ID或分片为空");
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            orderService.confirmDoorAuto(orderId, orderRequest.getQuarter(), user, 1);

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.saveConfirmDoor] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest确认上门",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.saveConfirmDoor] user:{}", userId, e);
            }
            String msg = Throwables.getRootCause(e).getMessage();
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_CONFIRMDOOR.code, (StringUtils.isBlank(msg) ? "订单确认上门失败" : msg));
        }
    }

    /**
     * （新）确认上门
     * @param request
     * @param response
     * @param orderRequest
     * @return
     */
    @RequestMapping(value = "confirmDoorNew", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> quickCompleteWONew(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "参数输入不完整：订单ID或分片为空");
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }

            boolean bool = VerificationUtils.compare(
                    orderId,orderRequest.getQuarter(),OperationCommand.OperationCode.CONFIRM_DOOR_NEW.code,userInfo.getEngineerId(),userInfo.getServicePointId()
            );
            if (!bool){
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_CONFIRMDOOR.code,ErrorCode.ORDER_CAN_NOT_CONFIRMDOOR.message);
            }

            RestOrderDetailInfoNew restOrderDetailInfoNew = orderService.confirmDoorAutoNew(orderId, orderRequest.getQuarter(), user, 1);
            //汇总费用
            if (restOrderDetailInfoNew.getServices().size() > 0) {
                Map<String, Double> fees = Maps.newHashMapWithExpectedSize(6);
                fees.put("engineerServiceCharge", 0.0);
                fees.put("engineerTravelCharge", 0.0);
                fees.put("engineerExpressCharge", 0.0);
                fees.put("engineerMaterialCharge", 0.0);
                fees.put("engineerOtherCharge", 0.0);
                fees.put("engineerChage", 0.0);
                //安维只能看到自己做的上门服务
                List<RestOrderDetail> services = restOrderDetailInfoNew.getServices().stream()
                        .filter(t -> t.getServicePointId().equals(Long.valueOf(userInfo.getServicePointId()))
                                && t.getEngineerId().equals(Long.valueOf(userInfo.getEngineerId()))
                        )
                        .collect(Collectors.toList());
                if (services.size() > 0) {
                    services.stream().forEach(t -> {
                        fees.put("engineerServiceCharge", fees.get("engineerServiceCharge") + t.getEngineerServiceCharge());
                        fees.put("engineerTravelCharge", fees.get("engineerTravelCharge") + t.getEngineerTravelCharge());
                        fees.put("engineerExpressCharge", fees.get("engineerExpressCharge") + t.getEngineerExpressCharge());
                        fees.put("engineerMaterialCharge", fees.get("engineerMaterialCharge") + t.getEngineerMaterialCharge());
                        fees.put("engineerOtherCharge", fees.get("engineerOtherCharge") + t.getEngineerOtherCharge());
                        fees.put("engineerChage", fees.get("engineerChage") + t.getEngineerChage());
                    });
                    restOrderDetailInfoNew.setServices(services);
                    restOrderDetailInfoNew.setEngineerServiceCharge(fees.get("engineerServiceCharge"));
                    restOrderDetailInfoNew.setEngineerTravelCharge(fees.get("engineerTravelCharge"));
                    restOrderDetailInfoNew.setEngineerExpressCharge(fees.get("engineerExpressCharge"));
                    restOrderDetailInfoNew.setEngineerMaterialCharge(fees.get("engineerMaterialCharge"));
                    restOrderDetailInfoNew.setEngineerOtherCharge(fees.get("engineerOtherCharge"));
                    restOrderDetailInfoNew.setEngineerCharge(fees.get("engineerChage"));

                } else {
                    restOrderDetailInfoNew.setServices(Lists.newArrayList());
                }
            }
            return RestResultGenerator.success(restOrderDetailInfoNew);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.saveConfirmDoor] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest确认上门",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.saveConfirmDoor] user:{}", userId, e);
            }
            String msg = Throwables.getRootCause(e).getMessage();
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_CONFIRMDOOR.code, (StringUtils.isBlank(msg) ? "订单确认上门失败" : msg));
        }
    }







    /**
     * CS2018
     * 网点派单
     */
    @RequestMapping(value = "servicePlan", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> setWOServiceEngineer(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestBody RestServicePlanRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        if (StringUtils.isBlank(orderRequest.getEngineerId())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "未设定安维");
        }
        if (!StringUtils.isNumeric(orderRequest.getEngineerId())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "安维id类型错误");
        }
        Long engineerId = Long.valueOf(orderRequest.getEngineerId());
        if (engineerId <= 0) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "安维id应为正数");
        }

        if (orderRequest.getRemarks().trim().length() > 200) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "派单说明长度过长，请勿超过200字");
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = new Order();
            order.setId(orderId);
            order.setQuarter(orderRequest.getQuarter());
            order.setRemarks(orderRequest.getRemarks());

            OrderCondition condition = new OrderCondition();
            condition.setOrderId(orderId);
            condition.setQuarter(orderRequest.getQuarter());
            condition.setEngineer(new User(Long.valueOf(orderRequest.getEngineerId())));
            condition.setServicePoint(new ServicePoint(userInfo.getServicePointId()));
            order.setCreateBy(user);
            order.setOrderCondition(condition);

            orderService.servicePointPlanOrder(order);

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.setWOServiceEngineer] user:{} ,json:{}", userId, gson, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest网点派单",
                        "POST",
                        gson,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.setWOServiceEngineer] user:{}", userId, e);
            }
            return RestResultGenerator.exception("无法派单给师傅");
        }
    }

    /**
     * CS2015
     * 工单完工
     */
    @RequestMapping(value = "appClose", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> saveOrderComplete(HttpServletRequest request, HttpServletResponse response,
                                                @RequestBody RestCloseOrderRequest orderRequest) {
        return restOrderService.saveAppComplete(request, null, orderRequest);
    }
    @RequestMapping(value = "appCloseV2", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> saveOrderCompleteV2(HttpServletRequest request, HttpServletResponse response,
                                                @RequestBody RestCloseOrderRequest orderRequest) {
        return restOrderService.saveAppCompleteV2(request, null, orderRequest);
    }

    /**
     * CS2027
     * 电话联系用户
     */
    @RequestMapping(value = "callUser", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> callUser(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "参数输入不完整：订单ID或分片为空");
        }

        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        long userId = 0;
        RestResult restResult = RestResultGenerator.success();
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (user == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Engineer engineer = servicePointService.getEngineerFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            if (engineer == null) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, ErrorCode.MEMBER_ENGINEER_NO_EXSIT.message);
            }
            Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.STATUS, true);
            if (order == null || order.getOrderCondition() == null || order.getOrderStatus() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            //检查订单状态
            restResult = restOrderService.checkOrderStatus(order);
            if (restResult.getCode() != ErrorCode.NO_ERROR.code) {
                return restResult;
            }
            restResult = orderService.saveCallUser(order, user);
            return restResult;
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String gson = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.callUser] user:{} ,json:{}", userId, gson, e);
            } catch (Exception e1) {
                log.error("[RestOrderController.callUser] user:{}", userId, e);
            }
            if (restResult != null && restResult.getCode() > 0) {
                return restResult;
            } else {
                return RestResultGenerator.exception("安维人员APP联系用户");
            }
        }
    }

    //endregion 业务

    //region 配件

    /**
     * CS2006
     * 配件列表
     */
    @RequestMapping(value = "accessory/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getAccessoryListByWO(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            List<MaterialMaster> materialMasters = orderMaterialService.findMaterialMastersByOrderIdMS(orderId, orderRequest.getQuarter());
            List<RestMaterialMaster> list = mapper.mapAsList(materialMasters, RestMaterialMaster.class);
            return RestResultGenerator.success(list);
        } catch (OrderException oe) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderController.getAccessoryListByWO] orderId:{}", orderId, e);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * CS2010
     * 按配件单id读取返件申请单信息
     */
    @RequestMapping(value = "accessory/returnDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getReturnAccessoryDetail(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long id = null;
        try {
            id = Long.valueOf(orderRequest.getId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "配件单id类型错误");
        }

        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            MaterialReturn returnForm = orderMaterialReturnService.getMaterialReturnNoAttachById(null, id, orderRequest.getQuarter(), 0, 0, 1, 0);
            if (returnForm == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "返件申请单不存在，或读取失败，请重试。");
            }
            RestMaterialMaster master = null;
            try {
                master = mapper.map(returnForm, RestMaterialMaster.class);
                if (master == null) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "数据处理错误。");
                }
            } catch (Exception e) {
                LogUtils.saveLog("Rest读取返件单错误:转换错误", "getReturnAccessoryDetail", orderRequest.getId().toString(), e, new User(userInfo.getUserId()));
            }
            return RestResultGenerator.success(master);
        } catch (OrderException oe) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderController.getReturnAccessoryDetail] orderId:{}", id, e);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * CS2023
     * 上传配件图片(预留)
     */
    @RequestMapping(value = "accessory/uploadPic", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> uploadAccessoryPic(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest muti = (MultipartHttpServletRequest) request;

        String savePath = new String();
        savePath = Global.getUploadfilesDir();
        StringBuffer sbPath = new StringBuffer(200);
        if (StringUtils.isBlank(savePath)) {
            savePath = request.getServletContext().getRealPath("") + "/uploads/";
        }
        sbPath.append(DateUtils.getYear())
                .append("/")
                .append(DateUtils.getMonth())
                .append("/")
                .append(DateUtils.getDay())
                .append("/");

        File f1 = new File(savePath + sbPath.toString());
        if (!f1.exists()) {
            f1.mkdirs();
        }

        MultiValueMap<String, MultipartFile> map = muti.getMultiFileMap();
        String name = new String();
        String extName = new String();
        String type = new String();
        long size;
        FileUpload uploadResult;
        List<FileUpload> files = Lists.newArrayList();
//        String host = Global.getConfig("userfiles.host") + "/";
        String host = ApiPropertiesUtils.getUserFiles().getHost() + "/";
        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {
            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                uploadResult = new FileUpload();
                try {
                    name = multipartFile.getOriginalFilename();
                    uploadResult.setOrigalName(multipartFile.getOriginalFilename());
                    if (StringUtils.isBlank(name)) {
                        uploadResult.setStatus(1);
                    } else {
                        //扩展名格式：
                        if (name.lastIndexOf(".") >= 0) {
                            extName = name.substring(name.lastIndexOf("."));
                        }
                        name = UUID.randomUUID().toString() + extName;
                        multipartFile.transferTo(new File(savePath + sbPath.toString() + name));
                        uploadResult.setFileName(sbPath.toString() + name);
                        uploadResult.setStatus(0);
                        uploadResult.setUrl(host + uploadResult.getFileName());
                    }
                } catch (IllegalStateException | IOException e) {
                    uploadResult.setStatus(1);
                    log.error("[RestOrderController.uploadAccessoryPic]", e);
                } finally {
                    files.add(uploadResult);
                }
            }
        }
        map.clear();//清除元素
        return RestResultGenerator.success(files);
    }


    /**
     * CS2011
     * 保存配件申请
     * 每次提交只提交一个产品的配件单，如是套组，在app中进行产品拆分
     */
    @RequestMapping(value = "accessory/save", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> saveAccessory(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam("file") MultipartFile[] files,
                                            @RequestParam("json") String json) {
        String message = new String("");
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestSaveAccessoryRequest orderRequest = GsonUtils.getInstance().fromJson(json, RestSaveAccessoryRequest.class);
            if (orderRequest == null) {
                message = ErrorCode.WRONG_REQUEST_FORMAT.message;
            } else if (StringUtils.isBlank(orderRequest.getOrderId()) || StringUtils.isBlank(orderRequest.getQuarter())) {
                message = "订单参数为空";
            } else if (orderRequest.getApplyType() == null || orderRequest.getApplyType() == 0 || orderRequest.getApplyType() > 2) {
                message = "申请类型无值，或超出系统定义范围";
            } else if (orderRequest.getItems() == null || orderRequest.getItems().size() == 0) {
                message = "请选择配件";
            }
            if (StringUtils.isNoneBlank(message)) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, message);
            }
            Long orderId = null;
            try {
                orderId = Long.valueOf(orderRequest.getOrderId());
            } catch (Exception e) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
            }
            String quarter = orderRequest.getQuarter();
            Date date = new Date();

            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.DETAIL, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            //save files.html
            List<MaterialAttachment> attachments = Lists.newArrayList();
            if (files != null && files.length > 0) {
                String savePath = new String();
                savePath = Global.getUploadfilesDir();
                StringBuffer sbPath = new StringBuffer(200);
                if (StringUtils.isBlank(savePath)) {
                    savePath = request.getServletContext().getRealPath("") + "/uploads/";
                }
                sbPath.append(DateUtils.getYear())
                        .append("/")
                        .append(DateUtils.getMonth())
                        .append("/")
                        .append(DateUtils.getDay())
                        .append("/");

                File f1 = new File(savePath + sbPath.toString());
                if (!f1.exists()) {
                    f1.mkdirs();
                }

                String name = new String();
                String extName = new String();
                String type = new String();
                long size;
                OrderAttachment attach;
                FileUpload uploadResult;
//                String host = Global.getConfig("userfiles.host") + "/";
//                String host = Global.getConfig("userfiles.host") + "/";
                for (int i = 0, isize = files.length; i < isize; i++) {
                    MultipartFile file = files[i];
                    uploadResult = new FileUpload();
                    try {
                        name = file.getOriginalFilename();
                        uploadResult.setOrigalName(file.getOriginalFilename());
                        if (StringUtils.isBlank(name)) {
                            uploadResult.setStatus(1);
                        } else {
                            //扩展名格式：
                            if (name.lastIndexOf(".") >= 0) {
                                extName = name.substring(name.lastIndexOf("."));
                            }
                            name = UUID.randomUUID().toString() + extName;
                            file.transferTo(new File(savePath + sbPath.toString() + name));

                            MaterialAttachment attachment = new MaterialAttachment();
                            attachment.setId(sequenceIdService.nextId());
                            attachment.setOrderId(orderId);
                            attachment.setQuarter(quarter);
                            attachment.setFilePath(sbPath.toString() + name);
                            attachment.setCreateBy(user);
                            attachment.setCreateDate(date);
                            attachments.add(attachment);
                        }
                    } catch (IllegalStateException | IOException e) {
                        LogUtils.saveLog(
                                request,
                                null,
                                e,
                                "Rest配件申请",
                                "POST",
                                json,
                                new User(userId)
                        );
                        return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.code, ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.message + ":保存文件失败");
                    }
                }//end for
            }
            //save to db
            MaterialMaster m = new MaterialMaster();
            m.setId(sequenceIdService.nextId());
            String no = SeqUtils.NextSequenceNo("MaterialFormNo", 0, 3);
            if (StringUtils.isBlank(no)) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "生成配件单号错误,请重试！");
            }
            m.setMasterNo(no);
            m.setThrdNo(order.getParentBizOrderId() == null ? "" : order.getParentBizOrderId());
            //截取前30个字符
            m.setOrderCreator(StringUtils.left(order.getOrderCondition().getCreateBy().getName(), 30));
            m.setOrderDetailId(0l);
            m.setOrderId(orderId);
            m.setQuarter(quarter);
            m.setDataSource(order.getDataSource().getIntValue());//2019-10-18
            String shopId = Optional.ofNullable(order.getB2bShop()).map(t->t.getShopId()).orElse(StrUtil.EMPTY);
            m.setShopId(StrUtil.trimToEmpty(shopId));//店铺id 2021/06/22
            m.setCreateBy(user);
            m.setCreateDate(date);
            m.setApplyType(new Dict(orderRequest.getApplyType().toString()));
            m.setRemarks(orderRequest.getRemarks());
            Product product = productService.getProductByIdFromCache(Long.valueOf(orderRequest.getProductId()));
            if (product == null) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "读取产品:" + orderRequest.getProductId() + " 失败");
            }
            m.setProduct(product);
            StringBuffer sb = new StringBuffer(100);
            sb.append(",").append(orderRequest.getProductId()).append(",");
            m.setProductIds(sb.toString());
            sb.setLength(0);
            sb.append(",").append(product.getName()).append(",");
            m.setProductNames(sb.toString());
            sb.toString();
            //2019-06-03
            m.setOrderNo(order.getOrderNo());
            OrderCondition condition = order.getOrderCondition();
            m.setCustomer(condition.getCustomer());
            m.setArea(condition.getArea());
            m.setSubArea(condition.getSubArea());
            m.setUserName(condition.getUserName());
            m.setUserPhone(condition.getServicePhone());
            m.setUserAddress(condition.getAddress());
            m.setCanRush(condition.getCanRush());
            m.setKefuType(condition.getKefuType());
            m.setProductCategoryId(condition.getProductCategoryId());
            m.setDescription(order.getDescription());
            orderMaterialService.setArea(condition, m);
            //end
            HashMap<String, Double> totalPriceMap = Maps.newHashMapWithExpectedSize(1);
            totalPriceMap.put("totalPrice", 0.0);
            Double totalPrice = 0.0;
            long cid = m.getCustomer().getId();
            long pid = m.getProduct().getId();
            orderRequest.getItems().stream()
                    .forEach(t -> {
                        MaterialItem item = new MaterialItem();
                        item.setId(sequenceIdService.nextId());
                        item.setProduct(product);
                        item.setMaterialMasterId(m.getId());
                        item.setQuarter(quarter);
                        item.setQty(t.getQty());
                        item.setCreateBy(user);
                        item.setCreateDate(date);
                        //2019-06-03
                        item.setUseQty(item.getQty());
                        item.setRtvQty(0);
                        item.setRtvFlag(0);
                        long materialId = Long.valueOf(t.getMaterialId());
                        //返件标记及价格以客户设定优先
                        Material material = customerMaterialService.getMaterialInfoOfCustomer(cid, pid, materialId);
                        if (material == null) {
                            throw new OrderException("读取客户配件配置错误");
                        }
                        //get mateiral name
                        if (StringUtils.isBlank(material.getName())) {
                            Material prodMaterial = materialService.getFromCache(materialId);
                            if (prodMaterial == null) {
                                throw new OrderException("读取产品配件配置错误");
                            }
                            material.setName(prodMaterial.getName());
                        }
                        item.setMaterial(material);
                        item.setReturnFlag(material.getIsReturn());
                        //价格以输入为准
                        item.setPrice(t.getPrice());
                        //item.setPrice(customerMaterial.getPrice());//以客户设定为准
                        //有一个配件返件，单头标记为：需要返件
                        if (material.getIsReturn() == 1) {
                            m.setReturnFlag(material.getIsReturn());
                        }
                        //end
                        item.setTotalPrice(item.getPrice() * item.getQty());
                        totalPriceMap.replace("totalPrice", totalPriceMap.get("totalPrice") + item.getTotalPrice());
                        m.getItems().add(item);
                    });
            m.setTotalPrice(totalPriceMap.get("totalPrice"));//汇总价格
            m.setAttachments(attachments);
            //get orderDetaillId
            List<OrderDetail> details = order.getDetailList();
            if (details != null && details.size() > 0) {
                OrderDetail detail = details.stream().filter(t -> t.getProduct().getId().longValue() == m.getProduct().getId()).findFirst().orElse(null);
                if (detail == null) {
                    //套组拆分
                    List<Product> products = productService.getProductListOfSet(m.getProduct().getId());
                    if (products != null && products.size() > 0) {
                        detail = details.stream().filter(t -> products.contains(t.getProduct().getId())).findFirst().orElse(null);
                    }
                }
                if (detail != null) {
                    m.setOrderDetailId(detail.getId());
                }
            }
            orderMaterialService.addAppMaterialApplies(order, Lists.newArrayList(m));
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.saveAccessory] user:{} ,json:{}", userId, json, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest配件申请",
                        "POST",
                        json,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.saveAccessory] user:{}", userId, e);
            }
            return RestResultGenerator.exception("保存配件申请单失败");
        }
    }

    /**
     * CS2025
     * 保存返件申请
     */
    @RequestMapping(value = "accessory/saveReturn", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> saveReturnAccessory(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("file") MultipartFile[] files,
                                                  @RequestParam("json") String json) {
        String message = new String("");
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestSaveReturnAccessoryRequest orderRequest = GsonUtils.getInstance().fromJson(json, RestSaveReturnAccessoryRequest.class);
            if (orderRequest == null) {
                message = ErrorCode.WRONG_REQUEST_FORMAT.message;
            } else if (StringUtils.isBlank(orderRequest.getId())) {
                message = "返件申请单id为空";
            } else if (StringUtils.isBlank(orderRequest.getExpressCompany())) {
                message = "请选择快递公司";
            } else if (StringUtils.isBlank(orderRequest.getExpressNo())) {
                message = "请输入快递单号";
            }

            if (StringUtils.isNoneBlank(message)) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, message);
            }

            Date date = new Date();

            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Long id = Long.valueOf(orderRequest.getId());
            MaterialReturn returnForm = orderMaterialReturnService.getMaterialReturnById(id, null, orderRequest.getQuarter(), 0, 0, 0, 0);
            if (returnForm == null || returnForm.getId() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "返件申请单不存在，或读取失败，请重试");
            }
            long orderId = returnForm.getOrderId();
            String quarter = returnForm.getQuarter();
            Order order = orderService.getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取失败，请重试");
            }
            //save files.html
            List<MaterialAttachment> attachments = Lists.newArrayList();
            if (files != null && files.length > 0) {
                String savePath = new String();
                savePath = Global.getUploadfilesDir();
                StringBuffer sbPath = new StringBuffer(200);
                if (StringUtils.isBlank(savePath)) {
                    savePath = request.getServletContext().getRealPath("") + "/uploads/";
                }
                sbPath.append(DateUtils.getYear())
                        .append("/")
                        .append(DateUtils.getMonth())
                        .append("/")
                        .append(DateUtils.getDay())
                        .append("/");

                File f1 = new File(savePath + sbPath.toString());
                if (!f1.exists()) {
                    f1.mkdirs();
                }

                String name = new String();
                String extName = new String();
                String type = new String();
                long size;
                OrderAttachment attach;
                FileUpload uploadResult;

//                String host = Global.getConfig("userfiles.host") + "/";
                for (int i = 0, isize = files.length; i < isize; i++) {
                    MultipartFile file = files[i];
                    uploadResult = new FileUpload();
                    try {
                        name = file.getOriginalFilename();
                        uploadResult.setOrigalName(file.getOriginalFilename());
                        if (StringUtils.isBlank(name)) {
                            uploadResult.setStatus(1);
                        } else {
                            //扩展名格式：
                            if (name.lastIndexOf(".") >= 0) {
                                extName = name.substring(name.lastIndexOf("."));
                            }
                            name = UUID.randomUUID().toString() + extName;
                            file.transferTo(new File(savePath + sbPath.toString() + name));

                            MaterialAttachment attachment = new MaterialAttachment();
                            attachment.setId(sequenceIdService.nextId());
                            attachment.setOrderId(orderId);
                            attachment.setQuarter(quarter);
                            attachment.setFilePath(sbPath.toString() + name);
                            attachment.setCreateBy(user);
                            attachment.setCreateDate(date);
                            attachments.add(attachment);
                        }
                    } catch (IllegalStateException | IOException e) {
                        LogUtils.saveLog(
                                request,
                                null,
                                e,
                                "Rest配件申请",
                                "POST",
                                json,
                                new User(userId)
                        );
                        return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.code, ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.message + ":保存文件失败");
                    }
                }//end for
            }
            //save to db
            Dict expressCompany = MSDictUtils.getDictByValue(orderRequest.getExpressCompany(), "express_type");
            if (expressCompany != null) {
                returnForm.setExpressCompany(expressCompany);
            } else {
                returnForm.setExpressCompany(new Dict(orderRequest.getExpressCompany()));
            }
            returnForm.setExpressNo(StringUtils.left(orderRequest.getExpressNo(), 20));//长度不超过20
            returnForm.setUpdateBy(user);
            returnForm.setUpdateDate(date);
            returnForm.setAttachments(attachments);
            orderMaterialReturnService.updateMaterialReturnApplyExpressForApp(order, returnForm);

            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
        } catch (Exception e) {
            try {
                log.error("[RestOrderController.saveReturnAccessory] user:{} ,json:{}", userId, json, e);
                LogUtils.saveLog(
                        request,
                        null,
                        e,
                        "Rest返件",
                        "POST",
                        json,
                        new User(userId)
                );
            } catch (Exception e1) {
                log.error("[RestOrderController.saveReturnAccessory] user:{}", userId, e);
            }
            return RestResultGenerator.exception("保存配件申请单失败");
        }
    }

    /**
     * CS2007
     * 按id删除配件申请记录
     */
    @RequestMapping(value = "accessory/delete", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> deleteAccessory(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderBaseRequest orderRequest) {
        if (orderRequest == null || StringUtils.isBlank(orderRequest.getOrderId())
                || StringUtils.isBlank(orderRequest.getQuarter()) || StringUtils.isBlank(orderRequest.getId())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        Long orderId = null;
        try {
            orderId = Long.valueOf(orderRequest.getOrderId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }
        Long id = null;
        try {
            id = Long.valueOf(orderRequest.getId());
        } catch (Exception e) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message + "：类型错误");
        }

        Order order = orderService.getOrderById(orderId, orderRequest.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
        if (order == null || order.getItems() == null || order.getDataSource().getIntValue() == null || order.getItems().isEmpty()) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
        }

        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            MaterialMaster master = orderMaterialService.getMaterialMasterHeadById(id, orderRequest.getQuarter());
            if (master == null) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_DELCOMPONENT.code, "配件申请单不存在");
            }
            //配件单删除检查
            if (orderMaterialService.isOpenB2BMaterialSource(master.getDataSource())) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_DELCOMPONENT.code, "配件单已同步到商家系统，等待商家处理，不允许删除");
            }
            if (Integer.parseInt(master.getStatus().getValue()) > 1) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_DELCOMPONENT.code, "配件申请单处理中，不允许删除");
            }
            OrderCondition orderCondition = order.getOrderCondition();
            orderMaterialService.deleteMaterialAppy(orderId, orderRequest.getQuarter(), id,master.getMasterNo(), user,orderCondition.getStatus(),orderCondition.getCustomerId(),order.getDataSourceId());
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            try {
                String json = GsonUtils.toGsonString(orderRequest);
                log.error("[RestOrderController.deleteAccessory] user:{} ,json:{}", userId, json, e);
            } catch (Exception e1) {
                log.error("[RestOrderController.deleteAccessory] user:{}", userId, e);
            }
            return RestResultGenerator.exception("删除配件申请单失败");
        }
    }

    //endregion 配件

    //region 辅材

    /**
     * 获取产品的辅材或服务项目
     */
    @RequestMapping(value = "getProductAuxiliaryMaterial", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getProductAuxiliaryMaterial(HttpServletRequest request, @RequestBody RestGetProductAuxiliaryMaterialRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getItems() == null || order.getDataSource().getIntValue() == null || order.getItems().isEmpty()) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }

            List<Long> productIds = order.getItems().stream().map(OrderItem::getProductId).distinct().collect(Collectors.toList());
            RestProductAuxiliaryMaterials productAuxiliaryMaterials = appOrderService.getProductAuxiliaryMaterialsV2(productIds);
            return RestResultGenerator.success(productAuxiliaryMaterials);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("获取工单的辅材或服务项目失败");
        }
    }

    @RequestMapping(value = "saveOrderAuxiliaryMaterials", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> saveOrderAuxiliaryMaterials(HttpServletRequest request, @RequestBody RestSaveOrderAuxiliaryMaterialsRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            appOrderService.saveOrderAuxiliaryMaterialsV2(params, new User(userId));
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("保存工单的辅材或服务项目失败");
        }
    }
    @RequestMapping(value = "saveOrderAuxiliaryMaterialsV2", consumes = "multipart/form-data",  method = RequestMethod.POST)
    public RestResult<Object> saveOrderAuxiliaryMaterialsNew(HttpServletRequest request,@RequestParam("file") MultipartFile[] files, @RequestParam("json") String json) {
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestSaveOrderAuxiliaryMaterialsRequest params = GsonUtils.getInstance().fromJson(json, RestSaveOrderAuxiliaryMaterialsRequest.class);
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            String filePath = "";
            if (files!=null && files.length>0){
                TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFiles(request, files);
                if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().isEmpty()) {
                    throw new AttachmentSaveFailureException("保存收费清单图片附件失败");
                }
                filePath = saveFileResponse.getBElement().get(0);
            }
            appOrderService.saveOrderAuxiliaryMaterialsV3(params,filePath, new User(userId));
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("保存工单的辅材或服务项目失败");
        }
    }


    @RequestMapping(value = "saveOrderAuxiliaryMaterialsV3", consumes = "multipart/form-data",  method = RequestMethod.POST)
    public RestResult<Object> saveOrderAuxiliaryMaterialsV3(HttpServletRequest request,@RequestParam("file") MultipartFile[] files, @RequestParam("json") String json) {
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestSaveOrderAuxiliaryMaterialsRequest params = GsonUtils.getInstance().fromJson(json, RestSaveOrderAuxiliaryMaterialsRequest.class);
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (params.getProducts()!=null&&params.getProducts().size()>0&& params.getActualTotalCharge()!=null &&params.getActualTotalCharge()<=0){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code,"费用不能为0");
        }
        if (params.getProducts()!=null&&params.getProducts().size()>0&&params.getActualTotalCharge()!=null&&params.getActualTotalCharge()>0) {
            if (params.isUpdatePhoto()) {
                if (files.length <= 0) {
                    return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "没有上传辅材费用图片");
                }
            }
        }
        long userId = 0;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            userId = userInfo.getUserId();
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            String filePath = "";
            if (files!=null && files.length>0){
                TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFiles(request, files);
                if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().isEmpty()) {
                    throw new AttachmentSaveFailureException("保存收费清单图片附件失败");
                }
                filePath = saveFileResponse.getBElement().get(0);
            }
            appOrderService.saveOrderAuxiliaryMaterialsV3(params,filePath, new User(userId));
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("保存工单的辅材或服务项目失败");
        }
    }

    @RequestMapping(value = "getOrderAuxiliaryMaterials", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getOrderAuxiliaryMaterials(HttpServletRequest request, @RequestBody RestGetOrderAuxiliaryMaterialsRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getId() == null || order.getDataSource().getIntValue() == null || StringUtils.isBlank(order.getQuarter())) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            RestOrderAuxiliaryMaterials orderAuxiliaryMaterials = appOrderService.getOrderAuxiliaryMaterialsV2(order);
            return RestResultGenerator.success(orderAuxiliaryMaterials);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("获取工单的辅材或服务项目失败");
        }
    }

    @RequestMapping(value = "saveOrderAuxiliaryMaterialsV4", consumes = "multipart/form-data",  method = RequestMethod.POST)
    public RestResult<Object> saveOrderAuxiliaryMaterialsV4(HttpServletRequest request,@RequestParam("file") MultipartFile[] files, @RequestParam("json") String json) {
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestSaveOrderAuxiliaryMaterialsRequestV4 params = GsonUtils.getInstance().fromJson(json, RestSaveOrderAuxiliaryMaterialsRequestV4.class);
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())
                || params.getActualTotalCharge() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (params.getActualTotalCharge() <= 0){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code,"费用不能为0");
        }
        if (params.isUpdatePhoto()) {
            if (files.length <= 0) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "没有上传辅材费用图片");
            }
        }
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            String filePath = "";
            if (files!=null && files.length>0){
                TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFiles(request, files);
                if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().isEmpty()) {
                    throw new AttachmentSaveFailureException("保存收费清单图片失败");
                }
                filePath = saveFileResponse.getBElement().get(0);
            }
            appOrderService.saveOrderAuxiliaryMaterialsV4(params,filePath, new User(userInfo.getUserId()));
            return RestResultGenerator.success();
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("保存工单辅材收费失败");
        }
    }

    @RequestMapping(value = "getOrderAuxiliaryMaterialsV4", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getOrderAuxiliaryMaterialsV4(HttpServletRequest request, @RequestBody RestGetOrderAuxiliaryMaterialsRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0 || StringUtils.isBlank(params.getQuarter())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            RestOrderAuxiliaryMaterialsV4 orderAuxiliaryMaterials = appOrderService.getOrderAuxiliaryMaterialsV4(order);
            return RestResultGenerator.success(orderAuxiliaryMaterials);
        } catch (OrderException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("获取工单的辅材收费单失败");
        }
    }

    //endregion 辅材
}
