package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerErrorTypeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerErrorTypeFeignFallbackFactory.class)
public interface MSCustomerErrorTypeFeign {

    /**
     * 根据客户id,产品id,故障分类ids获取故障分类(从缓存获取)
     * @param customerId
     * @param nameValuePairs key:产品id,value:故障分类id
     * @return
     */
    @PostMapping("/errorType/findListByProductIdAndIdsFromCache")
    MSResponse<List<MDErrorType>> findListByProductIdAndIdsFromCache(@RequestParam("customerId") Long customerId, @RequestBody List<NameValuePair<Long,Long>> nameValuePairs);

    /**
     * 根据客户id跟产品id获取故障分类列表,如果没数据则在根据产品id和customerId=0获取
     * @param productId
     * @param customerId
     * @return
     */
    @GetMapping("/errorType/findErrorTypesByProductIdNewFromCache")
    MSResponse<List<MDErrorType>> findListByProductIdAndCustomerIdFromCache(@RequestParam("productId") Long productId,@RequestParam("customerId") Long customerId);

    /**
     * 根据产品获取故障分类列表
     * @param productId
     * @return
     */
    @GetMapping("/errorType/findErrorTypesByProductId")
    MSResponse<List<MDErrorType>> findErrorTypesByProductId(@RequestParam("productId") Long productId,@RequestParam("customerId") Long customerId);

    /**
     * 根据客户id+产品id+id获取故障分类(缓存中获取)
     * @param customerId
     * @param productId
     * @param id
     * @return
     */
    @PostMapping("/errorType/getByProductIdAndCustomerIdFromCache")
    MSResponse<MDErrorType> getByProductIdAndCustomerIdFromCache(@RequestParam("customerId") Long customerId, @RequestParam("productId") Long productId,
                                                                 @RequestParam("id") Long id);
}
