package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialCategory;
import com.wolfking.jeesite.ms.providermd.feign.AuxiliaryMaterialCategoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class AuxiliaryMaterialCategoryService {
    @Autowired
    private AuxiliaryMaterialCategoryFeign auxiliaryMaterialCategoryFeign;

    /**
     * 根据所有数据
     *
     * @return
     */
    public List<MDAuxiliaryMaterialCategory> findAllList(){
        MSResponse<List<MDAuxiliaryMaterialCategory>> msResponse = auxiliaryMaterialCategoryFeign.findAllList();
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return Lists.newArrayList();
        }
    }
}
