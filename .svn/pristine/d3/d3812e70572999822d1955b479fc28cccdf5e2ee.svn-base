package com.wolfking.jeesite.modules.api.entity.receipt.validate;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetOrderValidateInfoResponse extends AppBaseEntity {

    @Getter
    @Setter
    private Long productId = 0L;
    @Getter
    @Setter
    private String productName = "";
    @Getter
    @Setter
    private String productSn = "";

    @Getter
    @Setter
    private AppDict errorType = new AppDict();
    @Getter
    @Setter
    private AppDict errorCode = new AppDict();
    @Getter
    @Setter
    private AppDict actionCode = new AppDict();

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
     */
    @Getter
    @Setter
    private List<AppDict> checkValidateResultValues = Lists.newArrayList();
    /**
     * 检验鉴定详情
     */
    @Getter
    @Setter
    private String checkValidateDetail = "";
    /**
     * 包装鉴定结果
     */
    @Getter
    @Setter
    private List<AppDict> packValidateResultValues = Lists.newArrayList();
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
     * 已上传的好评图片
     */
    @Getter
    @Setter
    private List<PicItem> pics = Lists.newArrayList();

    public static class PicItem {
        @Getter
        @Setter
        private String code;
        @Getter
        @Setter
        private String name;
        @Getter
        @Setter
        private String url;
        public PicItem() {}

        public PicItem(String code, String name, String url) {
            this.code = code;
            this.name = name;
            this.url = url;
        }
    }
}
