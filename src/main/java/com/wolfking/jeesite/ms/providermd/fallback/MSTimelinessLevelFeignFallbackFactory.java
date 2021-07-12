package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDTimelinessLevel;
import com.wolfking.jeesite.ms.providermd.feign.MSTimelinessLevelFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MSTimelinessLevelFeignFallbackFactory implements FallbackFactory<MSTimelinessLevelFeign> {

    @Override
    public MSTimelinessLevelFeign create(Throwable throwable) {
        return new MSTimelinessLevelFeign() {


            @Override
            public MSResponse<MDTimelinessLevel> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDTimelinessLevel>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDTimelinessLevel>> findList(MDTimelinessLevel mdTimelinessLevel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDTimelinessLevel mdTimelinessLevel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDTimelinessLevel mdTimelinessLevel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDTimelinessLevel mdTimelinessLevel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }
}
