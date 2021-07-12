package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.google.common.collect.Lists;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppSaveOrderPraiseInfoJsonParameterRequest extends AppBaseEntity {

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

    /**
     * 是否有图片
     */
    public boolean hasPictures() {
        boolean flag = false;
        if (pics != null && !pics.isEmpty()) {
            for (PicItem item : pics) {
                if (StringUtils.isNotBlank(item.getUrl())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
