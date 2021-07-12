package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointArea;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointAreaFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSServicePointAreaFeignFallbackFactory implements FallbackFactory<MSServicePointAreaFeign> {
    @Override
    public MSServicePointAreaFeign create(Throwable throwable) {
        return new MSServicePointAreaFeign() {
            /**
             * 查询网点负责的区域id清单
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<List<Long>> findAreaIds(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点id列表获取网点区域列表
             *
             * @param servicePointIds
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointArea>> findServicePointAreasByServicePointIds(List<Long> servicePointIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 移除网点下的所有区域
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Integer> removeAreas(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 给网点分配区域
             *
             * @param servicePointId
             * @param areas
             * @return
             */
            @Override
            public MSResponse<Integer> assignAreas(Long servicePointId, List<Long> areas) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取区域ID列表复制
             *
             * @param mdServicePointArea
             * @return
             */
            @Override
            public MSResponse<MSPage<MDServicePointArea>> findListWithAreaIds(MDServicePointArea mdServicePointArea) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
