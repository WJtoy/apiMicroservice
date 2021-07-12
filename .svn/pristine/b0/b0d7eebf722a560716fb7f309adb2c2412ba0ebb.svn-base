package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetOrderPraiseListItemResponse extends AppBaseEntity {

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
     * 工单来源
     */
    @Getter
    @Setter
    private Integer dataSource = 0;

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
