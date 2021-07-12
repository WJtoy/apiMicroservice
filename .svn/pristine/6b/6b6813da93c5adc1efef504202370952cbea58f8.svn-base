package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerArea;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerAreaFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSEngineerAreaFeignFallbackFactory implements FallbackFactory<MSEngineerAreaFeign> {
    @Override
    public MSEngineerAreaFeign create(Throwable throwable) {
        return new MSEngineerAreaFeign() {
            /**
             * 通过安维id获取安维对应的区域id
             *
             * @param engineerId
             * @return
             */
            @Override
            public MSResponse<List<Long>> findEngineerAreaIds(Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过安维id获取安维区域id列表
             *
             * @param engineerIds
             * @return
             */
            @Override
            public MSResponse<List<MDEngineerArea>> findEngineerAreasWithIds(List<Long> engineerIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 给安维人员分配区域id
             *
             * @param engineerId
             * @param areas
             * @return
             */
            @Override
            public MSResponse<Integer> assignEngineerAreas(List<Long> areas, Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据安维人员id删除其对应的区域信息
             *
             * @param engineerId
             * @return
             */
            @Override
            public MSResponse<Integer> removeEnigineerAreas(Long engineerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据安维人员，区域id列表删除不在当前区域的安维区域
             *
             * @param servicePointId
             * @param areas
             * @return
             */
            @Override
            public MSResponse<Integer> deleteEnigineerAreas(Long servicePointId, List<Long> areas) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
