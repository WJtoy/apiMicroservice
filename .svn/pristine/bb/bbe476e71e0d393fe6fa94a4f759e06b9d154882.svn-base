package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductPicMapping;
import com.wolfking.jeesite.ms.providermd.feign.MSProductPicMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductPicMappingFeignFallbackFactory implements FallbackFactory<MSProductPicMappingFeign> {
    @Override
    public MSProductPicMappingFeign create(Throwable throwable) {
        return new MSProductPicMappingFeign() {
            @Override
            public MSResponse<MSPage<MDProductPicMapping>> findList(MDProductPicMapping mdProductPicMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductPicMapping>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDProductPicMapping> get(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDProductPicMapping> getByProductId(Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDProductPicMapping mdProductPicMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDProductPicMapping mdProductPicMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDProductPicMapping mdProductPicMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
