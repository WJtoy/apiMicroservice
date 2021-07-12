package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.ms.providermd.feign.MSTimelinessLevelFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSTimelinessLevelService {

    @Autowired
    private MSTimelinessLevelFeign msTimelinessLevelFeign;

    /**
     * 缓存获取所有数据
     *
     * @return
     */
    public List<TimelinessLevel> findAllList() {
        List<TimelinessLevel> list = MDUtils.findAllList(TimelinessLevel.class, msTimelinessLevelFeign::findAllList);
        if (list != null && list.size() > 0) {
            return list.stream().sorted(Comparator.comparing(TimelinessLevel::getId)).collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }
}
