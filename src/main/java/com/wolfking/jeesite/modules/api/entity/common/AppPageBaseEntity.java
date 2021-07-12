package com.wolfking.jeesite.modules.api.entity.common;

import lombok.Getter;
import lombok.Setter;

public class AppPageBaseEntity extends AppBaseEntity {

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

}
