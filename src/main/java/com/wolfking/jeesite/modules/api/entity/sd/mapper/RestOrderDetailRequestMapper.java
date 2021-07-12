package com.wolfking.jeesite.modules.api.entity.sd.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.md.dto.MDErrorCodeDto;
import com.wolfking.jeesite.common.config.Global;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.Encodes;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetail;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderDetailInfo;
import com.wolfking.jeesite.modules.api.entity.sd.RestOrderItem;
import com.wolfking.jeesite.modules.api.entity.sd.request.RestDetailRequest;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.service.ProductService;
import com.wolfking.jeesite.modules.md.service.ServiceTypeService;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.utils.AreaUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.utils.MSDictUtils;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Api提交的上门服务内容转订单上门服务
 */
@Component
public class RestOrderDetailRequestMapper extends CustomMapper<RestDetailRequest, OrderDetail>{

    @Override
    public void mapAtoB(RestDetailRequest a, OrderDetail b, MappingContext context) {
        if(b == null){
            b = new OrderDetail();
        }
        b.setId(Long.valueOf(a.getId()));
        b.setOrderId(Long.valueOf(a.getOrderId()));
        b.setQuarter(a.getQuarter());
        b.setRemarks(StringUtils.toString(a.getRemarks()));
        b.setServiceCategory(new Dict(a.getServiceCategoryId(),a.getServiceCategoryName()));
        MDErrorType errorType = new MDErrorType();
        errorType.setId(StringUtils.toLong(a.getErrorCodeId()));
        errorType.setName(StringUtils.toString(a.getErrorTypeName()));
        b.setErrorType(errorType);
        MDErrorCode errorCode = new MDErrorCode();
        errorCode.setId(StringUtils.toLong(a.getErrorCodeId()));
        errorCode.setName(StringUtils.toString(a.getErrorCodeName()));
        b.setErrorCode(errorCode);
        MDActionCode actionCode = new MDActionCode();
        actionCode.setId(StringUtils.toLong(a.getActionCodeId()));
        actionCode.setName(StringUtils.toString(a.getActionCodeName()));
        b.setActionCode(actionCode);
        b.setOtherActionRemark(StringUtils.toString(a.getOtherActionRemark()));
    }

    @Override
    public void mapBtoA(OrderDetail b, RestDetailRequest a, MappingContext context) {
    }

}
