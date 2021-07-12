/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.utils;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.config.Global;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 数据库分片工具
 */
public class QuarterUtils {

	/**
	 * 按季度分片
	 * @return
	 */
	public static String getSeasonQuarter(Date date)
	{
		String year = DateUtils.formatDate(date, "yyyy");
		int season = DateUtils.getSeason(date);
		return String.format("%s%s",year,season);
	}

	/**
	 * 按季度分片
	 * @return
	 */
	public static String getSeasonQuarter(Long millis)
	{
		Date date = DateUtils.longToDate(millis);
		String year = DateUtils.formatDate(date, "yyyy");
		int season = DateUtils.getSeason(date);
		return String.format("%s%s",year,season);
	}

	/**
	 * 根据订单号获得数据库分片
	 * @param orderNo
	 * @return
	 */
	public static String getOrderQuarterFromNo(String orderNo){
		if(StringUtils.isBlank(orderNo)){
			return "";
		}
		orderNo = StringUtils.trimToEmpty(orderNo);
		if(orderNo.length()==14){//K2017100100001
			try {
				String strdate = orderNo.substring(1, 9);
				Date date = DateUtils.parse(strdate,"yyyyMMdd");
				int quarer = DateUtils.getSeason(date);
				return String.format("%s%s",DateUtils.getYear(date),quarer);
			}catch (Exception e){
				return "";
			}
		}
		return "";
	}

	/**
	 * 根据单据编号获得数据库分片
	 * @param no			编号
	 * @param start			日期起始位置
	 * @param len			日期长度
	 * @param dateFormat	日期格式
	 * @return	分片
	 */
	public static String getQuarterOfNo(String no,int start,int len,String dateFormat){
		if(StringUtils.isBlank(no)){
			return StringUtils.EMPTY;
		}
		no = StringUtils.trim(no);
		StringBuffer sbQuarter = new StringBuffer(6);
		try {
			String strdate = StringUtils.mid(no,start,len);
			Date date = DateUtils.parse(strdate,dateFormat);
			int quarer = DateUtils.getSeason(date);
			sbQuarter.append(DateUtils.getYear(date)).append(quarer);
			return sbQuarter.toString();
		}catch (Exception e){
			return StringUtils.EMPTY;
		}finally {
			sbQuarter.setLength(0);
		}
	}

/*
	public static void main(String[] args) throws Exception {
		Date date = new Date();
		date = DateUtils.addMonth(date,-17);
		System.out.println(DateUtils.formatDate(date,"yyyy-MM-dd"));
//		Date date = new Date(2017,6,1,8,30,0);
		String quarter = QuarterUtils.getSeasonQuarter(date);
		System.out.println(quarter);
	}
*/

	public static List<String> getQuarters(Date startDate,Date endDate){
		List<String> quarters = Lists.newArrayList();
		if(startDate == null){
			return quarters;
		}
		if(endDate == null || DateUtils.isGreaterNow(endDate)){
			endDate = new Date();
		}
		startDate = DateUtils.parseDate(DateUtils.formatDate(startDate,"yyyy-MM-dd"));
		endDate = DateUtils.parseDate(DateUtils.formatDate(endDate,"yyyy-MM-dd"));
		int startMonth,endMonth;
		while(true){
			startMonth = startDate.getMonth();
			endMonth = endDate.getMonth();
			if(startDate.getTime()>endDate.getTime()
					&& startMonth != endMonth
					&& (startMonth/3 + 1) != (endMonth /3 + 1)
					){
				break;
			}
			quarters.add(getSeasonQuarter(startDate));
			startDate = DateUtils.addMonth(startDate,3);
		}
		return quarters;
	}

}
