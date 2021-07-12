package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.ServicePointLog;
import com.wolfking.jeesite.modules.md.entity.viewModel.ServicePointPlanRemarkModel;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.providermd.service.MSServicePointLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 服务网点
 * Ryan Lu
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ServicePointLogService extends LongIDBaseService {

    @Autowired
    private MSServicePointLogService msServicePointLogService;

    /**
     * 保存网点日志
     */
    @Transactional()
    public void saveServicePointLog(Long servicePointId, ServicePointLog.ServicePointLogType type, String title, String content, User user) {
        ServicePointLog log = new ServicePointLog();
        if (servicePointId != null && type != null && user != null && user.getId() != null) {
            log.setServicePoint(new ServicePoint(servicePointId));
            log.setType(type.getValue());
            log.setTitle(StringUtils.toString(title));
            log.setContent(StringUtils.toString(content));
            log.setOperator(StringUtils.toString(user.getName()));
            log.setCreateBy(user);
            log.setCreateDate(new Date());
            //servicePointLogDao.insert(log);     //mark on 2020-1-18
            msServicePointLogService.insert(log); // add on 2019-12-24
        } else {
            log.setServicePoint(new ServicePoint(servicePointId));
            log.setType(type != null ? type.getValue() : null);
            log.setTitle(title);
            log.setContent(content);
            log.setOperator(user != null ? user.getName() : null);
            log.setCreateBy(user);
            log.setCreateDate(new Date());
            String logJson = GsonUtils.toGsonString(log);
            LogUtils.saveLog("保存网点日志失败", "ServicePointLogService.saveServicePointLog", logJson, null, null);
        }
    }


    /**
     * 获取网点的备注历史
     */
    public List<ServicePointPlanRemarkModel> getHisRemarks(Long servicePointId) {
        List<ServicePointPlanRemarkModel> list = Lists.newArrayList();
        if (servicePointId != null && servicePointId > 0) {
            //List<ServicePointLog> logList = servicePointLogDao.getHisRemarks(servicePointId); //mark on 2020-1-18

            List<ServicePointLog> logList = msServicePointLogService.findHisRemarks(servicePointId);    //add on 2019-12-24

            if (!logList.isEmpty()) {
                logList.forEach(i -> {
                    ServicePointPlanRemarkModel model = new ServicePointPlanRemarkModel();
                    model.setName(i.getOperator());
                    model.setPlanRemark(i.getContent());
                    model.setDate(DateUtils.formatDateTime(i.getCreateDate()));
                    list.add(model);
                });
            }
        }
        return list;
    }

    public static String toServicePointJson(ServicePoint servicePoint) {
        String json = "";
        if (servicePoint != null) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("status", servicePoint.getStatusValue());
            map.put("level", servicePoint.getLevel() != null ? servicePoint.getLevel().getIntValue() : 0);
            map.put("timeLinessFlag", servicePoint.getTimeLinessFlag());
            map.put("insuranceFlag", servicePoint.getInsuranceFlag());
            map.put("appInsuranceFlag", servicePoint.getAppInsuranceFlag());
            map.put("bankIssue", servicePoint.getBankIssue() != null ? servicePoint.getBankIssue().getValue() : "");  // add on 2020-2-28
            map.put("resetPrice", servicePoint.getResetPrice());  //是否重置价格  //add on 2020-3-5
            map.put("customizePriceFlag", servicePoint.getCustomizePriceFlag()); // 自定义价格数据 //add on 2020-6-1
            json = GsonUtils.toGsonString(map);
        }
        return json;
    }

}
