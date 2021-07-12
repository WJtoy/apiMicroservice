package com.wolfking.jeesite.ms.tmall.md.utils;

import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import com.wolfking.jeesite.ms.tmall.md.service.B2bCustomerMapService;

import java.util.List;

/**
 * B2B关联工具类
 */
public class B2BMapUtils {

    private static B2bCustomerMapService customerMapService = SpringContextHolder.getBean(B2bCustomerMapService.class);

    /**
     * 为实体设置创建者、创建时间、更新者、更新时间
     */
    public static List<B2bCustomerMap> getAllShopList(int dataSource) {
        return customerMapService.getAllShopList(dataSource);
    }






}
