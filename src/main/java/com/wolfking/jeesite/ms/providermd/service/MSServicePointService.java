package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointAddress;
import com.kkl.kklplus.entity.md.MDServicePointViewModel;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointAddressFeign;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSServicePointService {
    @Autowired
    private MSServicePointFeign msServicePointFeign;

    @Autowired
    private MSServicePointAddressFeign msServicePointAddressFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 根据网点id获取网点信息
     *
     * @param id 网点Id
     * @return
     */
    public ServicePoint getById(Long id) {
        return MDUtils.getObjNecessaryConvertType(ServicePoint.class, () -> msServicePointFeign.getById(id));
    }

    /**
     * 从缓存中获取网点数据
     *
     * @param id 网点id
     * @return
     */
    public ServicePoint getCacheById(Long id) {
        return MDUtils.getObjNecessaryConvertType(ServicePoint.class, () -> msServicePointFeign.getCacheById(id));
    }

    /**
     * 通过网点id列表及要获取的字段列表获取网点列表 // add on 2019-10-14
     *
     * @param servicePointIds
     * @return 要返回的字段跟参数fields中相同
     */
    public List<MDServicePointViewModel> findBatchByIdsByCondition(List<Long> servicePointIds, List<String> fields, Integer delFlag) {
        Class<?> cls = MDServicePointViewModel.class;
        Field[] fields1 = cls.getDeclaredFields();

        Long icount = Arrays.asList(fields1).stream().filter(r -> fields.contains(r.getName())).count();
        if (icount.intValue() != fields.size()) {
            throw new RuntimeException("按条件获取网点列表数据要求返回的字段有问题，请检查");
        }

        List<MDServicePointViewModel> mdServicePointViewModelList = Lists.newArrayList();
        if (servicePointIds == null || servicePointIds.isEmpty()) {
            return mdServicePointViewModelList;
        }
        if (servicePointIds.size() < 200) {  //小于200 一次调用,
            MSResponse<List<MDServicePointViewModel>> msResponse = msServicePointFeign.findBatchByIdsByCondition(servicePointIds, fields, delFlag);
            if (MSResponse.isSuccess(msResponse)) {
                mdServicePointViewModelList = msResponse.getData();
            }
        } else { // 大于等于200 分批次调用
            List<MDServicePointViewModel> mdServicePointViewModels = Lists.newArrayList();
            List<List<Long>> servicePointIdList = Lists.partition(servicePointIds, 1000); //测试验证一次取1000笔数据比较合理
            servicePointIdList.stream().forEach(longList -> {
                MSResponse<List<MDServicePointViewModel>> msResponse = msServicePointFeign.findBatchByIdsByCondition(longList, fields, delFlag);
                if (MSResponse.isSuccess(msResponse)) {
                    Optional.ofNullable(msResponse.getData()).ifPresent(mdServicePointViewModels::addAll);
                }
            });
            if (!mdServicePointViewModels.isEmpty()) {
                mdServicePointViewModelList.addAll(mdServicePointViewModels);
            }
        }

        return mdServicePointViewModelList;
    }

    /**
     * 通过网点id列表及要获取的字段列表获取网点列表 // add on 2019-10-14
     *
     * @param servicePointIds
     * @param fields
     * @param delFlag
     * @return
     */
    public Map<Long, MDServicePointViewModel> findBatchByIdsByConditionToMap(List<Long> servicePointIds, List<String> fields, Integer delFlag) {
        List<MDServicePointViewModel> mdServicePointViewModelList = findBatchByIdsByCondition(servicePointIds, fields, delFlag);
        return mdServicePointViewModelList != null && !mdServicePointViewModelList.isEmpty() ? mdServicePointViewModelList.stream().collect(Collectors.toMap(MDServicePointViewModel::getId, Function.identity())) : Maps.newHashMap();
    }


    /**
     * 有条件更新网点信息
     *
     * @param maps
     * @return
     */
    public MSErrorCode updateServicePointByMap(HashMap<String, Object> maps) {
        return MDUtils.genericCustomConditionSave(maps, msServicePointFeign::updateServicePointByMap);
    }

    /**
     * 获取是否开启互助基金标志
     * @param id
     * @return
     */
    public NameValuePair<Integer, Integer> getInsuranceFlagByIdForAPI(Long id){
        return MDUtils.getObjUnnecessaryConvertType(()->
                msServicePointFeign.getInsuranceFlagByIdForAPI(id));
    }

    /***
     * app关闭购买互助金
     */
    public MSErrorCode updateInsuranceFlagForAPI(MDServicePoint mdServicePoint){
        return MDUtils.customSave((() ->
                msServicePointFeign.updateInsuranceFlagForAPI(mdServicePoint)
        ));
    }

    /**
     * 是否启用网点保险扣除
     *
     * @param id
     * @param appInsuranceFlag
     * @param updateBy
     * @param updateDate
     * @return
     */
    public MSErrorCode appReadInsuranceClause(Long id, Integer appInsuranceFlag, Long updateBy, Date updateDate) {
        long date = updateDate != null ? updateDate.getTime() : new Date().getTime();
        return MDUtils.customSave((() ->
                msServicePointFeign.appReadInsuranceClause(id, appInsuranceFlag, updateBy, date)
        ));
    }


    /**
     * 更新网点地址信息
     *
     * @param servicePoint
     * @return
     */
    public MSErrorCode updateServicePointAddress(ServicePoint servicePoint) {
        return MDUtils.genericSave(servicePoint, MDServicePoint.class, false, msServicePointFeign::updateServicePointAddress);
    }


    /**
     * 更新网点的账号信息
     *
     * @param servicePoint
     * @return
     */
    public MSErrorCode updateServicePointBankAccountInfo(ServicePoint servicePoint) {
        return MDUtils.genericSave(servicePoint, MDServicePoint.class, false, msServicePointFeign::updateServicePointBankAccountInfo);
    }

    //region API

    /**
     * 添加网点的收货地址
     *
     * @param mdServicePointAddress
     */
    public void saveAddress(MDServicePointAddress mdServicePointAddress) {
        MSErrorCode msErrorCode = MDUtils.customSave(() -> msServicePointAddressFeign.save(mdServicePointAddress));
        if (msErrorCode.getCode() > 0) {
            throw new RuntimeException("调用微服务添加网点收货地址失败.失败原因:" + msErrorCode.getMsg());
        }
    }

    /**
     * 添加网点的收货地址
     *
     * @param servicePointId
     */
    public MDServicePointAddress getAddressByServicePointIdFromCache(Long servicePointId) {
        //MDServicePointAddress mdServicePointAddress = MDUtils.getByCustomCondition(()->msServicePointAddressFeign.getByServicePointIdFromCache(servicePointId));
        //return mdServicePointAddress;
        MDServicePointAddress mdServicePointAddress = MDUtils.getObjUnnecessaryConvertType(() -> msServicePointAddressFeign.getByServicePointIdFromCache(servicePointId));
        return mdServicePointAddress;
    }


    //endregion API

    /**
     * 从缓存中获取网点数据(为了网点付款，付款确认中快速返回有限网点字段信息(id,servicepointno,name,primaryId)
     * @param id 网点id
     * @return
     *   id,servicePointNo,name,primaryId,customizePriceFlag
     */
    public ServicePoint getSimpleCacheById(Long id) {
        return MDUtils.getObjNecessaryConvertType(ServicePoint.class, ()->msServicePointFeign.getSimpleCacheById(id));
    }

    /**
     * 更新网点未完工单数量
     * @param paramMap
     */
    public void updateUnfinishedOrderCountByMapForSD(Map<String,Object> paramMap){
        MSResponse<Integer> msResponse = msServicePointFeign.updateUnfinishedOrderCountByMapForSD(paramMap);
        if(!MSResponse.isSuccess(msResponse)){
            throw new RuntimeException("更新网点未完工单数量调用微服务失败.失败原因:" +msResponse.getMsg());
        }
    }

}
