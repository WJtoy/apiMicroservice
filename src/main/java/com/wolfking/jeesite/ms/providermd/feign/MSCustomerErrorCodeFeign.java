package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerErrorCodeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerErrorCodeFeignFallbackFactory.class)
public interface MSCustomerErrorCodeFeign {

    /**
     * 根据客户id,产品id,故障分类ids获取故障现象(从缓存获取)
     * @param customerId
     * @param nameValuePairs key:产品id,value:故障现象id
     * @return
     */
    @PostMapping("/errorCode/findListByProductIdAndIdsFromCache")
    MSResponse<List<MDErrorCode>> findListByProductIdAndIdsFromCache(@RequestParam("customerId") Long customerId, @RequestBody List<NameValuePair<Long,Long>> nameValuePairs);

    /**
     * 根据客户id,产品id和故障分类id获取故障代码列表
     * @param productId
     * @param errorTypeId
     * @return
     */
    @GetMapping("errorCode/findListByProductAndErrorType")
    MSResponse<List<MDErrorCode>> findListByProductAndErrorType(@RequestParam("errorTypeId") Long errorTypeId, @RequestParam("productId") Long productId, @RequestParam("customerId") Long customerId);

    /**
     * 根据客户id+产品id+id获取故障代码(缓存中获取)
     * @param customerId
     * @param productId
     * @param id
     * @return
     */
    @PostMapping("/errorCode/getByProductIdAndCustomerIdFromCache")
    MSResponse<MDErrorCode> getByProductIdAndCustomerIdFromCache(@RequestParam("customerId") Long customerId, @RequestParam("productId") Long productId,
                                                                 @RequestParam("id") Long id);
}
