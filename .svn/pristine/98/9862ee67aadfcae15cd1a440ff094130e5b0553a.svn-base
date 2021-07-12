package com.wolfking.jeesite.ms.globalmapping.service;

import com.kkl.kklplus.entity.md.GlobalMappingSyncTypeEnum;
import com.kkl.kklplus.entity.md.mq.MQProductCategoryProductMappingMessage;
import com.wolfking.jeesite.ms.globalmapping.mq.sender.ProductCategoryProductMappingMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class ProductCategoryProductMappingService {

    @Autowired
    private ProductCategoryProductMappingMQSender productCategoryProductMappingMQSender;

    @Transactional()
    public void saveProductCategoryProductMapping(GlobalMappingSyncTypeEnum syncType, Long productId, Long productCategoryId) {
        if (syncType != null && productId != null && productId > 0 && productCategoryId != null && productCategoryId > 0) {
        /*    List<String> quarters = GMQuarterUtils.getQuarters();
            if (!quarters.isEmpty()) {
                MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.Builder builder;
                for (String quarter : quarters) {
                    builder = MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.newBuilder();
                    builder.setQuarter(quarter)
                            .setProductCategoryId(productCategoryId)
                            .setProductId(productId)
                            .setSyncType(syncType.getValue());
                    productCategoryProductMappingMQSender.send(builder.build());
                }
            }*/
            MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.Builder builder;
            builder = MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.newBuilder();
            builder.setProductCategoryId(productCategoryId)
                    .setProductId(productId)
                    .setSyncType(syncType.getValue());
            productCategoryProductMappingMQSender.send(builder.build());
        }
    }

    @Transactional()
    public void deleteProductCategoryProductMapping(Long productId) {
        if (productId != null && productId > 0) {
         /*   List<String> quarters = GMQuarterUtils.getQuarters();
            if (!quarters.isEmpty()) {
                MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.Builder builder;
                for (String quarter : quarters) {
                    builder = MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.newBuilder();
                    builder.setQuarter(quarter)
                            .setProductId(productId)
                            .setSyncType(GlobalMappingSyncTypeEnum.DELETE.getValue());
                    productCategoryProductMappingMQSender.send(builder.build());
                }
            }*/
            MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.Builder builder;
            builder = MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.newBuilder();
            builder.setProductId(productId)
                    .setSyncType(GlobalMappingSyncTypeEnum.DELETE.getValue());
            productCategoryProductMappingMQSender.send(builder.build());

        }
    }
}
