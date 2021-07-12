package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品时效奖励设定
 */
@Data
@NoArgsConstructor
public class TimeLinessPrice extends LongIDDataEntity<TimeLinessPrice> {
    public static String TIME_LINESS_LEVEL = "Time_Liness_Level";
    public Dict timeLinessLevel;//时效等级(订单在数据字典,type:Time_Liness_Level)//因为这里需要缓存数据字典的descrption值所有不能用DictSimpleAdapter
    private ProductCategory category = new ProductCategory();//产品类别
    private Double amount=0.00;//奖励金额
}
