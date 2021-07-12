package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductCategoryServicePointFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductCategoryServicePointFallbackFactory.class)
public interface MSProductCategoryServicePointFeign {
    /**
     * 修改网点产品类目映射
     * @param servicePointId 网点id
     * @param productCategoryIds  产品类目id列表
     * @return
     */
    @PutMapping("/productCategoryServicePoint/update")
    MSResponse<Integer> update(@RequestParam("servicePointId") Long servicePointId,
                               @RequestParam("productCategoryIds") List<Long> productCategoryIds);


    @GetMapping("/productCategoryServicePoint/findListByServicePointIdFromCacheForSD/{servicePointId}")
    MSResponse<List<Long>> findListByServicePointIdFromCacheForSD(@PathVariable("servicePointId") Long servicePointId);

    /**
     * 根据网点Id和品类id从缓存中判断网点品类是否存在
     * @param servicePointId
     * @param productCategoryId
     * @return
     */
    @GetMapping("/productCategoryServicePoint/existByPointIdAndCategoryIdFromCacheForSD")
    MSResponse<Boolean> existByPointIdAndCategoryIdFromCacheForSD(@RequestParam("servicePointId") Long servicePointId, @RequestParam("productCategoryId") Long productCategoryId);

    /**
     * 根据网点id列表和品类id获取网点id列表
     * @param sids   网点id列表
     * @param productCategoryId  品类id
     * @return
     */
    @PostMapping("/productCategoryServicePoint/findListByProductCategoryIdAndServicePointIds")
    MSResponse<List<Long>> findListByProductCategoryIdAndServicePointIds(@RequestBody List<Long> sids, @RequestParam("productCategoryId") Long productCategoryId);
}
