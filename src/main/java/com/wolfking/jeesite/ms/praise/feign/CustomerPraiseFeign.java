package com.wolfking.jeesite.ms.praise.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraisePageSearchModel;
import com.wolfking.jeesite.ms.praise.feign.fallback.CustomerPraiseFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 好评费
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = CustomerPraiseFactory.class)
public interface CustomerPraiseFeign {


    /**
     * 客户查询已通过好评单列表
     * */
    @PostMapping("/praise/customer/approvedList")
    MSResponse<MSPage<PraiseListModel>> approvedList(@RequestBody PraisePageSearchModel praisePageSearchModel);


}
