package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.md.B2BProductMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BProductMappingFeign;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BProductMappingService {

    @Autowired
    private B2BProductMappingFeign productMappingFeign;

    @Autowired
    private MicroServicesProperties msProperties;

    /**
     * 一键同步,每次调用微服务同步数据的大小
     */
    private static final int SYNC_SIZE = 12;



    /**
     * 根据id获取
     * @param id
     * @return
     */
    public B2BProductMapping getById(Long id){
        MSResponse<B2BProductMapping> msResponse = productMappingFeign.getProductMappingById(id);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return null;
        }
    }


    /**
     * 查询数据源中所有B2B产品与工单系统产品的映射关系
     *
     * @param dataSource B2BDataSourceEnum
     * @return
     */
    public List<B2BProductMapping> getListByDataSource(B2BDataSourceEnum dataSource) {
        List<B2BProductMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null) {
                MSResponse<List<B2BProductMapping>> responseEntity = productMappingFeign.getListByDataSource(dataSource.id);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                }
            }
        }
        return list;
    }

    public List<B2BProductMapping> getListByCustomerCategoryIds(B2BDataSourceEnum dataSource, List<String> customerCategoryIds) {
        List<B2BProductMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null && customerCategoryIds != null && !customerCategoryIds.isEmpty()) {
                MSResponse<List<B2BProductMapping>> responseEntity = productMappingFeign.getListByCustomerCategoryIds(dataSource.id, customerCategoryIds);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                }
            }
        }
        return list;
    }

    public List<B2BProductMapping> getListByCustomerCategoryIds(B2BDataSourceEnum dataSource, String shopId, List<String> customerCategoryIds) {
        List<B2BProductMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null && customerCategoryIds != null && StringUtils.isNotBlank(shopId) && !customerCategoryIds.isEmpty()) {
                MSResponse<List<B2BProductMapping>> responseEntity = productMappingFeign.getListByCustomerCategoryIds(dataSource.id, customerCategoryIds);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                    list = list.stream().filter(i -> StringUtils.isNotBlank(i.getShopId()) && i.getShopId().equals(shopId)).collect(Collectors.toList());
                }
            }
        }
        return list;
    }

    /**
     * 获取多个店铺指定类目对应的产品
     */
    public Map<String, List<B2BProductMapping>> getListByCustomerCategoryIds(B2BDataSourceEnum dataSource, List<String> shopIds, List<String> customerCategoryIds) {
        Map<String, List<B2BProductMapping>> result = Maps.newHashMap();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null && customerCategoryIds != null && shopIds != null) {
                customerCategoryIds = customerCategoryIds.stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                shopIds = shopIds.stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                if (!customerCategoryIds.isEmpty() && !shopIds.isEmpty()) {
                    MSResponse<List<B2BProductMapping>> responseEntity = productMappingFeign.getListByCustomerCategoryIds(dataSource.id, customerCategoryIds);
                    if (MSResponse.isSuccess(responseEntity)) {
                        List<B2BProductMapping> list;
                        for (String sId : shopIds) {
                            list = responseEntity.getData().stream().filter(i -> StringUtils.isNotBlank(i.getShopId()) && i.getShopId().equals(sId)).collect(Collectors.toList());
                            if (!list.isEmpty()) {
                                result.put(sId, list);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public Map<Long, List<String>> getListByProductIds(B2BDataSourceEnum dataSource, List<Long> productIds) {
        Map<Long, List<String>> map = Maps.newHashMap();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null && productIds != null && !productIds.isEmpty()) {
                MSResponse<Map<Long, List<String>>> responseEntity = productMappingFeign.getListByProductIds(dataSource.id, productIds);
                if (MSResponse.isSuccess(responseEntity)) {
                    map = responseEntity.getData();
                }
            }
        }
        return map;
    }

    /**
     * 分页查询
     *
     * @param page,b2BProductMapping
     * @return
     */
    public Page<B2BProductMapping> getList(Page<B2BProductMapping> page, B2BProductMapping b2BProductMapping) {
        Page<B2BProductMapping> b2BProductMappingPage = new Page<>();
        b2BProductMappingPage.setPageSize(page.getPageSize());
        b2BProductMappingPage.setPageNo(page.getPageNo());
        b2BProductMapping.setPage(new MSPage<>(b2BProductMappingPage.getPageNo(), b2BProductMappingPage.getPageSize()));
        MSResponse<MSPage<B2BProductMapping>> returnCustomerMapping = productMappingFeign.getProductMappingList(b2BProductMapping);
        if (MSResponse.isSuccess(returnCustomerMapping)) {
            MSPage<B2BProductMapping> data = returnCustomerMapping.getData();
            b2BProductMappingPage.setCount(data.getRowCount());
            b2BProductMappingPage.setList(data.getList());
        }
        return b2BProductMappingPage;
    }

    /**
     * 根据数据源查询
     *
     * @param dataSource
     * @return
     */
    public List<B2BProductMapping> getListByDataSource(Integer dataSource) {
        MSResponse<List<B2BProductMapping>> returnCustomerMapping = productMappingFeign.getListByDataSource(dataSource);
        List<B2BProductMapping> list = returnCustomerMapping.getData();
        return list;
    }

    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @param <T>
     * @return
     */
    public <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<T>> result = new ArrayList<List<T>>();

        int sourceSize = source.size();
        int size = (source.size() / n) + 1;
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }

}
