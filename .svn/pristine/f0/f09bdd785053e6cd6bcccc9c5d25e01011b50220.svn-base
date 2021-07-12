package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.kkl.kklplus.entity.md.dto.MDServicePointPriceDto;
import com.wolfking.jeesite.modules.md.entity.ServicePrice;
import com.wolfking.jeesite.ms.providermd.feign.MSServicePointPriceFeign;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MSServicePointPriceService {
    @Autowired
    private MSServicePointPriceFeign msServicePointPriceFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 按需,按网点价格类型读取网点价格
     *
     * @param servicePointId 网点id
     * @param products       NameValuePair<产品id,服务项目id>
     * @return
     */
    public List<ServicePrice> findPricesListByCustomizePriceFlagFromCache(Long servicePointId, List<NameValuePair<Long, Long>> products) {
        MSResponse<List<MDServicePointPriceDto>> msResponse = msServicePointPriceFeign.findPricesListByCustomizePriceFlagFromCache(servicePointId, products);
        if (!MSResponse.isSuccessCode(msResponse)) {
            return null;
        }
        List<MDServicePointPriceDto> list = msResponse.getData();
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        } else {
            List<ServicePrice> prices = mapper.mapAsList(list, ServicePrice.class);
            list.clear();
            return prices;
        }
    }

    /**
     * 按需读取网点的偏远区域价格
     *
     * @param servicePointId 网点id
     * @param products       NameValuePair<产品id,服务项目id>
     * @return
     */
    public List<ServicePrice> findPricesListByRemotePriceFlagFromCacheForSD(Long servicePointId, List<NameValuePair<Long, Long>> products) {
        MSResponse<List<MDServicePointPriceDto>> msResponse = msServicePointPriceFeign.findPricesListByRemotePriceFlagFromCacheForSD(servicePointId, products);
        if(!MSResponse.isSuccessCode(msResponse)){
            return null;
        }
        List<MDServicePointPriceDto> list = msResponse.getData();
        if(CollectionUtils.isEmpty(list)){
            return Lists.newArrayList();
        }else{
            List<ServicePrice> prices = mapper.mapAsList(list, ServicePrice.class);
            list.clear();
            return prices;
        }
    }

}
