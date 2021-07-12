package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BProductMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BProductMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class B2BProductMappingFeignFallbackFactory implements FallbackFactory<B2BProductMappingFeign> {

    @Override
    public B2BProductMappingFeign create(Throwable throwable) {
        return new B2BProductMappingFeign() {
            @Override
            public MSResponse<List<B2BProductMapping>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<B2BProductMapping>> getListByCustomerCategoryIds(Integer dataSource, List<String> customerCategoryIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Map<Long, List<String>>> getListByProductIds(Integer dataSource, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BProductMapping> getProductMappingById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BProductMapping>> getProductMappingList(B2BProductMapping productMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BProductMapping> insertProductMapping(B2BProductMapping productMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateProductMapping(B2BProductMapping productMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteProductMapping(B2BProductMapping productMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<B2BProductMapping>> insertBatch(String strJson) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> syncByDataSource(List<B2BProductMapping> productMappingList,Long createById) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }
}
