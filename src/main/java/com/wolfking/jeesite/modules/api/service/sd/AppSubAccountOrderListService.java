/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.service.sd;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrder;
import com.wolfking.jeesite.modules.sd.dao.AppSubAccountOrderListDao;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import com.wolfking.jeesite.modules.sd.service.AppOrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 子账号的工单列表
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppSubAccountOrderListService extends AppBaseService {

    @Autowired
    private AppOrderService appOrderService;
    @Resource
    private AppSubAccountOrderListDao appSubAccountOrderListDao;

    //region 工单列表

//    private String getAreaAndCountyName(String fullAreaName) {
//        String result = "";
//        if (StringUtils.isNotBlank(fullAreaName)) {
//            String[] addressArr = fullAreaName.split(" ");
//            if (addressArr.length > 2) {
//                List<String> temp = Lists.newArrayList();
//                for (int i = 2; i < addressArr.length; i++) {
//                    temp.add(addressArr[i]);
//                }
//                result = StringUtils.join(temp, "");
//            }
//        }
//        return result;
//    }
//
//    private String getAppServiceAddress(String fullAreaName, String serviceAddress) {
//        StringBuilder address = new StringBuilder();
//        address.append(getAreaAndCountyName(fullAreaName));
//        if (StringUtils.isNotBlank(serviceAddress)) {
//            address.append(serviceAddress);
//        }
//        return address.toString();
//    }


    /**
     * app未完工工单列表
     */
    public Page<RestOrder> getNotCompletedOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel) {
        searchModel.setPage(page);
        Date startDate = OrderUtils.getGoLiveDate();
        Date endDate = searchModel.getEndAcceptDate() == null ? new Date() : searchModel.getEndAcceptDate();
        Date date;
        if (searchModel.getBeginAcceptDate() != null) {
            date = DateUtils.addMonth(searchModel.getBeginAcceptDate(), -1);
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
        } else {
            date = DateUtils.addMonth(endDate, -5);
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
            searchModel.setBeginAcceptDate(startDate);
        }
        List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
        if (quarters != null && quarters.size() > 0) {
            int size = quarters.size();
            if (size > 2 && searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGAPPOINTMENT) {
                searchModel.setQuarters(Lists.newArrayList(quarters.get(size - 1), quarters.get(size - 2)));
            } else {
                searchModel.setQuarters(quarters);
            }
        }
        Date appointDate = DateUtils.getEndOfDay(new Date());
        searchModel.setAppointmentDate(appointDate);
        List<RestOrder> orderList;
        if (!checkOrderNo(searchModel) || !checkServicePhone(searchModel)) {
            orderList = Lists.newArrayList();
        } else {
            if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGAPPOINTMENT) {
                orderList = getWaitingAppointmentOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_PROCESSING) {
                orderList = getProcessingOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_REMINDER) {
                orderList = getWaitReplyReminderOrderList(searchModel);
            } else {
                orderList = getPedingOrderList(searchModel);
            }
        }
        Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());

        if (orderList != null && orderList.size() > 0) {
            Map<String, Dict> dicts = MSDictUtils.getDictMap(Dict.DICT_TYPE_ORDER_SERVICE_TYPE);
            Set<Integer> sets = Sets.newHashSet(0, 2, 3);
            Dict orderServiceTypeDict;
            for (RestOrder item : orderList) {
                if (item.getPendingType() != null
                        && !sets.contains(item.getPendingType())
                        && item.getAppointDate() != null
                        && DateUtils.pastMinutes(item.getAppointDate()) < 0) {
                    item.setPendingFlag(1);
                } else {
                    item.setPendingFlag(0);
                }
                item.setIsNewOrder(1);
                orderServiceTypeDict = dicts.get(item.getOrderServiceType().toString());
                if (orderServiceTypeDict != null) {
                    item.setOrderServiceTypeName(orderServiceTypeDict.getLabel());
                }
                String address = getAppServiceAddress(item.getAreaName(), item.getServiceAddress());
                item.setServiceAddress(address);
            }
            //待回复催单
            appOrderService.loadWaitReplyReminderInfo(orderList);
            rtnPage.setList(orderList);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        return rtnPage;
    }

    /**
     * 待预约工单
     */
    private List<RestOrder> getWaitingAppointmentOrderList(OrderServicePointSearchModel searchModel) {
        return appSubAccountOrderListDao.getWaitingAppointmentOrderList(searchModel);
    }

    /**
     * 处理中工单
     */
    private List<RestOrder> getProcessingOrderList(OrderServicePointSearchModel searchModel) {
        return appSubAccountOrderListDao.getProcessingOrderList(searchModel);
    }

    /**
     * 催单待回复工单列表
     */
    private List<RestOrder> getWaitReplyReminderOrderList(OrderServicePointSearchModel searchModel) {
        return appSubAccountOrderListDao.getWaitReplyReminderOrderList(searchModel);
    }

    /**
     * 停滞工单
     */
    private List<RestOrder> getPedingOrderList(OrderServicePointSearchModel searchModel) {
        List<RestOrder> orderList = Lists.newArrayList();
        switch (searchModel.getOrderListType()) {
            case OrderServicePointSearchModel.ORDER_LIST_TYPE_PENDING:
                orderList = appSubAccountOrderListDao.getPendingOrderList(searchModel);
                break;
            case OrderServicePointSearchModel.ORDER_LIST_TYPE_APPOINTED:
                orderList = appSubAccountOrderListDao.getAppointedOrderList(searchModel);
                break;
            case OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGPARTS:
                orderList = appSubAccountOrderListDao.getWaitingPartOrderList(searchModel);
                break;
        }
        return orderList;
    }


    //endregion 工单列表


}
