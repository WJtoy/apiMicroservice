/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.sd.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.md.service.CustomerProductCompletePicService;
import com.wolfking.jeesite.modules.md.service.ProductCompletePicService;
import com.wolfking.jeesite.modules.md.utils.ProductCompletedPicItemAdapter;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 订单工具类
 *
 * @author Ryan Lu
 * @version 2013-5-29
 */
public class OrderUtils {

    private static ProductCompletePicService productCompletePicService = SpringContextHolder.getBean(ProductCompletePicService.class);
    private static CustomerProductCompletePicService customerProductCompletePicService = SpringContextHolder.getBean(CustomerProductCompletePicService.class);

    public static final String SYNC_CUSTOMER_CHARGE_DICT="customer_auto_remotecharge_category";
    public static final String LIMIT_REMOTECHARGE_CATEGORY_DICT = "limit_remotecharge_category";

    public static final Long ORDER_EXPIRED = 93600L;//2*24*60*60l;//未完成订单缓存2天
    public static final Long ORDER_LOCK_EXPIRED = 60L;//锁定时间

    //读取订单内容分级
    public enum OrderDataLevel {
        //订单单头
        HEAD,
        //查询条件
        CONDITION,
        //财务
        FEE,
        //状态
        STATUS,
        //实际服务
        DETAIL,
        //日志
        LOG
    }

    /**
     * 自动同步加的应收费用
     */
    public enum SyncCustomerCharge {
        //远程费
        TRAVEL,
        //其他费用
        OTHER
    }

    /**
     * 完工项目类型
     */
    public enum OrderTypeEnum {
        TEST(0,"检测"),
        INSTALL(1, "安装"),
        REPAIRE(2, "维修"),
        BACK(3,"退货"),
        EXCHANGE(4,"换货");

        private int id;
        private String name;
        private Class<?> clazz;

