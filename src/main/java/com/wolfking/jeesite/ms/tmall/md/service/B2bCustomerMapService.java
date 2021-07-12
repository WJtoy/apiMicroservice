package com.wolfking.jeesite.ms.tmall.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.ms.b2bcenter.md.service.B2BCustomerMappingService;
import com.wolfking.jeesite.ms.tmall.md.entity.B2bCustomerMap;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 对接系统店铺与厂商关联
 *
 * @author Ryan
 * @date 2018/05/04
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class B2bCustomerMapService extends LongIDBaseService {

    @Autowired
    private B2BCustomerMappingService customerMappingService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 按厂商获得所有关联店铺(可能1:n)
     */
    public List<B2bCustomerMap> getShopListByCustomer(int dataSource, Long customerId) {
        List<B2bCustomerMap> result = Lists.newArrayList();
        if (customerId != null) {
            List<B2BCustomerMapping> list = customerMappingService.getListByDataSource(B2BDataSourceEnum.valueOf(dataSource));
            if (list != null && list.size() > 0) {
                B2bCustomerMap customerMap = null;
                for (B2BCustomerMapping item : list) {
                    if (item.getCustomerId().equals(customerId)) {
                        customerMap = mapperFacade.map(item, B2bCustomerMap.class);
                        result.add(customerMap);
                    }
                }
            }
        }
        return result;

    }

    /**
     * 因为缓存都是缓存每一个客户的店铺信息
     * 所以取所有客户的店铺时直接是从数据库读取
     * 获得所有店铺列表
     *
     * @param dataSource
     * @return
     */
    public List<B2bCustomerMap> getAllShopList(int dataSource) {
        List<B2bCustomerMap> result = Lists.newArrayList();
        List<B2BCustomerMapping> list = customerMappingService.getListByDataSource(B2BDataSourceEnum.valueOf(dataSource));
        if (list != null && list.size() > 0) {
            B2bCustomerMap customerMap = null;
            for (B2BCustomerMapping item : list) {
                customerMap = mapperFacade.map(item, B2bCustomerMap.class);
                result.add(customerMap);
            }
        }
        return result;
    }

    /**
     * 获得店铺名
     *
     * @param dataSource 数据来源
     * @param shopId     店铺id
     */
    public B2bCustomerMap getShopInfo(int dataSource, String shopId) {
        List<B2bCustomerMap> list = getAllShopList(dataSource);
        if (list != null && list.size() > 0) {
            return list.stream().filter(t -> t.getShopId().equalsIgnoreCase(shopId)).findFirst().orElse(null);
        }
        return null;
    }

    /**
     * 获得店铺名
     *
     * @param dataSource 数据来源
     * @param customerId 客户id
     * @param shopId     店铺id
     */
    public String getShopName(int dataSource, Long customerId, String shopId) {
        List<B2bCustomerMap> list = getShopListByCustomer(dataSource, customerId);
        if (list != null && list.size() > 0) {
            B2bCustomerMap b2bCustomerMap = list.stream().filter(t -> t.getShopId().equalsIgnoreCase(shopId)).findFirst().orElse(null);
            if (b2bCustomerMap == null) {
                return "";
            } else {
                return b2bCustomerMap.getShopName();
            }
        }
        return "";
    }
}
