package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDProductPicMapping;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerProductPicMappingFeign;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class MSCustomerProductPicMappingService {

    @Autowired
    private MSCustomerProductPicMappingFeign msCustomerProductPicMappingFeign;

    @Autowired
    private MapperFacade mapper;


    /**
     * 根据客户Id和产品Id获取完工图片配置信息
     * @param customerId
     * @param productId
     * @return
     */
    public ProductCompletePic getCustomerProductPicByProductAndCustomer(long customerId,long productId){
        MSResponse<MDProductPicMapping> msResponse = msCustomerProductPicMappingFeign.getCustomerProductPicByProductAndCustomer(customerId,productId);
        if(MSResponse.isSuccess(msResponse)){
            ProductCompletePic entity = mapper.map(msResponse.getData(),ProductCompletePic.class);
            if(entity!=null){
                return entity;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 根据客户Id和产品Id集合获取完工图片配置信息List
     * @param productIds
     * @param customerId
     * @return
     */
    public List<ProductCompletePic> findCustomerProductPicList(List<Long> productIds,long customerId){
        if (productIds == null || productIds.isEmpty() || customerId == 0) {
            return Lists.newArrayList();
        }
        productIds = productIds.stream().distinct().collect(Collectors.toList());
        MSResponse<List<MDProductPicMapping>> msResponse = msCustomerProductPicMappingFeign.findCustomerProductPicList(productIds,customerId);
        if(MSResponse.isSuccess(msResponse)){
            List<ProductCompletePic> list = mapper.mapAsList(msResponse.getData(),ProductCompletePic.class);
            if(list!=null && !list.isEmpty()){
                return list;
            }else{
                return Lists.newArrayList();
            }
        }else{
            return Lists.newArrayList();
        }
    }


    /**
     * 根据客户Id和产品Id集合获取完工图片配置信息map
     * @param productIds
     * @param customerId
     * @return
     */
    public Map<Long,ProductCompletePic> findCustomerProductPicMap(List<Long> productIds, long customerId){
       List<ProductCompletePic> list = findCustomerProductPicList(productIds,customerId);
       if(list==null || list.isEmpty()){
           return Maps.newHashMap();
       }
        Map<Long, ProductCompletePic> productCompletePicMap = list.stream().collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));
        return productCompletePicMap;
    }

}
