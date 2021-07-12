package com.wolfking.jeesite.ms.supor.sd.service;

import cn.hutool.core.util.StrUtil;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderProcessLogMessage;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderStatusUpdateMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderActionEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderStatusEnum;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.supor.sd.SuporOrderProcess;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderProcessLogReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.supor.sd.feign.SuporOrderFeign;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class SuporOrderService extends B2BOrderManualBaseService {

    @Autowired
    private SuporOrderFeign suporOrderFeign;
    @Resource
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderItemCompleteService orderItemCompleteService;
    @Autowired
    private OrderCacheReadService orderCacheReadService;


    /**
     * 取消工单转换
     */
    public MSResponse cancelOrderTransition(B2BOrderTransferResult b2BOrderTransferResult) {
        return suporOrderFeign.cancelOrderTransition(b2BOrderTransferResult);
    }


    //region 工单状态检查


    /**
     * 批量检查工单是否可转换
     */
    public MSResponse checkB2BOrderProcessFlag(List<B2BOrderTransferResult> b2bOrderNos) {
        return suporOrderFeign.checkWorkcardProcessFlag(b2bOrderNos);
    }

    //endregion 工单状态检查


    //region 更新工单转换状态

    /**
     * 调用同望微服务的B2B工单转换进度更新接口
     */
    public MSResponse sendB2BOrderConversionProgressUpdateCommandToB2B(List<B2BOrderTransferResult> progressList) {
        return suporOrderFeign.updateTransferResult(Lists.newArrayList(progressList));
    }

    //endregion 更新工单转换状态


    //region 康宝工单状态变更

    /**
     * 往微服务发送工单状态更新命令
     */
    public MSResponse sendOrderStatusUpdateCommandToB2B(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        MSResponse response = null;
        if (message.getStatus() == B2BOrderStatusEnum.PLANNED.value) {
            response = orderPlanned(message);
        } else if (message.getStatus() == B2BOrderStatusEnum.APPOINTED.value) {
            response = orderAppointed(message);
        } else if (message.getStatus() == B2BOrderStatusEnum.CANCELED.value) {
            response = orderCancelled(message);
        } else {
            response = new MSResponse(MSErrorCode.SUCCESS);
            String msgJson = new JsonFormat().printToString(message);
            LogUtils.saveLog("B2B工单状态变更消息的状态错误", "SuporOrderService.sendOrderStatusUpdateCommandToB2B", msgJson, null, null);
        }
        return response;
    }

    /**
     * 派单
     */
    private MSResponse orderPlanned(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        SuporOrderProcess process = new SuporOrderProcess();
        process.setUniqueId(message.getMessageId());
        process.setB2bOrderId(message.getB2BOrderId());
        process.setCreateById(message.getUpdaterId());
        process.setCreateDt(message.getUpdateDt());

        process.setPersonName(message.getEngineerName());
        process.setWorkerPhone(message.getEngineerMobile());
        return suporOrderFeign.plan(process);
    }

    /**
     * 预约
     */
    private MSResponse orderAppointed(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        SuporOrderProcess process = new SuporOrderProcess();
        process.setUniqueId(message.getMessageId());
        process.setB2bOrderId(message.getB2BOrderId());
        process.setCreateById(message.getUpdaterId());
        process.setCreateDt(message.getUpdateDt());

        process.setPreServiceTime(message.getEffectiveDt());
        if (message.getKklPendingType().equals(Order.PENDINGTYPE_WAITINGPARTS.toString())) {
            process.setChangeReason(2);
        } else {
            process.setChangeReason(1);
        }
        return suporOrderFeign.appoint(process);
    }

    /**
     * 取消
     */
    private MSResponse orderCancelled(MQB2BOrderStatusUpdateMessage.B2BOrderStatusUpdateMessage message) {
        SuporOrderProcess process = new SuporOrderProcess();
        process.setUniqueId(message.getMessageId());
        process.setB2bOrderId(message.getB2BOrderId());
        process.setCreateById(message.getUpdaterId());
        process.setCreateDt(message.getUpdateDt());

        process.setCancelReason(StringUtils.toInteger(message.getB2BReason()));
        process.setCancelDes(message.getRemarks());
        return suporOrderFeign.cancel(process);
    }

    /**
     * 处理B2B发来的处理指令
     * 1.取消工单
     *
     * @param processEntity
     * @return
     */
    public MSResponse processKKLOrder(B2BOrderProcessEntity processEntity) {
        boolean flag = false;
        try {
            if (processEntity.getActionType() == B2BOrderActionEnum.CONVERTED_CANCEL) {
                flag = cancelKKLOrder(processEntity);
            }
        } catch (Exception e) {
            B2BOrderProcessEntity.saveFailureLog(processEntity, "修改快可立工单", "MBOOrderService.processKKLOrder", e);
        }
        return new MSResponse(flag ? MSErrorCode.SUCCESS : MSErrorCode.FAILURE);
    }

    /**
     * 收到拼多多指令后，直接取消系统工单
     * 1.未派单，取消
     * 2.已退单申请，审核退单
     * 3.其他状态，b2b取消订单
     */
    private boolean cancelKKLOrder(B2BOrderProcessEntity entity) {
        boolean result = false;
        Order order = orderCacheReadService.getOrderById(entity.getKklOrderId(), null, OrderUtils.OrderDataLevel.CONDITION, true, true);
        if (order != null && order.getOrderCondition() != null) {
            String remarks = StringUtils.toString(entity.getRemarks()) + "（" + order.getDataSource().getLabel() + "平台通知取消B2B工单）";
//            if (order.getOrderCondition().getStatusValue() <= Order.ORDER_STATUS_ACCEPTED) {
//                orderService.cancelOrderNew(order.getId(), B2BOrderVModel.b2bUser, remarks, false);
//                result = true;
//            } else if (order.getOrderCondition().getStatusValue() == Order.ORDER_STATUS_RETURNING) { //当前是退单审核状态，则直接退单审核
//                orderService.approveReturnOrderNew(order.getId(), order.getQuarter(), remarks, B2BOrderVModel.b2bUser);
//                result = true;
//            } else {
//                orderService.b2bCancelOrder(order.getId(), order.getQuarter(), new Dict("51", "厂家(电商)通知取消"), remarks, B2BOrderVModel.b2bUser);
//                result = true;
//            }
        } else {
            B2BOrderProcessEntity.saveFailureLog(entity, "读取工单失败", "MBOOrderService.cancelKKLOrder", null);
        }
        return result;
    }

    //endregion region 工单状态变更


    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 创建派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createPlanRequestEntity(String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        B2BOrderStatusUpdateReqEntity.Builder builder;
        if (StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createAppointRequestEntity(Date appointmentDate, Integer pendingType) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && pendingType != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate)
                    .setPendingType(pendingType.toString());
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCancelRequestEntity(Integer kklCancelType) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        String cancelResponsible = null;
        if (kklCancelType != null) {
            cancelResponsible = MSDictUtils.getDictLabel(kklCancelType.toString(), Dict.DICT_TYPE_CANCEL_RESPONSIBLE, "");
            Integer cancelReason = B2BMDUtils.getSuporCancelType(kklCancelType);
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setB2bReason(cancelReason == null ? "0" : cancelReason.toString())
                    .setRemarks(StrUtil.trimToEmpty(cancelResponsible));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

}
