package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerCategory;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BCustomerCategoryFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BCustomerCategoryFeignFallbackFactory.class)
public interface B2BCustomerCategoryFeign {

    @PostMapping("/b2bCustomerCategory/getList")
    MSResponse<MSPage<B2BCustomerCategory>> getList(@RequestBody B2BCustomerCategory customerCategory);

    @PostMapping("/b2bCustomerCategory/insert")
    MSResponse<B2BCustomerCategory> insert(@RequestBody B2BCustomerCategory customerCategory);

    @PostMapping("/b2bCustomerCategory/update")
    MSResponse<Integer> update(@RequestBody B2BCustomerCategory customerCategory);

    @PostMapping("/b2bCustomerCategory/delete")
    MSResponse<Integer> delete(@RequestBody B2BCustomerCategory customerCategory);

    @GetMapping("/b2bCustomerCategory/getListByDataSource/{dataSource}")
    MSResponse<List<B2BCustomerCategory>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

}
