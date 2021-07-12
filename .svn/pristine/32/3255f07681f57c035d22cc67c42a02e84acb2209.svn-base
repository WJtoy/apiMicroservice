package com.wolfking.jeesite.modules.md.service;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.service.BaseService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.md.dao.ServicePointDao;
import com.wolfking.jeesite.modules.md.entity.ServicePointFinance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ServicePointFinanceService extends BaseService {
    @Resource
    private ServicePointDao servicePointDao;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    private static final int SHARDING_COUNT = 40;  //分片数量

    public ServicePointFinance getFromCache(Long servicePointId) {
        if (servicePointId == null || servicePointId <= 0) {
            throw new RuntimeException("输入的网点Id错误!");
        }
        long modKey = servicePointId % SHARDING_COUNT;
        String key = String.format(RedisConstant.MD_SERVICEPOINT_FINANCE, modKey);
        ServicePointFinance servicePointFinance = redisUtilsLocal.hGet(RedisConstant.RedisDBType.REDIS_MD_DB, key, servicePointId.toString(), ServicePointFinance.class);
        if (servicePointFinance == null) {
            // 缓存中获取到数据为空，从DB中获取
            servicePointFinance = servicePointDao.getFinanceNew(servicePointId);
        }

        return servicePointFinance;
    }

    public void updateCache(ServicePointFinance servicePointFinance) {
        if (servicePointFinance == null) {
            throw new RuntimeException("网点财务对象为空!");
        }
        if (servicePointFinance.getId() == null || servicePointFinance.getId() <= 0) {
            throw new RuntimeException("输入的网点Id错误!");
        }
        Long servicePointId = servicePointFinance.getId();
        long modKey = servicePointId % SHARDING_COUNT;
        String key = String.format(RedisConstant.MD_SERVICEPOINT_FINANCE, modKey);
        String lockkey = String.format("lock:servicepointfinance:%s", servicePointId);
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey, 1, 60);//1分钟
        if (!locked) {
            throw new RuntimeException("网点财务正在修改中，请稍候重试。");
        }

        try {
            redisUtilsLocal.hmSet(RedisConstant.RedisDBType.REDIS_MD_DB, key, servicePointId.toString(), servicePointFinance, -1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (locked && lockkey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockkey);
            }
        }
    }

}
