package com.wolfking.jeesite.ms.pdd.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.pdd.sd.PddOrderUpdate;
import com.wolfking.jeesite.ms.pdd.sd.fallback.PddOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "kklplus-b2b-pdd", fallbackFactory = PddOrderFeignFallbackFactory.class)
public interface PddOrderFeign {

    //region 转单

    /**
     * 获取拼多多工单
     */
    @PostMapping("/orderInfo/getList")
    MSResponse<MSPage<B2BOrder>> getList(@RequestBody B2BOrderSearchModel workcardSearchModel);

    /**
     * 检查工单是否可以转换
     */
    @PostMapping("/orderInfo/checkWorkcardProcessFlag")
    MSResponse checkWorkcardProcessFlag(@RequestBody List<B2BOrderTransferResult> workcardIds);

    /**
     * 更新转单进度
     */
    @PostMapping("/orderInfo/updateOrderTransferResult")
    MSResponse updateOrderTransferResult(@RequestBody List<B2BOrderTransferResult> orderTransferResults);

    /**
     * 取消转单工单
     */
    @PostMapping("/orderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult orderTransferResult);


    @GetMapping("/orderInfo/updateInstallFlag")
    MSResponse updateInstallFlag();
    //endregion 转单

    //region 工单处理流程

    /**
     * 派单、预约、上门、取消、退单
     */
    @PostMapping("/orderInfo/update")
    MSResponse workStatus(@RequestBody PddOrderUpdate orderUpdate);

    /**
     * Pdd方取消订单，工单系统处理完成后，回调通知B2B
     */
    @PostMapping("/orderInfo/updateProcessFlag/{id}")
    MSResponse updateProcessFlag(@PathVariable("id") Long id);

    //endregion 工单处理流程
}
