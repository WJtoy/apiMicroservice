package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCancelTypeMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceTypeMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BCancelTypeMappingFeignFallbackFactory;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BServiceTypeMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BCancelTypeMappingFeignFallbackFactory.class)
public interface B2BCancelTypeMappingFeign {

    @GetMapping("/b2bCancelTypeMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BCancelTypeMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

    @GetMapping("/b2bCancelTypeMapping/get/{id}")
    MSResponse<B2BCancelTypeMapping> getCancelTypeMappingById(@PathVariable("id") Long id);

    @PostMapping("/b2bCancelTypeMapping/getList")
    MSResponse<MSPage<B2BCancelTypeMapping>> getCancelTypeMappingList(@RequestBody B2BCancelTypeMapping cancelTypeMapping);

    @PostMapping("/b2bCancelTypeMapping/insert")
    MSResponse<B2BCancelTypeMapping> insertCancelTypeMapping(@RequestBody B2BCancelTypeMapping cancelTypeMapping);

    @PostMapping("/b2bCancelTypeMapping/update")
    MSResponse<Integer> updateCancelTypeMapping(@RequestBody B2BCancelTypeMapping cancelTypeMapping);

    @PostMapping("/b2bCancelTypeMapping/delete")
    MSResponse<Integer> deleteCancelTypeMapping(@RequestBody B2BCancelTypeMapping cancelTypeMapping);

}
