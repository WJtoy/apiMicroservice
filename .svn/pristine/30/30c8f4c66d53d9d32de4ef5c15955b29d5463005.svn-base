package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.MDMaterial;
import com.kkl.kklplus.entity.md.MDProductMaterial;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.ProductMaterial;
import com.wolfking.jeesite.ms.providermd.feign.MSMaterialFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MSMaterialService {

    @Autowired
    private MSMaterialFeign msMaterialFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 根据id获取配件信息
     * @param id
     * @return
     */
    public Material getById(Long id) {
        return MDUtils.getById(id, Material.class, msMaterialFeign::getById);
    }

    /**
     * 获取所有数据
     * @return
     */
    public List<Material> findAllList() {
        return MDUtils.findAllList(Material.class, msMaterialFeign::findAllList);
    }


    /**
     * 根据产品Id获取产品配件列表
     * @param productId
     * @return
     */
    public List<ProductMaterial> findMaterialIdByProductId(Long productId){
        MSResponse<List<MDProductMaterial>> msResponse = msMaterialFeign.findMaterialIdByProductId(productId);
        return findProductMaterial(msResponse);
    }

    private List<ProductMaterial> findProductMaterial(MSResponse<List<MDProductMaterial>> msResponse){
        if(MSResponse.isSuccess(msResponse)){
            List<MDProductMaterial> mdProductMaterialList = msResponse.getData();
            List<ProductMaterial> productMaterialList = mapper.mapAsList(mdProductMaterialList,ProductMaterial.class);
            return productMaterialList;
        }else{
            return Lists.newArrayList();
        }
    }

    /**
     * list配件集合转map
     * @return
     */
    public Map<Long,Material> findAllMaterialMap(){
        List<Material> list= findAllList();
        Map<Long,Material> map =new HashMap<>();
        if(list!=null && list.size()>0){
            map = list.stream().distinct().collect(Collectors.toMap(Material::getId, material -> material));
        }
        return map;
    }

}
