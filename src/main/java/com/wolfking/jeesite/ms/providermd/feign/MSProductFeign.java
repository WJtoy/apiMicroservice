package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProduct;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductFeignFallbackFactory.class)
public interface MSProductFeign {

    /**
     * 通过id获取单个产品
     * @param id
     * @return
     */
    @GetMapping("/product/getById/{id}")
    MSResponse<MDProduct> getById(@PathVariable("id") Long id);

    /**
     * 根据name获取单个产品
     * @param name
     * @return
     */
    @GetMapping("/product/getIdByName/{name}")
    MSResponse<Long> getIdByName(@PathVariable("name") String name);

    /**
     * 通过产品分类id获取产品分类
     * @param productCategoryId
     * @return
     */
    @GetMapping("/product/getIdByProductCategoryId/{productCategoryId}")
    MSResponse<Long> getIdByProductCategoryId(@PathVariable("productCategoryId") Long productCategoryId);

    /**
     *
     * @param productId
     * @return
     */
    @GetMapping("product/getSetProductByProductId/{productId}")
    MSResponse<MDProduct> getSetProductByProductId(@PathVariable("productId") Long productId);

    /**
     * 获取所有产品数据
     * @return
     */
    @GetMapping("/product/findAllList")
    MSResponse<List<MDProduct>> findAllList();

    /**
     * 根据产品类别id获取产品列表
     * @param productCategoryId
     * @return
     */
    @GetMapping("/product/findListByProductCategoryId/{productCategoryId}")
    MSResponse<List<MDProduct>> findListByProductCategoryId(@PathVariable("productCategoryId") Long productCategoryId);

    /**
     * 根据产品类别id获取单品产品列表
     * @param productCategoryId
     * @return
     */
    @GetMapping("/product/findSingleListByProductCategoryId")
    MSResponse<List<MDProduct>> findSingleListByProductCategoryId(@RequestParam("productCategoryId") Long productCategoryId);

    /**
     * 分页获取产品信息
     * @param mdProduct
     * @return
     */
    @PostMapping("/product/findList")
    MSResponse<MSPage<MDProduct>> findList(@RequestBody MDProduct mdProduct);

    /**
     * 分页获取产品信息
     * @param mdProduct
     * @return
     */
    @PostMapping("/product/findListForPrice")
    MSResponse<MSPage<MDProduct>> findListForPrice(@RequestBody MDProduct mdProduct);

    /**
     * 按条件返回产品信息列表
     * @param mdProduct
     * @return
     * id,name,set_flag,sort,product_category_id
     */
    @PostMapping("/product/findListByConditions")
    MSResponse<List<MDProduct>> findListByConditions(@RequestBody MDProduct mdProduct);

    /**
     * 添加产品
     * @param mdProduct
     * @return
     */
    @PostMapping("/product/insert")
    MSResponse<Integer> insert(@RequestBody MDProduct mdProduct);

    /**
     * 删除产品
     * @param mdProduct
     * @return
     */
    @DeleteMapping("/product/delete")
    MSResponse<Integer> delete(@RequestBody MDProduct mdProduct);

    /**
     * 更新产品信息
     * @param mdProduct
     * @return
     */
    @PutMapping("/product/updateProduct")
    MSResponse<Integer> updateProduct(@RequestBody MDProduct mdProduct);

    /**
     * 更新产品排序
     * @param mdProductList
     * @return
     */
    @PutMapping("/product/updateSort")
    MSResponse<Integer> updateSort(@RequestBody List<MDProduct> mdProductList);

    @PutMapping("/product/approveProduct")
    MSResponse<Integer> approveProduct(@RequestBody MDProduct mdProduct);

    /**
     * 根据配件获取产品配件,用于删除配件是判断配件是否绑定产品
     * @param materialId
     * @return
     */
    @GetMapping("/product/getProductByMaterialId/{materialId}")
    MSResponse<HashMap<String,Object>> getProductByMaterialId(@PathVariable("materialId") Long materialId);

    /**
     * 获取所有的产品配件,用于web端redis缓存产品配件
     * @return
     */
    @GetMapping("product/findAllProductMaterial")
    MSResponse<List<MDProductMaterial>> findAllProductMaterial();


    /**
     * 根据id从缓存获取产品
     * @return
     */
    @GetMapping("product/getProductByIdFromCache")
    MSResponse<MDProduct> getProductByIdFromCache(@RequestParam("id") Long id);


    /**
     * 根据Id集合从缓存获取产品集合
     * @param ids
     * @return
     */
    @PostMapping("product/findProductByIdListFromCache")
    MSResponse<List<MDProduct>> findProductByIdListFromCache(@RequestParam("ids") List<Long> ids);

}
