package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorTypeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSErrorTypeFeignFallbackFactory implements FallbackFactory<MSErrorTypeFeign> {
    @Override
    public MSErrorTypeFeign create(Throwable throwable) {
        return new MSErrorTypeFeign() {
            /**
             * 保存故障分类
             *
             * @param mdErrorType
             * @return
             */
            @Override
            public MSResponse<Integer> save(MDErrorType mdErrorType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取故障分类列表
             *
             * @param mdErrorType
             * @return
             */
            @Override
            public MSResponse<MSPage<MDErrorType>> findList(MDErrorType mdErrorType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品获取故障分类列表
             *
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDErrorType>> findErrorTypesByProductId(Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 删除故障分类
             *
             * @param mdErrorType
             * @return
             */
            @Override
            public MSResponse<Integer> delete(MDErrorType mdErrorType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 修改故障分类
             *
             * @param mdErrorType
             * @return
             */
            @Override
            public MSResponse<Integer> update(MDErrorType mdErrorType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过产品id及故障分类名称获取故障分类id
             *
             * @param productId
             * @param name
             * @return
             */
            @Override
            public MSResponse<Long> getByProductIdAndName(Long productId, String name) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id，或id查询故障分类
             *
             * @param productId
             * @param id
             * @return
             */
            @Override
            public MSResponse<List<MDErrorType>> findListByProductId(Long productId, Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
