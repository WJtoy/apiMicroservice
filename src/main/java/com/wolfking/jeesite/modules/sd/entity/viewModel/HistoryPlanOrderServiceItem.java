package com.wolfking.jeesite.modules.sd.entity.viewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 历史派单记录视图模型之服务明细
 * @date 2019-10-30 09:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPlanOrderServiceItem {
    private String brand;
    private long productId;
    private String productName;
    private String productSpec;
    private int qty;
    private long serviceTypeId;
    private String serviceTypeName;
    //安维费用
    private double engineerServiceCharge;// 安维服务费(应付)
    private double engineerTravelCharge;// 安维远程费(应付)
    private double engineerExpressCharge;// 快递费（应付）
    private double engineerMaterialCharge;// 安维配件费(应付)
    private double engineerOtherCharge;// 安维其它费用(应付)
    // 安维合计(应付) = 安维远程费(应付) + 安维远程费(应付) + 安维配件费(应付) + 安维其它费用(应付)
    private double engineerTotalCharge;
    public double getEngineerTotalCharge(){
        return BigDecimal.valueOf(engineerServiceCharge+engineerTravelCharge+engineerExpressCharge+engineerMaterialCharge+engineerOtherCharge).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
