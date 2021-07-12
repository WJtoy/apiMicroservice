package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerErrorCodeFeign;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorCodeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSCustomerErrorCodeService {

    @Autowired
    private MSCustomerErrorCodeFeign customerErrorCodeFeign;

    /**
     * 根据客户id,产品id,故障分类ids获取故障现象(从缓存获取)
     *
     * @param customerId
     * @param productErrorCodeIds key:产品id,value:故障现象
     * @return
     */
    public List<MDErrorCode> findListByCustomerIdAndProductIdsAndIds(Long customerId, List<NameValuePair<Long, Long>> productErrorCodeIds) {
        MSResponse<List<MDErrorCode>> msResponse = customerErrorCodeFeign.findListByProductIdAndIdsFromCache(customerId, productErrorCodeIds);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 根据客户id+产品id+故障分类id获取故障现象(新)
     * */
    public List<MDErrorCode> findListByProductAndErrorType(Long errorTypeId, Long productId, Long customerId) {
        return MDUtils.findListUnnecessaryConvertType(()->customerErrorCodeFeign.findListByProductAndErrorType(errorTypeId,productId,customerId));
    }

    /**
     * 根据客户id+产品id+id获取故障信息(缓存中获取)
     * @param customerId
     * @param productId
     * @param id
     * @return
     */
    public MDErrorCode getByProductIdAndCustomerIdFromCache(Long customerId, Long productId, Long id){
        if(id == null || id <=0){
            return null;
        }
        MSResponse<MDErrorCode> msResponse = customerErrorCodeFeign.getByProductIdAndCustomerIdFromCache(customerId,productId,id);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return null;
        }
    }

}
