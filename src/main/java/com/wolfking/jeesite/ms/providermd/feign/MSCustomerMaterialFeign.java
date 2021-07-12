package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomerMaterial;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerMaterialFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSCustomerMaterialFeignFallbackFactory.class)
public interface MSCustomerMaterialFeign {
    /**
     * 根据id获取
     * @param id
     * @return
     */
    @GetMapping("/customerMaterial/getById/{id}")
    MSResponse<MDCustomerMaterial> getById(@PathVariable("id") Long id);

    /**
     * 分页查询
     * @param mdCustomerMaterial
     * @return
     */
    @PostMapping("/customerMaterial/findList")
    MSResponse<MSPage<MDCustomerMaterial>> findList(@RequestBody MDCustomerMaterial mdCustomerMaterial);

    /**
     * 根据客户品类产品分页查询
     * @param mdCustomerMaterial
     * @return
     */
    @PostMapping("/customerMaterial/findListByProductAndCustomerIdAndMaterial")
    MSResponse<MSPage<MDCustomerMaterial>> findListByProductAndCustomerIdAndMaterial(@RequestBody MDCustomerMaterial mdCustomerMaterial);
    /**
     * 批量添加
     * @param mdMaterialList
     * @return
     */
    @PostMapping("/customerMaterial/batchOperation")
    MSResponse<Integer> batchInsert(@RequestBody List<MDCustomerMaterial> mdMaterialList);

    /**
     * 删除
     * @param mdCustomerMaterial
     * @return
     */
    @DeleteMapping("/customerMaterial/delete")
    MSResponse<Integer> delete(@RequestBody MDCustomerMaterial mdCustomerMaterial);

    /**
     * 根据客户id,产品id,配件id获取客户配件
     * @param customerId,productId,materialId
     * @return
     */
    @GetMapping("/customerMaterial/getCustomerMaterialByCustomerAndProductAndMaterial")
    MSResponse<MDCustomerMaterial> getCustomerMaterialByCustomerAndProductAndMaterial(@RequestParam("customerId") Long customerId,
                                                                                      @RequestParam("productId") Long productId,
                                                                                      @RequestParam("materialId") Long material);

    /**
     * 根据客户id,产品id客户配件
     * @param customerId,productId
     * @return
     */
    @GetMapping("/customerMaterial/findCustomerMaterialByCustomerIdAndProductId")
    MSResponse<List<MDCustomerMaterial>> findListByCustomerAndProduct(@RequestParam("customerId") Long customerId,
                                                                      @RequestParam("productId") Long productId);


    /**
     * 根据客户id和产品id删除客户配件
     * @param customerId 客户id
     * @param productId  产品id
     * @return
     */
    @PostMapping("/customerMaterial/deleteByCustomerAndProduct")
    MSResponse<Integer> deleteByCustomerAndProduct(@RequestParam("customerId") Long customerId, @RequestParam("productId") Long productId);


    /**
     * 据客户ID、产品ID、配件IDS获取配件信息
     * @param customerId,productId,materialIds
     * @return
     */
    @GetMapping("/customerMaterial/findListByCustomerIdAndMaterialIdsFromCache")
    MSResponse<List<MDCustomerMaterial>> findListByCustomerIdAndMaterialIdsFromCache(@RequestParam("customerId") Long customerId,
                                                                                     @RequestBody List<NameValuePair<Long, Long>> NameValuePairList);
    /**
     * 从客户获取配件并更新到系统中去
     * @param customerMaterials
     * @return
     */
    @PostMapping("/customerMaterial/updateCustomerMaterials")
    MSResponse<String> updateCustomerMaterials(@RequestBody List<MDCustomerMaterial> customerMaterials);

    /**
     * 根据客户id和产品id，客户型号从客户配件中获取产品id及配件id列表
     * @param customerId
     * @param productId
     * @return
     */
    @GetMapping("/customerMaterial/findProductMaterialByCustomerAndProduct")
    MSResponse<List<MDProductMaterial>> findProductMaterialByCustomerAndProduct(@RequestParam("customerId") Long customerId, @RequestParam("productId") Long productId, @RequestParam("customerModelId") String customerModelId);

    /**
     * 根据客户id和产品id、客户型号列表从客户配件中获取产品id及配件id列表
     * @param customerId
     * @param nameValuePairs long-产品id，String- customerModel
     * @return
     */
    @GetMapping("/customerMaterial/findProductMaterialByCustomerAndProductIds")
    MSResponse<List<MDProductMaterial>> findProductMaterialByCustomerAndProductIds(@RequestParam("customerId") Long customerId, @RequestBody List<NameValuePair<Long,String>> nameValuePairs);

    /**
     * 根据客户id，产品id，配件id，客户型号(可为空)列表获取客户配件信息
     * @param customerMaterials
     * @return
     */
    @PostMapping("/customerMaterial/findListByCustomerMaterial")
    MSResponse<List<MDCustomerMaterial>> findListByCustomerMaterial(@RequestBody List<MDCustomerMaterial> customerMaterials);

}
