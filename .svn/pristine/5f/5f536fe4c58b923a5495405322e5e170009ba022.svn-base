package com.wolfking.jeesite.ms.sf.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sf.sd.SfOrderHandle;
import com.wolfking.jeesite.ms.sf.sd.fallback.SFOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 顺丰B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-sf", fallbackFactory = SFOrderFeignFallbackFactory.class)
public interface SFOrderFeign {

    //region 工单转换

    /**
     * 获取B2B工单
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

    /**
     * 取消工單轉換
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);

    //endregion 工单转换

    //region 工单处理

    @PostMapping("/orderHandle/planned")
    MSResponse planned(@RequestBody SfOrderHandle sfOrderHandle);

    @PostMapping("/orderHandle/appointment")
    MSResponse appointment(@RequestBody SfOrderHandle sfOrderHandle);

    @PostMapping("/orderHandle/cancel")
    MSResponse cancel(@RequestBody SfOrderHandle sfOrderHandle);

    @PostMapping("/orderHandle/finish")
    MSResponse finish(@RequestBody SfOrderHandle sfOrderHandle);

    //endregion 工单处理
}
