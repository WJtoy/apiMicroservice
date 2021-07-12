package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BSurchargeItemMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BSurchargeItemMappingFeignFallbackFactory.class)
public interface B2BSurchargeItemMappingFeign {


    /**
     * 使用id查询
     * @param id
     * @return
     */
    @GetMapping("/b2bSurchargeItemMapping/get/{id}")
    MSResponse<B2BSurchargeItemMapping> get(@PathVariable("id") Long id);

    /**
     * 分页查询
     * @param surchargeItemMapping
     * @return
     */
    @PostMapping("/b2bSurchargeItemMapping/getList")
    MSResponse<MSPage<B2BSurchargeItemMapping>> getList(@RequestBody B2BSurchargeItemMapping surchargeItemMapping);

    /**
     * 保存B2B附加费项目信息
     * @param surchargeItemMapping
     * @return
     */
    @PostMapping("/b2bSurchargeItemMapping/insert")
    MSResponse<B2BSurchargeItemMapping> insert(@RequestBody B2BSurchargeItemMapping surchargeItemMapping);

    /**
     * 修改B2B附加费项目信息
     * @param surchargeItemMapping
     * @return
     */
    @PutMapping("/b2bSurchargeItemMapping/update")
    MSResponse<Integer> update(@RequestBody B2BSurchargeItemMapping surchargeItemMapping);

    /**
     * 删除B2B附加费分类信息
     * @param surchargeItemMapping
     * @return
     */
    @DeleteMapping("/b2bSurchargeItemMapping/delete")
    MSResponse<Integer> delete(@RequestBody B2BSurchargeItemMapping surchargeItemMapping);

    /**
     * 根据数据源获取B2B附加费项目信息
     * @param dataSource
     * @return
     */
    @GetMapping("/b2bSurchargeItemMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BSurchargeItemMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

}
