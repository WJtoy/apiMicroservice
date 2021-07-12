package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.ms.providermd.feign.AuxiliaryMaterialItemFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class AuxiliaryMaterialItemService {
    @Autowired
    private AuxiliaryMaterialItemFeign auxiliaryMaterialItemFeign;

    /**
     * 获取所有辅件收费项目
     * @param
     * @return
     */
    public List<MDAuxiliaryMaterialItem> findAllList(){
        MSResponse<List<MDAuxiliaryMaterialItem>> msResponse = auxiliaryMaterialItemFeign.findAllList();
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return Lists.newArrayList();
        }
    }


    /**
     * 根据产品产品id集合存获取辅件信息
     * @param productIds
     * @return
     */
    public List<MDAuxiliaryMaterialItem> getListByProductId(List<String> productIds){
        MSResponse<List<MDAuxiliaryMaterialItem>> msResponse = auxiliaryMaterialItemFeign.getListByProductId(productIds);
        if(MSResponse.isSuccess(msResponse)){
             return msResponse.getData();
        }else{
            return Lists.newArrayList();
        }
    }
}
