package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Lists;
import com.kkl.kklplus.entity.md.MDMaterialRequirement;
import lombok.Data;

import java.util.List;

@Data
public class MaterialRequire {
    private List<MDMaterialRequirement> requirementList= Lists.newArrayList();
}
