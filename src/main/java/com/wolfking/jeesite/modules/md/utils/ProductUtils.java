/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.md.utils;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.ms.providermd.service.MSProductCategoryNewService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
public class ProductUtils {
    private static ProductService productService = SpringContextHolder.getBean(ProductService.class);
    private static MSProductCategoryNewService msProductCategoryNewService = SpringContextHolder.getBean(MSProductCategoryNewService.class);
    ;

    /**
     * 获取产品列表
     *
     * @return
     */
    public static Map<Long, Product> getAllProductMap() {
        List<Product> productList = productService.findAllList();
        Map<Long, Product> productMap = Maps.newHashMap();
        for (Product item : productList) {
            productMap.put(item.getId(), item);
        }
        return productMap;
    }

    public static Map<Long, ProductCategory> getAllProductCategoryMap() {
        //List<ProductCategory> productCategories = msProductCategoryService.findAllList();
        List<ProductCategory> productCategories = msProductCategoryNewService.findAllListForMDWithEntity();
        Map<Long, ProductCategory> productCategoryMap;
        if (productCategories != null && !productCategories.isEmpty()) {
            productCategoryMap = productCategories.stream().collect(Collectors.toMap(LongIDBaseEntity::getId, i -> i));
        } else {
            productCategoryMap = Maps.newHashMap();
        }
        return productCategoryMap;
    }

}
