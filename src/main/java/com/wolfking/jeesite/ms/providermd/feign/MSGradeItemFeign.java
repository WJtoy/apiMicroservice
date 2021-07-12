package com.wolfking.jeesite.ms.providermd.feign;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDGradeItem;
import com.wolfking.jeesite.ms.providermd.fallback.MSGradeItemFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSGradeItemFeignFallbackFactory.class)
public interface MSGradeItemFeign {
    @GetMapping("/gradeItem/getById/{id}")
    MSResponse<MDGradeItem> getById(@PathVariable("id") Long id);

    @GetMapping("/gradeItem/findListByGradeId/{gradeId}")
    MSResponse<List<MDGradeItem>> findListByGradeId(@PathVariable("gradeId") Long gradeId);

    @PostMapping("/gradeItem/insert")
    MSResponse<Integer> insert(@RequestBody MDGradeItem mdGradeItem);

    @PutMapping("/gradeItem/update")
    MSResponse<Integer> update(@RequestBody MDGradeItem mdGradeItem);

    @DeleteMapping("/gradeItem/delete")
    MSResponse<Integer> delete(@RequestBody MDGradeItem mdGradeItem);
}
