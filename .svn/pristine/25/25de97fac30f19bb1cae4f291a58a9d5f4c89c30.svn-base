package com.wolfking.jeesite.ms.feign.sys;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysUser;
import com.wolfking.jeesite.ms.fallback.sys.MSUserFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author Zhoucy
 * @date 2018/9/25 10:11
 **/
@FeignClient(name = "provider-user", fallbackFactory = MSUserFeignFallbackFactory.class)
public interface MSUserFeign {

    /**
     * 根据用户Id获取用户信息
     */
    @GetMapping("/user/get/{id}")
    MSResponse<SysUser> get(@PathVariable("id") Long id);

    /**
     * 根据用户Id获取用户名
     */
    @GetMapping("/user/getName/{id}")
    MSResponse<String> getName(@PathVariable("id") Long id);

    /**
     * 根据用户类型获取用户信息
     */
    @GetMapping("/user/getListByUserType/{userType}")
    MSResponse<List<SysUser>> getListByUserType(@PathVariable("userType") Integer userType);

    /**
     * 根据用户类型获取用户信息
     */
    @GetMapping("/user/getMapByUserType/{userType}")
    MSResponse<Map<Long, SysUser>> getMapByUserType(@PathVariable("userType") Integer userType);

    /**
     * 根据用户类型获取用户名称
     */
    @GetMapping("/user/getNamesByUserType/{userType}")
    MSResponse<Map<Long, String>> getNamesByUserType(@PathVariable("userType") Integer userType);

    /**
     * 根据用户Id列表获取用户信息
     */
    @PostMapping("/user/getListByUserIds")
    MSResponse<List<SysUser>> getListByUserIds(@RequestBody List<Long> userIds);

    /**
     * 根据用户Id列表获取用户信息
     */
    @PostMapping("/user/getMapByUserIds")
    MSResponse<Map<Long, SysUser>> getMapByUserIds(@RequestBody List<Long> userIds);

    /**
     * 根据用户Id列表获取用户名称
     */
    @PostMapping("/user/getNamesByUserIds")
    MSResponse<Map<Long, String>> getNamesByUserIds(@RequestBody List<Long> userIds);

    /**
     * 将所有的字典项重新加载到缓存中(不包括安维师傅)
     */
    @GetMapping("/user/reloadAllToRedis")
    MSResponse<Boolean> reloadAllToRedis();

    /**
     * 刷新缓存中的用户信息
     */
    @PostMapping("/user/reloadUserToRedis")
    MSResponse<Boolean> reloadUserToRedis(@RequestBody SysUser user);

    /**
     * 将用户信息加载到缓存
     */
    @PostMapping("/user/addUserToRedis")
    MSResponse<Boolean> addUserToRedis(@RequestBody SysUser user);
}
