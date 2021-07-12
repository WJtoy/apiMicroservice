package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorAction;
import com.kkl.kklplus.entity.md.dto.MDErrorActionDto;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorActionFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSErrorActionFeignFallbackFactory implements FallbackFactory<MSErrorActionFeign> {
    @Override
    public MSErrorActionFeign create(Throwable throwable) {
        return new MSErrorActionFeign() {
            /**
             * 分页获取故障处理列表
             *
             * @param mdErrorActionDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDErrorActionDto>> findList(MDErrorActionDto mdErrorActionDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取故障处理列表
             *
             * @param mdErrorActionDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDErrorActionDto>> findListWithProduct(MDErrorActionDto mdErrorActionDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 删除
             *
             * @param mdErrorAction
             * @return
             */
            @Override
            public MSResponse<Integer> delete(MDErrorAction mdErrorAction) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过产品和故障代码判断是否存在
             *
             * @param errorCodeId
             * @param productId
             * @return
             */
            @Override
            public MSResponse<Long> getIdByProductAndErrorCode(Long errorCodeId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据errorActionId获取errorActionDto的相关数据
             *
             * @param errorActionId
             * @return
             */
            @Override
            public MSResponse<MDErrorActionDto> getAssociatedDataById(Long errorActionId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 更新故障分析
             *
             * @param mdActionCode
             * @return
             */
            @Override
            public MSResponse<Integer> updateActionCodeNameAndAnalysis(MDActionCode mdActionCode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
