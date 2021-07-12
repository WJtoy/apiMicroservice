package com.wolfking.jeesite.ms.service.push;

import com.kkl.kklplus.entity.push.AppMessageCategoryEnum;
import com.kkl.kklplus.entity.push.CastMethodEnum;
import com.kkl.kklplus.entity.push.MQAppPushMessage;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.modules.mq.sender.PushMessageSender;
import com.wolfking.jeesite.modules.sys.entity.APPNotice;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import com.wolfking.jeesite.ms.entity.AppPushMessage;
import com.wolfking.jeesite.ms.providersys.service.MSAppNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 推送切换为为服务
 */
@Slf4j
@Service
public class APPMessagePushService {

    @Autowired
    private PushMessageSender pushMessageSender;

    @Autowired
    private MSAppNoticeService appNoticeService;  //add on 2020-7-10


    public void sendMessage(AppPushMessage message) {
//        boolean pushEnabled = Boolean.valueOf(Global.getConfig("pushEnabled"));
        boolean pushEnabled = ApiPropertiesUtils.getWeb().getPushEnabled();
        if (!pushEnabled) {
            return;
        }
        //APPNotice appNotice = appNoticeDao.getByUserId(message.getUserId());      //mark on 2020-7-10
        APPNotice appNotice = appNoticeService.getByUserId(message.getUserId());    //add on 2020-7-10
        if (appNotice != null) {
            try {
                MQAppPushMessage.PushMessage mqMessage = MQAppPushMessage.PushMessage.newBuilder()
                        .setPassThrough(message.getPassThroughType().value)
                        .setPlatform(appNotice.getPlatform())
                        .setRegId(appNotice.getChannelId())
                        .setMessageType(message.getMessageType().value)
                        .setSubject(message.getSubject())
                        .setUserId(message.getUserId())
                        .setTimestamp(message.getTimestamp())
                        .setTitle(message.getTitle())
                        .setDescription(message.getDescription())
                        .setContent(message.getContent())
                        .setCastMethod(CastMethodEnum.UNICAST.getValue())
                        .setCategory(AppMessageCategoryEnum.ORDER.getValue())
                        .build();

                pushMessageSender.send(mqMessage);
            } catch (Exception e) {
                log.error("发送推送错误,user:{} ,msg:{}", message.getUserId(), message.getContent(), e);
            }
        }
    }

}
