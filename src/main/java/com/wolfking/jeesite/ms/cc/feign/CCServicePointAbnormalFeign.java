package com.wolfking.jeesite.ms.cc.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.vm.AbnormalFormSearchModel;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.cc.feign.fallback.CCAbnormalFormFactory;
import com.wolfking.jeesite.ms.cc.feign.fallback.CCServicePointAbnormalFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 网点异常单
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = CCServicePointAbnormalFactory.class)
public interface CCServicePointAbnormalFeign {

    /**
     *  网点驳回异常
     *  @param abnormalFormSearchModel
     */
    @PostMapping("/abnormalForm/servicePoint/praiseRejectList")
    MSResponse<MSPage<AbnormalForm>> servicePointPraiseRejectAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);


}
