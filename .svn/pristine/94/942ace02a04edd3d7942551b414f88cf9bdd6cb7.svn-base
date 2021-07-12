package com.wolfking.jeesite.ms.providermd.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorAction;
import com.kkl.kklplus.entity.md.dto.MDErrorActionDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSErrorActionFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "provider-md", fallbackFactory = MSErrorActionFeignFallbackFactory.class)
public interface MSErrorActionFeign {

    /**
     * 分页获取故障处理列表
     * @param mdErrorActionDto
     * @return
     */
    @PostMapping("/errorAction/findList")
    MSResponse<MSPage<MDErrorActionDto>> findList(@RequestBody MDErrorActionDto mdErrorActionDto);

    /**
     * 分页获取故障处理列表
     * @param mdErrorActionDto
     * @return
     */
    @PostMapping("/errorAction/findListWithProduct")
    MSResponse<MSPage<MDErrorActionDto>> findListWithProduct(@RequestBody MDErrorActionDto mdErrorActionDto);

    /**
     * 删除
     * @param mdErrorAction
     * @return
     */
    @DeleteMapping("/errorAction/delete")
    MSResponse<Integer> delete(@RequestBody MDErrorAction mdErrorAction);

    /**
     * 通过产品和故障代码判断是否存在
     * @param errorCodeId
     * @param productId
     * @return
     */
    @GetMapping("/errorAction/getIdByProductAndErrorCode")
    MSResponse<Long> getIdByProductAndErrorCode(@RequestParam("errorCodeId") Long errorCodeId, @RequestParam("productId") Long productId);

    /**
     * 根据errorActionId获取errorActionDto的相关数据
     * @param errorActionId
     * @return
     */
    @GetMapping("/errorAction/getAssociatedDataById")
    MSResponse<MDErrorActionDto> getAssociatedDataById(@RequestParam("errorActionId") Long errorActionId);

    /**
     * 更新故障分析
     * @param mdActionCode
     * @return
     */
    @PutMapping("/errorAction/updateActionCodeNameAndAnalysis")
    MSResponse<Integer> updateActionCodeNameAndAnalysis(@RequestBody  MDActionCode mdActionCode);

}
