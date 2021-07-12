package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDGradeItem;
import com.wolfking.jeesite.ms.providermd.feign.MSGradeItemFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MSGradeItemFeignFallbackFactory implements FallbackFactory<MSGradeItemFeign> {
    @Override
    public MSGradeItemFeign create(Throwable throwable) {
        return new MSGradeItemFeign() {


            @Override
            public MSResponse<MDGradeItem> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDGradeItem>> findListByGradeId(Long gradeId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDGradeItem mdGradeItem) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDGradeItem mdGradeItem) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDGradeItem mdGradeItem) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
