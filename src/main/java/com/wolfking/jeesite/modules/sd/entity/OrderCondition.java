package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ProductCategory;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.entity.UrgentLevel;
import com.wolfking.jeesite.modules.sd.utils.OrderConditionAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 订单信息,主要包含查询条件
 */
@JsonAdapter(OrderConditionAdapter.class)
public class OrderCondition extends LongIDDataEntity<OrderCondition> {

    public static final int ORDER_PENDDING_TYPE_WAITING_ACCESSORY = 2;// 等配件
    public static final int ORDER_PENDDING_TYPE_APPOINTMENT = 3;// 预约时间
    public static final int ORDER_PENDDING_TYPE_OTHER_PENDING = 145;// 停滞(不包括等配件、等预约)

    public OrderCondition() {
    }

    public OrderCondition(Long orderId) {
        this.orderId = orderId;
    }

    private Long version = 0l;//版本，用于检查订单时候有变更，每次对订单操作都要在redis中递增

    private Long orderId;
    private String quarter = "";//数据库分片，与订单相同
    private String orderNo = "";// 订单号
    private Customer customer;//上游客户(订单来源)
    private int customerType;//客户类型 0-普通客户 1-vip客户
    private Long provinceId; // 省Id
    private Long cityId; // 市Id
    private Area area;// 用户区域(终端用户)
    private Area subArea;// 4级街道 2018/06/06
    // 用户
    private String userName = "";// 用户名
    /**
     * 对应页面的手机号
     */
    private String phone1 = "";
    private String phone2 = "";//对应页面的座机号
    private String phone3 = "";
    private String servicePhone = "";// 用户实际联络电话

    private String areaName = "";//省市区县
    private String address = "";// 用户地址
    private String serviceAddress = "";// 实际上门地址

    private Dict status;// 状态
    private User kefu;// 客服

    //停滞
    private Integer pendingFlag = 2;// 异常标记 1:异常 2:正常,3:修改完成再次对帐,具体修改存入orderprocesslog
    private Dict pendingType;// 停滞原因
    private Date pendingTypeDate;// 开始停滞日期
    private Date appointmentDate;// 停滞到期日期

    //预约
    private Integer reservationTimes = 0; //预约次数 18/01/26
    private Date reservationDate;//预约日期

    //反馈标记
    private Long feedbackId = 0l;//反馈id
    private Integer feedbackFlag = 0;// 反馈标记 0：无投诉 1：有投诉,但未关闭 2:投诉已关闭
    //反馈标题,2017-09-23 改为存储最后反馈的内容(长度100）
    private String feedbackTitle = "";
    private Date feedbackDate;// 最后反馈日期
    private Integer replyFlag = 0;//问题反馈回复标示 0：已读 1：(客服回复，客户未读) 2：客户回复，客服未读
    private Integer replyFlagKefu = 0;// 1:客服回复,标记订单为异常 ，0:厂家处理之后需要手动改为正常
    private Integer replyFlagCustomer = 0;// 1:厂家回复,标记订单为异常 ，0:客服处理之后需要手动改为正常
    private Integer feedbackCloseFlag = 0;//问题反馈关闭标识，不存储

    private Integer appAbnormalyFlag = 0;// 1:app异常标记 app_abnormaly_flag
    private Integer operationAppFlag = 0;//标识是手机操作订单,还是客服后台介入操作订单 0：客户派单 1:手机接单

    private Date closeDate;// 关闭日期 -->OrderStatus也有

    //配件
    private Integer partsFlag = 0;//配件标记
    private Integer returnPartsFlag = 0;//反件标记
    private Long partsApplyDate = 0L;//配件申请日期
    //客评
    // 客评标记
    // old- 0:默认，待客评，1：已评 2:待客评，APP触发客评短信时置为2，客评后变为1
    // new- 0:未客评 1:人工客评 2:短信客评 3:语音回访客评 4:app自动客评
    private Integer gradeFlag = 0;
    //private Integer autoGradeFlag = 0;//自动客评标记,0:非自动客评 1：是 (终端用户发送短信回复1自动客评，自动完工客户)
    private Integer autoChargeFlag = 0;//自动对帐标记 0：否，1：自动客评，对帐，2：自动对帐 3:自动对账中

