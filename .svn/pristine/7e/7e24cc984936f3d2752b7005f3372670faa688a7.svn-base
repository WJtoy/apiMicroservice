package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDPlanRadius;
import com.wolfking.jeesite.ms.providermd.feign.MSPlanRadiusFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSPlanRadiusFeignFallbackFactory implements FallbackFactory<MSPlanRadiusFeign> {
    @Override
    public MSPlanRadiusFeign create(Throwable throwable) {
        return new MSPlanRadiusFeign() {

            @Override
            public MSResponse<MDPlanRadius> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDPlanRadius>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDPlanRadius>> findList(MDPlanRadius mdPlanRadius) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDPlanRadius mdPlanRadius) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDPlanRadius mdPlanRadius) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> enableOrDisable(MDPlanRadius mdPlanRadius) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDPlanRadius> getByAreaId(Long areaId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
