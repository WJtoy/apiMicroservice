package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDUrgentLevel;
import com.wolfking.jeesite.ms.providermd.fallback.MSUrgentLevelFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSUrgentLevelFeignFallbackFactory.class)
public interface MSUrgentLevelFeign {
    @GetMapping("/urgentLevel/getById")
    MSResponse<MDUrgentLevel> getById(@RequestParam("id") Long id);

    /**
     *根据id从缓存获取
     **/
    @GetMapping("/urgentLevel/getFromCache")
    MSResponse<MDUrgentLevel> getFromCache(@RequestParam("id") Long id);

    /**
     *从缓存获取所有加急等级
     **/
    @GetMapping("/urgentLevel/findAllList")
    MSResponse<List<MDUrgentLevel>> findAllList();

    @PostMapping("/urgentLevel/findList")
    MSResponse<MSPage<MDUrgentLevel>> findList(@RequestBody MDUrgentLevel mdUrgentLevel);

    @PostMapping("/urgentLevel/insert")
    MSResponse<Integer> insert(@RequestBody MDUrgentLevel mdUrgentLevel);

    @PutMapping("/urgentLevel/update")
    MSResponse<Integer> update(@RequestBody MDUrgentLevel mdUrgentLevel);

    @DeleteMapping("/urgentLevel/delete")
    MSResponse<Integer> delete(@RequestBody MDUrgentLevel mdUrgentLevel);

}
