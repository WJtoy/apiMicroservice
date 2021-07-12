/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具类
 *
 * @author ThinkGem
 * @version 2013-05-22
 *
 * @date 2020-05-05
 * 将公用方法迁移到com.kkl.utils.StringUtils
 */
@Slf4j
public class StringUtils2 {



    /**
     * Gson将json以字符方式存储redis时，进行了转换
     * 在头和尾增加了“"”,且加了转义"\"
     * @param json
     * @return
     */
    public static String fromGsonString(String json) {
        if (StrUtil.isBlank(json)) {
            return "";
        }
        StringBuffer jsonsb = new StringBuffer(2000);
        jsonsb.append(json);
        if (jsonsb.substring(0, 1).equalsIgnoreCase("\"")) {
            jsonsb.deleteCharAt(0);
            jsonsb.deleteCharAt(jsonsb.length() - 1);
        }
        return jsonsb.toString().replace("\\", "");
    }

    /**
     * 读取产品标准名称，去除"T","S"
     * @param name 原产品名称
     * @return  标准名称
     */
    public static String getStandardProductName(String name){
        if(StrUtil.isBlank(name)){
            return name;
        }
        return StrUtil.removePrefix(StrUtil.removeAll(name.toUpperCase(),'T','S'),"VIP");
    }
}
