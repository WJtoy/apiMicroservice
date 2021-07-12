package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import lombok.Data;

@Data
public class UrgentChargeModel {
    @JsonAdapter(UrgentSimpleAdapter.class)
    private UrgentLevel urgentLevel;// 加急等级
    private Double chargeIn = 0.0;// 应付
    private Double chargeOut = 0.0;// 应收

    private Long areaId;//区域 ryan
}
