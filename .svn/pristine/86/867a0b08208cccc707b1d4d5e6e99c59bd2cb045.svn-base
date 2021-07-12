package com.wolfking.jeesite.ms.providersys.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysAppNotice;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAppNoticeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSSysAppNoticeFeignFallbackFactory implements FallbackFactory<MSSysAppNoticeFeign> {
    @Override
    public MSSysAppNoticeFeign create(Throwable throwable) {
        return new MSSysAppNoticeFeign() {
            /**
             * 根据用户Id获取单个手机App通知对象Id
             *
             * @param userId
             * @return
             */
            @Override
            public MSResponse<Long> getOneIdByUserId(Long userId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据用户id获取手机App通知
             *
             * @param userId
             * @return
             */
            @Override
            public MSResponse<SysAppNotice> getByUserId(Long userId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 添加手机App通知
             *
             * @param sysAppNotice
             * @return
             */
            @Override
            public MSResponse<Integer> insert(SysAppNotice sysAppNotice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> updateByUserId(SysAppNotice sysAppNotice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
