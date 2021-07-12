package com.wolfking.jeesite.ms.callbackService.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.voiceservice.VoiceTask;
import com.wolfking.jeesite.ms.callbackService.fallback.VoiceCallbackFeignFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "kklplus-order-callback-service", fallbackFactory = VoiceCallbackFeignFallback.class)
public interface VoiceCallbackFeign {

    /**
     * 按订单id读取回访记录
     */
    @RequestMapping("/voice/get/{site}/{quarter}/{orderId}")
    MSResponse<VoiceTask> getTaskInfo(@PathVariable("site") String site,@PathVariable("quarter") String quarter,@PathVariable("orderId") Long orderId);

}
