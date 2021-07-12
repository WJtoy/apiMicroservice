/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.controller.sd;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.Pair;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.md.RestRepairAction;
import com.wolfking.jeesite.modules.api.entity.sd.RestRepairInfo;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestDetailRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestGetRepairServiceTypesRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestOrderRepairBaseRequest;
import com.wolfking.jeesite.modules.api.service.sd.AppOrderRepairService;
import com.wolfking.jeesite.modules.api.service.sd.RestOrderService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.providermd.service.*;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * 上门服务
 *
 * @author Ryan
 * @version 2019-12-26
 */
@Slf4j
@RestController
@RequestMapping("/api/repair/")
public class RestRepairController extends RestBaseController {
    //id generator
    private static final Integer REPAIR_SERVICE_TYPE = 2;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private MSCustomerErrorTypeService msErrorTypeService;

    @Autowired
    private MSCustomerErrorCodeService msErrorCodeService;

    @Autowired
    private MSActionCodeService msActionCodeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestOrderService restOrderService;

    @Autowired
    private AppOrderRepairService appOrderRepairService;

    @Autowired
    private MapperFacade mapperFacade;

    //region 基础资料

    /**
     * 读取维修的服务项目列表
     * {
     * "orderTypeId":订单类型id,
     * "orderTypeName":"订单类型名称"
     * }
     */
    @RequestMapping(value = "/md/getServiceTypes", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getServiceTypesOfOrderServiceType(HttpServletRequest request, HttpServletResponse response) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            List<Pair<Long, String>> list = getRepairServiceTypes();
            if (list == null) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "未维护维修的服务项目");
            }
            return RestResultGenerator.success(list);
        } catch (Exception e) {
            try {
                log.error("异常， user:{} ", userInfo.getUserId(), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    @RequestMapping(value = "/md/getRepairServiceTypes", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getRepairServiceTypes(HttpServletRequest request, @RequestBody RestGetRepairServiceTypesRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }

            List<Pair<Long, String>> list = appOrderRepairService.getRepairServiceTypes(params.getOrderId(), params.getQuarter());
            if (list == null || list.isEmpty()) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "未维护维修的服务项目");
            }
            return RestResultGenerator.success(list);
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[RestRepairController.getRepairServiceTypes] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取维修的服务项目失败");
        }
    }

    /**
     * 根据产品id(单品)读取故障分类列表
     * {
     * "productId":产品id(单品),
     * "customerId":客户Id
     * }
     */
    @RequestMapping(value = "/md/getErrorTypes", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getErrorTypes(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody RestOrderRepairBaseRequest orderTypeRequest) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (orderTypeRequest == null || orderTypeRequest.getProductId() == null || orderTypeRequest.getProductId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            long customerId = orderTypeRequest.getCustomerId()==null?0:orderTypeRequest.getCustomerId();
            RestResult errorCodeResult = getErrorTypes(orderTypeRequest.getProductId(), StringUtils.toLong(customerId));
            return errorCodeResult;
        } catch (Exception e) {
            try {
                log.error("异常， user:{} ,productId:{}, customerId:{}", userInfo.getUserId(), orderTypeRequest.getProductId(),orderTypeRequest.getCustomerId(), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * 根据产品id(单品)+故障分类id读取故障现象列表
     * {
     * "productId":产品id(单品),
     * "errorTypeId",故障分类id
     * }
     */
    @RequestMapping(value = "/md/getErrorCodes", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getErrorCodes(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody RestOrderRepairBaseRequest orderTypeRequest) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (orderTypeRequest == null || orderTypeRequest.getProductId() == null || orderTypeRequest.getProductId() <= 0
                    || orderTypeRequest.getErrorTypeId() == null || orderTypeRequest.getErrorTypeId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            RestResult errorCodeResult = getErrorCodes(orderTypeRequest.getErrorTypeId(), orderTypeRequest.getProductId(),StringUtils.toLong(orderTypeRequest.getCustomerId()));
            return errorCodeResult;
        } catch (Exception e) {
            try {
                log.error("异常， user:{} ,productId:{} ,errorTypeId:{} customerId:{}", userInfo.getUserId(), orderTypeRequest.getProductId(), orderTypeRequest.getErrorTypeId(),orderTypeRequest.getCustomerId(), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * 根据产品id(单品)+故障现象id读取故障处理列表
     * {
     * "productId":产品id(单品),
     * "errorCodeId",故障现象id
     * }
     */
    @RequestMapping(value = "/md/getErrorActions", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getErrorActions(HttpServletRequest request, HttpServletResponse response,
                                              @RequestBody RestOrderRepairBaseRequest orderTypeRequest) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (orderTypeRequest == null || orderTypeRequest.getProductId() == null || orderTypeRequest.getProductId() <= 0
                    || orderTypeRequest.getErrorCodeId() == null || orderTypeRequest.getErrorCodeId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            List<MDActionCodeDto> actionCodes = null;
            try {
                actionCodes = msActionCodeService.findListByProductAndErrorCode(orderTypeRequest.getErrorCodeId(), orderTypeRequest.getProductId());
            } catch (Exception e) {
                log.error("读取故障处理错误，productId:{}", orderTypeRequest.getProductId(), e);
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "读取故障处理错误");
            }
            List<RestRepairAction> list = null;
            List<Pair<Long, String>> serviceList = null;
            if (!CollectionUtils.isEmpty(actionCodes)) {
                list = mapperFacade.mapAsList(actionCodes, RestRepairAction.class);
            } else {
                //装载维修类型的服务项目
                serviceList = getRepairServiceTypes();
                if (CollectionUtils.isEmpty(serviceList)) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取维修的服务项目错误，请重试");
                }
            }
            Map<String, Object> maps = Maps.newHashMapWithExpectedSize(2);
            maps.put("actions", list == null ? Lists.newArrayList() : list);
            maps.put("services", serviceList == null ? Lists.newArrayList() : serviceList);
            return RestResultGenerator.success(maps);
        } catch (Exception e) {
            try {
                log.error("异常， user:{} ,productId:{} ,errorCodeId:{}", userInfo.getUserId(), orderTypeRequest.getProductId(), orderTypeRequest.getErrorCodeId(), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    //endregion 基础资料

    //region 上门服务

    /**
     * 读取具体上门服务信息供修改维修信息
     * {
     * "id": 上门服务id,
     * "orderId": 订单id,
     * "quarter": 分片,
     * }
     */
    @RequestMapping(value = "getDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getDetail(HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody RestDetailRequest restRequest) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            //检查提交内容
            if (restRequest == null || StringUtils.isBlank(restRequest.getId())
                    || StringUtils.isBlank(restRequest.getOrderId()) || StringUtils.isBlank(restRequest.getQuarter())) {
                return RestResultGenerator.requestParameterError();
            }
            Long orderId = StringUtils.toLong(restRequest.getOrderId());
            Long detailId = StringUtils.toLong(restRequest.getId());
            if (orderId <= 0 || detailId <= 0) {
                return RestResultGenerator.requestParameterError();
            }
            List<OrderDetail> details = orderService.getOrderDetails(orderId, restRequest.getQuarter(), false);
            if (CollectionUtils.isEmpty(details)) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取实际服务内容失败，系统正在处理中，请稍后重试");
            }
            OrderDetail orderDetail = details.stream().filter(t -> t.getId().equals(detailId)).findFirst().orElse(null);
            if (orderDetail == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "请刷新工单页面，确认实际服务是否存在。");
            }
            if (orderDetail.getServiceCategory().getIntValue() != 2) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此实际服务不是维修类，不能维护维修故障。");
            }
            RestRepairInfo repairInfo = mapperFacade.map(orderDetail, RestRepairInfo.class);
            if (repairInfo == null || StringUtils.isBlank(repairInfo.getId())) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "数据处理：转换错误。");
            }
            long pid = Optional.ofNullable(orderDetail.getProduct()).map(t -> t.getId()).orElse(0L);
            if (pid <= 0) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取产品信息失败，请重试");
            }
            repairInfo.setQuarter(restRequest.getQuarter());
            //是否已经维护维修信息
            boolean hasRepaired = false;
            //无维修分类
            long errorTypeId = StringUtils.toLong(orderDetail.getErrorType().getId());
            if (errorTypeId > 0) {
                hasRepaired = true;
            } else if (StringUtils.isNotBlank(orderDetail.getOtherActionRemark())) {
                //有其他维修故障说明
                hasRepaired = true;
            }
            if (hasRepaired) {
                //未维护故障分类
                if (errorTypeId == 0) {
                    repairInfo.setHasErrorType(0);
                    //装载维修类型的服务项目
                    List<Pair<Long, String>> list = getRepairServiceTypes();
                    if (list == null) {
                        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取维修的服务项目错误，请重试");
                    }
                    repairInfo.setServiceTypes(list);
                } else {
                    repairInfo.setHasErrorType(1);
                }
            } else {
                Order order = orderService.getOrderById(orderId,restRequest.getQuarter(), OrderUtils.OrderDataLevel.CONDITION,true);
                if(order == null || order.getOrderCondition() == null){
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取订单信息时报，请稍后重试");
                }
                Long customerId = Optional.ofNullable(order.getOrderCondition().getCustomer()).map(t->t.getId()).orElse(0L);
                //判断是否维护故障分类
                try {
                    List<MDErrorType> errorTypes = msErrorTypeService.findListByProductIdAndCustomerIdFromCache(pid,customerId);
                    if (CollectionUtils.isEmpty(errorTypes)) {
                        repairInfo.setHasErrorType(0);
                        //装载维修类型的服务项目
                        List<Pair<Long, String>> list = getRepairServiceTypes();
                        if (list == null) {
                            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取维修的服务项目错误，请重试");
                        }
                        repairInfo.setServiceTypes(list);
                    } else {
                        repairInfo.setHasErrorType(1);
                    }
                } catch (Exception e) {
                    log.error("读取故障分类错误，productId:{}", pid, e);
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取故障分类错误");
                }
            }
            return RestResultGenerator.success(repairInfo);
        } catch (Exception e) {
            try {
                log.error("异常，data:{}", userInfo.getUserId(), GsonUtils.getInstance().toGson(restRequest), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    /**
     * 完善上门服务维修信息
     * {
     * "id": "上门服务id",
     * "orderId": "订单id",
     * "quarter": "分片",
     * "serviceTypeId":"服务项目id",
     * "serviceTypeName":"服务项目",
     * "errorType": {
     * "key":"故障分类id",
     * "value":"故障分类"
     * },
     * "errorCode": {
     * "key":"故障现象id",
     * "value":"故障现象"
     * },
     * "actionCode": {
     * "key":"故障处理id",
     * "value":"故障处理"
     * },
     * "otherActionRemark":"其他故障说明",
     * "remarks":"备注"
     * }
     */
    @RequestMapping(value = "updateDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> updateDetail(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody RestRepairInfo orderDetail) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            long userId = userInfo.getUserId();
            User user = UserUtils.getAcount(userId);
            RestResult checkResult = checkSubmitDetail(orderDetail);
            if (checkResult.getCode().intValue() != ErrorCode.NO_ERROR.code) {
                return checkResult;
            }
            OrderDetail detail = mapperFacade.map(orderDetail, OrderDetail.class);
            detail.setUpdateBy(user);
            detail.setUpdateDate(new Date());
            restOrderService.editRepair(detail);
            return RestResultGenerator.success();
        } catch (Exception e) {
            try {
                log.error("异常，data:{}", userInfo.getUserId(), GsonUtils.getInstance().toGson(orderDetail), e);
            } catch (Exception e1) {
                log.error("异常 user:{}", userInfo.getUserId(), e);
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }
    }

    //region 公共

    /**
     * 检查上门服务提交的内容是否合格
     */
    private RestResult checkSubmitDetail(RestRepairInfo detail) {
        if (detail == null || StringUtils.toLong(detail.getId()) <= 0
                || StringUtils.toLong(detail.getOrderId()) <= 0 || StringUtils.isBlank(detail.getQuarter())
        ) {
            return RestResultGenerator.requestParameterError();
        }
        if (StringUtils.toLong(detail.getServiceTypeId()) <= 0) {
            return RestResultGenerator.requestParameterError("选择的服务项目错误");
        }
        //选择了故障分类
        long id;
        if (StringUtils.isNotBlank(detail.getErrorType().getKey()) && Long.valueOf(detail.getErrorType().getKey()) > 0) {
            if (StringUtils.isBlank(detail.getErrorType().getValue())) {
                return RestResultGenerator.requestParameterError("故障分类名称无内容");
            }
            id = StringUtils.toLong(detail.getErrorCode().getKey());
            if (id <= 0) {
                return RestResultGenerator.requestParameterError("请选择故障现象");
            }
            if (StringUtils.isBlank(detail.getErrorCode().getValue())) {
                return RestResultGenerator.requestParameterError("故障现象名称无内容");
            }
            if (StringUtils.isBlank(detail.getActionCode().getValue())) {
                return RestResultGenerator.requestParameterError("请选择或输入故障处理内容");
            }
        } else if (StringUtils.isBlank(detail.getOtherActionRemark())) {
            return RestResultGenerator.requestParameterError("请输入其他故障维修说明");
        }

        return RestResultGenerator.success();
    }

    private List<Pair<Long, String>> getRepairServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.findListOfOrderType(REPAIR_SERVICE_TYPE);
        if (CollectionUtils.isEmpty(serviceTypes)) {
            return null;
        }
        int size = serviceTypes.size();
        List<Pair<Long, String>> list = Lists.newArrayListWithCapacity(size);
        ServiceType serviceType;
        for (int i = 0; i < size; i++) {
            serviceType = serviceTypes.get(i);
            list.add(new Pair<>(serviceType.getId(), serviceType.getName()));
        }
        return list;
    }

    private RestResult getErrorTypes(Long productId,Long customerId) {
        List<MDErrorType> errorTypes = null;
        try {
            errorTypes = msErrorTypeService.findErrorTypesByProductId(customerId,productId);
        } catch (Exception e) {
            log.error("读取故障分类错误，productId:{}", productId, e);
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "读取故障分类错误");
        }
        if (CollectionUtils.isEmpty(errorTypes)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "请确认该产品是否维护了故障分类.");
        }
        int size = errorTypes.size();
        List<Pair<Long, String>> list = Lists.newArrayListWithCapacity(size);
        MDErrorType errorType;
        for (int i = 0; i < size; i++) {
            errorType = errorTypes.get(i);
            list.add(new Pair<>(errorType.getId(), errorType.getName()));
        }
        return RestResultGenerator.success(list);
    }


    private RestResult getErrorCodes(Long errorTypeId, Long productId,Long customerId) {
        List<MDErrorCode> errorCodes = null;
        try {
            errorCodes = msErrorCodeService.findListByProductAndErrorType(errorTypeId, productId,customerId);
        } catch (Exception e) {
            log.error("读取故障现象错误，productId:{}", productId, e);
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "读取故障现象错误");
        }
        if (CollectionUtils.isEmpty(errorCodes)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, "请确认该产品是否维护了故障现象.");
        }

        int size = errorCodes.size();
        List<Pair<String, String>> list = Lists.newArrayListWithCapacity(size);
        MDErrorCode errorCode;
        for (int i = 0; i < size; i++) {
            errorCode = errorCodes.get(i);
            list.add(new Pair<String, String>(errorCode.getId().toString(), errorCode.getName()));
        }
        return RestResultGenerator.success(list);
    }

    //endregion

    //endregion 上门服务
}
