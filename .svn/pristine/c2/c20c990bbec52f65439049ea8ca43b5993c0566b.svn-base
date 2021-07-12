package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDCustomer;
import com.kkl.kklplus.entity.md.MDCustomerAddress;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerNewFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSCustomerNewFeignFallbackFactory.class)
public interface MSCustomerNewFeign {


    @PostMapping("/customer/insertCustomerUnion")
    MSResponse<NameValuePair<Long,Long>> insertCustomerUnion(@RequestBody MDCustomer mdCustomer);


    @PutMapping("/customer/updateCustomerUnion")
    MSResponse<Integer> updateCustomerUnion(@RequestBody MDCustomer mdCustomer);

    /**
     * 根据ID,tyoe获取客户地址信息
     *
     * @param customerId,
     * @return customerId
     * addressType
     */
    @GetMapping("/customerAddress/getByCustomerIdAndType")
    MSResponse<MDCustomerAddress> getByCustomerIdAndType(@RequestParam("customerId") Long customerId, @RequestParam("addressType") Integer addressType);

    /**
     * 根据ID,获取客户所有地址信息
     *
     * @param customerId,
     * @return customerId
     * addressType
     */
    @GetMapping("/customerAddress/findListByCustomerId")
    MSResponse<List<MDCustomerAddress>> findListByCustomerId(@RequestParam("customerId") Long customerId);

    /**
     * 更新客户地址信息
     *
     * @param mdCustomerAddress
     */
    @PutMapping("/customerAddress/update")
    MSResponse<Integer> update(@RequestBody MDCustomerAddress mdCustomerAddress);

    /**
     * 插入客户地址信息
     *
     * @param mdCustomerAddress
     */
    @PostMapping("/customerAddress/insert")
    MSResponse<Integer> insert(@RequestBody MDCustomerAddress mdCustomerAddress);

    /**
     * 检查客户名称是否存在
     * @param name
     * @return
     */
    @GetMapping("/customer/existByName")
    MSResponse<Long> existByName(@RequestParam("name") String name);
}
