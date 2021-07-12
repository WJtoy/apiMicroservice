package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.PlanRadiusAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 派单区域半径设定
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonAdapter(PlanRadiusAdapter.class)
public class PlanRadius extends LongIDDataEntity<PlanRadius> {
    private Integer radius1 = 0;  //半径1
    private Integer radius2 = 0;  //半径2
    private Integer radius3 = 0;  //半径3
    @JsonAdapter(AreaSimpleAdapter.class)  // 区域
    private Area area;
    private Integer autoPlanFlag;  //自动派单标志
    private List<Long> areaIdList = Lists.newArrayList(); //区域Id集合

    public PlanRadius(long areaId){
        if(this.area == null){
            this.area = new Area(areaId);
        }else{
            this.area.setId(areaId);
        }
    }

}
