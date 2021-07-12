package com.wolfking.jeesite.ms.inse.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.inse.sd.InseOrderRemark;
import com.wolfking.jeesite.ms.inse.sd.fallback.InseOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kklplus-b2b-inse", fallbackFactory = InseOrderFeignFallbackFactory.class)
public interface InseOrderFeign {

    @PostMapping("/inseOrderInfo/saveLog")
    MSResponse saveLog(@RequestBody InseOrderRemark orderRemark);
}
