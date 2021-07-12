package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerTimeliness;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerTimelinessFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerTimelinessFeignFallbackFactory.class)
public interface MSCustomerTimelinessFeign {

    /**
     * 根据customerId，areaId获取时效列表
     * @param customerId
     * @return
     */
    @GetMapping("/customerTimeliness/findListByCustomerId/{customerId}")
    MSResponse<List<MDCustomerTimeliness>> findListByCustomerId(@PathVariable("customerId") Long customerId);

    /**
     * 分页获取客户列表
     * @param mdCustomerTimeliness
     * @return
     */
    @PostMapping("/customerTimeliness/findList")
    MSResponse<MSPage<Long>> findList(@RequestBody MDCustomerTimeliness mdCustomerTimeliness);

    /**
     * 删除时效记录
     * @param mdCustomerTimeliness
     * @return
     */
    @DeleteMapping("/customerTimeliness/delete")
    MSResponse<Integer> delete(@RequestBody MDCustomerTimeliness mdCustomerTimeliness);

    /**
     * 批量添加时效记录
     * @param mdCustomerTimelinessList
     * @return
     */
    @PostMapping("/customerTimeliness/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDCustomerTimeliness> mdCustomerTimelinessList);
}
