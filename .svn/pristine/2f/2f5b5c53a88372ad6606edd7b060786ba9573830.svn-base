package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.InsurancePrice;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.ms.providermd.service.MSProductCategoryNewService;
import com.wolfking.jeesite.ms.providermd.service.MSProductInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jeff on 2017/4/24.
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class InsurancePriceService extends LongIDBaseService {

    @SuppressWarnings("rawtypes")
    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private MSProductCategoryNewService msProductCategoryNewService;


    @Autowired
    private MSProductInsuranceService msProductInsuranceService;

    public List<InsurancePrice> findAllList() {
        List<InsurancePrice> list = msProductInsuranceService.findAllList();
        if (list != null && list.size() > 0) {
            // 调用ProductCategory获取数据
            //List<ProductCategory> productCategoryList = msProductCategoryService.findAllList();  //mark on 2020-4-1
            List<ProductCategory> productCategoryList = msProductCategoryNewService.findAllListForMDWithEntity();  //add on 2020-4-1
            Map<Long, ProductCategory> productCategoryMap = Maps.newHashMap();
            if (productCategoryList != null && !productCategoryList.isEmpty()) {
                productCategoryMap = productCategoryList.stream().collect(Collectors.toMap(ProductCategory::getId, r -> r));
            }
            Map<Long, ProductCategory> finalProductCategoryMap = productCategoryMap;
            list.stream().forEach(insurancePrice -> {
                ProductCategory productCategory = finalProductCategoryMap.get(insurancePrice.getCategory().getId());
                insurancePrice.getCategory().setName(productCategory == null ? "" : productCategory.getName());
            });
        }

        return list;

    }
}
