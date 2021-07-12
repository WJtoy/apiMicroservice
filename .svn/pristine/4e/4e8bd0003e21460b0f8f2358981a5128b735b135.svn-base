package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointStation;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointStationFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSServicePointStationFeignFallbackFactory implements FallbackFactory<MSServicePointStationFeign> {
    @Override
    public MSServicePointStationFeign create(Throwable throwable) {
        return new MSServicePointStationFeign() {

            /*
            //  此方法作废 2019-12-27
            @Override
            public MSResponse<MDServicePointStation> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
            */

            /**
             * 根据网点Id和服务区域id从缓存中获取网点区域信息
             *
             * @param servicePointId
             * @param stationId
             * @return
             */
            @Override
            public MSResponse<MDServicePointStation> getFromCacheByPointIdAndStationId(Long servicePointId, Long stationId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*
           //  此方法作废 2019-12-27
           @Override
            public MSResponse<MDServicePointStation> getByServicePointIdAndAreaId(MDServicePointStation mdServicePointStation) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
            */

            /**
             * 通过网点id查找该网点是否有自动派单(即autoPlanFlag 为1)
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Long> autoPlanByServicePointId(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDServicePointStation>> findList(MDServicePointStation mdServicePointStation) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDServicePointStation>> findAutoPlanList(List<Long> subAreaIdList, int pageNo, int pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 查找网点覆盖的四级区域列表
             *
             * @param pageNo
             * @param pageSize
             * @return
             */
            @Override
            public MSResponse<MSPage<Long>> findCoverAreaList(int pageNo, int pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePointStation>> findListFromCacheByServicePointId(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDServicePointStation mdServicePointStation) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDServicePointStation mdServicePointStation) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDServicePointStation mdServicePointStation) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDServicePointStation>> batchInsertOrUpdate(List<MDServicePointStation> servicePointStations) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchDelete(List<MDServicePointStation> servicePointStations) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
