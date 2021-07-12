package com.wolfking.jeesite.ms.providersys.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysAppActive;
import com.wolfking.jeesite.ms.providersys.fallback.MSSysAppActiveFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name ="provider-sys", fallbackFactory = MSSysAppActiveFeignFallbackFactory.class)
public interface MSSysAppActiveFeign {


    @PostMapping("/appActive/save")
    MSResponse<Integer> saveActiveInfo(@RequestBody SysAppActive sysAppActive);

}
