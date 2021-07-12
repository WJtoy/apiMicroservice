/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.utils;

import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.util.Assert;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author ThinkGem
 * @version 2014-4-15
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return "";
        }
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到查询时间年月字符串 格式（yyyyMM）
     */
    public static String getYearMonth(Date date) {
        return formatDate(date, "yyyyMM");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay(Date date) {
        return formatDate(date, "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @return
     */
    public static int getSeason() {
        return getSeason(new Date());
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 获得年+季度
     * 格式:20193
     * @param date
     */
    public static String getQuarter(Date date){
        int quarer = getSeason(date);
        return String.format("%s%s",getYear(date),quarer);
    }

    /**
     * 获得两个时间的季度边界
     * @param start
     * @param end
     * @return
     */
    public static String[] getQuarterRange(Date start,Date end){
        if(start == null){
            start = new Date();
        }
        if(end == null){
            end = new Date();
        }
        String[] quarters = new String[2];
        quarters[0] = getQuarter(start);
        if(start == end){
            quarters[1] = quarters[0];
        }else {
            quarters[1] = getQuarter(end);
        }
        return quarters;
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取开始日期，小时:分：秒 -> 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
		/*
		if (date == null)
		{
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
		*/

        if (date == null) {
            return null;
        }
        try {
            date = parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            //e.printStackTrace();
            log.error("getDateStart:{}", date, e);
        }
        return date;

    }

    /**
     * 获取结束日期，小时:分：秒 -> 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
		/*
		if (date == null)
		{
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		try
		{
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59:999");
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
		*/
        if (date == null) {
            return null;
        }
        try {
            date = parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59:999", "yyyy-MM-dd HH:mm:ss:SSS");
        } catch (ParseException e) {
            //e.printStackTrace();
            log.error("getDateEnd:{}", date, e);
        }
        return date;
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的天数
     *
     * @param startDate 起始时间
     * @return toDate    截止时间
     */
    public static long pastDays(Date startDate, Date toDate) {
        long t = toDate.getTime() - startDate.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param startDate 起始时间
     * @return
     */
    public static long pastHour(Date startDate, Date toDate) {
        long t = toDate.getTime() - startDate.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param startDate 起始时间
     * @return
     */
    public static long pastMinutes(Date startDate, Date toDate) {
        long t = toDate.getTime() - startDate.getTime();
        return t / (60 * 1000);
    }

    /**
     * 时间戳转换成日期
     */
    public Date timeStampToDate(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return calendar.getTime();
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * @param args
     * @throws ParseException
     */

    /**
     * 时间加年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date, int year) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.YEAR, year);
        return c1.getTime();
    }

    /**
     * 时间加月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, int month) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.MONTH, month);
        return c1.getTime();
    }

    /**
     * 在java.util.DateObject上增加/减少几天
     *
     * @param date java.util.Date instance
     * @param days 增加/减少的天数
     * @return java.util.Date Object
     */
    public static Date addDays(Date date, int days) {
        long temp = date.getTime();
        return new Date(temp + DateUtils.DAY_MILLI * days);
    }

    /**
     * 在java.util.DateObject上增加/减少几小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.HOUR, hour);
        return c1.getTime();
    }

    /**
     * 在java.util.DateObject上增加/减少几秒钟
     *
     * @param date
     * @param second
     * @return
     */
    public static Date addSecond(Date date, int second) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.SECOND, second);
        return c1.getTime();
    }

    /**
     * 判断两个时间是否为同一时间
     *
     * @param date1
     * @param date2
     * @param formatString
     * @return isSameDate(date1, date2, " yyyy - MM - dd ")
     */
    public static boolean isSameDate(Date date1, Date date2, String formatString) {
        boolean result = false;
        try {
//			SimpleDateFormat formt1 = new SimpleDateFormat(formatString);
//			String str1 = formt1.format(date1);
//			String str2 = formt1.format(date2);
            String str1 = formatDate(date1, formatString);
            String str2 = formatDate(date2, formatString);
            if (str1 == null || str2 == null) {
                return false;
            }
            if (str1.equals(str2)) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /*
     * 获取时间戳
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

//	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
//	}

    /**
     * 获得指定日期的前一天
     *
     * @param date
     * @return
     */
    public static Date getDayBefore(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        int nday = c.get(Calendar.DATE);
        c.set(Calendar.DATE, nday - 1);
        java.util.Date d = (java.util.Date) c.getTime();
        java.sql.Date sqlDate = new java.sql.Date(d.getTime());
        return sqlDate;
    }

    /**
     * 日期时候超过现在
     */
    public static boolean isGreaterNow(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t < 0;
    }

    /**
     * 日期时候超过现在
     */
    public static boolean isGreaterNowByString(String date) {
        //DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date datetime = parse(date, "yyyy-MM-dd HH:mm:ss");
            long t = System.currentTimeMillis() - datetime.getTime();
            return t < 0;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern) throws ParseException {
//		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
//				pattern).parse(strDate);
        return StringUtils.isBlank(strDate) ? null : parseDate(strDate, new String[]{pattern});
    }

    /**
     * 获取指定时间所属的月份的总天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 将数字year、month、day转成日期类型
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取日期的年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期的月（一月返回1，十二月返回12）
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的小时
     */
    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 是否是同一天
     */
    public static boolean isSameDay(Date aDate, Date bDate) {
        boolean isSame = false;
        if (aDate != null && bDate != null) {
            isSame = getStartOfDay(aDate).getTime() == getStartOfDay(bDate).getTime();
        }
        return isSame;
    }

    /**
     * 仅比较两个日期对象的时间部分
     * 返回值：0 - 相等，1 - aDate的时间部分比bDate的时间部分大，-1 - aDate的时间部分比bDate的时间部分小
     */
    public static int compareTimePart(Date aDate, Date bDate) {
        Assert.notNull(aDate, "DateUtils.compareTimePart：aDate不能为null");
        Assert.notNull(bDate, "DateUtils.compareTimePart：bDate不能为null");

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();

        calendar.setTime(aDate);
        Date aNewDate = getDate(now, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));

        calendar.setTime(bDate);
        Date bNewDate = getDate(now, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));

        if (aNewDate.getTime() == bNewDate.getTime()) {
            return 0;
        } else if (aNewDate.getTime() > bNewDate.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 获取某天的开始时间
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取指定日期所属月份的第一天
     *
     * @param date
     * @return
     */
    public static Date getStartDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得指定天中指定小时，分钟，秒
     *
     * @param date
     * @param hour
     * @param minute
     * @param sencond
     * @return
     */
    public static Date getDate(Date date, Integer hour, Integer minute, Integer sencond) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //hour
        if (hour == null || hour < 0 || hour > 23) {
            hour = 0;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        //minute
        if (minute == null || minute < 0 || minute > 59) {
            minute = 0;
        }
        calendar.set(Calendar.MINUTE, minute);
        //sencond
        if (sencond == null || sencond < 0 || sencond > 59) {
            sencond = 0;
        }
        calendar.set(Calendar.SECOND, sencond);
        //millisecond
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDate(Date date, Integer hour, Integer minute, Integer sencond, Integer millisecond) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (hour == null || hour < 0 || hour > 23) {
            hour = 0;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        if (minute == null || minute < 0 || minute > 59) {
            minute = 0;
        }
        calendar.set(Calendar.MINUTE, minute);

        if (sencond == null || sencond < 0 || sencond > 59) {
            sencond = 0;
        }
        calendar.set(Calendar.SECOND, sencond);

        if (millisecond == null || millisecond < 0 || millisecond > 999) {
            millisecond = 0;
        }
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    /**
     * 获取当月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return calendar.getTime();
    }

    /**
     * 获得当前的日期毫秒
     */
    public static long nowTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前的时间戳
     */
    public static Timestamp nowTimeStamp() {
        return new Timestamp(nowTimeMillis());
    }

    /**
     * 传入的时间戳
     */
    public static String timestampToDate(Timestamp timestamp) {
        return DateFormatUtils.format(timestamp.getTime(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * Long转日期
     *
     * @param millis
     * @return
     */
    public static Date longToDate(Long millis) {
        if(millis == null || millis.longValue()<=0){
            return null;
        }
        try {
            return parse(DateFormatUtils.format(millis, "yyyy-MM-dd HH:mm:ss.SSS"), "yyyy-MM-dd HH:mm:ss.SSS");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算日期相差月份
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static int getDateDiffMonth(Date startDate, Date endDate) {
        int yearDiff = (DateUtils.getYear(endDate) - DateUtils.getYear(startDate)) * 12;
        int monthDiff = DateUtils.getMonth(endDate) - DateUtils.getMonth(startDate);
        return Math.abs(yearDiff + monthDiff);
    }

    /**
     * 计算日期相差天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static int getDateDiffDay(Date startDate, Date endDate) {
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        Long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / nd);
    }

    /**
     * 计算日期相差小时
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static float getDateDiffHour(Date startDate, Date endDate) {
        float nd = 1000f * 60 * 60;//一天的毫秒数
        Long diff = endDate.getTime() - startDate.getTime();
        return (float) (diff / nd);
    }

    /**
     * 将时间戳转成日期字符串
     */
    public static String formatDateString(Long timestamp) {
        String dateString = "";
        if (timestamp != null && timestamp > 0) {
            Date date = new Date(timestamp);
            dateString = formatDateTime(date);
        }
        return dateString;
    }

    /**
     * 分钟转小时+分钟
     * @param minutes
     * @return  xx小时xx分钟
     */
    public static String minuteToTimeString(int minutes,String hourTitle,String minuteTitle){

        if(minutes<60){
            return minutes + minuteTitle;
        }
        int hour = Math.round(minutes / 60);
        int minute = Math.round(minutes - (hour * 60));
        return hour + hourTitle + (minute == 0 ? "" : minute + minuteTitle);
    }

    /**
     * 几个时间相差多少小时
     * @param
     * @return
     */
    public static double differTime(long beginDate,long endDate){
        double praiseTimeliness = (double)(endDate-beginDate)/(60*60*1000);
        praiseTimeliness = BigDecimal.valueOf(praiseTimeliness).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return praiseTimeliness;
    }

    /**
     * 获取时效(返回小于0说明已经超时)
     * @param date 当前时间
     * @param timeOut 超时时间
     * @param beginDt 开始时间
     * @param endDt   结束时间
     * @return
     */
    public static double calculateTimeliness(Date date,long timeOut,long beginDt,long endDt){
        double timeDiffer = 0.0;
        if(timeOut > date.getTime()){//到期时间大于当前时间
            boolean isSameDate = DateUtils.isSameDay(date,new Date(timeOut));
            boolean isEffectiveDate = isEffectiveDate(date.getTime(),9,18); //判断前期时间是不否在当前9点到18点之间
            if(isSameDate){ //如果是同一天
                if(isEffectiveDate){ //在工作时间内当天9点～18点
                    timeDiffer = DateUtils.differTime(date.getTime(),timeOut);
                }else { //如果非工作时间内,到期时间减去当天9点
                    timeDiffer = DateUtils.differTime(beginDt,timeOut);
                }
            }else { //如果到期时间在第二天
                if(isEffectiveDate){ //在工作时间内 当天18点-现在时间+到期时间-第二天9点
                    long longTimeDiffer = endDt-date.getTime()+timeOut-beginDt-(24*60*60*1000);
                    timeDiffer =(double)longTimeDiffer/(60*60*1000);
                    timeDiffer = BigDecimal.valueOf(timeDiffer).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }else{ //到期时间-第二天9点
                    timeDiffer = DateUtils.differTime(beginDt+(24*60*60*1000),timeOut);
                }
            }
        }else{
            timeDiffer = DateUtils.differTime(date.getTime(),timeOut);
        }
        return timeDiffer;
    }

    /**
     * 判断当前时间是否在工作时间内(9点-18点)
     * @param dataMillis 当前时间
     * @return
     */
    public static boolean isEffectiveDate(long dataMillis,int firstHour,int secondHour) {
        TwoTuple<Long,Long> twoTuple = getTwoTupleDate(firstHour,secondHour);
        if(twoTuple.getAElement()<= dataMillis && dataMillis<= twoTuple.getBElement()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 返回当天9点和18点的时间错个连个时间点
     * @param
     * @return
     */
    public static TwoTuple<Long,Long> getTwoTupleDate(int firstHour,int secondHour){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, firstHour);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date startTime = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, secondHour);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date endTime = c.getTime();
        TwoTuple<Long,Long> twoTuple = new TwoTuple<Long, Long>(startTime.getTime(),endTime.getTime());
        return twoTuple;
    }
}
