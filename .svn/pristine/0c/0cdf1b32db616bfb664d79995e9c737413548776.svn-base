package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.mapper.adapters.DateAdapter;
import com.wolfking.jeesite.common.mapper.adapters.DateTimeAdapter;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Customer;
import com.wolfking.jeesite.modules.md.entity.ServicePoint;
import com.wolfking.jeesite.modules.md.utils.AreaSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.CustomerSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Area;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.adapter.UserSimpleAdapter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 投诉单
 * @author Ryan
 * @date 2018-01-27
 */
public class OrderComplain extends LongIDDataEntity<OrderComplain>
{

	private static final long serialVersionUID = 1L;

	public static final Integer STATUS_APPLIED = 0;
	public static final Integer STATUS_PROCESSING = 1; //处理中
	public static final Integer STATUS_CLOSED = 2; //已关闭
	public static final Integer STATUS_APPEAL = 3;//申诉
	public static final Integer STATUS_CANCEL = 4;//已撤销

	// Fields
	//@GsonIgnore
	@NotNull(message = "工单ID不能为空")
	private long orderId;
	@NotEmpty(message = "数据库分片不能为空")
	private String quarter = "";//数据库分片，与工单相同
	@GsonIgnore
	@NotNull(message = "工单号不能为空")
	private String orderNo = "";//工单号

	@NotNull(message = "产品类别不能为空")
	private Long productCategoryId;
	private String productCategoryName;

	//省
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area province;

	//市
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area city;

	//区/县
	@JsonAdapter(AreaSimpleAdapter.class)
	private Area area;

	//@NotNull(message = "投诉单号不能为空")
	private String complainNo;//投诉单号

	@GsonIgnore
	@NotNull(message = "厂商不能为空")
	@JsonAdapter(CustomerSimpleAdapter.class)
	private Customer customer;//厂商

	@NotNull(message = "投诉类型不能为空")
	@JsonAdapter(DictSimpleAdapter.class)
	private Dict complainType;//投诉类型

	@JsonAdapter(DictSimpleAdapter.class)
	private Dict status;//状态

	@NotEmpty(message = "用户姓名不能为空")
	private String userName;//用户姓名

	@NotEmpty(message = "用户电话不能为空")
	private String userPhone;

	@NotEmpty(message = "用户地址不能为空")
	private String userAddress;

	@GsonIgnore
	@NotNull(message = "客服不能为空")
	private User kefu;//客服

	@GsonIgnore
    private int complainObject = 0;//投诉对象，多个

	@NotNull(message = "投诉人不能为空")
	private String complainBy;//投诉人

	@JsonAdapter(DateAdapter.class)
	@NotNull(message = "投诉日期不能为空")
	private Date complainDate;//投诉日期

	private int complainItem=0;//投诉项目(多选)

	@NotEmpty(message = "投诉描述不能为空")
	@Length(max =500,message = "投诉描述不能超过500字")
	private String complainRemark = "";

	@GsonIgnore
	private int attachmentQty = 0;//附件数

	@GsonIgnore
	private List<OrderComplainAttachment> applyAttaches = Lists.newArrayList();//申请时的附件

	//判定
	@GsonIgnore
    private int judgeObject=0;//责任人，多个

	@JsonAdapter(UserSimpleAdapter.class)
	private User judgeBy;//判定人

	@JsonAdapter(DateTimeAdapter.class)
	private Date judgeDate;//判定日期

	@GsonIgnore
	private int judgeItem = 0;//判定项目

	private String judgeRemark = "";

	@GsonIgnore
	private int judgeAttachmentQty = 0;//附件数

	@GsonIgnore
	private List<OrderComplainAttachment> judgeAttaches = Lists.newArrayList();//判定时的附件

	//结案
	@GsonIgnore
	private int completeResult = 0;//结果

	private String completeRemark = "";//处理意见

	@JsonAdapter(UserSimpleAdapter.class)
	private User completeBy;//结案人

	@JsonAdapter(DateTimeAdapter.class)
	private Date completeDate;//结案日期

	@GsonIgnore
	private int completeAttachmentQty = 0;//附件数

	@GsonIgnore
	private List<OrderComplainAttachment> completeAttaches = Lists.newArrayList();//判定时的附件

	//赔偿
	@GsonIgnore
	private int compensateResult = 0;//赔偿处理

	@GsonIgnore
	private Double customerAmount = 0.0;

