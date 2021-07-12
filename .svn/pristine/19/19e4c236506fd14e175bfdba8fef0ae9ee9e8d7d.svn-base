package com.wolfking.jeesite.modules.md.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.TimeLinessPrice;
import com.wolfking.jeesite.ms.providermd.service.MSProductTimeLinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jeff on 2017/4/24.
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TimeLinessPriceService extends LongIDBaseService {

    @Autowired
    private MSProductTimeLinessService msProductTimeLinessService;

    public List<TimeLinessPrice> findAllList() {
        return msProductTimeLinessService.findAllList();
    }


}
