package com.wolfking.jeesite.ms.providermd.service;

import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.ms.providermd.feign.MSProductPicMappingFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSProductPicMappingService {
    @Autowired
    private MSProductPicMappingFeign msProductPicMappingFeign;

    /**
     * 根据id获取完工图片
     *
     * @param productId
     * @return
     */
    public ProductCompletePic getByProductId(Long productId) {
        return MDUtils.getById(productId, ProductCompletePic.class, msProductPicMappingFeign::getByProductId);
    }

    /**
     * 获取所有的完工图片
     * @return
     */
    public List<ProductCompletePic> findAllList() {
        return MDUtils.findAllList(ProductCompletePic.class, msProductPicMappingFeign::findAllList);
    }

}
