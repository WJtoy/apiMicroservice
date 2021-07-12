package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

public class AppGetServicePointWithdrawListRequest extends AppBaseEntity {
    /**
     * 页码
     */
    @Getter
    @Setter
    private Integer pageNo = 1;
    /**
     * 页尺寸
     */
    @Getter
    @Setter
    private Integer pageSize = 10;

    /**
     * 年份，如2020
     */
    @Getter
    @Setter
    private Integer yearIndex = 0;
    /**
     * 月份，如6
     */
    @Getter
    @Setter
    private Integer monthIndex = 0;
}
