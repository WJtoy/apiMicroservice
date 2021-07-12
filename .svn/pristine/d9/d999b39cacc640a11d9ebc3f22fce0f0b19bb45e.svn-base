package com.wolfking.jeesite.ms.globalmapping.service;

import com.kkl.kklplus.entity.md.GlobalMappingSyncTypeEnum;
import com.kkl.kklplus.entity.md.mq.MQProductCategoryUserMappingMessage;
import com.wolfking.jeesite.ms.globalmapping.mq.sender.ProductCategoryUserMappingMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class ProductCategoryUserMappingService {

    @Autowired
    private ProductCategoryUserMappingMQSender productCategoryUserMappingMQSender;

    @Transactional()
    public void saveProductCategoryUserMapping(GlobalMappingSyncTypeEnum syncType, Long userId, List<Long> productCategoryIds) {
        if (syncType != null && userId != null && userId > 0 && productCategoryIds != null && !productCategoryIds.isEmpty()) {
      /*      List<String> quarters = GMQuarterUtils.getQuarters();
            productCategoryIds = productCategoryIds.stream().filter(i -> i != null && i > 0).distinct().collect(Collectors.toList());
            if (!quarters.isEmpty()) {
                MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.Builder builder;
                for (String quarter : quarters) {
                    builder = MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.newBuilder();
                    builder.setQuarter(quarter)
                            .setUserId(userId)
                            .addAllProductCategoryId(productCategoryIds)
                            .setSyncType(syncType.getValue());
                    productCategoryUserMappingMQSender.send(builder.build());
                }
            }*/
            MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.Builder builder;
            builder = MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.newBuilder();
            builder.setUserId(userId)
                    .addAllProductCategoryId(productCategoryIds)
                    .setSyncType(syncType.getValue());

            productCategoryUserMappingMQSender.send(builder.build());
        }
    }

    @Transactional()
    public void deleteProductCategoryUserMapping(Long userId) {
        if (userId != null && userId > 0) {
       /*     List<String> quarters = GMQuarterUtils.getQuarters();
            MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.Builder builder;
            if (!quarters.isEmpty()) {
                for (String quarter : quarters) {
                    builder = MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.newBuilder();
                    builder.setQuarter(quarter)
                            .setUserId(userId)
                            .setSyncType(GlobalMappingSyncTypeEnum.DELETE.getValue());
                    productCategoryUserMappingMQSender.send(builder.build());
                }
            }*/
            MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.Builder builder;
            builder = MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.newBuilder();
            builder.setUserId(userId)
                    .setSyncType(GlobalMappingSyncTypeEnum.DELETE.getValue());
            productCategoryUserMappingMQSender.send(builder.build());
        }
    }
}
