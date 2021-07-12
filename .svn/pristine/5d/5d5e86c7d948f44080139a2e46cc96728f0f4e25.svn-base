package com.wolfking.jeesite.ms.material.mq.entity.mapper;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.common.material.B2BMaterial;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.modules.sd.entity.MaterialAttachment;
import com.wolfking.jeesite.modules.sd.entity.MaterialItem;
import com.wolfking.jeesite.modules.sd.entity.MaterialMaster;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工单配件申请单转B2配件单
 * @author Ryan Lu
 * @version 1.0.0
 * @date 2019-09-09 17:42
 */
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class B2BMaterialMapper {

    @Mappings({
            @Mapping(target = "quarter",source = "quarter"),
            @Mapping(target = "kklMasterId",source = "id"),
            @Mapping(target = "kklMasterNo",source = "masterNo"),
            @Mapping(target = "kklOrderId",source = "orderId"),
            @Mapping(target = "kklOrderNo",source = "orderNo"),
            @Mapping(target = "orderNo",source = "thrdNo"),
            @Mapping(target = "userName",source = "userName"),
            @Mapping(target = "userMobile",source = "userPhone"),
            @Mapping(target = "userAddress",source = "userAddress"),
            @Mapping(target = "masterCount",source = "applyTime"),
            @Mapping(target = "description",source = "remarks"),
            @Mapping(target = "status",constant = "1"),//默认值
            @Mapping(target = "createById",source = "createBy.id"),
            @Mapping(target = "createDt",expression = "java(model.getCreateDate().getTime())"),
            @Mapping(target = "updateById",source = "createBy.id"),
            @Mapping(target = "returnFlag",source = "returnFlag"),
            @Mapping(target = "pics",ignore = true),
            @Mapping(target = "picsJson",ignore = true),
            @Mapping(target = "materItems",ignore = true),
            @Mapping(target = "materItemsJson",ignore = true),
            @Mapping(target = "createBy",ignore = true),
            @Mapping(target = "createDate",ignore = true),
            @Mapping(target = "updateBy",ignore = true),
            @Mapping(target = "updateDate",ignore = true),
            @Mapping(target = "updateDt",ignore = true),
            @Mapping(target = "delFlag",ignore = true),
            @Mapping(target = "dataSource",ignore = true),
            @Mapping(target = "page",ignore = true),
            @Mapping(target = "isNewRecord",ignore = true),
            @Mapping(target = "applyType",ignore = true),
    })
    public abstract B2BMaterial toB2BMaterialForm(MaterialMaster model);

    @AfterMapping
    protected void after(@MappingTarget final B2BMaterial joyoung, MaterialMaster model) {
        List<MaterialItem> items = model.getItems();
        List<B2BMaterial.MaterItem> materItems = Lists.newArrayListWithCapacity(ObjectUtils.isEmpty(items)?0:items.size());
        //items
        B2BMaterial.MaterItem materItem;
        for (MaterialItem item:model.getItems()){
            materItem= B2BMaterial.MaterItem.builder()
                    .productName(item.getProduct().getName())
                    .qty(item.getQty())
                    .materialDesc(item.getMaterial().getName())
                    .totalPrice(item.getTotalPrice())
                    .build();
            materItems.add(materItem);
        }
        joyoung.setMaterItems(materItems);
        items = null;
        //pics
        List<MaterialAttachment> attachments = model.getAttachments();
        if (!ObjectUtils.isEmpty(attachments)){
//            String picHost = Global.getConfig("userfiles.host");
            String picHost = ApiPropertiesUtils.getUserFiles().getHost();
            List<String> pics = attachments.stream().map(t-> String.format("%s/%s",picHost,t.getFilePath()))
                    .collect(Collectors.toList());
            joyoung.setPics(pics);
            pics=null;
        }
        attachments = null;
    }

}