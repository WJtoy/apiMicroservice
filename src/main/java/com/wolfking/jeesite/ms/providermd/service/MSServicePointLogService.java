package com.wolfking.jeesite.ms.providermd.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.md.MDServicePointLog;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.modules.md.entity.ServicePointLog;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointLogFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSServicePointLogService {
    @Autowired
    private MSServicePointLogFeign msServicePointLogFeign;

    public List<ServicePointLog> findHisRemarks(Long servicePointId) {
        return MDUtils.findAllList(ServicePointLog.class, ()->msServicePointLogFeign.findHisRemarks(servicePointId));
    }

    public void insert(ServicePointLog servicePointLog) {
        MSErrorCode msErrorCode = MDUtils.genericSave(servicePointLog, MDServicePointLog.class, true, msServicePointLogFeign::insert);
        if (msErrorCode.getCode() >0) {
            throw new RuntimeException("保存网点日志到微服务出错。原因:" + msErrorCode.getMsg());
        }
    }

}
