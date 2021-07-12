package com.wolfking.jeesite.ms.joyoung.sd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderSearchModel;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderTransferResult;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.kkl.kklplus.entity.common.material.B2BMaterialArrival;
import com.kkl.kklplus.entity.common.material.B2BMaterialClose;
import com.kkl.kklplus.entity.joyoung.sd.*;
import com.wolfking.jeesite.ms.joyoung.sd.fallback.JoyoungOrderFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "kklplus-b2b-joyoung", fallbackFactory = JoyoungOrderFeignFallbackFactory.class)
public interface JoyoungOrderFeign {

    /**
     * 九阳工单处理日志
     */
    @RequestMapping("/joyoungOrderProcesslog/saveOrderProcesslog")
    MSResponse saveOrderProcesslog(@RequestBody JoyoungOrderProcessLog orderProcessLog);

    //region 配件

    /**
     * 申请配件单
     */
    @PostMapping("/material/newMaterial")
    MSResponse newMaterialForm(@RequestBody B2BMaterial joyoungMaterial);

    /**
     * by配件单关闭
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     */
    @PostMapping("/material/close")
    MSResponse materialClose(@RequestBody B2BMaterialClose joyoungMaterialClose);

    /**
     * by订单关闭配件单
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     */
    @PostMapping("/material/closeByOrderId")
    MSResponse materialCloseByOrder(@RequestBody B2BMaterialClose joyoungMaterialClose);

    /**
     * 到货
     */
    @PostMapping("/material/arrival")
    MSResponse materialArrival(@RequestBody B2BMaterialArrival joyoungMaterialArrival);

    /**
     * 审核
     * 消息队列处理成功后，同步更新微服务
     */
    @PostMapping("/materialApply/updateFlag/{id}")
    MSResponse updateApplyFlag(@PathVariable("id") Long id);

    /**
     * 发货回调
     * 消息队列处理成功后，同步更新微服务
     */
    @PostMapping("/materialDeliver/updateFlag/{id}")
    MSResponse updateDeliverFlag(@PathVariable("id") Long id);

    //endregion 配件

    /**
     * 条码检查
     */
    @GetMapping("/product/barCodeVerify/{productBarCode}")
    MSResponse getProductData(@PathVariable("productBarCode") String productBarCode);
}
