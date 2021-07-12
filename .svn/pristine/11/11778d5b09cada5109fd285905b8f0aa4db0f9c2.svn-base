package com.wolfking.jeesite.ms.philips.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.philips.sd.PhilipsOrderUpdate;
import com.wolfking.jeesite.ms.philips.sd.fallback.PhilipsOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 威博B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-philips", fallbackFactory = PhilipsOrderFeignFallbackFactory.class)
public interface PhilipsOrderFeign {

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

    /**
     * 取消工單轉換
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);

    //endregion


    //region 工单状态变更

    @PostMapping("/order/update/planned")
    MSResponse planned(@RequestBody PhilipsOrderUpdate orderUpdate);

    @PostMapping("/order/update/appointed")
    MSResponse appointed(@RequestBody PhilipsOrderUpdate orderUpdate);

    @PostMapping("/order/update/completed")
    MSResponse completed(@RequestBody PhilipsOrderUpdate orderUpdate);

    @PostMapping("/order/update/cancelled")
    MSResponse cancelled(@RequestBody PhilipsOrderUpdate orderUpdate);
    //endregion

}
