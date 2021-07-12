package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointStation;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointStationFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSServicePointStationFeignFallbackFactory.class)
public interface MSServicePointStationFeign {

    /**
     * 根据id获取网点服务点
     * @param id
     * @return
     */
    /*
    // 此方法作废 2019-12-27
    @GetMapping("/servicePointStation/getById/{id}")
    MSResponse<MDServicePointStation> getById(@PathVariable("id") Long id);
    */

    /**
     * 根据网点Id和服务区域id从缓存中获取网点区域信息
     * @param servicePointId
     * @param stationId
     * @return
     */
    @GetMapping("/servicePointStation/getFromCacheByPointIdAndStationId")
    MSResponse<MDServicePointStation> getFromCacheByPointIdAndStationId(@RequestParam("servicePointId") Long servicePointId, @RequestParam("stationId") Long stationId);

    /**
     * 通过网点id和区域id查询服务点
     * @param mdServicePointStation
     * @return
     */
    /*
    // 此方法作废 2019-12-27
    @PostMapping("servicePointStation/getByServicePointIdAndAreaId")
    MSResponse<MDServicePointStation> getByServicePointIdAndAreaId(@RequestBody MDServicePointStation mdServicePointStation);
    */

    /**
     * 通过网点id查找该网点是否有自动派单(即autoPlanFlag 为1)
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointStation/autoPlanByServicePointId")
    MSResponse<Long> autoPlanByServicePointId(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 根据服务网点获取数据(用于web加载缓存)
     * @param mdServicePointStation
     * @return
     */
    @PostMapping("/servicePointStation/findList")
    MSResponse<MSPage<MDServicePointStation>> findList(@RequestBody MDServicePointStation mdServicePointStation);

    /**
     * 根据服务网点获取数据
     * @param areas
     * @return
     */
    @PostMapping("/servicePointStation/findAutoPlanList")
    MSResponse<MSPage<MDServicePointStation>> findAutoPlanList(@RequestBody List<Long> areas, @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize);

    /**
     * 查找网点覆盖的四级区域列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/servicePointStation/findCoverAreaList")
    MSResponse<MSPage<Long>> findCoverAreaList(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize);

    /**
     * 根据网点id从缓存中获取数据
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointStation/findByServicePointId")
    MSResponse<List<MDServicePointStation>> findListFromCacheByServicePointId(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 添加数据
     * @param mdServicePointStation
     * @return
     */
    @PostMapping("/servicePointStation/insert")
    MSResponse<Integer> insert(@RequestBody MDServicePointStation mdServicePointStation);

    /**
     * 修改数据
     * @param mdServicePointStation
     * @return
     */
    @PutMapping("/servicePointStation/update")
    MSResponse<Integer> update(@RequestBody MDServicePointStation mdServicePointStation);

    /**
     * 删除(逻辑删除)
     * @param mdServicePointStation
     * @return
     */
    @DeleteMapping("/servicePointStation/delete")
    MSResponse<Integer> delete(@RequestBody MDServicePointStation mdServicePointStation);

    /**
     * 批量添加或者修改
     * @param servicePointStations
     * @return
     */
    @PostMapping("/servicePointStation/batchInsertOrUpdate")
    MSResponse<List<MDServicePointStation>> batchInsertOrUpdate(@RequestBody List<MDServicePointStation> servicePointStations);

    /**
     * 批量删除
     * @param servicePointStations
     * @return
     */
    @DeleteMapping("/servicePointStation/batchDelete")
    MSResponse<Integer> batchDelete(@RequestBody List<MDServicePointStation> servicePointStations);

}
