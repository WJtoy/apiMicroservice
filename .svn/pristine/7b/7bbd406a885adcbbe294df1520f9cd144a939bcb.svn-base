package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.viewModel.TimelinessSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.Date;

@Data
@NoArgsConstructor
public class TimelinessLevel extends LongIDDataEntity<TimelinessLevel> {
    private String name = "";
    @Min(value = 0,message = "应收不能小于0")
    private Double chargeIn = 0.00;

    @Min(value = 0,message = "应付不能小于0")
    private Double chargeOut = 0.00;

    private Integer sort;//等级排序

    public TimelinessLevel(Long id) {
        super(id);
    }
}
