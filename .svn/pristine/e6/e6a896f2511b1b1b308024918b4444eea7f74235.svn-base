package com.wolfking.jeesite.modules.api.entity.sd;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.api.entity.sd.adapter.RestOrderStatusLogAdapter;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;

import java.util.List;

/**
 * 订单日志
 */
@JsonAdapter(RestOrderStatusLogAdapter.class)
public class RestOrderStatusLog {
    private String orderNo;
    List<OrderProcessLog> logs = Lists.newArrayList();

    public RestOrderStatusLog(){}

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<OrderProcessLog> getLogs() {
        return logs;
    }

    public void setLogs(List<OrderProcessLog> logs) {
        this.logs = logs;
    }
}
