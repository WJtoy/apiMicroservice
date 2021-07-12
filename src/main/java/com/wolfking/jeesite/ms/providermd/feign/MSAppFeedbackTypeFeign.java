package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.kkl.kklplus.entity.md.dto.MDAppFeedbackTypeDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSAppFeedbackTypeeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * App反馈类型
 */
@FeignClient(name = "provider-md", fallbackFactory= MSAppFeedbackTypeeFeignFallbackFactory.class)
public interface MSAppFeedbackTypeFeign {

    /**
     * 获取所有app反馈类型
     */
    @GetMapping("/appFeedbackType/findAllList")
    MSResponse<List<MDAppFeedbackType>> findAllList();

    /**
     * 根据id从缓存读取
     */
    @GetMapping("/appFeedbackType/getByIdFromCache")
    MSResponse<MDAppFeedbackType> getByIdFromCache(@RequestParam("id") Long id);

}
