package com.wolfking.jeesite.ms.providermd.service;

import com.wolfking.jeesite.modules.md.entity.UrgentCustomer;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerUrgentFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MSCustomerUrgentService {
    @Autowired
    private MSCustomerUrgentFeign msCustomerUrgentFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 根据customerId，areaId获取加急列表
     * @param urgentCustomer
     * @return
     */
    public List<UrgentCustomer> findListByCustomerId(UrgentCustomer urgentCustomer) {
        return MDUtils.findListByCustomCondition(urgentCustomer.getCustomer().getId(), UrgentCustomer.class, msCustomerUrgentFeign::findListByCustomerId);
    }

}
