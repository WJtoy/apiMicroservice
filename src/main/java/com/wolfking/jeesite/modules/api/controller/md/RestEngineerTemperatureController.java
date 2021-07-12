package com.wolfking.jeesite.modules.api.controller.md;

import com.kkl.kklplus.utils.QuarterUtils;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.controller.RestBaseController;
import com.wolfking.jeesite.modules.api.entity.md.RestEngineerTemperature;
import com.wolfking.jeesite.modules.api.entity.md.RestEngineerTemperatureInfo;
import com.wolfking.jeesite.modules.api.entity.md.RestGetEngineerTemperaturePage;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.util.*;
import com.wolfking.jeesite.modules.md.entity.viewModel.EngineerTemperatureSearchModel;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * @Auther wj
 * @Date 2021/1/15 10:05
 */
@Slf4j
@RestController
@RequestMapping("/api/temperature/")
public class RestEngineerTemperatureController extends RestBaseController {

      @Autowired
      private ServicePointService servicePointService;



    /**
     * 保存安维人员体温
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploadEngineerTemperature", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> uploadEngineerTemperature(HttpServletRequest request , @RequestBody RestEngineerTemperature params) throws Exception {
        RestLoginUserInfo loginUserInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        if (loginUserInfo == null || loginUserInfo.getUserId() == null || loginUserInfo.getServicePointId() == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        if (params == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        if (params.getHealthOption() == null || params.getTemperature() == null){
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        return servicePointService.saveEngineerTemperature(loginUserInfo,params);
    }

    /**
     * 获取安维体温列表
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getEngineerTemperatureList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult<Object> getEngineerTemperatureList(HttpServletRequest request, @RequestBody RestGetEngineerTemperaturePage params) throws Exception {
        if (params == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }

        if (params.getPageNo() == null || params.getPageNo() == 0) {
            params.setPageNo(1);
        }
        if (params.getPageSize() == null || params.getPageSize() == 0) {
            params.setPageSize(10);
        }

        if (params == null || params.getYear() == null || params.getMonth() == null) {
            return RestResultGenerator.custom(ErrorCode.WRONG_REQUEST_FORMAT.code, ErrorCode.WRONG_REQUEST_FORMAT.message);
        }
        RestLoginUserInfo loginUserInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        if (loginUserInfo == null || loginUserInfo.getUserId() == null || loginUserInfo.getServicePointId() == null) {
            return RestResultGenerator.custom(ErrorCode.LOGIN_INFO_MISSING.code, ErrorCode.LOGIN_INFO_MISSING.message);
        }
        Page<RestEngineerTemperatureInfo> page = new Page<>(params.getPageNo(), params.getPageSize());
        Page<EngineerTemperatureSearchModel> searchPage = new Page<>(page.getPageNo(), page.getPageSize());
        EngineerTemperatureSearchModel engineerTemperatureSearchModel = new EngineerTemperatureSearchModel();
        engineerTemperatureSearchModel.setPage(searchPage);
        engineerTemperatureSearchModel.setEngineerId(loginUserInfo.getEngineerId());
        engineerTemperatureSearchModel.setQuarter(QuarterUtils.getQuarter(DateUtils.getStartOfDay(DateUtils.getDate(params.getYear(),params.getMonth(), 1))));
        engineerTemperatureSearchModel.setBeginCreateDate(DateUtils.getStartOfDay(DateUtils.getDate(params.getYear(),params.getMonth(), 1)));
        engineerTemperatureSearchModel.setEndCreateDate(DateUtils.getStartOfDay(DateUtils.getDate(params.getYear(), params.getMonth() + 1, 1)));
        return servicePointService.getEngineerTemperatureList(page,engineerTemperatureSearchModel,searchPage);
    }
}
