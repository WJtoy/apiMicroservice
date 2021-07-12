package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDMaterial;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.ms.providermd.fallback.MSMaterialFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSMaterialFeignFallbackFactory.class)
public interface MSMaterialFeign {
    @GetMapping("/material/getById/{id}")
    MSResponse<MDMaterial> getById(@PathVariable("id") Long id);

    @GetMapping("/material/findAllList")
    MSResponse<List<MDMaterial>> findAllList();

    @PostMapping("/material/findList")
    MSResponse<MSPage<MDMaterial>> findList(@RequestBody MDMaterial mdMaterial);

    @PostMapping("/material/insert")
    MSResponse<Integer> insert(@RequestBody MDMaterial mdMaterial);

    @PutMapping("/material/update")
    MSResponse<Integer> update(@RequestBody MDMaterial mdMaterial);

    @DeleteMapping("/material/delete")
    MSResponse<Integer> delete(@RequestBody MDMaterial mdMaterial);

    @PostMapping("/material/getIdByName/{name}")
    MSResponse<Long> getIdByName(@PathVariable("name") String name);

    @GetMapping("/material/getByMaterialCategoryId/{materialCategoryId}")
    MSResponse<Long> getByMaterialCategoryId(@PathVariable("materialCategoryId") Long materialCategoryId);

    @GetMapping("/material/findMaterialListByIds")
    MSResponse<List<MDMaterial>> findMaterialListByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/material/findMaterialIdByProductId/{productId}")
    MSResponse<List<MDProductMaterial>> findMaterialIdByProductId(@PathVariable("productId") Long productId);

    @GetMapping("/material/findProductMaterialListByProductIdList")
    MSResponse<List<MDProductMaterial>> findProductMaterialListByProductIdList(@RequestParam("productIds") List<Long> productIds);


}
