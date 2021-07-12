package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.dto.MDProductSpecDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductSpecTypeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品规格
 * */
@FeignClient(name="provider-md", fallbackFactory = MSProductSpecTypeFeignFallbackFactory.class)
public interface MSProductSpecTypeFeign {

    /**
     * 根据产品一级分类和二级分类获取产品规格以及参数
     * @param productTypeId
     * @param productTypeItemId
     * */
    @GetMapping("/productSpecType/findListByTypeIdAndItemId")
    MSResponse<List<MDProductSpecDto>> findListByTypeIdAndItemId(@RequestParam("productTypeId") Long productTypeId,
                                                                 @RequestParam("productTypeItemId") Long productTypeItemId );

}
