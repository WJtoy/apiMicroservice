package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductCategory;
import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MSProductCategoryFeignFallbackFactory implements FallbackFactory<MSProductCategoryFeign> {
    @Override
    public MSProductCategoryFeign create(Throwable throwable) {
        return new MSProductCategoryFeign() {
            @Override
            public MSResponse<List<MDProductCategory>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getIdByCode(String code) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getIdByName(String code) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDProductCategory>> findList(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过id列表获取类别列表
             *
             * @param ids
             * @return
             */
            @Override
            public MSResponse<List<MDProductCategory>> findListByIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDProductCategory> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从产品品类中获取品类名称
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<String> getNameById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id从缓存中获取产品类别
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDProductCategory> getFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id从缓存中获取产品类别名称
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<String> getNameFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDProductCategory mdProductCategory) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
