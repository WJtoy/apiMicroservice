package com.wolfking.jeesite.ms.xyyplus.sd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.wolfking.jeesite.ms.xyyplus.sd.feign.XYYPlusOrderFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class XYYPlusOrderFeignFallbackFactory implements FallbackFactory<XYYPlusOrderFeign> {

    @Override
    public XYYPlusOrderFeign create(Throwable throwable) {

        return new XYYPlusOrderFeign() {
            @Override
            public MSResponse newMaterial(B2BMaterial material) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse updateAuditFlag(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse updateDeliverFlag(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }

}
