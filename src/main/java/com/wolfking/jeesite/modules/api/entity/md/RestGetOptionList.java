package com.wolfking.jeesite.modules.api.entity.md;

/**
 * 获取选项列表
 */
public class RestGetOptionList {
    //0:completed_type完工项，1：PendingType停滞原因，2：order_abnormal_reasonAPP异常类型
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
