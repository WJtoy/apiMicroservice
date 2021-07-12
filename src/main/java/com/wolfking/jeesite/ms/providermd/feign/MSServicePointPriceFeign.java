package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.md.MDServicePointPrice;
import com.kkl.kklplus.entity.md.dto.MDServicePointPriceDto;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointPriceFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSServicePointPriceFeignFallbackFactory.class)
public interface MSServicePointPriceFeign {

    /**
     * 获取单个网点信息
     * @param id
     * @return
     */
    @GetMapping("/servicePointPrice/getPrice")
    MSResponse<MDServicePointPriceDto> getPrice(@RequestParam("id") Long id);

    /**
     * 获取价格列表
     * @param servicePointPriceDto
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesList")
    MSResponse<MSPage<MDServicePointPriceDto>> findPricesList(@RequestBody MDServicePointPriceDto servicePointPriceDto);

    /**
     * 从缓存中获取价格列表
     * @param servicePointPriceDto
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesListFromCache")
    MSResponse<List<MDServicePointPriceDto>> findPricesListFromCache(@RequestBody MDServicePointPriceDto servicePointPriceDto);

    /**
     *  按需读取网点价格
     * @param servicePointId    网点id
     * @param products  NameValuePair<产品id,服务项目id>
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesListFromCacheNew")
    MSResponse<List<MDServicePointPriceDto>> findPricesByProductsFromCache(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<NameValuePair<Long,Long>> products);

    /**
     *  按需,按网点价格类型读取网点价格
     * @param servicePointId    网点id
     * @param products  NameValuePair<产品id,服务项目id>
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesListByCustomizePriceFlagFromCache")
    MSResponse<List<MDServicePointPriceDto>> findPricesListByCustomizePriceFlagFromCache(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<NameValuePair<Long,Long>> products);

    /**
     *  按需读取网点的偏远区域价格
     * @param servicePointId    网点id
     * @param products  NameValuePair<产品id,服务项目id>
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesListByRemotePriceFlagFromCacheForSD")
    MSResponse<List<MDServicePointPriceDto>> findPricesListByRemotePriceFlagFromCacheForSD(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<NameValuePair<Long,Long>> products);

    /**
     * 通过网点列表获取价格列表
     * @param mdServicePointPriceDto
     * @return
     */
    @PostMapping("/servicePointPrice/findPricesByPoints")
    MSResponse<MSPage<MDServicePointPrice>> findPricesByPoints(@RequestBody MDServicePointPriceDto mdServicePointPriceDto);

    /**
     * 通过网点id和价格轮次列表获取价格列表
     * @param servicePointId
     * @param priceType
     * @return
     */
    @PostMapping("/servicePointPrice/findStandardServicePointPricesByServicePoints")
    MSResponse<List<MDServicePointPrice>> findStandardServicePointPricesByServicePoints(@RequestParam("servicePointId") Long servicePointId, @RequestParam("priceType") Integer priceType, @RequestBody List<Long> productIds);

    /**
     * 通过价格轮次和产品id获取网点价格
     * @param priceType
     * @param productId
     * @return
     */
    @PostMapping("/servicePointPrice/findStandardPricesByProductIdAndPriceType")
    MSResponse<List<MDServicePointPrice>> findStandardPricesByProductIdAndPriceType( @RequestParam("productId") Long productId, @RequestParam("priceType") Integer priceType);
    /**
     * 启用网点价格
     * @param mdServicePointPrice
     * @return
     */
    @PutMapping("/servicePointPrice/activePrice")
    MSResponse<Integer> activePrice(@RequestBody MDServicePointPrice mdServicePointPrice);

    /**
     * 停用价格
     * @param mdServicePointPrice
     * @return
     */
    @PutMapping("/servicePointPrice/stopPrice")
    MSResponse<Integer> stopPrice(@RequestBody MDServicePointPrice mdServicePointPrice);


    /**
     * 修改价格
     * @param mdServicePointPrice
     * @return
     */
    @PutMapping("/servicePointPrice/updatePrice")
    MSResponse<Integer> updatePrice(@RequestBody MDServicePointPrice mdServicePointPrice);

    /**
     * 删除网点的所有价格
     * @param servicePointId
     * @return
     */
    @DeleteMapping("/servicePointPrice/deletePrices")
    MSResponse<Integer> deletePrices(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 按网点和产品删除价格
     * @param pointId
     * @param products
     * @return
     */
    @DeleteMapping("/servicePointPrice/deletePricesByPointAndProducts")
    MSResponse<Integer> deletePricesByPointAndProducts(@RequestParam("pointId") Long pointId, @RequestBody List<Long> products);

    /**
     * 批量新增或修改
     * @param servicePrices
     * @return
     */
    @PostMapping("/servicePointPrice/batchInsertOrUpdate")
    MSResponse<Integer> batchInsertOrUpdate(@RequestBody List<MDServicePointPrice> servicePrices);

    /**
     * 批量新增或修改New
     * @param servicePrices
     * @return
     */
    @PostMapping("/servicePointPrice/batchInsertOrUpdateNew")
    MSResponse<Integer> batchInsertOrUpdateNew(@RequestBody List<MDServicePointPrice> servicePrices);

    /**
     * 批量删除
     * @param servicePrices
     * @return
     */
    @DeleteMapping("/servicePointPrice/batchDelete")
    MSResponse<Integer> batchDelete(@RequestBody List<MDServicePointPrice> servicePrices);

    /**
     * 重载网点价格到缓存
     * @param servicePointId
     * @return
     */
    @GetMapping("/servicePointPrice/reloadPointPriceWithCache")
    MSResponse<Integer> reloadPointPriceWithCache(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 修改网点价格标识
     * @param servicePoint
     * @return
     */
    @PutMapping("/servicePoint/updateCustomizePriceFlag")
    MSResponse<Integer> updateCustomizePriceFlag(@RequestBody MDServicePoint servicePoint);

}
