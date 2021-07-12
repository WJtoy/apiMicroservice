package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceTypeMapping;
import com.kkl.kklplus.entity.common.MSPage;
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
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BServiceTypeMappingFeignFallbackFactory.class)
public interface B2BServiceTypeMappingFeign {

    @GetMapping("/b2BServiceTypeMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BServiceTypeMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

    @GetMapping("/b2BServiceTypeMapping/get/{id}")
    MSResponse<B2BServiceTypeMapping> getServiceTypeMappingById(@PathVariable("id") Long id);

    @PostMapping("/b2BServiceTypeMapping/getList")
    MSResponse<MSPage<B2BServiceTypeMapping>> getServiceTypeMappingList(@RequestBody B2BServiceTypeMapping serviceTypeMapping);

    @PostMapping("/b2BServiceTypeMapping/insert")
    MSResponse<B2BServiceTypeMapping> insertServiceTypeMapping(@RequestBody B2BServiceTypeMapping serviceTypeMapping);

    @PostMapping("/b2BServiceTypeMapping/update")
    MSResponse<Integer> updateServiceTypeMapping(@RequestBody B2BServiceTypeMapping serviceTypeMapping);

    @PostMapping("/b2BServiceTypeMapping/delete")
    MSResponse<Integer> deleteServiceTypeMapping(@RequestBody B2BServiceTypeMapping serviceTypeMapping);

    @PostMapping("/b2BServiceTypeMapping/getByField")
    MSResponse<Long> getByField(@RequestBody B2BServiceTypeMapping serviceTypeMapping);

}
