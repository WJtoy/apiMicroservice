package com.wolfking.jeesite.modules.md.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePic;
import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.md.utils.ProductCompletePicItemMapper;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.providermd.service.MSProductPicMappingService;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 品牌Service
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ProductCompletePicService extends LongIDBaseService {
    @Autowired
    private MSProductPicMappingService msProductPicMappingService;

    /**
     * 从数据字典中同步标题，排序，说明等信息,并读取产品信息
     */
    public void syncItemInfoFromDict(List<ProductCompletePic> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Dict> dicts = MSDictUtils.getDictList(ProductCompletePic.DICTTYPE);
        if (dicts == null || dicts.isEmpty()) {
            return;
        }
        List<ProductCompletePicItem> items = Mappers.getMapper(ProductCompletePicItemMapper.class).listToPicItem(dicts);
        Map<String, ProductCompletePicItem> itemMaps = items.stream().collect(Collectors.toMap(ProductCompletePicItem::getPictureCode, item -> item));
        dicts = null;
        ProductCompletePic m;
        ProductCompletePicItem item;
        Product product;
        for (int i = 0, size = list.size(); i < size; i++) {
            m = list.get(i);
            m.parseItemsFromJson();//json to list
            if (!m.getItems().isEmpty()) {
                for (ProductCompletePicItem itm : m.getItems()) {
                    if (itemMaps.containsKey(itm.getPictureCode())) {
                        item = itemMaps.get(itm.getPictureCode());
                        itm.setTitle(item.getTitle());
                        itm.setRemarks(item.getRemarks());
                        itm.setSort(item.getSort());
                    }
                }
                m.setItems(m.getItems().stream().sorted(Comparator.comparing(ProductCompletePicItem::getSort)).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 优先从缓存中按id获得对象
     *
     * @param prouctId 产品id
     * @return
     */
    public ProductCompletePic getFromCache(long prouctId) {
        ProductCompletePic productCompletePic = msProductPicMappingService.getByProductId(prouctId);
        if (productCompletePic == null) {
            return productCompletePic;
        }
        List<ProductCompletePic> lists = Lists.newArrayList();
        lists.add(productCompletePic);
        syncItemInfoFromDict(lists);
        if (lists != null && !lists.isEmpty()) {
            return lists.get(0);
        }
        return null;
    }

    /**
     * 加载所有，当缓存未命中则从数据库装载至缓存
     *
     * @return
     */
    public List<ProductCompletePic> findAllList() {
        List<ProductCompletePic> list = msProductPicMappingService.findAllList();
        if (list != null && !list.isEmpty()) {
            syncItemInfoFromDict(list);
        }
        return list;
    }

    public Map<Long, ProductCompletePic> getProductCompletePicMap(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Maps.newHashMap();
        }
        List<ProductCompletePic> list = findAllList();
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(x -> productIds.contains(x.getProduct().getId())).collect(Collectors.toList());
        }
        if (list != null && !list.isEmpty()) {
            Map<Long, ProductCompletePic> productCompletePicMap = list.stream().collect(Collectors.toMap(i -> i.getProduct().getId(), i -> i));
            return productCompletePicMap;
        }
        return Maps.newHashMap();
    }

}
