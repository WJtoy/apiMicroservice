package com.wolfking.jeesite.modules.api.entity.receipt.validate;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * 好评单详情
 */
public class AppGetOrderValidateStandardResponse extends AppBaseEntity {
    /**
     * 鉴定结果选项
     */
    @Getter
    @Setter
    private List<AppDict> checkValidateResults = Lists.newArrayList();
    /**
     * 包装鉴定选项
     */
    @Getter
    @Setter
    private List<AppDict> packValidateResults = Lists.newArrayList();

    /**
     * 鉴定图片
     */
    @Getter
    @Setter
    private List<PicItem> pics = Lists.newArrayList();


    public static class PicItem extends AppBaseEntity {
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
