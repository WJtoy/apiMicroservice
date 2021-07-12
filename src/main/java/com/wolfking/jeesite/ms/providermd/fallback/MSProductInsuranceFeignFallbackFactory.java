package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductInsurance;
import com.wolfking.jeesite.ms.providermd.feign.MSProductInsuranceFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductInsuranceFeignFallbackFactory implements FallbackFactory<MSProductInsuranceFeign> {
    @Override
    public MSProductInsuranceFeign create(Throwable throwable) {
        return new MSProductInsuranceFeign() {
            @Override
            public MSResponse<MSPage<MDProductInsurance>> findList(MDProductInsurance mdProductInsurance) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductInsurance>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDProductInsurance> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDProductInsurance mdProductInsurance) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDProductInsurance mdProductInsurance) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDProductInsurance mdProductInsurance) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
