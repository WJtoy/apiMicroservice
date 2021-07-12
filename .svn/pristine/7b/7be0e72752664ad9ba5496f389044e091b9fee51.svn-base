package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.dto.MDErrorCodeDto;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorCodeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSErrorCodeFeignFallbackFactory implements FallbackFactory<MSErrorCodeFeign> {
    @Override
    public MSErrorCodeFeign create(Throwable throwable) {
        return new MSErrorCodeFeign() {
            /**
             * 删除故障代码
             *
             * @param mdErrorCode
             * @return
             */
            @Override
            public MSResponse<Integer> delete(MDErrorCode mdErrorCode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 保存故障代码
             *
             * @param mdErrorCode
             * @return
             */
            @Override
            public MSResponse<Integer> save(MDErrorCode mdErrorCode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据productId， ErrorTypeId及错误代码名字获取故障代码id
             *
             * @param mdErrorCode
             * @return
             */
            @Override
            public MSResponse<Long> getByProductAndErrorType(MDErrorCode mdErrorCode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取故障代码列表
             *
             * @param mdErrorCode
             * @return
             */
            @Override
            public MSResponse<MSPage<MDErrorCodeDto>> findListReturnErrorCodeDto(MDErrorCode mdErrorCode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id和故障分类id获取故障代码列表
             *
             * @param productId
             * @param errorTypeId
             * @return
             */
            @Override
            public MSResponse<List<MDErrorCode>> findListByProductAndErrorType(Long errorTypeId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id和故障分类id获取故障代码id
             *
             * @param errorTypeId
             * @param productId
             * @return
             */
            @Override
            public MSResponse<Long> getIdByProductAndErrorType(Long errorTypeId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过产品id或者故障现象id获取故障现象列表
             *
             * @param id
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDErrorCodeDto>> findListByProductId(Long id, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
