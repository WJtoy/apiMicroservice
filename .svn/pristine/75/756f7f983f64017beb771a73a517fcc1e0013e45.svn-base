package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetOrderPraiseListItemNewResponse extends AppBaseEntity {

    /**
     * 好评单ID
     */
    @Getter
    @Setter
    private Long praiseId = 0L;
    /**
     * 工单号
     */
    @Getter
    @Setter
    private String orderNo = "";
    /**
     * 用户姓名
     */
    @Getter
    @Setter
    private String userName = "";
    /**
     * 用户电话
     */
    @Getter
    @Setter
    private String servicePhone = "";
    /**
     * 驳回或取消原因
     */
    @Getter
    @Setter
    private AppDict rejectionCategory = new AppDict("0", "");
    /**
     * 已上传的好评图片
     */
    @Getter
    @Setter
    private List<PicItem> pics = Lists.newArrayList();
    /**
     * 好评单创建时间
     */
    @Getter
    @Setter
    private Long createDate = 0L;

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
         * 图片的URL地址
         */
        @Getter
        @Setter
        private String url;

        public PicItem() {
        }

        public PicItem(String code, String name, String url) {
            this.code = code;
            this.name = name;
            this.url = url;
        }
    }
}
