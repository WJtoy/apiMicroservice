package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.ms.providermd.feign.MSUrgentLevelFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSUrgentLevelService {

    @Autowired
    private MSUrgentLevelFeign msUrgentLevelFeign;

    /**
     * 根据id从缓存获取加急等级
     *
     * @param id
     * @return
     */
    public UrgentLevel getFromCache(Long id) {
        return MDUtils.getById(id, UrgentLevel.class, msUrgentLevelFeign::getFromCache);
    }

    /**
     * 缓存获取所有数据
     *
     * @return
     */
    public List<UrgentLevel> findAllList() {
        List<UrgentLevel> list = MDUtils.findAllList(UrgentLevel.class, msUrgentLevelFeign::findAllList);
        if (list != null && list.size() > 0) {
            return list.stream().sorted(Comparator.comparing(UrgentLevel::getId)).collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }

}
