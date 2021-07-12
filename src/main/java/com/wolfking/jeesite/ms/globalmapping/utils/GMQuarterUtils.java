package com.wolfking.jeesite.ms.globalmapping.utils;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GMQuarterUtils {

    public static List<String> getQuarters() {
        Integer quarterQty = StringUtils.toInteger(MSDictUtils.getDictSingleValue("quarterCount", "0"));
        List<String> quarters = Lists.newArrayList();
        if (quarterQty > 0) {
            Date goLiveDate = OrderUtils.getGoLiveDate();
            Date startDate = getStartDateOfSeason(goLiveDate);
            Date endDate = DateUtils.addMonth(startDate, 3 * quarterQty);
            endDate = DateUtils.addDays(endDate, -1);
            quarters = getQuarters(startDate, endDate);
        }
        return quarters;
    }

    private static List<String> getQuarters(Date startDate, Date endDate) {
        List<String> quarters = Lists.newArrayList();
        if (startDate != null && endDate != null) {
            startDate = DateUtils.parseDate(DateUtils.formatDate(startDate, "yyyy-MM-dd"));
            endDate = DateUtils.parseDate(DateUtils.formatDate(endDate, "yyyy-MM-dd"));
            int startMonth, endMonth;
            while (true) {
                startMonth = startDate.getMonth();
                endMonth = endDate.getMonth();
                if (startDate.getTime() > endDate.getTime() && startMonth != endMonth && (startMonth / 3 + 1) != (endMonth / 3 + 1)) {
                    break;
                }
                quarters.add(QuarterUtils.getSeasonQuarter(startDate));
                startDate = DateUtils.addMonth(startDate, 3);
            }
        }
        return quarters;
    }

    private static Date getStartDateOfSeason(Date date) {
        int season = DateUtils.getSeason(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (season) {
            case 1:
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case 2:
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case 3:
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case 4:
                calendar.set(Calendar.MONTH, 9);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }
        return calendar.getTime();
    }
}
