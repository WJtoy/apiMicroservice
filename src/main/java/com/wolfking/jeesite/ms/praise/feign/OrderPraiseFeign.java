package com.wolfking.jeesite.ms.praise.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.praise.*;
import com.wolfking.jeesite.ms.praise.feign.fallback.OrderPraiseFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 好评费
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = OrderPraiseFactory.class)
public interface OrderPraiseFeign {

    /**
     * 申请好评费
     */
    @PostMapping("/praise/common/new")
    MSResponse<Praise> saveApplyPraise(@RequestBody Praise praise);


    /**
     * 根据订单Id获取好评费(返回内容:id,好评照片,好评费,产品,状态)
     */
    @GetMapping("/praise/common/getByOrderId/{orderId}/{quarter}")
    MSResponse<Praise> getByOrderId(@PathVariable("quarter")String quarter,
                                           @PathVariable("orderId") Long orderId);

    /**
     * 修改好评费
     */
    @PostMapping("/praise/common/resubmit")
    MSResponse<Integer> resubmit(@RequestBody Praise praise);


    /**
     * 客户查询已通过好评单列表
     * */
    @PostMapping("/praise/customer/approvedList")
    MSResponse<MSPage<PraiseListModel>> approvedList(@RequestBody PraisePageSearchModel praisePageSearchModel);


    /**
     * 根据id和分片
     * */
    @GetMapping("/praise/common/getById/{id}/{quarter}")
    MSResponse<Praise> getById(@PathVariable("quarter")String quarter,
                                      @PathVariable("id") Long id);



    /**
     * 根据好评单id和分片获取跟踪进度列表
     * */
    @GetMapping("/praise/log/get/{praiseId}/{quarter}")
    MSResponse<List<PraiseLog>> finPraiseLogList(@PathVariable("quarter")String quarter,
                                        @PathVariable("praiseId") Long praiseId);


    /**
     * 业务审核通过好评单
     * */
    @PostMapping("/praise/common/approve")
    MSResponse<Integer> approve(@RequestBody Praise praise);

    /**
     * 业务驳回好评单
     * */
    @PostMapping("/praise/common/reject")
    MSResponse<Integer> reject(@RequestBody PraiseAbnormalMessage praiseAbnormalMessage);

    /**
     * 业务取消好评单
     * */
    @PostMapping("/praise/common/cancelled")
    MSResponse<Integer> cancelled(@RequestBody Praise praise);


    /**
     * 根据订单id，网点id，分片获取好评单
     * @param quarter
     * @param orderId
     * @param servicepointId
     * */
    @GetMapping("praise/common/getByoIdAndspId/{orderId}/{servicepointId}/{quarter}")
    MSResponse<Praise> getByOrderIdAndServicepointId(@PathVariable("quarter")String quarter,
                                                     @PathVariable("orderId") Long orderId,
                                                     @PathVariable("servicepointId") Long servicepointId);

    /**
     * 业客服待审核好评单列表
     */
    @PostMapping("/praise/kefu/noFeesPendingReviewList")
    MSResponse<MSPage<PraiseListModel>> noFeesPendingReviewList(@RequestBody PraisePageSearchModel praisePageSearchModel);

    /**
     * 客服查看已通过审核无费用的好评单
     * */
    @PostMapping("/praise/kefu/noFeesApprovedList")
    MSResponse<MSPage<PraiseListModel>> noFeesApprovedKefuList(@RequestBody PraisePageSearchModel praisePageSearchModel);


    /**
     * 客服修改已通过审核无费用的好评单为无效
     * */
    @PostMapping("/praise/common/invalidation")
    MSResponse<Integer> invalidation(@RequestBody Praise praise);

    /**
     * 客服查看无效的好评单
     * */
    @PostMapping("/praise/kefu/invalidationList")
    MSResponse<MSPage<PraiseListModel>> invalidationKefuLis(@RequestBody PraisePageSearchModel praisePageSearchModel);


    /**
     * 根据时间获取好评单数量
     * */
    @GetMapping("/praise/common/getPraiseCount/{beginDt}")
    MSResponse<Integer> praiseCount(@PathVariable("beginDt") Long beginDt);

}
