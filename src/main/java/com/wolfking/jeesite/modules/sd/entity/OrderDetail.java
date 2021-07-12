package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.md.MDActionCode;
import com.kkl.kklplus.entity.md.MDErrorCode;
import com.kkl.kklplus.entity.md.MDErrorType;
import com.kkl.kklplus.entity.md.dto.MDActionCodeDto;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderDetailAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 订单实际服务记录
 * 
 */
@JsonAdapter(OrderDetailAdapter.class)
public class OrderDetail extends LongIDDataEntity<OrderDetail>
{

	private static final long serialVersionUID = 1L;

	// Fields
	@GsonIgnore
	//private Order order;
	private Long orderId;
	private String quarter = "";//数据库分片，与订单相同
	private int itemNo = 10;// 项次
	private int serviceTimes = 0;// 第几次上门服务
	private Product product;// 产品
	private String brand = "";// 品牌
	private String productSpec = "";// 型号/规格
	private ServiceType serviceType;// 服务类型
	private int qty = 1;// 数量
	private int engineerUsePrice = 0; //网点使用哪个定价,0:优惠价 1:标准价

	//customer
	private Double standPrice = 0.00;// 服务类型标准价
	private Double discountPrice = 0.00;// 服务类型优惠价
	private Double charge = 0.00;// 服务费(应收)
	private Double materialCharge = 0.00;// 配件費用(应收)
	private Double expressCharge = 0.00;// 快递费(应收）
	private Double travelCharge = 0.00;// 远程費用(应收)
	private Double otherCharge = 0.00;// 其他費用(应收)
	private Double customerCharge = 0.00;//客户应付=charge+materialCharge+expressCharge+travelCharge+otherCharge

	//engineer
	private Double engineerStandPrice = 0.00;// 安维标准价
	private Double engineerDiscountPrice = 0.00;// 安维优惠价
	private ServicePoint servicePoint; //安维网点,servicepoint_id,对应原来 engineer:安维人(主账号)
	private Engineer engineer;// 安维人员,安维主账号派单给安维 (engineer_id,原来的sub engineer)
	private Dict engineerPaymentType;// 结算方式
	private Double engineerServiceCharge = 0.00;// 安维服务费(应付)
	private Double engineerTravelCharge = 0.00;// 安维远程费(应付)
	private String travelNo = "";// 远程费审核单号
	private Double engineerExpressCharge = 0.00;// 快递费（应付）
	private Double engineerMaterialCharge = 0.00;// 安维配件费(应付)
	private Double engineerOtherCharge = 0.00;// 安维其它费用(应付)
	//安维应收= engineerServiceCharge+engineerTravelCharge+engineerExpressCharge+engineerMaterialCharge+engineerOtherCharge
	//private Double engineerChage = 0.00;

	// 安维合计(应付) = 安维远程费(应付) + 安维远程费(应付) + 安维配件费(应付) + 安维其它费用(应付)
	private Double engineerTotalCharge = 0.00;

	private Date engineerInvoiceDate;// 安维付款时间
	private int seqNo = 0;// 同次上门排序，按价格由高到低
	private Integer syncChargeTags = 0; // 2020-09-09 自动同步加的应收费用标记，参考OrderUtils.SyncCustomerCharge

	//region 故障维修
	// 服务类型
	private Dict serviceCategory;
	// 故障分类
	private MDErrorType errorType;
	// 故障现象
	private MDErrorCode errorCode;
	// 故障分析&处理
	private MDActionCode actionCode;
	// 其他故障说明
	private String otherActionRemark;
	// 故障内容(以上几项的文本格式信息)
	private String errorContent;
	// 是否有故障分类
	private int hasErrorType;
	//endregion

	private List<Material> materialList = Lists.newArrayList();

	//辅助字段
	private int orderServiceTimes;// 订单已上门服务次数
	//添加类型，0：正常上门服务（app或安维添加）
	// 1：异常处理添加
	private int addType = 0;
	//订单历史json
	@GsonIgnore
	private Date chargeDate;//对账日期
	@GsonIgnore
	private Date customerInvoiceDate;//结账日期(厂商)
	@GsonIgnore
	private String engineerInvoiceRemarks = "";//付款描述
	@GsonIgnore
	private Double engineerWriteOffCharge = 0.0;//安维退补总金额

	// Constructors
	public OrderDetail()
	{
		super();
	}

	public OrderDetail(Long orderId)
	{
		super();
		this.orderId = orderId;
		this.itemNo = 10;
	}

	public OrderDetail(Long orderId,Integer itemNo)
	{
		super();
		this.orderId = orderId;
		this.itemNo = 10;
	}

