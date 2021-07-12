package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2bcenter.md.B2BAsyncOperationEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BAsyncTriggerB2BOperationMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderActionEnum;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.mq.sender.B2BAsyncTriggerB2BOperationMQSender;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCenterAsyncTriggerB2BOperationService {

//    @Autowired
//    private B2BAsyncTriggerB2BOperationMQSender b2BAsyncTriggerB2BOperationMQSender;
//
//    @Autowired
//    private B2BCenterOrderService b2BCenterOrderService;

    public static void saveFailureLog(String method, String msgJson, Exception ex) {
        LogUtils.saveLog("异步触发B2B操作失败", method, msgJson, ex, B2BOrderVModel.b2bUser);
    }

    //region 消费队列消息

//    public void processAsyncTriggerB2BOperationMessage(MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage message) {
//        if (B2BAsyncOperationEnum.isB2BAsyncOperation(message.getB2BAsyncOperationId())) {
//            try {
//                if (message.getB2BAsyncOperationId() == B2BAsyncOperationEnum.UPDATE_ORDER_STATUS_TO_CHARGED.id) {
//                    b2BCenterOrderService.chargeB2BOrder(message.getCustomerId(), message.getKklOrderId(), message.getKklQuarter(), message.getUpdaterId(), message.getUpdateDt());
//                }
//            } catch (Exception e) {
//                B2BCenterAsyncTriggerB2BOperationService.saveFailureLog("B2BCenterAsyncTriggerB2BOperationService.processAsyncTriggerB2BOperationMessage", new JsonFormat().printToString(message), new Exception(e.getLocalizedMessage()));
//            }
//        } else {
//            B2BCenterAsyncTriggerB2BOperationService.saveFailureLog("B2BCenterAsyncTriggerB2BOperationService.processAsyncTriggerB2BOperationMessage", "B2B异常操作码不正确", null);
//        }
//    }

    //endregion 消费队列消息

    //region 往队列发送消息

    /**
     * 异步触发B2B的工单对账操作
     */
//    public void triggerOrderChargeOperation(Long customerId, Long orderId, String quarter, Long updateBy, Long updateAt) {
//        if (orderId != null && orderId > 0) {
//            MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage message = MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage.newBuilder()
//                    .setB2BAsyncOperationId(B2BAsyncOperationEnum.UPDATE_ORDER_STATUS_TO_CHARGED.id)
//                    .setCustomerId(customerId)
//                    .setKklOrderId(orderId)
//                    .setKklQuarter(StringUtils.toString(quarter))
//                    .setUpdaterId(updateBy == null ? B2BOrderVModel.b2bUser.getId() : updateBy)
//                    .setUpdateDt(updateAt == null ? (new Date()).getTime() : updateAt)
//                    .build();
//            b2BAsyncTriggerB2BOperationMQSender.sendDelay(message);
//        }
//    }

    //endregion 往队列发送消息

}
