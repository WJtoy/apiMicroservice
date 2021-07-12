package com.wolfking.jeesite.ms.cc.feign.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.vm.AbnormalFormSearchModel;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.cc.feign.CCAbnormalFormFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CCAbnormalFormFactory implements FallbackFactory<CCAbnormalFormFeign> {

    @Override
    public CCAbnormalFormFeign create(Throwable throwable) {
        return new CCAbnormalFormFeign() {

            @Override
            public MSResponse<Integer> save(AbnormalForm abnormalForm) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> closeAbnormalForm(AbnormalForm abnormalForm) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> waitProcessList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> processedList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> closeByOrderId(AbnormalForm abnormalForm) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insertBatch(List<AbnormalForm> abnormalFormList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> closeReviewAbnormal(AbnormalForm abnormalForm) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> getCountByOrderId(Long orderId, String quarter,Integer formType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> appAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> reviewAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> appCompleteAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> smsAbnormalList(AbnormalFormSearchModel abnormalFormSearchMode) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> oldAppAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<AbnormalForm>> kefuPraiseRejectAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
