package com.wolfking.jeesite.ms.providermd.service;

import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryServicePointFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSProductCategoryServicePointService {
    @Autowired
    private MSProductCategoryServicePointFeign productCategoryServicePointFeign;

    /**
     * 根据网点id查询网点品类
     *
     * @param servicePointId
     * @return
     */
    public List<Long> findListByServicePiontIdFromCacheForSD(Long servicePointId) {
        return MDUtils.findListUnnecessaryConvertType(()->productCategoryServicePointFeign.findListByServicePointIdFromCacheForSD(servicePointId));
    }

}
