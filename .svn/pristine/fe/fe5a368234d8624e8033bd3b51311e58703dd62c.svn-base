package com.wolfking.jeesite.ms.utils;

import com.google.gson.JsonObject;
import com.kkl.kklplus.entity.b2b.common.B2BProcessFlag;
import com.kkl.kklplus.entity.b2b.mq.B2BMQQueueType;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author: Zhoucy
 * @date: 2020/8/12
 * @Description:
 */
@Lazy(false)
@Component
@Slf4j
public class B2BFailureLogUtils {

    /**
     * 记录B2B操作的错误日志
     */
    public static void saveFailureLog(B2BMQQueueType queueType, String messageJson, Long createById,
                                B2BProcessFlag processFlag, int processTime, String processComment) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("queueType", queueType.id);
            jo.addProperty("messageJson", messageJson);
            jo.addProperty("createById", createById);
            jo.addProperty("processFlag", processFlag.value);
            jo.addProperty("processTime", processTime);
            jo.addProperty("processComment", processComment);
            String logContent = GsonUtils.toGsonString(jo);
            LogUtils.saveLog("B2BFailureLogUtils.saveFailureLog", queueType.queueName, logContent, null, null);
        } catch (Exception e) {
            log.error("B2BFailureLogUtils.saveFailureLog", e);
        }
    }

}
