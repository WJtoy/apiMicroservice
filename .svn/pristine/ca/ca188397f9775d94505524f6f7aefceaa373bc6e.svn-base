/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.service.sd;

import cn.hutool.http.HtmlUtil;
import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.modules.api.entity.sd.RestGetProductFixSpec;
import com.wolfking.jeesite.modules.md.entity.CustomerProduct;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderItem;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 工单信息
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppOrderInfoService extends AppBaseService {

    @Autowired
    private OrderCacheReadService orderCacheReadService;
    @Autowired
    private MSCustomerProductService msCustomerProductService;


    public RestGetProductFixSpec getProductFixSpec(Long orderId, String quarter, Long productId, Integer orderItemIndex) {
        Order order = orderCacheReadService.getOrderById(orderId, quarter, OrderUtils.OrderDataLevel.CONDITION, true, false);
        if (order == null || order.getOrderCondition() == null || order.getOrderCondition().getCustomerId() == 0) {
            throw new OrderException("读取工单信息失败");
        }

        if (order.getItems() == null || order.getItems().size() <= orderItemIndex) {
            throw new OrderException("读取工单下单项目失败");

        }
        OrderItem orderItem = order.getItems().get(orderItemIndex);
        if (orderItem.getProduct() == null || orderItem.getProduct().getId() == null || !productId.equals(orderItem.getProduct().getId())) {
            throw new OrderException("读取工单产品信息失败");
        }
        Product product = orderItem.getProduct();
        RestGetProductFixSpec productFixSpec = new RestGetProductFixSpec();
        productFixSpec.setProductName(StringUtils.toString(product.getName()));
        productFixSpec.setProductSpec(StringUtils.toString(orderItem.getProductSpec()));
        List<RestGetProductFixSpec.PicItem> picItems = Lists.newArrayList();
        if (orderItem.getPics() != null && !orderItem.getPics().isEmpty()) {
            for (String picUrl : orderItem.getPics()) {
                if (StringUtils.isNotBlank(picUrl)) {
                    picItems.add(new RestGetProductFixSpec.PicItem(OrderPicUtils.getPicUrl(picUrl)));
                }
            }
        }
        productFixSpec.setPics(picItems);
        CustomerProduct customerProduct = msCustomerProductService.getFixSpecFromCache(order.getOrderCondition().getCustomerId(), product.getId());
        if (customerProduct != null) {
            productFixSpec.setFixSpec(StringUtils.toString(customerProduct.getFixSpec()));
            productFixSpec.setFixSpecHtml(StringUtils.toString( StringEscapeUtils.unescapeHtml4(customerProduct.getFixSpecification())));
            productFixSpec.setVideoUrl(StringUtils.toString(customerProduct.getVideoUrl()));
        }
        return productFixSpec;
    }

}
