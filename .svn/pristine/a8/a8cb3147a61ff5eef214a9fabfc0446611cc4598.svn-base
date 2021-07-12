package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePic;
import com.wolfking.jeesite.modules.api.entity.md.RestProductCompletePicItem;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
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
public class RestProductPicToProductCompletePicMapper extends CustomMapper<RestProductCompletePic, ProductCompletePic> {

    @Override
    public void mapAtoB(RestProductCompletePic a, ProductCompletePic b, MappingContext context) {

    }

    @Override
    public void mapBtoA(ProductCompletePic b, RestProductCompletePic a, MappingContext context) {
        a.setProductId(b.getProduct().getId());
        a.setProductName(b.getProduct().getName());
        if (b.getItems() != null && b.getItems().size() > 0) {
            RestProductCompletePicItem restPicItem;
            for (ProductCompletePicItem item : b.getItems()) {
                restPicItem = new RestProductCompletePicItem();
                restPicItem.setPictureCode(item.getPictureCode());
                restPicItem.setSort(item.getSort() == null ? 0 : item.getSort());
                restPicItem.setMustFlag(item.getMustFlag());
                restPicItem.setTitle(item.getTitle());
//                if (item.getMustFlag() != null && item.getMustFlag() == 1) {
//                    restPicItem.setTitle(item.getTitle() == null ? "*" : item.getTitle()+ "*");
//                } else {
//                    restPicItem.setTitle(item.getTitle());
//                }
                restPicItem.setRemarks(item.getRemarks());
                restPicItem.setUrl(item.getUrl());
                a.getItems().add(restPicItem);
            }
            a.setItems(a.getItems().stream().sorted(Comparator.comparing(RestProductCompletePicItem::getSort)).collect(Collectors.toList()));
        }
    }
}
