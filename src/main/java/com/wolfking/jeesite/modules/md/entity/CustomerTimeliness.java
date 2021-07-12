package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.viewModel.AreaTimelinessModel;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import lombok.Data;

import java.util.List;

@Data
public class CustomerTimeliness  extends LongIDDataEntity<CustomerTimeliness> {

    @JsonAdapter(CustomerSimpleAdapter.class)
    private Customer customer;
    @JsonAdapter(AreaSimpleAdapter.class)
    private Area area;
    @JsonAdapter(DictSimpleAdapter.class)
    private TimelinessLevel timelinessLevel;//时效等级

    private Double chargeIn = 0.0;//应收
    private Double chargeOut = 0.0;//应付

    @GsonIgnore
    private List<AreaTimelinessModel> areaTimelinessModelList;//辅助，页面保存多个等级价格
}
