package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialCategory;
import com.wolfking.jeesite.ms.providermd.feign.AuxiliaryMaterialCategoryFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuxiliaryMaterialCategoryFeignFallbackFactory implements FallbackFactory<AuxiliaryMaterialCategoryFeign> {

    @Override
    public AuxiliaryMaterialCategoryFeign create(Throwable throwable) {
        return new AuxiliaryMaterialCategoryFeign() {
            @Override
            public MSResponse<List<MDAuxiliaryMaterialCategory>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };

    }
}
