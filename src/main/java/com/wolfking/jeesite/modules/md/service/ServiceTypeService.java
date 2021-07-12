/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.ms.providermd.service.MSServiceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 服务类型
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ServiceTypeService extends LongIDBaseService {

    @Autowired
    private MSServiceTypeService msServiceTypeService;

    /**
     * 读取所有服务类型
     * 优先缓存读取
     *
     * @return
     */
    public List<ServiceType> findAllList() {
        List<ServiceType> serviceTypesList = msServiceTypeService.findAllList();
        if (serviceTypesList != null && serviceTypesList.size() > 0) {
            return serviceTypesList.stream().sorted(Comparator.comparing(ServiceType::getSort).thenComparing(ServiceType::getName))
                    .collect(Collectors.toList());
        }
        return serviceTypesList;
    }

    /**
     * 获取所有服务类型
     *
     * @return
     */
    public Map<Long, ServiceType> getAllServiceTypeMap() {
        List<ServiceType> serviceTypeList = findAllList();
        if (CollectionUtils.isEmpty(serviceTypeList)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        Map<Long, ServiceType> serviceTypeMap = Maps.newHashMap();
        for (ServiceType item : serviceTypeList) {
            serviceTypeMap.put(item.getId(), item);
        }
        return serviceTypeMap;
    }

    /**
     * 获取所有的服务类型
     *
     * @return map<Long, String> key为id,value为服务类型名称</>
     */
    public Map<Long, String> findAllIdsAndNames() {
        return msServiceTypeService.findAllIdsAndNames();
    }


    /**
     * 获取所有的服务类型
     *
     * @return map<Long, String> key为id,value为服务类型编码(code)</>
     */
    public Map<Long, String> findIdsAndCodes() {
        return msServiceTypeService.findIdsAndCodes();
    }


    /**
     * 获取所有的服务类型
     *
     * @return list  对象只有id跟服务类型名称有值
     * @Parm id, name 为MDServiceType 的属性名
     */
    public List<ServiceType> findAllListIdsAndNames() {
        List<String> list = Lists.newArrayList();
        list.add("id");
        list.add("name");
        return msServiceTypeService.findAllListWithCondition(list);
    }


    /**
     * 获取所有的服务类型
     *
     * @return 对象只返回id 和服务名称,code,warrantyStatus
     * @Parm id, name, code, warrantyStatus 为MDServiceType 属性名
     */
    public List<ServiceType> findAllListIdsAndNamesAndCodes() {
        List<String> list = Lists.newArrayList();
        list.add("id");
        list.add("name");
        list.add("code");
        list.add("warrantyStatus");
        return msServiceTypeService.findAllListWithCondition(list);
    }

    /**
     * 按订单类型读取服务类型列表
     */
    public List<ServiceType> findListOfOrderType(Integer orderType) {
        if (orderType == null || orderType < 0) {
            return Lists.newArrayList();
        }
        List<ServiceType> serviceTypes = findAllList();
        if (!org.springframework.util.CollectionUtils.isEmpty(serviceTypes)) {
            serviceTypes = serviceTypes.stream().filter(t -> t.getOrderServiceType().equals(orderType) && t.getDelFlag() == 0).collect(Collectors.toList());
        }
        return serviceTypes == null ? Lists.newArrayList() : serviceTypes;
    }
}
