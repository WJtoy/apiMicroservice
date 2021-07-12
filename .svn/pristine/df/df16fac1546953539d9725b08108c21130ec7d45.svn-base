package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDMaterialCategory;
import com.wolfking.jeesite.ms.providermd.fallback.MSMaterialCategoryFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSMaterialCategoryFeignFallbackFactory.class)
public interface MSMaterialCategoryFeign {
    @GetMapping("/materialCategory/getById/{id}")
    MSResponse<MDMaterialCategory> getById(@PathVariable("id") Long id);

    @GetMapping("/materialCategory/findAllList")
    MSResponse<List<MDMaterialCategory>> findAllList();

    @GetMapping("/materialCategory/findAllListWithIdAndName")
    MSResponse<List<NameValuePair<Long, String>>> findAllListWithIdAndName();

    @PostMapping("/materialCategory/findList")
    MSResponse<MSPage<MDMaterialCategory>> findList(@RequestBody MDMaterialCategory mdMaterialCategory);

    @PostMapping("/materialCategory/insert")
    MSResponse<Integer> insert(@RequestBody MDMaterialCategory mdMaterialCategory);

    @PutMapping("/materialCategory/update")
    MSResponse<Integer> update(@RequestBody MDMaterialCategory mdMaterialCategory);

    @DeleteMapping("/materialCategory/delete")
    MSResponse<Integer> delete(@RequestBody MDMaterialCategory mdMaterialCategory);

    @PostMapping("/materialCategory/getIdByName/{name}")
    MSResponse<Long> getIdByName(@PathVariable("name") String name);
}
