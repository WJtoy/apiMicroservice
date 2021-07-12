package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerActionCodeFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MSCustomerErrorActionService {


    @Autowired
    private MSCustomerActionCodeFeign customerActionCodeFeign;

    /**
     * 根据客户id,产品id,故障分类ids获取故障处理(从缓存获取)
     *
     * @param customerId
     * @param productActionCodeIds key:产品id,value:故障处理
     * @return
     */
    public List<MDActionCode> findListByCustomerIdAndProductIdsAndIds(Long customerId, List<NameValuePair<Long, Long>> productActionCodeIds) {
        MSResponse<List<MDActionCode>> msResponse = customerActionCodeFeign.findListByProductIdAndIdsFromCache(customerId, productActionCodeIds);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 根据客户id+产品id+id获取故障处理信息(缓存中获取)
     * @param customerId
     * @param productId
     * @param id
     * @return
     */
    public MDActionCode getByProductIdAndCustomerIdFromCache(Long customerId, Long productId, Long id){
        if(id == null || id <=0){
            return null;
        }
        MSResponse<MDActionCode> msResponse = customerActionCodeFeign.getByProductIdAndCustomerIdFromCache(customerId,productId,id);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return null;
        }
    }
}
