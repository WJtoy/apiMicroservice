package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFee;
import com.wolfking.jeesite.ms.providermd.fallback.MSCustomerPraiseFeeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="provider-md", fallbackFactory = MSCustomerPraiseFeeFeignFallbackFactory.class)
public interface MSCustomerPraiseFeeFeign {
    /**
     * 根据ID查询客户好评费
     * @param id    id
     * @return
     */
    @GetMapping("/customer/praiseFee/getById")
    MSResponse<MDCustomerPraiseFee> getById(@RequestParam("id") Long id);

    /**
     * 根据客户ID查询客户好评费
     * @param customerId    客户id
     * @return
     */
    @GetMapping("/customer/praiseFee/getByCustomerIdFromCacheForCP")
    MSResponse<MDCustomerPraiseFee> getByCustomerIdFromCacheForCP(@RequestParam("customerId") Long customerId);

    /**
     * 根据客户ID查询客户好评费-New  2020-4-30
     * @param customerId    客户id
     * @return
     * customerId,praise_fee_flag,praise_fee,max_praise_fee,discount,praisestandardItem,checkstandardItem,praise_requirement
     */
    @GetMapping("/customer/praiseFee/getByCustomerIdFromCacheNewForCP")
    MSResponse<MDCustomerPraiseFee> getByCustomerIdFromCacheNewForCP(@RequestParam("customerId") Long customerId);

    /**
    * 根据客户ID判断客户是否已添加好评费
    */
    @GetMapping("/customer/praiseFee/isExistsByCustomerId")
    public MSResponse<Boolean> isExistsByCustomerId(@RequestParam("customerId") Long customerId);

    /**
      分页获取客户好评费
    */
    @PostMapping("/customer/praiseFee/findList")
    MSResponse<MSPage<MDCustomerPraiseFee>> findList(@RequestBody MDCustomerPraiseFee mdCustomerPraiseFee);

    /*
     添加客户好评费
    */
    @PostMapping("/customer/praiseFee/insert")
    MSResponse<Integer> insert(@RequestBody MDCustomerPraiseFee mdCustomerPraiseFee);

    /*
    修改客户好评费
    */
    @PutMapping("/customer/praiseFee/update")
    MSResponse<Integer> update(@RequestBody MDCustomerPraiseFee mdCustomerPraiseFee);

    /*
    * 删除客户好评费
     */
    @DeleteMapping("/customer/praiseFee/delete")
    MSResponse<Integer> delete(@RequestBody MDCustomerPraiseFee mdCustomerPraiseFee);
}
