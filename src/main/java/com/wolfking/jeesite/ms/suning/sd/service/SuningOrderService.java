package com.wolfking.jeesite.ms.suning.sd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderItemCompleteService;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SuningOrderService extends B2BOrderManualBaseService {

    @Autowired
    private OrderItemCompleteService orderItemCompleteService;

    //-------------------------------------------------------------------------------------------------创建状态变更请求实体

    /**
     * 创建苏宁派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createSuningPlanRequestEntity(Long engineerId, String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (engineerId != null && StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile)
                    .setEngineerId(engineerId.toString());
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建苏宁预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createSuningAppointRequestEntity(Date appointmentDate, String remarks) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate)
                    .setRemarks(StringUtils.toString(remarks));
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建苏宁完工请求对象
     */
    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createCompleteRequestEntity(Long orderId, String quarter, Date completedDate) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(completedDate);
            List<OrderItemComplete> completeList = orderItemCompleteService.getByOrderId(orderId, quarter);
            if (completeList != null && !completeList.isEmpty()) {
                CanboOrderCompleted.CompletedItem completedItem = new CanboOrderCompleted.CompletedItem();
                completedItem.setBarcode(StringUtils.toString(completeList.get(0).getUnitBarcode()));
                completedItem.setOutBarcode(StringUtils.toString(completeList.get(0).getOutBarcode()));
                builder.setCompletedItems(Lists.newArrayList(completedItem));
            }
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    /**
     * 创建苏宁取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createSuningCancelRequestEntity(Integer kklCancelType) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (kklCancelType != null) {
            Dict cancelResponsibleDict = MSDictUtils.getDictByValue(kklCancelType.toString(), Dict.DICT_TYPE_CANCEL_RESPONSIBLE);
            if (cancelResponsibleDict != null && StringUtils.isNotBlank(cancelResponsibleDict.getLabel())) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setRemarks(cancelResponsibleDict.getLabel());
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }
}
