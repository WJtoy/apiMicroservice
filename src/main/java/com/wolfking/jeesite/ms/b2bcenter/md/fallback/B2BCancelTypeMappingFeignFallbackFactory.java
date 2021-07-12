package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCancelTypeMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceTypeMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCancelTypeMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class B2BCancelTypeMappingFeignFallbackFactory implements FallbackFactory<B2BCancelTypeMappingFeign> {

    @Override
    public B2BCancelTypeMappingFeign create(Throwable throwable) {
        return new B2BCancelTypeMappingFeign() {


            @Override
            public MSResponse<List<B2BCancelTypeMapping>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BCancelTypeMapping> getCancelTypeMappingById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BCancelTypeMapping>> getCancelTypeMappingList(B2BCancelTypeMapping cancelTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BCancelTypeMapping> insertCancelTypeMapping(B2BCancelTypeMapping cancelTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateCancelTypeMapping(B2BCancelTypeMapping cancelTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteCancelTypeMapping(B2BCancelTypeMapping cancelTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
