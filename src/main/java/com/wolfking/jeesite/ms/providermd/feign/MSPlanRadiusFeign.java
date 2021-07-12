package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDPlanRadius;
import com.wolfking.jeesite.ms.providermd.fallback.MSPlanRadiusFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSPlanRadiusFeignFallbackFactory.class)
public interface MSPlanRadiusFeign {
    @GetMapping("/planRadius/getById/{id}")
    MSResponse<MDPlanRadius> getById(@PathVariable("id") Long id);

    @GetMapping("/planRadius/findAllList")
    MSResponse<List<MDPlanRadius>> findAllList();

    @PostMapping("/planRadius/findList")
    MSResponse<MSPage<MDPlanRadius>> findList(@RequestBody MDPlanRadius mdPlanRadius);

    @PostMapping("/planRadius/insert")
    MSResponse<Integer> insert(@RequestBody MDPlanRadius mdPlanRadius);

    @PutMapping("/planRadius/update")
    MSResponse<Integer> update(@RequestBody MDPlanRadius mdPlanRadius);

    @DeleteMapping("/planRadius/enableOrDisable")
    MSResponse<Integer> enableOrDisable(@RequestBody MDPlanRadius mdPlanRadius);

    @GetMapping("/planRadius/getByAreaId/{areaId}")
    MSResponse<MDPlanRadius> getByAreaId(@PathVariable("areaId") Long areaId);
}
