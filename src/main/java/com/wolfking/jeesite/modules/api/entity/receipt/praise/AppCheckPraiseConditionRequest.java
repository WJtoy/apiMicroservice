package com.wolfking.jeesite.modules.api.entity.receipt.praise;

import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

public class AppCheckPraiseConditionRequest extends AppBaseEntity {

    @Getter
    @Setter
    private Long orderId = 0L;

    @Getter
    @Setter
    private String quarter = "";

    @Getter
    @Setter
    private Integer dataSource = 0;
}
