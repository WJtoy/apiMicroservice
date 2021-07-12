package com.wolfking.jeesite.ms.providersys.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.sys.SysAppActive;
import com.kkl.kklplus.entity.sys.SysAppNotice;
import com.wolfking.jeesite.modules.sys.entity.APPActive;
import com.wolfking.jeesite.modules.sys.entity.APPNotice;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAppActiveFeign;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAppNoticeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MSAppActiveService {
    @Autowired
    private MSSysAppActiveFeign sysAppActiveFeign;

    /**
     * 调用微服务保存版本号活跃信息
     * @param appActive
     */
    public void saveActiveInfo(APPActive appActive) {
        MSErrorCode msErrorCode = MDUtils.genericSave(appActive, SysAppActive.class, false, sysAppActiveFeign::saveActiveInfo);
        if (msErrorCode.getCode() >0) {
            throw new RuntimeException("调用微服务更新手机App通知失败,失败原因:"+ msErrorCode.getMsg());
        }
    }
}
