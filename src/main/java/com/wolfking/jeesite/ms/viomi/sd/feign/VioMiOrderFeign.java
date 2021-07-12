package com.wolfking.jeesite.ms.viomi.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.viomi.sd.VioMiOrderCancel;
import com.kkl.kklplus.entity.viomi.sd.VioMiOrderHandle;
import com.kkl.kklplus.entity.viomi.sd.VioMiOrderRemark;
import com.kkl.kklplus.entity.viomi.sd.VioMiOrderSnCode;
import com.wolfking.jeesite.ms.viomi.sd.fallback.VioMiOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 云米B2B微服务接口调用
 */
@FeignClient(name = "kklplus-b2b-viomi", fallbackFactory = VioMiOrderFeignFallbackFactory.class)
public interface VioMiOrderFeign {

    //region 工单转换

    /**
     * 获取B2B工单
     */
    @PostMapping("/viomiOrderInfo/getList")
    MSResponse<MSPage<B2BOrder>> getList(@RequestBody B2BOrderSearchModel orderSearchModel);

    /**
     * 检查工单是否可转换
     */
    @PostMapping("/viomiOrderInfo/checkWorkcardProcessFlag")
    MSResponse checkWorkcardProcessFlag(@RequestBody List<B2BOrderTransferResult> workcardIds);

    /**
     * 更新订单转换进度标志
     */
    @PostMapping("/viomiOrderInfo/updateTransferResult")
    MSResponse updateTransferResult(@RequestBody List<B2BOrderTransferResult> workcardTransferResults);

    /**
     * 取消工單轉換
     */
    @PostMapping("/viomiOrderInfo/cancelOrderTransition")
    MSResponse cancelOrderTransition(@RequestBody B2BOrderTransferResult b2BOrderTransferResult);

    //endregion 工单转换

    //region 工单处理

    /**
     * 派送师傅
     */
    @PostMapping("/order/handle/planing")
    MSResponse<Integer> planing(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 预约上门
     */
    @PostMapping("/order/handle/appointment")
    MSResponse<Integer> appointment(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 上门打卡
     */
    @PostMapping("/order/handle/clockInHome")
    MSResponse<Integer> clockInHome(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 处理完成
     */
    @PostMapping("/order/handle/processComplete")
    MSResponse<Integer> processComplete(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 申请完单
     */
    @PostMapping("/order/handle/applyFinished")
    MSResponse<Integer> applyFinished(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 工单回访
     */
    @PostMapping("/order/handle/orderReturnVisit")
    MSResponse<Integer> orderReturnVisit(@RequestBody VioMiOrderHandle vioMiOrderHandle);

    /**
     * 取消工单
     */
    @PostMapping("/order/cancel")
    MSResponse cancel(@RequestBody VioMiOrderCancel cancel);

    /**
     * 保存工单日志
     */
    @PostMapping("/viomiOrderInfo/log")
    MSResponse saveLog(@RequestBody VioMiOrderRemark remark);

    //endregion 工单处理

    //region 确认消息消费

    @GetMapping("/order/cancel/updateProcessFlag/{id}")
    MSResponse updateProcessFlag(@PathVariable("id") Long id);

    //endregion 确认消息消费

    //region 产品

    /**
     * 获取产品配件
     */
    @GetMapping("/vioMiProduct/getProductParts/{product69Code}/{createById}")
    MSResponse getProductParts(@PathVariable("product69Code") String product69Code, @PathVariable("createById") Long createById);

    /**
     * 获取故障类别
     */
    @PostMapping("/vioMiProduct/getFaultType")
    MSResponse getFaultType();

    /**
     * 产品SN码验证
     */
    @PostMapping("/vioMiProduct/getGradeSn")
    MSResponse getGradeSn(@RequestBody VioMiOrderSnCode vioMiOrderSnCode);

    //endregion 产品

}
