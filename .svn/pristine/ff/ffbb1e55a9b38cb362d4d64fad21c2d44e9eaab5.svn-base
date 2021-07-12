package com.wolfking.jeesite.ms.supor.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.supor.sd.SuporOrderProcess;
import com.wolfking.jeesite.ms.supor.sd.fallback.SuporOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 苏泊尔B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-supor", fallbackFactory = SuporOrderFeignFallbackFactory.class)
public interface SuporOrderFeign {

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
     * 取消工单转换
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);

    //endregion


    //region 工单状态变更

    /**
     * 派单
     */
    @PostMapping("/orderProcess/plan")
     MSResponse plan(@RequestBody SuporOrderProcess orderProcess);

    /**
     * 预约
     */
    @PostMapping("/orderProcess/appoint")
     MSResponse appoint(@RequestBody SuporOrderProcess orderProcess);

    /**
     * 取消
     */
    @PostMapping("/orderProcess/cancel")
     MSResponse cancel(@RequestBody SuporOrderProcess orderProcess);

    //endregion

}
