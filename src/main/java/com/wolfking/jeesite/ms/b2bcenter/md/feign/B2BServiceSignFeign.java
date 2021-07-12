package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSign;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BServiceSignFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "kklplus-b2b-pdd", fallbackFactory = B2BServiceSignFeignFallbackFactory.class)
public interface B2BServiceSignFeign {

    @PostMapping("/serviceSign/getList")
    MSResponse<MSPage<B2BSign>> getServiceSignList(@RequestBody B2BSign sign);

    @GetMapping("/serviceSign/getById")
    MSResponse<B2BSign> getServiceSignById(@RequestParam("id") Long id);

    @PostMapping("/serviceSign/audit")
    MSResponse<Boolean> getServiceSignAudit(@RequestBody B2BSign sign);
}
