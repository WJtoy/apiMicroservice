package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.viewModel.AreaUrgentModel;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
public class UrgentCustomer extends LongIDDataEntity<UrgentCustomer> {

    @GsonIgnore
    private Customer customer;

    private UrgentLevel urgentLevel;

    @JsonAdapter(AreaSimpleAdapter.class)
    private Area area;//省级

    @Min(value = 0,message = "应收不能小于0")
    private Double chargeIn;
    @Min(value = 0,message = "应付不能小于0")
    private Double chargeOut;

    @GsonIgnore
    private List<AreaUrgentModel> list;//辅助 界面添加多个等级
}
