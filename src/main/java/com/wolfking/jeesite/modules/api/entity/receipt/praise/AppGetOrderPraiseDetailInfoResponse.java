package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.kkl.kklplus.entity.praise.PraiseStatusEnum;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;


/**
 * 好评单详情
 */
public class AppGetOrderPraiseDetailInfoResponse extends AppBaseEntity {

    /**
     * 好评单状态：0 - 未添加好评单，10 - 创建，20 - 待审核， 30 - 驳回， 40 审核通过，50 - 已取消， 60 - 无效
     */
    @Getter
    @Setter
    private AppDict praiseStatus = new AppDict(String.valueOf(PraiseStatusEnum.NONE.code), PraiseStatusEnum.NONE.msg);
    /**
     * 驳回或取消原因
     */
    @Getter
    @Setter
    private AppDict rejectionCategory = new AppDict("0", "");
    /**
     * 网点好评费
     */
    @Getter
    @Setter
    private Double praiseFee = 0.0;
    /**
     * 好评单标准
     */
    @Getter
    @Setter
    private AppOrderPraiseFeeStandard standard;
}
