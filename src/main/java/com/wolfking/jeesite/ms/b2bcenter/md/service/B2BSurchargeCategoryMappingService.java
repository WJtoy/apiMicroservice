package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeCategoryMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialCategory;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeCategoryMappingVModel;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BSurchargeCategoryMappingFeign;
import com.wolfking.jeesite.ms.b2bcenter.md.mapper.B2BSurchargeCategoryMappingMapper;
import com.wolfking.jeesite.ms.providermd.service.AuxiliaryMaterialCategoryService;
import org.mapstruct.factory.Mappers;
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
public class B2BSurchargeCategoryMappingService {

    @Autowired
    private B2BSurchargeCategoryMappingFeign surchargeCategoryMappingFeign;

    @Autowired
    private AuxiliaryMaterialCategoryService auxiliaryMaterialCategoryService;

    /**
     * 分页查询
     *
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<B2BSurchargeCategoryMappingVModel> getList(Page page, B2BSurchargeCategoryMapping surchargeCategoryMapping){
        MSPage<B2BSurchargeCategoryMapping> msPage = new MSPage<>(page.getPageNo(), page.getPageSize());
        surchargeCategoryMapping.setPage(msPage);
        MSResponse<MSPage<B2BSurchargeCategoryMapping>> returnSurchargeCategoryMapping = surchargeCategoryMappingFeign.getList(surchargeCategoryMapping);
        Page<B2BSurchargeCategoryMappingVModel> returnPage = new Page<>(page.getPageNo(), page.getPageSize());
        if (MSResponse.isSuccess(returnSurchargeCategoryMapping)) {
            MSPage<B2BSurchargeCategoryMapping> data = returnSurchargeCategoryMapping.getData();
            returnPage.setCount(data.getRowCount());
            List<B2BSurchargeCategoryMappingVModel> returnList = toB2BSurchargeCategoryMappingVModels(data.getList());
            returnPage.setList(returnList);
        }else{
            throw new RuntimeException(returnSurchargeCategoryMapping.getMsg());
        }
        return returnPage;
    }

    /**
     *  surchargeCategoryMappings 转换成 B2BSurchargeCategoryMappingVModel
     **/
    protected List<B2BSurchargeCategoryMappingVModel> toB2BSurchargeCategoryMappingVModels(List<B2BSurchargeCategoryMapping> surchargeCategoryMappings){
        List<B2BSurchargeCategoryMappingVModel> list = Lists.newArrayList();
        if(surchargeCategoryMappings!=null && !surchargeCategoryMappings.isEmpty()){
            B2BSurchargeCategoryMappingVModel surchargeCategoryMappingVModel;
            List<MDAuxiliaryMaterialCategory> auxiliaryMaterialCategoryList = auxiliaryMaterialCategoryService.findAllList();
            Map<Long,MDAuxiliaryMaterialCategory> map = Maps.newHashMap();
            if(auxiliaryMaterialCategoryList!=null && !auxiliaryMaterialCategoryList.isEmpty()){
                 map = auxiliaryMaterialCategoryList.stream().collect(Collectors.toMap(MDAuxiliaryMaterialCategory::getId, auxiliaryMaterialCategory -> auxiliaryMaterialCategory));
            }
            MDAuxiliaryMaterialCategory auxiliaryMaterialCategory;
            for(B2BSurchargeCategoryMapping item:surchargeCategoryMappings){
                surchargeCategoryMappingVModel = Mappers.getMapper(B2BSurchargeCategoryMappingMapper.class).toB2BSurchargeCategoryMappingVModel(item);
                if(surchargeCategoryMappingVModel!=null){
                    auxiliaryMaterialCategory = map.get(surchargeCategoryMappingVModel.getAuxiliaryMaterialCategoryId());
                    if(auxiliaryMaterialCategory!=null){
                        surchargeCategoryMappingVModel.setAuxiliaryMaterialCategoryName(auxiliaryMaterialCategory.getName());
                    }
                    list.add(surchargeCategoryMappingVModel);
                }
            }
        }
        return list;
    }

    /**
     * 根据数据源获取B2B附加费分类信息
     *
     * @param dataSource
     * @return
     */
    public List<B2BSurchargeCategoryMapping> getListByDataSource(Integer dataSource){
        List<B2BSurchargeCategoryMapping> list = null;
        MSResponse<List<B2BSurchargeCategoryMapping>> msResponse = surchargeCategoryMappingFeign.getListByDataSource(dataSource);
        if(MSResponse.isSuccess(msResponse)){
            list = msResponse.getData();
            return list;
        }else{
            list = new ArrayList<>();
            return list;
        }
    }

    /**
     * 保存或修改B2B附加费分类信息
     *
     * @param surchargeCategoryMapping
     * @return
     */
      public void save(B2BSurchargeCategoryMapping surchargeCategoryMapping){
          if(surchargeCategoryMapping.getId()==null || surchargeCategoryMapping.getId()<=0){
              surchargeCategoryMapping.preInsert();
              MSResponse<B2BSurchargeCategoryMapping> msResponse = surchargeCategoryMappingFeign.insert(surchargeCategoryMapping);
              if(MSResponse.isSuccess(msResponse)){
                  surchargeCategoryMapping.setId(msResponse.getData().getId());
              }else{
                  throw new RuntimeException(msResponse.getMsg());
              }
          }else{
              surchargeCategoryMapping.preUpdate();
              MSResponse<Integer> msResponse = surchargeCategoryMappingFeign.update(surchargeCategoryMapping);
              if(!MSResponse.isSuccess(msResponse)){
                  throw new BaseException(msResponse.getMsg());
              }
          }
      }

    /**
     * 删除B2B附加费分类信息
     *
     * @param surchargeCategoryMapping
     * @return
     */
     public void delete(B2BSurchargeCategoryMapping surchargeCategoryMapping){
         surchargeCategoryMapping.preUpdate();
         MSResponse<Integer> msResponse = surchargeCategoryMappingFeign.delete(surchargeCategoryMapping);
         if(!MSResponse.isSuccess(msResponse)){
             throw new BaseException(msResponse.getMsg());
         }
     }
}
