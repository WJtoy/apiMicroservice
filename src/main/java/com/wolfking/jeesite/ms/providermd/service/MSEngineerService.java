package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDEngineer;
import com.kkl.kklplus.entity.md.dto.MDEngineerDto;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.ms.providermd.feign.MSEngineerFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MSEngineerService {
    @Autowired
    private MSEngineerFeign msEngineerFeign;

    @Autowired
    private MapperFacade mapper;






    /**
     * 根据id获取安维人员信息
     *
     * @param id
     * @return
     */
    public Engineer getById(Long id) {
        if (id == null) {
            return null;
        }
        Engineer engineer = MDUtils.getById(id, Engineer.class, msEngineerFeign::getById);
        if (engineer == null) {
            engineer = new Engineer(id);
        }
        return engineer;
    }

    /**
     * 根据id获取安维人员信息
     *
     * @param id
     * @return
     */
    public Engineer getByIdFromCache(Long id) {
        if (id == null) {
            return null;
        }
        Engineer engineer = MDUtils.getById(id, Engineer.class, msEngineerFeign::getByIdByFromCache);
        if (engineer == null) {
            engineer = new Engineer(id);
        }
        return engineer;
    }


    // region API

    /**
     * 基本信息：姓名、电话、派单、完成、催单、投诉单(API)
     *
     * @param engineerId
     * @return
     */
    public Engineer getBaseInfoFromCache(Long engineerId) {
        //return MDUtils.getEntity(Engineer.class, ()->msEngineerFeign.getBaseInfoFromCache(engineerId));
        return MDUtils.getObjNecessaryConvertType(Engineer.class, () -> msEngineerFeign.getBaseInfoFromCache(engineerId));
    }

    /**
     * 基本信息：姓名、电话、派单、完成、催单、投诉单(API)
     *
     * @param engineerId
     * @return
     */
    public Engineer getDetailInfoFromCache(Long servicePointId, Long engineerId) {
        //return MDUtils.getEntity(Engineer.class, ()->msEngineerFeign.getDetailInfoFromCache(servicePointId, engineerId));
        return MDUtils.getObjNecessaryConvertType(Engineer.class, () -> msEngineerFeign.getDetailInfoFromCache(servicePointId, engineerId));
    }

    /**
     * 更新安维地址（API)
     *
     * @param engineer
     */
    public void updateAddress(Engineer engineer) {
        MSErrorCode msErrorCode = MDUtils.genericSave(engineer, MDEngineer.class, false, msEngineerFeign::updateAddress);
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务更新安维地址失败.失败原因:" + msErrorCode.getMsg());
        }
    }
    // endregion API

    /**
     * 根据网点id和安维id从缓存中获取安维信息
     *
     * @param servicePointId
     * @param engineerId
     * @return
     */
    public Engineer getEngineerFromCache(Long servicePointId, Long engineerId) {
        //return MDUtils.getEntity(Engineer.class,(()->msEngineerFeign.getEngineerFromCache(engineerId, servicePointId)));
        return MDUtils.getObjNecessaryConvertType(Engineer.class, (() -> msEngineerFeign.getEngineerFromCache(engineerId, servicePointId)));
    }


    /**
     * 根据id列表获取安维人员列表
     *
     * @param ids
     * @return
     */
    public List<Engineer> findEngineersByIds(List<Long> ids, List<String> fields) {
        List<Field> fieldList = Lists.newArrayList();
        Class<?> cls = Engineer.class;
        while (cls != null) {
            Field[] fields1 = cls.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields1));
            cls = cls.getSuperclass();
        }
        Long icount = fieldList.stream().filter(r -> fields.contains(r.getName())).count();
        if (icount.intValue() != fields.size()) {
            throw new RuntimeException("按条件获取安维列表数据要求返回的字段有问题，请检查");
        }

        List<Engineer> engineerList = Lists.newArrayList();
        if (ids != null && !ids.isEmpty()) {
            Lists.partition(ids, 1000).forEach(longList -> {
                MSResponse<List<MDEngineer>> msResponse = msEngineerFeign.findEngineersByIds(longList, fields);
                if (MSResponse.isSuccess(msResponse)) {
                    List<Engineer> engineerListFromMS = mapper.mapAsList(msResponse.getData(), Engineer.class);
                    if (engineerListFromMS != null && !engineerListFromMS.isEmpty()) {
                        engineerList.addAll(engineerListFromMS);
                    }
                }
            });
        }
        return engineerList;
    }


    /**
     * 根据网点id从缓存中获取安维信息 //2019-11-2
     *
     * @param servicePointId
     * @return
     */
    public List<Engineer> findEngineerByServicePointIdFromCache(Long servicePointId) {
        return MDUtils.findListNecessaryConvertType(Engineer.class, () -> msEngineerFeign.findEngineerByServicePointIdFromCache(servicePointId));
    }


    /**
     * 分页查询安维数据
     *
     * @param page
     * @param engineer
     * @return
     */
    public Page<Engineer> findEngineerList(Page<Engineer> page, Engineer engineer) {
        //return MDUtils.findListForPage(page, engineer, Engineer.class, MDEngineerDto.class, msEngineerFeign::findEngineerList);
        return MDUtils.findListForPage(page, engineer, Engineer.class, MDEngineerDto.class, msEngineerFeign::findEngineerListFromAPI);
    }

    /**
     * 更新安维人员单数与评分
     *
     * @param paramMap
     * @return
     */
    public void updateEngineerByMap(Map<String, Object> paramMap) {
        if (paramMap.isEmpty()) {
            return;
        }
        log.warn("updateEngineerByMap 传入的数据:{}", paramMap);

        MDEngineer mdEngineer = new MDEngineer();
        mdEngineer.setId(Long.valueOf(paramMap.get("id").toString()));
        if (paramMap.get("orderCount") != null) {
            mdEngineer.setOrderCount(Integer.valueOf(paramMap.get("orderCount").toString()));
        }
        if (paramMap.get("planCount") != null) {
            mdEngineer.setPlanCount(Integer.valueOf(paramMap.get("planCount").toString()));
        }
        if (paramMap.get("breakCount") != null) {
            mdEngineer.setBreakCount(Integer.valueOf(paramMap.get("breakCount").toString()));
        }
        if (paramMap.get("grade") != null) {
            mdEngineer.setGrade(Double.valueOf(paramMap.get("grade").toString()));
        }
        MSErrorCode msErrorCode = MDUtils.customSave(() -> msEngineerFeign.updateEngineerByMap(mdEngineer));

        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务修改安维人员单数及评分失败,失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 修改师傅体温
     * @param engineer
     */
    public MSErrorCode updateTemperature(Engineer engineer) {
        MDEngineer mdEngineer = mapper.map(engineer, MDEngineer.class);
        MSErrorCode msErrorCode = MDUtils.customSave(() -> msEngineerFeign.updateTemperatureForAPI(mdEngineer));
        return msErrorCode;
    }
}
