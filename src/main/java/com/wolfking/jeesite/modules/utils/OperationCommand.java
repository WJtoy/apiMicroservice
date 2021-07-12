package com.wolfking.jeesite.modules.utils;

/**
 * @Auther wj
 * @Date 2021/3/9 10:20
 */
public interface OperationCommand {

    public enum OperationCode {

        GRAB(1001, "网点接单"),
        UPLOAD_FINISH_PIC_NEW(1002, "上传订单完成图片"),
        DELETE_FINISH_FinishPIC_NEW(1003, "删除订单完成图片"),
        SET_APPOINTMENT_DATE(1004, "预约日期"),
        SET_PENGDING(1005, "安维设置停滞原因"),
        SET_ABNORMAL(1006, "app标记异常"),
        CONFIRM_DOOR_NEW(1007, "确认上门"),
        SERVICE_PLAN(1008,"网点派单"),
        CLEAR_FINISH_PICS(1009,"按订单id清除订单所有完成图片"),
        APP_CLOSE(1010,"App工单完工"),
        SAVE_ORDER_AUXILIARY_MATERIALS_V3(1011,"保存工单的辅材或服务项目"),
        SAVE_MATERIAL_APPLICATION_NEW(1012,"保存配件申请单"),
        UPDATE_DETAIL(1013,"完善上门服务维修信息"),
        SAVE_ORDER_PRAISE_INFO(1014,"保存工单好评信息"),
        SAVE_ORDER_VALIDATE_INFO(1015,"保存工单鉴定信息"),
        SAVE_PRODUCT_SN(1016,"保存产品sn码");


        public int code;
        public String name;

        private OperationCode(int code, String name) {
            this.code = code;
            this.name = name;
        }

    }

}
