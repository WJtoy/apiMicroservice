package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerProductPicMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 品牌Service
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CustomerProductCompletePicService extends LongIDBaseService {
    
    @Autowired
    private MSCustomerProductPicMappingService msCustomerProductPicMappingService;

    /**
     * 优先从缓存中按id获得对象
     *
     * @param productId 产品id
     * @param customerId
     * @return
     */
    public ProductCompletePic getFromCache(long productId,long customerId) {
        ProductCompletePic productCompletePic = msCustomerProductPicMappingService.getCustomerProductPicByProductAndCustomer(customerId,productId);
        if(productCompletePic !=null){
            return productCompletePic;
        }
        return null;
    }

    public Map<Long, ProductCompletePic> getProductCompletePicMap(List<Long> productIds, Long customerId) {
        if (productIds == null || productIds.isEmpty() || customerId == null) {
            return Maps.newHashMap();
        }
        //调用微服务 2019-9-25 start
        Map<Long,ProductCompletePic> map = msCustomerProductPicMappingService.findCustomerProductPicMap(productIds,customerId);
        if(map!=null && map.size()>0){
            return map;
        }
        return Maps.newHashMap();
    }

}