	@GsonIgnore
	private Double userAmount = 0.0;
	//罚款
	@GsonIgnore
	private int amerceResult = 0;//罚款处理
	@GsonIgnore
	private ServicePoint servicePoint = new ServicePoint(0l);// 网点
	@GsonIgnore
	private Double servicePointAmount = 0.0;//网点罚款
	@GsonIgnore
	private Double kefuAmount = 0.0;//客服罚款

	//以下是辅助字段
	@GsonIgnore
	private int action = 0;//0:new 1:edit
	@GsonIgnore
	private List<ServicePoint> servicePoints = Lists.newArrayList();
	@GsonIgnore
	@Size(min = 1,message = "请选择至少一个投诉对象")
	private List<String> complainObjectsIds = Lists.newArrayList();
	@GsonIgnore
	private List<Dict> complainObjects = Lists.newArrayList();
	private String complainObjectLabels = "";//显示

	@GsonIgnore
	@Size(min = 1, max = 2, message = "请选择至少一个投诉项目，最多两个投诉项目")
	private List<String> complainItemsIds = Lists.newArrayList();
	@GsonIgnore
	private List<Dict> complainItems = Lists.newArrayList();//用于前端显示
	private String complainItemLabels = "";

	@GsonIgnore
	private List<String> judgeObjectsIds = Lists.newArrayList();
	@GsonIgnore
	private List<Dict> judgeObjects = Lists.newArrayList();
	private String judgeObjectLabels = "";//显示
	@GsonIgnore
	private List<String> judgeItemsIds = Lists.newArrayList();
	@GsonIgnore
	private List<Dict> judgeItems = Lists.newArrayList();//用于前端显示
	private String judgeItemLabels = "";//显示
	@GsonIgnore
	private List<String> completeResultIds = Lists.newArrayList();//结案
	@GsonIgnore
	private List<Dict> completeResults = Lists.newArrayList();
	private String completeResultLabels = "";//显示
	@GsonIgnore
    private List<String> compensateObjects = Lists.newArrayList();//赔偿对象
	@GsonIgnore
    private List<String> amerceObjects = Lists.newArrayList();//罚款对象
	@GsonIgnore
	private List<String> compensateResultIds = Lists.newArrayList();//赔偿
	@GsonIgnore
	private List<String> amerceResultIds = Lists.newArrayList();//罚款

	private Date appointDate;//2018-06-27待跟进时间,新增时保存为创建时间，可以在后面修改设定

	@JsonAdapter(UserSimpleAdapter.class)
	private User appealBy;

	@JsonAdapter(DateAdapter.class)
	private Date appealDate;

	private String appealRemark="";

	//突击标识
	private Integer canRush = 0;

	public OrderComplain() { }

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

	public String getComplainNo() {
		return complainNo;
	}

	public void setComplainNo(String complainNo) {
		this.complainNo = complainNo;
	}

