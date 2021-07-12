package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrder;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AppPrimaryAccountNoSubOrderListDao extends LongIDCrudDao<Order> {

    /**
     * app待预约工单列表
     */
    List<RestOrder> getWaitingAppointmentOrderList(OrderServicePointSearchModel searchModel);

    /**
     * app处理中工单列表
     */
    List<RestOrder> getProcessingOrderList(OrderServicePointSearchModel searchModel);

    /**
     * app催单待回复工单列表
     */
    List<RestOrder> getWaitReplyReminderOrderList(OrderServicePointSearchModel searchModel);

    /**
     * app已预约工单列表
     */
    List<RestOrder> getAppointedOrderList(OrderServicePointSearchModel searchModel);

    /**
     * app等配件工单列表
     */
    List<RestOrder> getWaitingPartOrderList(OrderServicePointSearchModel searchModel);

    /**
     * app停滞工单列表
     */
    List<RestOrder> getPendingOrderList(OrderServicePointSearchModel searchModel);

}
