package com.wolfking.jeesite.modules.sd.entity.viewModel;

import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.common.utils.QuarterUtils;
import com.wolfking.jeesite.modules.md.entity.Engineer;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.utils.ApiPropertiesUtils;

import java.util.Date;
import java.util.List;

/**
 * 异常处理查询模型
 * 
 * @author Ryan
 */

public class OrderServicePointSearchModel extends LongIDDataEntity<OrderServicePointSearchModel> {

	private static final int ORDER_LENGTH = 14;

	private static final long serialVersionUID = 1L;
	/*
	 *  10 - 待预约
     *  20 - 处理中
     *  30 - 已预约
     *  40 - 等配件
     *  50 - 停滞(不包括等配件、预约中)
     *  60 - 催单待回复
     */

	public static final int ORDER_LIST_TYPE_WAITINGAPPOINTMENT = 10;
	public static final int ORDER_LIST_TYPE_PROCESSING = 20;
	public static final int ORDER_LIST_TYPE_APPOINTED = 30;
	public static final int ORDER_LIST_TYPE_WAITINGPARTS = 40;
	public static final int ORDER_LIST_TYPE_PENDING = 50;
	// 2019/12/19 催单待回复订单列表
	public static final int ORDER_LIST_TYPE_REMINDER = 60;
	private String quarter;
	private Integer orderListType;
	private Date appointmentDate;
	private Long servicePointId;
	private Long engineerId;
	private Date beginAcceptDate;
	private Date endAcceptDate;
	private String acceptDateRange;

	private Date beginCompleteDate;
	private Date endCompleteDate;
	private String completeDateRange;

	private String orderNo;
	private String userName;
	private String userPhone;
	private int isPhone = 0; //是否是合法的手机号码
	private String engineerName;
	private Long areaId;
	private Integer orderServiceType;
	private Integer complaint;
	private Integer urgent;
	private Integer reminder;
	private String address;
	private String servicePhone;
	private Long masterId;//主帐号id
	private int isAppRequest = 0;//app请求标志
	//for历史订单查询
	private String isEngineerInvoiced;//是否已结算
	private List<Long> engineerIds; //网点按安维name查询时，转成id列表
	private Long searchEngineerId; //按选择安维查询
	List<Engineer> engineerList; //网点下安维列表
	private int orderNoSearchType = 0;//工单单号搜索类型

	private List<String> quarters;

	private Date startOfToday = null;
	private Date endOfToday = null;

	private Dict engineerChargeStatus;//安维对帐状态

	@GsonIgnore
	private Integer limitOffset = 0;
	@GsonIgnore
	private Integer limitRows = 10;

	private Date beginAppointDate;
	private Date endAppointDate;

