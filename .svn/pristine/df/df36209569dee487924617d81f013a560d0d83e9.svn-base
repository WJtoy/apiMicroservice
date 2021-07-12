package com.wolfking.jeesite.ms.praise.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.PraiseListModel;
import com.kkl.kklplus.entity.praise.PraisePageSearchModel;
import com.wolfking.jeesite.ms.praise.feign.fallback.ServicePointPraiseFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 网点好评费
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = ServicePointPraiseFactory.class)
public interface ServicePointPraiseFeign {

    /**
     * 网点查询待处理好评信息列表
     */
    @PostMapping("/praise/servicePoint/pendingReviewList")
    MSResponse<MSPage<PraiseListModel>> pendingReviewList(@RequestBody PraisePageSearchModel praisePageSearchModel);

    /**
     * 网点查询已审核好评信息列表
     */
    @PostMapping("/praise/servicePoint/approvedList")
    MSResponse<MSPage<PraiseListModel>> approvedList(@RequestBody PraisePageSearchModel praisePageSearchModel);

    /**
     * 网点查询已驳回好评信息列表
     */
    @PostMapping("/praise/servicePoint/rejectList")
    MSResponse<MSPage<PraiseListModel>> rejectList(@RequestBody PraisePageSearchModel praisePageSearchModel);


    /**
     * 网点查询所有好评信息列表
     */
    @PostMapping("/praise/servicePoint/search")
    MSResponse<MSPage<PraiseListModel>> findPraiseList(@RequestBody PraisePageSearchModel praisePageSearchModel);

}
