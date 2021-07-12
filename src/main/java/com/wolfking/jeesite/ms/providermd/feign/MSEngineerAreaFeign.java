package com.wolfking.jeesite.ms.providermd.feign;


import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerArea;
import com.wolfking.jeesite.ms.providermd.fallback.MSEngineerAreaFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-md", fallbackFactory = MSEngineerAreaFeignFallbackFactory.class)
public interface MSEngineerAreaFeign {
    /**
     * 通过安维id获取安维对应的区域id
     * @param engineerId
     * @return
     */
    @GetMapping("/engineerArea/findEngineerAreaIds")
    MSResponse<List<Long>> findEngineerAreaIds(@RequestParam("engineerId") Long engineerId);

    /**
     * 通过安维id获取安维区域id列表
     * @param engineerIds
     * @return
     */
    @PostMapping("/engineerArea/findEngineerAreasWithIds")
    MSResponse<List<MDEngineerArea>> findEngineerAreasWithIds(@RequestBody List<Long> engineerIds);

    /**
     * 给安维人员分配区域id
     * @param engineerId
     * @param areas
     * @return
     */
    @PostMapping("/engineerArea/assignEngineerAreas")
    MSResponse<Integer> assignEngineerAreas( @RequestBody List<Long> areas, @RequestParam("engineerId") Long engineerId);

    /**
     * 根据安维人员id删除其对应的区域信息
     * @param engineerId
     * @return
     */
    @DeleteMapping("/engineerArea/removeEngineerAreas")
    MSResponse<Integer> removeEnigineerAreas(@RequestParam("engineerId") Long engineerId);

    /**
     * 根据安维人员，区域id列表删除不在当前区域的安维区域
     * @param servicePointId
     * @param areas
     * @return
     */
    @DeleteMapping("/engineerArea/deleteEngineerAreas")
    MSResponse<Integer> deleteEnigineerAreas(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<Long> areas);

}
