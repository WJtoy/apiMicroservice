package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServicePointAddress;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointAddressFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSServicePointAddressFeignFallbackFactory implements FallbackFactory<MSServicePointAddressFeign> {
    @Override
    public MSServicePointAddressFeign create(Throwable throwable) {
        return new MSServicePointAddressFeign() {
            /**
             * 添加网点地址 (API)
             *
             * @param servicePointAddress
             * @return
             */
            @Override
            public MSResponse<Integer> save(MDServicePointAddress servicePointAddress) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从缓存中获取网点地址(API)
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<MDServicePointAddress> getByServicePointIdFromCache(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
