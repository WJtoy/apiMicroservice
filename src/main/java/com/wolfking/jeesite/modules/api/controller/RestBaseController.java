package com.wolfking.jeesite.modules.api.controller;

import com.thoughtworks.xstream.core.BaseException;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.modules.api.config.ValidTokenException;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class RestBaseController {

    protected static final String REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID = "sessionUserId";

    @ExceptionHandler(BaseException.class)
    public Object handleBaseException(HttpServletRequest request, BaseException e) {
        e.printStackTrace();
        return RestResultGenerator.exception(e.getMessage());
    }

    @ExceptionHandler(ValidTokenException.class)
    public Object handleBaseException(HttpServletRequest request, ValidTokenException e) {
        return RestResultGenerator.custom(ErrorCode.INVALID_TOKEN.code, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleBaseException(HttpServletRequest request, RuntimeException e) {
        log.error("restBaseController.handleBaseException", e);
        if (e instanceof NullPointerException) {
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "值为空");
        } else if (e instanceof HttpMessageNotReadableException) {
            LogUtils.saveLog("REST.异常", "RestBaseController", "", e, null);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "传输数据格式错误，请联系客服或管理员");
        } else if (e instanceof IllegalStateException) {
            LogUtils.saveLog("REST.异常", "RestBaseController", "", e, null);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "数据格式错误，请联系客服或管理员");
        } else if (e instanceof OrderException) {
            //具体业务抛出的异常，不记录日志
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, e.getMessage());
        } else {
            LogUtils.saveLog("REST.异常", "RestBaseController", "", e, null);
            return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "数据处理异常，请重试");
        }
    }

    /**
     * 读取当前账号信息
     *
     * @param request
     * @return
     */
    protected User getLoginUser(HttpServletRequest request) {
        User user = null;
        try {
            RestLoginUserInfo userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return user;
            }
            long userId = userInfo.getUserId();
            user = UserUtils.getAcount(userId);
        } catch (Exception e) {
            log.error("读取账号错误", e);
        } finally {
            return user;
        }
    }

}
