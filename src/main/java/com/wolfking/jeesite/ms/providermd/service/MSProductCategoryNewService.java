package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.ms.providermd.feign.MSProductCategoryNewFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MSProductCategoryNewService {
    @Autowired
    private MSProductCategoryNewFeign msProductCategoryNewFeign;

    /**
     * 获取全部产品类别-->基础资料
     *
     * @return id, name
     */
    public List<NameValuePair<Long, String>> findAllListForMD() {
        return MDUtils.findListUnnecessaryConvertType(() -> msProductCategoryNewFeign.findAllListForMD());
    }

    /**
     * 用实体类形式获取全部产品类别-->基础资料
     *
     * @return id, name
     */
    public List<ProductCategory> findAllListForMDWithEntity() {
        return convertToProductCategoryList(findAllListForMD());
    }


    /**
     * 批量获取获取产品类别-->基础资料
     *
     * @param ids
     * @return id, name
     */
    public List<NameValuePair<Long, String>> findListByIdsForMD(List<Long> ids) {
        return MDUtils.findListUnnecessaryConvertType(() -> msProductCategoryNewFeign.findListByIdsForMD(ids));
    }

    /**
     * 批量获取获取产品类别-->基础资料
     *
     * @param ids
     * @return id, name
     */
    public List<ProductCategory> findListByIdsForMDWithEntity(List<Long> ids) {
        return convertToProductCategoryList(findListByIdsForMD(ids));
    }

    /**
     * 根据ID获取产品类别-->基础资料
     *
     * @param id
     * @return id，code,name,del_flag,remarks
     */
    public ProductCategory getByIdForMD(Long id) {
        return MDUtils.getObjNecessaryConvertType(ProductCategory.class, ()->msProductCategoryNewFeign.getByIdForMD(id));
    }

    /**
     * 根据ID从缓存读取-->基础资料
     *
     * @param id
     * @return id, name
     */
    public String getFromCacheForMD(Long id) {
        return MDUtils.getObjUnnecessaryConvertType(() -> msProductCategoryNewFeign.getFromCacheForMD(id));
    }

    /**
     * 用实体类形式根据ID从缓存读取-->基础资料
     *
     * @param id
     * @return id, name
     */
    public ProductCategory getFromCacheForMDWithEntity(Long id) {
        String name = getFromCacheForMD(id);
        ProductCategory productCategory = new ProductCategory(id);
        productCategory.setName(name);
        return productCategory;
    }


    /**
     * 将NameValue列表转换为品类列表
     *
     * @param nameValuePairList
     * @return
     */
    private List<ProductCategory> convertToProductCategoryList(List<NameValuePair<Long, String>> nameValuePairList) {
        if (nameValuePairList != null && !nameValuePairList.isEmpty()) {
            return nameValuePairList.stream().map(nv -> {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setId(nv.getName());
                productCategory.setName(nv.getValue());
                return productCategory;
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }
}