    //private Integer autoCompleteFlag = 0;//自动完工标记 0：否，1：APP触发，自动完工，2：APP触发,需短信回复自动客评
    private String appCompleteType = ""; //APP完工类型(数据字典：completed_type)
    private Date appCompleteDate; //app自动完工日期

    //private String productCategoryIds = ""; //订单item中所有产品类别id(用两个逗号括起来,如",1,,2,"),用于查询
    private Long productCategoryId = 0L;
    @GsonIgnore
    private ProductCategory productCategory;
    private String productIds = ""; //订单item中所有产品id(用两个逗号括起来),用于查询
    private Integer hasSet = 0; //服务产品是否有套组
    private String serviceTypes = "";//订单服务类型列表,主要用于查询，如",1,,2,"
    private int orderServiceType = 1;//订单服务类型 1:安装单 2:维修单 (数据字典:order_service_type)
    private String orderServiceTypeName = "安装单";

    //安维
    private ServicePoint servicePoint; //安维网点,servicepoint_id,对应原来 engineer:安维人(主账号)
    private User engineer;// 师傅，类型是User，实际保存的id是md_engineer.id

    //上门服务
    private Integer serviceTimes = 0;//上门次数
    private Integer appSubmitService = 0;//安维提交上门服务
    private Integer finishPhotoQty = 0; //完成照片数量,包含客户上门服务上传及安维app上传

    private Integer trackingFlag = 0;// 进度跟踪标记
    private String trackingMessage = "";//最新进度跟踪内容
    private Date trackingDate; //最新进度跟踪日期

    private Integer totalQty = 0; //实际产品数量(上门服务更新),order表不变更
    //comment by ryan at 2018/09/11
    //private Double orderCharge = 0.00;// 合计订单金额(应收)(上门服务更新)，Fee表也同时变更
    //private Double engineerTotalCharge = 0.00;// 安维总金额(应付)(上门服务更新)，Fee表也同时变更
    //comment end

    private Integer chargeFlag = 0;//厂商对帐标记
    private Integer transToHistoried = 0;//是否转成历史订单 1:是
    private int isComplained = 0;//投诉标识 0:无 1:有 2:有判责为网点 18/01/24

    private int rushOrderFlag = 0;//突击单标记 0:无突击单 1:有突击单，但未完成 2:突击单已完成 //2018/04/13
    private Double timeLiness = 0.00;//网点时效(派单~客评的用时) //2018/05/17
    private Date arrivalDate;//商品到货日期 //2018/05/19
    private UrgentLevel urgentLevel = new UrgentLevel(0l); //加急等级 urgent_level_id 2018/06/06
    private String fullAddress = "";//用于B2B订单转换时，显示原始地址
    private Integer subStatus = 0;//订单子状态，用于订单筛选 2018/6/19
    private String customerOwner = "";//客户负责人
    private int reminderFlag = 0; // 催单标记 2019/07/09 comment at 2019/08/15 restore 2019/12/06
    //辅助字段
    private int reminderCheckResult = 0;// 催单检查结果
    private String reminderCheckTitle = "";// 催单检查说明
    private int reminderTimes = 1;//催单次数

    //可突击标识 0:非突击区域(可发起突击单),1:突击区域(不可发起突击单)
    private Integer canRush = 0;

    //客服类型(0:大客服 1:自动客服 2:突击客服 3:vip客服)
    private Integer kefuType = 0;


    //挂起状态：1 - 挂起，0 - 恢复
    private Integer suspendFlag = OrderSuspendFlagEnum.NORMAL.getValue();
    //挂起类型：10 - 鉴定
    private Integer suspendType = OrderSuspendTypeEnum.NONE.getValue();


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Length(min = 14, max = 14, message = "订单号长度应为14位")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @NotNull(message = "客户不能为空")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @NotNull(message = "区域不能为空")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @NotNull(message = "用户不能为空")
    @Length(max = 50, message = "用户名不能超过50")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NotNull(message = "用户手机号不能为空")
    @Length(max = 11, message = "手机号长度不能超过11位")
    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    @Length(max = 16, message = "固话长度不能超过16位")
    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @Length(max = 16, message = "电话不能超过16位")
    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    @Length(max = 11, message = "联络人电话长度不能超过11位")
    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    @Length(max = 60, message = "用户地址长度不能超过60个汉字")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(max = 120, message = "实际上门地址长度不能超过120个汉字")
    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    @NotNull(message = "订单状态不能为空")
    public Dict getStatus() {
        return status;
    }

