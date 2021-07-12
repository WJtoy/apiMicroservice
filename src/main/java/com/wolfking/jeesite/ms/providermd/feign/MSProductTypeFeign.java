package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductType;
import com.kkl.kklplus.entity.md.dto.MDProductTypeDto;
import com.kkl.kklplus.entity.md.dto.TreeDTO;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductTypeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductTypeFeignFallbackFactory.class)
public interface MSProductTypeFeign {


    /**
     * 根据id
     * @param id
     * */
    @GetMapping("/productType/getById")
    MSResponse<MDProductTypeDto> getById(@RequestParam("id") Long id);

    /**
     * 分页查询
     * @param productTypeDto
     * */
    @PostMapping("/productType/findList")
    MSResponse<MSPage<MDProductTypeDto>> findList(@RequestBody MDProductTypeDto productTypeDto);


    /**
     * 根据名称获取分类Id
     * @param name
     * */
     @GetMapping("/productType/getIdByName")
     MSResponse<Long> getIdByName(@RequestParam("name") String name);


    /**
     * 添加数据
     * @param mdProductTypeDto
     * */
    @PostMapping("productType/insert")
    MSResponse<Integer> insert(@RequestBody MDProductTypeDto mdProductTypeDto);

    /**
     * 更新数据
     * @param mdProductTypeDto
     * */
    @PutMapping("productType/update")
    MSResponse<Integer> update(@RequestBody MDProductTypeDto mdProductTypeDto);

    /**
     * 删除
     * @param mdProductTypeDto
     * */
    @DeleteMapping("productType/delete")
    MSResponse<Integer> delete(@RequestBody MDProductTypeDto mdProductTypeDto);


    /**
     * 根据品类id获取
     * @param productCategoryId
     * */
    @GetMapping("productType/findListByProductCategoryId")
    MSResponse<List<MDProductType>>  findListByCategoryId(@RequestParam("productCategoryId") Long productCategoryId);

    /**
     * 根据多品类id获取产品分类
     * @param productCategoryIds
     * */
    @GetMapping("productType/findTypeAndItemsByCategoryIds")
    MSResponse<List<TreeDTO>>  findTypeAndItemsByCategoryIds(@RequestParam("productCategoryIds") List<Long> productCategoryIds);

}
