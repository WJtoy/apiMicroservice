package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.dto.MDProductSpecDto;
import com.wolfking.jeesite.ms.providermd.feign.MSProductSpecTypeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MSProductSpecTypeFeignFallbackFactory implements FallbackFactory<MSProductSpecTypeFeign> {
    @Override
    public MSProductSpecTypeFeign create(Throwable throwable) {
        return new MSProductSpecTypeFeign() {
            @Override
            public MSResponse<List<MDProductSpecDto>> findListByTypeIdAndItemId(Long productTypeId, Long productTypeItemId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
