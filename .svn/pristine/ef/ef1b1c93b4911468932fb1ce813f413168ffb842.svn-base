package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSign;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceSignFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class B2BServiceSignFeignFallbackFactory implements FallbackFactory<B2BServiceSignFeign> {
    @Override
    public B2BServiceSignFeign create(Throwable cause) {
        return new B2BServiceSignFeign(){

            @Override
            public MSResponse<MSPage<B2BSign>> getServiceSignList(B2BSign sign) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BSign> getServiceSignById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Boolean> getServiceSignAudit(B2BSign sign) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
