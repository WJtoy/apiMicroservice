package com.wolfking.jeesite.modules.api.controller.fi;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.api.entity.fi.RestGetServicePointCurrencyList;
import com.wolfking.jeesite.modules.api.entity.fi.RestServicePointCurrency;
import com.wolfking.jeesite.modules.api.entity.md.RestLoginUserInfo;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.api.util.RestSessionUtils;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import com.wolfking.jeesite.modules.fi.service.ServicePointCurrencyService;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fi/")
public class RestFIController {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    private ServicePointService servicePointService;

    @Autowired
    private ServicePointCurrencyService servicePointCurrencyService;

    /**
     * 获取网点余额信息
     *
     * @return
     * @throws Exception
     */
    @PostMapping(value = "getServicePointBalance", produces = "application/json;charset=UTF-8")
    public RestResult<Object> getBalance(HttpServletRequest request) throws Exception {
        RestLoginUserInfo loginUserInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        return servicePointService.getBalance(loginUserInfo.getServicePointId());
    }

    /**
     * 获取网点帐户明细
     *
     * @return
     * @throws Exception
     */
    @PostMapping(value = "getServicePointCurrencyList", produces = "application/json;charset=UTF-8")
    public RestResult<Object> getServicePointCurrencyList(HttpServletRequest request, @RequestBody RestGetServicePointCurrencyList getServicePointCurrencyList) throws Exception {
        RestLoginUserInfo loginUserInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        Integer actionType = null;
        if (getServicePointCurrencyList.getType() > 0) {
            if (getServicePointCurrencyList.getType().equals(1)) {
                actionType = 20;
            } else if (getServicePointCurrencyList.getType().equals(2)) {
                actionType = 50;
            } else if (getServicePointCurrencyList.getType().equals(3)) {
                actionType = 3040;
            }
        }
        Page<EngineerCurrency> page = new Page<>(getServicePointCurrencyList.getPageNo(), getServicePointCurrencyList.getPageSize());
        page = servicePointCurrencyService.getServicePointCurrencyListForApi(loginUserInfo.getServicePointId(),
                actionType,
                getServicePointCurrencyList.getBeginDate() != null && getServicePointCurrencyList.getBeginDate() > 0 ? new Date(getServicePointCurrencyList.getBeginDate()) : null,
                getServicePointCurrencyList.getEndDate() != null && getServicePointCurrencyList.getEndDate() > 0 ? new Date(getServicePointCurrencyList.getEndDate()) : null,
                "", page);
        Page<RestServicePointCurrency> returnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        if (page != null && page.getList() != null && page.getList().size() > 0) {
            List<RestServicePointCurrency> rtnList = Lists.newArrayList();
            List<EngineerCurrency> list = page.getList();
            List<String> months = list.stream().map(t -> DateUtils.formatDate(t.getCreateDate(), "yyyy-MM")).distinct().collect(Collectors.toList());
            String month = new String("");
            Date startDate, endDate;
            double amountIn = 0;
            double amountOut = 0;
            int ractionType;
            double amount;
            List<Map<String, Object>> maps;
            final Map<String, Dict> dicts = MSDictUtils.getDictMap("ServicePointActionType");//切换为微服务
            for (int i = 0, size = months.size(); i < size; i++) {
                month = months.get(i);
                startDate = DateUtils.parse(month + "-01", "yyyy-MM-dd");
                endDate = DateUtils.getLastDayOfMonth(startDate);
                maps = servicePointCurrencyService.getServicePointCurrencySummryByMonthApi(loginUserInfo.getServicePointId(), actionType, startDate, endDate);
                amountIn = 0;
                amountOut = 0;
                if (maps != null) {
                    for (Map<String, Object> map : maps) {
                        ractionType = (Integer) map.get("action_type");
                        amount = new BigDecimal(map.get("amount").toString()).doubleValue();
                        if (ractionType <= 40) {
                            amountIn = amountIn + amount;
                        } else {
                            amountOut = amountOut + Math.abs(amount);
                        }
                    }
                }
                final double famountIn = amountIn;
                final double famountOut = amountOut;
                final Integer fmonth = Integer.valueOf(month.replace("-", ""));
                list.stream()
                        .filter(t -> DateUtils.formatDate(t.getCreateDate(), "yyyyMM").equals(fmonth.toString()))
                        .forEach(t -> {
                            final RestServicePointCurrency rest = mapper.map(t, RestServicePointCurrency.class);
                            if (dicts != null) {
                                final Dict dict = dicts.get(t.getActionType().toString());
                                if (dict != null) {
                                    rest.setActionType(dict.getDescription());
                                } else {
                                    rest.setActionType("读取失败，请刷新");
                                }
                            }
                            rest.setMonth(fmonth);
                            rest.setAmountIn(famountIn);
                            rest.setAmountOut(famountOut);
                            rtnList.add(rest);
                        });
            }
            returnPage.setList(rtnList);
        }
        return RestResultGenerator.success(returnPage);
    }