    public void setStatus(Dict status) {
        this.status = status;
    }

    public int getStatusValue() {
        if (status == null) return 0;
        return Integer.parseInt(status.getValue());
    }

    public Integer getPendingFlag() {
        return pendingFlag;
    }

    public void setPendingFlag(Integer pendingFlag) {
        this.pendingFlag = pendingFlag;
    }

    public Dict getPendingType() {
        return pendingType;
    }

    public void setPendingType(Dict pendingType) {
        this.pendingType = pendingType;
    }

    public Date getPendingTypeDate() {
        return pendingTypeDate;
    }

    public void setPendingTypeDate(Date pendingTypeDate) {
        this.pendingTypeDate = pendingTypeDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getFeedbackFlag() {
        return feedbackFlag;
    }

    public void setFeedbackFlag(Integer feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Range(min = 0, max = 1, message = "配件标识超出范围")
    public Integer getPartsFlag() {
        return partsFlag;
    }

    public void setPartsFlag(Integer partsFlag) {
        this.partsFlag = partsFlag;
    }

    @Range(min = 0, max = 1, message = "返件标识超出范围")
    public Integer getReturnPartsFlag() {
        return returnPartsFlag;
    }

    public void setReturnPartsFlag(Integer returnPartsFlag) {
        this.returnPartsFlag = returnPartsFlag;
    }

    public Integer getReplyFlagKefu() {
        return replyFlagKefu;
    }

    public void setReplyFlagKefu(Integer replyFlagKefu) {
        this.replyFlagKefu = replyFlagKefu;
    }

    public Integer getReplyFlagCustomer() {
        return replyFlagCustomer;
    }

    public void setReplyFlagCustomer(Integer replyFlagCustomer) {
        this.replyFlagCustomer = replyFlagCustomer;
    }

    public Integer getGradeFlag() {
        return gradeFlag;
    }

    public void setGradeFlag(Integer gradeFlag) {
        this.gradeFlag = gradeFlag;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public ServicePoint getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    public User getEngineer() {
        return engineer;
    }

    public void setEngineer(User engineer) {
        this.engineer = engineer;
    }

    public User getKefu() {
        return kefu;
    }

    public void setKefu(User kefu) {
        this.kefu = kefu;
    }

    @Range(min = 0, message = "产品数量必须大于0")
    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Integer getAppAbnormalyFlag() {
        return appAbnormalyFlag;
    }

    public void setAppAbnormalyFlag(Integer appAbnormalyFlag) {
        this.appAbnormalyFlag = appAbnormalyFlag;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public Integer getFinishPhotoQty() {
        return finishPhotoQty;
    }

    public void setFinishPhotoQty(Integer finishPhotoQty) {
        this.finishPhotoQty = finishPhotoQty;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    //public String getProductCategoryIds() {
    //    return productCategoryIds;
    //}
    //
    //public void setProductCategoryIds(String productCategoryIds) {
    //    this.productCategoryIds = productCategoryIds;
    //}

    public Integer getFeedbackCloseFlag() {
        return feedbackCloseFlag;
    }

    public void setFeedbackCloseFlag(Integer feedbackCloseFlag) {
        this.feedbackCloseFlag = feedbackCloseFlag;
    }

    public Integer getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(Integer replyFlag) {
        this.replyFlag = replyFlag;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(String serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Integer getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(Integer chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public Integer getOperationAppFlag() {
        return operationAppFlag;
    }

    public void setOperationAppFlag(Integer operationAppFlag) {
        this.operationAppFlag = operationAppFlag;
    }

    public Integer getTrackingFlag() {
        return trackingFlag;
    }

    public void setTrackingFlag(Integer trackingFlag) {
        this.trackingFlag = trackingFlag;
    }

    public String getTrackingMessage() {
        return trackingMessage;
    }

    public void setTrackingMessage(String trackingMessage) {
        this.trackingMessage = trackingMessage;
    }

    public Date getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
    }

    public Integer getHasSet() {
        return hasSet;
    }

    public void setHasSet(Integer hasSet) {
        this.hasSet = hasSet;
    }

    public Integer getAutoChargeFlag() {
        return autoChargeFlag;
    }

    public void setAutoChargeFlag(Integer autoChargeFlag) {
        this.autoChargeFlag = autoChargeFlag;
    }

    public Integer getAppSubmitService() {
        return appSubmitService;
    }

    public void setAppSubmitService(Integer appSubmitService) {
        this.appSubmitService = appSubmitService;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public int getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(int orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public String getOrderServiceTypeName() {
        return orderServiceTypeName;
    }

    public void setOrderServiceTypeName(String orderServiceTypeName) {
        this.orderServiceTypeName = orderServiceTypeName;
    }

    public String getAppCompleteType() {
        return appCompleteType;
    }

    public void setAppCompleteType(String appCompleteType) {
        this.appCompleteType = appCompleteType;
    }


    public Integer getTransToHistoried() {
        return transToHistoried;
    }

    public void setTransToHistoried(Integer transToHistoried) {
        this.transToHistoried = transToHistoried;
    }

    public int getIsComplained() {
        return isComplained;
    }

    public void setIsComplained(int isComplained) {
        this.isComplained = isComplained;
    }

    public Integer getReservationTimes() {
        return reservationTimes;
    }

    public void setReservationTimes(Integer reservationTimes) {
        this.reservationTimes = reservationTimes;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Integer getRushOrderFlag() {
        return rushOrderFlag;
    }

    public void setRushOrderFlag(Integer rushOrderFlag) {
        this.rushOrderFlag = rushOrderFlag;
    }

    public Double getTimeLiness() {
        return timeLiness;
    }

    public void setTimeLiness(Double timeLiness) {
        this.timeLiness = timeLiness;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Date getAppCompleteDate() {
        return appCompleteDate;
    }

    public void setAppCompleteDate(Date appCompleteDate) {
        this.appCompleteDate = appCompleteDate;
    }

    public Area getSubArea() {
        return subArea;
    }

    public void setSubArea(Area subArea) {
        this.subArea = subArea;
    }

    public void setRushOrderFlag(int rushOrderFlag) {
        this.rushOrderFlag = rushOrderFlag;
    }

    public UrgentLevel getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(UrgentLevel urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public Integer getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Integer subStatus) {
        this.subStatus = subStatus;
    }

    public String getCustomerOwner() {
        return customerOwner;
    }

    public void setCustomerOwner(String customerOwner) {
        this.customerOwner = customerOwner;
    }

    public Long getPartsApplyDate() {
        return partsApplyDate;
    }

    public void setPartsApplyDate(Long partsApplyDate) {
        this.partsApplyDate = partsApplyDate;
    }

    public int getReminderCheckResult() {
        return reminderCheckResult;
    }

    public void setReminderCheckResult(int reminderCheckResult) {
        this.reminderCheckResult = reminderCheckResult;
    }

    public String getReminderCheckTitle() {
        return reminderCheckTitle;
    }

    public void setReminderCheckTitle(String reminderCheckTitle) {
        this.reminderCheckTitle = reminderCheckTitle;
    }

    public long getCustomerId() {
        return getCustomer() != null && getCustomer().getId() != null ? getCustomer().getId() : 0;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public int getReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(int reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public Integer getCanRush() {
        return canRush;
    }

    public void setCanRush(Integer canRush) {
        this.canRush = canRush;
    }

    public int getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(int reminderTimes) {
        this.reminderTimes = reminderTimes;
    }


    public Integer getSuspendFlag() {
        return suspendFlag;
    }

    public void setSuspendFlag(Integer suspendFlag) {
        this.suspendFlag = suspendFlag;
    }

    public Integer getSuspendType() {
        return suspendType;
    }

    public void setSuspendType(Integer suspendType) {
        this.suspendType = suspendType;
    }

    public Integer getKefuType() {
        return kefuType;
    }

    public void setKefuType(Integer kefuType) {
        this.kefuType = kefuType;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
