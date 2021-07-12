/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.td.service;

import com.kkl.kklplus.entity.sys.SysSMSTypeEnum;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.api.entity.md.RestGetVerifyCode;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestEnum;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.mq.sender.sms.SmsMQSender;
import com.wolfking.jeesite.modules.sys.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.RedisDBType.REDIS_TEMP_DB;
import static com.wolfking.jeesite.common.config.redis.RedisConstant.VERCODE_KEY;

/**
 * 消息Service
 */
@Configurable
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class MessageService extends LongIDBaseService {

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @SuppressWarnings("rawtypes")
    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private SmsMQSender smsMQSender;

    @Autowired
    private UserDao userDao;

    /**
     * 获取短信验证码
     *
     * @param verifyCode 0：注册，1：重置密码
     * @return
     */
    public RestResult<Object> getVerifyCode(RestGetVerifyCode verifyCode) {
        RestEnum.VerifyCodeType verifyCodeType = RestEnum.VerifyCodeType.valueOf(RestEnum.VerifyCodeTypeString[verifyCode.getType()]);
        if (verifyCodeType == RestEnum.VerifyCodeType.saveServicePointBankAccountInfo) {
            Random random = new Random();
            String strCode = String.valueOf(random.nextInt(999999) + 1000000).substring(1);
            String strContent = "您正在修改快可立全国联保网点的银行账号信息,验证码为:" + strCode + ",如非本人操作,请忽略";
            // 使用新的短信发送方法 2019/02/28
//                smsMQSender.send(verifyCode.getPhone(),strContent.toString(),"",0,System.currentTimeMillis());
            //TODO: 短信类型
            smsMQSender.sendNew(verifyCode.getPhone(), strContent.toString(), "", 0, System.currentTimeMillis(), SysSMSTypeEnum.VERIFICATION_CODE);
            //缓存验证码，修改网点的银行账号信息时验证
            String verifyCodeCacheKey = String.format(VERCODE_KEY, verifyCodeType.ordinal(), verifyCode.getPhone());
            if (redisUtilsLocal.exists(REDIS_TEMP_DB, verifyCodeCacheKey)) {
                redisUtilsLocal.remove(REDIS_TEMP_DB, verifyCodeCacheKey);
            }
            redisUtilsLocal.set(REDIS_TEMP_DB, verifyCodeCacheKey, strCode, 5 * 60);
        } else {
            Integer delFlag = userDao.getDelFlagByMobile(verifyCode.getPhone());
            //用户不存在
            if (delFlag == null) {
                if (verifyCodeType == RestEnum.VerifyCodeType.register) {
                    Random random = new Random();
                    String strCode = String.valueOf(random.nextInt(999999) + 1000000).substring(1);

                    StringBuilder strContent = new StringBuilder();
                    strContent.append("您正在注册快可立全国联保账号,验证码为:" + strCode + ",如非本人操作,请忽略");
                    // 使用新的短信发送方法 2019/02/28
//                smsMQSender.send(verifyCode.getPhone(),strContent.toString(),"",0,System.currentTimeMillis());
                    //TODO: 短信类型
                    smsMQSender.sendNew(verifyCode.getPhone(), strContent.toString(), "", 0, System.currentTimeMillis(), SysSMSTypeEnum.VERIFICATION_CODE);
                    //缓存验证码，注册时验证
                    String verifyCodeCacheKey = String.format(VERCODE_KEY, verifyCodeType.ordinal(), verifyCode.getPhone());
                    if (redisUtilsLocal.exists(REDIS_TEMP_DB, verifyCodeCacheKey)) {
                        redisUtilsLocal.remove(REDIS_TEMP_DB, verifyCodeCacheKey);
                    }
                    redisUtilsLocal.set(REDIS_TEMP_DB, verifyCodeCacheKey, strCode, 5 * 60);
                } else if (verifyCodeType == RestEnum.VerifyCodeType.resetPassword) {
                    return RestResultGenerator.custom(ErrorCode.MEMBER_PHONE_NOT_EXIST.code, ErrorCode.MEMBER_PHONE_NOT_EXIST.message);
                }
            }
            //用户存在
            else {
                if (verifyCodeType == RestEnum.VerifyCodeType.register) {
                    return RestResultGenerator.custom(ErrorCode.MEMBER_PHONE_REGISTERED.code, ErrorCode.MEMBER_PHONE_REGISTERED.message);
                } else if (verifyCodeType == RestEnum.VerifyCodeType.resetPassword) {
                    if (delFlag.equals(1)) {
                        return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, ErrorCode.MEMBER_ENGINEER_NO_EXSIT.message);
                    }
                    Random random = new Random();
                    String strCode = String.valueOf(random.nextInt(99999) + 100000);

                    StringBuilder strContent = new StringBuilder();
                    strContent.append("您正在进行找回密码操作,验证码为:" + strCode + ",如非本人操作,请忽略");
                    // 使用新的短信发送方法 2019/02/28
//                smsMQSender.send(verifyCode.getPhone(),strContent.toString(),"",0,System.currentTimeMillis());
                    //TODO: 短信类型
                    smsMQSender.sendNew(verifyCode.getPhone(), strContent.toString(), "", 0, System.currentTimeMillis(), SysSMSTypeEnum.VERIFICATION_CODE);
                    //缓存验证码，重置密码时验证
                    String verifyCodeCacheKey = String.format(VERCODE_KEY, verifyCodeType.ordinal(), verifyCode.getPhone());
                    if (redisUtilsLocal.exists(REDIS_TEMP_DB, verifyCodeCacheKey)) {
                        redisUtilsLocal.remove(REDIS_TEMP_DB, verifyCodeCacheKey);
                    }
                    redisUtilsLocal.set(REDIS_TEMP_DB, verifyCodeCacheKey, strCode, 5 * 60);
                }
            }
        }
        return RestResultGenerator.success();
    }
}
