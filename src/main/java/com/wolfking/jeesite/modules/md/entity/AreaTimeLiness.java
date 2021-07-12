package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.utils.AreaTimeLinessAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.Data;

/**
 * 区域产品时效奖励开关表
 */
@Data
@JsonAdapter(AreaTimeLinessAdapter.class)
public class AreaTimeLiness extends LongIDDataEntity<AreaTimeLiness> {
    public static final int IS_SEARCHING_NO     = 0;
    public static final int IS_SEARCHING_YES    = 1;

    private Area area;
    private int isOpen = 0;
    private long productCategoryId;

    private Integer isSearching; //是否从数据库中搜索数据

    public boolean isSearching() {
        if (isSearching != null && isSearching == IS_SEARCHING_YES) {
            return true;
        }
        else {
            return false;
        }
    }
    public int getIsSearchingYes() {
        return IS_SEARCHING_YES;
    }
}
