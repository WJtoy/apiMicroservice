/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sys.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.api.entity.md.AppUploadAppVersionActive;
import com.wolfking.jeesite.modules.api.util.RestEnum;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.sys.entity.APPActive;
import com.wolfking.jeesite.ms.providersys.service.MSAppActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author F1053038
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class APPVerActiveService extends LongIDBaseService {

    @Autowired
    private MSAppActiveService msAppActiveService;
    @Autowired
    private WebProperties webProperties;

    //region api functions

    /**
     * 保存活跃信息和版本号
     *
     * @param userId
     * @param phoneType
     * @param version
     * @return
     */
    public RestResult<Object> saveVerActive(Long userId, RestEnum.PhoneType phoneType, String version) {
        APPActive appActive = new APPActive();
        appActive.setUserId(userId);
        appActive.setPlatform(phoneType.ordinal());
        appActive.setVer(version);
        appActive.setCreateDate(new Date());
        appActive.setUpdateDate(new Date());
        msAppActiveService.saveActiveInfo(appActive);
        if (phoneType == RestEnum.PhoneType.iPhone) {
            AppUploadAppVersionActive uploadAppVersionActive = new AppUploadAppVersionActive();
            uploadAppVersionActive.setIosAuditFlag(isIosAuditedAppVersion(version));
            return RestResultGenerator.success(uploadAppVersionActive);
        } else {
            return RestResultGenerator.success();
        }
    }


    /**
     * 该版本的App是否正在审核[用于在审核时关闭一下app功能，方便通过审核]
     */
    private boolean isIosAuditedAppVersion(String version) {
        boolean isAuditing = false;
        String auditedVersion = webProperties.getApp().getIosAuditedAppVersion();
        String[] auditedVersionArr = StrUtil.split(auditedVersion, ".");
        String[] versionArr = StrUtil.split(version, ".");
        if (versionArr.length > 0 && auditedVersionArr.length > 0) {
            int versionNumber = 0;
            int auditedVersionNumber = 0;
            for (int i = 0; i < versionArr.length; i++) {
                versionNumber = NumberUtil.parseInt(versionArr[i]);
                if (auditedVersionArr.length > i) {
                    auditedVersionNumber = NumberUtil.parseInt(auditedVersionArr[i]);
                }
                if (versionNumber > auditedVersionNumber) {
                    isAuditing = true;
                    break;
                }
            }
        }
        return !isAuditing;
    }


}
