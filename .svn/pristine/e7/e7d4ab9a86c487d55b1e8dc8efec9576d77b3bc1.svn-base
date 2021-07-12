package com.wolfking.jeesite.modules.api.util;

public enum ErrorCode {

    //定义
    NO_ERROR(0, "success"),
    INVALID_TOKEN(1, "非法请求,token验证失败"),
    FAIL_REDIS_ACCESS(2, "缓存操作失败"),
    RECORD_NOT_EXIST(3, "记录不存在"),
    NOT_FOUND(4, "请求地址不存在"),
    LOGIN_INFO_MISSING(5, "读取登录帐号信息错误，请重试"),

    UNKNOWN_EXCEPTION(10000, "未知错误,请重试"),
    MD5_NO_SUCH_ALGORITHM(10001, "MD5加密出现问题"),
    NETWORK_ISSUE(10002, "网络问题"),
    SIGN_NOT_MATCH(10003, "签名不匹配"),
    WRONG_REQUEST_FORMAT(10004, "参数格式不匹配"),
    REQUEST_BODY_VALIDATE_FAIL(10005, "请求内容不匹配"),
    REQUEST_VALIDATE_FAIL(10006, "请求失败"),
    DATA_PROCESS_ERROR(10007, "数据处理错误,请重试"),
    //user
    MEMBER_ACCOUNT_EXSIT(20001, "会员帐号已存在"),
    MEMBER_ACCOUNT_NOT_EXSIT_OR_PASS_WRONG(20002, "用户名或密码错误"),
    MEMBER_SESSION_TIMEOUT(20003, "登录已过期，请重新登录"),
    MEMBER_OLD_PASS_WRONG(20004, "旧密码错误"),
    MEMBER_PHONE_NOT_MATCH(20005, "手机号码与会员绑定手机号码不同"),
    MEMBER_VERIFYCODE_FAIL(20006, "验证码错误"),
    MEMBER_VERIFYCODE_TIMEOUT(20007, "验证码已过期"),
    MEMBER_PHONE_REGISTERED(20008, "手机号码已被其他账号绑定"),
    MEMBER_ENGINEER_NO_EXSIT(20009, "安维不存在，或已停用"),
    MEMBER_REGISTER_ARTICLE_NO_FOUND(20010, "服务章程不存在"),
    MEMBER_REGISTER_AREA_NO_FOUND(20011, "没有找到区域记录"),
    MEMBER_PICTURE_UPLOAD_FAILED(20012, "上传图片失败"),
    MEMBER_REGISTER_STEP2_FAILED(20013, ""),
    MEMBER_PHONE_NOT_EXIST(20014, "手机号码不存在"),

    //MD，SYS
    NO_BASEDATA(30001, "读取基本资料:%s 失败"),
    GET_SYS_DICT_FAILED(30002, "读取数据字典失败"),
    PRODUCT_NOT_EXIST(30003, "产品未定义"),
    //SD
    ORDER_CAN_NOT_COMPLETE(40002, "工单完工失败：订单无上门服务项目"),
    ORDER_FINISH_SERVICE(40003, "订单已完成服务"),
    CUSTOMER_PRICE_NOT_EXIST(40004, "客户价格未设定"),
    ENGINEER_PRICE_NOT_EXIST(40005, "安维价格未设定"),
    MATERIAL_APPLIED_EXIST(40006, "存在已审核或是拒绝的配件申请"),
    ORDER_VERIFY_CODE_FAILED(40007, "验证码不正确"),
    ORDER_CAN_NOT_PLAN(40008, "派单失败,订单状态错误"),
    ORDER_CAN_NOT_GRAB(40009, "抢单失败,订单状态错误"),
    ORDER_CAN_NOT_APPOINTMENT(40010, "预约失败,订单状态错误"),
    ORDER_CAN_NOT_SAVECOMPONENT(40011, "保存配件申请失败，订单状态错误"),
    ORDER_CAN_NOT_SAVEABNORMAL(40012, "标记异常失败，订单状态错误"),
    ORDER_CAN_NOT_DELCOMPONENT(40013, "删除配件申请失败，订单状态错误"),
    ORDER_CAN_NOT_DELCOMPONENT_APPROVED(40014, "删除配件申请失败，配件申请已经审核或驳回"),
    ORDER_CAN_NOT_DELORDERDETAIL(40015, "删除订单详情失败，订单状态错误"),
    ORDER_CAN_NOT_SAVEATTACHMENT(40016, "保存订单附件失败，订单状态错误"),
    ORDER_CAN_NOT_SAVEORDERCOMPLETE(40017, "保存订单完成错误，订单状态错误"),
    ORDER_CAN_NOT_CONFIRMDOOR(40018, "确认上门失败，订单状态错误"),
    ORDER_CAN_NOT_BACKAPPROVE(40019, "订单回退到抢单区失败，订单状态错误"),
    ORDER_NOT_OVER_TWO_HOUR(40020, "订单回退到抢单区失败，接单未超过两小时"),
    ORDER_ALREADY_APPOINTED(40021, "订单回退到抢单区失败，订单已经预约客户时间"),
    ORDER_HAS_MATERIALAPPLY_NEW(40022, "订单完成失败，还有未审核的配件申请"),
    ORDER_HAS_NOT_ATTACHMENT(40023, "请先上传客户要求的最少服务效果图"),
    APPOINTED_DATE_ERROR(40024, "预约时间超过48小时，必须选择停滞原因"),
    ORDER_RESEND_VERIFY_CODE_ERROR(40025, "重发验证码失败,连接短信平台失败"),
    PLAN_ORDER_MSG_MAIL(40026, "派单成功"),
    PLAN_ORDER_PUSH_FAIL(40027, "派单成功,但推送失败"),
    NO_SUBENGINEER_LIST(40028, "没有可以派单的安维师傅"),
    ORDER_NO_APPOINTED(40029, "请先联系用户,预约上门时间"),
    ORDER_HAS_ORDERDETAIL(40030, "订单回退失败，已经添加上门服务"),
    ORDER_DELETE_DETAIL_OF_OTHERS(40031, "删除失败，不能删除其他安维人员的上门服务详情"),
    USRE_NO_ACCESS_APP(40032, "抢单失败，没有手机接单的权限"),
    ORDERDETAIL_QTY(40033, "添加上门信息的数量有误"),
    USERINFO_MODIFY_FAIL_OF_APPROVED(40034, "修改信息失败，信息已经审核，不允许修改"),
    ORDER_REDIS_LOCKED(40035, "订单处理中"),
    AREA_HAS_NO_KEFU(40036, "此区域暂未分配跟进客服，暂时无法下单。请联系管理员：18772732342，QQ:572202493"),
    NOT_FOUND_PENDINGTYPE(40037, "停滞原因不存在"),
    READ_ORDER_FAIL(40038, "读取订单信息失败"),
    //other
    JEDIS_NOT_RUNNING(50003, "系统缓存失败"),
    APP_PUSH_FAILED(50004, "手机消息推送失败"),

    NOT_PRIMARY_ACCOUNT(60001, "该功能只允许主账号使用");


    public String message;
    public int code;

    // 构造方法
    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.code + "_" + this.message;
    }

}
