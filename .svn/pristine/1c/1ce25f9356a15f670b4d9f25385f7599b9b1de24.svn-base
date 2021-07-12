package com.wolfking.jeesite.ms.b2bcenter.md.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BSurchargeItemMapping;
import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.ms.b2bcenter.md.feign.B2BSurchargeItemMappingFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class B2BSurchargeItemMappingFeignFallbackFactory implements FallbackFactory<B2BSurchargeItemMappingFeign> {

    @Override
    public B2BSurchargeItemMappingFeign create(Throwable throwable) {
        return new B2BSurchargeItemMappingFeign() {

            @Override
            public MSResponse<B2BSurchargeItemMapping> get(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<B2BSurchargeItemMapping>> getList(B2BSurchargeItemMapping surchargeItemMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<B2BSurchargeItemMapping> insert(B2BSurchargeItemMapping surchargeItemMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(B2BSurchargeItemMapping surchargeItemMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(B2BSurchargeItemMapping surchargeItemMapping) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<List<B2BSurchargeItemMapping>> getListByDataSource(Integer dataSource) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
