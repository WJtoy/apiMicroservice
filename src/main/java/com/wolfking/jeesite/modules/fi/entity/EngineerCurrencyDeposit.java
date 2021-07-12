package com.wolfking.jeesite.modules.fi.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import lombok.Data;

/**
 * @Auther wj
 * @Date 2021/2/22 15:21
 */
@Data
public class EngineerCurrencyDeposit extends LongIDDataEntity<EngineerCurrency> {

    private ServicePoint servicePoint;
    private Integer currencyType;
    private String currencyNo;
    private Double beforeBalance;
    private Double balance;
    private Double amount;
    private Integer paymentType;
    private Integer actionType;
    private String quarter;
}
