package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDProductCategory;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductCategoryNewFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductCategoryNewFeignFallbackFactory.class)
public interface MSProductCategoryNewFeign {
    /**
     * 获取全部产品类别-->基础资料
     * @return
     * id,name
     */
    @GetMapping("/productCategoryNew/findAllListForMD")
    MSResponse<List<NameValuePair<Long, String>>> findAllListForMD();

    /**
     * 获取全部产品类别-->报表
     * @return
     * id,name
     */
    @GetMapping("/productCategoryNew/findAllListForRPT")
    MSResponse<List<NameValuePair<Long, String>>> findAllListForRPT();

    /**
     * 获取全部产品类别-->工单
     * @return
     * id,name
     */
    @GetMapping("/productCategoryNew/findAllListForSD")
    MSResponse<List<NameValuePair<Long, String>>> findAllListForSD();

    /**
     * 批量获取获取产品类别-->基础资料
     * @param ids
     * @return
     * id,name
     */
    @PostMapping("/productCategoryNew/findListByIdsForMD")
    MSResponse<List<NameValuePair<Long,String>>> findListByIdsForMD(@RequestBody List<Long> ids);

    /**
     * 分页&条件(code,name)获取产品类别-->基础资料
     * @param mdProductCategory
     * @return
     */
    @PostMapping("/productCategoryNew/findListForMD")
    MSResponse<MSPage<MDProductCategory>> findListForMD(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 获取全部产品品类id
     * @return
     */
    @GetMapping("/productCategoryNew/findIdListForMD")
    MSResponse<List<Long>> findIdListForMD();

    /**
     * 根据ID获取产品类别-->基础资料
     * @param id
     * @return
     * id，code,name,del_flag,remarks
     */
    @GetMapping("/productCategoryNew/getByIdForMD/{id}")
    MSResponse<MDProductCategory> getByIdForMD(@PathVariable("id") Long id);

    /**
     * 根据ID从缓存读取-->基础资料
     * @return
     * id,name
     */
    @GetMapping("/productCategoryNew/getFromCacheForMD")
    MSResponse<String> getFromCacheForMD(@RequestParam("id") Long id);

    /**
     * 根据ID从缓存读取->工单
     * @param id
     * @return
     * name
     */
    @GetMapping("/productCategoryNew/getFromCacheForSD")
    MSResponse<String> getFromCacheForSD(@RequestParam("id") Long id);

    /**
     * 根据产品Code获取ID-->基础资料
     * @param code
     * @return
     *  id
     */
    @GetMapping("/productCategoryNew/getIdByCodeForMD")
    MSResponse<Long> getIdByCodeForMD(@RequestParam("code") String code);

    /**
     * 根据产品Code获取ID-->基础资料
     * @param name
     * @return
     *  id
     */
    @GetMapping("/productCategoryNew/getIdByNameForMD")
    MSResponse<Long> getIdByNameForMD(@RequestParam("name") String name);

    /**
     * 添加产品类别
     * @param mdProductCategory
     * @return
     */
    @PostMapping("/productCategoryNew/insert")
    MSResponse<Integer> insert(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 更新产品类别
     * @param mdProductCategory
     * @return
     */
    @PutMapping("/productCategoryNew/update")
    MSResponse<Integer> update(@RequestBody MDProductCategory mdProductCategory);

    /**
     * 删除一个产品类别
     * @param mdProductCategory
     * @return
     */
    @DeleteMapping("/productCategoryNew/delete")
    MSResponse<Integer> delete(@RequestBody MDProductCategory mdProductCategory);
}
