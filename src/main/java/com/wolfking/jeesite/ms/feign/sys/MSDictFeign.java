package com.wolfking.jeesite.ms.feign.sys;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.SysDict;
import com.wolfking.jeesite.ms.fallback.sys.MSDictFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "provider-sys", fallbackFactory = MSDictFeignFallbackFactory.class)
public interface MSDictFeign {

    //region 查询字典项

    @GetMapping("/dict/get/{id}")
    MSResponse<SysDict> get(@PathVariable("id") Long id);

    @GetMapping("/dict/getAllList")
    MSResponse<List<SysDict>> getAllList();

    @PostMapping("/dict/getList")
    MSResponse<MSPage<SysDict>> getList(@RequestBody SysDict dict);

    @GetMapping("/dict/getListByType/{type}")
    MSResponse<List<SysDict>> getListByType(@PathVariable("type") String type);

    //endregion



    //region 查询字典项类型

    @GetMapping("/dict/getTypeList")
    MSResponse<List<String>> getTypeList();

    //endregion



    //region 新建/更新/删除字典项

    @PostMapping("/dict/insert")
    MSResponse<SysDict> insert(@RequestBody SysDict dict);

    @PostMapping("/dict/update")
    MSResponse<Integer> update(@RequestBody SysDict dict);

    @PostMapping("/dict/delete")
    MSResponse<Integer> delete(@RequestBody SysDict dict);

    //endregion

    //region 操作缓存

    @GetMapping("/dict/reloadAllToRedis")
    MSResponse<Boolean> reloadAllToRedis();

    @GetMapping("/dict/reloadToRedis/{type}")
    MSResponse<Boolean> reloadToRedis(@PathVariable("type") String type);

    //endregion
}
