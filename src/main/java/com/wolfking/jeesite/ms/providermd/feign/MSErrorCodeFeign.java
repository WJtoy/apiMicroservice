package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.dto.MDErrorCodeDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSErrorCodeFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSErrorCodeFeignFallbackFactory.class)
public interface MSErrorCodeFeign {


    /**
     * 删除故障代码
     * @param mdErrorCode
     * @return
     */
    @DeleteMapping("errorCode/delete")
    MSResponse<Integer> delete(@RequestBody MDErrorCode mdErrorCode);

    /**
     * 保存故障代码
     * @param mdErrorCode
     * @return
     */
    @PostMapping("errorCode/save")
    MSResponse<Integer> save(@RequestBody MDErrorCode mdErrorCode);

    /**
     * 根据productId， ErrorTypeId及错误代码名字获取故障代码id
     * @param mdErrorCode
     * @return
     */
    @GetMapping("errorCode/getByProductAndErrorType")
    MSResponse<Long> getByProductAndErrorType(@RequestBody MDErrorCode mdErrorCode);

    /**
     * 分页获取故障代码列表
     * @param mdErrorCode
     * @return
     */
    @PostMapping("errorCode/findListReturnErrorCodeDto")
    MSResponse<MSPage<MDErrorCodeDto>> findListReturnErrorCodeDto(@RequestBody MDErrorCode mdErrorCode);

    /**
     * 根据产品id和故障分类id获取故障代码列表
     * @param productId
     * @param errorTypeId
     * @return
     */
    @GetMapping("errorCode/findListByProductAndErrorType")
    MSResponse<List<MDErrorCode>> findListByProductAndErrorType(@RequestParam("errorTypeId") Long errorTypeId,@RequestParam("productId") Long productId);

    /**
     * 根据产品id和故障分类id获取故障代码id
     * @param errorTypeId
     * @param productId
     * @return
     */
    @GetMapping("/errorCode/getIdByProductAndErrorType")
    MSResponse<Long> getIdByProductAndErrorType(@RequestParam("errorTypeId") Long errorTypeId,@RequestParam("productId") Long productId);

    /**
     * 通过产品id或者故障现象id获取故障现象列表
     * @param id
     * @param productId
     * @return
     */
    @GetMapping("/errorCode/findListByProductId")
    MSResponse<List<MDErrorCodeDto>> findListByProductId(@RequestParam("id") Long id, @RequestParam("productId") Long productId);
}
