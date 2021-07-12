package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductInsurance;
import com.wolfking.jeesite.ms.providermd.fallback.MSProductInsuranceFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSProductInsuranceFeignFallbackFactory.class)
public interface MSProductInsuranceFeign {

    @PostMapping("/productInsurance/findList")
    MSResponse<MSPage<MDProductInsurance>> findList(@RequestBody MDProductInsurance mdProductInsurance);

    @GetMapping("/productInsurance/findAllList")
    MSResponse<List<MDProductInsurance>> findAllList();

    @GetMapping("/productInsurance/get/{id}")
    MSResponse<MDProductInsurance> getById(@PathVariable("id") Long id);

    @PostMapping("/productInsurance/insert")
    MSResponse<Integer> insert(@RequestBody MDProductInsurance mdProductInsurance);

    @PutMapping("/productInsurance/update")
    MSResponse<Integer> update(@RequestBody MDProductInsurance mdProductInsurance);

    @DeleteMapping("/productInsurance/delete")
    MSResponse<Integer> delete(@RequestBody MDProductInsurance mdProductInsurance);

}
