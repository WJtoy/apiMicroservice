package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BProductMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BProductMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BProductMappingFeignFallbackFactory.class)
public interface B2BProductMappingFeign {

    @GetMapping("/b2BProductMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BProductMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

    @GetMapping("/b2BProductMapping/getListByCustomerCategoryIds")
    MSResponse<List<B2BProductMapping>> getListByCustomerCategoryIds(@RequestParam("dataSource") Integer dataSource,
                                                                     @RequestParam("customerCategoryIds") List<String> customerCategoryIds);

    @GetMapping("/b2BProductMapping/getListByProductIds")
    MSResponse<Map<Long, List<String>>> getListByProductIds(@RequestParam("dataSource") Integer dataSource,
                                                            @RequestParam("productIds") List<Long> productIds);

    @GetMapping("/b2BProductMapping/get/{id}")
    MSResponse<B2BProductMapping> getProductMappingById(@PathVariable("id") Long id);

    @PostMapping("/b2BProductMapping/getList")
    MSResponse<MSPage<B2BProductMapping>> getProductMappingList(@RequestBody B2BProductMapping productMapping);

    @PostMapping("/b2BProductMapping/insert")
    MSResponse<B2BProductMapping> insertProductMapping(@RequestBody B2BProductMapping productMapping);

    @PostMapping("/b2BProductMapping/update")
    MSResponse<Integer> updateProductMapping(@RequestBody B2BProductMapping productMapping);

    @PostMapping("/b2BProductMapping/delete")
    MSResponse<Integer> deleteProductMapping(@RequestBody B2BProductMapping productMapping);

    @PostMapping("/b2BProductMapping/deleteByDataSource/{dataSource}")
    MSResponse<Integer> deleteByDataSource(@PathVariable("dataSource") Integer dataSource);

    @PostMapping("/b2BProductMapping/insertBatch")
    MSResponse<List<B2BProductMapping>> insertBatch(@RequestParam("strJson") String strJson);

    @PostMapping("/b2BProductMapping/syncByDataSource")
    MSResponse<Integer> syncByDataSource(@RequestBody List<B2BProductMapping> productMappingList,@RequestParam("createById") Long createById);

}
