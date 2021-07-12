package com.wolfking.jeesite.modules.sd.entity;


import com.google.gson.annotations.JsonAdapter;
import com.kkl.kklplus.entity.common.NameValuePair;
import com.wolfking.jeesite.common.config.redis.GsonIgnore;
import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.md.entity.ServiceType;
import com.wolfking.jeesite.modules.md.utils.ProductSimpleAdapter;
import com.wolfking.jeesite.modules.md.utils.ServiceTypeSimpleAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.adapter.DictSimpleAdapter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单明细
 */
public class OrderItem extends LongIDDataEntity<OrderItem> {

	private static final long serialVersionUID = 1L;
	
	// Fields
	@GsonIgnore
	private Long orderId;
	private String quarter = "";//数据库分片，与订单相同
	private Integer itemNo = 10;//订单项次
	@JsonAdapter(ProductSimpleAdapter.class)
	private Product product;//产品
	private String brand = "";//品牌
	private String productSpec = "";//型号/规格
	@JsonAdapter(ServiceTypeSimpleAdapter.class)
	private ServiceType serviceType;//服务类型
	private Double standPrice = 0.00;//服务类型标准价
	private Double discountPrice = 0.00;//服务类型优惠价
	private Integer qty =1;//数量
	private Double charge = 0.00;//金额
	private Double blockedCharge = 0.00;//冻结金额
	@JsonAdapter(DictSimpleAdapter.class)
	private Dict expressCompany = new Dict("");
	private String expressNo = "";

	//辅助字段
	/**
	 * B2B产品编码
	 */
	private String b2bProductCode = ""; /* orderItem增加B2B产品编码 */

	//灯饰下单 2020-03-18
	//产品分类
	private NameValuePair<Long,String> productType = new NameValuePair<>();
	//产品二级分类
	private NameValuePair<Long,String> productTypeItem = new NameValuePair<>();
	//产品图片
	private List<String> pics;

	// Constructors
	public OrderItem() {
		super();
	}

	
	public OrderItem(Long id) {
		super();
		this.id = id;
	}
	
	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
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

	@NotNull(message = "服务类型不能为空")
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
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

	@Min(value = 1,message = "数量不能小于1")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@DecimalMin(value = "0.0",message = "金额不能小于0")
	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
	}

	@DecimalMin(value = "0.0",message = "冻结金额不能小于0")
	public Double getBlockedCharge() {
		return blockedCharge;
	}

	public void setBlockedCharge(Double blockedCharge) {
		this.blockedCharge = blockedCharge;
	}

//	@Length(max = 20,message = "快递公司长度不能超过20")
	public Dict getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(Dict expressCompany) {
		this.expressCompany = expressCompany;
	}

	@Length(max = 20,message = "快递单号长度不能超过20")
	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}


	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId(){return this.product.getId();}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getB2bProductCode() {
		return b2bProductCode;
	}

	public void setB2bProductCode(String b2bProductCode) {
		this.b2bProductCode = b2bProductCode;
	}

	public NameValuePair<Long, String> getProductType() {
		return productType;
	}

	public void setProductType(NameValuePair<Long, String> productType) {
		this.productType = productType;
	}

	public NameValuePair<Long, String> getProductTypeItem() {
		return productTypeItem;
	}

	public void setProductTypeItem(NameValuePair<Long, String> productTypeItem) {
		this.productTypeItem = productTypeItem;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}

	public String getProductTypeName(){
		return String.format(this.productType.getValue(),this.productTypeItem.getValue());
	}
}