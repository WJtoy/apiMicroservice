package com.wolfking.jeesite.ms.material.service;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.material.B2BMaterialClose;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.ms.lb.sd.service.LbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新迎燕B2B配件微服务
 *
 * @autor wang shoujiang
 * @date 2020/8/5 16:15
 */
@Slf4j
@Component
public class LbMaterialExecutor extends B2BMaterialExecutor {

    @Autowired
    private LbOrderService lbOrderService;

    @Autowired
    private AreaService areaService;

    /**
     * 申请配件单
     */
    public MSResponse newMaterialForm(MaterialMaster materialMaster) {
        StringBuilder address = new StringBuilder(250);
        if (materialMaster.getApplyType().getIntValue().equals(MaterialMaster.APPLY_TYPE_CHANGJIA)) {//向厂家申请
            if (materialMaster.getReceiverAreaId() != null && materialMaster.getReceiverAreaId() > 0) {
                Area area = areaService.getFromCache(materialMaster.getReceiverAreaId());
                if (area != null) {
                    String areaName = area.getFullName().replaceAll(" ", "-");
                    address.append(areaName).append("-").append(materialMaster.getReceiverAddress());
                } else {
                    address.append(materialMaster.getReceiverAddress());
                }
                materialMaster.setUserName(materialMaster.getReceiver());
                materialMaster.setUserPhone(materialMaster.getReceiverPhone());
                materialMaster.setUserAddress(StringUtils.left(address.toString().trim(), 250));
            } else {
                String areaName = materialMaster.getUserAddress().replaceAll("\\s+", "-");
                materialMaster.setUserAddress(StringUtils.left(areaName, 250));
            }
        } else {
            String areaName = materialMaster.getUserAddress().replaceAll("\\s+", "-");
            materialMaster.setUserAddress(StringUtils.left(areaName, 250));
        }
        address = null;
        return lbOrderService.newMaterial(materialMaster);
    }

    @Override
    public MSResponse materialClose(Long formId, String formNo, B2BMaterialClose.CloseType closeType, String remark, Long user) {
        return new MSResponse<>(MSErrorCode.SUCCESS);
    }

    @Override
    public MSResponse materialCloseByOrder(Long orderId, B2BMaterialClose.CloseType closeType, String remark, Long user) {
        return new MSResponse<>(MSErrorCode.SUCCESS);
    }

    @Override
    public MSResponse materialArrival(Long formId, String formNo, Long arriveAt, String remark, Long user) {
        return new MSResponse<>(MSErrorCode.SUCCESS);
    }

    /**
     * 处理完"审核"消息回调通知微服务
     */
    @Override
    public MSResponse notifyApplyFlag(Long id) {
        return lbOrderService.notifyApplyFlag(id);
    }

    /**
     * 处理完"已发货"消息回调通知微服务
     */
    @Override
    public MSResponse notifyDeliverFlag(Long id) {
        return lbOrderService.notifyDeliverFlag(id);
    }

}
