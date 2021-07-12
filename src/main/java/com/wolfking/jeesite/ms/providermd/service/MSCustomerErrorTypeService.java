package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerErrorTypeFeign;
import com.wolfking.jeesite.ms.providermd.feign.MSErrorTypeFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MSCustomerErrorTypeService {

    @Autowired
    private MSErrorTypeFeign errorTypeFeign;

    @Autowired
    private MSCustomerErrorTypeFeign customerErrorTypeFeign;

    /**
     * 根据客户id,产品id,故障分类ids获取故障分类(从缓存获取)
     *
     * @param customerId
     * @param productErrorTypeIds key:产品id,value:故障分类id
     * @return
     */
    public List<MDErrorType> findListByCustomerIdAndProductIdsAndIds(Long customerId, List<NameValuePair<Long, Long>> productErrorTypeIds) {
        MSResponse<List<MDErrorType>> msResponse = customerErrorTypeFeign.findListByProductIdAndIdsFromCache(customerId, productErrorTypeIds);
        if (MSResponse.isSuccessCode(msResponse)) {
            return msResponse.getData();
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 根据客户id跟产品id获取故障分类列表,如果没数据则在根据产品id和customerId=0获取(提供工单接口配置)
     * @param productId
     * @param customerId
     * @return
     */
    public List<MDErrorType> findListByProductIdAndCustomerIdFromCache(Long productId,Long customerId) {
        MSResponse<List<MDErrorType>> msResponse = customerErrorTypeFeign.findListByProductIdAndCustomerIdFromCache(productId,customerId);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return Lists.newArrayList();
    }

    /**
     * 根据客户Id和产品id获取故障分类（用于基础资料配置）
     * */
    public List<MDErrorType> findErrorTypesByProductId(Long customerId, Long productId) {
        MSResponse<List<MDErrorType>> msResponse = customerErrorTypeFeign.findErrorTypesByProductId(productId,customerId);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        }
        return Lists.newArrayList();
    }

    /**
     * 根据客户id+产品id+id获取故障分类(缓存中获取)
     * @param customerId
     * @param productId
     * @param id
     * @return
     */
    public MDErrorType getByProductIdAndCustomerIdFromCache(Long customerId, Long productId, Long id){
        if(id == null || id <=0){
            return null;
        }
        MSResponse<MDErrorType> msResponse = customerErrorTypeFeign.getByProductIdAndCustomerIdFromCache(customerId,productId,id);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return null;
        }
    }

}
