package com.wolfking.jeesite.modules.sd.entity;

import com.kkl.kklplus.entity.voiceservice.Fragment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单智能回访记录
 * @author Ryan
 * @date 2019/01/15 20:56
 */
@Data
@NoArgsConstructor
public class OrderVoiceTask implements Serializable {

    public enum TaskResult {

        ALL(0,"所有"),
        OK(1,"成功"),
        FAIL(2,"失败"),
        CANCELLED(3,"已取消");

        public int code;
        public String name;

        private TaskResult(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    private Long id;

    //任务类型
    //1.回访
    private int voiceType = 1;

    //订单id
    private Long orderId;

    //订单数据分片
    private String quarter;

    // 接通时间
    private long connectedAt;

    // 挂断时间
    private long disconnectedAt;

    // 客户名称
    private String userName = "";

    // 客户电话
    private String phone = "";

    // 项目模板名称
    private String projectCaption = "";

    // 通话时长(秒)
    private Integer talkTimes = 0;

    // 回访结果 1:成功 2:失败
    private int taskResult = 0;

    // 挂断原因 包含：
    // 对方挂机，本方挂机，关机，空号，正 在通话中，无法接通，
    // 暂停服务，用户正忙，拨号方式不正确， 来电提醒，其他
    private String endReason = "";

    // 类别
    private String  status = "";

    // 分值
    private Integer score = 0;

    // 标签
    private String labelling = "";

    // 标签列表
    private List<String> labels;

    //备注
    private String remark = "";

    private String createBy;
    private Long createDate = 0L;
    private Long updateDate = 0L;

}