	/**
	 * 用于stream汇总网点费用
	 *
	 * @param engineerServiceCharge 	安维服务费(应付)
	 * @param engineerTravelCharge		安维远程费(应付)
	 * @param engineerExpressCharge		快递费（应付）
	 * @param engineerMaterialCharge	安维配件费(应付)
	 * @param engineerOtherCharge		安维其它费用(应付)
	 */
	public OrderDetail(Double engineerServiceCharge,Double engineerTravelCharge,Double engineerExpressCharge,Double engineerMaterialCharge,Double engineerOtherCharge)
	{
		this.engineerServiceCharge = engineerServiceCharge;// 安维服务费(应付)
		this.engineerTravelCharge = engineerTravelCharge;// 安维远程费(应付)
		this.engineerExpressCharge = engineerExpressCharge;// 快递费（应付）
		this.engineerMaterialCharge = engineerMaterialCharge;// 安维配件费(应付)
		this.engineerOtherCharge = engineerOtherCharge;// 安维其它费用(应付)
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	@Min(value = 0,message = "上门次数不能小于0")
	public int getServiceTimes() {
		return serviceTimes;
	}

	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	@NotNull(message = "产品不能为空")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Length(max = 20,message = "品牌长度不能超过10个汉字")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Length(max = 40,message = "规格长度不能超过20个汉字")
	public String getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}

	@NotNull(message = "服务项目不能为空")
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Min(value = 1,message = "数量不能小于1")
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@DecimalMin(value = "0.0",message = "标准价不能小于0")
	public Double getStandPrice() {
		return standPrice;
	}

	public void setStandPrice(Double standPrice) {
		this.standPrice = standPrice;
	}

	@DecimalMin(value = "0.0",message = "折扣价不能小于0")
	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	@DecimalMin(value = "0.0",message = "金额不能小于0")
	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	@DecimalMin(value = "0.0",message = "厂商配件费用不能小于0")
	public Double getMaterialCharge() {
		return materialCharge;
	}

	public void setMaterialCharge(Double materialCharge) {
		this.materialCharge = materialCharge;
	}

	@DecimalMin(value = "0.0",message = "快递费不能小于0")
	public Double getExpressCharge() {
		return expressCharge;
	}

	public void setExpressCharge(Double expressCharge) {
		this.expressCharge = expressCharge;
	}

	@DecimalMin(value = "0.0",message = "远程费不能小于0")
	public Double getTravelCharge() {
		return travelCharge;
	}

	public void setTravelCharge(Double travelCharge) {
		this.travelCharge = travelCharge;
	}

	@DecimalMin(value = "0.0",message = "其他费用不能小于0")
	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	@DecimalMin(value = "0.0",message = "安维标准价不能小于0")
	public Double getEngineerStandPrice() {
		return engineerStandPrice;
	}

	public void setEngineerStandPrice(Double engineerStandPrice) {
		this.engineerStandPrice = engineerStandPrice;
	}

	@DecimalMin(value = "0.0",message = "安维折扣价不能小于0")
	public Double getEngineerDiscountPrice() {
		return engineerDiscountPrice;
	}

	public void setEngineerDiscountPrice(Double engineerDiscountPrice) {
		this.engineerDiscountPrice = engineerDiscountPrice;
	}

	@NotNull(message = "服务网点不能为空")
	public ServicePoint getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(ServicePoint servicePoint) {
		this.servicePoint = servicePoint;
	}

	@NotNull(message = "安维不能为空")
	public Engineer getEngineer() {
		return engineer;
	}

	public void setEngineer(Engineer engineer) {
		this.engineer = engineer;
	}

//	@NotNull(message = "安维支付方式不能为空")
	public Dict getEngineerPaymentType() {
		return engineerPaymentType;
	}

	public void setEngineerPaymentType(Dict engineerPaymentType) {
		this.engineerPaymentType = engineerPaymentType;
	}

	@DecimalMin(value = "0.0",message = "安维服务费不能小于0")
	public Double getEngineerServiceCharge() {
		return engineerServiceCharge;
	}

	public void setEngineerServiceCharge(Double engineerServiceCharge) {
		this.engineerServiceCharge = engineerServiceCharge;
	}

	@DecimalMin(value = "0.0",message = "安维远程费不能小于0")
	public Double getEngineerTravelCharge() {
		return engineerTravelCharge;
	}

	public void setEngineerTravelCharge(Double engineerTravelCharge) {
		this.engineerTravelCharge = engineerTravelCharge;
	}

	public String getTravelNo() {
		return travelNo;
	}

	public void setTravelNo(String travelNo) {
		this.travelNo = travelNo;
	}

	@DecimalMin(value = "0.0",message = "安维快递费不能小于0")
	public Double getEngineerExpressCharge() {
		return engineerExpressCharge;
	}

	public void setEngineerExpressCharge(Double engineerExpressCharge) {
		this.engineerExpressCharge = engineerExpressCharge;
	}

	@DecimalMin(value = "0.0",message = "安维配件费不能小于0")
	public Double getEngineerMaterialCharge() {
		return engineerMaterialCharge;
	}

