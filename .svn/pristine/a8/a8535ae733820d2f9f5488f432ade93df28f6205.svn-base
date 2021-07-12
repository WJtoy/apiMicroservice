package com.wolfking.jeesite.ms.providermd.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.md.dto.MDCustomerPriceDto;
import com.wolfking.jeesite.modules.md.entity.CustomerPrice;
import com.wolfking.jeesite.ms.providermd.feign.MSCustomerPriceFeign;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MSCustomerPriceService {

    @Autowired
    private MSCustomerPriceFeign msCustomerPriceFeign;

    @Autowired
    private MapperFacade mapper;

    /**
     * 读取某客户的价格清单
     * 先从缓存读取，缓存不存在从数据库读取，并更新缓存
     *
     * @param customerId 客户id
     * @return
     */
    public List<CustomerPrice> getPricesFromCache(Long customerId){
      MSResponse<List<MDCustomerPriceDto>> msResponse =  msCustomerPriceFeign.findCustomerPriceWithAssociatedFromCache(customerId);
      return mdCustomerPriceToCustomer(msResponse);
    }

    /**
     * MDCustomerPrice 转 CustomerPrice
     * @param msResponse
     * @return
     */
    private List<CustomerPrice> mdCustomerPriceToCustomer(MSResponse<List<MDCustomerPriceDto>> msResponse){
        if(MSResponse.isSuccess(msResponse)){
            List<CustomerPrice> list = mapper.mapAsList(msResponse.getData(),CustomerPrice.class);
            if(list!=null && list.size()>0){
                return list;
            }else{
                return Lists.newArrayList();
            }
        }else{
            return Lists.newArrayList();
        }
    }



}
