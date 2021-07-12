package com.wolfking.jeesite.modules.api.controller.md;

import com.google.gson.JsonObject;
import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.modules.api.entity.common.NoticeConstant;
import com.wolfking.jeesite.modules.api.entity.md.RestGetUserInfo;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther wj
 * @Date 2021/3/4 10:21
 */
@Slf4j
@RestController
@RequestMapping("/api/config/")
public class RestConfigController {

    @Autowired
    private WebProperties webProperties;


    @RequestMapping(value = "getProjectConfigure", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getProjectConfigure(HttpServletRequest request) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("engineerHealthEnabled",webProperties.getEngineerHealthEnabled());
            jo.addProperty("noticeEnabled",webProperties.getApp().getNoticeEnabled());
            jo.addProperty("noticeVersion",webProperties.getApp().getNoticeVersion());
            jo.addProperty("iosAuditedAppVersion",webProperties.getApp().getIosAuditedAppVersion());
            jo.addProperty("servicePointCooperationTermsEnabled",webProperties.getApp().getServicePointCooperationTermsEnabled());
            jo.addProperty("noticeContent", NoticeConstant.NOTICE_CONTENT);
            jo.addProperty("identityCardEnabled",webProperties.getApp().getIdentityCardEnabled());
            return RestResultGenerator.success(jo);
        }catch (Exception e){
            return RestResultGenerator.custom(ErrorCode.UNKNOWN_EXCEPTION.code,"获取项目配置异常");
        }
    }



}
