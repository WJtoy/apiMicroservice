package com.wolfking.jeesite.ms.lb.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.wolfking.jeesite.ms.lb.sd.fallback.LbOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 新迎燕B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-lb", fallbackFactory = LbOrderFeignFallbackFactory.class)
public interface LbOrderFeign {

    /**
     * 申请配件
     */
    @PostMapping("material/new")
    MSResponse newMaterial(@RequestBody B2BMaterial material);


    @GetMapping("material/updateAuditFlag/{id}")
    MSResponse updateAuditFlag(@PathVariable("id") Long id);

    @GetMapping("material/updateDeliverFlag/{id}")
    MSResponse updateDeliverFlag(@PathVariable("id") Long id);
}
