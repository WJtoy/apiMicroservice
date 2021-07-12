package com.wolfking.jeesite.ms.globalmapping.service;

import com.kkl.kklplus.entity.md.GlobalMappingSyncTypeEnum;
import com.wolfking.jeesite.ms.globalmapping.dao.ProductCategoryServicePointMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class ProductCategoryServicePointMappingService {

    @Resource
    private ProductCategoryServicePointMapper productCategoryServicePointMapper;

    @Transactional()
    public void saveProductCategoryServicePointMapping(GlobalMappingSyncTypeEnum syncType, Long servicePointId, List<Long> productCategoryIds) {
        /*
        // mark on 2020-6-12
        if (syncType != null && servicePointId != null && servicePointId > 0 && productCategoryIds != null && !productCategoryIds.isEmpty()) {
            productCategoryIds = productCategoryIds.stream().filter(i -> i != null && i > 0).distinct().collect(Collectors.toList());
            if (syncType == GlobalMappingSyncTypeEnum.ADD) {
                if (!productCategoryIds.isEmpty()) {
                    productCategoryServicePointMapper.insertProductCategoryIds(servicePointId, productCategoryIds);
                }
            } else if (syncType == GlobalMappingSyncTypeEnum.UPDATE) {
                productCategoryServicePointMapper.deleteByServicePointId(servicePointId);
                if (!productCategoryIds.isEmpty()) {
                    productCategoryServicePointMapper.insertProductCategoryIds(servicePointId, productCategoryIds);
                }
            } else if (syncType == GlobalMappingSyncTypeEnum.DELETE) {
                productCategoryServicePointMapper.deleteByServicePointId(servicePointId);
            }
        }
        */
    }

    @Transactional()
    public void deleteProductCategoryServicePointMapping(Long servicePointId) {
        if (servicePointId != null && servicePointId > 0) {
           // productCategoryServicePointMapper.deleteByServicePointId(servicePointId); // mark on 2020-6-12
        }
    }

    /**
     * 根据产品类目id获取网点id列表
     * @param productCategoryId
     * @return
     */
    public List<Long> findListByProductCategoryId(Long productCategoryId) {
        //return productCategoryServicePointMapper.findListByProductCategoryId(productCategoryId);  // mark on 2020-6-12
        return null;
    }

    /**
     * 批量按网点id检查网点是否有指定品类权限
     * @param sids  网点id列表  不超过100个
     * @param productCategoryId 品类id
     * @return
     */
    public List<Long> findListByProductCategoryIdAndServicePointIds(List<Long> sids,Long productCategoryId) {
        if(CollectionUtils.isEmpty(sids) || productCategoryId == null || productCategoryId.longValue() <= 0){
            return null;
        }
        //return productCategoryServicePointMapper.findListByProductCategoryIdAndServicePointIds(sids,productCategoryId);  // mark on 2020-6-12
        return null;
    }

}
