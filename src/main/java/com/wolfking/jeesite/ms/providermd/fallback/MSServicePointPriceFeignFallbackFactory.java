package com.wolfking.jeesite.ms.providermd.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointPrice;
import com.kkl.kklplus.entity.md.dto.MDServicePointPriceDto;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointPriceFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class MSServicePointPriceFeignFallbackFactory implements FallbackFactory<MSServicePointPriceFeign> {
    @Override
    public MSServicePointPriceFeign create(Throwable throwable) {
        return new MSServicePointPriceFeign() {
            /**
             * 获取单个网点信息
             *
             * @param id
             * @return
             */
            @Override
            public MSResponse<MDServicePointPriceDto> getPrice(Long id) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 获取价格列表
             *
             * @param servicePointPriceDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDServicePointPriceDto>> findPricesList(MDServicePointPriceDto servicePointPriceDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 从缓存中获取价格列表
             *
             * @param servicePointPriceDto
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPriceDto>> findPricesListFromCache(MDServicePointPriceDto servicePointPriceDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             *  按需读取网点价格
             * @param servicePointId    网点id
             * @param products  NameValuePair<产品id,服务项目id>
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPriceDto>> findPricesByProductsFromCache(Long servicePointId, List<NameValuePair<Long,Long>> products){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 按需,按网点价格类型读取网点价格
             *
             * @param servicePointId 网点id
             * @param products       NameValuePair<产品id,服务项目id>
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPriceDto>> findPricesListByCustomizePriceFlagFromCache(Long servicePointId, List<NameValuePair<Long, Long>> products) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 按需读取网点的偏远区域价格
             *
             * @param servicePointId 网点id
             * @param products       NameValuePair<产品id,服务项目id>
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPriceDto>> findPricesListByRemotePriceFlagFromCacheForSD(Long servicePointId, List<NameValuePair<Long, Long>> products) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过网点列表获取价格列表
             *
             * @param mdServicePointPriceDto
             * @return
             */
            @Override
            public MSResponse<MSPage<MDServicePointPrice>> findPricesByPoints(MDServicePointPriceDto mdServicePointPriceDto) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过网点id和价格轮次列表获取价格列表
             *
             * @param servicePointId
             * @param priceType
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPrice>> findStandardServicePointPricesByServicePoints(Long servicePointId, Integer priceType, List<Long> productIds) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 通过价格轮次和产品id获取网点价格
             *
             * @param priceType
             * @param productId
             * @return
             */
            @Override
            public MSResponse<List<MDServicePointPrice>> findStandardPricesByProductIdAndPriceType(Long productId, Integer priceType) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 启用网点价格
             *
             * @param mdServicePointPrice
             * @return
             */
            @Override
            public MSResponse<Integer> activePrice(MDServicePointPrice mdServicePointPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 停用价格
             *
             * @param mdServicePointPrice
             * @return
             */
            @Override
            public MSResponse<Integer> stopPrice(MDServicePointPrice mdServicePointPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 修改价格
             *
             * @param mdServicePointPrice
             * @return
             */
            @Override
            public MSResponse<Integer> updatePrice(MDServicePointPrice mdServicePointPrice) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 删除网点的所有价格
             *
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Integer> deletePrices(Long servicePointId) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 按网点和产品删除价格
             *
             * @param pointId
             * @param products
             * @return
             */
            @Override
            public MSResponse<Integer> deletePricesByPointAndProducts(Long pointId, List<Long> products) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 批量新增或修改
             *
             * @param servicePrices
             * @return
             */
            @Override
            public MSResponse<Integer> batchInsertOrUpdate(List<MDServicePointPrice> servicePrices) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<Integer> batchInsertOrUpdateNew(List<MDServicePointPrice> servicePrices) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 批量删除
             *
             * @param servicePrices
             * @return
             */
            @Override
            public MSResponse<Integer> batchDelete(List<MDServicePointPrice> servicePrices) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 重载网点价格到缓存
             * @param servicePointId
             * @return
             */
            @Override
            public MSResponse<Integer> reloadPointPriceWithCache(Long servicePointId){
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            /**
             * 修改网点价格标识
             * @param servicePoint
             * @return
             */
            @Override
            public MSResponse<Integer> updateCustomizePriceFlag(MDServicePoint servicePoint) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
