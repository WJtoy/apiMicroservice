package com.wolfking.jeesite.modules.sd.dao;

import com.wolfking.jeesite.common.persistence.LongIDCrudDao;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrder;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderDetail;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface AppOrderDao extends LongIDCrudDao<Order> {

    /**
     * 查询安维师傅可接工单列表
     * @param excludeDataSources 排除的数据源
     */  //TODO: getGradOrderListWithoutEngineerArea是getGrabOrderList方法的去EngineerArea版和去ProductCategoryIds版 // add on 2020-6-20
    List<Order> getGradOrderListWithoutEngineerAreaAndProductCategory(@Param("quarters") List<String> quarters,
                                                                      @Param("areaIds") List<Long> areaIds,
                                                                      @Param("productCategoryIds") List<Long> productCategoryIds,
                                                                      @Param("excludeDataSources") List<Integer> excludeDataSources);

    /**
     * 查询安维的待回访工单列表
     */
    List<Order> getAppCompletedOrderListForPrimaryAccount(OrderServicePointSearchModel searchModel);

    List<Order> getAppCompletedOrderListForSubAccount(OrderServicePointSearchModel searchModel);

    /**
     * 获取工单的实际服务明细 - 工单的数量要在50以内
     */
    List<OrderDetail> getOrderDetailListByOrderIdsNew(@Param("quarters") List<String> quarters,
                                                      @Param("orderIds") List<Long> orderIds,
                                                      @Param("servicePointId") Long servicePointId,
                                                      @Param("engineerId") Long engineerId);


    /**
     * 查询安维的待审核、已入账工单列表
     */
    List<Order> getCompletedOrderListForPrimaryAccount(OrderServicePointSearchModel searchModel);

    List<Order> getCompletedOrderListForSubAccount(OrderServicePointSearchModel searchModel);


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
