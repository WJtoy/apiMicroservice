package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductTimeLiness;
import com.wolfking.jeesite.ms.providermd.feign.MSProductTimeLinessFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductTimeLinessFeignFallbackFactory implements FallbackFactory<MSProductTimeLinessFeign> {
    @Override
    public MSProductTimeLinessFeign create(Throwable throwable) {
        return new MSProductTimeLinessFeign() {
            @Override
            public MSResponse<List<MDProductTimeLiness>> getPrices(Long productCategoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteByProductCategoryId(Long productCategoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductTimeLiness>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsert(List<MDProductTimeLiness> mdProductTimeLinessList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
