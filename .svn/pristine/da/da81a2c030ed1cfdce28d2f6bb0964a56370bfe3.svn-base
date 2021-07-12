package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppSaveOrderPraiseInfoJsonParameterNewRequest extends AppBaseEntity {

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
    /**
     * 类型 1-申请好评 2-不申请好评
     */
    @Getter
    @Setter
    private int praiseType = 0;
    /**
     * 完工类型
     */
    @Getter
    @Setter
    private String completeType = "";
    /**
     * 客户好评费
     */
    @Getter
    @Setter
    private Double customerApplyPraiseFee = 0.0;
    /**
     * 网点好评费
     */
    @Getter
    @Setter
    private Double servicePointApplyPraiseFee = 0.0;
    /**
     * 要上传的文件的位置编码（要与文件顺序一致）
     */
    @Getter
    @Setter
    private List<String> picFileCodes = Lists.newArrayList();
    /**
     * 原来已上传的好评图片
     */
    @Getter
    @Setter
    private List<UploadedPicItem> pics = Lists.newArrayList();
    public static class UploadedPicItem {
        /**
         * 图片位置编号
         */
        @Getter
        @Setter
        private String code = "";
        /**
         * 图片的URL地址
         */
        @Getter
        @Setter
        private String url = "";
    }

}
