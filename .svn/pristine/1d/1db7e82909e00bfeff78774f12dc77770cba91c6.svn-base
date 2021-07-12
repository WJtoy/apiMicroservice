package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.md.B2BWarrantyMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCustomerMappingFeign;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BWarrantyMappingFeign;
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
public class B2BWarrantyMappingService {

    @Autowired
    private B2BWarrantyMappingFeign warrantyMappingFeign;

    @Autowired
    private MicroServicesProperties msProperties;

    /**
     * 查询数据源中所有质保类型列表
     *
     * @param dataSource B2BDataSourceEnum
     * @return
     */
    public List<B2BWarrantyMapping> getListByDataSource(B2BDataSourceEnum dataSource) {
        List<B2BWarrantyMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            if (dataSource != null) {
                MSResponse<List<B2BWarrantyMapping>> responseEntity = warrantyMappingFeign.getListByDataSource(dataSource.id);
                if (MSResponse.isSuccess(responseEntity)) {
                    list = responseEntity.getData();
                }
            }
        }
        return list;
    }

    /**
     * 分页查询
     * @param page,b2BCustomerMapping
     * @return
     */
    public Page<B2BWarrantyMapping> getList(Page<B2BWarrantyMapping> page, B2BWarrantyMapping warrantyMapping) {
        if (warrantyMapping.getPage() == null) {
            MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BWarrantyMapping> warrantyMappingPage = new Page<>();
        warrantyMappingPage.setPageSize(page.getPageSize());
        warrantyMappingPage.setPageNo(page.getPageNo());
        warrantyMapping.setPage(new MSPage<>(warrantyMappingPage.getPageNo(), warrantyMappingPage.getPageSize()));
        MSResponse<MSPage<B2BWarrantyMapping>> returnWarrantyMapping = warrantyMappingFeign.getList(warrantyMapping);
        if (MSResponse.isSuccess(returnWarrantyMapping)) {
            MSPage<B2BWarrantyMapping> data = returnWarrantyMapping.getData();
            warrantyMappingPage.setCount(data.getRowCount());
            warrantyMappingPage.setList(data.getList());
        }else{
            warrantyMappingPage.setCount(0);
            warrantyMappingPage.setList(Lists.newArrayList());
        }
        return warrantyMappingPage;
    }

    /**
     * 根据id获取
     * @param id
     * @return
     */
    public B2BWarrantyMapping getById(Long id) {
        MSResponse<B2BWarrantyMapping> msResponse = warrantyMappingFeign.getById(id);
        if(MSResponse.isSuccess(msResponse)){
            return msResponse.getData();
        }else{
            return new B2BWarrantyMapping();
        }
    }
}
