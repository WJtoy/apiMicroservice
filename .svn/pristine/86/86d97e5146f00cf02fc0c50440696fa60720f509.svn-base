package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDCustomerProduct;
import com.kkl.kklplus.entity.md.MDProduct;
import com.wolfking.jeesite.modules.md.entity.CustomerProduct;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerProductFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MSCustomerProductService {

    @Autowired
    private MSCustomerProductFeign msCustomerProductFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 从缓存中获取安装规范 （API）
     *
     * @param customerId
     * @param productId
     * @return
     */
    public CustomerProduct getFixSpecFromCache(Long customerId, Long productId) {
        //return MDUtils.getEntity(CustomerProduct.class, ()->msCustomerProductFeign.getFixSpecFromCache(customerId, productId));
        return MDUtils.getObjNecessaryConvertType(CustomerProduct.class, () -> msCustomerProductFeign.getFixSpecFromCacheForApi(customerId, productId));

    }

    public Map<Long,Integer> findFixSpecByCustomerIdAndProductIdsFromCache(Long customerId, List<Long> productIds){
        MSResponse<Map<Long, Integer>> msResponse = msCustomerProductFeign.findFixSpecByCustomerIdAndProductIdsFromCacheForAPI(customerId, productIds);
        if (MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else {
            return new HashMap<>();
        }
    }
    /**
     * 根据客户id获取
     *
     * @param customerId
     * @return
     */
    public List<CustomerProduct> getByCustomer(Long customerId) {
        MSResponse<List<MDCustomerProduct>> msResponse = msCustomerProductFeign.findByCustomer(customerId);
        if (MSResponse.isSuccess(msResponse)) {
            List<CustomerProduct> list = mapper.mapAsList(msResponse.getData(), CustomerProduct.class);
            return list;
        } else {
            return Lists.newArrayList();
        }
    }


    /**
     * 根据客户Id重缓存获取产品
     *
     * @param customerId
     * @return
     */
    public List<Product> findProductByCustomerIdFromCache(Long customerId) {
        MSResponse<List<MDProduct>> msResponse = msCustomerProductFeign.findProductByCustomerIdFromCache(customerId);
        if (MSResponse.isSuccess(msResponse)) {
            return mapper.mapAsList(msResponse.getData(), Product.class);
        } else {
            return Lists.newArrayList();
        }
    }
}
