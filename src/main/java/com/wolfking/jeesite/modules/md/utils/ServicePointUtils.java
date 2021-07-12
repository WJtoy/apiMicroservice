/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.md.utils;

import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import lombok.extern.slf4j.Slf4j;

/**
 * 字典工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Slf4j
public class ServicePointUtils {

    private static WebProperties webProperties = SpringContextHolder.getBean(WebProperties.class);

    /**
     * 检查网点的保险启用开关
     */
    public static boolean servicePointInsuranceEnabled(ServicePoint servicePoint) {
        boolean flag = false;
        //TODO: 不再判断APP是否确认合作条款，2019-4-23
        if (webProperties.getServicePoint().getInsuranceEnabled()
                && servicePoint != null && servicePoint.getInsuranceFlag() != null && servicePoint.getInsuranceFlag() == ServicePoint.INSURANCE_FLAG_ENABLED) {
            flag = true;
        }
        return flag;
    }

}
