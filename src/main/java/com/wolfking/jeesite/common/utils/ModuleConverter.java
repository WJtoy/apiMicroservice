package com.wolfking.jeesite.common.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 获取日志模块的名称
 * @author Ryan Lu
 * @version 1.0.0
 * @date 2019-08-28 12:41
 */
public class ModuleConverter extends ClassicConverter {
    private static final int MAX_LENGTH = 20;

    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLoggerName().length() > MAX_LENGTH) {
            return "";
        } else {
            return event.getLoggerName();
        }
    }
}
