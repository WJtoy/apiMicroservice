package com.wolfking.jeesite.ms.um.sd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.um.sd.UmOrderStatusUpdate;
import com.wolfking.jeesite.ms.um.sd.feign.UmOrderFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UmOrderFeignFallbackFactory implements FallbackFactory<UmOrderFeign> {

    @Override
    public UmOrderFeign create(Throwable throwable) {
        return new UmOrderFeign() {
            @Override
            public MSResponse statusUpdate(UmOrderStatusUpdate orderStatusUpdate) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
