package com.wolfking.jeesite.ms.mapper.common;

import com.kkl.kklplus.entity.common.MSPage;
import com.wolfking.jeesite.common.persistence.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PageMapper {

    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    @Mappings({
            @Mapping(source = "rowCount",target = "count"),//属性名不一致映射
            @Mapping(target = "pageCount",ignore=true)
    })
    Page toPage(MSPage entity);

    @Mappings({
            @Mapping(source = "count",target = "rowCount")
    })
    MSPage toMSPage(Page entity);

}
