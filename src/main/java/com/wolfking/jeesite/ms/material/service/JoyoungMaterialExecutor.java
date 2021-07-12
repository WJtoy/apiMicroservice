package com.wolfking.jeesite.ms.material.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterialClose;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.ms.joyoung.sd.service.JoyoungOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 九阳B2B配件微服务
 * @autor Ryan Lu
 * @date 2019/9/11 16:15
 */
@Slf4j
@Component
public class JoyoungMaterialExecutor extends B2BMaterialExecutor {

    @Autowired
    private JoyoungOrderService joyoungOrderService;

    /**
     * 申请配件单
     */
    public MSResponse newMaterialForm(MaterialMaster materialMaster){
        return joyoungOrderService.newMaterialForm(materialMaster);
        /* test
        if(joyoungOrderService == null){
            return new MSResponse(MSErrorCode.newInstance(MSErrorCode.FAILURE,"服务未实例化"),null);
        }else{
            return new MSResponse(MSErrorCode.SUCCESS);
        }
        */
    }

    /**
     * by配件单关闭
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     * @param formId    配件单id
     * @param formNo    配件单号
     * @param closeType 关闭类型：正常关闭，异常签收，取消
     * @param remark    备注/说明
     */
    public MSResponse materialClose(Long formId, String formNo, B2BMaterialClose.CloseType closeType, String remark,Long user){
        return joyoungOrderService.materialClose(formId,formNo,closeType,remark,user);
    }

    /**
     * by工单关闭配件单
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     * @param orderId   工单id
     * @param closeType 关闭类型：正常关闭，异常签收，取消
     * @param remark    备注/说明
     */
    public MSResponse materialCloseByOrder(Long orderId, B2BMaterialClose.CloseType closeType, String remark,Long user){
        return joyoungOrderService.materialCloseByOrder(orderId,closeType,remark,user);
    }

    /**
     * 到货通知
     * @param formId    配件单id
     * @param formNo    配件单号
     * @param arriveAt  到货时间(时间戳，毫秒)
     * @param remark    备注/说明
     */
    public MSResponse materialArrival(Long formId,String formNo,Long arriveAt,String remark,Long user){
        //return joyoungOrderService.materialArrival(formId,formNo,arriveAt,remark,user);
        return new MSResponse<>(MSErrorCode.SUCCESS);
    }

    /**
     * 更新工单系统配件审核结果后，回调B2B
     * 消息队列处理成功后，同步更新微服务
     */
    public MSResponse notifyApplyFlag(Long id){
        return joyoungOrderService.notifyApplyFlag(id);
    }

    /**
     * 更新工单系统配件：已发货后，回调B2B
     * 消息队列处理成功后，同步更新微服务
     */
    public MSResponse notifyDeliverFlag(Long id){
        return joyoungOrderService.notifyDeliverFlag(id);
    }

}