        OrderTypeEnum(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

    private static Set<Integer> ORDER_PROCESS_LOG_STATUS_FLAG_SET_KEFU = Sets.newHashSet(
            OrderProcessLog.OPL_SF_NOT_CHANGE_STATUS,
            OrderProcessLog.OPL_SF_CHANGED_STATUS,
            OrderProcessLog.OPL_SF_PENDDING,
            OrderProcessLog.OPL_SF_TRACKING,
            OrderProcessLog.OPL_SF_PENDINGED);

    private static Set<Integer> ORDER_PROCESS_LOG_STATUS_FLAG_SET_CUSTOMER = Sets.newHashSet(
            OrderProcessLog.OPL_SF_CHANGED_STATUS,
            OrderProcessLog.OPL_SF_PENDDING,
            OrderProcessLog.OPL_SF_PENDINGED);

    private static Set<Integer> ORDER_PROCESS_LOG_CLOSE_FLAG_SET_SERVICE_POINT = Sets.newHashSet(0, 2);


    /**
     * 获得系统上线日期
     */
    public static Date getGoLiveDate() {
        Date goLiveDate = null;
        //from config file
//        String date = Global.getConfig("GoLiveDate");
        String date = ApiPropertiesUtils.getWeb().getGoLiveDate();
        if (StringUtils.isNotBlank(date)) {
            goLiveDate = DateUtils.parseDate(date);
        }
        if (goLiveDate != null) {
            return goLiveDate;
        }
        //from micoService
        Dict dict = MSDictUtils.getDictByValue("GoLiveDate", "GoLiveDate");
        if (dict == null) {
            return DateUtils.getDate(2018, 1, 1);
        }
        goLiveDate = DateUtils.parseDate(dict.getLabel());
        if (goLiveDate != null) {
            return goLiveDate;
        }
        return DateUtils.getDate(2018, 1, 1);
    }


    /**
     * 根据传入日期获得分片的开始日期和结束日期
     *
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @param aheadMonths 提前多少月(正数，为负代表向后延迟)
     * @param aheadDays   提前多少天(正数)
     * @return
     */
    public static Date[] getQuarterDates(Date startDate, Date endDate, Integer aheadMonths, Integer aheadDays) {
        Date quarterSDate = getGoLiveDate();
        if (startDate == null) {
            startDate = new Date();
        }
        if (aheadMonths != null && aheadMonths != 0) {
            startDate = DateUtils.addMonth(startDate, 0 - aheadMonths);
        }
        if (aheadMonths != null && aheadDays != 0) {
            startDate = DateUtils.addDays(startDate, 0 - aheadDays);
        }
        if (startDate != null && quarterSDate.getTime() < startDate.getTime()) {
            quarterSDate = startDate;
        }
        return new Date[]{quarterSDate, endDate == null ? new Date() : endDate};
    }

    /**
     * 筛选出派单给指定网点的时间区间内所产生的工单日志，另外还包括工单在完成状态(80)下所产生的日志
     */
    public static List<OrderProcessLog> filterServicePointOrderProcessLog(List<OrderProcessLog> logList, List<OrderPlan> orderPlanList, Long servicePointId) {
        List<OrderProcessLog> list = Lists.newArrayList();
        if (logList != null && logList.size() > 0
                && orderPlanList != null && orderPlanList.size() > 0
                && servicePointId != null && servicePointId > 0) {
            List<LongTwoTuple> timePointList = Lists.newArrayList();
            for (OrderPlan plan : orderPlanList) {
                if (plan.getCreateDate() != null) {
                    timePointList.add(new LongTwoTuple(plan.getServicePoint().getId(), plan.getCreateDate().getTime()));
                }
                if (plan.getUpdateDate() != null) {
                    timePointList.add(new LongTwoTuple(plan.getServicePoint().getId(), plan.getUpdateDate().getTime()));
                }
            }
            timePointList = timePointList.stream().sorted(Comparator.comparing(LongTwoTuple::getBElement)).collect(Collectors.toList());
            LongTwoTuple timePoint;
            LongThreeTuple timeRange;
            Long endTimestamp = DateUtils.addDays(new Date(), 1).getTime();
            Long cElement;
            List<LongThreeTuple> timeRangeList = Lists.newArrayList();
            if (timePointList.size() == 1) {
                timePoint = timePointList.get(0);
                timeRange = new LongThreeTuple();
                timeRange.setAElement(timePoint.getAElement());
                timeRange.setBElement(timePoint.getBElement());
                timeRange.setCElement(endTimestamp);
                timeRangeList.add(timeRange);
            } else {
                for (int i = 0; i < timePointList.size(); i++) {
                    timePoint = timePointList.get(i);
                    cElement = i + 1 < timePointList.size() ? timePointList.get(i + 1).getBElement() : endTimestamp;
                    timeRange = new LongThreeTuple();
                    timeRange.setAElement(timePoint.getAElement());
                    timeRange.setBElement(timePoint.getBElement());
                    timeRange.setCElement(cElement);
                    timeRangeList.add(timeRange);
                }
            }

            timeRangeList = timeRangeList.stream().filter(i -> i.getAElement().equals(servicePointId)).collect(Collectors.toList());
            List<OrderProcessLog> tempLogList;
            for (LongThreeTuple item : timeRangeList) {
                tempLogList = logList.stream()
                        .filter(i -> i.getCreateDate().getTime() >= item.getBElement()
                                && i.getCreateDate().getTime() < item.getCElement()
                                && !i.getStatusValue().equals(Order.ORDER_STATUS_COMPLETED))
                        .collect(Collectors.toList());
                list.addAll(tempLogList);
            }
            tempLogList = logList.stream().filter(i -> i.getStatusValue().equals(Order.ORDER_STATUS_COMPLETED)).collect(Collectors.toList());
            list.addAll(tempLogList);

            list = list.stream().sorted(Comparator.comparing(OrderProcessLog::getId).reversed()).collect(Collectors.toList());
        }

        return list;
    }


    /**
     * ProductCompletePicItem列表转成json字符串
     */
    public static String toProductCompletePicItemsJson(List<ProductCompletePicItem> picItems) {
        String json = null;
        if (picItems != null && picItems.size() > 0) {
            Gson gson = new GsonBuilder().registerTypeAdapter(ProductCompletePicItem.class, ProductCompletedPicItemAdapter.getInstance()).create();
            json = gson.toJson(picItems, new TypeToken<List<ProductCompletePicItem>>() {
            }.getType());
            /**
             * 因为myCat1.6不支持在json或text类型的字段中存储英文括号，故将所有的英文括号替换成中文括号.
             */
            json = json.replace("(", "（");
            json = json.replace(")", "）");
        }
        return json;
    }

    /**
     * json字符串转成ProductCompletePicItem列表
     */
    public static List<ProductCompletePicItem> fromProductCompletePicItemsJson(String json) {
        List<ProductCompletePicItem> picItems = null;
        if (StringUtils.isNotEmpty(json)) {
            picItems = GsonUtils.getInstance().getGson().fromJson(json, new TypeToken<List<ProductCompletePicItem>>() {
            }.getType());
        }
        return picItems != null ? picItems : Lists.newArrayList();
    }

    /**
     * 获取客户产品的完工图片规格
     */
    public static Map<Long, ProductCompletePic> getCustomerProductCompletePicMap(List<Long> productIds, Long customerId) {
        if (productIds.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<Long, ProductCompletePic> map = customerProductCompletePicService.getProductCompletePicMap(productIds, customerId);
        if (map == null || map.isEmpty()) {
            map = productCompletePicService.getProductCompletePicMap(productIds);
        } else {
            List<Long> ids = Lists.newArrayList();
            for (Long id : productIds) {
                if (!map.containsKey(id)) {
                    ids.add(id);
                }
            }
            if (!ids.isEmpty()) {
                Map<Long, ProductCompletePic> tempMap = productCompletePicService.getProductCompletePicMap(ids);
                if (!tempMap.isEmpty()) {
                    map.putAll(tempMap);
                }
            }
        }
        return map;
    }

    /**
     * 获取客户产品的完工图片规格
     */
    public static ProductCompletePic getCustomerProductCompletePic(Long prouctId, Long customerId) {
        if (prouctId == null || customerId == null) {
            return null;
        }
        ProductCompletePic productCompletePic = customerProductCompletePicService.getFromCache(prouctId, customerId);
        if (productCompletePic == null) {
            productCompletePic = productCompletePicService.getFromCache(prouctId);
        }
        return productCompletePic;
    }

    /**
     * 计算日志的可见性标志
     */
    public static int calcProcessLogVisibilityFlag(OrderProcessLog log) {
        Set<VisibilityFlagEnum> visibilityFlags = Sets.newHashSet();
        if (log != null) {
            Integer statusFlag = log.getStatusFlag();
            String remarks = log.getRemarks();
            Integer closeFlag = log.getCloseFlag();
            Integer statusValue = log.getStatusValue();
            if (statusFlag != null) {
                if (ORDER_PROCESS_LOG_STATUS_FLAG_SET_KEFU.contains(statusFlag)) {
                    visibilityFlags.add(VisibilityFlagEnum.KEFU);
                }
                if (ORDER_PROCESS_LOG_STATUS_FLAG_SET_CUSTOMER.contains(statusFlag)) {
                    visibilityFlags.add(VisibilityFlagEnum.CUSTOMER);
                } else if (statusFlag.intValue() == OrderProcessLog.OPL_SF_TRACKING && StringUtils.isNotBlank(remarks)) {
                    visibilityFlags.add(VisibilityFlagEnum.CUSTOMER);
                }
            }
            if (closeFlag != null && ORDER_PROCESS_LOG_CLOSE_FLAG_SET_SERVICE_POINT.contains(closeFlag)
                    && statusValue != null && statusValue >= Order.ORDER_STATUS_PLANNED && statusValue <= Order.ORDER_STATUS_CHARGED) {
                visibilityFlags.add(VisibilityFlagEnum.SERVICE_POINT);
            }
        }
        return VisibilityFlagEnum.or(visibilityFlags);
    }

}
