package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerAddress;
import com.kkl.kklplus.entity.md.MDRegionPermission;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerAddressFeign;
import com.wolfking.jeesite.ms.providermd.feign.MSRegionPermissionFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSRegionPermissionFeignFallbackFactory implements FallbackFactory<MSRegionPermissionFeign> {
    @Override
    public MSRegionPermissionFeign create(Throwable throwable) {

        return new MSRegionPermissionFeign() {

            @Override
            public MSResponse<List<MDRegionPermission>> findListWithCategory(MDRegionPermission regionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDRegionPermission>> findListByAreaIdAndCategory(MDRegionPermission regionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchSave(List<MDRegionPermission> regionPermissions) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDRegionPermission>> findListByCategoryAndCityId(MDRegionPermission regionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> getSubAreaStatusFromCacheForSD(MDRegionPermission mdRegionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> getRemoteFeeStatusFromCacheForSD(MDRegionPermission mdRegionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据品类,市,区(县),街道(productCategoryId,cityId,subAreaId)获取偏远区域状态->工单（偏远区域）
             *
             * @param regionPermission
             * @return
             */
            @Override
            public MSResponse<Integer> getRemoteAreaStatusFromCacheForSD(MDRegionPermission regionPermission) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }
}
