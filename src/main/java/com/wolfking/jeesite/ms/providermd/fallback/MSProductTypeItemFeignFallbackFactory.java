package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDProductTypeItem;
import com.wolfking.jeesite.ms.providermd.feign.MSProductTypeItemFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MSProductTypeItemFeignFallbackFactory implements FallbackFactory<MSProductTypeItemFeign> {
    @Override
    public MSProductTypeItemFeign create(Throwable throwable) {
        return new MSProductTypeItemFeign() {

            @Override
            public MSResponse<List<MDProductTypeItem>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductTypeItem>> findListByProductTypeId(Long productTypeId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
