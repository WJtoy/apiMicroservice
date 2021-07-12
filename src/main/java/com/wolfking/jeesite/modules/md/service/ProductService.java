package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.persistence.LongIDBaseEntity;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.api.entity.common.RestCommonIds;
import com.wolfking.jeesite.modules.api.entity.md.RestMaterial;
import com.wolfking.jeesite.modules.api.entity.md.RestProduct;
import com.wolfking.jeesite.modules.api.util.RestResult;
import com.wolfking.jeesite.modules.api.util.RestResultGenerator;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.ms.providermd.service.MSProductCategoryNewService;
import com.wolfking.jeesite.ms.providermd.service.MSProductService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
public class ProductService extends LongIDBaseService {
    @SuppressWarnings("rawtypes")
    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private MSProductCategoryNewService msProductCategoryNewService;

    @Autowired
    private MSProductService msProductService;

    @Autowired
    private MaterialService materialService;

    /**
     * 拆分套组产品，返回组成产品列表
     *
     * @param id 套组产品id
     */
    public List<Product> getProductListOfSet(Long id) {
        List<Product> list = Lists.newArrayList();
        if (id == null || id <= 0) {
            return list;
        }
        Product pset = getProductByIdFromCache(id);
        if (pset == null || StringUtils.isBlank(pset.getProductIds())) {
            return list;
        }
        String[] ids = pset.getProductIds().split(",");
        String pid = new String("");
        for (int i = 0, size = ids.length; i < size; i++) {
            pid = ids[i];
            if (StringUtils.isBlank(pid)) {
                continue;
            }
            Product p = getProductByIdFromCache(Long.valueOf(pid));
            if (p != null) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * 根据产品ID获取配件列表
     *
     * @param productId
     * @return
     */
    public List<RestMaterial> getMaterialByProductId(Long productId) {
        List<RestMaterial> restMaterialList = Lists.newArrayList();
        List<Material> materialList = materialService.getMaterialListByProductId(productId);
        if (materialList != null && materialList.size() > 0) {
            restMaterialList = mapper.mapAsList(materialList, RestMaterial.class);
        }
        return restMaterialList;
    }

    public List<RestMaterial> getMaterialByProductId(Long customerId, Long productId, String customerModel) {
        List<RestMaterial> restMaterialList = Lists.newArrayList();
        List<Material> materialList = materialService.findMaterialsByProductIdMS(customerId, productId, customerModel);
        if (materialList != null && materialList.size() > 0) {
            restMaterialList = mapper.mapAsList(materialList, RestMaterial.class);
        }
        return restMaterialList;
    }


    //region redis操作

    /**
     * 从缓存读取产品信息，当缓存未命中则从数据库装载至缓存
     *
     * @param id
     * @return
     */
    public Product getProductByIdFromCache(Long id) {
        Product product = msProductService.getProductByIdFromCache(id);
        if (product != null && product.getCategory() != null && product.getCategory().getId() > 0) {
            product.getCategory().setName(msProductCategoryNewService.getFromCacheForMD(product.getCategory().getId())); //add on 2020-4-1
            return product;
        } else {
            return null;
        }
    }

    /**
     * 加载所有产品，当缓存未命中则从数据库装载至缓存
     *
     * @return
     */
    public List<Product> findAllList() {
        List<Product> productList = msProductService.findAllList();
        return handleProductCategory(productList);
    }

    public Map<Long, Product> getProductMap(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<Long, Product> productMap = Maps.newHashMap();
        List<Product> list = msProductService.findProductByIdListFromCache(ids);
        if(CollectionUtils.isNotEmpty(list)) {
            handleProductCategory(list);
            productMap = list.stream().collect(Collectors.toMap(LongIDBaseEntity::getId, i -> i));
        }
        return productMap;
    }


    /**
     * 加载非套组产品，当缓存未命中则从数据库装载至缓存
     *
     * @return

    public List<Product> getSingleProductList() {
        // 调用微服务 add on 2020-2-11
        List<Product> productListAll = msProductService.findAllList();
        if (productListAll != null && productListAll.size() > 0) {
            List<Product> singleProductList = productListAll.stream().filter(t -> t.getSetFlag() == 0).collect(Collectors.toList());
            return handleProductCategory(singleProductList);
        } else {
            return Lists.newArrayList();
        }
    }*/

    //region api functions
    public RestResult<Object> getProductMaterialList(@RequestBody RestCommonIds commonIds) {
        List<RestProduct> productMaterials = Lists.newArrayList();
        for (String id : commonIds.getIds()) {
            Long productId = Long.valueOf(id);
            RestProduct restProduct = new RestProduct();
            restProduct.setId(id);
            restProduct.setMaterials(getMaterialByProductId(productId));
            productMaterials.add(restProduct);
        }
        return RestResultGenerator.success(productMaterials);
    }
    //endregion api functions


    /**
     * 处理产品中的产品分类
     *
     * @param productList
     * @return
     */
    private List<Product> handleProductCategory(List<Product> productList) {
        if (productList == null) {
            return Lists.newArrayList();
        }
        // ProductCategory微服务调用
        List<Long> ids = productList != null && !productList.isEmpty() ? productList.stream().map(x -> x.getCategory().getId()).distinct().collect(Collectors.toList()) : Lists.newArrayList();
        List<ProductCategory> productCategoryList = ids != null && !ids.isEmpty() ? msProductCategoryNewService.findListByIdsForMDWithEntity(ids) : Lists.newArrayList(); //add on 2020-4-1
        Map<Long, ProductCategory> productCategoryMap = Maps.newHashMap();
        if (productCategoryList != null && !productCategoryList.isEmpty()) {
            productCategoryMap = productCategoryList.stream().collect(Collectors.toMap(ProductCategory::getId, r -> r));
        }

        Map<Long, ProductCategory> finalProductCategoryMap = productCategoryMap;
        productList.stream().forEach(product -> {
            ProductCategory productCategory = finalProductCategoryMap.get(product.getCategory().getId());
            product.getCategory().setName(productCategory == null ? "" : productCategory.getName());
        });

        return productList;
    }
}
