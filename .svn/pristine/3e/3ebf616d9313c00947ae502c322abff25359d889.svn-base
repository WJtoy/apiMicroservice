package com.wolfking.jeesite.modules.api.controller.receipt;

import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.common.AppPageBaseEntity;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppCancelOrderPraiseRequest;
import com.wolfking.jeesite.modules.api.entity.receipt.praise.AppGetOrderPraiseInfoRequest;
import com.wolfking.jeesite.modules.api.util.ErrorCode;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * APP工单好评控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/receipt/praise")
public class AppOrderPraiseController extends RestBaseController {

    /**
     * CS9001
     * 获取工单好评信息
     */
    @RequestMapping(value = "getOrderPraiseInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getOrderPraiseInfo(HttpServletRequest request, @RequestBody AppGetOrderPraiseInfoRequest params) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本好评单接口已停用，请及时更新APP");
    }

    /**
     * CS9002
     * 保存工单好评信息
     */
    @RequestMapping(value = "saveOrderPraiseInfo", consumes = "multipart/form-data", method = RequestMethod.POST)
    public RestResult<Object> saveOrderPraiseInfo(HttpServletRequest request,
                                                  @RequestParam("file") MultipartFile[] files,
                                                  @RequestParam("json") String json) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本好评单接口已停用，请及时更新APP");
    }

    /**
     * CS9003
     * 取消工单好评
     */
    @RequestMapping(value = "cancelOrderPraise", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> cancelOrderPraise(HttpServletRequest request,
                                                @RequestBody AppCancelOrderPraiseRequest params) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本好评单接口已停用，请及时更新APP");
    }

    /**
     * CS9004
     * 获取被驳回的好评单列表
     */
    @RequestMapping(value = "getRejectedOrderPraiseList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public RestResult<Object> getRejectedOrderPraiseList(HttpServletRequest request, @RequestBody AppPageBaseEntity params) {
        return RestResultGenerator.custom(ErrorCode.DATA_PROCESS_ERROR.code, "此版本好评单接口已停用，请及时更新APP");
    }
}
