package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDProductTypeItem;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductTypeItemFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductTypeItemFeignFallbackFactory.class)
public interface MSProductTypeItemFeign {

    /**
     * 分页查询
     * */
    @GetMapping("/productTypeItem/findAllList")
    MSResponse<List<MDProductTypeItem>> findAllList();


    /**
     * 根据productTypeId获取数据productTypeItem集合
     * */
    @GetMapping("/productTypeItem/findListByProductTypeId")
    MSResponse<List<MDProductTypeItem>> findListByProductTypeId(@RequestParam("productTypeId") Long productTypeId);

}
