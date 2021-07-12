package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServicePointArea;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointAreaFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSServicePointAreaService {
    @Autowired
    private MSServicePointAreaFeign msServicePointAreaFeign;

    /**
     * 查询网点负责的区域id清单
     *
     * @param servicePointId
     * @return
     */
    public List<Long> findAreaIds(Long servicePointId) {
        return MDUtils.findListUnnecessaryConvertType(()->msServicePointAreaFeign.findAreaIds(servicePointId));
    }

}
