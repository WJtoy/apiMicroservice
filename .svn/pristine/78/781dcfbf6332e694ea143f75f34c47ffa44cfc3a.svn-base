package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceTypeMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceTypeMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class B2BServiceTypeMappingFeignFallbackFactory implements FallbackFactory<B2BServiceTypeMappingFeign> {

    @Override
    public B2BServiceTypeMappingFeign create(Throwable throwable) {
        return new B2BServiceTypeMappingFeign() {
            @Override
            public MSResponse<List<B2BServiceTypeMapping>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BServiceTypeMapping> getServiceTypeMappingById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BServiceTypeMapping>> getServiceTypeMappingList(B2BServiceTypeMapping serviceTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);

            }

            @Override
            public MSResponse<B2BServiceTypeMapping> insertServiceTypeMapping(B2BServiceTypeMapping serviceTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateServiceTypeMapping(B2BServiceTypeMapping serviceTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteServiceTypeMapping(B2BServiceTypeMapping serviceTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getByField(B2BServiceTypeMapping serviceTypeMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
