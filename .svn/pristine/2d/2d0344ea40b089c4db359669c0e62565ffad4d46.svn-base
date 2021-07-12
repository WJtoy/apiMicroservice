package com.wolfking.jeesite.ms.jd.sd.service;

import com.kkl.kklplus.entity.canbo.sd.CanboOrderCompleted;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.service.ServicePointService;
import com.wolfking.jeesite.modules.sd.dao.OrderItemDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.entity.TwoTuple;
import com.wolfking.jeesite.modules.sd.service.OrderItemService;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.b2bcenter.md.utils.B2BMDUtils;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderStatusUpdateReqEntity;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BOrderManualBaseService;
import com.wolfking.jeesite.ms.utils.MSUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderService.KKL_SYSTEM_USER;
import static com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterOrderService.USER_ID_KKL_AUTO_GRADE;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JdOrderService extends B2BOrderManualBaseService {

    private static final User KKL_JD_B2B_USER = new User(0L, "快可立全国联保", "075729235666");

    @Autowired
    private ServicePointService servicePointService;

    @Resource
    private OrderItemService orderItemService;

    //-------------------------------------------------------------------------------------------------创建状态变更请求实体
    /**
     * 创建京东派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createJdPlanRequestEntity(Long servicePointId, String engineerName, String engineerMobile) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (servicePointId != null && servicePointId > 0 && StringUtils.isNotBlank(engineerName) && StringUtils.isNotBlank(engineerMobile)) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEngineerName(engineerName)
                    .setEngineerMobile(engineerMobile)
                    .setServicePointId(servicePointId);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }


    /**
     * 创建京东预约派单请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createJdPlanAndAppointRequestEntity(Integer pendingType, Date appointmentDate, Long servicePointId, Long engineerId) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null && servicePointId != null && servicePointId > 0 && engineerId != null && engineerId > 0) {
            Engineer engineer = servicePointService.getEngineerFromCache(servicePointId, engineerId);
            if (engineer != null && StringUtils.isNotBlank(engineer.getName()) && StringUtils.isNotBlank(engineer.getContactInfo())) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setPendingType(pendingType == null ? "" : pendingType.toString())
                        .setEffectiveDate(appointmentDate)
                        .setEngineerName(engineer.getName())
                        .setEngineerMobile(engineer.getContactInfo())
                        .setServicePointId(servicePointId);
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

    /**
     * 创建京东预约请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createJdOnlyAppointRequestEntity(Date appointmentDate) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (appointmentDate != null) {
            B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
            builder.setEffectiveDate(appointmentDate);
            result.setAElement(true);
            result.setBElement(builder);
        }
        return result;
    }

    @Transactional()
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createJdCompleteRequestEntityNew(User updater, Long orderId, String quarter, List<OrderItem> orderItems) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (orderId != null && orderId > 0 && StringUtils.isNotBlank(quarter) && updater != null && updater.getId() != null && updater.getId() > 0) {
            if (updater.getId() == USER_ID_KKL_AUTO_GRADE) {
                updater = new User(KKL_SYSTEM_USER.getId(), KKL_SYSTEM_USER.getName(), KKL_SYSTEM_USER.getMobile());
            } else {
                updater = MSUserUtils.get(updater.getId());
            }
            if (updater == null) {
                updater = new User(KKL_SYSTEM_USER.getId(), KKL_SYSTEM_USER.getName(), KKL_SYSTEM_USER.getMobile());
            }
            if (StringUtils.isBlank(updater.getName())) {
                updater.setName(KKL_SYSTEM_USER.getName());
            }
            if (StringUtils.isBlank(updater.getMobile())) {
                updater.setMobile(KKL_SYSTEM_USER.getMobile());
            }
            if (orderItems == null || orderItems.isEmpty()) {
                Order order = orderItemService.getOrderItems(quarter, orderId);//2020-12-20 sd_order -> sd_order_head
                if (order != null) {
                    orderItems = order.getItems();
                }
            }
            List<CanboOrderCompleted.CompletedItem> completedItems = getOrderCompletedItems(orderId, quarter, orderItems);
            double actualTotalSurcharge = getActualTotalSurcharge(orderId, quarter);
            if (StringUtils.isNotBlank(updater.getName()) && (StringUtils.isNotBlank(updater.getMobile()) || StringUtils.isNotBlank(updater.getPhone()))) {
                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setUpdaterName(updater.getName())
                        .setCompletedItems(completedItems)
                        .setActualTotalSurcharge(actualTotalSurcharge)
                        .setUpdaterMobile(StringUtils.isNotBlank(updater.getMobile()) ? updater.getMobile() : updater.getPhone());
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

    /**
     * 创建京东取消请求对象
     */
    public TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> createJdCancelRequestEntity(Integer kklCancelType, User updater) {
        TwoTuple<Boolean, B2BOrderStatusUpdateReqEntity.Builder> result = new TwoTuple<>(false, null);
        if (kklCancelType != null && updater != null && updater.getId() != null && updater.getId() > 0) {
            updater = MSUserUtils.get(updater.getId());
            if (updater == null) {
                updater = KKL_JD_B2B_USER;
            }
            Integer jdCancelType = B2BMDUtils.getJdCancelType(kklCancelType);
            if (jdCancelType != null) {
                String updaterName = StringUtils.isNotBlank(updater.getName()) ? updater.getName() : KKL_JD_B2B_USER.getName();
                String updaterMobile = StringUtils.isNotBlank(updater.getMobile()) ? updater.getMobile() : updater.getPhone();
                updaterMobile = StringUtils.isNotBlank(updaterMobile) ? updaterMobile : KKL_JD_B2B_USER.getMobile();

                B2BOrderStatusUpdateReqEntity.Builder builder = new B2BOrderStatusUpdateReqEntity.Builder();
                builder.setUpdaterName(updaterName)
                        .setUpdaterMobile(updaterMobile)
                        .setInstallStaus(jdCancelType);
                result.setAElement(true);
                result.setBElement(builder);
            }
        }
        return result;
    }

}
