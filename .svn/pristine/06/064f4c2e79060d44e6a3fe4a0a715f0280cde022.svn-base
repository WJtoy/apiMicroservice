package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryServicePointFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSProductCategoryServicePointFallbackFactory implements FallbackFactory<MSProductCategoryServicePointFeign> {
    @Override
    public MSProductCategoryServicePointFeign create(Throwable throwable) {
        return new MSProductCategoryServicePointFeign(){
            /**
             * 修改网点产品类目映射
             *
             * @param servicePointId     网点id
             * @param productCategoryIds 产品类目id列表
             * @return
             */
            @Override
            public MSResponse<Integer> update(Long servicePointId, List<Long> productCategoryIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<Long>> findListByServicePointIdFromCacheForSD(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点Id和品类id从缓存中判断网点品类是否存在
             *
             * @param servicePointId
             * @param productCategoryId
             * @return
             */
            @Override
            public MSResponse<Boolean> existByPointIdAndCategoryIdFromCacheForSD(Long servicePointId, Long productCategoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点id列表和品类id获取网点id列表
             *
             * @param sids              网点id列表
             * @param productCategoryId 品类id
             * @return
             */
            @Override
            public MSResponse<List<Long>> findListByProductCategoryIdAndServicePointIds(List<Long> sids, Long productCategoryId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
