package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.ms.providermd.fallback.AuxiliaryMaterialItemFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "provider-md", fallbackFactory = AuxiliaryMaterialItemFeignFallbackFactory.class)
public interface AuxiliaryMaterialItemFeign {

    @PostMapping("/auxiliaryMaterialItem/getListByProductId")
    MSResponse<List<MDAuxiliaryMaterialItem>> getListByProductId(@RequestBody List<String> productIds);

    @GetMapping("/auxiliaryMaterialItem/findAllList")
    MSResponse<List<MDAuxiliaryMaterialItem>> findAllList();

}
