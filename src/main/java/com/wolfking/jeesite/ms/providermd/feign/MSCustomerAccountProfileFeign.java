package com.wolfking.jeesite.ms.providermd.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDCustomerAccountProfile;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerAccountProfileFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerAccountProfileFallbackFactory.class)
public interface MSCustomerAccountProfileFeign {
    /**
     * 根据id获取单个客户账户资料
     * @param id
     * @return
     */
    @GetMapping("/customerAccountProfile/getById/{id}")
    MSResponse<MDCustomerAccountProfile> getById(@PathVariable("id") Long id);

}
