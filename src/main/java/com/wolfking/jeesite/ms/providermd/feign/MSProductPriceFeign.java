package com.wolfking.jeesite.ms.providermd.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductPrice;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductPriceFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductPriceFeignFallbackFactory.class)
public interface MSProductPriceFeign {
    @PostMapping("/productPrice/findList")
    MSResponse<MSPage<MDProductPrice>> findList(@RequestBody MDProductPrice mdProductPrice);

    @GetMapping("/productPrice/findAllList")
    MSResponse<List<MDProductPrice>> findAllList();

    @GetMapping("/productPrice/findGroupList")
    MSResponse<List<MDProductPrice>> findGroupList(@RequestParam("priceType") Integer priceType, @RequestParam("productIds") List<Long> productIds, @RequestParam(value = "serviceTypeIds") List<Long> serviceTypeIds, @RequestParam("servicePointId") Long servicePointId, @RequestParam("customerId") Long customerId);

    @GetMapping("/productPrice/findAllGroupList")
    MSResponse<List<MDProductPrice>> findAllGroupList(@RequestParam("priceType") Integer priceType, @RequestParam("productIds") List<Integer> productIds);

    @GetMapping("/productPrice/findAllPriceList")
    MSResponse<List<MDProductPrice>> findAllPriceList(@RequestParam("priceType") Integer priceType, @RequestParam("productIds") List<Integer> productIds);

    @GetMapping("/productPrice/getById/{id}")
    MSResponse<MDProductPrice> getById(@PathVariable("id") Long id);

    @GetMapping("/productPrice/getIdByProductIdAndServiceTypeIdAndPriceType/{productId}/{serviceTypeId}/{priceType}")
    MSResponse<Long> getIdByProductIdAndServiceTypeIdAndPriceType(@PathVariable("productId") Long productId, @PathVariable("serviceTypeId") Long serviceTypeId, @PathVariable("priceType") Integer priceType);

    /**
     * 根据产品id，服务类型id，第几轮价格获取厂商指导价  //add on 2019-11-26
     * @param productId
     * @param serviceTypeId
     * @param priceType
     * @return
     */
    @GetMapping("/productPrice/getPriceByProductIdAndServiceTypeIdAndPriceType")
    MSResponse<Double> getPriceByProductIdAndServiceTypeIdAndPriceType(@RequestParam("priceType") Integer priceType, @RequestParam("productId") Long productId, @RequestParam("serviceTypeId") Long serviceTypeId);

    /**
     * 根据产品id，服务类型id，第几轮价格获取网点参考价格
     * @param productId
     * @param serviceTypeId
     * @param priceType
     * @return
     */
    @GetMapping("/productPrice/getEngineerPriceByProductIdAndServiceTypeIdAndPriceType")
    MSResponse<MDProductPrice> getEngineerPriceByProductIdAndServiceTypeIdAndPriceType(@RequestParam("priceType") Integer priceType, @RequestParam("productId") Long productId, @RequestParam("serviceTypeId") Long serviceTypeId);


    @PostMapping("/productPrice/insert")
    MSResponse<Integer> insert(@RequestBody MDProductPrice mdProductPrice);

    @PutMapping("/productPrice/update")
    MSResponse<Integer> update(@RequestBody MDProductPrice mdProductPrice);

    @DeleteMapping("/productPrice/delete")
    MSResponse<Integer> delete(@RequestBody MDProductPrice mdProductPrice);

    @PostMapping("/productPrice/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDProductPrice> mdProductPriceList);
}
