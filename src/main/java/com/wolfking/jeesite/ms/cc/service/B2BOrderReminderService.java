package com.wolfking.jeesite.ms.cc.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderReminderProcessMessage;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.mq.sender.B2BCenterOrderReminderCloseMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther wj
 * @Date 2020/11/9 13:22
 */

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class B2BOrderReminderService {

    @Autowired
    private B2BCenterOrderReminderCloseMQSender b2BCenterOrderReminderCloseMQSender;

    public MSResponse sendReminderProcess(MQB2BOrderReminderProcessMessage.B2BOrderReminderProcessMessage message, String b2BReminderNo){
        MSResponse msResponse = new MSResponse(MSErrorCode.SUCCESS);
        int dataSource = message.getDataSource();
        if (B2BDataSourceEnum.isB2BDataSource(dataSource) && message.getKklReminderId() > 0
                && StringUtils.isNotBlank(b2BReminderNo)
                && StringUtils.isNotBlank(message.getContent())){
            if (dataSource == B2BDataSourceEnum.JOYOUNG.id || dataSource == B2BDataSourceEnum.MQI.id){
                b2BCenterOrderReminderCloseMQSender.sendRetry(message,1);
            }
        }
        return msResponse;
    }

}
