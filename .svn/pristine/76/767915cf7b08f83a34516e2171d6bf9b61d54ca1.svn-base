package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDGrade;
import com.wolfking.jeesite.ms.providermd.fallback.MSGradeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSGradeFeignFallbackFactory.class)
public interface MSGradeFeign {
    @GetMapping("/grade/getById/{id}")
    MSResponse<MDGrade> getById(@PathVariable("id") Long id);

    @GetMapping("/grade/findAllList")
    MSResponse<List<MDGrade>> findAllList();

    @PostMapping("/grade/findList")
    MSResponse<MSPage<MDGrade>> findList(@RequestBody MDGrade mdGrade);

    @PostMapping("/grade/insert")
    MSResponse<Integer> insert(@RequestBody MDGrade mdGrade);

    @PutMapping("/grade/update")
    MSResponse<Integer> update(@RequestBody MDGrade mdGrade);

    @DeleteMapping("/grade/delete")
    MSResponse<Integer> delete(@RequestBody MDGrade mdGrade);

    @GetMapping("/grade/findAllEnabledGradeAndItems")
    MSResponse<List<MDGrade>> findAllEnabledGradeAndItems();
}
