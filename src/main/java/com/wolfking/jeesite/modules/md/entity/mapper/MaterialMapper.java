package com.wolfking.jeesite.modules.md.entity.mapper;

import com.wolfking.jeesite.modules.md.entity.CustomerMaterial;
import com.wolfking.jeesite.modules.md.entity.Material;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import org.mapstruct.*;

/**
 * 配件设定转换
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class MaterialMapper {

    @Mappings({
            @Mapping(target = "product",source = "product"),
            @Mapping(target = "id",source = "material.id"),
            @Mapping(target = "name",source = "material.name"),
            @Mapping(target = "isReturn",source = "isReturn"),
            @Mapping(target = "price",source = "price"),
            @Mapping(target = "model",ignore = true),
            @Mapping(target = "updateBy",ignore = true),
            @Mapping(target = "updateDate",ignore = true),
            @Mapping(target = "delFlag",ignore = true),
            @Mapping(target = "createBy",ignore = true),
            @Mapping(target = "createDate",ignore = true),
            @Mapping(target = "remarks",ignore = true),
            @Mapping(target = "productList",ignore = true),
            @Mapping(target = "materialCategory",ignore = true)
    })
    public abstract Material customerToMaterial(CustomerMaterial model);

}
