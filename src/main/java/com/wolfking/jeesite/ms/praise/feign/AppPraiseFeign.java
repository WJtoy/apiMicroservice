package com.wolfking.jeesite.ms.praise.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.PraiseAppListModel;
import com.kkl.kklplus.entity.praise.PraisePageSearchModel;
import com.wolfking.jeesite.ms.praise.feign.fallback.AppPraiseFactory;
import com.wolfking.jeesite.ms.praise.feign.fallback.ServicePointPraiseFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * App好评费
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = AppPraiseFactory.class)
public interface AppPraiseFeign {


    /**
     * 获取好评单列表
     */
    @PostMapping("/praise/app/rejectList")
    MSResponse<MSPage<PraiseAppListModel>> rejectAppList(@RequestBody PraisePageSearchModel praisePageSearchModel);

    /**
     * 获取好评单列表
     */
    @PostMapping("/praise/app/search")
    MSResponse<MSPage<PraiseAppListModel>> findPraiseList(@RequestBody PraisePageSearchModel praisePageSearchModel);

}
