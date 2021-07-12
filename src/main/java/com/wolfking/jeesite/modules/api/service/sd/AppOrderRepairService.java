package com.wolfking.jeesite.modules.api.service.sd;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.api.entity.common.Pair;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * APP维修故障
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppOrderRepairService extends LongIDBaseService {

    private static final int REPAIR_SERVICE_TYPE = 2;

    @Autowired
    private OrderCacheReadService orderCacheReadService;
    @Autowired
    private ServiceTypeService serviceTypeService;

    /**
     * 获取维修的服务项目列表
     */
    public List<Pair<Long, String>> getRepairServiceTypes(Long orderId, String quarter) {
        Order order = orderCacheReadService.getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.HEAD, true, false);
        if (order == null) {
            throw new OrderException("读取工单信息失败");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderException("读取工单下单项目失败");
        }
        Map<Long, ServiceType> serviceTypeMap = serviceTypeService.getAllServiceTypeMap();
        boolean isOot = false;
        ServiceType serviceType;
        for (OrderItem item : order.getItems()) {
            serviceType = serviceTypeMap.get(item.getServiceType().getId());
            if (serviceType != null && serviceType.getWarrantyStatus() != null
                    && ServiceType.WARRANTY_STATUS_OOT.equals(serviceType.getWarrantyStatus().getValue())) {
                isOot = true;
                break;
            }
        }
        List<ServiceType> serviceTypes = Lists.newArrayList();
        for (ServiceType item : serviceTypeMap.values()) {
            if (item.getOrderServiceType() == REPAIR_SERVICE_TYPE && item.getDelFlag() == 0) {
                if (isOot) {
                    if (ServiceType.WARRANTY_STATUS_OOT.equals(item.getWarrantyStatus().getValue())) {
                        serviceTypes.add(item);
                    }
                } else {
                    serviceTypes.add(item);
                }
            }
        }
        List<Pair<Long, String>> result = Lists.newArrayList();
        serviceTypes = serviceTypes.stream().sorted(Comparator.comparing(ServiceType::getSort).thenComparing(ServiceType::getName))
                .collect(Collectors.toList());
        for (ServiceType item : serviceTypes) {
            result.add(new Pair<>(item.getId(), item.getName()));
        }
        return result;
    }


}
