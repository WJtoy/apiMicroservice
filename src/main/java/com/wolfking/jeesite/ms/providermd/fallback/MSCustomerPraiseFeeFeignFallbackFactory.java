package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDCustomerPraiseFee;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerPraiseFeeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MSCustomerPraiseFeeFeignFallbackFactory implements FallbackFactory<MSCustomerPraiseFeeFeign> {
    @Override
    public MSCustomerPraiseFeeFeign create(Throwable throwable) {
        return new MSCustomerPraiseFeeFeign() {
            /**
             * 根据ID查询客户好评费
             *
             * @param id id
             * @return
             */
            @Override
            public MSResponse<MDCustomerPraiseFee> getById(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户ID查询客户好评费
             *
             * @param customerId 客户id
             * @return
             */
            @Override
            public MSResponse<MDCustomerPraiseFee> getByCustomerIdFromCacheForCP(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户ID查询客户好评费-New  2020-4-30
             *
             * @param customerId 客户id
             * @return customerId, praise_fee_flag, praise_fee, max_praise_fee, discount, praisestandardItem, checkstandardItem, praise_requirement
             */
            @Override
            public MSResponse<MDCustomerPraiseFee> getByCustomerIdFromCacheNewForCP(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据客户ID判断客户是否已添加好评费
             */
            @Override
            public MSResponse<Boolean> isExistsByCustomerId(Long customerId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取客户好评费
             *
             * @param mdCustomerPraiseFee
             */
            @Override
            public MSResponse<MSPage<MDCustomerPraiseFee>> findList(MDCustomerPraiseFee mdCustomerPraiseFee) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> insert(MDCustomerPraiseFee mdCustomerPraiseFee) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> update(MDCustomerPraiseFee mdCustomerPraiseFee) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> delete(MDCustomerPraiseFee mdCustomerPraiseFee) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
