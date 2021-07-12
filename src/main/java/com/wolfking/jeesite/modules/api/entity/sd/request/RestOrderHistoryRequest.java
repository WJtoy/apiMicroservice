package com.wolfking.jeesite.modules.api.entity.sd.request;

/**
 * 历史订单列表请求
 */
public class RestOrderHistoryRequest extends RestOrderRequest  {

    private String isEngineerInvoiced;//是否已结算
    private String	orderNo;//工单号
    private	String userName;//用户名
    private	String phone;//电话
    private	String address;//地址
    private	Integer orderServiceType;//订单类型
    private	Long beginAcceptDate;//接单日期(网点派单日期)
    private	Long endAcceptDate;
    private	String engineerName;//安维

    public RestOrderHistoryRequest(){}

    public String getIsEngineerInvoiced() {
        return isEngineerInvoiced;
    }

    public void setIsEngineerInvoiced(String isEngineerInvoiced) {
        this.isEngineerInvoiced = isEngineerInvoiced;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(Integer orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public Long getBeginAcceptDate() {
        return beginAcceptDate;
    }

    public void setBeginAcceptDate(Long beginAcceptDate) {
        this.beginAcceptDate = beginAcceptDate;
    }

    public Long getEndAcceptDate() {
        return endAcceptDate;
    }

    public void setEndAcceptDate(Long endAcceptDate) {
        this.endAcceptDate = endAcceptDate;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }
}
