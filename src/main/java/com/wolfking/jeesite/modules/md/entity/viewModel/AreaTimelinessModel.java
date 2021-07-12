package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.Data;

import java.util.List;

@Data
public class AreaTimelinessModel {
    @JsonAdapter(AreaSimpleAdapter.class)
    private Area area;//省级区域
    private List<TimelinessChargeModel> list;
}
