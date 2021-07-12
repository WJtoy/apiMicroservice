package com.wolfking.jeesite.modules.mq.service;

import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.modules.mq.dto.MQOrderServicePointMessage;
import com.wolfking.jeesite.modules.mq.sender.OrderServicePointMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 网点订单业务实现层
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ServicePointOrderBusinessService {

    @Autowired
    private OrderServicePointMessageSender sender;
    @Autowired
    private SequenceIdService sequenceIdService;


    //region 生产方法

    /**
     * 派单/抢单
     *
     * @param builder
     */
    public void planOrder(MQOrderServicePointMessage.ServicePointMessage.Builder builder) {
        MQOrderServicePointMessage.ServicePointMessage message = builder
                .setId(sequenceIdService.nextId())
                .setOperationType(MQOrderServicePointMessage.OperationType.Create)
                .build();
        sender.sendRetry(message, 0, 0);
    }


    /**
     * 网点派单/App
     */
    public void changeEngineer(long orderId, String quarter, long servicePointId, long engineerId, int masterFlag, long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.ChangeEngineer)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .setMasterFlag(masterFlag)
                .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                        .setServicePointId(servicePointId)
                        .setEngineerId(engineerId)
                        .build())
                .build();
        sender.sendRetry(message, 0, 0);
    }

    /**
     * 预约时间
     *
     * @param orderId
     * @param quarter
     * @param servicePointId   网点id，用于判断：已派单的才更新
     * @param subStatus
     * @param pendingType      停滞原因
     * @param appointmentDate
     * @param reservationDate
     * @param appAbnormalyFlag 工单异常标记
     * @param updateBy
     * @param updateAt
     */
    public void pending(long orderId, String quarter, long servicePointId, int subStatus, int pendingType, long appointmentDate, long reservationDate, int appAbnormalyFlag, long updateBy, long updateAt) {
        //未派单，不需更新
        if (servicePointId <= 0) {
            return;
        }
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.Pending)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .setPendingType(pendingType)
                .setAppointmentDate(appointmentDate)
                .setReservationDate(reservationDate)
                .setSubStatus(subStatus)
                .setAbnormalyFlag(appAbnormalyFlag)
                .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                        .setServicePointId(servicePointId)
                        .build())
                .build();
        sender.sendRetry(message, 0, 0);
    }

    /**
     * 确认上门
     * 2019/09/03 RyanLu
     * 新增对sd_order_plan.service_flag处理
     */
    public void confirmOnSiteService(long orderId, String quarter, Long servicePointId, Long engineerId, Integer status, int subStatus, long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage.Builder builder = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.ConfirmOnSiteService)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                        .setServicePointId(servicePointId)
                        .setEngineerId(engineerId == null ? 0L : engineerId) //删除的上门服务记录中的安维人员id 2019/09/03
                        .build())
                .setReservationDate(updateAt)
                .setPendingType(0)
                .setSubStatus(subStatus);
        if (status != null) {
            builder.setOrderInfo(MQOrderServicePointMessage.OrderInfo.newBuilder()
                    .setStatus(status)
                    .build());
        }
        MQOrderServicePointMessage.ServicePointMessage message = builder.build();
        sender.sendRetry(message, 0, 0);
    }


    /**
     * 对账
     */
    public void orderCharge(long orderId, String quarter, int status, int subStatus, long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.Charge)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .setSubStatus(subStatus)
                .setChargeDate(updateAt)
                .setOrderInfo(MQOrderServicePointMessage.OrderInfo.newBuilder()
                        .setStatus(status)//order.ORDER_STATUS_CHARGED
                        .build())
                .build();
        sender.sendRetry(message, 0, 0);
    }


    /**
     * app完工
     *
     * @param orderId
     * @param quarter
     * @param subStatus       子状态
     * @param appCompleteType 完工类型(字符)
     * @param abnormalyFlag   工单标记异常标志
     * @param updateBy
     * @param updateAt
     */
    public void appComplete(long orderId, String quarter, Integer subStatus, String appCompleteType, int abnormalyFlag, long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.OrderAppComplete)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setSubStatus(subStatus)
                .setAppCompleteType(appCompleteType) //app完工类型
                .setAbnormalyFlag(abnormalyFlag) //异常标记
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .build();
        sender.sendRetry(message, 0, 0);
    }

    /**
     * 工单标记/取消异常
     *
     * @param orderId
     * @param quarter
     * @param updateBy
     * @param updateAt
     */
    public void abnormalyFlag(long orderId, String quarter, Long servicePointId, int abnormalyFlag, long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.AbnormalyFlag)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                        .setServicePointId(servicePointId == null ? 0 : servicePointId)
                        .build())
                .setAbnormalyFlag(abnormalyFlag) //异常标记
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .build();
        sender.sendRetry(message, 0, 0);
    }

    /**
     * 工单关联单据处理
     * 包含加急，催单，投诉单等等
     *
     * @param orderId
     * @param quarter
     * @param reminderStatus 催单状态
     * @param complainFlag   投诉
     * @param urgentLevelId  加急
     * @param updateBy
     * @param updateAt
     */
    public void relatedForm(long orderId, String quarter,
                            Integer reminderStatus, Integer complainFlag, Integer urgentLevelId,
                            long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.RelatedForm)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setReminderFlag(Objects.isNull(reminderStatus) ? 0 : reminderStatus.intValue())
                .setComplainFlag(Objects.isNull(complainFlag) ? 0 : complainFlag.intValue())
                .setUrgentLevelId(Objects.isNull(urgentLevelId) ? 0 : urgentLevelId.intValue())
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .build();
        sender.sendRetry(message, 0, 0);
    }

    /**
     * 更新好评单状态
     *
     * @param orderId
     * @param quarter
     * @param servicePointId 网点
     * @param praiseStatus   好评单状态
     * @param updateBy
     * @param updateAt
     */
    public void syncPraiseStatus(long orderId, String quarter, Long servicePointId, Integer praiseStatus,
                                 long updateBy, long updateAt) {
        MQOrderServicePointMessage.ServicePointMessage message = MQOrderServicePointMessage.ServicePointMessage.newBuilder()
                .setOperationType(MQOrderServicePointMessage.OperationType.PraiseForm)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setServicePointInfo(MQOrderServicePointMessage.ServicePointInfo.newBuilder()
                        .setServicePointId(servicePointId == null ? 0 : servicePointId)
                        .build())
                .setPraiseStatus(praiseStatus)
                .setOperationAt(updateAt)
                .setOperationBy(updateBy)
                .build();
        sender.sendRetry(message, 0, 0);
    }

    //endregion 生产方法

}
