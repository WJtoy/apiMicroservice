package com.wolfking.jeesite.ms.providermd.service;

import com.wolfking.jeesite.modules.md.entity.CustomerAccountProfile;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerAccountProfileFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MSCustomerAccountProfileService {
    @Autowired
    private MSCustomerAccountProfileFeign msCustomerAccountProfileFeign;

    /**
     * 根据id获取客户账号信息
     * @param id
     * @return
     * id
     * customerId
     * orderApproveFlag
     * createBy
     * createDate
     * updateBy
     * updateDate
     * remarks
     * delFlag
     */
    public CustomerAccountProfile getById(Long id) {
        /*
        MSResponse<MDCustomerAccountProfile> msResponse = msCustomerAccountProfileFeign.getById(id);
        CustomerAccountProfile customerAccountProfile = null;
        if (MSResponse.isSuccess(msResponse)) {
            log.warn("CustomerAccountProfile微服务getById方法返回:{}",msResponse.getData());
            customerAccountProfile = mapper.map(msResponse.getData(), CustomerAccountProfile.class);
        }
        return customerAccountProfile;
        */

        return MDUtils.getById(id, CustomerAccountProfile.class, msCustomerAccountProfileFeign::getById);
    }
}
