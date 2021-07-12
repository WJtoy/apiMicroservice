package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServicePointLog;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointLogFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSServicePointLogFeignFallbackFactory implements FallbackFactory<MSServicePointLogFeign> {
    @Override
    public MSServicePointLogFeign create(Throwable throwable) {
        return new MSServicePointLogFeign() {
            /**
             * 新增网点日志
             *
             * @param mdServicePointLog
             * @return
             */
            @Override
            public MSResponse<Integer> insert(MDServicePointLog mdServicePointLog) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id获取网点日志
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDServicePointLog> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点id获取网点历史备注信息
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointLog>> findHisRemarks(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点id获取网点派单历史备注信息
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointLog>> findHisPlanRemarks(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
