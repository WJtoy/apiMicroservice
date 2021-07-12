package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BSurchargeCategoryMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class B2BSurchargeCategoryMappingFeignFallbackFactory implements FallbackFactory<B2BSurchargeCategoryMappingFeign> {

    @Override
    public B2BSurchargeCategoryMappingFeign create(Throwable throwable) {
        return new B2BSurchargeCategoryMappingFeign() {


            @Override
            public MSResponse<B2BSurchargeCategoryMapping> get(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BSurchargeCategoryMapping>> getList(B2BSurchargeCategoryMapping surchargeCategoryMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BSurchargeCategoryMapping> insert(B2BSurchargeCategoryMapping surchargeCategoryMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(B2BSurchargeCategoryMapping surchargeCategoryMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(B2BSurchargeCategoryMapping surchargeCategoryMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<B2BSurchargeCategoryMapping>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
