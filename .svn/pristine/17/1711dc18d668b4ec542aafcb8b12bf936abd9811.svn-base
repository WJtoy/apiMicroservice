package com.wolfking.jeesite.modules.md.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.viewModel.UrgentSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class UrgentLevel extends LongIDDataEntity<UrgentLevel> {

    public static final String MARK_BGCOLOR_HEX_STR_DEFAULT = "#BD4247";

    private String label="";
    @Min(value = 0,message = "应收不能小于0")
    private Double chargeIn;//应收
    @Min(value = 0,message = "应付不能小于0")
    private Double chargeOut;//应付

    private Integer sort;//不给默认值，不然界面加载默认值容易不输入

    private String markBgcolor = MARK_BGCOLOR_HEX_STR_DEFAULT; //加急标记的背景颜色

    public UrgentLevel(Long id) {
        super(id);
    }

    public UrgentLevel(Long id,String remarks) {
        super(id);
        this.remarks = remarks;
    }
}
