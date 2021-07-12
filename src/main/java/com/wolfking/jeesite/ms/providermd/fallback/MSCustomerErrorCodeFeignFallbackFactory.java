package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerErrorCodeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSCustomerErrorCodeFeignFallbackFactory implements FallbackFactory<MSCustomerErrorCodeFeign> {
    @Override
    public MSCustomerErrorCodeFeign create(Throwable throwable) {
        return new MSCustomerErrorCodeFeign() {

            @Override
            public MSResponse<List<MDErrorCode>> findListByProductIdAndIdsFromCache(Long customerId, List<NameValuePair<Long, Long>> nameValuePairs) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDErrorCode>> findListByProductAndErrorType(Long errorTypeId, Long productId, Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDErrorCode> getByProductIdAndCustomerIdFromCache(Long customerId, Long productId, Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
