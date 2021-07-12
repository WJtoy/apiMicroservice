package com.wolfking.jeesite.modules.api.controller.fi;

import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.fi.mywallet.*;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.service.fi.AppMyWalletService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrencyDeposit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther wj
 * @Date 2021/2/22 14:48
 */
@Slf4j
@RestController
@RequestMapping("/api/fi/deposit")
class AppDepositController {

    @Autowired
    private AppMyWalletService appMyWalletService;


    private RestLoginUserInfo getUserInfo(HttpServletRequest request) {
        return RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
    }


    /**
     * 获取质保金明细
     */
    @PostMapping(value = "getServicePointDepositList", produces = "application/json;charset=UTF-8")
    public RestResult<Object> getServicePointDepositList(HttpServletRequest request, @RequestBody AppGetServicePointDepositListRequest params) {
        if (params == null || params.getYearIndex() == null || params.getMonthIndex() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = getUserInfo(request);
            if (userInfo == null || userInfo.getUserId() == null || userInfo.getServicePointId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            if (!userInfo.getPrimary()) {
                return RestResultGenerator.custom(ErrorCode.NOT_PRIMARY_ACCOUNT.code, ErrorCode.NOT_PRIMARY_ACCOUNT.message);
            }
            Page<EngineerCurrencyDeposit> page = new Page<>(params.getPageNo(), params.getPageSize());
            AppGetServicePointDepositListResponse response = appMyWalletService.getServicePointDepositList(userInfo.getServicePointId(), params.getYearIndex(), params.getMonthIndex(), page);
            return RestResultGenerator.success(response);
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[AppMyWalletController.getServicePointDepositList] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取网点质保金（充值）明细失败");
        }
    }


}
