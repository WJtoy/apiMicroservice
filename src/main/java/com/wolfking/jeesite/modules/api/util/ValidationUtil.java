package com.wolfking.jeesite.modules.api.util;

import cn.hutool.core.util.ReUtil;

/**
 * @author: Zhoucy
 * @date: 2020/12/23
 * @Description:
 */
public class ValidationUtil {

    public static boolean isMobileNumber(String phoneNumber) {
        return ReUtil.isMatch("^1[3456789]\\d{9}$", phoneNumber);
    }

}
