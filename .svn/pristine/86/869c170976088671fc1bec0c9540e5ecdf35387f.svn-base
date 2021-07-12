package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.ServicePointSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * 网点订单数据表
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServicePoint implements Serializable {

    public enum PlanType{
        //无(0)
        NONE,
        //自动派单(1)
        AUTO,
        //APP抢单(2)
        APP,
        //客服(3)
        KEFU,
        //突击(4)
        CRUSH
    }

    public OrderServicePoint(Long id){
        this.orderId = id;
    }

    public OrderServicePoint(Long orderId,String orderNo, String quarter){
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.quarter = quarter;
    }

    private Long id;
    // 销售渠道
    private int orderChannel;
    // 工单来源
    private int dataSource;
    private String quarter = "";
    private Long orderId;
    private String orderNo;

    // 订单类型   1:安装单   2:维修单 (数据字典:order_service_type)
    private int orderServiceType = 0;
    private String userName;
    private String servicePhone;
    private String serviceAddress;
    //区域(区县级)
    private Area area;

    private Dict status;
    private Integer subStatus;

    private Long servicePointId;
    private Engineer engineer;

    //当前网点标记
    private int isCurrent = 0;

    // 上门标记
    private Integer serviceFlag;

    // 派单时间
    private Date planDate;
    private long planAt;
    // 派单次序，1:首次派单
    private int planOrder;
    // 派单方式，参考：PlanType
    private int planType;

    // 预约时间
    private Date appointmentDate;
    private long appointmentAt;

    // 网点处理时间
    private Date reservationDate;
    private long reservationAt;
    // 停滞原因
    private Integer pendingType;

    // 完成时间
    private Date closeDate;
    private long closeAt;
    // 对账时间
    private Date chargeDate;
    private long chargeAt;
    // 创建者
    protected User createBy;

    // 创建日期
    protected Date createDate;

    // 更新者
    protected User updateBy;

    // 更新日期
    protected Date updateDate;

    // 删除标记，1：删除
    private Integer delFlag = 1;

    // 催单标记(状态)
    private int reminderFlag;
    // 排序，根据reminderFlag算出,规则如下：
    // reminderFalg = 1,-> 2
    // >0 , -> 1
    // 其他，-> 0
    private int reminderSort;
    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
        this.reminderSort = reminderFlag == 1?2:(reminderFlag>0?1:0);
    }

    // 安维账号类型，1-主账号
    private int masterFlag;
    // 异常标记
    private int abnormalyFlag;
    // 投诉标记
    private int complainFlag;
    //加急等级id
    private int urgentLevelId;
    // app完工类型
    private String appCompleteType;
    /*
    public static void main(String[] args) {
        int reminderFlag = 1;
        System.out.printf("sort: %d" ,reminderFlag == 1?2:(reminderFlag>0?1:0));
        reminderFlag = 3;
        System.out.println("");
        System.out.printf("sort: %d" ,reminderFlag == 1?2:(reminderFlag>0?1:0));
        reminderFlag = 0;
        System.out.println("");
        System.out.printf("sort: %d" ,reminderFlag == 1?2:(reminderFlag>0?1:0));
        Dict dict = null;
        int channel = Optional.ofNullable(dict).map(Dict::getIntValue).orElse(0);
        System.out.printf("channel: %d",channel);
    }
    */

}

