package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BCustomerMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BCustomerMappingFeignFallbackFactory.class)
public interface B2BCustomerMappingFeign {

    @GetMapping("/b2BCustomerMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BCustomerMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

    @GetMapping("/b2BCustomerMapping/get/{id}")
    MSResponse<B2BCustomerMapping> getCustomerMappingById(@PathVariable("id") Long id);

    @PostMapping("/b2BCustomerMapping/getList")
    MSResponse<MSPage<B2BCustomerMapping>> getCustomerMappingList(@RequestBody B2BCustomerMapping customerMapping);

    @PostMapping("/b2BCustomerMapping/insert")
    MSResponse<B2BCustomerMapping> insertCustomerMapping(@RequestBody B2BCustomerMapping customerMapping);

    @PostMapping("/b2BCustomerMapping/update")
    MSResponse<Integer> updateCustomerMapping(@RequestBody B2BCustomerMapping customerMapping);

    @PostMapping("/b2BCustomerMapping/delete")
    MSResponse<Integer> deleteCustomerMapping(@RequestBody B2BCustomerMapping customerMapping);

    @GetMapping("/b2BCustomerMapping/getByShopId")
    MSResponse<Long> getByShopId(@RequestParam("shopId") String shopId, @RequestParam("dataSource") Integer dataSource);

    /**
     * 根据所有信息
     *
     * @param
     * @return
     */
    @GetMapping("/b2BCustomerMapping/findAllList")
    MSResponse<List<B2BCustomerMapping>> findAllList();


    /**
     * 从缓存中读取所有默认店铺数据
     *
     * @return Map<Integer   ,   String> key:数据源 value:店铺Id
     */
    @GetMapping("/b2BCustomerMapping/getDefaultShopMap")
    MSResponse<Map<Integer, String>> getDefaultShopMap();

    /**
     * 获取系统中所有的店铺名称
     */
    @GetMapping("/b2BCustomerMapping/getAllCustomerMapping")
    MSResponse<List<B2BCustomerMapping>> getAllCustomerMapping();

    /**
     * 获取指定客户店铺的名称
     */
    @GetMapping("/b2BCustomerMapping/getShopName")
    MSResponse<String> getShopName(@RequestParam("customerId") Long customerId, @RequestParam("shopId") String shopId);

    /**
     * 获取分页->基础资料
     * @param customerMapping
     * @return
     */
    @PostMapping("/b2BCustomerShop/findList")
    MSResponse<MSPage<B2BCustomerMapping>> findList(@RequestBody B2BCustomerMapping customerMapping);
}