    @PostMapping(value = "getServicePointCurrencyDetail", produces = "application/json;charset=UTF-8")
    public RestResult<Object> getServicePointCurrencyDetail(HttpServletRequest request, @RequestBody RestGetServicePointCurrencyList getServicePointCurrencyList) throws Exception {
        RestLoginUserInfo loginUserInfo = RestSessionUtils.getLoginUserInfoFromRestSession(request.getAttribute("sessionUserId").toString());
        Integer actionType = null;
        if (getServicePointCurrencyList.getType() > 0) {
            if (getServicePointCurrencyList.getType().equals(1)) {
                actionType = 20;
            } else if (getServicePointCurrencyList.getType().equals(2)) {
                actionType = 50;
            } else if (getServicePointCurrencyList.getType().equals(3)) {
                actionType = 3040;
            }
        }
        Date queryDate = DateUtils.getDate(getServicePointCurrencyList.getYear(), getServicePointCurrencyList.getMonth(), 1);
        Date startDate = DateUtils.getStartDayOfMonth(queryDate);
        Date endDate = DateUtils.getLastDayOfMonth(queryDate);
        Page<EngineerCurrency> page = new Page<>(getServicePointCurrencyList.getPageNo(), getServicePointCurrencyList.getPageSize());
        page = servicePointCurrencyService.getServicePointCurrencyListForApi(loginUserInfo.getServicePointId(),
                actionType,
                startDate,
                endDate,
                "", page);
        Page<RestServicePointCurrency> returnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        double sumIn = 0d;
        double sumOut = 0d;
        Integer sumActionType;
        double amount;
        //计算汇总
        if (getServicePointCurrencyList.getPageNo().equals(1)) {
            List<Map<String, Object>> maps = servicePointCurrencyService.getServicePointCurrencySummryByMonthApi(loginUserInfo.getServicePointId(), actionType, startDate, endDate);
            for (Map<String, Object> map : maps) {
                sumActionType = (Integer) map.get("action_type");
                amount = new BigDecimal(map.get("amount").toString()).doubleValue();
                if (sumActionType <= 40) {
                    sumIn = sumIn + amount;
                } else {
                    sumOut = sumOut + Math.abs(amount);
                }
            }
        }
        final Map<String, Dict> dicts = MSDictUtils.getDictMap("ServicePointActionType");//切换为微服务
        if (page != null && page.getList() != null && page.getList().size() > 0) {
            returnPage.setList(mapper.mapAsList(page.getList(), RestServicePointCurrency.class));
            if (dicts != null && dicts.size() > 0) {
                double finalSumIn = sumIn;
                double finalSumOut = sumOut;
                returnPage.getList().stream()
                        .forEach(t -> {
                            final Dict dict = dicts.get(t.getActionTypeValue().toString());
                            if (dict != null) {
                                t.setActionType(dict.getDescription());
                            } else {
                                t.setActionType("");
                            }
                            t.setAmountIn(finalSumIn);
                            t.setAmountOut(finalSumOut);
                        });
            }
        }
        return RestResultGenerator.success(returnPage);
    }
}
