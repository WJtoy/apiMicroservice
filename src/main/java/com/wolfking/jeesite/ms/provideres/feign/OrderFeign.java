package com.wolfking.jeesite.ms.provideres.feign;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.es.ServicePointStation;
import com.wolfking.jeesite.ms.provideres.fallback.OrderFeignFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单es微服务
 * 包含根据经纬度获得符合派单的网点列表
 */
@FeignClient(name = "provider-es", fallbackFactory = OrderFeignFallback.class)
public interface OrderFeign {

    /**
     * 按经纬度获得最近的网点列表
     * @param pageNo    页数
     * @param pageSize  分页大小
     * @param areaId    区县id
     * @param distance  距离
     * @param longitude 经度
     * @param latitude  纬度
     * @param name      网点名称
     * @param no        网点编号
     * @param phone     网点电话
     * @param stationName   网点服务点名称
     * @param stationAddress    网点服务点地址
     * @return
     */
    @RequestMapping("/order/servicepoint/nearby")
    MSResponse<MSPage<ServicePointStation>> getNearByServicePoint(@RequestParam("areaId") long areaId, @RequestParam("distance") int distance,
                                                                  @RequestParam("longitude") double longitude,@RequestParam("latitude") double latitude,
                                                                  @RequestParam("name") String name,@RequestParam("no") String no,@RequestParam("autoPlanFlag")  Integer autoPlanFlag, @RequestParam("phone") String phone,
                                                                  @RequestParam("stationName") String stationName, @RequestParam("stationAddress") String stationAddress,
                                                                  @RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize
                                                                  );


    @RequestMapping("/order/servicepoint/nearbyMatchSubAreaId")
    MSResponse<MSPage<ServicePointStation>> getNearByMatchSubAreaId(@RequestParam("subAreaId") long subAreaId,
                                                                  @RequestParam("name") String name,
                                                                  @RequestParam("no") String no,
                                                                  @RequestParam("autoPlanFlag")  Integer autoPlanFlag,
                                                                  @RequestParam("phone") String phone,
                                                                  @RequestParam("stationName") String stationName,
                                                                  @RequestParam("stationAddress") String stationAddress,
                                                                  @RequestParam("pageNo") int pageNo,
                                                                  @RequestParam("pageSize") int pageSize );


}
