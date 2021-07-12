package com.wolfking.jeesite.modules.md.utils;

import com.wolfking.jeesite.modules.md.entity.ProductCompletePicItem;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @autor Ryan Lu
 * @date 2018/8/18 下午3:59
 */
@Mapper
public abstract class ProductCompletePicItemMapper {

    /**
     * 数据字典 to 产品完成图片模型
     */
    @Mappings({
            @Mapping(target = "pictureCode",source = "entity.value"),
            @Mapping(target = "title", source = "label"),
            @Mapping(target = "sort",source = "sort" ),
            @Mapping(target = "remarks", source = "description"),
            @Mapping(target = "mustFlag",ignore = true),
            @Mapping(target = "checked",ignore = true),
            @Mapping(target = "url",ignore = true),
    })
    public abstract ProductCompletePicItem toPicItem(Dict entity);

    public abstract List<ProductCompletePicItem> listToPicItem(List<Dict> dicts);

}
