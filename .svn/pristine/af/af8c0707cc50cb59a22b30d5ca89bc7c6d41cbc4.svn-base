package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerPrice;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerPriceFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSCustomerPriceFeignFallbackFactory.class)
public interface MSCustomerPriceFeign {

    /**
     * 获得某客户的所有价格清单
     * @param customerId 客户id
     * @param delFlag 0:启用的价格 1:停用的价格 2:待审核的价格 null:所有
     * @return
     */
    /*@GetMapping("/customerPrice/findPrices")
    MSResponse<List<MDCustomerPriceDto>> findPrices(@RequestParam("customerId") Long customerId, @RequestParam("delFlag") Integer delFlag);*/

    @GetMapping("/customerPriceNew/findPrices")
    MSResponse<List<MDCustomerPriceDto>> findPricesNew(@RequestParam("customerId") Long customerId, @RequestParam("delFlag") Integer delFlag);

    /**
     * 获得某客户的生效价格清单用于缓存
     * @param id 客户id
     * @return
     */
    @GetMapping("/customerPriceNew/findCustomerPriceWithAssociated")
    MSResponse<List<MDCustomerPriceDto>> findCustomerPriceWithAssociated(@RequestParam("id") Long id);


    /**
     * 按id列表获得价格列表(for cache)
     * @param ids 客户价格id列表
     * @return
     */
    @PostMapping("/customerPriceNew/findPricesByPriceIds")
    MSResponse<List<MDCustomerPriceDto>> findPricesByPriceIds(@RequestBody List<Long> ids);


    /**
     * 按多个id获得客户下价格
     * @param customerIds 客户id列表
     * @param productId 产品id
     * @param serviceTypeId 服务类型id
     * @return
     */
    /*@PostMapping("/customerPrice/findPricesByCustomers")
    MSResponse<List<MDCustomerPrice>> findPricesByCustomers(@RequestParam("customerIds") List<Long> customerIds,@RequestParam("productId") Long productId,
                                                   @RequestParam("serviceTypeId") Long serviceTypeId);*/

    @PostMapping("/customerPriceNew/findPricesByCustomers")
    MSResponse<List<MDCustomerPrice>> findPricesByCustomersNew(@RequestParam("customerIds") List<Long> customerIds,@RequestParam("productId") Long productId,
                                                            @RequestParam("serviceTypeId") Long serviceTypeId);

    /**
     * 获得待审核价格清单
     * @param customerPriceDto 查询条件
     * @return
     */
    @PostMapping("/customerPriceNew/findApprovePriceList")
    MSResponse<MSPage<MDCustomerPriceDto>> findApprovePriceList(@RequestBody MDCustomerPriceDto customerPriceDto);


    /**
     * 根据客户id从缓存中获取客户价格
     * @param id 客户id
     * @return
     */
    @GetMapping("customerPriceNew/findCustomerPriceWithAssociatedFromCache")
    MSResponse<List<MDCustomerPriceDto>> findCustomerPriceWithAssociatedFromCache(@RequestParam("id") Long id);


    /**
     * 添加客户价格
     * @param mdCustomerPrice 查询条件
     * @return
     */
    /*@PostMapping("/customerPrice/insert")
    MSResponse<Integer> insert(@RequestBody MDCustomerPrice mdCustomerPrice);*/

    /**
     * 添加客户价格New
     * @param mdCustomerPrice
     * @return
     */
    @PostMapping("/customerPriceNew/insert")
    MSResponse<Integer> insert(@RequestBody MDCustomerPrice mdCustomerPrice);


    /**
     * 审核价格
     * @param ids 客户价格id
     * @param updateById 审核人
     * @param updateDate 审核时间
     * @return
     */
    @PutMapping("/customerPriceNew/approvePrices")
    MSResponse<Integer> approvePrices(@RequestParam("ids") List<Long> ids,@RequestParam("updateById") Long updateById,
                                      @RequestParam("updateDate") Long updateDate);

    /**
     * 获得某客户的所有价格清单
     * @param id 客户价格id
     * @param delFlag 0:启用的价格 1:停用的价格 2:待审核的价格 null:所有
     * @return
     */
    /*@GetMapping("/customerPrice/getPrice")
    MSResponse<MDCustomerPriceDto> getPrice(@RequestParam("id") Long id,@RequestParam("delFlag") Integer delFlag);*/

    @GetMapping("/customerPriceNew/getPrice")
    MSResponse<MDCustomerPriceDto> getPriceNew(@RequestParam("id") Long id,@RequestParam("delFlag") Integer delFlag);

    /**
     * 修改价格
     * @param paramMap
     * @return
     */
    /*@PutMapping("/customerPrice/updatePriceByMap")
    MSResponse<Integer> updatePriceByMap(@RequestBody HashMap<String,Object> paramMap);*/

    @PutMapping("/customerPriceNew/updatePriceByMap")
    MSResponse<Integer> updatePriceByMapNew(@RequestBody HashMap<String,Object> paramMap);

    /**
     * 修改价格
     * @param mdCustomerPrice
     * @return
     */
    /*@PutMapping("/customerPrice/updateOnePrice")
    MSResponse<Integer> update(@RequestBody MDCustomerPrice mdCustomerPrice);*/

    @PutMapping("/customerPriceNew/update")
    MSResponse<Integer> update(@RequestBody MDCustomerPrice mdCustomerPrice);


    /**
     * 删除客户产品价格
     * @param customerId
     * @param productIds
     */
    @DeleteMapping("/customerPriceNew/deletePricesByCustomerAndProducts")
    MSResponse<Integer> deletePricesByCustomerAndProducts(@RequestParam("customerId") Long customerId,@RequestParam("productIds") List<Long> productIds);


    /**
     * 批量添加或者修改
     * @param customerPriceList
     */
    /*@PostMapping("customerPrice/batchInsertOrUpdate")
    MSResponse<Integer> insertOrUpdateBatch(@RequestBody List<MDCustomerPrice> customerPriceList);*/

    @PostMapping("customerPriceNew/batchInsertOrUpdate")
    MSResponse<Integer> insertOrUpdateBatchNew(@RequestBody List<MDCustomerPrice> customerPriceList);



    /**
     * 批量添加
     * @param customerPriceList
     */
    @PostMapping("customerPriceNew/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDCustomerPrice> customerPriceList);

}
