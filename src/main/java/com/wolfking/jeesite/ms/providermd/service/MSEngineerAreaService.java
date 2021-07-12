package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineerArea;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerAreaFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSEngineerAreaService {
    @Autowired
    private MSEngineerAreaFeign msEngineerAreaFeign;

    /**
     * 通过安维id获取安维对应的区域id
     *
     * @param engineerId
     * @return
     */
    public List<Long> findEngineerAreaIds(Long engineerId) {
        MSResponse<List<Long>> msResponse = msEngineerAreaFeign.findEngineerAreaIds(engineerId);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return Lists.newArrayList();
    }

    /**
     * 通过安维id获取安维区域id列表
     *
     * @param engineerIds
     * @return
     */
    public List<MDEngineerArea> findEngineerAreasWithIds(List<Long> engineerIds) {
        MSResponse<List<MDEngineerArea>> msResponse = msEngineerAreaFeign.findEngineerAreasWithIds(engineerIds);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return Lists.newArrayList();
    }


}
