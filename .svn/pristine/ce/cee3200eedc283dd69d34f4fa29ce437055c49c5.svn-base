package com.wolfking.jeesite.modules.utils;

import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0
 * 好评单工具类
 * @date 2020/4/28 4:30 下午
 */
@Slf4j
public class PraiseUtils {

    /**
     * 计算好评单费用
     *
     * @param baseCost  底价
     * @param upperCost 上限，应大于等于底价
     * @param discount  折扣，如0.1，网点最终费用是x0.9，建议使用cost-cost*0.1方式，否则可能造成小数点后计算有出入
     * @param items     已上传项，包含基本费用  NameValuePair<key:String,value:double(费用标准)>
     * @return 好评费 NameValuePair<key:厂商费用,value:网点费用>
     */
    public static NameValuePair<Double, Double> calculatePraiseCost(double baseCost, double upperCost, double discount, List<NameValuePair<String, Double>> items) {
        //double spCost = baseCost - CurrencyUtil.round2(baseCost * discount);
        double spCost = baseCost - NumberUtils.doubleScale(baseCost * discount, 2);
        //上限小于底价
        if (upperCost < baseCost) {
            return new NameValuePair<Double, Double>(0.00, 0.00);
        }
        //无上传项目，按底价计算
        if (items == null || items.size() == 0) {
            spCost = NumberUtils.doubleScale(spCost, 2, false);
            return new NameValuePair<Double, Double>(baseCost, spCost);
        }
        double cost = baseCost;
        for (NameValuePair<String, Double> kv : items) {
            cost = cost + kv.getValue();
        }
        spCost = cost - NumberUtils.doubleScale(cost * discount, 2);
        ////上限不限制
        //if(upperCost == 0){
        //    return new NameValuePair<Double,Double>(cost,spCost);
        //}
        //超过上限
        if (cost > upperCost) {
            spCost = upperCost - NumberUtils.doubleScale(upperCost * discount, 2);
            spCost = NumberUtils.doubleScale(spCost, 2, false);
            return new NameValuePair<Double, Double>(upperCost, spCost);
        }
        //未超
        spCost = NumberUtils.doubleScale(spCost, 2, false);
        return new NameValuePair<Double, Double>(cost, spCost);
    }

}
