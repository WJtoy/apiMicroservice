package com.wolfking.jeesite.ms.providermd.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.md.MDServicePointProduct;
import com.kkl.kklplus.entity.md.dto.MDServicePointProductDto;
import com.wolfking.jeesite.ms.providermd.fallback.MSServicePointProductFeignFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="provider-md", fallbackFactory = MSServicePointProductFeignFallbackFactory.class)
public interface MSServicePointProductFeign {

    /**
     * 读取网点与产品分类的产品列表（用来替换ServicePointDao.getServicePointProductsByIds方法)
     * @param mdServicePointProductDto
     * @return
     */
    @PostMapping("/servicePointProduct/findList")
    MSResponse<MSPage<MDServicePointProductDto>> findList(@RequestBody MDServicePointProductDto mdServicePointProductDto);

    /**
     * 分页获取获取网点产品id列表
     * @param mdServicePointProduct
     * @return
     */
    @PostMapping("/servicePointProduct/findProductIds")
    MSResponse<MSPage<MDServicePointProduct>>  findProductIds(@RequestBody MDServicePointProduct mdServicePointProduct);

    /**
     * 给网点配置产品
     * @param products
     * @param servicePointId
     * @return
     */
    @PostMapping("/servicePointProduct/assignProducts")
    MSResponse<Integer>  assignProducts(@RequestBody List<Long> products, @RequestParam("servicePointId") Long servicePointId);

    /**
     * 移除网点负责的产品id
     * @param servicePointId
     * @return
     */
    @DeleteMapping("/servicePointProduct/removeProducts")
    MSResponse<Integer> removeProducts(@RequestParam("servicePointId") Long servicePointId);

    /**
     * 根据网点id，判读产品id列表是否都存在于网点产品中
     * @param servicePointId 网点id
     * @param productIds    产品id列表
     * @return
     */
    @PostMapping("/servicePointProduct/existProductsForSD")
    MSResponse<Integer> existProductsForSD(@RequestParam("servicePointId") Long servicePointId, @RequestBody List<Long> productIds);
}
