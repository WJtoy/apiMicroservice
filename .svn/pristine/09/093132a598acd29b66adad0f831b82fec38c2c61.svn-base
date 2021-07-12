package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDServiceType;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.ms.providermd.feign.MSServiceTypeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MSServiceTypeService {

    @Autowired
    private MSServiceTypeFeign msServiceTypeFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<ServiceType> findAllList() {
        return MDUtils.findAllList(ServiceType.class, msServiceTypeFeign::findAllList);
    }

    /**
     * 获取所有的服务类型
     *
     * @return map<Long, String> key为id,value为服务类型名称</>
     */
    public Map<Long, String> findAllIdsAndNames() {
        MSResponse<Map<Long, String>> msResponse = msServiceTypeFeign.findAllIdsAndNames();
        if (MSResponse.isSuccess(msResponse)) {
            Map<Long, String> map = msResponse.getData();
            return map;
        } else {
            return Maps.newHashMap();
        }
    }

    /**
     * 获取所有的服务类型
     *
     * @return map<Long, String> key为id,value为服务类型编码(code)</>
     */
    public Map<Long, String> findIdsAndCodes() {
        MSResponse<Map<Long, String>> msResponse = msServiceTypeFeign.findIdsAndCodes();
        if (MSResponse.isSuccess(msResponse)) {
            Map<Long, String> map = msResponse.getData();
            return map;
        } else {
            return Maps.newHashMap();
        }
    }


    /**
     * 根据对象属性名,返回相对应的数据
     *
     * @param fieldList 需要返回数据的对象的属性名(如果需要返回id跟名称，即fieldList.add("id")和fieldList.add("name"))
     * @return list
     */
    public List<ServiceType> findAllListWithCondition(List<String> fieldList) {
        MSResponse<List<MDServiceType>> msResponse = msServiceTypeFeign.findAllListWithCondition(fieldList);
        if (MSResponse.isSuccess(msResponse)) {
            List<ServiceType> list = mapper.mapAsList(msResponse.getData(), ServiceType.class);
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return Lists.newArrayList();
            }
        } else {
            return Lists.newArrayList();
        }
    }

}
