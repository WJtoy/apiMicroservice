package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductCategoryBrand;
import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryBrandFeign;
import com.wolfking.jeesite.ms.tmall.md.entity.B2BCategoryBrand;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductCategoryBrandFeignFallbackFactory implements FallbackFactory<MSProductCategoryBrandFeign> {
    @Override
    public MSProductCategoryBrandFeign create(Throwable throwable) {
        return new MSProductCategoryBrandFeign() {
            @Override
            public MSResponse<List<B2BCategoryBrand>> getCategoryBrandMap(String categoryIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取产品的类型与品牌的对应关系
             *
             * @param categoryIds
             * @return
             */
            @Override
            public MSResponse<List<B2BCategoryBrand>> findCategoryBrandMap(List<Long> categoryIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<Long>> getBrandIdsByCategoryId(Long categoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductCategoryBrand>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDProductCategoryBrand>> findList(MDProductCategoryBrand mdProductCategoryBrand) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsert(List<MDProductCategoryBrand> mdProductCategoryBrandList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deleteByCategoryId(Long categoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
