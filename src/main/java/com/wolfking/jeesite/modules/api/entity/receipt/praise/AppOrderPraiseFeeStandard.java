package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 好评单详情
 */
public class AppOrderPraiseFeeStandard extends AppBaseEntity {

    public static final String TIPS = "温馨提示：此处只用于上传好评照片，上传错误或虚假照片，考核200元/单；超过三次，取消接单权限。";

    /**
     * 温馨提示内容
     */
    @Getter
    @Setter
    private String tips = TIPS;
    /**
     * 好评要求
     */
    @Getter
    @Setter
    private String praiseRequirement = "";
    /**
     * 起始好评费
     */
    @Getter
    @Setter
    private Double minCustomerPraiseFee = 0.0;
    /**
     * 上限费用
     */
    @Getter
    @Setter
    private Double maxCustomerPraiseFee = 0.0;
    /**
     * 扣点
     */
    @Getter
    @Setter
    private Double discount = 0.0;
    /**
     * 需上传或已上传的好评图片
     */
    @Getter
    @Setter
    private List<PicItem> pics = Lists.newArrayList();

    /**
     * 实例图片
     */
    @Getter
    @Setter
    private List<ExamplePicItem> examplePics = Lists.newArrayList();

    public static class PicItem {
        /**
         * 图片位置编号
         */
        @Getter
        @Setter
        private String code = "";
        /**
         * 图片位置名称
         */
        @Getter
        @Setter
        private String name = "";
        /**
         * 好评图片费用`
         */
        @Getter
        @Setter
        private Double fee = 0.0;
        /**
         * 必选，0-否，1-是
         */
        @Getter
        @Setter
        private Integer mustFlag = 0;
        /**
         * 图片的URL地址
         */
        @Getter
        @Setter
        private String url = "";
        /**
         * 是否已上传标记（0 - 未上传， 1 - 已上传）
         */
        @Getter
        @Setter
        private Integer uploadFlag = 0;
    }

    public static class ExamplePicItem {
        /**
         * 图片位置编号
         */
        @Getter
        @Setter
        private String code = "";
        /**
         * 图片位置名称
         */
        @Getter
        @Setter
        private String name = "";
        /**
         * 图片的URL地址
         */
        @Getter
        @Setter
        private String url = "";
    }
}
