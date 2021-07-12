package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

public class AppGetServicePointWriteOffChargeDetailRequest extends AppBaseEntity {
    /**
     * 退补单项目ID
     */
    @Getter
    @Setter
    private Long itemId = 0L;
    /**
     * 分片
     */
    @Getter
    @Setter
    private String quarter = "";
}
