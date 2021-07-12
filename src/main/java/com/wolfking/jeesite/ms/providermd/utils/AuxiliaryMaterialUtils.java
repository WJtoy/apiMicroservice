package com.wolfking.jeesite.ms.providermd.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.entity.common.MSBase;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialCategory;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.ms.providermd.service.AuxiliaryMaterialCategoryService;
import com.wolfking.jeesite.ms.providermd.service.AuxiliaryMaterialItemService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/***
 *
 *  Created by Zhoucy on 2019/7/12.
 */
public class AuxiliaryMaterialUtils {

    private static AuxiliaryMaterialCategoryService auxiliaryMaterialCategoryService = SpringContextHolder.getBean(AuxiliaryMaterialCategoryService.class);
    private static AuxiliaryMaterialItemService auxiliaryMaterialItemService = SpringContextHolder.getBean(AuxiliaryMaterialItemService.class);

    //region 产品辅材或服务项目

    public static Map<Long, List<MDAuxiliaryMaterialItem>> getAuxiliaryMaterialCategoryAndItemMap(List<Long> productIds) {
        Map<Long, List<MDAuxiliaryMaterialItem>> result = Maps.newHashMap();
        if (productIds != null && !productIds.isEmpty()) {
            List<String> tempProductIds = productIds.stream().filter(Objects::nonNull).distinct().map(Object::toString).collect(Collectors.toList());
            List<MDAuxiliaryMaterialItem> items = auxiliaryMaterialItemService.getListByProductId(tempProductIds);
            if (items != null && !items.isEmpty()) {
                List<MDAuxiliaryMaterialCategory> categories = auxiliaryMaterialCategoryService.findAllList();
                Map<Long, MDAuxiliaryMaterialCategory> categoryMap = categories.stream().collect(Collectors.toMap(MSBase::getId, i -> i));
                Long productId;
                MDAuxiliaryMaterialCategory category;
                for (MDAuxiliaryMaterialItem item : items) {
                    productId = item.getProductId();
                    if (item.getCategory() != null && item.getCategory().getId() != null && productId != null && productIds.contains(productId)) {
                        category = categoryMap.get(item.getCategory().getId());
                        if (category != null) {
                            item.setCategory(category);
                            if (result.containsKey(productId)) {
                                result.get(productId).add(item);
                            } else {
                                result.put(productId, Lists.newArrayList(item));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Map<Long, MDAuxiliaryMaterialItem> getAuxiliaryMaterialItemMap(List<Long> productIds) {
        Map<Long, MDAuxiliaryMaterialItem> map = Maps.newHashMap();
        if (productIds != null && !productIds.isEmpty()) {
            List<String> tempProductIds = productIds.stream().filter(Objects::nonNull).distinct().map(Object::toString).collect(Collectors.toList());
            List<MDAuxiliaryMaterialItem> items = auxiliaryMaterialItemService.getListByProductId(tempProductIds);
            map = items.stream().filter(i -> productIds.contains(i.getProductId())).collect(Collectors.toMap(MSBase::getId, i -> i));
        }
        return map;
    }

    public static Map<Long, MDAuxiliaryMaterialCategory> getAuxiliaryMaterialCategoryMap() {
        Map<Long, MDAuxiliaryMaterialCategory> map = Maps.newHashMap();
        List<MDAuxiliaryMaterialCategory> categories = auxiliaryMaterialCategoryService.findAllList();
        if (categories != null && !categories.isEmpty()) {
            map = categories.stream().collect(Collectors.toMap(MSBase::getId, i -> i));
        }
        return map;
    }


    //endregion 产品辅材或服务项目

}
