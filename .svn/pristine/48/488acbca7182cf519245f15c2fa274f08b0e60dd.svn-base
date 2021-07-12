package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductCategoryBrand;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductCategoryBrandFeignFallbackFactory;
import com.wolfking.jeesite.ms.tmall.md.entity.B2BCategoryBrand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductCategoryBrandFeignFallbackFactory.class)
public interface MSProductCategoryBrandFeign {
    
    @GetMapping("/productCategoryBrand/getCategoryBrandMap")
    MSResponse<List<B2BCategoryBrand>> getCategoryBrandMap(@RequestParam("categoryIds") String categoryIds);

    /**
     * 获取产品的类型与品牌的对应关系
     * @param categoryIds
     * @return
     */
    @PostMapping("/productCategoryBrand/findCategoryBrandMap")
    MSResponse<List<B2BCategoryBrand>> findCategoryBrandMap(@RequestParam("categoryIds") List<Long> categoryIds);

    @GetMapping("/productCategoryBrand/getBrandIdsByCategoryId/{categoryId}")
    MSResponse<List<Long>> getBrandIdsByCategoryId(@PathVariable("categoryId") Long categoryId);

    @GetMapping("/productCategoryBrand/findAllList")
    MSResponse<List<MDProductCategoryBrand>> findAllList();

    @PostMapping("/productCategoryBrand/findList")
    MSResponse<MSPage<MDProductCategoryBrand>> findList(@RequestBody MDProductCategoryBrand mdProductCategoryBrand);

    @PostMapping("/productCategoryBrand/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDProductCategoryBrand> mdProductCategoryBrandList);

    @DeleteMapping("/productCategoryBrand/delete/{id}")
    MSResponse<Integer> delete(@PathVariable("id") Long id);

    @DeleteMapping("/productCategoryBrand/deleteByCategoryId/{categoryId}")
    MSResponse<Integer> deleteByCategoryId(@PathVariable("categoryId") Long categoryId);
}
