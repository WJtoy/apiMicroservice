package com.wolfking.jeesite.modules.api.wx.api;

import com.adobe.xmp.impl.Base64;
import com.github.pagehelper.util.StringUtil;



import com.google.gson.JsonObject;

import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.IdGen;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.api.config.Constant;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.JwtUtil;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.wx.config.WeChatConfig;
import com.wolfking.jeesite.modules.api.wx.request.CodeParam;
import com.wolfking.jeesite.modules.api.wx.request.LoginParam;
import com.wolfking.jeesite.modules.api.wx.utils.WXUtils;
import com.wolfking.jeesite.modules.http.HttpUtils;

import com.wolfking.jeesite.modules.http.response.ResponseBody;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.SystemService;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/weChat/service/")
public class WeChatController {


    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private WebProperties webProperties;

    private static WeChatConfig weChatConfig = SpringContextHolder.getBean(WeChatConfig.class);




    /**
     * 微信小程序登陆
     * @param
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "wxlogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> wxlogin(@RequestBody LoginParam loginParam) {
        if (StringUtil.isEmpty(loginParam.getStatus()) || StringUtil.isEmpty(loginParam.getEncryptedData()) || StringUtil.isEmpty(loginParam.getIv())) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
            String openId = (String) redisUtilsLocal.get(loginParam.getStatus(),String.class);
        if(openId == null){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
            String session_key = (String) redisUtilsLocal.get(openId,String.class);
        if (session_key == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            String aesResult = WXUtils.decryptData(loginParam.getEncryptedData(), session_key, loginParam.getIv()); //解密获取用户信息
            if (aesResult == null){
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
                JSONObject wxInfo = JSONObject.fromObject(aesResult);
                String phone = wxInfo.getString("phoneNumber");
                Long userId = systemService.getAppUserByPhone(phone);
            if (userId == null) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_PHONE_NOT_EXIST.code, ErrorCode.MEMBER_PHONE_NOT_EXIST.message);
            }
                User user = systemService.getUser(userId);
                if (user == null) {
                    return RestResultGenerator.custom(ErrorCode.MEMBER_PHONE_NOT_EXIST.code, ErrorCode.MEMBER_PHONE_NOT_EXIST.message);
                }
                Engineer engineer = servicePointService.getAppEngineer(user.getId(), Constant.JWT_TTL);
                if (engineer == null) {
                    return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, ErrorCode.MEMBER_ENGINEER_NO_EXSIT.message);
                }
                //登录标志更新及登录日期
                if (engineer.getAppLoged() < 1) {
                    user.setAppLoged(2);
                    engineer.setAppLoged(1);
                }
                String session = IdGen.uuid();
                String subject = JwtUtil.generalSubject(userId.toString(), session);
                String key = String.format(RedisConstant.APP_SESSION, userId.toString());
                int engineerCount = servicePointService.getEngineersFromCache(engineer.getServicePoint().getId()).size();
                    try {
                        String token = JwtUtil.createJWT(Constant.JWT_ID, subject, Constant.JWT_TTL);
                        JsonObject jo = new JsonObject();
                        jo.addProperty("id", engineer.getId().toString());
                        jo.addProperty("name", user.getName());
                        jo.addProperty("token", token);
                        jo.addProperty("isPrimary", engineer.getMasterFlag().equals(1));
                        jo.addProperty("hasSub", engineerCount > 1);
                        jo.addProperty("insuranceEnabled", webProperties.getServicePoint().getInsuranceEnabled());
                        jo.addProperty("insuranceForced", webProperties.getServicePoint().getInsuranceForced());
                        jo.addProperty("appInsuranceFlag", engineer.getServicePoint().getAppInsuranceFlag());
                        try {
                            redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "session", session, Constant.JWT_TTL);
                            //redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "phoneType", loginInfo.getPhoneType(), Constant.JWT_TTL);
                            redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_NEW_APP_DB, key, "isPrimary", engineer.getMasterFlag().equals(1), Constant.JWT_TTL);
                        } catch (Exception e) {
                            LogUtils.saveLog("Rest登录错误", "SecurityController.loginWx", loginParam.getStatus(), e, null);
                        }
                        return RestResultGenerator.success(jo);
                    } catch (Exception e) {
                        log.error("数据异常{}",e.getMessage());
                    }

            } catch(Exception e){
                e.printStackTrace();
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }


    /***
     * 获取code
     * @param
     * @return
     */
    @RequestMapping(value = "getCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getCode(@RequestBody CodeParam codeParam) {

            if (StringUtil.isEmpty(codeParam.getCode())) {
                return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
            }
            Map<String,String> map = new HashMap<String,String>();
            map.put("appid", weChatConfig.getWeChatDataConfig().getAppId());
            map.put("js_code", codeParam.getCode());
            map.put("grant_type", weChatConfig.getWeChatDataConfig().getAuthorizationCode());
            map.put("secret", weChatConfig.getWeChatDataConfig().getAppSecret());
            ResponseBody<ResponseBody> result =  HttpUtils.doGet(weChatConfig.getWeChatDataConfig().getWeChatUrl(),map, ResponseBody.class);
            if (result.getData() != null && result.getData().getErrcode() == 0) {
                JSONObject object = JSONObject.fromObject(result.getOriginalJson());
                if (object !=null){
                String session_key = object.getString("session_key");
                String openID = object.getString("openid");
                JsonObject jo = new JsonObject();
                String status = IdGen.uuid();
                redisUtilsLocal.set(status,openID,Constant.wxTime);
                redisUtilsLocal.set(openID,session_key,Constant.wxTime);

                jo.addProperty("status",status);
                return RestResultGenerator.success(jo);
                }
            }
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, ErrorCode.DATA_PROCESS_ERROR.message);
        }

}
