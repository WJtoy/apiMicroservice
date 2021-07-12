package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePicItem;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sd.entity.OrderItemComplete;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Zhoucy
 * @date 2018/8/21 14:25
 **/
@Component
public class RestProductPicToOrderItemCompleteMapper extends CustomMapper<RestProductCompletePic, OrderItemComplete> {

    @Override
    public void mapAtoB(RestProductCompletePic a, OrderItemComplete b, MappingContext context) {

    }

    @Override
    public void mapBtoA(OrderItemComplete b, RestProductCompletePic a, MappingContext context) {
        a.setUniqueId(b.getId());
        a.setProductId(b.getProduct().getId());
        a.setProductName(b.getProduct().getName());
        a.setItemNo(b.getItemNo());
        a.setProductSN(b.getUnitBarcode());
        if (b.getItemList() != null && b.getItemList().size() > 0) {
            RestProductCompletePicItem restPicItem;
            for (ProductCompletePicItem item : b.getItemList()) {
                restPicItem = new RestProductCompletePicItem();
                restPicItem.setPictureCode(item.getPictureCode());
                restPicItem.setSort(item.getSort() == null ? 0 : item.getSort());
                restPicItem.setMustFlag(item.getMustFlag());
                if (item.getMustFlag() != null && item.getMustFlag() == 1) {
                    restPicItem.setTitle(item.getTitle() == null ? "*" : item.getTitle()+ "*");
                } else {
                    restPicItem.setTitle(item.getTitle());
                }
                restPicItem.setUrl(item.getUrl());
                a.getItems().add(restPicItem);
            }
            a.setItems(a.getItems().stream().sorted(Comparator.comparing(RestProductCompletePicItem::getSort)).collect(Collectors.toList()));
        }
    }
}
