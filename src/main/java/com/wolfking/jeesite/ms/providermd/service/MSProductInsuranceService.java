package com.wolfking.jeesite.ms.providermd.service;

import com.wolfking.jeesite.modules.md.entity.InsurancePrice;
import com.wolfking.jeesite.ms.providermd.feign.MSProductInsuranceFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MSProductInsuranceService {
    @Autowired
    private MSProductInsuranceFeign msProductInsuranceFeign;

    public List<InsurancePrice> findAllList() {
        return MDUtils.findAllList(InsurancePrice.class, msProductInsuranceFeign::findAllList);
    }
}
