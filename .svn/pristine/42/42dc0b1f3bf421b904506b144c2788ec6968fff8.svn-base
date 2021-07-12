package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.ms.providermd.service.MSUrgentLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UrgentLevelService extends LongIDBaseService {

    @Autowired
    private MSUrgentLevelService msUrgentLevelService;

    /**
     * 加载所有加急等级，当缓存未命中则从数据库装载至缓存
     */
    public List<UrgentLevel> findAllList() {
        //调用微服务
        List<UrgentLevel> urgentLevels = msUrgentLevelService.findAllList();
        return urgentLevels;
    }

    /**
     * 加载所有加急等级，当缓存未命中则从数据库装载至缓存
     *
     * @return
     */
    public Map<Long, UrgentLevel> findAllMap() {
        List<UrgentLevel> list = findAllList();
        if (list == null || list.size() == 0) {
            return Maps.newHashMap();
        }
        return list.stream().collect(Collectors.toMap(
                e -> e.getId(),
                e -> e
        ));
    }

    /**
     * 获得加急等级信息
     *
     * @param id
     * @return
     */
    public UrgentLevel getFromCache(long id) {
        UrgentLevel urgentLevel = msUrgentLevelService.getFromCache(id);
        return urgentLevel;
    }
}
