package com.wolfking.jeesite.ms.cc.feign.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.vm.AbnormalFormSearchModel;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.cc.feign.CCServicePointAbnormalFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;



@Component
public class CCServicePointAbnormalFactory implements FallbackFactory<CCServicePointAbnormalFeign> {

    @Override
    public CCServicePointAbnormalFeign create(Throwable throwable) {
        return new CCServicePointAbnormalFeign() {
            @Override
            public MSResponse<MSPage<AbnormalForm>> servicePointPraiseRejectAbnormalList(AbnormalFormSearchModel abnormalFormSearchModel) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
