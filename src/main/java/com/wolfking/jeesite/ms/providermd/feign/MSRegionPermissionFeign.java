package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDRegionPermission;
import com.wolfking.jeesite.ms.providermd.fallback.MSRegionPermissionFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSRegionPermissionFeignFallbackFactory.class)
public interface MSRegionPermissionFeign {
    /**
     * 根据品类和城市获取区域设置信息
     * @return
     */
    @PostMapping("/regionPermission/findListWithCategory")
    MSResponse<List<MDRegionPermission>> findListWithCategory(@RequestBody MDRegionPermission regionPermission);

    @PostMapping("/regionPermission/findListByAreaIdAndCategory")
    MSResponse<List<MDRegionPermission>> findListByAreaIdAndCategory(@RequestBody MDRegionPermission regionPermission);

    /**
     * 批量操作
     * @param regionPermissions
     * @return
     */
    @PostMapping("/regionPermission/batchSave")
    MSResponse<Integer> batchSave(@RequestBody List<MDRegionPermission> regionPermissions);

    /**
     * 根据城市和产品品类获取启用区域
     * @param regionPermission
     * @return
     */
    @PostMapping("/regionPermission/findListByCategoryAndCityId")
    MSResponse<List<MDRegionPermission>> findListByCategoryAndCityId(@RequestBody MDRegionPermission regionPermission);

    /**
     * 根据省市区街道判断是否有可突击区域
     * @param mdRegionPermission
     * @return
     */
    @PostMapping("regionPermission/getSubAreaStatusFromCacheForSD")
    MSResponse<Integer> getSubAreaStatusFromCacheForSD(@RequestBody MDRegionPermission mdRegionPermission);


    /**
     * 根据市区街道街道是否有远程费
     * @param mdRegionPermission
     * @return
     */
    @PostMapping("/regionPermission/getRemoteFeeStatusFromCacheForSD")
    MSResponse<Integer> getRemoteFeeStatusFromCacheForSD(@RequestBody MDRegionPermission mdRegionPermission);

    /**
     * 根据品类,市,区(县),街道(productCategoryId,cityId,subAreaId)获取偏远区域状态->工单（偏远区域）
     * @param regionPermission
     * @return
     */
    @PostMapping("regionPermission/getRemoteAreaStatusFromCacheForSD")
    MSResponse<Integer> getRemoteAreaStatusFromCacheForSD(@RequestBody MDRegionPermission regionPermission);
}
