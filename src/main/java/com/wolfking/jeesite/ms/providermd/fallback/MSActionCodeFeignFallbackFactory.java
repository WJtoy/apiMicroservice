package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorAction;
import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.kkl.kklplus.entity.md.dto.MDErrorActionDto;
import com.wolfking.jeesite.ms.providermd.feign.MSActionCodeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSActionCodeFeignFallbackFactory implements FallbackFactory<MSActionCodeFeign> {
    @Override
    public MSActionCodeFeign create(Throwable throwable) {
        return new MSActionCodeFeign() {
            /**
             * 通过产品和故障代码获取故障处理id,故障名称，服务类型及服务类型id
             *
             * @param errorCodeId
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDActionCodeDto>> findListByProductAndErrorCode(Long errorCodeId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id，或id获取处理代码列表
             *
             * @param id
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDActionCodeDto>> findListByProductId(Long id, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
