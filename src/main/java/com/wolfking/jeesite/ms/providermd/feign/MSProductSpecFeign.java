package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductSpec;
import com.kkl.kklplus.entity.md.dto.MDProductSpecDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductSpecFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 产品规格
 * */
@FeignClient(name="provider-md", fallbackFactory = MSProductSpecFeignFallbackFactory.class)
public interface MSProductSpecFeign {


    /**
     * 根据id
     * @param id
     * */
    @GetMapping("/productSpec/getById")
    MSResponse<MDProductSpec> getById(@RequestParam("id") Long id);

    /**
     * 分页查询
     * @param mdProductSpecDto
     * */
    @PostMapping("/productSpec/findList")
    MSResponse<MSPage<MDProductSpecDto>> findList(@RequestBody MDProductSpecDto mdProductSpecDto);


    /**
     * 根据名称获取分类Id
     * @param name
     * */
     @GetMapping("/productSpec/existForName")
     MSResponse<Long> getIdByName(@RequestParam("name") String name);


    @GetMapping("/productSpec/getDtoWithSpecId")
    MSResponse<MDProductSpecDto> getDtoWithSpecId(@RequestParam("id") Long id);



    /**
     * 添加数据
     * @param mdProductSpecDto
     * */
    @PostMapping("productSpec/insert")
    MSResponse<Integer> insert(@RequestBody MDProductSpecDto mdProductSpecDto);

    /**
     * 更新数据
     * @param mdProductSpecDto
     * */
    @PutMapping("productSpec/update")
    MSResponse<Integer> update(@RequestBody MDProductSpecDto mdProductSpecDto);

    /**
     * 删除
     * @param mdProductSpecDto
     * */
    @DeleteMapping("productSpec/delete")
    MSResponse<Integer> delete(@RequestBody MDProductSpecDto mdProductSpecDto);




}
