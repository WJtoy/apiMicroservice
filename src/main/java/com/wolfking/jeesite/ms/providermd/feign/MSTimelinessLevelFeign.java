package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDTimelinessLevel;
import com.wolfking.jeesite.ms.providermd.fallback.MSTimelinessLevelFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSTimelinessLevelFeignFallbackFactory.class)
public interface MSTimelinessLevelFeign {
    @GetMapping("/timelinessLevel/getById")
    MSResponse<MDTimelinessLevel> getById(@RequestParam("id") Long id);

    /**
     *从缓存获取所有时效等级
     **/
    @GetMapping("/timelinessLevel/findAllList")
    MSResponse<List<MDTimelinessLevel>> findAllList();

    @PostMapping("/timelinessLevel/findList")
    MSResponse<MSPage<MDTimelinessLevel>> findList(@RequestBody MDTimelinessLevel mdTimelinessLevel);

    @PostMapping("/timelinessLevel/insert")
    MSResponse<Integer> insert(@RequestBody MDTimelinessLevel mdTimelinessLevel);

    @PutMapping("/timelinessLevel/update")
    MSResponse<Integer> update(@RequestBody MDTimelinessLevel mdTimelinessLevel);

    @DeleteMapping("/timelinessLevel/delete")
    MSResponse<Integer> delete(@RequestBody MDTimelinessLevel mdTimelinessLevel);

}