	public void setEngineerMaterialCharge(Double engineerMaterialCharge) {
		this.engineerMaterialCharge = engineerMaterialCharge;
	}

	@DecimalMin(value = "0.0",message = "安维其它费用不能小于0")
	public Double getEngineerOtherCharge() {
		return engineerOtherCharge;
	}

	public void setEngineerOtherCharge(Double engineerOtherCharge) {
		this.engineerOtherCharge = engineerOtherCharge;
	}

	@DecimalMin(value = "0.0",message = "安维合计金额不能小于0")
	public Double getEngineerTotalCharge() {
		return engineerTotalCharge;
	}

	public void setEngineerTotalCharge(Double engineerTotalCharge) {
		this.engineerTotalCharge = engineerTotalCharge;
	}

	public Date getEngineerInvoiceDate() {
		return engineerInvoiceDate;
	}

	public void setEngineerInvoiceDate(Date engineerInvoiceDate) {
		this.engineerInvoiceDate = engineerInvoiceDate;
	}

	public int getOrderServiceTimes() {
		return orderServiceTimes;
	}

	public void setOrderServiceTimes(int orderServiceTimes) {
		this.orderServiceTimes = orderServiceTimes;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public List<Material> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<Material> materialList) {
		this.materialList = materialList;
	}

	public Double getCustomerCharge() {
		return charge + materialCharge + expressCharge + travelCharge + otherCharge;
	}

	public Double getEngineerChage() {
		return engineerServiceCharge + engineerMaterialCharge + engineerExpressCharge + engineerTravelCharge + engineerOtherCharge;
	}

	public Long getProductId(){return this.product.getId();}

	public int getAddType() {
		return addType;
	}

	public void setAddType(int addType) {
		this.addType = addType;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}


    public Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    public Date getCustomerInvoiceDate() {
        return customerInvoiceDate;
    }

    public void setCustomerInvoiceDate(Date customerInvoiceDate) {
        this.customerInvoiceDate = customerInvoiceDate;
    }

    public String getEngineerInvoiceRemarks() {
        return engineerInvoiceRemarks;
    }

    public void setEngineerInvoiceRemarks(String engineerInvoiceRemarks) {
        this.engineerInvoiceRemarks = engineerInvoiceRemarks;
    }

	public Double getEngineerWriteOffCharge() {
		return engineerWriteOffCharge;
	}

	public void setEngineerWriteOffCharge(Double engineerWriteOffCharge) {
		this.engineerWriteOffCharge = engineerWriteOffCharge;
	}

	public int getEngineerUsePrice() {
		return engineerUsePrice;
	}

	public void setEngineerUsePrice(int engineerUsePrice) {
		this.engineerUsePrice = engineerUsePrice;
	}

	public void setCustomerCharge(Double customerCharge) {
		this.customerCharge = customerCharge;
	}

	public Dict getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(Dict serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public MDErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(MDErrorType errorType) {
		this.errorType = errorType;
	}

	public MDErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(MDErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public MDActionCode getActionCode() {
		return actionCode;
	}

	public void setActionCode(MDActionCode actionCode) {
		this.actionCode = actionCode;
	}

	public String getOtherActionRemark() {
		return otherActionRemark;
	}

	public void setOtherActionRemark(String otherActionRemark) {
		this.otherActionRemark = otherActionRemark;
	}

	public int getHasErrorType() {
		return hasErrorType;
	}

	public void setHasErrorType(int hasErrorType) {
		this.hasErrorType = hasErrorType;
	}

	public Integer getSyncChargeTags() {
		return syncChargeTags;
	}

	public void setSyncChargeTags(Integer syncChargeTags) {
		this.syncChargeTags = syncChargeTags;
	}

	public String getErrorContent() {
		if(StringUtils.isNotBlank(this.errorContent)){
			return this.errorContent;
		}
		StringBuilder content = new StringBuilder();
		if(this.errorType == null || this.errorType.getId() == null || this.errorType.getId() <=0){
			if(StringUtils.isNotBlank(this.otherActionRemark)){
				content.append(this.serviceCategory.getIntValue()>1?"其他故障:":"安装说明:")
						.append(this.otherActionRemark.trim());
				this.errorContent = content.toString();
				content.setLength(0);
				//this.errorContent = this.serviceCategory.getIntValue()>1?"其他故障:":"安装说明:" + this.otherActionRemark;
				return errorContent;
			}
			return StringUtils.EMPTY;
		}
		content.append("故障分类: ").append(this.errorType.getName()).append("<br/>")
				.append("故障现象: ").append(this.errorCode.getName()).append("<br/>")
				.append("故障处理: ").append(this.actionCode.getName()).append("<br/>")
				.append("其他故障: ").append(this.otherActionRemark==null?"":this.otherActionRemark);
		this.errorContent = content.toString();
		content.setLength(0);
		return errorContent;
	}
}