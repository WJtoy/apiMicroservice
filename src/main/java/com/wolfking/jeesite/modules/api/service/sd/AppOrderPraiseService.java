/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.service.sd;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFee;
import com.kkl.kklplus.entity.praise.*;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.common.utils.CurrencyUtil;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppGetOrderPraiseInfoResponse;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppGetOrderPraiseListItemResponse;
import com.wolfking.jeesite.modules.mq.service.ServicePointOrderBusinessService;
import com.wolfking.jeesite.modules.sd.dao.AppOrderDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.service.OrderStatusFlagService;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.modules.sys.utils.SeqUtils;
import com.wolfking.jeesite.ms.praise.entity.ViewPraiseModel;
import com.wolfking.jeesite.ms.praise.feign.AppPraiseFeign;
import com.wolfking.jeesite.ms.praise.feign.OrderPraiseFeign;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerPraiseFeeService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * App工单好评
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppOrderPraiseService extends LongIDBaseService {
    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private MSCustomerPraiseFeeService customerPraiseFeeService;

    @Autowired
    private OrderPraiseFeign orderPraiseFeign;

    @Autowired
    private AreaService areaService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatusFlagService orderStatusFlagService;

    @Autowired
    private ServicePointOrderBusinessService servicePointOrderBusinessService;

    @Resource
    private AppOrderDao appOrderDao;

    @Autowired
    private AppPraiseFeign appPraiseFeign;

    @Autowired
    private SequenceIdService sequenceIdService;

    /**
     * 获取客户好评费用标准
     */
    public MDCustomerPraiseFee getCustomerPraiseFee(Long customerId) {
        MDCustomerPraiseFee customerPraiseFee = customerPraiseFeeService.getByCustomerIdFromCacheForCP(customerId);
        if (customerPraiseFee == null) {
            customerPraiseFee = new MDCustomerPraiseFee();
            customerPraiseFee.setPraiseFee(0.0);
            customerPraiseFee.setPicCount(1);
            customerPraiseFee.setDiscount(0.0);
            customerPraiseFee.setPraiseRequirement("");
        }
        return customerPraiseFee;
    }


    /**
     * 获取好评单信息
     */
    public AppGetOrderPraiseInfoResponse getOrderPraiseInfo(Order order, Long servicePointId) {
        AppGetOrderPraiseInfoResponse response = new AppGetOrderPraiseInfoResponse();
        Praise praise = getPraise(order.getId(), order.getQuarter(), servicePointId);
        MDCustomerPraiseFee customerPraiseFee = getCustomerPraiseFee(order.getOrderCondition().getCustomerId());
        response.setPicCount(customerPraiseFee.getPicCount());
        response.setPraiseRequirement(customerPraiseFee.getPraiseRequirement());
        if (praise != null) {
            PraiseStatusEnum statusEnum = PraiseStatusEnum.fromCode(praise.getStatus());
            response.setPraiseStatus(new AppDict(String.valueOf(statusEnum.code), statusEnum.msg));
            if (praise.getStatus() == PraiseStatusEnum.APPROVE.code) {
                response.setRejectionCategory(new AppDict("0", "有效好评"));
            } else {
                int rejectionCategory = praise.getRejectionCategory() == null ? 0 : praise.getRejectionCategory();
                Dict praiseAbnormalDict = MSDictUtils.getDictByValue(String.valueOf(rejectionCategory), "praise_abnormal_type");
                if (praiseAbnormalDict != null) {
                    response.setRejectionCategory(new AppDict(praiseAbnormalDict.getValue(), praiseAbnormalDict.getLabel()));
                }
                response.setRemarks(StringUtils.toString(praise.getRemarks()));
            }
            response.setPraiseFee(praise.getServicepointPraiseFee());
            List<AppGetOrderPraiseInfoResponse.PicItem> picItems = Lists.newArrayList();
            if (praise.getPics() != null && !praise.getPics().isEmpty()) {
                for (String picFilePath : praise.getPics()) {
                    if (StringUtils.isNotBlank(picFilePath)) {
                        picItems.add(new AppGetOrderPraiseInfoResponse.PicItem(OrderPicUtils.getOrderPicHostDir() + picFilePath));
                    }
                }
            }
            response.setPics(picItems);
        } else {
            response.setPraiseStatus(new AppDict(String.valueOf(PraiseStatusEnum.NONE.code), PraiseStatusEnum.NONE.msg));
            response.setPraiseFee(calcServicePointPraiseFee(customerPraiseFee.getPraiseFee(), customerPraiseFee.getDiscount()));
        }
        return response;
    }

    /**
     * 保存APP好评单
     */
    @Transactional()
    public void saveOrderPraiseInfo(Order order, Long servicePointId, Long engineerId, List<String> praisePicUrls, User user) {
        String lockKey = String.format(PraiseConstrant.LOCK_PRAISE_WRITE_OPERATION, order.getId());
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, PraiseConstrant.LOCK_EXPIRED_PRAISE_WRITE_OPERATION);
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新页面。");
        }
        Praise praise = getPraise(order.getId(), order.getQuarter(), servicePointId);
        if (praise != null && praise.getStatus() != PraiseStatusEnum.NEW.code && praise.getStatus() != PraiseStatusEnum.REJECT.code) {
            throw new OrderException("此好评单当前不允许修改");
        }
        Date now = new Date();
        try {
            if (praise == null) {
                createOrderPraise(order, servicePointId, engineerId, praisePicUrls, user, now);
            } else {
                updateOrderPraise(praise, praisePicUrls, user, now);
            }
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

    /**
     * 取消好评单
     */
    @Transactional()
    public void cancelOrderPraise(Order order, Long servicePointId, User user) {
        String lockKey = String.format(PraiseConstrant.LOCK_PRAISE_WRITE_OPERATION, order.getId());
        boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, PraiseConstrant.LOCK_EXPIRED_PRAISE_WRITE_OPERATION);
        if (!locked) {
            throw new RuntimeException("此好评单正在处理中，请稍候重试");
        }
        Praise praise = getPraise(order.getId(), order.getQuarter(), servicePointId);
        if (praise == null) {
            throw new RuntimeException("读取好评单失败，请稍候重试");
        }
        if (praise.getStatus() != PraiseStatusEnum.NEW.code && praise.getStatus() != PraiseStatusEnum.REJECT.code) {
            throw new OrderException("此好评单当前不允许取消");
        }
        try {
            Date now = new Date();
            praise.setStatus(PraiseStatusEnum.CANCELED.code);
            praise.setRemarks("师傅使用APP[取消]好评单");
            praise.setUpdateById(user.getId());
            praise.setUpdateBy(user.getName());
            praise.setUpdateDt(now.getTime());

            PraiseLog praiseLog = new PraiseLog();
            praiseLog.setId(sequenceIdService.nextId());//2020/05/25
            praiseLog.setStatus(praise.getStatus());
            praiseLog.setActionType(PraiseActionEnum.REJECT_TO_CANCELED.code);
            praiseLog.setVisibilityFlag(ViewPraiseModel.VISIBILITY_FLAG_ALL);
            praiseLog.setContent("师傅使用APP[取消]好评单");
            praiseLog.setCreatorType(PraiseCreatorTypeEnum.SERVICE_POINT.code);
            praise.setPraiseLog(praiseLog);
            MSResponse<Integer> msResponse = orderPraiseFeign.cancelled(praise);
            if (!MSResponse.isSuccessCode(msResponse)) {
                throw new RuntimeException("取消好评单失败:" + msResponse.getMsg());
            }
            try {
                servicePointOrderBusinessService.syncPraiseStatus(praise.getOrderId(), praise.getQuarter(), praise.getServicepointId(),
                        praise.getStatus(), user.getId(), now.getTime());
            } catch (Exception e) {
                log.error("发送消息队列更新好评单状态失败 form: {}", GsonUtils.toGsonString(praise), e);
            }
        } finally {
            if (lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

    /**
     * 获取驳回的好评单列表
     */
    public Page<AppGetOrderPraiseListItemResponse> getRejectedOrderPraiseList(Long servicePointId, Long engineerId, Boolean isPrimaryAccount, Integer pageNo, Integer pageSize) {
        Page<AppGetOrderPraiseListItemResponse> returnPage = new Page<>(pageNo, pageSize);
        List<AppGetOrderPraiseListItemResponse> itemList = Lists.newArrayList();
        PraisePageSearchModel searchModel = new PraisePageSearchModel();
        searchModel.setPage(new MSPage<>(pageNo, pageSize));
        searchModel.setServicepointId(servicePointId);
        searchModel.setEngineerId(isPrimaryAccount ? null : engineerId);
        MSResponse<MSPage<PraiseAppListModel>> msResponse = appPraiseFeign.rejectAppList(searchModel);
        if (MSResponse.isSuccess(msResponse)) {
            List<PraiseAppListModel> models = msResponse.getData().getList();
            if (models != null && !models.isEmpty()) {
                Map<String, Dict> praiseAbnormalTypeMap = MSDictUtils.getDictMap("praise_abnormal_type");
                AppGetOrderPraiseListItemResponse item;
                Dict praiseAbnormalDict;
                for (PraiseAppListModel model : models) {
                    item = new AppGetOrderPraiseListItemResponse();
                    item.setPraiseId(model.getId());
                    PraiseStatusEnum statusEnum = PraiseStatusEnum.fromCode(model.getStatus());
                    item.setPraiseStatus(new AppDict(String.valueOf(statusEnum.code), statusEnum.msg));
                    item.setOrderNo(model.getOrderNo());
                    item.setDataSource(model.getDataSource());
                    item.setUserName(model.getUserName());
                    item.setServicePhone(model.getUserPhone());
                    int rejectionCategory = model.getRejectionCategory() == null ? 0 : model.getRejectionCategory();
                    praiseAbnormalDict = praiseAbnormalTypeMap.get(String.valueOf(rejectionCategory));
                    if (praiseAbnormalDict != null) {
                        item.setRejectionCategory(new AppDict(praiseAbnormalDict.getValue(), praiseAbnormalDict.getLabel()));
                    }
                    item.setRemarks(StringUtils.toString(model.getRemarks()));
                    for (String picFilePath : model.getPics()) {
                        if (StringUtils.isNotBlank(picFilePath)) {
                            item.getPics().add(new AppGetOrderPraiseListItemResponse.PicItem(OrderPicUtils.getOrderPicHostDir() + picFilePath));
                        }
                    }
                    itemList.add(item);
                }
            }
        }
        returnPage.setList(itemList);
        return returnPage;
    }

    /**
     * 创建工单好评单
     */
    private void createOrderPraise(Order order, Long servicePointId, Long engineerId, List<String> picUrls, User user, Date createDate) {
        String praiseNo = SeqUtils.NextSequenceNo("praiseNo", 0, 3);
        if (StringUtils.isBlank(praiseNo)) {
            throw new RuntimeException("生成好评单号失败");
        }
        OrderCondition condition = order.getOrderCondition();
        List<String> productNames = order.getItems().stream().map(t -> t.getProduct().getName()).distinct().collect(Collectors.toList());
        MDCustomerPraiseFee customerPraiseFee = getCustomerPraiseFee(condition.getCustomerId());

        Praise praise = new Praise();
        praise.setId(sequenceIdService.nextId());//2020/05/25
        praise.setOrderId(order.getId());
        praise.setOrderNo(order.getOrderNo());
        praise.setQuarter(order.getQuarter());
        praise.setProductNames(StringUtils.join(productNames, ","));
        praise.setProductCategoryId(condition.getProductCategoryId());
        praise.setDataSource(order.getDataSourceId());
        praise.setWorkcardId(order.getWorkCardId());
        praise.setParentBizOrderId(order.getParentBizOrderId());
        praise.setAreaId(condition.getArea().getId());
        praise.setSubAreaId(condition.getSubArea().getId());
        Area area = areaService.getFromCache(condition.getArea().getId());
        if (area != null) {
            List<String> ids = Splitter.onPattern(",")
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(area.getParentIds());
            if (ids.size() >= 2) {
                praise.setCityId(Long.valueOf(ids.get(ids.size() - 1)));
                praise.setProvinceId(Long.valueOf(ids.get(ids.size() - 2)));
            }
        }
        praise.setCustomerId(condition.getCustomer().getId());
        praise.setServicepointId(servicePointId);
        praise.setEngineerId(engineerId);
        praise.setKefuId(condition.getKefu() != null && condition.getKefu().getId() != null ? condition.getKefu().getId() : 0);
        praise.setUserName(condition.getUserName());
        praise.setUserPhone(condition.getServicePhone());
        praise.setUserAddress(condition.getArea().getName() + condition.getServiceAddress());

        praise.setPraiseNo(praiseNo);
        praise.setStatus(PraiseStatusEnum.NEW.code);
        praise.setCustomerPraiseFee(customerPraiseFee.getPraiseFee());
        praise.setServicepointPraiseFee(calcServicePointPraiseFee(customerPraiseFee.getPraiseFee(), customerPraiseFee.getDiscount()));
        praise.setPicsJson(GsonUtils.toGsonString(picUrls));
        praise.setRemarks("");
        praise.setCreateById(user.getId());
        praise.setCreateDt(createDate.getTime());
        praise.setCanRush(condition.getCanRush());
        praise.setKefuType(condition.getKefuType());

        PraiseLog praiseLog = new PraiseLog();
        praiseLog.setId(sequenceIdService.nextId());//2020/05/25
        praiseLog.setStatus(PraiseStatusEnum.NEW.code);
        praiseLog.setActionType(PraiseStatusEnum.NEW.code);
        praiseLog.setCreatorType(PraiseCreatorTypeEnum.SERVICE_POINT.code);
        praiseLog.setVisibilityFlag(ViewPraiseModel.VISIBILITY_FLAG_ALL);
        praiseLog.setContent("[新建]好评单");

        praise.setPraiseLog(praiseLog);
        MSResponse<Praise> msResponse = orderPraiseFeign.saveApplyPraise(praise);
        if (!MSResponse.isSuccess(msResponse)) {
            throw new RuntimeException("创建好评单失败失败:" + msResponse.getMsg());
        }

        orderStatusFlagService.updatePraiseStatus(order.getId(), order.getQuarter(), PraiseStatusEnum.NEW.code);

        OrderProcessLog processLog = new OrderProcessLog();
        processLog.setOrderId(order.getId());
        processLog.setQuarter(order.getQuarter());
        processLog.setAction("新建好评单");
        processLog.setActionComment("APP创建好评单：" + praiseNo);
        processLog.setStatus(condition.getStatus().getLabel());
        processLog.setStatusValue(condition.getStatusValue());
        processLog.setStatusFlag(OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS);
        processLog.setCloseFlag(0);
        processLog.setCreateBy(user);
        processLog.setCreateDate(createDate);
        processLog.setCustomerId(condition.getCustomerId());
        processLog.setDataSourceId(order.getDataSourceId());
        processLog.setVisibilityFlag(ViewPraiseModel.VISIBILITY_FLAG_ALL);
        orderService.saveOrderProcessLogWithNoCalcVisibility(processLog);
        try {
            servicePointOrderBusinessService.syncPraiseStatus(praise.getOrderId(), praise.getQuarter(), praise.getServicepointId(),
                    praise.getStatus(), user.getId(), createDate.getTime());
        } catch (Exception e) {
            log.error("发送消息队列更新好评单状态失败 form: {}", GsonUtils.toGsonString(praise), e);
        }
    }

    /**
     * 修改工单好评单
     */
    private void updateOrderPraise(Praise praise, List<String> picUrls, User user, Date updateDate) {
        int status;
        int action;
        String logContent;
        if (praise.getStatus() == PraiseStatusEnum.REJECT.code) {
            status = PraiseStatusEnum.PENDING_REVIEW.code;
            action = PraiseActionEnum.RESUBMIT.code;
            logContent = "修改被[驳回]的好评单";
        } else {
            status = praise.getStatus();
            action = PraiseActionEnum.UPDATE_PIC.code;
            logContent = "修改好评单";
        }

        praise.setPicsJson(GsonUtils.toGsonString(picUrls));
        praise.setStatus(status);
        praise.setUpdateById(user.getId());
        praise.setUpdateBy(user.getName());
        praise.setUpdateDt(updateDate.getTime());

        PraiseLog praiseLog = new PraiseLog();
        praiseLog.setId(sequenceIdService.nextId());//2020/05/25
        praiseLog.setStatus(status);
        praiseLog.setActionType(action);
        praiseLog.setCreatorType(PraiseCreatorTypeEnum.SERVICE_POINT.code);
        praiseLog.setVisibilityFlag(ViewPraiseModel.VISIBILITY_FLAG_ALL);
        praiseLog.setContent(logContent);

        praise.setPraiseLog(praiseLog);
        MSResponse<Integer> msResponse = orderPraiseFeign.resubmit(praise);
        if (!MSResponse.isSuccessCode(msResponse)) {
            throw new RuntimeException("修改好评单失败:" + msResponse.getMsg());
        }
        if (status == PraiseStatusEnum.PENDING_REVIEW.code) {
            try {
                servicePointOrderBusinessService.syncPraiseStatus(praise.getOrderId(), praise.getQuarter(), praise.getServicepointId(),
                        praise.getStatus(), user.getId(), updateDate.getTime());
            } catch (Exception e) {
                log.error("发送消息队列更新好评单状态失败 form: {}", GsonUtils.toGsonString(praise), e);
            }
        }
    }

    /**
     * 计算网点的好评费
     *
     * @param customerPraiseFee 客户好评费
     * @param discount          扣点
     */
    private double calcServicePointPraiseFee(Double customerPraiseFee, Double discount) {
        double servicePointPraiseFee = 0;
        if (customerPraiseFee != null && customerPraiseFee > 0) {
            if (discount > 0) {
                servicePointPraiseFee = customerPraiseFee - CurrencyUtil.round2(customerPraiseFee * discount);
            } else {
                servicePointPraiseFee = customerPraiseFee;
            }
        }
        return servicePointPraiseFee;
    }

    /**
     * 根据订单Id获取工单好评信息
     */
    private Praise getPraise(Long orderId, String quarter, Long servicePointId) {
        Praise praise = null;
        MSResponse<Praise> msResponse = orderPraiseFeign.getByOrderIdAndServicepointId(quarter, orderId, servicePointId);
        if (MSResponse.isSuccess(msResponse)) {
            praise = msResponse.getData();
        }

        return praise;
    }
}
