package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductCategory;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductCategoryFeignFallbackFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name="provider-md", fallbackFactory = MSProductCategoryFeignFallbackFactory.class)
public interface MSProductCategoryFeign {
    /**
     * 获取分页的产品类别列表
     * @param mdProductCategory
     * @return
     */
    @PostMapping("/productCategory/findList")
    MSResponse<MSPage<MDProductCategory>> findList(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 通过id列表获取类别列表
     * @param ids
     * @return
     */
    @PostMapping("/productCategory/findListByIds")
    MSResponse<List<MDProductCategory>> findListByIds(@RequestBody List<Long> ids);

    /**
     * 获取所有的产品分类列表
     * @return
     */
    @GetMapping("/productCategory/findAllList")
    MSResponse<List<MDProductCategory>> findAllList();

    /**
     * 根据id获取产品类别
     * @param id
     * @return
     */
    @GetMapping("/productCategory/getById/{id}")
    MSResponse<MDProductCategory> getById(@PathVariable("id") Long id);

    /**
     * 从产品品类中获取品类名称
     * @param id
     * @return
     */
    @GetMapping("/productCategory/getNameById/{id}")
    MSResponse<String> getNameById(@PathVariable("id") Long id);

    /**
     * 根据id从缓存中获取产品类别
     * @param id
     * @return
     */
    @GetMapping("/productCategory/getFromCache/{id}")
    MSResponse<MDProductCategory> getFromCache(@PathVariable("id") Long id);

    /**
     * 根据id从缓存中获取产品类别名称
     * @param id
     * @return
     */
    @GetMapping("/productCategory/getNameFromCache/{id}")
    MSResponse<String> getNameFromCache(@PathVariable("id") Long id);

    /**
     * 根据code获取产品类别id
     * @param code
     * @return
     */
    @GetMapping("/productCategory/getIdByCode/{code}")
    MSResponse<Long> getIdByCode(@PathVariable("code") String code);

    /**
     * 根据name获取产品类别id
     * @param code
     * @return
     */
    @GetMapping("/productCategory/getIdByName/{name}")
    MSResponse<Long> getIdByName(@PathVariable("name") String code);

    /**
     * 添加产品类别
     * @param mdProductCategory
     * @return
     */
    @PostMapping("/productCategory/insert")
    MSResponse<Integer> insert(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 更新产品类别
     * @param mdProductCategory
     * @return
     */
    @PutMapping("/productCategory/update")
    MSResponse<Integer> update(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 删除一个产品类别
     * @param mdProductCategory
     * @return
     */
    @DeleteMapping("/productCategory/delete")
    MSResponse<Integer> delete(@RequestBody MDProductCategory mdProductCategory);
}
