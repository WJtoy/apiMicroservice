package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceTypeMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.UserUtils;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceTypeMappingFeign;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BServiceTypeMappingService {

    @Autowired
    private B2BServiceTypeMappingFeign serviceTypeMappingFeign;

    @Autowired
    private MicroServicesProperties msProperties;

    /**
     * 查询数据源中所有B2B服务类型与工单服务类型的映射关系
     *
     * @param dataSource B2BDataSourceEnum
     * @return
     */
    public List<B2BServiceTypeMapping> getListByDataSource(B2BDataSourceEnum dataSource) {
        List<B2BServiceTypeMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<List<B2BServiceTypeMapping>> responseEntity = serviceTypeMappingFeign.getListByDataSource(dataSource.id);
            if (MSResponse.isSuccess(responseEntity)) {
                list = responseEntity.getData();
            } else {
                list = Collections.emptyList();
            }
        }
        return list;
    }


    /**
     * 分页查询
     *
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<B2BServiceTypeMapping> getList(Page<B2BServiceTypeMapping> page, B2BServiceTypeMapping b2BServiceTypeMapping) {
        if (b2BServiceTypeMapping.getPage() == null) {
            MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BServiceTypeMapping> b2BServiceTypeMappingPage = new Page<>();
        b2BServiceTypeMappingPage.setPageSize(page.getPageSize());
        b2BServiceTypeMappingPage.setPageNo(page.getPageNo());
        b2BServiceTypeMapping.setPage(new MSPage<>(b2BServiceTypeMappingPage.getPageNo(), b2BServiceTypeMappingPage.getPageSize()));
        MSResponse<MSPage<B2BServiceTypeMapping>> returnB2BServiceTypeMapping = serviceTypeMappingFeign.getServiceTypeMappingList(b2BServiceTypeMapping);
        if (MSResponse.isSuccess(returnB2BServiceTypeMapping)) {
            MSPage<B2BServiceTypeMapping> data = returnB2BServiceTypeMapping.getData();
            b2BServiceTypeMappingPage.setCount(data.getRowCount());
            b2BServiceTypeMappingPage.setList(data.getList());
        }
        return b2BServiceTypeMappingPage;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public MSResponse<B2BServiceTypeMapping> getById(Long id) {
        return serviceTypeMappingFeign.getServiceTypeMappingById(id);
    }

    /**
     * 根据保修类型,数据源,服务类型名称查询
     *
     * @param b2BServiceTypeMapping
     * @return
     */
    public MSResponse<Long> checkIsExist(B2BServiceTypeMapping b2BServiceTypeMapping) {
        return serviceTypeMappingFeign.getByField(b2BServiceTypeMapping);
    }

}
