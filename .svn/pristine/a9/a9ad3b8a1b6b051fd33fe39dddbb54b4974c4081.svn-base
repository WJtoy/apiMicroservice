package com.wolfking.jeesite.ms.usatonga.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.usatonga.sd.*;
import com.wolfking.jeesite.ms.usatonga.fallback.UsatonGaOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 威博B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-usaton-ga", fallbackFactory = UsatonGaOrderFeignFallbackFactory.class)
public interface UsatonGaOrderFeign {

    //region 工单转换

    /**
     * 获取工单
     */
    @PostMapping("/orderInfo/getList")
    MSResponse<MSPage<B2BOrder>> getList(@RequestBody B2BOrderSearchModel orderSearchModel);

    /**
     * 检查工单是否可转换
     */
    @PostMapping("/orderInfo/checkWorkcardProcessFlag")
    MSResponse checkWorkcardProcessFlag(@RequestBody List<B2BOrderTransferResult> workcardIds);

    /**
     * 更新订单转换进度标志
     */
    @PostMapping("/orderInfo/updateTransferResult")
    MSResponse updateTransferResult(@RequestBody List<B2BOrderTransferResult> workcardTransferResults);

    //endregion


    //region 工单状态变更

    /**
     * 派单
     */
    @PostMapping("/orderPlanned/planned")
    MSResponse orderPlanned(@RequestBody UsatonGaOrderPlanned orderPlanned);

    /**
     * 预约服务时间
     */
    @PostMapping("/orderAppointed/appointed")
    MSResponse orderAppointed(@RequestBody UsatonGaOrderAppointed orderAppointed);

    /**
     * 完成服务
     */
    @PostMapping("/orderCompleted/completed")
    MSResponse orderCompleted(@RequestBody UsatonGaOrderCompleted orderCompleted);

    /**
     * 取消工单
     */
    @PostMapping("/orderCancelled/cancelled")
    MSResponse orderCancelled(@RequestBody UsatonGaOrderCancelled orderCancelled);

    /**
     * 取消工單轉換
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);
    //endregion

    @PostMapping("orderInfo/saveLog")
    MSResponse saveLog(@RequestBody UsatonGaOrderLog orderLog);
}
