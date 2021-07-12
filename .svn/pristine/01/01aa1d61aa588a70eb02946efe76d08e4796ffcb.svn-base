package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointProduct;
import com.kkl.kklplus.entity.md.dto.MDServicePointProductDto;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointProductFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MSServicePointProductFeignFallbackFactory implements FallbackFactory<MSServicePointProductFeign> {
    @Override
    public MSServicePointProductFeign create(Throwable throwable) {
        return new MSServicePointProductFeign() {
            /**
             * 读取网点与产品分类的产品列表（用来替换ServicePointDao.getServicePointProductsByIds方法)
             *
             * @param mdServicePointProductDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDServicePointProductDto>> findList(MDServicePointProductDto mdServicePointProductDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 分页获取获取网点产品id列表
             *
             * @param mdServicePointProduct
             * @return
             */
            @Override
            public MSResponse<MSPage<MDServicePointProduct>> findProductIds(MDServicePointProduct mdServicePointProduct) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 给网点配置产品
             *
             * @param products
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Integer> assignProducts(List<Long> products, Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 移除网点负责的产品id
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Integer> removeProducts(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 根据网点id，判读产品id列表是否都存在于网点产品中
             *
             * @param servicePointId 网点id
             * @param productIds     产品id列表
             * @return
             */
            @Override
            public MSResponse<Integer> existProductsForSD(Long servicePointId, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
