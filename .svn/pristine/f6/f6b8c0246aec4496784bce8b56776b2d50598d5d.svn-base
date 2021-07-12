/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.controller.sd;

import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.RestAppException;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.entity.sd.RestGetProductFixSpec;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestGetProductFixSpecRequest;
import com.wolfking.jeesite.modules.api.service.sd.AppOrderInfoService;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 工单信息
 */
@Slf4j
@RestController
@RequestMapping("/api/orderInfo/")
public class RestOrderInfoController extends RestBaseController {

    @Autowired
    private AppOrderInfoService appOrderInfoService;

    /**
     * 获取工单产品的安装规范
     */
    @RequestMapping(value = "getProductFixSpec", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getProductFixSpec(HttpServletRequest request, @RequestBody RestGetProductFixSpecRequest params) {
        if (params == null || params.getOrderId() == null || params.getOrderId() == 0
                || params.getProductId() == null || params.getProductId() == 0
                || params.getOrderItemIndex() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo userInfo = null;
        try {
            userInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute(REQUEST_ATTRIBUTE_NAME_SESSION_USER_ID).toString());
            if (userInfo == null || userInfo.getUserId() == null) {
                return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
            }
            RestGetProductFixSpec productFixSpec = appOrderInfoService.getProductFixSpec(params.getOrderId(), params.getQuarter(), params.getProductId(), params.getOrderItemIndex());
            return RestResultGenerator.success(productFixSpec);
        } catch (OrderException | RestAppException oe) {
            return RestResultGenerator.exception(oe.getMessage());
        } catch (Exception e) {
            log.error("[RestOrderInfoController.getProductFixSpec] user:{}", userInfo != null && userInfo.getUserId() != null ? userInfo.getUserId() : 0, e);
            return RestResultGenerator.exception("获取产品安装规范失败");
        }
    }

}
