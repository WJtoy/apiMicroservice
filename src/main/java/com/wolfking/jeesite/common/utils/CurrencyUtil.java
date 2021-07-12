/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.common.utils;

import java.util.StringTokenizer;


public class CurrencyUtil {
    private String num;
    private String prefix;
    private String suffix;
    private static String STR = "0123456789.";

    public CurrencyUtil(String number) {
        num = number;

        // 字符合法性判断
        if (CurrencyUtil.isAllRight(num)) {
            spit(num);
        } else {
            System.out.println("非法数据！");
        }
    }

    /**
     * @function 整数部分、小数部分初始化
     */
    private void spit(String num) {
        StringTokenizer st = new StringTokenizer(num, ".");

        if (st.countTokens() == 1)
            prefix = st.nextToken();
        else if (st.countTokens() == 2) {
            prefix = st.nextToken();
            suffix = st.nextToken();
        }
    }

    /**
     * @function 判断数据是否合法
     */
    public static boolean isAllRight(String num) {
        boolean flag = true;
        int i;                // 正负数
        int count = 0;        // 计算小数点个数

        // 不为空
        if (num != null && !num.equals("")) {
            // 正负数
            if (num.startsWith("-"))
                i = 1;
            else
                i = 0;

            for (; i < num.length() - 1; i++) {
                if (STR.indexOf(num.charAt(i)) == -1) {
                    flag = false;
                    break;
                }

                if ((num.charAt(i) + "").equals("."))
                    count++;
            }

            // 小数点后没数据
            if (num.endsWith("."))
                flag = false;

            // 不止一個小數點
            if (count > 1)
                flag = false;
        }

        return flag;
    }

    /**
     * 四舍五入,保留两位小数
     *
     * @param d
     * @return
     */
    public static double round2(double d) {
        return Math.round(d * 100) * 0.01d;
    }

}
