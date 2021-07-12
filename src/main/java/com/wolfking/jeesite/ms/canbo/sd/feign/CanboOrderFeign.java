package com.wolfking.jeesite.ms.canbo.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2b.order.WorkcardSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderAppointed;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCancelled;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderPlanned;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.canbo.sd.fallback.CanboOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 康宝B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-canbo", fallbackFactory = CanboOrderFeignFallbackFactory.class)
public interface CanboOrderFeign {

    //region 工单转换

    /**
     * 获取康宝工单
     *
     * @param orderSearchModel
     * @return
     */
    @PostMapping("/orderInfo/getList")
    MSResponse<MSPage<B2BOrder>> getList(@RequestBody B2BOrderSearchModel orderSearchModel);

    /**
     * 检查工单是否可转换
     *
     * @param workcardIds
     * @return
     */
    @PostMapping("/orderInfo/checkWorkcardProcessFlag")
    MSResponse checkWorkcardProcessFlag(@RequestBody List<B2BOrderTransferResult> workcardIds);

    /**
     * 更新订单转换进度标志
     *
     * @param workcardTransferResults
     * @return
     */
    @PostMapping("/orderInfo/updateTransferResult")
    MSResponse updateTransferResult(@RequestBody List<B2BOrderTransferResult> workcardTransferResults);

    //endregion


    //region 工单状态变更

    /**
     * 派单
     *
     * @param canboOrderPlanned
     * @return
     */
    @PostMapping("/orderPlanned/planned")
    MSResponse orderPlanned(@RequestBody CanboOrderPlanned canboOrderPlanned);

    /**
     * 预约服务时间
     *
     * @param canboOrderAppointed
     * @return
     */
    @PostMapping("/orderAppointed/appointed")
    MSResponse orderAppointed(@RequestBody CanboOrderAppointed canboOrderAppointed);

    /**
     * 完成服务
     *
     * @param canboOrderCompleted
     * @return
     */
    @PostMapping("/orderCompleted/completed")
    public MSResponse orderCompleted(@RequestBody CanboOrderCompleted canboOrderCompleted);

    /**
     * 取消工单
     *
     * @param canboOrderCancelled
     * @return
     */
    @PostMapping("/orderCancelled/cancelled")
    public MSResponse orderCancelled(@RequestBody CanboOrderCancelled canboOrderCancelled);

    /**
     * 取消工單轉換
     * @param b2BOrderTransferResult
     * @return
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    public MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);
    //endregion

}
