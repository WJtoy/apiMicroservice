package com.wolfking.jeesite.modules.api.controller.sd;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerCert;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.AttachmentSaveFailureException;


import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.sd.*;


import com.wolfking.jeesite.modules.api.service.sd.AppIdentityService;
import com.wolfking.jeesite.modules.api.util.*;


import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther wj
 * @Date 2021/4/8 10:33
 */
@Slf4j
@RestController
@RequestMapping("/api/identity/")
public class RestIdentityController {

    @Autowired
    private AppIdentityService appIdentityService;


    /**
     * 根据 type传入是否填写过
     * @param request
     * @param type
     * @return
     */
    @RequestMapping(value = "commonCheck",  method = RequestMethod.POST ,produces = "application/json;charset=UTF-8")
    public RestResult<Object> commonCheck(HttpServletRequest request,@RequestBody RestListType type) {
        RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        if (userInfo == null || userInfo.getUserId() == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        if (type == null || type.getType().size()==0){
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        List<RestCert> restCerts = Lists.newArrayList();
        try {
            for (Integer in : type.getType()) {
                if (MoldEnum.Cert.IDCARD.getValue()== in) {
                    RestCert restCert = new RestCert();
                    restCert.setType(in);
                    restCert.setFill(appIdentityService.getPast(userInfo.getEngineerId()));
                    restCerts.add(restCert);
                }
            }
            RestListTypeReturn restListTypeReturn = new RestListTypeReturn();
            restListTypeReturn.setList(restCerts);
            return RestResultGenerator.success(restListTypeReturn);
        }catch (Exception e){
            return RestResultGenerator.exception("信息获取异常");
        }
    }

    /**
     * 根据师傅id获取身份证号
     * @param request
     * @return
     */
    @RequestMapping(value = "getIdentity",  method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public RestResult<Object> getIdentity(HttpServletRequest request) {
        RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        if (userInfo == null || userInfo.getUserId() == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        try {
            String identity = appIdentityService.getIdentity(userInfo.getEngineerId());
            RestIdentityCard restIdentityCard = new RestIdentityCard();
            restIdentityCard.setIdentityCard(identity);
            return RestResultGenerator.success(restIdentityCard);
        }catch (Exception e){
            return RestResultGenerator.exception("获取身份证异常");
        }

    }


    /**
     * 保存上传的身份证图片
     * @param request
     * @param files
     * @param
     * @return
     */
    @RequestMapping(value = "saveIdentity", consumes = "multipart/form-data",  method = RequestMethod.POST)
    public RestResult<Object> saveIdentity(HttpServletRequest request, @RequestParam("file") MultipartFile[] files, @RequestParam("identityCard") String identityCard) {
        if (StringUtils.isBlank(identityCard)) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (files == null ){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            String filePath = "";
            String filePath2 = "";
            if (files != null && files.length == 2) {
                TwoTuple<Boolean, List<String>> saveFileResponse = OrderPicUtils.saveImageFilesForMDServicePoint(request, files);
                if (!saveFileResponse.getAElement() || saveFileResponse.getBElement().isEmpty()) {
                    throw new AttachmentSaveFailureException("保存身份证图片失败");
                }
                filePath = saveFileResponse.getBElement().get(0);
                filePath2 = saveFileResponse.getBElement().get(1);
            }
            List<MDEngineerCert> mdEngineerCerts = Lists.newArrayList();
            MDEngineerCert identity = new MDEngineerCert();
            identity.setNo(1);
            identity.setPicUrl(filePath);
            MDEngineerCert identity1 = new MDEngineerCert();
            identity1.setNo(2);
            identity1.setPicUrl(filePath2);
            mdEngineerCerts.add(identity);
            mdEngineerCerts.add(identity1);
            MSResponse<Integer> response = appIdentityService.updateIdCard(userInfo.getEngineerId(),userInfo.getUserId(),identityCard,mdEngineerCerts);
            if (response.getCode()>0){
                return RestResultGenerator.custom(ErrorCode.UNKNOWN_EXCEPTION.code, "保存身份信息失败");
            }
            return RestResultGenerator.success();
        } catch (Exception e) {
            return RestResultGenerator.exception("保存身份证图片异常");
        }
    }


}
