package com.wolfking.jeesite.modules.api.controller.receipt;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFeePraiseStandardItem;
import com.kkl.kklplus.entity.praise.PraisePicItem;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.AttachmentSaveFailureException;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.*;
import com.wolfking.jeesite.modules.api.service.sd.AppOrderPraiseNewService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * APP工单好评控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/receipt/praiseNew")
public class AppOrderPraiseNewController extends RestBaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AppOrderPraiseNewService orderPraiseNewService;

    private RestLoginUserInfo getUserInfo(HttpServletRequest request) {
        return RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
    }

    /**
     * CS9011
     * 获取工单好评信息
     */
    @RequestMapping(value = "getOrderPraiseInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getOrderPraiseInfo(HttpServletRequest request, @RequestBody AppGetOrderPraiseInfoRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = getUserInfo(request);
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            AppGetOrderPraiseDetailInfoResponse response = orderPraiseNewService.getOrderPraiseDetailInfo(order, userInfo.getServicePointId());
            return RestResultGenerator.success(response);
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderPraiseController.getOrderPraiseInfo] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取工单好评信息失败");
        }
    }

    /**
     * CS9012
     * 保存工单好评信息
     */
    @RequestMapping(value = "saveOrderPraiseInfo", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> saveOrderPraiseInfo(HttpServletRequest request,
                                                  @RequestParam("file") MultipartFile[] files,
                                                  @RequestParam("json") String json) {
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            AppSaveOrderPraiseInfoJsonParameterNewRequest params = GsonUtils.fromJsonNew(json, AppSaveOrderPraiseInfoJsonParameterNewRequest.class);
            if ((files == null || files.length == 0) && (params.getPics() == null || params.getPics().isEmpty())) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            if (params.getOrderId() == null || params.getOrderId() == 0) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            RestLoginUserInfo userInfo = getUserInfo(request);
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null || userInfo.getEngineerId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            User user = UserUtils.getAcount(userInfo.getUserId());
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (order == null || order.getOrderCondition() == null) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
            }
            //检查必须上传的好评图片是否都已上传
            List<String> uploadedPicCodes = params.getPics().stream().map(AppSaveOrderPraiseInfoJsonParameterNewRequest.UploadedPicItem::getCode).collect(Collectors.toList());
            uploadedPicCodes.addAll(params.getPicFileCodes());
            Map<String, MDCustomerPraiseFeePraiseStandardItem> praisePicItemStandardMap = orderPraiseNewService.getAppPraisePicItemStandard(order.getOrderCondition().getCustomerId());
            for (MDCustomerPraiseFeePraiseStandardItem standardItem : praisePicItemStandardMap.values()) {
                if (standardItem.getMustFlag() == 1 && !uploadedPicCodes.contains(standardItem.getCode())) {
                    throw new RuntimeException("创建好评单必须上传" + standardItem.getName());
                }
            }

            List<PraisePicItem> praisePicItems = Lists.newArrayList();
            MDCustomerPraiseFeePraiseStandardItem standardItem;
            PraisePicItem praisePicItem;
            if (params.getPics() != null && !params.getPics().isEmpty()) {
                for (AppSaveOrderPraiseInfoJsonParameterNewRequest.UploadedPicItem picItem : params.getPics()) {
                    standardItem = praisePicItemStandardMap.get(picItem.getCode());
                    if (standardItem != null) {
                        praisePicItem = new PraisePicItem();
                        praisePicItem.setCode(standardItem.getCode());
                        praisePicItem.setName(standardItem.getName());
                        praisePicItem.setUrl(StringUtils.replace(picItem.getUrl(), OrderPicUtils.getOrderPicHostDir(), ""));
                        praisePicItems.add(praisePicItem);
                    }
                }
            }
            if (files != null && files.length > 0) {
                TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFiles(request, files);
                if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().size() != params.getPicFileCodes().size()) {
                    throw new AttachmentSaveFailureException("保存好评图片失败");
                }
                for (int i = 0; i < params.getPicFileCodes().size(); i++) {
                    standardItem = praisePicItemStandardMap.get(params.getPicFileCodes().get(i));
                    if (standardItem != null) {
                        praisePicItem = new PraisePicItem();
                        praisePicItem.setCode(standardItem.getCode());
                        praisePicItem.setName(standardItem.getName());
                        praisePicItem.setUrl(saveFileResponse.getBElement().get(i));
                        praisePicItems.add(praisePicItem);
                    }
                }
            }
            orderPraiseNewService.saveOrderPraiseInfo(order, userInfo.getServicePointId(), userInfo.getEngineerId(),
                    praisePicItems, params.getCustomerApplyPraiseFee(), params.getServicePointApplyPraiseFee(), user);
            return RestResultGenerator.success();
        } catch (AttachmentSaveFailureException ae) {
            log.error("保存工单好评失败",ae);
            return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.code, ae.getMessage());
        } catch (OrderException oe) {
            log.error("保存工单好评失败",oe);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
        } catch (Exception e) {
            return RestResultGenerator.exception("保存工单好评失败");
        }
    }

    /**
     *
     */
    @RequestMapping(value = "saveOrderPraiseInfoV2", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> saveOrderPraiseInfoV2(HttpServletRequest request,
                                                  @RequestParam("file") MultipartFile[] files,
                                                  @RequestParam("json") String json) {
        if (StringUtils.isBlank(json)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
            try {
                AppSaveOrderPraiseInfoJsonParameterNewRequest params = GsonUtils.fromJsonNew(json, AppSaveOrderPraiseInfoJsonParameterNewRequest.class);

                if (params.getOrderId() == null || params.getOrderId() == 0) {
                    return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
                }
                RestLoginUserInfo userInfo = getUserInfo(request);
                if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null || userInfo.getEngineerId() <= 0) {
                    return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
                }
                User user = UserUtils.getAcount(userInfo.getUserId());
                if (null == user) {
                    return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
                }
                Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.DETAIL, true,true,true);
                if (order == null || order.getOrderCondition() == null) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "订单不存在，或读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
                }
                Dict completeTypeDict = MSDictUtils.getDictByValue(params.getCompleteType().trim(), "completed_type");//切换为微服务
                if (completeTypeDict == null) {
                    return RestResultGenerator.custom(ErrorCode.REQUEST_BODY_VALIDATE_FAIL.code, "系统中未定义此完成类型，请确认");
                }
                if (params.getPraiseType()==1) {
                    if ((files == null || files.length == 0) && (params.getPics() == null || params.getPics().isEmpty())) {
                        return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
                    }
                    //检查必须上传的好评图片是否都已上传
                    List<String> uploadedPicCodes = params.getPics().stream().map(AppSaveOrderPraiseInfoJsonParameterNewRequest.UploadedPicItem::getCode).collect(Collectors.toList());
                    uploadedPicCodes.addAll(params.getPicFileCodes());
                    Map<String, MDCustomerPraiseFeePraiseStandardItem> praisePicItemStandardMap = orderPraiseNewService.getAppPraisePicItemStandard(order.getOrderCondition().getCustomerId());
                    for (MDCustomerPraiseFeePraiseStandardItem standardItem : praisePicItemStandardMap.values()) {
                        if (standardItem.getMustFlag() == 1 && !uploadedPicCodes.contains(standardItem.getCode())) {
                            throw new RuntimeException("创建好评单必须上传" + standardItem.getName());
                        }
                    }

                    List<PraisePicItem> praisePicItems = Lists.newArrayList();
                    MDCustomerPraiseFeePraiseStandardItem standardItem;
                    PraisePicItem praisePicItem;
                    if (params.getPics() != null && !params.getPics().isEmpty()) {
                        for (AppSaveOrderPraiseInfoJsonParameterNewRequest.UploadedPicItem picItem : params.getPics()) {
                            standardItem = praisePicItemStandardMap.get(picItem.getCode());
                            if (standardItem != null) {
                                praisePicItem = new PraisePicItem();
                                praisePicItem.setCode(standardItem.getCode());
                                praisePicItem.setName(standardItem.getName());
                                praisePicItem.setUrl(StringUtils.replace(picItem.getUrl(), OrderPicUtils.getOrderPicHostDir(), ""));
                                praisePicItems.add(praisePicItem);
                            }
                        }
                    }
                    if (files != null && files.length > 0) {
                        TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFiles(request, files);
                        if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().size() != params.getPicFileCodes().size()) {
                            throw new AttachmentSaveFailureException("保存好评图片失败");
                        }
                        for (int i = 0; i < params.getPicFileCodes().size(); i++) {
                            standardItem = praisePicItemStandardMap.get(params.getPicFileCodes().get(i));
                            if (standardItem != null) {
                                praisePicItem = new PraisePicItem();
                                praisePicItem.setCode(standardItem.getCode());
                                praisePicItem.setName(standardItem.getName());
                                praisePicItem.setUrl(saveFileResponse.getBElement().get(i));
                                praisePicItems.add(praisePicItem);
                            }
                        }
                    }
                    orderPraiseNewService.saveOrderPraiseInfo(order, userInfo.getServicePointId(), userInfo.getEngineerId(),
                            praisePicItems, params.getCustomerApplyPraiseFee(), params.getServicePointApplyPraiseFee(), user);
                    orderPraiseNewService.updateCompleteInfo(order,completeTypeDict,user);
                    return RestResultGenerator.success();
                }else {
                    orderPraiseNewService.updateCompleteInfo(order,completeTypeDict,user);
                    return RestResultGenerator.success();
                }
            } catch (AttachmentSaveFailureException ae) {
                return RestResultGenerator.custom(ErrorCode.ORDER_CAN_NOT_SAVECOMPONENT.code, ae.getMessage());
            } catch (OrderException oe) {
                return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, oe.getMessage());
            } catch (Exception e) {
                return RestResultGenerator.exception("保存工单好评失败");
            }

    }

    /**
     * CS9013
     * 获取好评单列表
     */
    @RequestMapping(value = "getOrderPraiseList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getRejectedOrderPraiseList(HttpServletRequest request, @RequestBody AppGetOrderPraiseListRequest params) {
        if (params == null || params.getPageNo() == null || params.getPageNo() <= 0
                || params.getPageSize() == null || params.getPageSize() <= 0 || params.getStatus() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = getUserInfo(request);
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null || userInfo.getServicePointId() <= 0
                    || userInfo.getEngineerId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Page<AppGetOrderPraiseListItemNewResponse> response = orderPraiseNewService.getOrderPraiseList(userInfo.getServicePointId(),
                    userInfo.getEngineerId(), userInfo.getPrimary(), params.getStatus(),
                    params.getPageNo(), params.getPageSize());
            return RestResultGenerator.success(response);
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderPraiseController.getRejectedOrderPraiseList] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取好评单列表失败");
        }
    }

    /**
     * CS9014
     * 检查是否允许好评
     * 新增时间：2020-12-3
     */
    @RequestMapping(value = "checkPraiseCondition_v1", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> checkPraiseCondition_v1(HttpServletRequest request, @RequestBody AppCheckPraiseConditionRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() <= 0
                || StringUtils.isBlank(params.getQuarter()) || params.getDataSource() == null || params.getDataSource() <= 0) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = getUserInfo(request);
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null || userInfo.getServicePointId() <= 0
                    || userInfo.getEngineerId() <= 0) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            User user = UserUtils.getAcount(userInfo.getUserId());
            if (null == user) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (params.getDataSource() == B2BDataSourceEnum.USATON.id) {
                Order order = orderService.getOrderById(params.getOrderId(), params.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
                if (order == null || order.getOrderCondition() == null) {
                    return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "读取订单:" + ErrorCode.DATA_PROCESS_ERROR.message);
                }
                if (order.getDataSourceId() == B2BDataSourceEnum.USATON.id) {
                    NameValuePair<Boolean, String> result = orderPraiseNewService.checkPraiseCondition(order, user);
                    if (!result.getName()) {
                        return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, result.getValue());
                    }
                }
            }
            return RestResultGenerator.success();
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderPraiseController.checkPraiseCondition] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("检查是否允许上传好评照片失败");
        }
    }

}
