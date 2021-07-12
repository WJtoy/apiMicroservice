package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.entity.TimelinessLevel;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import lombok.Data;

@Data
public class TimelinessChargeModel {
    @JsonAdapter(TimelinessSimpleAdapter.class)
    private TimelinessLevel timelinessLevel;//时效等级
    private Double chargeIn = 0.0;// 应收
    private Double chargeOut = 0.0;// 应付
}
