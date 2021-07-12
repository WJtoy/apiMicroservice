package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.dto.MDErrorActionDto;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerActionCodeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MSCustomerActionCodeFeignFallbackFactory implements FallbackFactory<MSCustomerActionCodeFeign> {
    @Override
    public MSCustomerActionCodeFeign create(Throwable throwable) {
        return new MSCustomerActionCodeFeign() {

            @Override
            public MSResponse<List<MDActionCode>> findListByProductIdAndIdsFromCache(Long customerId, List<NameValuePair<Long, Long>> nameValuePairs) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDActionCode> getByProductIdAndCustomerIdFromCache(Long customerId, Long productId, Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
