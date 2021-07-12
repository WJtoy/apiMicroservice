package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

/**
 * 网点日志记录
 */
public class ServicePointLog extends LongIDDataEntity<ServicePointLog> {

    public static final int SERVICE_POINT_LOG_TYPE_CREATE_SERVICEPOINT = 10;
    public static final int SERVICE_POINT_LOG_TYPE_EDIT_SERVICEPOINT = 20;
    public static final int SERVICE_POINT_LOG_TYPE_EDIT_SERVICEPOINT_PLAN_REMARK = 21;
    public static final int SERVICE_POINT_LOG_TYPE_EDIT_SERVICEPOINT_REMARK = 22;
    public static final int SERVICE_POINT_LOG_TYPE_DEL_SERVICEPOINT = 30;

    /**
     * 网点日志类型
     */
    public enum ServicePointLogType {

        CREATE_SERVICEPOINT(10, "创建网点"),
        EDIT_SERVICEPOINT(20, "编辑网点"),
        EDIT_SERVICEPOINT_PLAN_REMARK(21, "编辑网点派单备注"),
        EDIT_SERVICEPOINT_REMARK(22, "编辑网点备注"),
        EDIT_APP_INSURANCE_FLAG(23, "APP阅读且同意(或不同意)保险条款"),
        EDIT_APP_SERVICEPOINT_ADDRESS(24, "APP编辑网点地址"),
        EDIT_APP_SERVICEPOINT_BANK_ACCTOUN_INFO(25, "APP编辑网点银行账号信息"),
        EDIT_SERVICEPOINT_AUTOPLANFLAG(26,"更新自动派单标志"),
        EDIT_SERVICEPOINT_LEVEL(27,"更新网点等级"),
        EDIT_SERVICEPOINT_BANKISSUE(28,"更新网点付款失败原因"),
        DEL_SERVICEPOINT(30, "删除网点");


        private int value;
        private String label;

        ServicePointLogType(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * 网点ID
     */
    private ServicePoint servicePoint;

    /**
     * 类型：
     * 1：创建网点
     * 2：编辑网点
     * 3：删除网点
     * 10：修改网点备注信息
     */
    private Integer type = 0;

    /**
     * 标题
     */
    private String title = "";

    /**
     * 内容
     */
    private String content = "";

    /**
     * 操作者名称
     */
    private String operator = "";

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public long getServicePointId() {
        long servicePointId = 0L;
        if (servicePoint != null && servicePoint.getId() != null) {
            servicePointId = servicePoint.getId();
        }
        return servicePointId;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
