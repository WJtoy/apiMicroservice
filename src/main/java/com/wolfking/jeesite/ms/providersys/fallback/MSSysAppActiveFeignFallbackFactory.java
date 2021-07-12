package com.wolfking.jeesite.ms.providersys.fallback;


import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysAppActive;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAppActiveFeign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSSysAppActiveFeignFallbackFactory implements FallbackFactory<MSSysAppActiveFeign> {
    @Override
    public MSSysAppActiveFeign create(Throwable throwable) {

        return new MSSysAppActiveFeign() {
            @Override
            public MSResponse<Integer> saveActiveInfo(SysAppActive sysAppActive) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
