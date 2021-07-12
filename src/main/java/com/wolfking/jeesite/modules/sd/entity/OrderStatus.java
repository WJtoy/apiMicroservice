package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.sd.utils.OrderConditionAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderStatusAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态
 */
@JsonAdapter(OrderStatusAdapter.class)
@Data
@NoArgsConstructor
public class OrderStatus implements Serializable {

    public static int CANCEL_RESPONSIBLE_CUSTOMER = 1; //工单取消类型 - 厂商
    public static int CANCEL_RESPONSIBLE_CUSTOMER_APPLY_RETURN = 51;//厂家(电商)通知取消
    public static int CANCEL_RESPONSIBLE_B2B = 100; //工单取消类型 - B2B退单
    public static int CANCEL_RESPONSIBLE_B2B_REASSIGN = 101;//工单取消类型 - B2B退单 - 改派

    public OrderStatus(Long orderId) {
        this.orderId = orderId;
    }

    private Long orderId;
    private String quarter = "";//数据库分片，与订单相同
    //订单开单审批,由客户主账号审批子账号订单
    private Integer customerApproveFlag = 0;// 客户开单审批标示
    private User customerApproveBy;// 审批人
    private Date customerApproveDate;// 审批日期 --> Order也定义了

    //接单(客服)
//    private Integer acceptFlag = 0;// 接单标示
//    private User acceptBy;// 接单人 ?
    private Date acceptDate;// 接单日期

    //派单(客服)
//    private Integer planFlag = 0;// 派单标示 1:已派单
    private User planBy;// 派单人
    private Date planDate;// 派单日期
    private String planComment = "";// 派单备注

    private Date firstContactDate;// 首次联系用户时间

    //安维
//    private ServicePoint servicePoint; //安维网点,servicepoint_id,对应原来 engineer:安维人(主账号)
//    private User engineer;// 安维主账号派单给安维子账号 (engineer_id,原来的sub engineer)

    //上门服务
    private Integer serviceFlag = 0;// 上门服务标示
    private Date serviceDate;// 上门服务日期
    private String serviceComment = "";// 上门服务备注

    private Integer serviceTimes = 0;// 累计上门次数,order表也有

    //关闭,客服关闭或者用户回复自动关闭
    private Integer closeFlag = 0;// 关闭状态
    private User closeBy;// 关闭人
    private Date closeDate;// 关闭日期

    //    private Integer chargeFlag = 0;// 结帐单标示
    private User chargeBy;// 生成对帐单人
    private Date chargeDate;// 生成对帐单时间 --> Order也定义了

    private Date engineerInvoiceDate;// 客户付款时间
    private Date customerInvoiceDate;// 安维结帐时间

    //取消订单
    private Integer cancelSponsor = 0;// 退单发起方,1:客户 2:KKL
    private Dict cancelResponsible;// 退单责任方（调整为记录退单的类型，在退单明细报表加过滤区分)
    private User cancelApplyBy;// 退单申请人
    private Date cancelApplyDate;// 退单申请日期
    private String cancelApplyComment = "";// 退单申请原因

    private Integer cancelApproveFlag = 0;// 退单审核标示 0:没有任何操作，1：通过审核 2：驳回
    private User cancelApproveBy;// 退单审核人
    private Date cancelApproveDate;// 退单审核日期
    private Date urgentDate; //加急开始日期

    //催单 2019-08-13
    private Integer reminderStatus;
    private Long reminderCreateBy;
    private Long reminderCreateAt;

    //投诉 2019-08-19
    /**
     * 投诉标识
     */
    private Integer complainFlag = 0;

    /**
     * 投诉时间
     */
    private Long complainAt = 0L;

    /**
     * 投诉人
     */
    private String complainBy = "";

    /**
     * 投诉状态
     */
    private Integer complainStatus = 0;



}
