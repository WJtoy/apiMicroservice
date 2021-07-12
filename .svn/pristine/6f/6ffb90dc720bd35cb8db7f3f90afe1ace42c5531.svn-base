package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BCustomerCategory;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BCustomerCategoryFeign;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCustomerCategoryService{
    @Autowired
    private B2BCustomerCategoryFeign customerCategoryFeign;

    /**
     * 分页查询
     *
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<B2BCustomerCategory> getList(Page<B2BCustomerCategory> page,B2BCustomerCategory customerCategory){
        if (customerCategory.getPage() == null) {
            MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
        }
        Page<B2BCustomerCategory> customerCategoryPage = new Page<>();
        customerCategoryPage.setPageSize(page.getPageSize());
        customerCategoryPage.setPageNo(page.getPageNo());
        customerCategory.setPage(new MSPage<>(customerCategoryPage.getPageNo(), customerCategoryPage.getPageSize()));
        MSResponse<MSPage<B2BCustomerCategory>> returnCustomerCategory = customerCategoryFeign.getList(customerCategory);
        if (MSResponse.isSuccess(returnCustomerCategory)) {
            MSPage<B2BCustomerCategory> data = returnCustomerCategory.getData();
            customerCategoryPage.setCount(data.getRowCount());
            customerCategoryPage.setList(data.getList());
        }else{
            customerCategoryPage.setCount(0);
            customerCategoryPage.setList(Lists.newArrayList());
        }
        return customerCategoryPage;
    }

    /**
     * 根据数据源获取客户料号
     *
     * @param dataSource
     * @return
     */
    public List<B2BCustomerCategory> getListByDataSource(Integer dataSource){
        List<B2BCustomerCategory> list = null;
        MSResponse<List<B2BCustomerCategory>> msResponse = customerCategoryFeign.getListByDataSource(dataSource);
        if(MSResponse.isSuccess(msResponse)){
            list = msResponse.getData();
            return list;
        }else{
            list = new ArrayList<>();
            return list;
        }
    }

    /**
     * 保存或修改
     *
     * @param customerCategory
     * @return
     */
      public void save(B2BCustomerCategory customerCategory){
          if(customerCategory.getId()==null || customerCategory.getId()<=0){
              MSResponse<B2BCustomerCategory> msResponse = customerCategoryFeign.insert(customerCategory);
              if(MSResponse.isSuccess(msResponse)){
                  customerCategory.setId(msResponse.getData().getId());
              }else{
                  throw new RuntimeException(msResponse.getMsg());
              }
          }else{
              MSResponse<Integer> msResponse = customerCategoryFeign.update(customerCategory);
              if(!MSResponse.isSuccess(msResponse)){
                  throw new BaseException(msResponse.getMsg());
              }
          }
      }

    /**
     * 删除
     *
     * @param customerCategory
     * @return
     */
     public void delete(B2BCustomerCategory customerCategory){
         MSResponse<Integer> msResponse = customerCategoryFeign.delete(customerCategory);
         if(!MSResponse.isSuccess(msResponse)){
             throw new BaseException(msResponse.getMsg());
         }
     }
}
