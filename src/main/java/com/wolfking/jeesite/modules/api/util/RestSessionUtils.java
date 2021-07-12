package com.wolfking.jeesite.modules.api.util;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.md.RestSession;

import java.util.Map;

public class RestSessionUtils {
    private static RedisUtilsLocal redisUtilsLocal = SpringContextHolder.getBean(RedisUtilsLocal.class);

    public static RestLoginUserInfo getLoginUserInfoFromRestSession(RestSession restSession) {
        return getLoginUserInfoFromRestSession(Long.valueOf(restSession.getUserId()));
    }

    public static RestLoginUserInfo getLoginUserInfoFromRestSession(String userId) {
        return getLoginUserInfoFromRestSession(Long.valueOf(userId));
    }

    public static RestLoginUserInfo getLoginUserInfoFromRestSession(long userId) {
        String key = String.format(RedisConstant.APP_SESSION, userId);
        if (redisUtilsLocal.exists(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key)) {
            Map<String, byte[]> loginUserInfoMap = redisUtilsLocal.hGetAll(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key);
            RestLoginUserInfo loginUserInfo = new RestLoginUserInfo();
            loginUserInfo.setUserId(Long.valueOf(userId));
            loginUserInfo.setServicePointId(Long.valueOf(new String(loginUserInfoMap.get("servicePointId"))));
            loginUserInfo.setEngineerId(Long.valueOf(new String(loginUserInfoMap.get("engineerId"))));
            loginUserInfo.setPhoneType(RestEnum.PhoneType.valueOf(RestEnum.PhoneTypeString[Integer.valueOf(new String(loginUserInfoMap.get("phoneType")))]));
            loginUserInfo.setPrimary(Boolean.valueOf(new String(loginUserInfoMap.get("isPrimary"))));
            return loginUserInfo;
        }
        return null;
    }
}
