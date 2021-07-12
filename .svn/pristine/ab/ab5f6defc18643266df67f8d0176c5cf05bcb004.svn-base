package com.wolfking.jeesite.ms.b2bcenter.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.common.exception.BaseException;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDAuxiliaryMaterialItem;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.b2bcenter.md.entity.B2BSurchargeItemMappingVModel;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BSurchargeItemMappingFeign;
import com.wolfking.jeesite.ms.b2bcenter.md.mapper.B2BSurchargeItemMappingMapper;
import com.wolfking.jeesite.ms.providermd.service.AuxiliaryMaterialItemService;
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
public class B2BSurchargeItemMappingService {

    @Autowired
    private B2BSurchargeItemMappingFeign surchargeItemMappingFeign;

    @Autowired
    private AuxiliaryMaterialItemService auxiliaryMaterialItemService;

    /**
     * 分页查询
     *
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<B2BSurchargeItemMappingVModel> getList(Page page, B2BSurchargeItemMapping surchargeItemMapping){
        MSPage<B2BSurchargeItemMapping> msPage = new MSPage<>(page.getPageNo(), page.getPageSize());
        surchargeItemMapping.setPage(msPage);
        MSResponse<MSPage<B2BSurchargeItemMapping>> returnSurchargeItemMapping = surchargeItemMappingFeign.getList(surchargeItemMapping);
        Page<B2BSurchargeItemMappingVModel> returnPage = new Page<>(page.getPageNo(), page.getPageSize());
        if (MSResponse.isSuccess(returnSurchargeItemMapping)) {
            MSPage<B2BSurchargeItemMapping> data = returnSurchargeItemMapping.getData();
            returnPage.setCount(data.getRowCount());
            List<B2BSurchargeItemMappingVModel> returnList = toB2BSurchargeItemMappingVModels(data.getList());
            returnPage.setList(returnList);
        }else{
            throw new RuntimeException(returnSurchargeItemMapping.getMsg());
        }
        return returnPage;
    }

    /**
     *  B2BSurchargeItemMapping 转成 B2BSurchargeItemMappingVModel
     */
    protected List<B2BSurchargeItemMappingVModel> toB2BSurchargeItemMappingVModels(List<B2BSurchargeItemMapping> surchargeItemMappings){
        List<B2BSurchargeItemMappingVModel> list = Lists.newArrayList();
        if(surchargeItemMappings!=null && !surchargeItemMappings.isEmpty()){
            B2BSurchargeItemMappingVModel surchargeItemMappingVModel;
            List<MDAuxiliaryMaterialItem> auxiliaryMaterialItemList = auxiliaryMaterialItemService.findAllList();
            MDAuxiliaryMaterialItem auxiliaryMaterialItem;
            Map<Long,MDAuxiliaryMaterialItem> map = Maps.newHashMap();
            if(auxiliaryMaterialItemList!=null && !auxiliaryMaterialItemList.isEmpty()){
                map = auxiliaryMaterialItemList.stream().collect(Collectors.toMap(MDAuxiliaryMaterialItem::getId, t -> t));
            }
            for(B2BSurchargeItemMapping item:surchargeItemMappings){
                surchargeItemMappingVModel = Mappers.getMapper(B2BSurchargeItemMappingMapper.class).toB2BSurchargeItemMappingVModel(item);
                if(surchargeItemMappingVModel!=null){
                    auxiliaryMaterialItem = map.get(surchargeItemMappingVModel.getAuxiliaryMaterialItemId());
                    if(auxiliaryMaterialItem!=null){
                        surchargeItemMappingVModel.setAuxiliaryMaterialItemName(auxiliaryMaterialItem.getName());
                    }
                    list.add(surchargeItemMappingVModel);
                }
            }
        }
        return list;
    }

    /**
     * 根据数据源获取B2B附加费项目信息
     *
     * @param dataSource
     * @return
     */
    public List<B2BSurchargeItemMapping> getListByDataSource(Integer dataSource){
        List<B2BSurchargeItemMapping> list = null;
        MSResponse<List<B2BSurchargeItemMapping>> msResponse = surchargeItemMappingFeign.getListByDataSource(dataSource);
        if(MSResponse.isSuccess(msResponse)){
            list = msResponse.getData();
            return list;
        }else{
            list = new ArrayList<>();
            return list;
        }
    }

    /**
     * 保存或修改B2B附加费项目信息
     *
     * @param surchargeItemMapping
     * @return
     */
      public void save(B2BSurchargeItemMapping surchargeItemMapping){
          if(surchargeItemMapping.getId()==null || surchargeItemMapping.getId()<=0){
              surchargeItemMapping.preInsert();
              MSResponse<B2BSurchargeItemMapping> msResponse = surchargeItemMappingFeign.insert(surchargeItemMapping);
              if(MSResponse.isSuccess(msResponse)){
                  surchargeItemMapping.setId(msResponse.getData().getId());
              }else{
                  throw new RuntimeException(msResponse.getMsg());
              }
          }else{
              surchargeItemMapping.preUpdate();
              MSResponse<Integer> msResponse = surchargeItemMappingFeign.update(surchargeItemMapping);
              if(!MSResponse.isSuccess(msResponse)){
                  throw new BaseException(msResponse.getMsg());
              }
          }
      }

    /**
     * 删除B2B附加费项目信息
     *
     * @param surchargeCategoryMapping
     * @return
     */
     public void delete(B2BSurchargeItemMapping surchargeCategoryMapping){
         surchargeCategoryMapping.preUpdate();
         MSResponse<Integer> msResponse = surchargeItemMappingFeign.delete(surchargeCategoryMapping);
         if(!MSResponse.isSuccess(msResponse)){
             throw new BaseException(msResponse.getMsg());
         }
     }
}
