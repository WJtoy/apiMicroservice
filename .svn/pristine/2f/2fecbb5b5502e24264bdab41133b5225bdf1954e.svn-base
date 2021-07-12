package com.wolfking.jeesite.modules.md.service;

import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.service.AreaService;
import com.wolfking.jeesite.modules.sys.service.SystemService;
import com.wolfking.jeesite.ms.providermd.service.MSEngineerService;
import com.wolfking.jeesite.ms.providermd.service.MSServicePointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务网点
 * Ryan Lu
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class EngineerService extends LongIDBaseService {

    @Autowired
    private SystemService systemService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MSEngineerService msEngineerService;

    @Autowired
    private MSServicePointService msServicePointService;

    /**
     * 根据id获取安维人员信息
     *
     * @param id
     * @return
     */
    public Engineer getEngineer(Long id) {
        Engineer engineer = msEngineerService.getById(id);
        engineer = getExtraInfoForEngineer(engineer);
        return engineer;
    }


    /**
     * 获取安维人员的附加信息
     *
     * @param engineer
     * @return
     */
    public Engineer getExtraInfoForEngineer(Engineer engineer) {
        Long servicePointId = null;
        Long engineerId = null;
        Long areaId = null;
        if (engineer != null) {
            servicePointId = engineer.getServicePoint() != null && engineer.getServicePoint().getId() != null ? engineer.getServicePoint().getId() : null;
            engineerId = engineer.getId();
            areaId = engineer.getArea() != null && engineer.getArea().getId() != null ? engineer.getArea().getId() : null;
        }
        ServicePoint servicePoint = msServicePointService.getCacheById(servicePointId);
        if (servicePoint != null) {
            if (engineer.getServicePoint() == null) {
                engineer.setServicePoint(new ServicePoint(servicePointId));
            }
            engineer.getServicePoint().setName(servicePoint.getName());
            engineer.getServicePoint().setServicePointNo(servicePoint.getServicePointNo());
        }

        User user = systemService.getUserByEngineerId(engineerId);
        if (user != null) {
            engineer.setAccountId(user.getId());
            engineer.setAppLoged(user.getAppLoged());
        }

        Area area = areaId == null ? null : areaService.getFromCache(areaId);
        if (area != null) {
            if (engineer.getArea() == null) {
                engineer.setArea(new Area(area.getId()));
            }
            engineer.getArea().setName(area.getName());
            engineer.getArea().setFullName(area.getFullName());
        }
        return engineer;
    }
}
