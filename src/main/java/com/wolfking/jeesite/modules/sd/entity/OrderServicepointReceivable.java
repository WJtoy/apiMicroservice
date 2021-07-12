package com.wolfking.jeesite.modules.sd.entity;

import com.kkl.kklplus.entity.md.MDServicePoint;
import com.kkl.kklplus.entity.sys.SysUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 网点应收应付表
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderServicepointReceivable implements Serializable {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 数据库分片
     */
    private String quarter;
    /**
     * 网点
     */
    private MDServicePoint servicePoint;
    /**
     * 应收应付项目
     */
    private int itemNo =0;

    /**
     * 关联单号，如好评单号
     */
    private String formNo;
    /**
     * 金额
     */
    private double amount = 0.00;
    /**
     * 备注
     */
    private String remark;

    private SysUser createBy;
    private long createAt;
    private SysUser udpateBy;
    private long updateAt;
    private int delFlag;
}