	public ServicePoint getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(ServicePoint servicePoint) {
		this.servicePoint = servicePoint;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Dict getComplainType() {
		return complainType;
	}

	public void setComplainType(Dict complainType) {
		this.complainType = complainType;
	}

	public Dict getStatus() {
		return status;
	}

	public void setStatus(Dict status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public User getKefu() {
		return kefu;
	}

	public void setKefu(User kefu) {
		this.kefu = kefu;
	}

	public String getComplainBy() {
		return complainBy;
	}

	public void setComplainBy(String complainBy) {
		this.complainBy = complainBy;
	}

	public Date getComplainDate() {
		return complainDate;
	}

	public void setComplainDate(Date complainDate) {
		this.complainDate = complainDate;
	}

	public int getComplainItem() {
		return complainItem;
	}

	public void setComplainItem(int complainItem) {
		this.complainItem = complainItem;
	}

	public List<Dict> getComplainItems() {
		return complainItems;
	}

	public void setComplainItems(List<Dict> complainItems) {
		this.complainItems = complainItems;
        if(complainItems != null && complainItems.size()>0){
            this.complainItemLabels =  complainItems.stream().map(t->t.getLabel()).collect(Collectors.joining(","));
        }else{
            this.complainItemLabels = "";
        }
	}

    public void setComplainItemLabels(String complainItemLabels) {
        this.complainItemLabels = complainItemLabels;
    }

    public String getComplainItemLabels() {
	    return this.complainItemLabels;
    }

	public String getComplainRemark() {
		return complainRemark;
	}

	public void setComplainRemark(String complainRemark) {
		this.complainRemark = complainRemark;
	}

	public List<OrderComplainAttachment> getApplyAttaches() {
		return applyAttaches;
	}

	public void setApplyAttaches(List<OrderComplainAttachment> applyAttaches) {
		this.applyAttaches = applyAttaches;
	}

	public User getJudgeBy() {
		return judgeBy;
	}

	public void setJudgeBy(User judgeBy) {
		this.judgeBy = judgeBy;
	}

	public int getJudgeItem() {
		return judgeItem;
	}

	public void setJudgeItem(int judgeItem) {
		this.judgeItem = judgeItem;
	}

	public List<Dict> getJudgeItems() {
		return judgeItems;
	}

	public void setJudgeItems(List<Dict> judgeItems) {
		this.judgeItems = judgeItems;
        if (judgeItems != null && judgeItems.size() > 0) {
            this.judgeItemLabels = judgeItemLabels = this.judgeItems.stream().map(t -> t.getLabel()).collect(Collectors.joining(","));
        } else {
            this.judgeItemLabels = "";
        }
	}

    public String getJudgeItemLabels() {
	    return this.judgeItemLabels;
    }

	public String getJudgeRemark() {
		return judgeRemark;
	}

	public void setJudgeRemark(String judgeRemark) {
		this.judgeRemark = judgeRemark;
	}

	public List<OrderComplainAttachment> getJudgeAttaches() {
		return judgeAttaches;
	}

	public void setJudgeAttaches(List<OrderComplainAttachment> judgeAttaches) {
		this.judgeAttaches = judgeAttaches;
	}

	public int getCompleteResult() {
		return completeResult;
	}

	public void setCompleteResult(int completeResult) {
		this.completeResult = completeResult;
	}

	public String getCompleteRemark() {
		return completeRemark;
	}

	public void setCompleteRemark(String completeRemark) {
		this.completeRemark = completeRemark;
	}

	public User getCompleteBy() {
		return completeBy;
	}

	public void setCompleteBy(User completeBy) {
		this.completeBy = completeBy;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public List<OrderComplainAttachment> getCompleteAttaches() {
		return completeAttaches;
	}

	public void setCompleteAttaches(List<OrderComplainAttachment> completeAttaches) {
		this.completeAttaches = completeAttaches;
	}

	public Double getCustomerAmount() {
		return customerAmount;
	}

	public void setCustomerAmount(Double customerAmount) {
		this.customerAmount = customerAmount;
	}

	public Double getUserAmount() {
		return userAmount;
	}

	public void setUserAmount(Double userAmount) {
		this.userAmount = userAmount;
	}

	public Double getServicePointAmount() {
		return servicePointAmount;
	}

	public void setServicePointAmount(Double servicePointAmount) {
		this.servicePointAmount = servicePointAmount;
	}

	public Double getKefuAmount() {
		return kefuAmount;
	}

	public void setKefuAmount(Double kefuAmount) {
		this.kefuAmount = kefuAmount;
	}

	public int getAttachmentQty() {
		return attachmentQty;
	}

	public void setAttachmentQty(int attachmentQty) {
		this.attachmentQty = attachmentQty;
	}

	public int getJudgeAttachmentQty() {
		return judgeAttachmentQty;
	}

	public void setJudgeAttachmentQty(int judgeAttachmentQty) {
		this.judgeAttachmentQty = judgeAttachmentQty;
	}

	public int getCompleteAttachmentQty() {
		return completeAttachmentQty;
	}

	public void setCompleteAttachmentQty(int completeAttachmentQty) {
		this.completeAttachmentQty = completeAttachmentQty;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Date getJudgeDate() {
		return judgeDate;
	}

	public void setJudgeDate(Date judgeDate) {
		this.judgeDate = judgeDate;
	}

    public int getComplainObject() {
        return complainObject;
    }

    public void setComplainObject(int complainObject) {
        this.complainObject = complainObject;
    }

    public List<Dict> getComplainObjects() {
        return complainObjects;
    }

    public void setComplainObjects(List<Dict> complainObjects) {
        this.complainObjects = complainObjects;
        if(complainObjects != null && complainObjects.size()>0){
            this.complainObjectLabels = complainObjects.stream().map(t->t.getLabel()).collect(Collectors.joining(","));
        }else{
            this.complainObjectLabels = "";
        }
    }

    public void setComplainObjectLabels(String complainObjectLabels) {
        this.complainObjectLabels = complainObjectLabels;
    }

    public String getComplainObjectLabels() {
        return complainObjectLabels;
    }

    public int getJudgeObject() {
        return judgeObject;
    }

    public void setJudgeObject(int judgeObject) {
        this.judgeObject = judgeObject;
    }

    public List<Dict> getJudgeObjects() {
        return judgeObjects;
    }

    public void setJudgeObjects(List<Dict> judgeObjects) {
        this.judgeObjects = judgeObjects;
        if(judgeObjects != null && judgeObjects.size()>0){
            this.judgeObjectLabels =judgeObjects.stream().map(t->t.getLabel()).collect(Collectors.joining(","));
        }else{
         this.judgeObjectLabels = "";
        }
    }

    public void setJudgeObjectLabels(String judgeObjectLabels) {
        this.judgeObjectLabels = judgeObjectLabels;
    }

    public String getJudgeObjectLabels() {
	    return this.judgeObjectLabels;
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

	public List<String> getComplainObjectsIds() {
		return complainObjectsIds;
	}

	public void setComplainObjectsIds(List<String> complainObjectsIds) {
		this.complainObjectsIds = complainObjectsIds;
	}

	public List<String> getComplainItemsIds() {
		return complainItemsIds;
	}

	public void setComplainItemsIds(List<String> complainItemsIds) {
		this.complainItemsIds = complainItemsIds;
	}

	public List<String> getJudgeObjectsIds() {
		return judgeObjectsIds;
	}

	public void setJudgeObjectsIds(List<String> judgeObjectsIds) {
		this.judgeObjectsIds = judgeObjectsIds;
	}

	public List<String> getJudgeItemsIds() {
		return judgeItemsIds;
	}

	public void setJudgeItemsIds(List<String> judgeItemsIds) {
		this.judgeItemsIds = judgeItemsIds;
	}


	public List<String> getCompleteResultIds() {
		return completeResultIds;
	}

	public void setCompleteResultIds(List<String> completeResultIds) {
		this.completeResultIds = completeResultIds;
	}

	public List<Dict> getCompleteResults() {
		return completeResults;
	}

	public void setCompleteResults(List<Dict> completeResults) {
		this.completeResults = completeResults;
        if(completeResults != null && completeResults.size()>0){
            this.completeResultLabels = completeResults.stream().map(t->t.getLabel()).collect(Collectors.joining(","));
        }else{
            this.completeResultLabels = "";
        }
	}

    public void setCompleteResultLabels(String completeResultLabels) {
        this.completeResultLabels = completeResultLabels;
    }

    public String getCompleteResultLabels() {
	    return  this.completeResultLabels;
    }

    public List<String> getCompensateObjects() {
        return compensateObjects;
    }

    public void setCompensateObjects(List<String> compensateObjects) {
        this.compensateObjects = compensateObjects;
    }

    public List<String> getAmerceObjects() {
        return amerceObjects;
    }

    public void setAmerceObjects(List<String> amerceObjects) {
        this.amerceObjects = amerceObjects;
    }

    public int getCompensateResult() {
        return compensateResult;
    }

    public void setCompensateResult(int compensateResult) {
        this.compensateResult = compensateResult;
    }

    public int getAmerceResult() {
        return amerceResult;
    }

    public void setAmerceResult(int amerceResult) {
        this.amerceResult = amerceResult;
    }

	public List<String> getCompensateResultIds() {
		return compensateResultIds;
	}

	public void setCompensateResultIds(List<String> compensateResultIds) {
		this.compensateResultIds = compensateResultIds;
	}

	public List<String> getAmerceResultIds() {
		return amerceResultIds;
	}

	public void setAmerceResultIds(List<String> amerceResultIds) {
		this.amerceResultIds = amerceResultIds;
	}

	public User getAppealBy() {
		return appealBy;
	}

	public void setAppealBy(User appealBy) {
		this.appealBy = appealBy;
	}

	public Date getAppealDate() {
		return appealDate;
	}

	public void setAppealDate(Date appealDate) {
		this.appealDate = appealDate;
	}

	public String getAppealRemark() {
		return appealRemark;
	}

	public void setAppealRemark(String appealRemark) {
		this.appealRemark = appealRemark;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Date getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(Date appointDate) {
		this.appointDate = appointDate;
	}

	public long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
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

	public Integer getCanRush() {
		return canRush;
	}

	public void setCanRush(Integer canRush) {
		this.canRush = canRush;
	}
}