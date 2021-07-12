package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BCenterPushOrderInfoToMsMessage;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.mq.sender.B2BCenterPushOrderInfoToMsMQSender;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import com.wolfking.jeesite.ms.keg.sd.service.KegOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCenterPushOrderInfoToMsService {

    @Autowired
    private B2BCenterPushOrderInfoToMsMQSender pushOrderInfoToMsMQSender;
    @Autowired
    private KegOrderService kegOrderService;

    public static void saveFailureLog(String method, String msgJson, Exception ex) {
        LogUtils.saveLog("B2BCenterPushOrderInfoToMsService.saveFailureLog", method, msgJson, ex, B2BMDUtils.B2B_USER);
    }

    /**
     * 创建工单
     */
    public void createOrder(Order order) {
        long customerId = order != null && order.getOrderCondition() != null ? order.getOrderCondition().getCustomerId() : 0;
        MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage msgObj = null;
        if (kegOrderService.isKegCustomer(customerId)) {
            msgObj = kegOrderService.createNewOrderMessage(order);
        }
        if (msgObj != null && msgObj.getErrorCode() == 0) {
            sendMessageToMQ(msgObj);
        }
    }

    /**
     * 完成工单
     */
    public void completeOrder(Order order, Date completedDate, User updater, Date updateDate) {
        long customerId = order != null && order.getOrderCondition() != null ? order.getOrderCondition().getCustomerId() : 0;
        MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage msgObj = null;
        if (order != null && order.getId() != null) {
            if (kegOrderService.isKegCustomer(customerId)) {
                msgObj = kegOrderService.createCompleteOrderMessage(order.getId(), order.getQuarter(), completedDate, updater);
            }
        }
        if (msgObj != null && msgObj.getErrorCode() == 0) {
            sendMessageToMQ(msgObj);
        }
    }

    //region 队列辅助方法

    /**
     * 往队列发送消息
     */
    private void sendMessageToMQ(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        pushOrderInfoToMsMQSender.send(message);
    }

    /**
     * 处理收到的队列消息
     */
    public MSResponse processMQMessage(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        MSResponse response = new MSResponse<>(MSErrorCode.SUCCESS);
        if (message.getMsCode() == B2BDataSourceEnum.KEG.id) {
            response = kegOrderService.pushOrderInfoToMS(message);
        }
        return response;
    }

    //endregion 队列辅助方法

}
