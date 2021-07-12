package com.wolfking.jeesite.modules.md.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.ms.providermd.service.MSTimelinessLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TimelinessLevelService extends LongIDBaseService {

    @Autowired
    private MSTimelinessLevelService msTimelinessLevelService;

    /**
     * 加载所有加急等级，当缓存未命中则从数据库装载至缓存
     *
     * @return
     */
    public List<TimelinessLevel> findAllList() {
        List<TimelinessLevel> listTimelinessLevel = msTimelinessLevelService.findAllList();
        return listTimelinessLevel;
        /*
        // mark on 2020-1-4
        if(listTimelinessLevel !=null && listTimelinessLevel.size()>0){
            return listTimelinessLevel;
        }
        boolean isExistsCache = redisUtils.exists(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_TIMELINESS_ALL);
        if (!isExistsCache){
            return loadDataFromDB2Cache();
        }
        List<TimelinessLevel> List = redisUtils.zRange(RedisConstant.RedisDBType.REDIS_MD_DB, RedisConstant.MD_TIMELINESS_ALL,0,-1, TimelinessLevel.class);
        return List;
        */
    }

}
