package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDProductPrice;
import com.wolfking.jeesite.ms.providermd.feign.MSProductPriceFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class MSProductPriceFeignFallbackFactory implements FallbackFactory<MSProductPriceFeign> {
    @Override
    public MSProductPriceFeign create(Throwable throwable) {
        return new MSProductPriceFeign() {
            @Override
            public MSResponse<List<MDProductPrice>> findAllList() {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<MDProductPrice>> findList(MDProductPrice mdProductPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductPrice>> findGroupList(Integer priceType, @RequestParam("productIds") List<Long> productIds, @RequestParam(value = "serviceTypeIds") List<Long> serviceTypeIds, Long servicePointId, Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductPrice>> findAllGroupList(Integer priceType, List<Integer> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<MDProductPrice>> findAllPriceList(Integer priceType, List<Integer> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MDProductPrice> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Long> getIdByProductIdAndServiceTypeIdAndPriceType(Long productId, Long serviceTypeId, Integer priceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id，服务类型id，第几轮价格获取网点参考价格
             *
             * @param priceType
             * @param productId
             * @param serviceTypeId
             * @return
             */
            @Override
            public MSResponse<MDProductPrice> getEngineerPriceByProductIdAndServiceTypeIdAndPriceType(Integer priceType, Long productId, Long serviceTypeId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据产品id，服务类型id，第几轮价格获取厂商指导价  //add on 2019-11-26
             *
             * @param productId
             * @param serviceTypeId
             * @param priceType
             * @return
             */
            @Override
            public MSResponse<Double> getPriceByProductIdAndServiceTypeIdAndPriceType(Integer priceType, Long productId, Long serviceTypeId ) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDProductPrice mdProductPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsert(List<MDProductPrice> mdProductPriceList) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDProductPrice mdProductPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDProductPrice mdProductPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
