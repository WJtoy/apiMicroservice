package com.wolfking.jeesite.ms.providersys.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.entity.sys.SysAppNotice;
import com.wolfking.jeesite.modules.sys.entity.APPNotice;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAppNoticeFeign;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MSAppNoticeService {
    @Autowired
    private MSSysAppNoticeFeign sysAppNoticeFeign;

    /**
     * 根据用户Id获取单个手机App通知对象Id
     *
     * @param userId
     * @return
    */
    public Long getOneIdByUserId(Long userId) {
        return MDUtils.getObjUnnecessaryConvertType(()->sysAppNoticeFeign.getOneIdByUserId(userId));
    }

    /**
     * 根据用户id获取手机App通知
     *
     * @param userId
     * @return
     */
    public APPNotice getByUserId(Long userId) {
        return MDUtils.getObjNecessaryConvertType(APPNotice.class, ()->sysAppNoticeFeign.getByUserId(userId));
    }

    /**
     * 添加手机App通知
     *
     * @param appNotice
     * @return
    */
    public void insert(APPNotice appNotice) {
        MSErrorCode msErrorCode = MDUtils.genericSave(appNotice, SysAppNotice.class, true, sysAppNoticeFeign::insert);
        if (msErrorCode.getCode() >0) {
            throw new RuntimeException("调用微服务添加手机App通知失败,失败原因:"+ msErrorCode.getMsg());
        }
    }

    /**
     * 根据用户id更新手机通知
     * @param appNotice
     */
    public void updateByUserId(APPNotice appNotice) {
        MSErrorCode msErrorCode = MDUtils.genericSave(appNotice, SysAppNotice.class, false, sysAppNoticeFeign::updateByUserId);
        if (msErrorCode.getCode() >0) {
            throw new RuntimeException("调用微服务更新手机App通知失败,失败原因:"+ msErrorCode.getMsg());
        }
    }
}
