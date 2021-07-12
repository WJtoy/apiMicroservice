package com.wolfking.jeesite.modules.sd.entity.mapper;

import com.wolfking.jeesite.modules.sd.entity.MaterialItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.sd.entity.MaterialReturn;
import com.wolfking.jeesite.modules.sd.entity.MaterialReturnItem;
import org.mapstruct.*;

/**
 * 配件申请单复制
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class MaterialMasterMapper {

    /**
     * 复制配件单
     * 不复制明细及图片
     */
    @Mappings({
            @Mapping(target = "items",ignore = true),
            @Mapping(target = "attachments",ignore = true),
            @Mapping(target = "mateirals",ignore = true),
            @Mapping(target = "pendingType",ignore = true),
            @Mapping(target = "pendingDate",ignore = true),
            @Mapping(target = "pendingContent",ignore = true),
            @Mapping(target = "totalPrice",ignore = true)
    })
    public abstract MaterialMaster clone(MaterialMaster model);

    /**
     * 配件单转返件单
     * 不包含配件明细及图片
     */
    @Mappings({
            @Mapping(target = "masterId",source = "id"),
            @Mapping(target = "returnNo",ignore = true),
            @Mapping(target = "expressCompany",ignore = true),
            @Mapping(target = "expressNo",ignore = true),
            @Mapping(target = "pendingType",ignore = true),
            @Mapping(target = "pendingDate",ignore = true),
            @Mapping(target = "pendingContent",ignore = true),
            @Mapping(target = "receivor",ignore = true),
            @Mapping(target = "receivorPhone",ignore = true),
            @Mapping(target = "receivorAddress",ignore = true),
            @Mapping(target = "signAt",ignore = true),
            @Mapping(target = "closeBy",ignore = true),
            @Mapping(target = "closeDate",ignore = true),
            @Mapping(target = "closeRemark",ignore = true),
            @Mapping(target = "delFlag",ignore = true),
            @Mapping(target = "createBy",ignore = true),
            @Mapping(target = "createDate",ignore = true),
            @Mapping(target = "updateBy",ignore = true),
            @Mapping(target = "updateDate",ignore = true),
            @Mapping(target = "items",ignore = true),
            @Mapping(target = "attachments",ignore = true),
            //@Mapping(target = "remarks",ignore = true),
    })
    public abstract MaterialReturn toReturnForm(MaterialMaster model);

    /**
     * 配件单明细转返件单明细
     */
    @Mappings({
            @Mapping(target = "id",ignore = true),
            @Mapping(target = "createBy",ignore = true),
            @Mapping(target = "createDate",ignore = true),
            @Mapping(target = "updateBy",ignore = true),
            @Mapping(target = "updateDate",ignore = true),
            @Mapping(target = "formId",ignore = true),
            @Mapping(target = "product",ignore = true),
            @Mapping(target = "sqlMap",ignore = true),
            @Mapping(target = "isNewRecord",ignore = true),
            @Mapping(target = "delFlag",ignore = true)
    })
    public abstract MaterialReturnItem toReturnItem(MaterialItem model);

}
