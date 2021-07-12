package com.wolfking.jeesite.ms.keg.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.keg.sd.KegOrder;
import com.kkl.kklplus.entity.keg.sd.KegOrderCompleted;
import com.wolfking.jeesite.ms.keg.sd.fallback.KegOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kklplus-b2b-keg", fallbackFactory = KegOrderFeignFallbackFactory.class)
public interface KegOrderFeign {

    /**
     * 写工单信息
     * @param kegOrder
     * @return
     */
    @PostMapping("/kegOrder/new")
    MSResponse newOrder(@RequestBody KegOrder kegOrder);

    /**
     * 写工单的完工信息
     */
    @PostMapping("/kegOrder/completed")
    MSResponse completed(@RequestBody KegOrderCompleted completed);

}
