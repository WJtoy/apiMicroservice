package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerPrice;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerPriceFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class MSCustomerPriceFeignFallbackFactory implements FallbackFactory<MSCustomerPriceFeign> {
    @Override
    public MSCustomerPriceFeign create(Throwable throwable) {
        return new MSCustomerPriceFeign() {

            /*@Override
            public MSResponse<List<MDCustomerPriceDto>> findPrices(Long customerId, Integer delFlag) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<List<MDCustomerPriceDto>> findPricesNew(Long customerId, Integer delFlag) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerPriceDto>> findCustomerPriceWithAssociated(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerPriceDto>> findPricesByPriceIds(List<Long> ids) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*@Override
            public MSResponse<List<MDCustomerPrice>> findPricesByCustomers(List<Long> customerIds, Long productId, Long serviceTypeId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<List<MDCustomerPrice>> findPricesByCustomersNew(List<Long> customerIds, Long productId, Long serviceTypeId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDCustomerPriceDto>> findApprovePriceList(MDCustomerPriceDto mdCustomerPriceDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDCustomerPriceDto>> findCustomerPriceWithAssociatedFromCache(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDCustomerPrice mdCustomerPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> approvePrices(List<Long> ids, Long updateBy, Long updateDate) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*@Override
            public MSResponse<MDCustomerPriceDto> getPrice(Long id, Integer delFlag) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<MDCustomerPriceDto> getPriceNew(Long id, Integer delFlag) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*@Override
            public MSResponse<Integer> updatePriceByMap(HashMap<String, Object> map) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<Integer> updatePriceByMapNew(HashMap<String, Object> paramMap) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDCustomerPrice mdCustomerPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> deletePricesByCustomerAndProducts(Long customerId, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insertOrUpdateBatchNew(List<MDCustomerPrice> customerPriceList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /*@Override
            public MSResponse<Integer> insertOrUpdateBatch(List<MDCustomerPrice> customerPriceList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }*/

            @Override
            public MSResponse<Integer> batchInsert(List<MDCustomerPrice> customerPriceList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
