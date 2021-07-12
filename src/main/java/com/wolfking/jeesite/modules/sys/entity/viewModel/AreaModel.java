package com.wolfking.jeesite.modules.sys.entity.viewModel;

import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.Data;

@Data
public class AreaModel extends Area {
    // 下级区域列表
    private java.util.List<Area> subAreas;
}
