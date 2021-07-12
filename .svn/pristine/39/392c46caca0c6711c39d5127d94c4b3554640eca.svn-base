package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorAction;
import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.kkl.kklplus.entity.md.dto.MDErrorActionDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSActionCodeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory= MSActionCodeFeignFallbackFactory.class)
public interface MSActionCodeFeign {

    /**
     * 通过产品和故障代码获取故障处理id,故障名称，服务类型及服务类型id
     * @param errorCodeId
     * @param productId
     * @return
     */
    @GetMapping("/actionCode/findListByProductAndErrorCode")
    MSResponse<List<MDActionCodeDto>> findListByProductAndErrorCode(@RequestParam("errorCodeId") Long errorCodeId, @RequestParam("productId") Long productId);


    /**
     * 根据产品id，或id获取处理代码列表
     * @param id
     * @param productId
     * @return
     */
    @GetMapping("/actionCode/findListByProductId")
    MSResponse<List<MDActionCodeDto>> findListByProductId(@RequestParam("id") Long id, @RequestParam("productId") Long productId);
}
