package com.wolfking.jeesite.ms.material.service;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterialClose;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * B2B配件微服务(基类)
 * @autor Ryan Lu
 * @date @date 2019/9/11 16:15
 */
@Slf4j
public abstract class B2BMaterialExecutor {

    /**
     * 新建配件单
     */
    public abstract MSResponse newMaterialForm(MaterialMaster materialMaster);

    /**
     * 按配件单关闭配件单
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     * @param formId    配件单id
     * @param formNo    配件单号
     * @param closeType 关闭类型：正常关闭，异常签收，取消
     * @param remark    备注/说明
     */
    public abstract MSResponse materialClose(Long formId, String formNo, B2BMaterialClose.CloseType closeType, String remark,Long user);

    /**
     * 关闭配件单
     * 包含正常关闭，异常签收，取消(订单退单/取消)
     * @param orderId    订单id
     * @param closeType 关闭类型：正常关闭，异常签收，取消
     * @param remark    备注/说明
     */
    public abstract MSResponse materialCloseByOrder(Long orderId, B2BMaterialClose.CloseType closeType, String remark,Long user);

    /**
     * 到货通知
     * @param formId    配件单id
     * @param formNo    配件单号
     * @param arriveAt  到货时间(时间戳，毫秒)
     * @param remark    备注/说明
     */
    public abstract MSResponse materialArrival(Long formId,String formNo,Long arriveAt,String remark,Long user);

    /**
     * 更新工单系统配件审核结果后，回调B2B
     * 消息队列处理成功后，同步更新微服务
     */
    public abstract MSResponse notifyApplyFlag(Long id);

    /**
     * 更新工单系统配件：已发货后，回调B2B
     * 消息队列处理成功后，同步更新微服务
     */
    public abstract MSResponse notifyDeliverFlag(Long id);
}
