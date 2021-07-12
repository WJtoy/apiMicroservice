package com.wolfking.jeesite.ms.inse.sd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.inse.sd.InseOrderRemark;
import com.wolfking.jeesite.ms.inse.sd.feign.InseOrderFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class InseOrderFeignFallbackFactory implements FallbackFactory<InseOrderFeign> {

    @Override
    public InseOrderFeign create(Throwable throwable) {
        return new InseOrderFeign() {
            @Override
            public MSResponse saveLog(InseOrderRemark orderRemark) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
