package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductPicMapping;
import com.kkl.kklplus.entity.md.MDProductPrice;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductPicMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductPicMappingFeignFallbackFactory.class)
public interface MSProductPicMappingFeign {
    @PostMapping("/productPic/findList")
    MSResponse<MSPage<MDProductPicMapping>> findList(@RequestBody MDProductPicMapping mdProductPicMapping);

    @GetMapping("/productPic/findAllList")
    MSResponse<List<MDProductPicMapping>> findAllList();

    @GetMapping("/productPic/get/{id}")
    MSResponse<MDProductPicMapping> get(@PathVariable("id") Long id);

    @GetMapping("/productPic/getByProductId/{productId}")
    MSResponse<MDProductPicMapping> getByProductId(@PathVariable("productId") Long productId);

    @PostMapping("/productPic/insert")
    MSResponse<Integer> insert(@RequestBody MDProductPicMapping mdProductPicMapping);

    @PutMapping("/productPic/update")
    MSResponse<Integer> update(@RequestBody MDProductPicMapping mdProductPicMapping);

    @DeleteMapping("/productPic/delete")
    MSResponse<Integer> delete(@RequestBody MDProductPicMapping mdProductPicMapping);
}
