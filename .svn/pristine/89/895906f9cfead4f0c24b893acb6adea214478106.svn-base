package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 产品安装规范
 */
public class RestGetProductFixSpec implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称
     */
    @Getter
    @Setter
    private String productName;

    /**
     * 产品规格
     */
    @Getter
    @Setter
    private String productSpec;

    /**
     * 安装规范
     */
    @Getter
    @Setter
    private String fixSpec;
    /**
     * 安装规范的html
     */
    @Getter
    @Setter
    private String fixSpecHtml;
    /**
     * 视频链接
     */
    @Getter
    @Setter
    private String videoUrl;

    /**
     * 产品图片
     */
    @Getter
    @Setter
    private List<PicItem> pics = Lists.newArrayList();

    public static class PicItem {

        /**
         * 图片的URL地址
         */
        @Getter
        @Setter
        private String url;

        public PicItem() {}

        public PicItem(String url) {
            this.url = url;
        }
    }
}
