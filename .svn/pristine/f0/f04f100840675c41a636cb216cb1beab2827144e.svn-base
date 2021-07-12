package com.wolfking.jeesite.modules.sd.entity;

import com.kkl.kklplus.entity.md.MDAppFeedbackType;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 工单意见跟踪日志表
 */
@Builder
//@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOpitionTrace implements Serializable {

    private static final long serialVersionUID = 1578464768114L;

    //id，主键
    private Long id;
    // 来源 1-工单系统 2-APP 3-短信 4-语音回访
    private Integer channel;
    //网点id
    private Long servicePointId;
    //同网点同意见类型(opinionId)的次数
    private int times;
    //同同意类型总的次数
    private int totalTimes;
    //订单id
    private Long orderId;
    //分片，与订单相同
    private String quarter;
    //意见id opinion_type：1，2 时，对应md_app_feedback_type.id
    private Integer opinionId;
    //意见上阶id，如果没有上阶，保存和opinion_id相同
    private Integer parentId;
    //反馈类型上阶
    MDAppFeedbackType parent;
    //反馈值
    private Integer opinionValue;
    //反馈文本
    private String opinionLabel;
    //反馈类型值
    private Integer opinionType;
    //预约日期
    private long appointmentAt;
    //备注
    private String remark;
    // 创建人
    private User createBy;
    //创建日期
    private long createAt;
    //标记异常
    private int isAbnormaly;
}
