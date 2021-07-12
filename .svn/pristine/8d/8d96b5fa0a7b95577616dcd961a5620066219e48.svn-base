package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerUrgent;
import com.wolfking.jeesite.modules.md.entity.UrgentCustomer;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerUrgentFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerUrgentFeignFallbackFactory.class)
public interface MSCustomerUrgentFeign {
    /**
     * 根据customerId，areaId获取时效列表
     * @param customerId
     * @return
     */
    @GetMapping("/customerUrgent/findListByCustomerId/{customerId}")
    MSResponse<List<MDCustomerUrgent>> findListByCustomerId(@PathVariable("customerId") Long customerId);

    /**
     * 分页获取客户列表
     * @param mdCustomerUrgent
     * @return
     */
    @PostMapping("/customerUrgent/findList")
    MSResponse<MSPage<Long>> findList(@RequestBody MDCustomerUrgent mdCustomerUrgent);

    /**
     * 删除时效记录
     * @param mdCustomerUrgent
     * @return
     */
    @DeleteMapping("/customerUrgent/delete")
    MSResponse<Integer> delete(@RequestBody MDCustomerUrgent mdCustomerUrgent);

    /**
     * 批量时效记录
     * @param mdCustomerUrgentList
     * @return
     */
    @PostMapping("/customerUrgent/batchInsert")
    MSResponse<Integer> batchInsert(@RequestBody List<MDCustomerUrgent> mdCustomerUrgentList);
}
