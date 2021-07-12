package com.wolfking.jeesite.ms.providersys.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysArea;
import com.wolfking.jeesite.ms.providersys.feign.MSSysAreaFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MSSysAreaFeignFallbackFactory implements FallbackFactory<MSSysAreaFeign> {
    @Override
    public MSSysAreaFeign create(Throwable throwable) {

        if(throwable != null) {
            log.error("MSSysAreaFeign FallbackFactory:{}", throwable.getMessage());
        }

        return new MSSysAreaFeign() {
            /**
             * 根据id从缓存中获取数据
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<SysArea> getFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据id从缓存中获取数据
             *
             * @param id
             * @param type
             * @return
             */
            @Override
            public MSResponse<SysArea> getFromCache(Long id, Integer type) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据区域类型从缓存中获取区域列表
             *
             * @param type
             * @param pageNo
             * @param pageSize
             * @return
             */
            @Override
            public MSResponse<List<SysArea>> findListByTypeFromCache(Integer type, Integer pageNo, Integer pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 为网点或师傅获取区域信息
             *
             * @param ids
             * @param pageNo
             * @param pageSize
             * @return
             */
            @Override
            public MSResponse<MSPage<SysArea>> findAreasForServicePointOrEngineer(List<Long> ids, Integer pageNo, Integer pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * @param areaIds
             * @return
             */
            @Override
            public MSResponse<List<SysArea>> findSpecListByIds(List<Long> areaIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 地址解析
             *
             * @param province 省
             * @param city     市
             * @param district 区
             * @return
             */
            @Override
            public MSResponse<String[]> decodeDistrictAddress(String province, String city, String district) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 地址解析
             *
             * @param province 省
             * @param city     市
             * @param district 区
             * @param street   街道
             * @return
             */
            @Override
            public MSResponse<String[]> decodeAddress(String province, String city, String district, String street) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
