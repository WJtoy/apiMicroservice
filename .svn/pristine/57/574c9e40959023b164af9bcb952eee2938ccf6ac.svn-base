package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointArea;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointAreaFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 2019-11-12
@FeignClient(name="provider-md", fallbackFactory = MSServicePointAreaFeignFallbackFactory.class)
public interface MSServicePointAreaFeign {
    /**
     * 查询网点负责的区域id清单
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointArea/findAreaIds")
    MSResponse<List<Long>> findAreaIds(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 根据网点id列表获取网点区域列表
     * @param servicePointIds
     * @return
     */
    @PostMapping("/servicePointArea/findServicePointAreasByServicePointIds")
    MSResponse<List<MDServicePointArea>> findServicePointAreasByServicePointIds(@RequestBody List<Long> servicePointIds);

    /**
     * 移除网点下的所有区域
     * @param servicePointId
     * @return
     */
    @DeleteMapping("/servicePointArea/removeAreas")
    MSResponse<Integer> removeAreas(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 给网点分配区域
     * @param servicePointId
     * @param areas
     * @return
     */
    @PostMapping("/servicePointArea/assignAreas")
    MSResponse<Integer> assignAreas(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<Long> areas);

    /**
     * 分页获取区域ID列表复制
     * @param mdServicePointArea
     * @return
     */
    @PostMapping("/servicePointArea/findListWithAreaIds")
    MSResponse<MSPage<MDServicePointArea>> findListWithAreaIds(@RequestBody MDServicePointArea mdServicePointArea);
}
