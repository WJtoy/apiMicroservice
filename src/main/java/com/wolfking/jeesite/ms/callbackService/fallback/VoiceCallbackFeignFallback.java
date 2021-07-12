package com.wolfking.jeesite.ms.callbackService.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.wolfking.jeesite.ms.callbackService.feign.VoiceCallbackFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class VoiceCallbackFeignFallback implements FallbackFactory<VoiceCallbackFeign> {

    @Override
    public VoiceCallbackFeign create(Throwable throwable) {
        return new VoiceCallbackFeign() {

            /**
             * 按订单id读取回访记录
             */
            @Override
            public MSResponse getTaskInfo(String site,String quarter,Long orderId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
