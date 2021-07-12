package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductPicMapping;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerProductPicMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name="provider-md", fallbackFactory = MSCustomerProductPicMappingFeignFallbackFactory.class)
public interface MSCustomerProductPicMappingFeign {
    /**
     * 根据id获取
     * @param id
     * @return
     */
    @GetMapping("/customerProductPic/getById/{id}")
    MSResponse<MDProductPicMapping> getById(@PathVariable("id") Long id);

    /**
     * 分页查询
     * @param mdProductPicMapping
     * @return
     */
    @PostMapping("/customerProductPic/findList")
    MSResponse<MSPage<MDProductPicMapping>> findList(@RequestBody MDProductPicMapping mdProductPicMapping);

    /**
     * 添加
     * @param mdProductPicMapping
     * @return
     */
    @PostMapping("/customerProductPic/insert")
    MSResponse<Integer> insert(@RequestBody MDProductPicMapping mdProductPicMapping);


    /**
     * 修改
     * @param mdProductPicMapping
     * @return
     */
    @PutMapping("/customerProductPic/update")
    MSResponse<Integer> update(@RequestBody MDProductPicMapping mdProductPicMapping);

    /**
     * 删除
     * @param mdProductPicMapping
     * @return
     */
    @DeleteMapping("/customerProductPic/delete")
    MSResponse<Integer> delete(@RequestBody MDProductPicMapping mdProductPicMapping);

    /**
     * 根据客户Id和产品Id获取完工图片配置信息
     * @param customerId
     * @param productId
     * @return
     */
    @GetMapping("/customerProductPic/getCustomerProductPicByProductAndCustomer")
    MSResponse<MDProductPicMapping> getCustomerProductPicByProductAndCustomer(@RequestParam("customerId") long customerId,
                                                                              @RequestParam("productId") long productId);

    /**
     * 根据客户Id和产品Id集合获取完工图片配置信息集合
     * @param productIds
     * @param customerId
     * @return
     */
    @PostMapping("/customerProductPic/findCustomerProductPicList")
    MSResponse<List<MDProductPicMapping>> findCustomerProductPicList(@RequestParam("productIds") List<Long> productIds,
                                                                     @RequestParam("customerId") Long customerId);


    /**
     * 获取所有
     * @return
     */
    @GetMapping("/customerProductPic/findAllList")
    MSResponse<List<MDProductPicMapping>> findAllList();
}
