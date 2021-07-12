package com.wolfking.jeesite.ms.providermd.fallback;


import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerProduct;
import com.kkl.kklplus.entity.md.MDProduct;
import com.kkl.kklplus.entity.md.dto.MDCustomerProductDto;
import com.kkl.kklplus.entity.md.dto.MDProductDto;
import com.wolfking.jeesite.modules.md.entity.CustomerProduct;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerProductFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
public class MSCustomerProductFeignFallbackFactory implements FallbackFactory<MSCustomerProductFeign> {
    @Override
    public MSCustomerProductFeign create(Throwable throwable) {
        return new MSCustomerProductFeign() {
            /**
             * 从缓存中获取安装规范(API)
             *
             * @param customerId
             * @param productId
             * @return
             */
            @Override
            public MSResponse<MDCustomerProduct> getFixSpecFromCache(Long customerId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomerProduct> getFixSpecFromCacheForApi(Long customerId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Map<Long, Integer>> findFixSpecByCustomerIdAndProductIdsFromCacheForAPI(Long customerId, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerProduct>> findByCustomer(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<Long>> findProductIdsById(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerProduct>> findCustomerProductsByIdsWithoutCustomerAndProduct(MDCustomerProduct mdCustomerProduct, List<Long> productIds) {
                return null;
            }

            @Override
            public MSResponse<Integer> deleteByCustomer(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsert(Long customerId, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerProductDto>> findCustomerProductList(MDCustomerProductDto mdCustomerProductDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerProductDto>> findList(MDCustomerProduct customerProduct) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomerProductDto> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDCustomerProduct mdCustomerProduct) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDCustomerProduct mdCustomerProduct) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDCustomerProduct mdCustomerProduct) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> newBatchInsert(MDCustomerProduct mdCustomerProduct, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchDelete(MDCustomerProduct mdCustomerProduct, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDCustomerProduct> checkExistWithCustomerProduct(Long customerId, Long productId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProduct>> findProductByCustomerIdFromCache(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductDto>> getCustomerProducts(Long customerId, Integer paymentType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> getRemoteFeeFlag(Long customerId, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<String> getProductSpecAndInfoForCreateOrder(Long customerId,Long productTypeItemId){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

        };
    }
}
