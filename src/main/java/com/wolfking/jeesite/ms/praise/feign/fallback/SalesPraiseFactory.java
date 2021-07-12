package com.wolfking.jeesite.ms.praise.feign.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraisePageSearchModel;
import com.wolfking.jeesite.ms.praise.feign.SalesPraiseFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class SalesPraiseFactory implements FallbackFactory<SalesPraiseFeign> {

    @Override
    public SalesPraiseFeign create(Throwable throwable) {
        return new SalesPraiseFeign() {
            @Override
            public MSResponse<MSPage<PraiseListModel>> pendingReviewList(PraisePageSearchModel praisePageSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<PraiseListModel>> approvedList(PraisePageSearchModel praisePageSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<PraiseListModel>> findPraiseList(PraisePageSearchModel praisePageSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
