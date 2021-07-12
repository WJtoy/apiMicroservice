/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sys.utils;

import cn.hutool.extra.servlet.ServletUtil;
import com.kkl.kklplus.entity.sys.SysLog;
import com.kkl.kklplus.entity.sys.mq.MQSysLogMessage;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.Exceptions;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.mq.sender.LogSender;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author ThinkGem
 * @version 2014-11-7
 */
@Lazy(false)
@Component
@Slf4j
public class LogUtils {

    @Autowired
    private LogSender logSender;

    private static LogUtils logUtils ;

    @PostConstruct
    public void init(){
        logUtils = this ;
        logUtils.logSender = this.logSender;
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title);
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
        try{
            String params = getRequestParameterString(request.getParameterMap());
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr(ServletUtil.getClientIP(request))
                    .setRequestUri(StringUtils.left(request.getRequestURI(),250))
                    .setMethod(request.getMethod())
                    .setParams(params)
                    .setUserAgent(StringUtils.left(request.getHeader("user-agent"),250))
                    .setException(ex==null?"": Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(0)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,uri:{} ",title,request.getRequestURI(),e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title,String params) {
        try{
            if(StringUtils.isBlank(params)) {
                params = getRequestParameterString(request.getParameterMap());
            }
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr(ServletUtil.getClientIP(request))
                    .setRequestUri(StringUtils.left(request.getRequestURI(),250))
                    .setMethod(request.getMethod())
                    .setParams(params)
                    .setUserAgent(StringUtils.left(request.getHeader("user-agent"),250))
                    .setException(ex==null?"": Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(0)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,uri:{} ",title,request.getRequestURI(),e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title,String params,User user) {
        long userId = (user==null || user.getId() == null)?0:user.getId();
        try{
            if(StringUtils.isBlank(params)) {
                params = getRequestParameterString(request.getParameterMap());
            }
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr(ServletUtil.getClientIP(request))
                    .setRequestUri(StringUtils.left(request.getRequestURI(),250))
                    .setMethod(request.getMethod())
                    .setParams(params)
                    .setUserAgent(StringUtils.left(request.getHeader("user-agent"),250))
                    .setException(ex==null?"": Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(userId)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,user:{} ,uri:{}",title,userId,request.getRequestURI(),e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title,String method,String params,User user) {
        long userId = (user == null || user.getId() == null) ? 0 : user.getId();
        try {
            if (StringUtils.isBlank(params)) {
                params = getRequestParameterString(request.getParameterMap());
            }
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr(ServletUtil.getClientIP(request))
                    .setRequestUri(StringUtils.left(request.getRequestURI(),250))
                    .setMethod(method)
                    .setParams(params)
                    .setUserAgent(StringUtils.left(request.getHeader("user-agent"),250))
                    .setException(ex == null ? "" : Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(userId)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,user:{} ,uri:{} ",title,userId,request.getRequestURI(),e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title,User user) {
        long userId = (user == null || user.getId() == null) ? 0 : user.getId();
        try {
            String params = getRequestParameterString(request.getParameterMap());
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr(ServletUtil.getClientIP(request))
                    .setRequestUri(StringUtils.left(request.getRequestURI(),250))
                    .setMethod(request.getMethod())
                    .setParams(params)
                    .setUserAgent(StringUtils.left(request.getHeader("user-agent"),250))
                    .setException(ex == null ? "" : Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(userId)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e) {
            log.error("[LogUtils.saveLog] title:{} ,uri:{} ,user:{}", title, request.getRequestURI(), userId, e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(String title,String method, String params, Exception ex,User user) {
        long userId = (user == null || user.getId() == null) ? 0 : user.getId();
        try {
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr("127.0.0.1")
                    .setRequestUri(StringUtils.left(method,250))
                    .setMethod("Log")
                    .setParams(params)
                    .setUserAgent("Web")
                    .setException(ex == null ? "" : Exceptions.getStackTraceAsString(ex))
                    .setCreateBy((user == null || user.getId() == null) ? 0 : user.getId())
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,method:{} ,user:{}",title,method,userId,e);
        }
    }

    /**
     * 保存日志
     */
    public static void saveLog(String title,String method, String params, Throwable ex,User user) {
        long userId = (user == null || user.getId() == null) ? 0 : user.getId();
        try {
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION)
                    .setTitle(title)
                    .setRemoteAddr("127.0.0.1")
                    .setRequestUri(StringUtils.left(method,250))
                    .setMethod("Log")
                    .setParams(params)
                    .setUserAgent("Web")
                    .setException(ex == null ? "" : Exceptions.getStackTraceAsString(ex))
                    .setCreateBy((user == null || user.getId() == null) ? 0 : user.getId())
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,method:{} ,user:{}",title,method,userId,e);
        }
    }

    public static void saveLog(String title,String method, String params, Exception ex,User user,Integer type) {
        long userId = (user == null || user.getId() == null) ? 0 : user.getId();
        try {
            MQSysLogMessage.SysLogMessage logMessage = MQSysLogMessage.SysLogMessage.newBuilder()
                    .setType(type)
                    .setTitle(title)
                    .setRemoteAddr("127.0.0.1")
                    .setRequestUri(StringUtils.left(method,250))
                    .setMethod("Log")
                    .setParams(params)
                    .setUserAgent("Web")
                    .setException(ex == null ? "" : Exceptions.getStackTraceAsString(ex))
                    .setCreateBy(userId)
                    .setCreateDate(System.currentTimeMillis())
                    .setQuarter(QuarterUtils.getSeasonQuarter(new Date()))
                    .build();
            logUtils.logSender.send(logMessage);
        }catch (Exception e){
            log.error("[LogUtils.saveLog] title:{} ,method:{} ,user:{}",title,method,userId,e);
        }
    }

    /**
     * 将web请求参数转成字符形式，如a=1&b=2&c=3
     * @param map
     * @return
     */
    public static String getRequestParameterString(Map<String, String[]> map){
        if(map == null || map.size() == 0){
            return "";
        }
        StringBuilder params = new StringBuilder();
        map.forEach((k,v)->{
            if(v.length>0) {
                params.append("&").append(k).append("=").append(URLEncoder.encode(v[0]));
            }else{
                params.append("&").append(k).append("=");
            }

        });
        return params.toString().substring(1);
    }

}
