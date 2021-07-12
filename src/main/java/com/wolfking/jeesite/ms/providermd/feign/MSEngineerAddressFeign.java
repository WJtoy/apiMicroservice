package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerAddress;
import com.wolfking.jeesite.ms.providermd.fallback.MSEngineerAddressFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "provider-md", fallbackFactory = MSEngineerAddressFeignFallbackFactory.class)
public interface MSEngineerAddressFeign {
    /**
     * 根据安维id获取安维收件信息
     *
     * @param id
     * @return
     */
    @GetMapping("/engineerAddress/getByEngineerId/{id}")
    MSResponse<MDEngineerAddress> getByEngineerId(@PathVariable("id") Long id);

    /**
     * 根据收件信息id获取收件信息
     *
     * @param id
     * @return
     */
    @GetMapping("/engineerAddress/getById/{id}")
    MSResponse<MDEngineerAddress> getById(@PathVariable("id") Long id);

    /**
     * 新增安维地址
     *
     * @param engineerAddress
     * @return
     */
    @PostMapping("/engineerAddress/insert")
    MSResponse<Integer> insert(@RequestBody MDEngineerAddress engineerAddress);

    /**
     * 修改安维地址
     *
     * @param engineerAddress
     * @return
     */
    @PutMapping("/engineerAddress/update")
    MSResponse<Integer> update(@RequestBody MDEngineerAddress engineerAddress);

    /**
     * 根据网点id和师傅id从缓存中获取师傅收件地址
     *
     * @param servicePointId
     * @return
     */
    @GetMapping("/engineerAddress/getByEngineerIdAndPointIdFromCache")
    MSResponse<MDEngineerAddress> getFromCache(@RequestParam("servicePointId") Long servicePointId, @RequestParam("engineerId") Long engineerId);

}
