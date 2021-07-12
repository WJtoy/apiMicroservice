package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerAddress;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerAddressFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSEngineerAddressFeignFallbackFactory implements FallbackFactory<MSEngineerAddressFeign> {
    @Override
    public MSEngineerAddressFeign create(Throwable throwable) {

        return new MSEngineerAddressFeign() {

            /**
             * 根据安维id获取安维收件信息
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDEngineerAddress> getByEngineerId(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDEngineerAddress> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDEngineerAddress engineerAddress) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDEngineerAddress engineerAddress) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDEngineerAddress> getFromCache(Long servicePointId, Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
