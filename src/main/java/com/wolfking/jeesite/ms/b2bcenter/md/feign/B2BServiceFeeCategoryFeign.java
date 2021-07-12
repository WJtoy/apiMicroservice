package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceFeeCategory;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BServiceFeeCategoryFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BServiceFeeCategoryFeignFallbackFactory.class)
public interface B2BServiceFeeCategoryFeign {

    @GetMapping("/b2bServiceFeeCategory/get/{id}")
    MSResponse<B2BServiceFeeCategory> get(@PathVariable("id") Long id);

    @PostMapping("/b2bServiceFeeCategory/getList")
    MSResponse<MSPage<B2BServiceFeeCategory>> getList(@RequestBody B2BServiceFeeCategory serviceFeeCategory);

    @PostMapping("/b2bServiceFeeCategory/insert")
    MSResponse<B2BServiceFeeCategory> insert(@RequestBody B2BServiceFeeCategory serviceFeeCategory);

    @PostMapping("/b2bServiceFeeCategory/update")
    MSResponse<Integer> update(@RequestBody B2BServiceFeeCategory serviceFeeCategory);

    @PostMapping("/b2bServiceFeeCategory/delete")
    MSResponse<Integer> delete(@RequestBody B2BServiceFeeCategory serviceFeeCategory);

    @GetMapping("/b2bServiceFeeCategory/getListByDataSource/{dataSource}")
    MSResponse<List<B2BServiceFeeCategory>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

}
