/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.utils;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.GsonIgnoreStrategy;
import com.wolfking.jeesite.common.mapper.adapters.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;


/**
 * Gson字符串工具类
 * @author Ryan Lu
 */
@Slf4j
public class GsonUtils {
	
	private static final Gson gson = new GsonBuilder()
			.addSerializationExclusionStrategy(new GsonIgnoreStrategy())
			//序列化null
			//.serializeNulls()
			//null <-> String
			.registerTypeAdapter(String.class, new StringConverter())
			//禁止转义html标签
			.disableHtmlEscaping()
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			//.excludeFieldsWithoutExposeAnnotation() // <---
			.setLongSerializationPolicy(LongSerializationPolicy.STRING)//由于js精度不够(2的53次方)，返回json时将Long转成字符
			.create();

	private static GsonUtils gsonUtils;

	public GsonUtils() {}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
	 */
	public static GsonUtils getInstance() {
		if (gsonUtils == null){
			gsonUtils = new GsonUtils();
		}
		return gsonUtils;
	}

	public Gson getGson(){
		return gson;
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 */
	public String toGson(Object object) {
		return gson.toJson(object);
	}

	/**
	 * 对象转换为JSON字符串
	 * @param object
	 * @return
	 */
	public static String toGsonString(Object object){
		try {
			return GsonUtils.getInstance().toGson(object);
		}catch (Exception e){
			log.error("[GsonUtils.toGsonString]",e);
			return "";
		}
	}

	/**
	 * JSON转对象
	 * @param json
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json,clazz);
	}

	/**
	 * 比较两个对象序列化json后不同
	 * @param src
	 * @param dsc
	 * @param excludes 排除属性
	 * @return
	 */
	public static String difference(Object src,Object dsc,Set<String> excludes) {
		String jsonSrc = gson.toJson(src);
		String jsonDsc = gson.toJson(dsc);
		return difference(jsonSrc,jsonDsc,excludes);
	}

	/**
	 * 以src为准，比较两个json不同之处
	 * @param src	原json
	 * @param dsc	目标json
	 * @param excludes 排除属性
	 * @return
	 */
    public static String difference(String src,String dsc,Set<String> excludes) {
		Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
		if(excludes == null){
			excludes = Sets.newHashSet();
		}
		Map<String, Object> firstMap = gson.fromJson(src, mapType);
		Map<String, Object> secondMap = gson.fromJson(dsc, mapType);

		MapDifference differenceMap = Maps.difference(firstMap, secondMap);
		boolean areEqual = differenceMap.areEqual();
		if (areEqual) {
			return null;
		}

		Map<String,Object> entriesDiffering = differenceMap.entriesDiffering();
		StringBuffer rtnStr = new StringBuffer(200);
		Object leftObject,rightObject;
		for (String key : entriesDiffering.keySet()) {
			if(excludes.contains(key)){
				continue;
			}
			MapDifference.ValueDifference maps  = (MapDifference.ValueDifference)entriesDiffering.get(key);
			leftObject = maps.leftValue();
			rightObject = maps.rightValue();
			if(!leftObject.equals(rightObject)){
				rtnStr.append("key= ").append(key)
						.append(",left= ").append(leftObject.toString())
						.append(",right= ")
						.append(rightObject.toString())
						.append("  ")
						.append(System.getProperty("line.separator"));
			}
		}
		return rtnStr.toString();

	}

	/**
	 * mycat低版本对mysql json支持不好，中文括号不支持
	 * @param json
	 * @return
	 */
	public static String MyCatJsonFormat(String json){
    	if(StringUtils.isBlank(json)){
    		return "";
		}
		return json
				.replaceAll("（","[")
				.replaceAll("\\(","[")
				.replaceAll("）","]")
				.replaceAll("\\)","]");
	}

	/*
	 * 将json格式的字符串转换为对象
	 */
	public static  <T> T fromJsonNew(String json, Class<T> clazz) {
		T objOfT = null;
		if (StringUtils.isNotBlank(json) && clazz != null) {
			try {
				objOfT = gson.fromJson(json, clazz);
			} catch (Exception e) {
				log.error("[GsonUtils.fromJsonNew]", Exceptions.getStackTraceAsString(e));
			}
		}
		return objOfT;
	}
}
