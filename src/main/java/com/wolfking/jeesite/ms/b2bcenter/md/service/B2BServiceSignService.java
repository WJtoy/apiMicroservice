package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSign;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCustomerMappingFeign;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceSignFeign;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BServiceSignService {

    @Autowired
    private B2BCustomerMappingFeign customerMappingFeign;

    @Autowired
    private B2BServiceSignFeign b2BServiceSignFeign;

    /**
     * 分页查询
     *
     * @param page,b2BSign
     * @return
     */
    public Page<B2BSign> getList(Page<B2BSign> page, B2BSign b2BSign) {
        if (b2BSign.getPage() == null) {
            PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BSign> b2BSignPage = new Page<>();
        b2BSignPage.setPageSize(page.getPageSize());
        b2BSignPage.setPageNo(page.getPageNo());
        b2BSign.setPage(new MSPage<>(b2BSignPage.getPageNo(), b2BSignPage.getPageSize()));
        if(b2BSign.getSignStatus() == null){
            b2BSign.setSignStatus(0);
        }
        MSResponse<MSPage<B2BSign>> msResponse = b2BServiceSignFeign.getServiceSignList(b2BSign);
        if (MSResponse.isSuccess(msResponse)) {
            MSPage<B2BSign> data = msResponse.getData();
            b2BSignPage.setCount(data.getRowCount());
            for(B2BSign entity:data.getList()){
                entity.setApplyDate(new Date(entity.getApplyTime()));
                entity.setDataSource(16);
            }
            b2BSignPage.setList(data.getList());

        }
        return b2BSignPage;
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    public MSResponse<B2BSign> getById(Long id) {
        return b2BServiceSignFeign.getServiceSignById(id);
    }

    public MSResponse<Boolean> audit(B2BSign b2BSign){
        return b2BServiceSignFeign.getServiceSignAudit(b2BSign);
    }

    /**
     * 根据shopId,dataSource获取数据
     *
     * @param shopId,dataSource
     * @return
     */
    public MSResponse<Long> checkShopId(Long shopId, Integer dataSource) {
        return customerMappingFeign.getByShopId(shopId.toString(), dataSource);
    }
}
