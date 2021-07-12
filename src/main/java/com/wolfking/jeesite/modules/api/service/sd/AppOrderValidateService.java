/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.api.service.sd;

import cn.hutool.core.util.ObjectUtil;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.validate.OrderValidate;
import com.kkl.kklplus.entity.validate.ValidateCreatorTypeEnum;
import com.kkl.kklplus.entity.validate.ValidatePicItem;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.common.utils.BitUtils;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.api.entity.receipt.validate.AppGetOrderValidateInfoResponse;
import com.wolfking.jeesite.modules.api.entity.receipt.validate.AppGetOrderValidateStandardResponse;
import com.wolfking.jeesite.modules.api.entity.receipt.validate.AppSaveOrderValidateJsonParameterRequest;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sd.utils.OrderPicUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerErrorActionService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerErrorCodeService;
import com.wolfking.jeesite.ms.providermd.service.MSCustomerErrorTypeService;
import com.wolfking.jeesite.ms.providermd.service.MSProductService;
import com.wolfking.jeesite.ms.validate.service.MSOrderValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * App工单好评
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AppOrderValidateService extends LongIDBaseService {
    @Autowired
    private MSOrderValidateService msOrderValidateService;
    @Autowired
    private MSCustomerErrorTypeService msCustomerErrorTypeService;
    @Autowired
    private MSCustomerErrorCodeService msCustomerErrorCodeService;
    @Autowired
    private MSCustomerErrorActionService msCustomerErrorActionService;
    @Autowired
    private MSProductService msProductService;

    /**
     * 获取鉴定标准
     */
    public AppGetOrderValidateStandardResponse getOrderValidateStandard() {
        AppGetOrderValidateStandardResponse result = new AppGetOrderValidateStandardResponse();
        List<Dict> checkValidateResultList = msOrderValidateService.getCheckValidateResultList();
        if (ObjectUtil.isNotEmpty(checkValidateResultList)) {
            List<AppDict> checkValidateResults = checkValidateResultList.stream().map(i -> new AppDict(i.getValue(), i.getLabel())).collect(Collectors.toList());
            result.setCheckValidateResults(checkValidateResults);
        }
        List<Dict> packValidateResultList = msOrderValidateService.getPackValidateResultList();
        if (ObjectUtil.isNotEmpty(packValidateResultList)) {
            List<AppDict> packValidateResults = packValidateResultList.stream().map(i -> new AppDict(i.getValue(), i.getLabel())).collect(Collectors.toList());
            result.setPackValidateResults(packValidateResults);
        }
        List<ValidatePicItem> picItems = msOrderValidateService.getValidatePicStandard();
        if (ObjectUtil.isNotEmpty(picItems)) {
            for (ValidatePicItem item : picItems) {
                result.getPics().add(new AppGetOrderValidateStandardResponse.PicItem(item.getCode(), item.getName(), item.getUrl()));
            }
        }
        return result;
    }

    /**
     * 获取鉴定单信息
     */
    public AppGetOrderValidateInfoResponse getOrderValidateInfo(Long orderId, String quarter) {
        AppGetOrderValidateInfoResponse result = null;
        OrderValidate validate = msOrderValidateService.getLastOrderValidate(orderId, quarter);
        if (validate != null) {
            result = new AppGetOrderValidateInfoResponse();
            result.setProductId(validate.getProductId());
            Product product = msProductService.getProductByIdFromCache(validate.getProductId());
            if (product != null) {
                result.setProductName(product.getName());
            }
            result.setProductSn(validate.getProductSn());
            result.setIsFault(validate.getIsFault());
            result.setErrorDescription(validate.getErrorDescription());
            result.setCheckValidateDetail(validate.getCheckValidateDetail());
            result.setPackValidateDetail(validate.getPackValidateDetail());
            result.setReceiver(validate.getReceiver());
            result.setReceivePhone(validate.getReceivePhone());
            result.setReceiveAddress(validate.getReceiveAddress());

            if (validate.getErrorTypeId() != null && validate.getErrorTypeId() > 0) {
                MDErrorType errorType = msCustomerErrorTypeService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getErrorTypeId());
                if (errorType != null) {
                    result.setErrorType(new AppDict(errorType.getId().toString(), errorType.getName()));
                }
            }
            if (validate.getErrorCodeId() != null && validate.getErrorCodeId() > 0) {
                MDErrorCode errorCode = msCustomerErrorCodeService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getErrorCodeId());
                if (errorCode != null) {
                    result.setErrorCode(new AppDict(errorCode.getId().toString(), errorCode.getName()));
                }
            }
            if (validate.getActionCodeId() != null && validate.getActionCodeId() > 0) {
                MDActionCode actionCode = msCustomerErrorActionService.getByProductIdAndCustomerIdFromCache(validate.getCustomerId(), validate.getProductId(), validate.getActionCodeId());
                if (actionCode != null) {
                    result.setActionCode(new AppDict(actionCode.getId().toString(), actionCode.getAnalysis()));
                }
            }

            Dict dict;
            List<String> checkValidateResultValues = BitUtils.getPositions(validate.getCheckValidateResult(), String.class);
            if (ObjectUtil.isNotEmpty(checkValidateResultValues)) {
                Map<String, Dict> checkValidateResultMap = msOrderValidateService.getCheckValidateResultMap();
                for (String value : checkValidateResultValues) {
                    dict = checkValidateResultMap.get(value);
                    if (dict != null) {
                        result.getCheckValidateResultValues().add(new AppDict(dict.getValue(), dict.getLabel()));
                    }
                }
            }
            List<String> packValidateResultValues = BitUtils.getPositions(validate.getPackValidateResult(), String.class);
            if (ObjectUtil.isNotEmpty(packValidateResultValues)) {
                Map<String, Dict> packValidateResultMap = msOrderValidateService.getPackValidateResultMap();
                for (String value : packValidateResultValues) {
                    dict = packValidateResultMap.get(value);
                    if (dict != null) {
                        result.getPackValidateResultValues().add(new AppDict(dict.getValue(), dict.getLabel()));
                    }
                }
            }

            if (ObjectUtil.isNotEmpty(validate.getPicItems())) {
                for (ValidatePicItem item : validate.getPicItems()) {
                    result.getPics().add(new AppGetOrderValidateInfoResponse.PicItem(item.getCode(), item.getName(), OrderPicUtils.getPraisePicUrl(item.getUrl())));
                }
            }
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void saveOrderValidateInfo(Long orderId, String quarter, AppSaveOrderValidateJsonParameterRequest params,
                                      List<ValidatePicItem> picItems, User user) {
        OrderValidate validate = new OrderValidate();
        validate.setProductId(params.getProductId());
        validate.setProductSn(params.getProductSn());
        validate.setProductIntSn("");
        validate.setIsFault(params.getIsFault());
        validate.setErrorTypeId(params.getErrorTypeId());
        validate.setErrorCodeId(params.getErrorCodeId());
        validate.setActionCodeId(params.getActionCodeId());
        validate.setErrorDescription(params.getErrorDescription());
        String[] checkValidateResultValues = StringUtils.split(params.getCheckValidateResultValues(), ",");
        validate.setCheckValidateResult(BitUtils.markedAndToTags(Arrays.stream(checkValidateResultValues).map(StringUtils::toInteger).collect(Collectors.toList())));
        validate.setCheckValidateDetail(params.getCheckValidateDetail());
        String[] packValidateResultValues = StringUtils.split(params.getPackValidateResultValues(), ",");
        validate.setPackValidateResult(BitUtils.markedAndToTags(Arrays.stream(packValidateResultValues).map(StringUtils::toInteger).collect(Collectors.toList())));
        validate.setPackValidateDetail(params.getPackValidateDetail());
        validate.setReceiver(params.getReceiver());
        validate.setReceivePhone(params.getReceivePhone());
        validate.setReceiveAddress(params.getReceiveAddress());
        validate.setPicItems(picItems);
        msOrderValidateService.createOrderValidate(orderId, quarter, validate, ValidateCreatorTypeEnum.SERVICE_POINT, user, new Date());
    }
}
