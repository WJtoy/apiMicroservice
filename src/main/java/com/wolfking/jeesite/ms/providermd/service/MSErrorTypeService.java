package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorTypeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MSErrorTypeService {
    @Autowired
    private MSErrorTypeFeign msErrorTypeFeign;

    public List<MDErrorType> findErrorTypesByProductId(Long productId) {
        MSResponse<List<MDErrorType>> msResponse = msErrorTypeFeign.findErrorTypesByProductId(productId);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return Lists.newArrayList();
    }

    /**
     * 按产品Id + id读取故障类型
     */
    public MDErrorType getByProductIdAndId(Long productId, Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        MSResponse<List<MDErrorType>> msResponse = msErrorTypeFeign.findListByProductId(productId, id);
        if (MSResponse.isSuccess(msResponse)) {
            List<MDErrorType> list = msResponse.getData();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        } else {
            return null;
        }
    }
}
