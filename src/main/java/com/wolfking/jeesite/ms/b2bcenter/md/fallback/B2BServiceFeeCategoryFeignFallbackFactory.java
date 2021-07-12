package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceFeeCategory;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceFeeCategoryFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class B2BServiceFeeCategoryFeignFallbackFactory implements FallbackFactory<B2BServiceFeeCategoryFeign> {

    @Override
    public B2BServiceFeeCategoryFeign create(Throwable throwable) {
        return new B2BServiceFeeCategoryFeign() {


            @Override
            public MSResponse<B2BServiceFeeCategory> get(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BServiceFeeCategory>> getList(B2BServiceFeeCategory serviceFeeCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BServiceFeeCategory> insert(B2BServiceFeeCategory serviceFeeCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(B2BServiceFeeCategory serviceFeeCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(B2BServiceFeeCategory serviceFeeCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<B2BServiceFeeCategory>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
