package com.wolfking.jeesite.modules.fi.service;

import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.service.LongIDCrudService;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.fi.dao.EngineerCurrencyDao;
import com.wolfking.jeesite.modules.fi.entity.EngineerCurrency;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeff on 2017/4/20.
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class ServicePointCurrencyService extends LongIDCrudService<EngineerCurrencyDao, EngineerCurrency> {
    @Resource
    private EngineerCurrencyDao engineerCurrencyDao;

    public Page<EngineerCurrency> getServicePointCurrencyListForApi(Long servicePointId, Integer actionType, Date beginDate, Date endDate, String currencyNo,
                                                              Page<EngineerCurrency> page) {
        // add on 2019-11-16 begin
        Date[] dates = OrderUtils.getQuarterDates(beginDate, endDate, 0, 0);
        List<String> quarters = QuarterUtils.getQuarters(dates[0], dates[1]);
        // add on 2019-11-16 end
        List<EngineerCurrency> list =  engineerCurrencyDao.getServicePointCurrencyListForApi(servicePointId, actionType, beginDate, endDate, currencyNo, quarters, page);
        page.setList(list);
        return page;
    }

    /**
     * 网点帐户明细按月汇总
     * @param servicePointId
     * @param actionType
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<Map<String,Object>> getServicePointCurrencySummryByMonthApi(Long servicePointId, Integer actionType, Date beginDate, Date endDate) {
         return engineerCurrencyDao.getServicePointCurrencySummryByMonthApi(servicePointId, actionType, beginDate, endDate);
    }
}
