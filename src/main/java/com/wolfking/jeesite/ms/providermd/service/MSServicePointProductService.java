package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.md.MDServicePointProduct;
import com.wolfking.jeesite.common.persistence.Page;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointProductFeign;
import com.wolfking.jeesite.ms.providermd.utils.MDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSServicePointProductService {
    @Autowired
    private MSServicePointProductFeign msServicePointProductFeign;

    /**
     * 通过网点id查询对应的产品id列表
     *
     * @param mdServicePointProduct
     * @return
     */
    public List<Long> findProductIds(MDServicePointProduct mdServicePointProduct) {
        List<Long> productIds = Lists.newArrayList();

        int pageNo = 1;
        Page<MDServicePointProduct> servicePointProductPage = new Page<>();
        servicePointProductPage.setPageSize(500);
        servicePointProductPage.setPageNo(pageNo);

        List<MDServicePointProduct> mdServicePointProductList = Lists.newArrayList();
        Page<MDServicePointProduct> returnPage = MDUtils.findMDEntityListForPage(servicePointProductPage, mdServicePointProduct, msServicePointProductFeign::findProductIds);
        mdServicePointProductList.addAll(returnPage.getList());

        while (pageNo < returnPage.getPageCount()) {
            pageNo++;
            servicePointProductPage.setPageNo(pageNo);
            Page<MDServicePointProduct> whileReturnPage = MDUtils.findMDEntityListForPage(servicePointProductPage, mdServicePointProduct, msServicePointProductFeign::findProductIds);
            mdServicePointProductList.addAll(whileReturnPage.getList());
        }
        if (!ObjectUtils.isEmpty(mdServicePointProductList)) {
            productIds = mdServicePointProductList.stream().map(MDServicePointProduct::getProductId).collect(Collectors.toList());
        }

        return productIds;
    }
}
