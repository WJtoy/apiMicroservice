package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerPriceNewFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class MSCustomerPriceNewFeignFallbackFactory implements FallbackFactory<MSCustomerPriceNewFeign> {
    @Override
    public MSCustomerPriceNewFeign create(Throwable throwable) {
        return new MSCustomerPriceNewFeign() {
            /**
             * 根据产品和服务类型获取客户的服务价格
             *
             * @param customerId 客户id
             * @param paramMap   key 为产品id ,value为服务类型id
             * @return
             */
            @Override
            public MSResponse<List<MDCustomerPriceDto>> findPricesByProductsAndServiceTypesFromCache(Long customerId, HashMap<Long, Long> paramMap) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
