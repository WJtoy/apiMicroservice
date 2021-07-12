package com.wolfking.jeesite.ms.providersys.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysAppNotice;
import com.wolfking.jeesite.ms.providersys.fallback.MSSysAppNoticeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name ="provider-sys", fallbackFactory = MSSysAppNoticeFeignFallbackFactory.class)
public interface MSSysAppNoticeFeign {
    /**
     * 根据用户Id获取单个手机App通知对象Id
     * @param userId
     * @return
     */
    @GetMapping("/appNotice/getOneIdByUserId/{userId}")
    MSResponse<Long> getOneIdByUserId(@PathVariable("userId") Long userId);

    /**
     * 根据用户id获取手机App通知
     * @param userId
     * @return
     */
    @GetMapping("/appNotice/getByUserId/{userId}")
    MSResponse<SysAppNotice> getByUserId(@PathVariable("userId") Long userId);

    /**
     * 添加手机App通知
     * @param sysAppNotice
     * @return
     */
    @PostMapping("/appNotice/insert")
    MSResponse<Integer> insert(@RequestBody SysAppNotice sysAppNotice);


    @PutMapping("/appNotice/updateByUserId")
    MSResponse<Integer> updateByUserId(@RequestBody SysAppNotice sysAppNotice);

}
