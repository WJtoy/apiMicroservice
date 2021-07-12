package com.wolfking.jeesite.ms.b2bcenter.md.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.fallback.B2BSurchargeCategoryMappingFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * B2BCenter微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-center", fallbackFactory = B2BSurchargeCategoryMappingFeignFallbackFactory.class)
public interface B2BSurchargeCategoryMappingFeign {


    /**
     * 根据id获取
     * @param id
     * @return
     */
    @GetMapping("/b2bSurchargeCategoryMapping/get/{id}")
    MSResponse<B2BSurchargeCategoryMapping> get(@PathVariable("id") Long id);


    /**
     * 分页查询
     * @param surchargeCategoryMapping
     * @return
     */
    @PostMapping("/b2bSurchargeCategoryMapping/getList")
    MSResponse<MSPage<B2BSurchargeCategoryMapping>> getList(@RequestBody B2BSurchargeCategoryMapping surchargeCategoryMapping);

    /**
     * 保存B2B附加费分类信息
     * @param surchargeCategoryMapping
     * @return
     */
    @PostMapping("/b2bSurchargeCategoryMapping/insert")
    MSResponse<B2BSurchargeCategoryMapping> insert(@RequestBody B2BSurchargeCategoryMapping surchargeCategoryMapping);

    /**
     * 修改B2B附加费分类信息
     * @param surchargeCategoryMapping
     * @return
     */

    @PutMapping("/b2bSurchargeCategoryMapping/update")
    MSResponse<Integer> update(@RequestBody B2BSurchargeCategoryMapping surchargeCategoryMapping);

    /**
     * 删除B2B附加费分类信息
     * @param surchargeCategoryMapping
     * @return
     */
    @DeleteMapping("/b2bSurchargeCategoryMapping/delete")
    MSResponse<Integer> delete(@RequestBody B2BSurchargeCategoryMapping surchargeCategoryMapping);


    /**
     * 根据数据源获取B2B附加费分类信息
     * @param dataSource
     * @return
     */
    @GetMapping("/b2bSurchargeCategoryMapping/getListByDataSource/{dataSource}")
    MSResponse<List<B2BSurchargeCategoryMapping>> getListByDataSource(@PathVariable("dataSource") Integer dataSource);

}
