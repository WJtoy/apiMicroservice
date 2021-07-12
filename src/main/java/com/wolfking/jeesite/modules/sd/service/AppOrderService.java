/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.cc.vm.BulkRereminderCheckModel;
import com.kkl.kklplus.entity.cc.vm.ReminderOrderModel;
import com.kkl.kklplus.entity.cc.vm.ReminderTimeLinessModel;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.sd.*;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveOrderAuxiliaryMaterialsRequest;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestSaveOrderAuxiliaryMaterialsRequestV4;
import com.wolfking.jeesite.modules.api.service.sd.AppBaseService;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.md.utils.ProductUtils;
import com.wolfking.jeesite.modules.sd.dao.AppOrderDao;
import com.wolfking.jeesite.modules.sd.dao.AppPrimaryAccountNoSubOrderListDao;
import com.wolfking.jeesite.modules.sd.dao.OrderDao;
import com.wolfking.jeesite.modules.sd.dao.OrderItemCompleteDao;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.entity.viewModel.OrderServicePointSearchModel;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderItemUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.cc.service.ReminderService;
import com.wolfking.jeesite.ms.providermd.service.MSEngineerAreaService;
import com.wolfking.jeesite.ms.providermd.service.MSProductCategoryServicePointService;
import com.wolfking.jeesite.ms.providermd.utils.AuxiliaryMaterialUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * App工单
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppOrderService extends AppBaseService {

    @Resource
    private AppOrderDao appOrderDao;

    @Resource
    private AppPrimaryAccountNoSubOrderListDao appPrimaryAccountNoSubOrderListDao;

    @Resource
    private OrderDao orderDao;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Resource
    private OrderItemCompleteDao orderItemCompleteDao;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderAuxiliaryMaterialService orderAuxiliaryMaterialService;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private MSEngineerAreaService msEngineerAreaService;

    @Autowired
    private MSProductCategoryServicePointService msProductCategoryServicePointService;

    //region 工单列表

    /**
     * APP待接单列表
     */
    public List<RestOrderGrab> getGrabOrderList(Long servicePointId, Long engineerId) {
        List<RestOrderGrab> restOrderGrabList = Lists.newArrayList();
        if (engineerId != null && engineerId != 0) {

            Date endDate = new Date();
            Date startDate = DateUtils.addMonth(endDate, -2);
            List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
            List<Long> areaids = msEngineerAreaService.findEngineerAreaIds(engineerId);
            List<Long> productCategoryIds = msProductCategoryServicePointService.findListByServicePiontIdFromCacheForSD(servicePointId);
            if (productCategoryIds == null) {
                productCategoryIds = Lists.newArrayList();
            }
            // 2020-12-31 京东优易+的订单不允许网点抢单
            List<Integer> excludeDataSources = Lists.newArrayList(B2BDataSourceEnum.JDUEPLUS.getId());
            List<Order> orderList = appOrderDao.getGradOrderListWithoutEngineerAreaAndProductCategory(quarters, areaids, productCategoryIds, excludeDataSources);
            if (orderList != null && orderList.size() > 0) {
                Map<String, Dict> orderServiceTypeMap = MSDictUtils.getDictMap("order_service_type");
                Dict orderServiceTypeDict = null;
                List<OrderItem> orderItemList = Lists.newArrayList();
                for (Order item : orderList) {
                    item.setItems(OrderItemUtils.pbToItems(item.getItemsPb()));//2020-12-19 sd_order -> sd_order_head
                    orderItemList.addAll(item.getItems());
                    if (item.getOrderCondition() != null) {
                        String address = getAppServiceAddress(item.getOrderCondition().getAreaName(), item.getOrderCondition().getServiceAddress());
                        item.getOrderCondition().setServiceAddress(address);
                        orderServiceTypeDict = orderServiceTypeMap.get(String.valueOf(item.getOrderCondition().getOrderServiceType()));
                        item.getOrderCondition().setOrderServiceTypeName(orderServiceTypeDict != null ? orderServiceTypeDict.getLabel() : "");
                    }
                }
                OrderItemUtils.setOrderItemProperties(orderItemList, Sets.newHashSet(CacheDataTypeEnum.SERVICETYPE, CacheDataTypeEnum.PRODUCT));

                RestOrderGrab restOrderGrab = null;
                for (Order item : orderList) {
                    restOrderGrab = mapper.map(item, RestOrderGrab.class);
                    restOrderGrabList.add(restOrderGrab);
                }
            }
        }
        return restOrderGrabList;
    }

    /**
     * app待客评工单列表
     */
    public Page<RestOrderGrading> getGradingOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel entity, boolean isPrimaryAccount) {
        entity.setPage(page);
        List<RestOrderGrading> restOrderHistoryList = Lists.newArrayList();
        List<String> quarters = getQuarters(entity);
        entity.setQuarters(quarters);
        List<Order> orderList;
        if (isPrimaryAccount) {
            orderList = appOrderDao.getAppCompletedOrderListForPrimaryAccount(entity);
        } else {
            orderList = appOrderDao.getAppCompletedOrderListForSubAccount(entity);
        }
        Page<RestOrderGrading> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());
        if (orderList != null && orderList.size() > 0) {
            List<Long> orderIdList = orderList.stream().map(i -> i.getOrderCondition().getOrderId()).collect(Collectors.toList());
            Long servicePointId = entity.getServicePointId();
            Long engineerId = entity.getEngineerId();
            List<OrderDetail> orderDetailList = appOrderDao.getOrderDetailListByOrderIdsNew(quarters, orderIdList, servicePointId, engineerId);

            Map<Long, List<OrderDetail>> orderDetailMap = Maps.newHashMap();
            if (orderDetailList != null && orderDetailList.size() > 0) {
                Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
                Map<Long, Product> productMap = ProductUtils.getAllProductMap();
                for (OrderDetail item : orderDetailList) {
                    item.setServiceType(serviceTypeMap.get(item.getServiceType().getId()));
                    item.setProduct(productMap.get(item.getProductId()));
                    if (orderDetailMap.containsKey(item.getOrderId())) {
                        orderDetailMap.get(item.getOrderId()).add(item);
                    } else {
                        orderDetailMap.put(item.getOrderId(), Lists.newArrayList(item));
                    }
                }
            }

            Map<String, Dict> orderServiceTypeMap = MSDictUtils.getDictMap("order_service_type");
            Dict orderServiceTypeDict = null;
            for (Order order : orderList) {
                List<OrderDetail> orderDetails = orderDetailMap.get(order.getOrderCondition().getOrderId());
                if (orderDetails != null) {
                    order.setDetailList(orderDetails);
                }
                if (order.getOrderCondition() != null) {
                    order.getOrderCondition().getStatus().setLabel("待客评");
                    String address = getAppServiceAddress(order.getOrderCondition().getAreaName(), order.getOrderCondition().getServiceAddress());
                    order.getOrderCondition().setServiceAddress(address);

                    orderServiceTypeDict = orderServiceTypeMap.get(String.valueOf(order.getOrderCondition().getOrderServiceType()));
                    order.getOrderCondition().setOrderServiceTypeName(orderServiceTypeDict != null ? orderServiceTypeDict.getLabel() : "");
                }
            }

            RestOrderGrading restOrderGrading = null;
            for (Order item : orderList) {
                restOrderGrading = mapper.map(item, RestOrderGrading.class);
                restOrderHistoryList.add(restOrderGrading);
            }
            rtnPage.setList(restOrderHistoryList);
        }

        return rtnPage;
    }

    /**
     * app完成工单列表（已客评、已对账）
     */
    public Page<RestOrderGrading> getCompletedOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel entity, boolean isPrimaryAccount) {
        List<RestOrderGrading> restOrderHistoryList = Lists.newArrayList();
        int offset = (page.getPageNo() - 1) * page.getPageSize();
        List<String> quarters = getQuarters(entity);
        entity.setLimitOffset(offset);
        entity.setLimitRows(page.getPageSize());
        entity.setQuarters(quarters);
        List<Order> orderList;
        if (isPrimaryAccount) {
            orderList = appOrderDao.getCompletedOrderListForPrimaryAccount(entity);
        } else {
            orderList = appOrderDao.getCompletedOrderListForSubAccount(entity);
        }
        Page<RestOrderGrading> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());
        if (orderList != null && orderList.size() > 0) {
            List<Long> orderIdList = orderList.stream().map(i -> i.getOrderCondition().getOrderId()).collect(Collectors.toList());
            Long servicePointId = entity.getServicePointId();
            Long engineerId = entity.getEngineerId();
            List<OrderDetail> orderDetailList = appOrderDao.getOrderDetailListByOrderIdsNew(quarters, orderIdList, servicePointId, engineerId);

            Map<Long, List<OrderDetail>> orderDetailMap = Maps.newHashMap();
            if (orderDetailList != null && orderDetailList.size() > 0) {
                Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
                Map<Long, Product> productMap = ProductUtils.getAllProductMap();
                for (OrderDetail item : orderDetailList) {
                    item.setServiceType(serviceTypeMap.get(item.getServiceType().getId()));
                    item.setProduct(productMap.get(item.getProductId()));
                    if (orderDetailMap.containsKey(item.getOrderId())) {
                        orderDetailMap.get(item.getOrderId()).add(item);
                    } else {
                        orderDetailMap.put(item.getOrderId(), Lists.newArrayList(item));
                    }
                }
            }

            Map<String, Dict> orderServiceTypeMap = MSDictUtils.getDictMap("order_service_type");
            Dict orderServiceTypeDict = null;
            for (Order order : orderList) {
                List<OrderDetail> orderDetails = orderDetailMap.get(order.getOrderCondition().getOrderId());
                if (orderDetails != null) {
                    order.setDetailList(orderDetails);
                }
                if (order.getOrderCondition() != null) {
                    order.getOrderCondition().getStatus().setLabel(order.getOrderCondition().getChargeFlag() == 1 ? "已入账" : "待客评");
                    String address = getAppServiceAddress(order.getOrderCondition().getAreaName(), order.getOrderCondition().getServiceAddress());
                    order.getOrderCondition().setServiceAddress(address);

                    orderServiceTypeDict = orderServiceTypeMap.get(String.valueOf(order.getOrderCondition().getOrderServiceType()));
                    order.getOrderCondition().setOrderServiceTypeName(orderServiceTypeDict != null ? orderServiceTypeDict.getLabel() : "");
                }
            }

            RestOrderGrading restOrderGrading = null;
            for (Order item : orderList) {
                restOrderGrading = mapper.map(item, RestOrderGrading.class);
                restOrderHistoryList.add(restOrderGrading);
            }
            rtnPage.setList(restOrderHistoryList);
        }
        if (restOrderHistoryList.size() == page.getPageSize()) {
            rtnPage.setAppPageCount(page.getPageNo() + 1);
        } else {
            rtnPage.setAppPageCount(page.getPageNo());
        }
        rtnPage.setAppRowCount((page.getPageNo() - 1) * page.getPageSize() + restOrderHistoryList.size());

        return rtnPage;
    }

    /**
     * app未完工工单列表
     */
    public Page<RestOrder> getNotCompletedOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel, boolean hasNoSubAccount) {
        if (!checkOrderNo(searchModel) || !checkServicePhone(searchModel)) {
            Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
            rtnPage.setOrderBy(page.getOrderBy());
            rtnPage.setList(Lists.newArrayList());
            return rtnPage;
        } else {
            if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGAPPOINTMENT) {
                return getWaitingAppointmentOrderList(page, searchModel, hasNoSubAccount);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_PROCESSING) {
                return getProcessingOrderList(page, searchModel, hasNoSubAccount);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_REMINDER) {
                return getWaitReplyReminderOrderList(page, searchModel, hasNoSubAccount);
            } else {
                return getPedingOrderList(page, searchModel, hasNoSubAccount);
            }
        }
    }

    /**
     * app待预约工单列表
     */
    private Page<RestOrder> getWaitingAppointmentOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel, boolean hasNoSubAccount) {
        searchModel.setPage(page);
        Date startDate = OrderUtils.getGoLiveDate();
        Date endDate = searchModel.getEndAcceptDate() == null ? new Date() : searchModel.getEndAcceptDate();
        Date date;
        if (searchModel.getBeginAcceptDate() != null) {
            //查询日期是派单日期，分片是按下单分片，所以在此时间上加一个月
            date = DateUtils.addMonth(searchModel.getBeginAcceptDate(), -1);
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
        } else {
            date = DateUtils.addMonth(endDate, -5);//5个月
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
            searchModel.setBeginAcceptDate(startDate);
        }
        List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
        if (quarters != null && quarters.size() > 0) {
            int size = quarters.size();
            if (size > 2) {
                searchModel.setQuarters(Lists.newArrayList(quarters.get(size - 1), quarters.get(size - 2)));
            } else {
                searchModel.setQuarters(quarters);
            }
        }
        Date appointDate = DateUtils.getEndOfDay(new Date());
        searchModel.setAppointmentDate(appointDate);
        List<RestOrder> orderList;
        if (hasNoSubAccount) {
            orderList = appPrimaryAccountNoSubOrderListDao.getWaitingAppointmentOrderList(searchModel);
        } else {
            orderList = appOrderDao.getWaitingAppointmentOrderList(searchModel);
        }
        Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());

        if (orderList != null && orderList.size() > 0) {
            Map<String, Dict> dicts = MSDictUtils.getDictMap("order_service_type");
            Set<Integer> sets = Sets.newHashSet(0, 2, 3);
            Dict orderServiceTypeDict = null;
            Area area = null;
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
            loadWaitReplyReminderInfo(orderList);
            rtnPage.setList(orderList);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        return rtnPage;
    }

    /**
     * app处理中工单列表
     */
    private Page<RestOrder> getProcessingOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel, boolean hasNoSubAccount) {
        searchModel.setPage(page);
        Date startDate = OrderUtils.getGoLiveDate();
        Date endDate = searchModel.getEndAcceptDate() == null ? new Date() : searchModel.getEndAcceptDate();
        Date date;
        if (searchModel.getBeginAcceptDate() != null) {
            //查询日期是派单日期，分片是按下单分片，所以在此时间上加一个月
            date = DateUtils.addMonth(searchModel.getBeginAcceptDate(), -1);
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
        } else {
            date = DateUtils.addMonth(endDate, -5);//5个月
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
            searchModel.setBeginAcceptDate(startDate);
        }
        List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
        if (quarters != null && quarters.size() > 0) {
            searchModel.setQuarters(quarters);
        }
        Date appointDate = DateUtils.getEndOfDay(new Date());
        searchModel.setAppointmentDate(appointDate);
        List<RestOrder> orderList;
        if (hasNoSubAccount) {
            orderList = appPrimaryAccountNoSubOrderListDao.getProcessingOrderList(searchModel);
        } else {
            orderList = appOrderDao.getProcessingOrderList(searchModel);
        }
        Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());

        if (orderList != null && orderList.size() > 0) {
            Map<String, Dict> dicts = MSDictUtils.getDictMap("order_service_type");
            Set<Integer> sets = Sets.newHashSet(0, 2, 3);
            Dict orderServiceTypeDict = null;

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
            loadWaitReplyReminderInfo(orderList);
            rtnPage.setList(orderList);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        return rtnPage;
    }

    /**
     * app催单待回复工单列表
     */
    private Page<RestOrder> getWaitReplyReminderOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel, boolean hasNoSubAccount) {
        searchModel.setPage(page);
        Date startDate = OrderUtils.getGoLiveDate();
        Date endDate = searchModel.getEndAcceptDate() == null ? new Date() : searchModel.getEndAcceptDate();
        Date date;
        if (searchModel.getBeginAcceptDate() != null) {
            //查询日期是派单日期，分片是按下单分片，所以在此时间上加一个月
            date = DateUtils.addMonth(searchModel.getBeginAcceptDate(), -1);
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
        } else {
            date = DateUtils.addMonth(endDate, -5);//5个月
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
            searchModel.setBeginAcceptDate(startDate);
        }
        List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
        if (quarters != null && quarters.size() > 0) {
            searchModel.setQuarters(quarters);
        }
        Date appointDate = DateUtils.getEndOfDay(new Date());
        searchModel.setAppointmentDate(appointDate);
        List<RestOrder> orderList;
        if (hasNoSubAccount) {
            orderList = appPrimaryAccountNoSubOrderListDao.getWaitReplyReminderOrderList(searchModel);
        } else {
            orderList = appOrderDao.getWaitReplyReminderOrderList(searchModel);
        }
        Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());

        if (orderList != null && orderList.size() > 0) {
            Map<String, Dict> dicts = MSDictUtils.getDictMap("order_service_type");
            Set<Integer> sets = Sets.newHashSet(0, 2, 3);
            Dict orderServiceTypeDict = null;

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
            loadWaitReplyReminderInfo(orderList);
            rtnPage.setList(orderList);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        return rtnPage;
    }

    /**
     * app停滞工单列表
     */
    private Page<RestOrder> getPedingOrderList(Page<OrderServicePointSearchModel> page, OrderServicePointSearchModel searchModel, boolean hasNoSubAccount) {
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
            date = DateUtils.addMonth(endDate, -5);//5个月
            if (date.getTime() > startDate.getTime()) {
                startDate = date;
            }
            searchModel.setBeginAcceptDate(startDate);
        }
        List<String> quarters = QuarterUtils.getQuarters(startDate, endDate);
        if (quarters != null && quarters.size() > 0) {
            searchModel.setQuarters(quarters);
        }
        Date appointDate = DateUtils.getEndOfDay(new Date());
        searchModel.setAppointmentDate(appointDate);

        List<RestOrder> orderList = Lists.newArrayList();
        if (hasNoSubAccount) {
            if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_PENDING) {
                orderList = appPrimaryAccountNoSubOrderListDao.getPendingOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_APPOINTED) {
                orderList = appPrimaryAccountNoSubOrderListDao.getAppointedOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGPARTS) {
                orderList = appPrimaryAccountNoSubOrderListDao.getWaitingPartOrderList(searchModel);
            }
        } else {
            if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_PENDING) {
                orderList = appOrderDao.getPendingOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_APPOINTED) {
                orderList = appOrderDao.getAppointedOrderList(searchModel);
            } else if (searchModel.getOrderListType() == OrderServicePointSearchModel.ORDER_LIST_TYPE_WAITINGPARTS) {
                orderList = appOrderDao.getWaitingPartOrderList(searchModel);
            }
        }

        Page<RestOrder> rtnPage = new Page<>(page.getPageNo(), page.getPageSize(), page.getCount());
        rtnPage.setOrderBy(page.getOrderBy());

        if (orderList != null && orderList.size() > 0) {
            Map<String, Dict> dicts = MSDictUtils.getDictMap("order_service_type");
            Set<Integer> sets = Sets.newHashSet(0, 2, 3);
            Dict orderServiceTypeDict = null;
            Area area = null;
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
            loadWaitReplyReminderInfo(orderList);
            rtnPage.setList(orderList);
        } else {
            rtnPage.setList(Lists.newArrayList());
        }
        return rtnPage;
    }

    private List<String> getQuarters(OrderServicePointSearchModel searchModel) {
        Date startDate = OrderUtils.getGoLiveDate();
        Date endDate = (searchModel.getEndAcceptDate() == null ? new Date() : searchModel.getEndAcceptDate());
        Date tempDate;
        if (searchModel.getBeginAcceptDate() != null) {
            tempDate = DateUtils.addMonth(searchModel.getBeginAcceptDate(), -1);//查询日期是派单日期，分片是按下单分片，所以在此时间上加一个月
            if (tempDate.getTime() > startDate.getTime()) {
                startDate = tempDate;
            }
        } else {
            tempDate = DateUtils.addMonth(endDate, -5);//4个月
            if (tempDate.getTime() > startDate.getTime()) {
                startDate = tempDate;
            }
        }
        return QuarterUtils.getQuarters(startDate, endDate);
    }

    //endregion 工单列表

    //region 日志

    public List<OrderProcessLog> getOrderProcessLogs(Long orderId, String quarter, Long servicePointId) {
        List<OrderProcessLog> list = Lists.newArrayList();
        if (orderId != null && servicePointId != null && StringUtils.isNotBlank(quarter)) {
            List<OrderProcessLog> logList = orderDao.getAppOrderLogs(orderId, quarter);
            List<OrderPlan> planList = orderDao.getOrderPlanList(orderId, quarter, null);
            list = OrderUtils.filterServicePointOrderProcessLog(logList, planList, servicePointId);
        }

        return list;
    }

    //endregion 日志

    //region 工单图片

    /**
     * 获取订单需上传的图片规格
     */
    public List<RestProductCompletePic> getProductPicRules(List<OrderItem> items, Long customerId) {
        List<RestProductCompletePic> result = Lists.newArrayList();
        if (items != null && !items.isEmpty()) {
            List<Long> productIds = items.stream()
                    .filter(i -> i.getProduct() != null && i.getProduct().getId() != null)
                    .map(OrderItem::getProductId).collect(Collectors.toList());
            Map<Long, Product> productMap = productService.getProductMap(productIds);
            List<TwoTuple<Long, Integer>> productQtyList = Lists.newArrayList();
            Product product = null;
            Long productId = null;
            for (OrderItem item : items) {
                product = productMap.get(item.getProductId());
                if (product != null) {
                    if (product.getSetFlag() == 1) {
                        final String[] setIds = product.getProductIds().split(",");
                        for (String id : setIds) {
                            productId = StringUtils.toLong(id);
                            if (productId > 0) {
                                productQtyList.add(new TwoTuple<>(productId, item.getQty()));
                            }
                        }
                    } else {
                        productQtyList.add(new TwoTuple<>(item.getProductId(), item.getQty()));
                    }
                }
            }

            if (!productQtyList.isEmpty()) {
                productIds = productQtyList.stream().map(TwoTuple::getAElement).collect(Collectors.toList());
                Map<Long, ProductCompletePic> productCompletePicMap = OrderUtils.getCustomerProductCompletePicMap(productIds, customerId);
                List<RestProductCompletePic> restProductCompletePicList = Lists.newArrayList();
                ProductCompletePic productCompletePic = null;
                RestProductCompletePic restProductPic = null;
                for (TwoTuple<Long, Integer> item : productQtyList) {
                    productCompletePic = productCompletePicMap.get(item.getAElement());
                    if (productCompletePic != null) {
                        restProductPic = mapper.map(productCompletePic, RestProductCompletePic.class);
                        restProductPic.setProductQty(item.getBElement());
                        restProductCompletePicList.add(restProductPic);
                    }
                }
                if (!restProductCompletePicList.isEmpty()) {
                    result = restProductCompletePicList.stream().sorted(Comparator.comparing(RestProductCompletePic::getProductId)).collect(Collectors.toList());
                }
            }
        }

        return result;
    }

    /**
     * 获取工单已上传的图片
     */
    public List<RestProductCompletePic> getUploadedProductPics(Long orderId, String quarter, Long customerId) {
        List<RestProductCompletePic> result = Lists.newArrayList();
        if (orderId != null) {
            List<OrderItemComplete> list = orderItemCompleteDao.getByOrderId(orderId, quarter);
            if (list != null && !list.isEmpty()) {
                List<Long> productIds = list.stream().map(i -> i.getProduct().getId()).collect(Collectors.toList());
                Map<Long, Product> productMap = productService.getProductMap(productIds);
//                Map<Long, ProductCompletePic> productCompletePicMap = productCompletePicService.getProductCompletePicMap(productIds);
                Map<Long, ProductCompletePic> productCompletePicMap = OrderUtils.getCustomerProductCompletePicMap(productIds, customerId);
                Product product;
                Set<String> pictureCodeSet;
                RestProductCompletePic restProductPic;
                for (OrderItemComplete item : list) {
                    product = productMap.get(item.getProduct().getId());
                    if (product != null) {
                        item.setProduct(product);
                    }
                    item.setItemList(OrderUtils.fromProductCompletePicItemsJson(item.getPicJson()));
                    if (item.getItemList() != null && !item.getItemList().isEmpty()) {
                        pictureCodeSet = Sets.newHashSet();
                        for (ProductCompletePicItem picItem : item.getItemList()) {
                            pictureCodeSet.add(picItem.getPictureCode());
                            if (StringUtils.isNotBlank(picItem.getUrl())) {
                                picItem.setUrl(OrderPicUtils.getOrderPicHostDir() + picItem.getUrl());
                            }
                        }
                        ProductCompletePic productPicRule = productCompletePicMap.get(item.getProduct().getId());
                        if (productPicRule != null && !productPicRule.getItems().isEmpty()) {
                            for (ProductCompletePicItem picItem : productPicRule.getItems()) {
                                if (!pictureCodeSet.contains(picItem.getPictureCode())) {
                                    item.getItemList().add(picItem);
                                }
                            }
                        }
                        restProductPic = mapper.map(item, RestProductCompletePic.class);
                        restProductPic.setProductQty(1);
                        result.add(restProductPic);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 上传图片
     */
    @Transactional(readOnly = false)
    public RestUploadProductCompletePic uploadOrderPic(String picFileName, Long orderId, String quarter, Long productId, Long uniqueId, String pictureCode, User user, Long customerId) {
        RestUploadProductCompletePic restPicItem = null;
        if (orderId != null && StringUtils.isNotBlank(quarter) && productId != null && productId > 0 && StringUtils.isNotBlank(pictureCode) && StringUtils.isNotBlank(picFileName)) {
            try {
                OrderItemComplete completePic = null;
                ProductCompletePicItem picItem = null;
                if (uniqueId != null && uniqueId > 0) {
                    completePic = orderItemCompleteDao.getById(uniqueId, quarter);
                }
                Map<String, ProductCompletePicItem> picItemMap = Maps.newHashMap();
                boolean isAddOperation = true;
                if (completePic != null) {
                    completePic.setItemList(OrderUtils.fromProductCompletePicItemsJson(completePic.getPicJson()));
                    for (ProductCompletePicItem item : completePic.getItemList()) {
                        picItemMap.put(item.getPictureCode(), item);
                        if (pictureCode.equals(item.getPictureCode())) {
                            picItem = item;
                            isAddOperation = false;
                        }
                    }
                } else {
                    Integer uploadedProductQty = orderItemCompleteDao.getProductQty(orderId, quarter, productId);
                    Order order = orderItemService.getOrderItems(quarter, orderId);
                    int productQty = 0;
                    if (order != null && !order.getItems().isEmpty()) {
                        productQty = getProductQty(order.getItems(), productId);
                    }
                    if (productQty <= uploadedProductQty) {
                        throw new OrderException("图片组数不能大于产品数量");
                    }
                }

                if (picItem == null) {
                    ProductCompletePic productPicRule = OrderUtils.getCustomerProductCompletePic(productId, customerId);
                    if (productPicRule != null && !productPicRule.getItems().isEmpty()) {
                        picItem = productPicRule.getItems().stream().filter(i -> pictureCode.equals(i.getPictureCode())).findFirst().orElse(null);
                        if (picItem != null) {
                            picItemMap.put(pictureCode, picItem);
                        } else {
                            throw new RuntimeException("产品没有配置图片规格");
                        }
                    }
                }
                if (picItem != null) {
                    picItem.setUrl(picFileName);
                    Date now = new Date();
                    picItem.setUploadDate(now);// 上传日期 2019-06-25
                    if (completePic == null) {
                        completePic = new OrderItemComplete();
                        completePic.setCreateBy(user);
                        completePic.setCreateDate(now);
                        completePic.setUpdateBy(user);
                        completePic.setUpdateDate(now);
                        completePic.setOrderId(orderId);
                        completePic.setQuarter(quarter);
                        completePic.setProduct(new Product(productId));
                        completePic.setItemNo(0);
                        completePic.setUploadQty(1);
                        String picJson = OrderUtils.toProductCompletePicItemsJson(Lists.newArrayList(picItem));
                        completePic.setPicJson(picJson);
                        orderItemCompleteDao.insert(completePic);
                    } else {
                        completePic.setUpdateBy(user);
                        completePic.setUpdateDate(now);
                        completePic.setUploadQty(picItemMap.size());
                        String picJson = OrderUtils.toProductCompletePicItemsJson(Lists.newArrayList(picItemMap.values()));
                        completePic.setPicJson(picJson);
                        orderItemCompleteDao.update(completePic);
                    }
                    if (isAddOperation) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("orderId", orderId);
                        params.put("quarter", quarter);
                        params.put("finishPhotoQty", 1);//+1
                        orderDao.updateCondition(params);
                        updateOrderPicCache(orderId, 1);
                    }
                    restPicItem = new RestUploadProductCompletePic();
                    restPicItem.setUniqueId(completePic.getId());
                    restPicItem.setPictureCode(picItem.getPictureCode());
                    restPicItem.setUrl(OrderPicUtils.getOrderPicHostDir() + picFileName);
                }

            } catch (Exception e) {
                log.error("[AppOrderService.uploadOrderPic] orderId:{} userId: {}", orderId, user.getId(), e.getLocalizedMessage());
            }
        }
        return restPicItem;
    }


    /**
     * 删除图片
     */
    @Transactional(readOnly = false)
    public void deletePic(HttpServletRequest request, Long orderId, String quarter, Long uniqueId, String pictureCode, User user) {
        if (uniqueId != null && uniqueId > 0 && StringUtils.isNotBlank(quarter) && StringUtils.isNotBlank(pictureCode)) {
            try {
                OrderItemComplete completePic = orderItemCompleteDao.getById(uniqueId, quarter);
                if (completePic != null) {
                    completePic.setItemList(OrderUtils.fromProductCompletePicItemsJson(completePic.getPicJson()));
                    List<ProductCompletePicItem> picItemList = Lists.newArrayList();
                    boolean isExists = false;
                    List<String> picFilePaths = Lists.newArrayList();
                    for (ProductCompletePicItem item : completePic.getItemList()) {
                        if (!pictureCode.equals(item.getPictureCode())) {
                            picItemList.add(item);
                        } else {
                            isExists = true;
                            picFilePaths.add(item.getUrl());
                        }
                    }
                    completePic.setUpdateBy(user);
                    completePic.setUpdateDate(new Date());
                    if (isExists) {
                        if (!picItemList.isEmpty()) {
                            String picJson = OrderUtils.toProductCompletePicItemsJson(Lists.newArrayList(picItemList));
                            completePic.setPicJson(picJson);
                            completePic.setUploadQty(picItemList.size());
                            orderItemCompleteDao.update(completePic);
                        } else {
                            orderItemCompleteDao.delete(completePic);
                        }
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("orderId", orderId);
                        params.put("quarter", quarter);
                        params.put("finishPhotoQty", -1);
                        orderDao.updateCondition(params);
                        updateOrderPicCache(orderId, -1);
                        for (String picPath : picFilePaths) {
                            OrderPicUtils.deleteFile(OrderPicUtils.getOrderPicMasterDir(request) + picPath);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("图片删除失败");
            }
        }
    }

    /**
     * 更新缓存
     */
    private void updateOrderPicCache(Long orderId, Integer picQty) {
        if (orderId != null && picQty != null && picQty != 0) {
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(orderId)
                    .incrFinishPhotoQty(picQty.longValue());
            OrderCacheUtils.update(builder.build());
        }
    }

    //计算图片数量
    public Integer getProductQty(List<OrderItem> items, Long productId) {
        int productQty = 0;
        if (items != null && !items.isEmpty()) {
            List<Long> productIds = items.stream().map(i -> i.getProduct().getId()).collect(Collectors.toList());
            Map<Long, Product> productMap = productService.getProductMap(productIds);
            Product product = null;
            for (OrderItem item : items) {
                product = productMap.get(item.getProductId());
                if (product != null) {
                    if (product.getSetFlag() == 1) {
                        final String[] setIds = product.getProductIds().split(",");
                        for (String id : setIds) {
                            if (productId.equals(Long.valueOf(id))) {
                                productQty = productQty + item.getQty();
                                break;
                            }
                        }
                    } else {
                        if (productId.equals(item.getProductId())) {
                            productQty = productQty + item.getQty();
                        }
                    }
                }
            }
        }

        return productQty;
    }

    /**
     * 更新产品条码
     */
    public void updateProductSN(Long uniqueId, String productSN, User user) {
        if (uniqueId != null && uniqueId > 0 && StringUtils.isNotBlank(productSN) && user != null && user.getId() != null) {
            OrderItemComplete entity = new OrderItemComplete();
            entity.setId(uniqueId);
            entity.setUnitBarcode(productSN);
            entity.setUpdateBy(user);
            entity.setUpdateDate(new Date());
            orderItemCompleteDao.updateBarCode(entity);
        }
    }

    //endregion 工单图片

    //region 辅材与服务项目

    /**
     * 检查工单是否使用了辅材或服务项目
     */
    public ThreeTuple<Boolean, Double, Double> getQtyAndTotalChargeOfOrderAuxiliaryMaterial(Long orderId, String quarter) {
        ThreeTuple<Boolean, Double, Double> result = new ThreeTuple<>();
        AuxiliaryMaterialMaster entity = orderAuxiliaryMaterialService.getAuxiliaryMaterialMaster(orderId, quarter);
        if (entity != null) {
            result.setAElement(true);
            result.setBElement(entity.getTotal());
            result.setCElement(entity.getActualTotalCharge());
        } else {
            result.setAElement(false);
            result.setBElement(0.0);
            result.setCElement(0.0);
        }
        return result;
    }

    //endregion 辅材与服务项目


    //region 辅材v2.0

    /**
     * 获取产品可设置的辅材与服务项目
     */
    public RestProductAuxiliaryMaterials getProductAuxiliaryMaterialsV2(List<Long> productIds) {
        List<RestProductAuxiliaryMaterials.Product> products = Lists.newArrayList();
        Map<Long, List<MDAuxiliaryMaterialItem>> map = AuxiliaryMaterialUtils.getAuxiliaryMaterialCategoryAndItemMap(productIds);
        if (!map.isEmpty()) {
            Map<Long, Product> productMap = productService.getProductMap(productIds);
            RestProductAuxiliaryMaterials.Product newObj;
            RestProductAuxiliaryMaterials.Category category;
            RestProductAuxiliaryMaterials.Item item;
            Map<Long, RestProductAuxiliaryMaterials.Category> categoryMap;
            Long categoryId;
            Product product;
            for (Map.Entry<Long, List<MDAuxiliaryMaterialItem>> entry : map.entrySet()) {
                product = productMap.get(entry.getKey());
                if (product != null) {
                    newObj = new RestProductAuxiliaryMaterials.Product();
                    newObj.setProductId(product.getId());
                    newObj.setProductName(product.getName());
                    categoryMap = Maps.newHashMap();
                    for (MDAuxiliaryMaterialItem innerItem : entry.getValue()) {
                        categoryId = innerItem.getCategory().getId();
                        if (categoryMap.containsKey(categoryId)) {
                            category = categoryMap.get(categoryId);
                        } else {
                            category = new RestProductAuxiliaryMaterials.Category();
                            category.setCategoryId(categoryId);
                            category.setCategoryName(innerItem.getCategory().getName());
                            categoryMap.put(categoryId, category);
                        }

                        item = new RestProductAuxiliaryMaterials.Item();
                        item.setItemId(innerItem.getId());
                        item.setItemName(innerItem.getName());
                        item.setCategoryId(category.getCategoryId());
                        item.setProductId(newObj.getProductId());
                        item.setCharge(innerItem.getPrice());
                        item.setCustomPriceFlag(innerItem.getType());
                        item.setUnit(innerItem.getUnit());
                        category.getItems().add(item);
                    }
                    newObj.setCategories(Lists.newArrayList(categoryMap.values()));
                    products.add(newObj);
                }
            }
        }
        RestProductAuxiliaryMaterials result = new RestProductAuxiliaryMaterials();
        result.setProducts(products);
        return result;
    }

    /**
     * 保存工单的辅材与服务项目
     */
    @Transactional()
    public void saveOrderAuxiliaryMaterialsV2(RestSaveOrderAuxiliaryMaterialsRequest params, User operator) {
        List<AuxiliaryMaterial> newItemList = Lists.newArrayList();
        int totalQty = 0;
        double totalCharge = 0.0;
        Date now = new Date();
        if (params.getProducts() != null && !params.getProducts().isEmpty()) {
            long productQty = params.getProducts().stream().distinct().count();
            if (params.getProducts().size() > productQty) {
                throw new OrderException("参数格式不匹配：产品不允许重复");
            }
            List<Long> productIds = params.getProducts().stream().map(RestSaveOrderAuxiliaryMaterialsRequest.Product::getProductId).collect(Collectors.toList());
            Map<Long, MDAuxiliaryMaterialItem> serviceFeeItemMap = AuxiliaryMaterialUtils.getAuxiliaryMaterialItemMap(productIds);
            AuxiliaryMaterial newItem;
            for (RestSaveOrderAuxiliaryMaterialsRequest.Product product : params.getProducts()) {
                for (RestSaveOrderAuxiliaryMaterialsRequest.Item material : product.getItems()) {
                    MDAuxiliaryMaterialItem feeItem = serviceFeeItemMap.get(material.getItemId());
                    if (feeItem != null) {
                        newItem = new AuxiliaryMaterial();
                        if (feeItem.getType() == MDAuxiliaryMaterialItem.AUXILIARY_MATERIAL_ITEM_TYPE_INPUT_CHARGE) { //非固定价格
                            newItem.setQty(1);
                            newItem.setSubtotal(material.getCharge());
                            totalQty += 1;
                            totalCharge += newItem.getSubtotal();
                        } else {
                            newItem.setQty(material.getQty());
                            newItem.setSubtotal(feeItem.getPrice() * material.getQty());
                            totalQty += newItem.getQty();
                            totalCharge += newItem.getSubtotal();
                        }
                        newItem.setMaterial(feeItem);
                        newItem.setCategory(feeItem.getCategory());
                        newItem.setUnit(feeItem.getUnit());
                        newItem.setProduct(new Product(product.getProductId()));
                        newItem.setOrderId(params.getOrderId());
                        newItem.setQuarter(params.getQuarter());
                        newItem.setCreateBy(operator);
                        newItem.setCreateDate(now);
                        newItem.setUpdateBy(operator);
                        newItem.setUpdateDate(now);
                        newItemList.add(newItem);
                    }
                }
            }
        }
        AuxiliaryMaterialMaster auxiliaryMaterialMaster = new AuxiliaryMaterialMaster();
        auxiliaryMaterialMaster.setOrderId(params.getOrderId());
        auxiliaryMaterialMaster.setQuarter(params.getQuarter());
        auxiliaryMaterialMaster.setQty(totalQty);
        auxiliaryMaterialMaster.setTotal(totalCharge);
        auxiliaryMaterialMaster.setActualTotalCharge(params.getActualTotalCharge() == null ? 0 : params.getActualTotalCharge());
        auxiliaryMaterialMaster.setRemarks(StringUtils.toString(params.getRemarks()));
        auxiliaryMaterialMaster.setCreateBy(operator);
        auxiliaryMaterialMaster.setCreateDate(now);
        auxiliaryMaterialMaster.setUpdateBy(operator);
        auxiliaryMaterialMaster.setUpdateDate(now);
        auxiliaryMaterialMaster.setItems(newItemList);
        orderAuxiliaryMaterialService.updateOrderAuxiliaryMaterials(auxiliaryMaterialMaster);
    }

    /**
     * 保存工单的辅材与服务项目
     */
    @Transactional()
    public void saveOrderAuxiliaryMaterialsV3(RestSaveOrderAuxiliaryMaterialsRequest params, String filePath, User operator) {
        List<AuxiliaryMaterial> newItemList = Lists.newArrayList();
        int totalQty = 0;
        double totalCharge = 0.0;
        Date now = new Date();
        if (params.getProducts() != null && !params.getProducts().isEmpty()) {
            long productQty = params.getProducts().stream().distinct().count();
            if (params.getProducts().size() > productQty) {
                throw new OrderException("参数格式不匹配：产品不允许重复");
            }
            List<Long> productIds = params.getProducts().stream().map(RestSaveOrderAuxiliaryMaterialsRequest.Product::getProductId).collect(Collectors.toList());
            Map<Long, MDAuxiliaryMaterialItem> serviceFeeItemMap = AuxiliaryMaterialUtils.getAuxiliaryMaterialItemMap(productIds);
            AuxiliaryMaterial newItem;
            for (RestSaveOrderAuxiliaryMaterialsRequest.Product product : params.getProducts()) {
                for (RestSaveOrderAuxiliaryMaterialsRequest.Item material : product.getItems()) {
                    MDAuxiliaryMaterialItem feeItem = serviceFeeItemMap.get(material.getItemId());
                    if (feeItem != null) {
                        newItem = new AuxiliaryMaterial();
                        if (feeItem.getType() == MDAuxiliaryMaterialItem.AUXILIARY_MATERIAL_ITEM_TYPE_INPUT_CHARGE) { //非固定价格
                            newItem.setQty(1);
                            newItem.setSubtotal(material.getCharge());
                            totalQty += 1;
                            totalCharge += newItem.getSubtotal();
                        } else {
                            newItem.setQty(material.getQty());
                            newItem.setSubtotal(feeItem.getPrice() * material.getQty());
                            totalQty += newItem.getQty();
                            totalCharge += newItem.getSubtotal();
                        }
                        newItem.setMaterial(feeItem);
                        newItem.setCategory(feeItem.getCategory());
                        newItem.setUnit(feeItem.getUnit());
                        newItem.setProduct(new Product(product.getProductId()));
                        newItem.setOrderId(params.getOrderId());
                        newItem.setQuarter(params.getQuarter());
                        newItem.setCreateBy(operator);
                        newItem.setCreateDate(now);
                        newItem.setUpdateBy(operator);
                        newItem.setUpdateDate(now);
                        newItemList.add(newItem);
                    }
                }
            }
        }
        AuxiliaryMaterialMaster auxiliaryMaterialMaster = new AuxiliaryMaterialMaster();
        auxiliaryMaterialMaster.setOrderId(params.getOrderId());
        auxiliaryMaterialMaster.setQuarter(params.getQuarter());
        auxiliaryMaterialMaster.setQty(totalQty);
        auxiliaryMaterialMaster.setTotal(totalCharge);
        auxiliaryMaterialMaster.setActualTotalCharge(params.getActualTotalCharge() == null ? 0 : params.getActualTotalCharge());
        auxiliaryMaterialMaster.setRemarks(StringUtils.toString(params.getRemarks()));
        auxiliaryMaterialMaster.setCreateBy(operator);
        auxiliaryMaterialMaster.setCreateDate(now);
        auxiliaryMaterialMaster.setUpdateBy(operator);
        auxiliaryMaterialMaster.setUpdateDate(now);
        auxiliaryMaterialMaster.setItems(newItemList);
        auxiliaryMaterialMaster.setFilePath(filePath);
        auxiliaryMaterialMaster.setUpdatePhoto(params.isUpdatePhoto());
        orderAuxiliaryMaterialService.updateOrderAuxiliaryMaterials(auxiliaryMaterialMaster);
    }



    /**
     * 获取工单已配置的辅材与服务项目
     */
    public RestOrderAuxiliaryMaterials getOrderAuxiliaryMaterialsV2(Order order) {
        List<RestOrderAuxiliaryMaterials.Product> list = Lists.newArrayList();
        AuxiliaryMaterialMaster materialMaster = orderAuxiliaryMaterialService.getOrderAuxiliaryMaterialsV2(order.getId(), order.getQuarter());
        if (materialMaster != null && !materialMaster.getItems().isEmpty()) {
            Map<Long, List<AuxiliaryMaterial>> materialMap = Maps.newHashMap();
            Long key;
            for (AuxiliaryMaterial item : materialMaster.getItems()) {
                key = item.getProduct().getId();
                if (materialMap.containsKey(key)) {
                    materialMap.get(key).add(item);
                } else {
                    materialMap.put(key, Lists.newArrayList(item));
                }
            }
            Map<Long, Map<Long, List<AuxiliaryMaterial>>> materialMapMap = Maps.newHashMap();
            for (Map.Entry<Long, List<AuxiliaryMaterial>> item : materialMap.entrySet()) {
                Map<Long, List<AuxiliaryMaterial>> tempMap = Maps.newHashMap();
                for (AuxiliaryMaterial innerItem : item.getValue()) {
                    key = innerItem.getCategory().getId();
                    if (tempMap.containsKey(key)) {
                        tempMap.get(key).add(innerItem);
                    } else {
                        tempMap.put(key, Lists.newArrayList(innerItem));
                    }
                }
                materialMapMap.put(item.getKey(), tempMap);
            }

            RestOrderAuxiliaryMaterials.Product restProduct;
            RestOrderAuxiliaryMaterials.Category restCategory;
            RestOrderAuxiliaryMaterials.Item restItem;
            AuxiliaryMaterial auxiliaryMaterial = null;
            for (Map.Entry<Long, Map<Long, List<AuxiliaryMaterial>>> item : materialMapMap.entrySet()) {
                restProduct = new RestOrderAuxiliaryMaterials.Product();
                for (Map.Entry<Long, List<AuxiliaryMaterial>> innerItem : item.getValue().entrySet()) {
                    restCategory = new RestOrderAuxiliaryMaterials.Category();
                    for (AuxiliaryMaterial material : innerItem.getValue()) {
                        auxiliaryMaterial = material;
                        restItem = new RestOrderAuxiliaryMaterials.Item();
                        restItem.setItemId(material.getMaterial().getId());
                        restItem.setItemName(material.getMaterial().getName());
                        restItem.setCategoryId(material.getCategory().getId());
                        restItem.setProductId(material.getProduct().getId());
                        restItem.setQty(material.getQty());
                        restItem.setCharge(material.getMaterial().getPrice());
                        restItem.setCustomPriceFlag(material.getMaterial().getType());
                        restItem.setUnit(material.getUnit());
                        restItem.setTotalCharge(material.getSubtotal());
                        restCategory.getItems().add(restItem);
                    }
                    if (auxiliaryMaterial != null) {
                        restCategory.setCategoryId(auxiliaryMaterial.getCategory().getId());
                        restCategory.setCategoryName(auxiliaryMaterial.getCategory().getName());
                    }
                    restProduct.getCategories().add(restCategory);
                }
                if (auxiliaryMaterial != null) {
                    restProduct.setProductId(auxiliaryMaterial.getProduct().getId());
                    restProduct.setProductName(auxiliaryMaterial.getProduct().getName());
                }
                list.add(restProduct);
            }
        }
        RestOrderAuxiliaryMaterials result = new RestOrderAuxiliaryMaterials();
        result.setProducts(list);
        if (materialMaster != null) {
            result.setTotalQty(materialMaster.getQty());
            result.setTotalCharge(materialMaster.getTotal());
            result.setFilePath(materialMaster.getFilePath());
            result.setRemarks(StringUtils.toString(materialMaster.getRemarks()));
            result.setActualTotalCharge(materialMaster.getActualTotalCharge());
        }
        return result;
    }




    //endregion 辅材v2.0

    //region 催单

    /**
     * 读取催单信息
     */
    public void loadWaitReplyReminderInfo(List<RestOrder> orderList) {
        List<ReminderOrderModel> orderModelList = Lists.newArrayList();
        orderList.stream().forEach(t -> {
            if (t.getReminderFlag() == 1) {
                orderModelList.add(ReminderOrderModel.builder()
                        .orderId(t.getOrderId())
                        .quarter(t.getQuarter())
                        .build()
                );
            }
        });
        if (!orderModelList.isEmpty()) {
            BulkRereminderCheckModel bulkRereminderCheckModel = new BulkRereminderCheckModel(orderModelList);
            Map<Long, ReminderTimeLinessModel> reminderModels = reminderService.bulkGetReminderTimeLinessByOrders(bulkRereminderCheckModel);
            if (reminderModels != null && reminderModels.size() > 0) {
                RestOrder order;
                ReminderTimeLinessModel model;
                for (int i = 0, size = orderList.size(); i < size; i++) {
                    order = orderList.get(i);
                    if (reminderModels.containsKey(order.getOrderId())) {
                        model = reminderModels.get(order.getOrderId());
                        if (model != null) {
                            order.setReminderItemNo(model.getItemNo());
                            order.setReminderTimeoutAt(model.getTimeoutAt());
                            //order.setReminderFlag(model.getStatus());
                        }
                    }
                }
            }
        }

    }

    //endregion 催单

    //region 辅材v4.0

    public RestOrderAuxiliaryMaterialsV4 getOrderAuxiliaryMaterialsV4(Order order) {
        RestOrderAuxiliaryMaterialsV4 restObj = null;
        AuxiliaryMaterialMaster materialMaster = orderAuxiliaryMaterialService.getOrderAuxiliaryMaterialsV4(order.getId(), order.getQuarter());
        if (materialMaster != null) {
            restObj = new RestOrderAuxiliaryMaterialsV4();
            restObj.setActualTotalCharge(materialMaster.getActualTotalCharge());
            if (StringUtils.isNotBlank(materialMaster.getFilePath())) {
                restObj.setFilePath(OrderPicUtils.getOrderPicHostDir() + materialMaster.getFilePath());
            }
        }
        return restObj;
    }

    @Transactional()
    public void saveOrderAuxiliaryMaterialsV4(RestSaveOrderAuxiliaryMaterialsRequestV4 params, String filePath, User operator) {
        List<AuxiliaryMaterial> newItemList = Lists.newArrayList();
        Date now = new Date();
        AuxiliaryMaterialMaster master = new AuxiliaryMaterialMaster();
        master.setOrderId(params.getOrderId());
        master.setQuarter(params.getQuarter());
        master.setFormType(AuxiliaryMaterialMaster.FormTypeEnum.NO_MATERIAL_ITEM.getValue());
        master.setQty(0);
        master.setTotal(0.0);
        master.setActualTotalCharge(params.getActualTotalCharge() == null ? 0 : params.getActualTotalCharge());
        master.setFilePath(filePath);
        master.setUpdatePhoto(params.isUpdatePhoto());
        master.setCreateBy(operator);
        master.setCreateDate(now);
        master.setUpdateBy(operator);
        master.setUpdateDate(now);
        orderAuxiliaryMaterialService.updateOrderAuxiliaryMaterialsV4(master);
    }

    //endregion 辅材v4.0
}
