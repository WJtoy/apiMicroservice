package com.wolfking.jeesite.modules.utils;

import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.utils.SpringContextHolder;

public class ApiPropertiesUtils {

    private static WebProperties web = SpringContextHolder.getBean(WebProperties.class);

    public static WebProperties getWeb() {
        return web;
    }

    public static WebProperties.PageProperties getPage() {
        return web.getPage();
    }
    
    public static WebProperties.UserFilesProperties getUserFiles() {
        return web.getUserFiles();
    }

    public static WebProperties.CacheProperties getCache() {
        return web.getCache();
    }

    public static WebProperties.SequenceProperties getSequence() {
        return web.getSequence();
    }
    
    

}
