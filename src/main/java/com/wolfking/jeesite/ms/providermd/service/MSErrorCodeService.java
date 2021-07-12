package com.wolfking.jeesite.ms.providermd.service;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.dto.MDErrorCodeDto;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorCodeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MSErrorCodeService {
    @Autowired
    private MSErrorCodeFeign msErrorCodeFeign;

    public List<MDErrorCode> findListByProductAndErrorType(Long errorTypeId, Long productId) {
        return MDUtils.findListUnnecessaryConvertType(() -> msErrorCodeFeign.findListByProductAndErrorType(errorTypeId, productId));
    }

    /**
     * 按产品id + id读取故障现象
     */
    public MDErrorCode getByProductIdAndId(Long productId, Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        MSResponse<List<MDErrorCodeDto>> msResponse = msErrorCodeFeign.findListByProductId(id, productId);
        if (MSResponse.isSuccess(msResponse)) {
            List<MDErrorCodeDto> list = msResponse.getData();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        } else {
            return null;
        }
    }
}
