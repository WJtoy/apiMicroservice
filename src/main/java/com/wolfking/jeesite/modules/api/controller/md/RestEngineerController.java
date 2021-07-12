package com.wolfking.jeesite.modules.api.controller.md;

import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.md.*;
import com.wolfking.jeesite.modules.api.service.md.AppEngineerService;
import com.wolfking.jeesite.modules.api.util.*;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/engineer/")
public class RestEngineerController extends RestBaseController {

    @Autowired
    private AppEngineerService appEngineerService;

    @Autowired
    private ServicePointService servicePointService;

    /**
     * 获取师傅个人信息
     */
    @RequestMapping(value = "getEngineerInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getUserInfo(HttpServletRequest request, @RequestBody RestGetUserInfo params) throws Exception {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            RestEnum.UserInfoType userInfoType = RestEnum.UserInfoType.valueOf(RestEnum.UserInfoTypeString[params.getType()]);
            RestEngineerInfo restEngineer = appEngineerService.getEngineerInfo(userInfoType, userInfo);
            return RestResultGenerator.success(restEngineer);
        } catch (RestAppException e) {
            return RestResultGenerator.exception(e.getMessage());
        } catch (Exception e) {
            log.error("[RestEngineerController.getEngineerInfo] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取个人信息失败");
        }
    }


    /**
     * 保存师傅地址信息接口
     */
    @RequestMapping(value = "saveEngineerAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> saveEngineerAddress(HttpServletRequest request,
                                                  @RequestBody RestEngineerAddress engineerAddress) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Engineer engineer = servicePointService.getEngineerFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            if (null == engineer) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取帐号信息失败");
            }
            User user = new User();
            user.setId(userInfo.getUserId());
            user.setName(engineer.getName());
            appEngineerService.saveEngineerAddress(engineer, engineerAddress, user);
            return RestResultGenerator.success();
        } catch (RestAppException e) {
            return RestResultGenerator.exception(e.getMessage());
        } catch (Exception e) {
            log.error("[RestEngineerController.saveEngineerAddress] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("保存用户地址失败");
        }
    }


    /**
     * 获取师傅收货地址信息接口
     */
    @RequestMapping(value = "getEngineerConsigneeAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getEngineerConsigneeAddress(HttpServletRequest request) throws Exception {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            AppGetEngineerConsigneeAddressResponse consigneeAddress = appEngineerService.getEngineerConsigneeAddressFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            return RestResultGenerator.success(consigneeAddress);
        } catch (RestAppException e) {
            return RestResultGenerator.exception(e.getMessage());
        } catch (Exception e) {
            log.error("[RestEngineerController.getEngineerConsigneeAddress] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取用户收货地址失败");
        }
    }

    /**
     * 保存师傅收货地址信息接口
     */
    @RequestMapping(value = "saveEngineerConsigneeAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> saveEngineerConsigneeAddress(HttpServletRequest request,
                                                           @RequestBody AppSaveEngineerConsigneeAddressRequest params) {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            Engineer engineer = servicePointService.getEngineerFromCache(userInfo.getServicePointId(), userInfo.getEngineerId());
            if (null == engineer) {
                return RestResultGenerator.custom(ErrorCode.MEMBER_ENGINEER_NO_EXSIT.code, "读取帐号信息失败");
            }
            User user = new User();
            user.setId(userInfo.getUserId());
            user.setName(engineer.getName());
            AppSaveEngineerConsigneeAddressResponse response = appEngineerService.saveEngineerConsigneeAddress(userInfo.getServicePointId(), userInfo.getEngineerId(), params, user);
            return RestResultGenerator.success(response);
        } catch (RestAppException e) {
            return RestResultGenerator.exception(e.getMessage());
        } catch (Exception e) {
            log.error("[RestEngineerController.saveEngineerAddress] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("保存用户收货地址失败");
        }
    }

    /**
     * 获取师傅个人地址和网点地址
     */
    @RequestMapping(value = "getEngineerAndServicePointAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getEngineerAndServicePointAddress(HttpServletRequest request) throws Exception {
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            AppGetEngineerAndServicePointAddressResponse response = appEngineerService.getEngineerAndServicePointAddress(userInfo.getServicePointId(), userInfo.getEngineerId());
            return RestResultGenerator.success(response);
        } catch (RestAppException e) {
            return RestResultGenerator.exception(e.getMessage());
        } catch (Exception e) {
            log.error("[RestEngineerController.getEngineerAndServicePointAddress] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取师傅个人地址和网点地址失败");
        }
    }
}
