package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.wolfking.jeesite.ms.providermd.fallback.MSErrorTypeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSErrorTypeFeignFallbackFactory.class)
public interface MSErrorTypeFeign {

    /**
     * 保存故障分类
     * @param mdErrorType
     * @return
     */
    @PostMapping("/errorType/save")
    MSResponse<Integer> save(@RequestBody MDErrorType mdErrorType);

    /**
     * 分页获取故障分类列表
     * @param mdErrorType
     * @return
     */
    @PostMapping("/errorType/findList")
    MSResponse<MSPage<MDErrorType>> findList(@RequestBody MDErrorType mdErrorType);

    /**
     * 根据产品获取故障分类列表
     * @param productId
     * @return
     */
    @GetMapping("/errorType/findErrorTypesByProductId")
    MSResponse<List<MDErrorType>> findErrorTypesByProductId(@RequestParam("productId") Long productId);

    /**
     * 删除故障分类
     * @param mdErrorType
     * @return
     */
    @DeleteMapping("/errorType/delete")
    MSResponse<Integer> delete(@RequestBody MDErrorType mdErrorType);

    /**
     * 修改故障分类
     * @param mdErrorType
     * @return
     */
    @PutMapping("/errorType/update")
    MSResponse<Integer> update(@RequestBody MDErrorType mdErrorType);

    /**
     * 通过产品id及故障分类名称获取故障分类id
     * @param productId
     * @param name
     * @return
     */
    @GetMapping("/errorType/getByProductIdAndName")
    MSResponse<Long> getByProductIdAndName(@RequestParam("productId") Long productId, @RequestParam("name") String name);

    /**
     * 根据产品id，或id查询故障分类
     * @param productId
     * @param id
     * @return
     */
    @GetMapping("/errorType/findListByProductId")
    MSResponse<List<MDErrorType>> findListByProductId(@RequestParam("productId") Long productId, @RequestParam("id") Long id);
}