	// Constructors
	public OrderServicePointSearchModel() {
		super();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getOrderListType() {
		return orderListType;
	}

	public void setOrderListType(Integer orderListType) {
		this.orderListType = orderListType;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Long getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(Long servicePointId) {
		this.servicePointId = servicePointId;
	}

	public Date getBeginAcceptDate() {
		return beginAcceptDate;
	}

	public void setBeginAcceptDate(Date beginAcceptDate) {
		this.beginAcceptDate = beginAcceptDate;
	}

	public Date getEndAcceptDate() {
		return endAcceptDate;
	}

	public void setEndAcceptDate(Date endAcceptDate) {
		this.endAcceptDate = endAcceptDate;
	}

	public Date getBeginCompleteDate() {
		return beginCompleteDate;
	}

	public void setBeginCompleteDate(Date beginCompleteDate) {
		this.beginCompleteDate = beginCompleteDate;
	}

	public Date getEndCompleteDate() {
		return endCompleteDate;
	}

	public void setEndCompleteDate(Date endCompleteDate) {
		this.endCompleteDate = endCompleteDate;
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

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Integer getOrderServiceType() {
		return orderServiceType;
	}

	public void setOrderServiceType(Integer orderServiceType) {
		this.orderServiceType = orderServiceType;
	}

	public Integer getComplaint() {
		return complaint;
	}

	public void setComplaint(Integer complaint) {
		this.complaint = complaint;
	}

	public Integer getUrgent() {
		return urgent;
	}

	public void setUrgent(Integer urgent) {
		this.urgent = urgent;
	}

	public Integer getReminder() {
		return reminder;
	}

	public void setReminder(Integer reminder) {
		this.reminder = reminder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getEngineerId() {
		return engineerId;
	}

	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getIsEngineerInvoiced() {
		return isEngineerInvoiced;
	}

	public void setIsEngineerInvoiced(String isEngineerInvoiced) {
		this.isEngineerInvoiced = isEngineerInvoiced;
	}

	public List<Long> getEngineerIds() {
		return engineerIds;
	}

	public void setEngineerIds(List<Long> engineerIds) {
		this.engineerIds = engineerIds;
	}

	public Long getMasterId() {
		return masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}

	public int getIsAppRequest() {
		return isAppRequest;
	}

	public void setIsAppRequest(int isAppRequest) {
		this.isAppRequest = isAppRequest;
	}

	public List<String> getQuarters() {
		return quarters;
	}

	public void setQuarters(List<String> quarters) {
		this.quarters = quarters;
	}

	public Date getStartOfToday() {
		return startOfToday;
	}

	public void setStartOfToday(Date startOfToday) {
		this.startOfToday = startOfToday;
	}

	public Date getEndOfToday() {
		return endOfToday;
	}

	public void setEndOfToday(Date endOfToday) {
		this.endOfToday = endOfToday;
	}

	public Dict getEngineerChargeStatus() {
		return engineerChargeStatus;
	}

	public void setEngineerChargeStatus(Dict engineerChargeStatus) {
		this.engineerChargeStatus = engineerChargeStatus;
	}


	public String getAcceptDateRange() {
		return acceptDateRange;
	}

	public void setAcceptDateRange(String acceptDateRange) {
		this.acceptDateRange = acceptDateRange;
	}

	public String getCompleteDateRange() {
		return completeDateRange;
	}

	public void setCompleteDateRange(String completeDateRange) {
		this.completeDateRange = completeDateRange;
	}

	public Integer getLimitOffset() {
		return limitOffset;
	}

	public void setLimitOffset(Integer limitOffset) {
		this.limitOffset = limitOffset;
	}

	public Integer getLimitRows() {
		return limitRows;
	}

	public void setLimitRows(Integer limitRows) {
		this.limitRows = limitRows;
	}

	public Date getBeginAppointDate() {
		return beginAppointDate;
	}

	public void setBeginAppointDate(Date beginAppointDate) {
		this.beginAppointDate = beginAppointDate;
	}

	public Date getEndAppointDate() {
		return endAppointDate;
	}

	public void setEndAppointDate(Date endAppointDate) {
		this.endAppointDate = endAppointDate;
	}

	public Long getSearchEngineerId() {
		return searchEngineerId;
	}

	public void setSearchEngineerId(Long searchEngineerId) {
		this.searchEngineerId = searchEngineerId;
	}
	public List<Engineer> getEngineerList() {
		return engineerList;
	}

	public void setEngineerList(List<Engineer> engineerList) {
		this.engineerList = engineerList;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public int getOrderNoSearchType(){
		if (StringUtils.isNotBlank(this.orderNo)){
			this.orderNo = this.orderNo.trim().toUpperCase();
//			String orderNoPrefix = Global.getConfig("OrderPrefix");
			String orderNoPrefix = ApiPropertiesUtils.getWeb().getOrderPrefix();
			if (orderNo.length() == ORDER_LENGTH && orderNo.startsWith(orderNoPrefix)){
				orderNoSearchType = 1;
				String quarter = QuarterUtils.getOrderQuarterFromNo(orderNo);
				if(StringUtils.isNotBlank(quarter)){
					this.quarter = quarter;
				}
			}else if (orderNo.startsWith(orderNoPrefix)){
				orderNoSearchType = 2;
			}
		}
		return this.orderNoSearchType;
	}

	public int getIsPhone(){
		if (StringUtils.isNotBlank(this.userPhone)){
			if("".equalsIgnoreCase(StringUtils.isPhoneWithRelaxed(this.userPhone))){
				this.isPhone = 1;
			}
		}
		return this.isPhone;
	}
}