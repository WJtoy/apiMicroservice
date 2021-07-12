package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerTimeliness;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerTimelinessFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MSCustomerTimelinessFeignFallbackFactory implements FallbackFactory<MSCustomerTimelinessFeign> {
    @Override
    public MSCustomerTimelinessFeign create(Throwable throwable) {
        return new MSCustomerTimelinessFeign() {
            @Override
            public MSResponse<Integer> batchInsert(List<MDCustomerTimeliness> mdCustomerTimelinessList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerTimeliness>> findListByCustomerId(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<Long>> findList(MDCustomerTimeliness mdCustomerTimeliness) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDCustomerTimeliness mdCustomerTimeliness) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
