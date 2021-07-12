package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceFeeCategory;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceFeeCategoryFeign;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BServiceFeeCategoryService {
    @Autowired
    private B2BServiceFeeCategoryFeign serviceFeeCategoryFeign;

    @Autowired
    private MicroServicesProperties msProperties;


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public B2BServiceFeeCategory get(Long id){
        if(msProperties.getB2bcenter().getEnabled()){
            MSResponse<B2BServiceFeeCategory> msResponse = serviceFeeCategoryFeign.get(id);
            if(MSResponse.isSuccess(msResponse)){
                B2BServiceFeeCategory serviceFeeCategory = msResponse.getData();
                return serviceFeeCategory;
            }else{
                return new B2BServiceFeeCategory();
            }
        }else{
            return new B2BServiceFeeCategory();
        }
    }

    /**
     * 分页查询
     *
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<B2BServiceFeeCategory> getList(Page<B2BServiceFeeCategory> page, B2BServiceFeeCategory serviceFeeCategory){
        Page<B2BServiceFeeCategory> serviceFeeCategoryPage = new Page<>();
        if(msProperties.getB2bcenter().getEnabled()) {
            if (serviceFeeCategory.getPage() == null) {
                MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
            }
            serviceFeeCategoryPage.setPageSize(page.getPageSize());
            serviceFeeCategoryPage.setPageNo(page.getPageNo());
            serviceFeeCategory.setPage(new MSPage<>(serviceFeeCategoryPage.getPageNo(), serviceFeeCategoryPage.getPageSize()));
            MSResponse<MSPage<B2BServiceFeeCategory>> returnServiceFeeCategory = serviceFeeCategoryFeign.getList(serviceFeeCategory);
            if (MSResponse.isSuccess(returnServiceFeeCategory)) {
                MSPage<B2BServiceFeeCategory> data = returnServiceFeeCategory.getData();
                serviceFeeCategoryPage.setCount(data.getRowCount());
                serviceFeeCategoryPage.setList(data.getList());
            } else {
                serviceFeeCategoryPage.setCount(0);
                serviceFeeCategoryPage.setList(Lists.newArrayList());
            }
            return serviceFeeCategoryPage;
        }else{
            serviceFeeCategoryPage.setCount(0);
            serviceFeeCategoryPage.setList(Lists.newArrayList());
            return serviceFeeCategoryPage;
        }
    }

    /**
     * 根据数据源获取
     * @param  dataSource
     * @return
     */
     public List<B2BServiceFeeCategory> getListByDataSource(Integer dataSource){
         if(msProperties.getB2bcenter().getEnabled()) {
             MSResponse<List<B2BServiceFeeCategory>> msResponse = serviceFeeCategoryFeign.getListByDataSource(dataSource);
             if (MSResponse.isSuccess(msResponse)) {
                 return msResponse.getData();
             } else {
                 return Lists.newArrayList();
             }
         }else{
             return Lists.newArrayList();
         }
     }


    /**
     * 保存或修改
     *
     * @param serviceFeeCategory
     * @return
     */
      public void save(B2BServiceFeeCategory serviceFeeCategory){
          if(msProperties.getB2bcenter().getEnabled()) {
              if (serviceFeeCategory.getId() == null || serviceFeeCategory.getId() <= 0) {
                  MSResponse<B2BServiceFeeCategory> msResponse = serviceFeeCategoryFeign.insert(serviceFeeCategory);
                  if (MSResponse.isSuccess(msResponse)) {
                      serviceFeeCategory.setId(msResponse.getData().getId());
                  } else {
                      throw new RuntimeException(msResponse.getMsg());
                  }
              } else {
                  MSResponse<Integer> msResponse = serviceFeeCategoryFeign.update(serviceFeeCategory);
                  if (!MSResponse.isSuccess(msResponse)) {
                      throw new BaseException(msResponse.getMsg());
                  }
              }
          }
      }

    /**
     * 删除
     *
     * @param serviceFeeCategory
     * @return
     */
     public void delete(B2BServiceFeeCategory serviceFeeCategory) {
         if (msProperties.getB2bcenter().getEnabled()) {
             MSResponse<Integer> msResponse = serviceFeeCategoryFeign.delete(serviceFeeCategory);
             if (!MSResponse.isSuccess(msResponse)) {
                 throw new BaseException(msResponse.getMsg());
             }
         }
     }
}
