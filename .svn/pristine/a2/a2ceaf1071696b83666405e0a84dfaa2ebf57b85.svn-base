package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BWarrantyMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BWarrantyMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BWarrantyMappingFeignFallbackFactory.class)
public interface B2BWarrantyMappingFeign {

    @GetMapping("/b2bWarrantyMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BWarrantyMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

    @GetMapping("/b2bWarrantyMapping/get/{id}")
    MSResponse<B2BWarrantyMapping> getById(@PathVariable("id") Long id);

    @PostMapping("/b2bWarrantyMapping/getList")
    MSResponse<MSPage<B2BWarrantyMapping>> getList(@RequestBody B2BWarrantyMapping warrantyMapping);

    @PostMapping("/b2bWarrantyMapping/insert")
    MSResponse<B2BWarrantyMapping> insert(@RequestBody B2BWarrantyMapping warrantyMapping);

    @PostMapping("/b2bWarrantyMapping/update")
    MSResponse<Integer> update(@RequestBody B2BWarrantyMapping warrantyMapping);

    @PostMapping("/b2bWarrantyMapping/delete")
    MSResponse<Integer> delete(@RequestBody B2BWarrantyMapping warrantyMapping);

}
