package com.wolfking.jeesite.ms.praise.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraisePageSearchModel;
import com.wolfking.jeesite.ms.praise.feign.fallback.SalesPraiseFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 业务好评费
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = SalesPraiseFactory.class)
public interface SalesPraiseFeign {

    /**
     * 业务查询待处理好评信息列表
     */
    @PostMapping("/praise/sales/pendingReviewList")
    MSResponse<MSPage<PraiseListModel>> pendingReviewList(@RequestBody PraisePageSearchModel praisePageSearchModel);


    /**
     * 业务查询已审核好评信息列表
     */
    @PostMapping("/praise/sales/approvedList")
    MSResponse<MSPage<PraiseListModel>> approvedList(@RequestBody PraisePageSearchModel praisePageSearchModel);

    /**
     * 业务查询所有好评信息列表
     */
    @PostMapping("/praise/sales/search")
    MSResponse<MSPage<PraiseListModel>> findPraiseList(@RequestBody PraisePageSearchModel praisePageSearchModel);
}
