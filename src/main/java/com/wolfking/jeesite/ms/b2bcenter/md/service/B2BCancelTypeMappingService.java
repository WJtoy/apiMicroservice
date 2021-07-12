package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCancelTypeMapping;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCancelTypeMappingFeign;
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
public class B2BCancelTypeMappingService {

    @Autowired
    private B2BCancelTypeMappingFeign cancelTypeMappingFeign;

    @Autowired
    private MicroServicesProperties msProperties;

    /**
     * 根据数据源来查询第三方的取消类型
     */
    public List<B2BCancelTypeMapping> getListByDataSource(B2BDataSourceEnum dataSource) {
        List<B2BCancelTypeMapping> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<List<B2BCancelTypeMapping>> responseEntity = cancelTypeMappingFeign.getListByDataSource(dataSource.id);
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
    public Page<B2BCancelTypeMapping> getList(Page<B2BCancelTypeMapping> page, B2BCancelTypeMapping cancelTypeMapping) {
        if (cancelTypeMapping.getPage() == null) {
            MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BCancelTypeMapping> b2bCancelTypeMappingPage = new Page<>();
        b2bCancelTypeMappingPage.setPageSize(page.getPageSize());
        b2bCancelTypeMappingPage.setPageNo(page.getPageNo());
        cancelTypeMapping.setPage(new MSPage<>(b2bCancelTypeMappingPage.getPageNo(), b2bCancelTypeMappingPage.getPageSize()));
        MSResponse<MSPage<B2BCancelTypeMapping>> returnB2BServiceTypeMapping = cancelTypeMappingFeign.getCancelTypeMappingList(cancelTypeMapping);
        if (MSResponse.isSuccess(returnB2BServiceTypeMapping)) {
            MSPage<B2BCancelTypeMapping> data = returnB2BServiceTypeMapping.getData();
            b2bCancelTypeMappingPage.setCount(data.getRowCount());
            b2bCancelTypeMappingPage.setList(data.getList());
        }
        return b2bCancelTypeMappingPage;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public MSResponse<B2BCancelTypeMapping> getById(Long id) {
        return cancelTypeMappingFeign.getCancelTypeMappingById(id);
    }
}
