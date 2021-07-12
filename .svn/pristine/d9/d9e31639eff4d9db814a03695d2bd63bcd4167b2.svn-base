package com.wolfking.jeesite.ms.cc.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.kkl.kklplus.entity.cc.vm.AbnormalFormSearchModel;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.cc.feign.fallback.CCAbnormalFormFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 异常单
 * 调用微服务：provider-customer-compliant
 */
@FeignClient(name = "provider-customer-compliant", fallbackFactory = CCAbnormalFormFactory.class)
public interface CCAbnormalFormFeign {

    /**
     *  添加异常单
     *  @param abnormalForm
     */
    @PostMapping("/abnormalForm/save")
    MSResponse<Integer> save(@RequestBody AbnormalForm abnormalForm);

    /**
     *  关闭异常单
     *  @param abnormalForm
     */
    @PostMapping("/abnormalForm/closeAbnormalForm")
    MSResponse<Integer> closeAbnormalForm(@RequestBody AbnormalForm abnormalForm);

    /**
     * 待处理列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("/abnormalForm/waitProcessList")
    MSResponse<MSPage<AbnormalForm>> waitProcessList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);

    /**
     * 已处理列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/processedList")
    MSResponse<MSPage<AbnormalForm>> processedList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);

    /**
     * 客服订单详情页面关闭订单异常
     * @param abnormalForm
     * @return
     */
    @PostMapping("abnormalForm/closeByOrderId")
    MSResponse<Integer> closeByOrderId(@RequestBody AbnormalForm abnormalForm);


    /**
     * 批量添加异常单(财务对账批量标记异常)
     * @param abnormalFormList
     * @return
     */
    @PostMapping("abnormalForm/insertBatch")
    MSResponse<Integer> insertBatch(@RequestBody List<AbnormalForm> abnormalFormList);


    /**
     * 根据工单号关闭审单异常(客服异常处理列表关闭异常工单)
     * @param abnormalForm
     * @return
     */
    @PutMapping("abnormalForm/closeReviewAbnormal")
    MSResponse<Integer> closeReviewAbnormal(@RequestBody AbnormalForm abnormalForm);


    /**
     * 根据订单id获取数量
     * @param orderId
     * @param quarter
     * @return
     */
    @GetMapping("abnormalForm/getCountByOrderId")
    MSResponse<Integer> getCountByOrderId(@RequestParam("orderId") Long orderId,@RequestParam("quarter") String quarter,@RequestParam("formType") Integer formType);

    /**
     * app异常列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/appAbnormalList")
    MSResponse<MSPage<AbnormalForm>> appAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);


    /**
     * 审单异常列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/reviewAbnormalList")
    MSResponse<MSPage<AbnormalForm>> reviewAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);


    /**
     * app完工异常列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/appCompleteAbnormalList")
    MSResponse<MSPage<AbnormalForm>> appCompleteAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);


    /**
     * 短信异常列表
     * @param abnormalFormSearchMode
     * @return
     */
    @PostMapping("abnormalForm/smsAbnormalList")
    MSResponse<MSPage<AbnormalForm>> smsAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchMode);


    /**
     * 旧app异常列表
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/oldAppAbnormalList")
    MSResponse<MSPage<AbnormalForm>> oldAppAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);

    /**
     * 好评异常
     * @param abnormalFormSearchModel
     * @return
     */
    @PostMapping("abnormalForm/kefu/praiseRejectList")
    MSResponse<MSPage<AbnormalForm>> kefuPraiseRejectAbnormalList(@RequestBody AbnormalFormSearchModel abnormalFormSearchModel);

}
