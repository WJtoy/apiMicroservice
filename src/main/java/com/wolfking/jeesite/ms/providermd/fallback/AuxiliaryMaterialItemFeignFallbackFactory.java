package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.ms.providermd.feign.AuxiliaryMaterialItemFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuxiliaryMaterialItemFeignFallbackFactory implements FallbackFactory<AuxiliaryMaterialItemFeign> {

    @Override
    public AuxiliaryMaterialItemFeign create(Throwable throwable) {
        return new AuxiliaryMaterialItemFeign() {

            @Override
            public MSResponse<List<MDAuxiliaryMaterialItem>> getListByProductId(List<String> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDAuxiliaryMaterialItem>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };

    }
}
