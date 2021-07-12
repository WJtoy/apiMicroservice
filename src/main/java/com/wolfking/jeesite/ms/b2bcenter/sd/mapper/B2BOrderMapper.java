package com.wolfking.jeesite.ms.b2bcenter.sd.mapper;

import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrder;
import com.wolfking.jeesite.ms.b2bcenter.sd.entity.B2BOrderVModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public abstract class B2BOrderMapper {

    /**
     * @param order
     * @return
     */
    @Mappings({
            @Mapping(source = "parentBizOrderId",target = "parentBizOrderId"),//不知什么原因此属性不转换
    })
    public abstract B2BOrderVModel toB2BOrderVModel(B2BOrder order);

}
