package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceFeeItem;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BServiceFeeItemFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BServiceFeeItemFeignFallbackFactory.class)
public interface B2BServiceFeeItemFeign {

    @GetMapping("/serviceFeeItem/get/{id}")
    MSResponse<B2BServiceFeeItem> get(@PathVariable("id") Long id);

    @PostMapping("/serviceFeeItem/getList")
    MSResponse<MSPage<B2BServiceFeeItem>> getList(@RequestBody B2BServiceFeeItem serviceFeeItem);

    @PostMapping("/serviceFeeItem/insert")
    MSResponse<B2BServiceFeeItem> insert(@RequestBody B2BServiceFeeItem serviceFeeItem);

    @PostMapping("/serviceFeeItem/update")
    MSResponse<Integer> update(@RequestBody B2BServiceFeeItem serviceFeeItem);

    @PostMapping("/serviceFeeItem/delete")
    MSResponse<Integer> delete(@RequestBody B2BServiceFeeItem serviceFeeItem);

    @GetMapping("/serviceFeeItem/getListByDataSource/{dataSource}")
    MSResponse<List<B2BServiceFeeItem>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

}
