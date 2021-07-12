/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.config;

import com.google.common.collect.Maps;
import com.google.protobuf.Api;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.PropertiesLoader;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.bind.RelaxedPropertyResolver;

import java.util.Map;

/**
 * 全局配置类
 *
 * @author ThinkGem
 * @author ryan
 * 取消getSiteCode()及getSiteName()方法
 * @version 2014-06-25
 * @date 2019-06-27
 */
@Slf4j
public class Global {

    public static final int ONE_SECOND = 1 * 1000;

    public static final int ONE_MINUTE = 1 * 60 * 1000;

    static RelaxedPropertyResolver resolver;
    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("bootstrap.yml");

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     * ${fns:getConfig('adminPath')}
     */
//    public static String getConfig(String key) {
//        String value = map.get(key);
//        if (value == null) {
//            try {
//                value = resolver.getProperty(key);
////                value = loader.getProperty(key);
//                if (StringUtils.isBlank(value)) {
//                    throw new RuntimeException("value null");
//                }
//                map.put(key, value);
//            } catch (Exception e) {
//                value = loader.getProperty(key);
//                map.put(key, value != null ? value : StringUtils.EMPTY);
//            }
//        }
//        return value;
//    }

    /**
     * 获取上传文件的根目录(/servlet/Upload/)
     * 绝对路径
     *
     * @return
     */
    public static String getUploadfilesDir() {
        String dir ="D:"+ ApiPropertiesUtils.getWeb().getUserFiles().getUploadDir();
//        String dir = getConfig("userfiles.uploaddir");
        if (StringUtils.isBlank(dir)) {
            return "";
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    public static String getJdbcType() {
        return "mysql";
    }
}
