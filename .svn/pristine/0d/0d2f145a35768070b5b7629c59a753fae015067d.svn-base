package com.wolfking.jeesite.ms.providermd.service;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.wolfking.jeesite.ms.providermd.feign.MSAppFeedbackTypeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSAppFeedbackTypeService {
    @Autowired
    private MSAppFeedbackTypeFeign msAppFeedbackTypeFeign;

    /**
     * 获取所有app反馈类型
     *
     * @return
     */
    public List<MDAppFeedbackType> findAllList() {
        return MDUtils.findListUnnecessaryConvertType(()-> msAppFeedbackTypeFeign.findAllList());
    }

    /**
     * 缓存命中根据id
     *
     * @param id
     * @return
     */
    public MDAppFeedbackType getByIdFromCache(Long id) {
        MSResponse<MDAppFeedbackType> msResponse = msAppFeedbackTypeFeign.getByIdFromCache(id);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return null;
    }

}
