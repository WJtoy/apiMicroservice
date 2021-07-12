package com.wolfking.jeesite.ms.providersys.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.sys.SysOffice;
import com.wolfking.jeesite.ms.providersys.fallback.MSSysOfficeFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "provider-sys", fallbackFactory = MSSysOfficeFallbackFactory.class)
public interface MSSysOfficeFeign {
    /**
     * 根据id获取id，name，code属性
     * @param id
     * @return id,name,code
     */
    @GetMapping("office/getSpecColumnById/{id}")
    MSResponse<SysOffice> getSpecColumnById(@PathVariable("id") Long id);

    /**
     * 根据部门id列表获取部门的(id,name,code)
     * @param ids
     * @return
     */
    @PostMapping("office/findSpecColumnListByIds")
    MSResponse<List<SysOffice>> findSpecColumnListByIds(@RequestBody List<Long> ids);
}
