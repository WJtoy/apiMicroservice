package com.wolfking.jeesite.ms.providermd.service;


import com.kkl.kklplus.entity.md.MDProduct;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.ms.providermd.feign.MSProductFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MSProductService {
    @Autowired
    private MSProductFeign msProductFeign;

    /**
     * 获取所有的产品数据
     *
     * @return
     */
    public List<Product> findAllList() {
        return MDUtils.findAllList(Product.class, msProductFeign::findAllList);
    }

    /**
     * 根据产品类别id获取单品产品列表
     *
     * @param productCategoryId
     * @return
     */
    public List<Product> findSingleListByProductCategoryId(Long productCategoryId) {
        //return MDUtils.findListByCustomCondition(productCategoryId, Product.class, msProductFeign::findListByProductCategoryId);
        return MDUtils.findAllList(Product.class, () -> msProductFeign.findSingleListByProductCategoryId(productCategoryId));
    }

    /**
     * 根据条件获取产品列表数据
     *
     * @param product
     * @return id, name, set_flag, sort, product_category_id
     */
    public List<Product> findListByConditions(Product product) {
        return MDUtils.findList(product, Product.class, MDProduct.class, msProductFeign::findListByConditions);
    }

    /**
     * 根据id从缓存获取产品信息
     *
     * @param id
     * @return
     */
    public Product getProductByIdFromCache(Long id) {
        return MDUtils.getById(id, Product.class, msProductFeign::getProductByIdFromCache);
    }

    /**
     * 根据Id集合从缓存获取产品集合
     *
     * @param ids
     * @return
     */
    public List<Product> findProductByIdListFromCache(List<Long> ids) {
        return MDUtils.findAllList(Product.class, () -> msProductFeign.findProductByIdListFromCache(ids));
    }


}
