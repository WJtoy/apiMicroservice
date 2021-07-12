package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.mapper.adapters.DateTimeAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.sd.entity.viewModel.ServicePointCrush;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 突击单
 * @author Ryan
 * @date 2018-01-27
 */
public class OrderCrush extends LongIDDataEntity<OrderCrush>
{
	private static final long serialVersionUID = 1L;

	public static final Integer STATUS_NEW = 0;
	public static final Integer STATUS_CLOSED = 1;
	public static final Integer STATUS_TEMPCREATE = 2;

	// Fields
	@NotNull(message = "工单ID不能为空")
	private long orderId;
	@NotEmpty(message = "数据库分片不能为空")
	private String quarter = "";//数据库分片，与工单相同
	@GsonIgnore
	@NotNull(message = "工单号不能为空")
	private String orderNo = "";//工单号

	@NotNull(message = "产品类别不能为空")
	private Long productCategoryId = 0L;
	private String productCategoryName;

	private String crushNo;//突击单号

	@GsonIgnore
	@NotNull(message = "厂商不能为空")
	@JsonAdapter(CustomerSimpleAdapter.class)
	private Customer customer;//厂商

	private Integer status;//状态

	//@NotEmpty(message = "用户姓名不能为空")
	private String userName;//用户姓名

	//@NotEmpty(message = "用户电话不能为空")
	private String userPhone;

	//@NotEmpty(message = "用户地址不能为空")
	private String userAddress;

	//@GsonIgnore
	//@NotNull(message = "客服不能为空")
	//private User kefu;//客服

	@Length(max = 200,message = "发起说明长度不能超过200")
	private String createRemark = "";
	@GsonIgnore
	private Date createDateBegin;//用来辅助时间的查询
	@GsonIgnore
	private Date createDateEnd;//用来辅助时间的查询

	//省
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area province;
	//市
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area city;
	//区/县
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area area;//2018-06-25 lzx

	private Integer areaLevel;//0 省 1市 2区

	@JsonAdapter(UserSimpleAdapter.class)
	private User closeBy;//完成人

	@JsonAdapter(DateTimeAdapter.class)
	private Date closeDate;//完成日期

	@Length(max = 500,message = "完成说明长度不能超过500")
	private String closeRemark = "";

	private Integer lastFlag = 0; //当前工单的最后一笔突击单
	private int itemNo = 1;//突击序号
    //以下是辅助字段

	//@GsonIgnore
	//private String data="";//网点JSON

    // 网点列表
	@GsonIgnore
	private List<ServicePoint> servicePoints=Lists.newArrayList();

	//街道
	private Area subArea;

	@GsonIgnore
	private int action = 0;//0:new 1:edit

	public OrderCrush() { }

	public OrderCrush(Long id, String quarter) {
		super(id);
		this.quarter = quarter;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getCreateRemark() {
		return createRemark;
	}

	public void setCreateRemark(String createRemark) {
		this.createRemark = createRemark;
	}

	public User getCloseBy() {
		return closeBy;
	}

	public void setCloseBy(User closeBy) {
		this.closeBy = closeBy;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getCloseRemark() {
		return closeRemark;
	}

	public void setCloseRemark(String closeRemark) {
		this.closeRemark = closeRemark;
	}

	public Integer getLastFlag() {
		return lastFlag;
	}

	public void setLastFlag(Integer lastFlag) {
		this.lastFlag = lastFlag;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public List<ServicePoint> getServicePoints() {
		return servicePoints;
	}

	public void setServicePoints(List<ServicePoint> servicePoints) {
		this.servicePoints = servicePoints;
	}

	public String getCrushNo() {
		return crushNo;
	}

	public void setCrushNo(String crushNo) {
		this.crushNo = crushNo;
	}

	public Date getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(Date createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	public Area getProvince() {
		return province;
	}

	public void setProvince(Area province) {
		this.province = province;
	}

	public Area getCity() {
		return city;
	}

	public void setCity(Area city) {
		this.city = city;
	}

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Area getSubArea() {
		return subArea;
	}

	public void setSubArea(Area subArea) {
		this.subArea = subArea;
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
}