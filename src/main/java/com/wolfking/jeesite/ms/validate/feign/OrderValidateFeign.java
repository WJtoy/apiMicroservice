package com.wolfking.jeesite.ms.validate.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.validate.OrderValidate;
import com.wolfking.jeesite.ms.validate.feign.fallback.OrderValidateFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * App鉴定单
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = OrderValidateFallbackFactory.class)
public interface OrderValidateFeign {

    /**
     * 创建鉴定单
     */
    @PostMapping("/orderValidate/insert")
    MSResponse<OrderValidate> saveValidate(@RequestBody OrderValidate validate);

    /**
     * 鉴定单审核通过
     */
    @PostMapping("/orderValidate/approve")
    MSResponse<Integer> approve(@RequestBody OrderValidate validate);

    /**
     * 鉴定审核驳回
     */
    @PostMapping("/orderValidate/reject")
    MSResponse<Integer> reject(@RequestBody OrderValidate validate);

    /**
     * 根据工单id和分片获取鉴定单列表
     */
    @GetMapping("/orderValidate/findListByOrderId")
    MSResponse<List<OrderValidate>> findListByOrderId(@RequestParam("orderId") Long orderId,
                                                      @RequestParam("quarter") String quarter);
}
