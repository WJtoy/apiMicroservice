package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BServiceFeeItem;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BServiceFeeItemFeign;
import com.wolfking.jeesite.ms.common.config.MicroServicesProperties;
import com.wolfking.jeesite.ms.mapper.common.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BServiceFeeItemService {
    @Autowired
    private B2BServiceFeeItemFeign serviceFeeItemFeign;

    @Autowired
    private MicroServicesProperties msProperties;


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public B2BServiceFeeItem get(Long id){
        B2BServiceFeeItem serviceFeeItem = new B2BServiceFeeItem();
        if(msProperties.getB2bcenter().getEnabled()) {
            MSResponse<B2BServiceFeeItem> msResponse = serviceFeeItemFeign.get(id);
            if (MSResponse.isSuccess(msResponse)) {
                 serviceFeeItem = msResponse.getData();
                return serviceFeeItem;
            } else {
                return serviceFeeItem;
            }
        }else{
            return serviceFeeItem;
        }
    }

    /**
     * 分页查询
     *
     * @param page,serviceFeeItem
     * @return
     */
    public Page<B2BServiceFeeItem> getList(Page<B2BServiceFeeItem> page, B2BServiceFeeItem serviceFeeItem){
        Page<B2BServiceFeeItem> serviceFeeItemPage = new Page<>();
        if(msProperties.getB2bcenter().getEnabled()) {
            if (serviceFeeItem.getPage() == null) {
                MSPage msPage = PageMapper.INSTANCE.toMSPage(page);
            }
            serviceFeeItemPage.setPageSize(page.getPageSize());
            serviceFeeItemPage.setPageNo(page.getPageNo());
            serviceFeeItem.setPage(new MSPage<>(serviceFeeItemPage.getPageNo(), serviceFeeItemPage.getPageSize()));
            MSResponse<MSPage<B2BServiceFeeItem>> returnServiceFeeItem = serviceFeeItemFeign.getList(serviceFeeItem);
            if (MSResponse.isSuccess(returnServiceFeeItem)) {
                MSPage<B2BServiceFeeItem> data = returnServiceFeeItem.getData();
                serviceFeeItemPage.setCount(data.getRowCount());
                serviceFeeItemPage.setList(data.getList());
            } else {
                serviceFeeItemPage.setCount(0);
                serviceFeeItemPage.setList(Lists.newArrayList());
            }
            return serviceFeeItemPage;
        }else{
            serviceFeeItemPage.setCount(0);
            serviceFeeItemPage.setList(Lists.newArrayList());
            return serviceFeeItemPage;
        }
    }


    /**
     * 保存或修改
     *
     * @param serviceFeeItem
     * @return
     */
      public void save(B2BServiceFeeItem serviceFeeItem){
          if(msProperties.getB2bcenter().getEnabled()) {
              if (serviceFeeItem.getId() == null || serviceFeeItem.getId() <= 0) {
                  MSResponse<B2BServiceFeeItem> msResponse = serviceFeeItemFeign.insert(serviceFeeItem);
                  if (MSResponse.isSuccess(msResponse)) {
                      serviceFeeItem.setId(msResponse.getData().getId());
                  } else {
                      throw new RuntimeException(msResponse.getMsg());
                  }
              } else {
                  MSResponse<Integer> msResponse = serviceFeeItemFeign.update(serviceFeeItem);
                  if (!MSResponse.isSuccess(msResponse)) {
                      throw new BaseException(msResponse.getMsg());
                  }
              }
          }
      }

    /**
     * 删除
     *
     * @param serviceFeeItem
     * @return
     */
     public void delete(B2BServiceFeeItem serviceFeeItem){
         if(msProperties.getB2bcenter().getEnabled()) {
             MSResponse<Integer> msResponse = serviceFeeItemFeign.delete(serviceFeeItem);
             if (!MSResponse.isSuccess(msResponse)) {
                 throw new BaseException(msResponse.getMsg());
             }
         }
     }

     /**
      * 根据数据源获取
      * @param  dataSource
      * @return
      */
    public List<B2BServiceFeeItem> getListByDataSource(Integer dataSource) {
        List<B2BServiceFeeItem> list = Lists.newArrayList();
        if (msProperties.getB2bcenter().getEnabled()) {
            MSResponse<List<B2BServiceFeeItem>> msResponse = serviceFeeItemFeign.getListByDataSource(dataSource);
            if (MSResponse.isSuccess(msResponse)) {
                list = msResponse.getData();
                return list;
            } else {
                return list;
            }
        }else{
            return list;
        }
    }
}
