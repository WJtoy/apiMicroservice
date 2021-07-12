package com.wolfking.jeesite.ms.providermd.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductTimeLiness;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductTimeLinessFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductTimeLinessFeignFallbackFactory.class)
public interface MSProductTimeLinessFeign {

    @GetMapping("/productTimeLiness/findAllList")
    MSResponse<List<MDProductTimeLiness>> findAllList();

    @GetMapping("/productTimeLiness/getPrices/{productCategoryId}")
    MSResponse<List<MDProductTimeLiness>> getPrices(@PathVariable("productCategoryId") Long productCategoryId);

    @DeleteMapping("/productTimeLiness/deleteByProductCategoryId/{productCategoryId}")
    MSResponse<Integer> deleteByProductCategoryId(@PathVariable("productCategoryId") Long productCategoryId);

    @PostMapping("/productTimeLiness/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDProductTimeLiness> mdProductTimeLinessList);
}
