package com.wolfking.jeesite.ms.provideres.fallback;

import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.es.ServicePointStation;
import com.wolfking.jeesite.ms.provideres.feign.OrderFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderFeignFallback implements FallbackFactory<OrderFeign> {

    @Override
    public OrderFeign create(Throwable throwable) {

        return new OrderFeign() {

            /**
             * 按订单id读取回访记录
             */
            @Override
            public MSResponse<MSPage<ServicePointStation>> getNearByServicePoint(long areaId, int distance,
                                                                                 double longitude,double latitude,
                                                                                 String name, String no, Integer autoPlanFlag, String phone,
                                                                                 String stationName, String stationAddress,int pageNo, int pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }

            @Override
            public MSResponse<MSPage<ServicePointStation>> getNearByMatchSubAreaId(long subAreaId, String name, String no, Integer autoPlanFlag, String phone, String stationName, String stationAddress, int pageNo, int pageSize) {
                return new MSResponse<>(MSErrorCode.FALLBACK_FAILURE);
            }
        };
    }
}
