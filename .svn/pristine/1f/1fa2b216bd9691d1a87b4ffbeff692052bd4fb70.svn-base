package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDCustomerAccountProfile;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerAccountProfileFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MSCustomerAccountProfileFallbackFactory implements FallbackFactory<MSCustomerAccountProfileFeign> {
    @Override
    public MSCustomerAccountProfileFeign create(Throwable throwable) {
        return new MSCustomerAccountProfileFeign(){
            @Override
            public MSResponse<MDCustomerAccountProfile> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
