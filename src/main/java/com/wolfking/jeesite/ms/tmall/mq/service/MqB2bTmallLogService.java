package com.wolfking.jeesite.ms.tmall.mq.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MqB2bTmallLogService {

//    @Resource
//    private MqB2bTmallDao mqB2bTmallDao;
//
//    private void insert(MqB2bTmallLog mqB2bTmallLog) {
//        if (mqB2bTmallLog != null) {
//            mqB2bTmallDao.insert(mqB2bTmallLog);
//        }
//    }
//
//    @Transactional()
//    public void insertMqB2bTmallLog(B2BMQQueueType queueType, String messageJson, Long createById,
//                                    B2BProcessFlag processFlag, int processTime, String processComment) {
//        MqB2bTmallLog mqB2bTmallLog = new MqB2bTmallLog();
//        Date now = new Date();
//        mqB2bTmallLog.setQueueId(queueType.id);
//        mqB2bTmallLog.setMessageJson(messageJson);
//        mqB2bTmallLog.setProcessFlag(processFlag.value);
//        mqB2bTmallLog.setProcessTime(processTime);
//        mqB2bTmallLog.setProcessComment(processComment);
//        mqB2bTmallLog.setCreateBy(new User(createById));
//        mqB2bTmallLog.setCreateDate(now);
//        mqB2bTmallLog.setUpdateBy(new User(createById));
//        mqB2bTmallLog.setUpdateDate(now);
//        mqB2bTmallLog.setQuarter(QuarterUtils.getSeasonQuarter(now));
//        insert(mqB2bTmallLog);
//    }


}
