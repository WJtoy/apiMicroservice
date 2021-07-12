package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetOrderPraiseInfoResponse extends AppBaseEntity {

    /**
     * 好评单状态：0 - 未添加好评单，10 - 创建，20 - 待审核， 30 - 驳回， 40 审核通过，50 - 已取消
     */
    @Getter
    @Setter
    private AppDict praiseStatus = new AppDict();

    /**
     * 驳回或取消原因
     */
    @Getter
    @Setter
    private AppDict rejectionCategory = new AppDict("0", "");

    /**
     * 备注
     */
    @Getter
    @Setter
    private String remarks = "";

    /**
     * 好评内容要求
     */
    @Getter
    @Setter
    private String praiseRequirement = "";

    /**
     * 网点好评费
     */
    @Getter
    @Setter
    private Double praiseFee = 0.0;

    /**
     * 好评图片数量要求
     */
    @Getter
    @Setter
    private Integer picCount = 0;

    /**
     * 已上传的好评图片
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

        public PicItem() {
        }

        public PicItem(String url) {
            this.url = url;
        }
    }
}
