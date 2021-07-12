package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServicePointAddress;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointAddressFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "provider-md", fallbackFactory = MSServicePointAddressFeignFallbackFactory.class)
public interface MSServicePointAddressFeign {
    /**
     * 保存网点地址 (API)
     * @param servicePointAddress
     * @return
     */
    @PostMapping("/servicePoint/address/save")
    MSResponse<Integer> save(@RequestBody MDServicePointAddress servicePointAddress);

    /**
     * 从缓存中获取网点地址(API)
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePoint/address/getByServicePointIdFromCache")
    MSResponse<MDServicePointAddress> getByServicePointIdFromCache(@RequestParam("servicePointId") Long servicePointId);
}
