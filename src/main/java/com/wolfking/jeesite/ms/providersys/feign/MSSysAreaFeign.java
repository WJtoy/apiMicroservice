package com.wolfking.jeesite.ms.providersys.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysArea;
import com.wolfking.jeesite.ms.providersys.fallback.MSSysAreaFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name ="provider-sys",fallbackFactory = MSSysAreaFeignFallbackFactory.class)
public interface MSSysAreaFeign {

    /**
     * 根据id从缓存中获取数据
     * @param id
     * @return
     */
    @GetMapping("/area/getFromCache/{id}")
    MSResponse<SysArea> getFromCache(@PathVariable("id") Long id);

    /**
     * 根据id从缓存中获取数据
     * @param id
     * @return
     */
    @GetMapping("/area/getFromCache")
    MSResponse<SysArea> getFromCache(@RequestParam("id") Long id, @RequestParam("type") Integer type);

    /**
     * 根据区域类型从缓存中获取区域列表
     * @param type
     * @return
     */
    @GetMapping("/area/findListByTypeFromCache")
    MSResponse<List<SysArea>> findListByTypeFromCache(@RequestParam("type") Integer type, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    /**
     * 为网点或师傅获取区域信息
     * @param ids
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/area/findAreasForServicePointOrEngineer")
    MSResponse<MSPage<SysArea>> findAreasForServicePointOrEngineer(@RequestBody List<Long> ids, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);

    /**
     *
     * @param areaIds
     * @return
     */
    @PostMapping("/area/findSpecListByIds")
    MSResponse<List<SysArea>> findSpecListByIds(@RequestBody List<Long> areaIds);

    /**
     * 地址解析
     * @param province 省
     * @param city  市
     * @param district  区
     * @return
     */
    @PostMapping("/area/decodeDistrictAddress")
    MSResponse<String[]> decodeDistrictAddress(@RequestParam("province") String province, @RequestParam("city") String city, @RequestParam("district") String district);

    /**
     * 地址解析
     * @param province 省
     * @param city  市
     * @param district  区
     * @param street  街道
     * @return
     */
    @PostMapping("/area/decodeAddress")
    MSResponse<String[]> decodeAddress(@RequestParam("province") String province, @RequestParam("city") String city, @RequestParam("district") String district,  @RequestParam("street") String street);

}
