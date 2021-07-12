package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerUrgent;
import com.wolfking.jeesite.modules.md.entity.UrgentCustomer;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerUrgentFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSCustomerUrgentFeignFallbackFactory implements FallbackFactory<MSCustomerUrgentFeign> {
    @Override
    public MSCustomerUrgentFeign create(Throwable throwable) {
        return new MSCustomerUrgentFeign() {
            @Override
            public MSResponse<Integer> batchInsert(List<MDCustomerUrgent> mdCustomerUrgentList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerUrgent>> findListByCustomerId(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<Long>> findList(MDCustomerUrgent mdCustomerTimeUrgent) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDCustomerUrgent mdCustomerTimeUrgent) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
