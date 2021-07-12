package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCustomerMappingFeign;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCustomerMappingService {

    @Autowired
    private B2BCustomerMappingFeign customerMappingFeign;

    @Autowired
    private MicroServicesProperties msProperties;

    /**
     * 查询数据源中所有的店铺与客户的对应关系
     *
     * @param dataSource B2BDataSourceEnum
     * @return
     */
    public List<B2BCustomerMapping> getListByDataSource(B2BDataSourceEnum dataSource) {
        List<B2BCustomerMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null) {
                MSResponse<List<B2BCustomerMapping>> responseEntity = customerMappingFeign.getListByDataSource(dataSource.id);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                }
            }
        }
        return list;
    }

    /**
     * 查询数据源中客户下的所有店铺列表
     *
     * @param dataSource B2BDataSourceEnum
     * @param customerId 客户id
     */
    public List<B2BCustomerMapping> getShopListByCustomer(B2BDataSourceEnum dataSource, Long customerId) {
        List<B2BCustomerMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null) {
                MSResponse<List<B2BCustomerMapping>> responseEntity = customerMappingFeign.getListByDataSource(dataSource.id);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                }
                if (list != null && list.size() > 0 && customerId != null && customerId > 0) {
                    list = list.stream().filter(t -> t.getCustomerId().longValue() == customerId.longValue()).collect(Collectors.toList());
                }
            }
        }
        return list;
    }

    /**
     * 分页查询
     *
     * @param page,b2BCustomerMapping
     * @return
     */
    public Page<B2BCustomerMapping> getList(Page<B2BCustomerMapping> page, B2BCustomerMapping b2BCustomerMapping) {
        if (b2BCustomerMapping.getPage() == null) {
            MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BCustomerMapping> b2BCustomerMappingPage = new Page<>();
        b2BCustomerMappingPage.setPageSize(page.getPageSize());
        b2BCustomerMappingPage.setPageNo(page.getPageNo());
        b2BCustomerMapping.setPage(new MSPage<>(b2BCustomerMappingPage.getPageNo(), b2BCustomerMappingPage.getPageSize()));
        MSResponse<MSPage<B2BCustomerMapping>> returnCustomerMapping = customerMappingFeign.getCustomerMappingList(b2BCustomerMapping);
        if (MSResponse.isSuccess(returnCustomerMapping)) {
            MSPage<B2BCustomerMapping> data = returnCustomerMapping.getData();
            b2BCustomerMappingPage.setCount(data.getRowCount());
            b2BCustomerMappingPage.setList(data.getList());
        }
        return b2BCustomerMappingPage;
    }

    /**
     * 保存数据
     *
     * @param b2BCustomerMapping
     * @return
     */
    public MSErrorCode save(B2BCustomerMapping b2BCustomerMapping) {

        if (b2BCustomerMapping.getId() != null && b2BCustomerMapping.getId() > 0) {
            b2BCustomerMapping.preUpdate();
            MSResponse<Integer> msResponse = customerMappingFeign.updateCustomerMapping(b2BCustomerMapping);
            return new MSErrorCode(msResponse.getCode(), msResponse.getMsg());
        } else {
            b2BCustomerMapping.preInsert();
            MSResponse<B2BCustomerMapping> msResponse = customerMappingFeign.insertCustomerMapping(b2BCustomerMapping);
            return new MSErrorCode(msResponse.getCode(), msResponse.getMsg());
        }
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    public MSResponse<B2BCustomerMapping> getById(Long id) {
        return customerMappingFeign.getCustomerMappingById(id);
    }

    /**
     * 根据shopId,dataSource获取数据
     *
     * @param shopId,dataSource
     * @return
     */
    public MSResponse<Long> checkShopId(String shopId, Integer dataSource) {
        return customerMappingFeign.getByShopId(shopId, dataSource);
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<B2BCustomerMapping> findAllList() {
        MSResponse<List<B2BCustomerMapping>> msResponse = customerMappingFeign.findAllList();
        List<B2BCustomerMapping> list = Lists.newArrayList();
        if (MSResponse.isSuccess(msResponse)) {
            list = msResponse.getData();
        }
        return list;
    }


    /**
     * 从缓存中读取所有默认店铺数据
     *
     * @return Map<Integer       ,       String> key:数据源 value:店铺Id
     */
    public Map<Integer, String> getDefaultShopMap() {
        Map<Integer, String> result = Maps.newHashMap();
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<Map<Integer, String>> responseEntity = customerMappingFeign.getDefaultShopMap();
            if (MSResponse.isSuccess(responseEntity)) {
                result = responseEntity.getData();
            }
        }
        return result;
    }

    /**
     * 获取系统中所有的店铺信息
     */
    public List<B2BCustomerMapping> getAllCustomerMapping() {
        List<B2BCustomerMapping> result = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<List<B2BCustomerMapping>> responseEntity = customerMappingFeign.getAllCustomerMapping();
            if (MSResponse.isSuccess(responseEntity)) {
                result = responseEntity.getData();
            }
        }
        return result;
    }

    /**
     * 获取指定客户店铺的名称
     */
    public String getshopName(Long customerId, String shopId) {
        String shopName = "";
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<String> responseEntity = customerMappingFeign.getShopName(customerId, shopId);
            if (MSResponse.isSuccess(responseEntity)) {
                shopName = responseEntity.getData();
            }
        }
        return shopName;
    }

}
