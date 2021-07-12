package com.wolfking.jeesite.ms.um.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.um.sd.UmOrderStatusUpdate;
import com.wolfking.jeesite.ms.um.sd.fallback.UmOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kklplus-b2b-um", fallbackFactory = UmOrderFeignFallbackFactory.class)
public interface UmOrderFeign {

    //-----------------------------------------------------------------------------------------------------工单处理日志
    @PostMapping("/order/statusUpdate")
    MSResponse statusUpdate(@RequestBody UmOrderStatusUpdate orderStatusUpdate);
}
