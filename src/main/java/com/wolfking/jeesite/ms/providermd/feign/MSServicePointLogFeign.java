package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServicePointLog;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointLogFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSServicePointLogFeignFallbackFactory.class)
public interface MSServicePointLogFeign {
    /**
     * 新增网点日志
     * @param mdServicePointLog
     * @return
     */
    @PostMapping("/servicePointLog/insert")
    MSResponse<Integer> insert(MDServicePointLog mdServicePointLog);

    /**
     * 根据id获取网点日志
     * @param id
     * @return
     */
    @GetMapping("/servicePointLog/getById")
    MSResponse<MDServicePointLog> getById(@RequestParam("id") Long id);

    /**
     * 根据网点id获取网点历史备注信息
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointLog/findHisRemarks")
    MSResponse<List<MDServicePointLog>> findHisRemarks(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 根据网点id获取网点派单历史备注信息
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointLog/findHisPlanRemarks")
    MSResponse<List<MDServicePointLog>> findHisPlanRemarks(@RequestParam("servicePointId") Long servicePointId);
}
