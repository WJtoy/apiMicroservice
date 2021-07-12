package com.wolfking.jeesite.modules.api.entity.receipt.validate;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppSaveOrderValidateJsonParameterRequest extends AppBaseEntity {

    /**
     * 工单ID
     */
    @Getter
    @Setter
    private Long orderId = 0L;
    /**
     * 工单分片
     */
    @Getter
    @Setter
    private String quarter = "";
    @Getter
    @Setter
    private Long productId = 0L;
    @Getter
    @Setter
    private String productSn = "";

    @Getter
    @Setter
    private Long errorTypeId = 0L;
    @Getter
    @Setter
    private Long errorCodeId = 0L;
    @Getter
    @Setter
    private Long actionCodeId = 0L;

    /**
     * 是否故障：0 - 否，1 - 是
     */
    @Getter
    @Setter
    private Integer isFault = 0;
    /**
     * 故障描述
     */
    @Getter
    @Setter
    private String errorDescription = "";

    /**
     * 检验鉴定结果
     * 格式：1，2，3,..
     */
    @Getter
    @Setter
    private String checkValidateResultValues = "";
    /**
     * 检验鉴定结果详情
     */
    @Getter
    @Setter
    private String checkValidateDetail = "";
    /**
     * 包装鉴定结果
     * 格式：1，2，3,..
     */
    @Getter
    @Setter
    private String packValidateResultValues = "";
    /**
     * 包装鉴定详情
     */
    @Getter
    @Setter
    private String packValidateDetail = "";
    /**
     * 收货人姓名
     * */
    @Getter
    @Setter
    private String receiver = "";
    /**
     * 收货电话
     */
    @Getter
    @Setter
    private String receivePhone = "";
    /**
     * 收货地址
     */
    @Getter
    @Setter
    private String receiveAddress = "";

    /**
     * 要上传的文件的位置编码（要与文件顺序一致）
     */
    @Getter
    @Setter
    private List<String> picFileCodes = Lists.newArrayList();
}
