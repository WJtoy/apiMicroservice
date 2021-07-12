package com.wolfking.jeesite.modules.sd.entity.mapper;

import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sd.entity.viewModel.ServicePointCrush;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

/**
 * ServicePointCrush <-> ServicePoint
 */
@Component
public class ServicePointToModellMapper extends CustomMapper<ServicePoint, ServicePointCrush> {

    @Override
    public void mapAtoB(ServicePoint a, ServicePointCrush b, MappingContext context) {
        b.setId(a.getId());
        b.setServicePointNo(a.getServicePointNo());
        b.setName(a.getName());
        b.setPrimary(a.getPrimary());
        b.setContactInfo1(a.getContactInfo1());
        b.setAddress(a.getAddress());
        b.setOrderCount(a.getOrderCount());
        b.setBreakCount(a.getBreakCount());
        b.setPlanCount(a.getPlanCount());
        b.setFinance(a.getFinance());
        b.setRemarks(a.getRemarks());
        b.setPaymentType(a.getFinance().getPaymentType().getLabel());
        b.setAppFlag(a.getPrimary().getAppFlag() == 1 ? "是" : "否");
        b.setMaster(a.getPrimary().getName());
        b.setPlanRemarks(a.getPlanRemark());
    }

    @Override
    public void mapBtoA(ServicePointCrush b, ServicePoint a, MappingContext context) {

    }
}
