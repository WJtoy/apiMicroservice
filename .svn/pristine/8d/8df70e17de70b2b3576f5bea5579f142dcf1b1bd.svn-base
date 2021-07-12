package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerPriceNewFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSCustomerPriceNewFeignFallbackFactory.class)
public interface MSCustomerPriceNewFeign {
    /**
     * 根据产品和服务类型获取客户的服务价格
     * @param customerId  客户id
     * @param paramMap key 为产品id ,value为服务类型id
     * @return
     */
    @PostMapping("/customerPriceNew/findPricesByProductsAndServiceTypesFromCache")
    MSResponse<List<MDCustomerPriceDto>> findPricesByProductsAndServiceTypesFromCache(@RequestParam("customerId") Long customerId, @RequestBody HashMap<Long, Long> paramMap);
}
