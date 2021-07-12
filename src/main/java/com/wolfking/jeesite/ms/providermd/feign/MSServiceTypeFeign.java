package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServiceType;
import com.wolfking.jeesite.ms.providermd.fallback.MSServiceTypeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name="provider-md", fallbackFactory = MSServiceTypeFeignFallbackFactory.class)
public interface MSServiceTypeFeign {
    @GetMapping("/serviceType/getById/{id}")
    MSResponse<MDServiceType> getById(@PathVariable("id") Long id);

    /**
     * 根据ID获取是否扣点和平台信息费开关标识-->财务
     * @param id
     * @return
     */
    @GetMapping("/serviceType/getTaxAndInfoFlagByIdForFI")
    MSResponse<MDServiceType> getTaxAndInfoFlagByIdForFI(@RequestParam("id") Long id);

    /**
     * 根据ID列表获取是否扣点和平台信息费开关标识列表-->财务
     * @param ids
     * @return
     */
    @PostMapping("/serviceType/findTaxAndInfoFlagListByIdsForFI")
    MSResponse<List<MDServiceType>> findTaxAndInfoFlagListByIdsForFI(@RequestBody List<Long> ids);

    @GetMapping("/serviceType/findAllList")
    MSResponse<List<MDServiceType>> findAllList();

    @PostMapping("/serviceType/findList")
    MSResponse<MSPage<MDServiceType>> findList(@RequestBody MDServiceType mdServiceType);

    @PostMapping("/serviceType/insert")
    MSResponse<Integer> insert(@RequestBody MDServiceType mdServiceType);

    @PutMapping("/serviceType/update")
    MSResponse<Integer> update(@RequestBody MDServiceType mdServiceType);

    @DeleteMapping("/serviceType/delete")
    MSResponse<Integer> delete(@RequestBody MDServiceType mdServiceType);


    /**
     * 根据id从缓存中读取服务类型
     * @param id
     * @return
     */
    @GetMapping("/serviceType/getFromCache/{id}")
    MSResponse<MDServiceType> getFromCache(@PathVariable("id") Long id);


    /**
     * 获取所有的服务类型
     * @return map<Long,String> key为id,value为服务类型名称</>
     */
    @GetMapping("/serviceType/findIdsAndNames")
    MSResponse<Map<Long,String>> findAllIdsAndNames();


    /**
     * 获取所有的服务类型
     * @return map<Long,String> key为id,value为服务类型编码(code)</>
     */
    @GetMapping("/serviceType/findIdsAndCodes")
    MSResponse<Map<Long,String>> findIdsAndCodes();

    /**
     * 根据对象属性名,返回相对应的数据
     * @param fields 需要返回数据的对象的属性名(如果需要返回id跟名称，即fieldList.add("id")和fieldList.add("name"))
     * @return list
     */
    @PostMapping("/serviceType/findAllListWithCondition")
    MSResponse<List<MDServiceType>> findAllListWithCondition(@RequestParam("fields") List<String> fields);

    /**
     * 获取工单类型为维修的服务类型列表 //2019-11-26
     * @return
     */
    @GetMapping("/serviceType/findListByMaintenance")
    MSResponse<List<MDServiceType>> findListByMaintenance();
}
